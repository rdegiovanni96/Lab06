package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {
	
	private Model model = new Model();
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<Integer> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		
		txtResult.clear();
		
		int mese = boxMese.getValue();
		String result = model.trovaSequenza(mese);
		txtResult.appendText(result);
		
	}

	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		
		txtResult.clear();
		
		int mese = boxMese.getValue();
		String result = model.getUmiditaMedia(mese);
		txtResult.appendText(result);
		

	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
		System.out.println(boxMese);
		boxMese.getItems().addAll(model.getTuttiIMesi());
		this.setModel(model);
	}

}
