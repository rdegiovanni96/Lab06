package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO mdao = new MeteoDAO();
	private List<Citta> listaCitta = null;
	private double bestScore;
	private List<SimpleCity> soluzione;
	
	public Model() {
		listaCitta=mdao.getTutteLeCitta();
		for(Rilevamento r: mdao.getAllRilevamenti()) {
			listaCitta.get(listaCitta.indexOf(new Citta(r.getLocalita()))).getRilevamenti().add(r)	;
		}
	}

	public String getUmiditaMedia(int mese) {
		
		String result = "";
		
		/*listaCitta=mdao.getTutteLeCitta();
		for(Rilevamento r: mdao.getAllRilevamenti()) {
			listaCitta.get(listaCitta.indexOf(new Citta(r.getLocalita()))).addRilevamento(r);	;
		}*/
		
		
		
		for(Citta c: this.listaCitta) {
			//double avg
			//System.out.println(c.getRilevamenti().toString());
			//for(Rilevamento r : c.getRilevamenti()) {
				//System.out.println("Prova");
			
			result += c.getNome()+" "+mdao.getAvgRilevamentiLocalitaMese(mese, c.getNome())+"\n";
			//}
		}
		
		result = result.trim();
		System.out.println(result);

		return result;
	}

	public String trovaSequenza(int mese) {
		//this.mese = mese;
		String result = "";
		soluzione = new ArrayList<SimpleCity>();
		bestScore = 100000;
		
		List<Citta> listaCitta = mdao.getTutteLeCitta();
		
		List<SimpleCity> parziale = new ArrayList<SimpleCity>();
		int livello = 0;
		
		recursive(livello, parziale, mese, listaCitta);
		
		for(SimpleCity sc: soluzione) {
			result += sc.getNome()+"\n";
		}
		
		result = result.trim();
		
		
		return result;
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata, int mese) {
		
		
		double score = 0.0;
		
		
		for(int i=0; i<soluzioneCandidata.size();i++) {
			score += mdao.getAllRilevamentiLocalitaMese(mese, soluzioneCandidata.get(i).getNome()).get(i).getUmidita();
			if(i>0) {
				if(!(soluzioneCandidata.get(i).equals(soluzioneCandidata.get(i-1))))
					score += this.COST;
			}
		}

		
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale, Citta c) {
		
		if(c.getCounter() >= this.NUMERO_GIORNI_CITTA_MAX)
			return false;
		if(parziale.size()==0)
			return true;
		if(parziale.get(parziale.size()-1).getNome().compareTo(c.getNome()) != 0) {
			if(parziale.size()==1 || parziale.size()==2)
				return false;
			if(parziale.get(parziale.size()-1).getNome().compareTo(parziale.get(parziale.size()-2).getNome()) != 0 || parziale.get(parziale.size()-1).getNome().compareTo(parziale.get(parziale.size()-3).getNome()) != 0)
				return false;
		}

		return true;
	}
	
	public List<Integer> getTuttiIMesi() {
		
		List<Integer> mesi = new ArrayList<Integer>();
		for(int i=0;i<12;i++) {
			mesi.add(i+1);
		}
		
		return mesi;
		
	}
	
	private void recursive(int livello, List<SimpleCity> parziale, int mese, List<Citta> listaCitta) {
		
		// condizione di terminazione
		if(livello==this.NUMERO_GIORNI_TOTALI) {
			if(this.punteggioSoluzione(parziale, mese) < bestScore) {
				bestScore = this.punteggioSoluzione(parziale, mese);
				soluzione = new ArrayList<>(parziale);
				System.out.println("Prova 1");
			}
			return;
		}
		
		System.out.println("Prova 2");
		
		for(Citta c: listaCitta) {
			
			if(this.controllaParziale(parziale, c)) {
			SimpleCity sc = new SimpleCity(c.getNome());
			parziale.add(sc);
			c.increaseCounter();
			recursive(livello+1,parziale,mese,listaCitta);
			parziale.remove(sc);
			c.diminuisciCounter();
			}
			}
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	


