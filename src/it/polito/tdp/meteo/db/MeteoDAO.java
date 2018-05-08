package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		List<Rilevamento> rilevamentiLocalitaMese = new ArrayList<Rilevamento>();
		for(Rilevamento r: this.getAllRilevamenti()) {
			if(r.getLocalita().equals(localita) && r.getData().getMonth() == mese-1)
				rilevamentiLocalitaMese.add(r);
		}

		return rilevamentiLocalitaMese;
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		
		double avg = 0.0;
		int somma = 0;
		int counter = 0;
		
		for(Rilevamento r: this.getAllRilevamentiLocalitaMese(mese, localita)) {
			somma += r.getUmidita();
			counter++;
		}

		return (double) somma/counter;
	}
	
	public List<Citta> getTutteLeCitta() {
		
		final String sql = "SELECT  distinct Localita FROM situazione";
		
		List<Citta> città = new ArrayList<Citta>();
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Citta c = new Citta(rs.getString("Localita"));
				città.add(c);
			}
			
			conn.close();
			return città;
			
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		
	}

}
