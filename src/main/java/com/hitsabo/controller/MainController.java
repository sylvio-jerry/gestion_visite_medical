package com.hitsabo.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController implements Initializable{

	private Parent fxml; 
	Stage stage; 
	
	@FXML
    private AnchorPane app, content;
	
	@FXML
    private Pane nav_accueil, nav_medecin,nav_patient,nav_visiter;
	
	@FXML
    private Label current_date, pseudo, heure;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.openAccueilWindow(null);
		this.setCurrentDate();
	}

	public void setCurrentDate() {
		
		LocalDateTime currdate = LocalDateTime.now();
		DateTimeFormatter currdateformat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String formattedDate = currdate.format(currdateformat);
		current_date.setText(formattedDate);
		
	}


	@FXML
    void openMedecinWindow(MouseEvent event) {
    	try {
    		this.fxml = FXMLLoader.load(getClass().getResource("/views/Medecin.fxml"));
    		content.getChildren().removeAll();
    		content.getChildren().setAll(this.fxml);


    		Pane[] panes = {nav_accueil, nav_patient, nav_visiter};
    		this.activePane(nav_medecin);
    		this.deactivePane(panes);

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@FXML
	void openAccueilWindow(MouseEvent event) {
		try {
			this.fxml = FXMLLoader.load(getClass().getResource("/views/Accueil.fxml"));
			content.getChildren().removeAll();
			content.getChildren().setAll(this.fxml);

			Pane[] panes = {nav_medecin, nav_patient, nav_visiter};
			this.activePane(nav_accueil);
			this.deactivePane(panes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openPatientWindow(MouseEvent event) {
		try {
			this.fxml = FXMLLoader.load(getClass().getResource("/views/Patient.fxml"));
			content.getChildren().removeAll();
			content.getChildren().setAll(this.fxml);


			Pane[] panes = {nav_accueil, nav_medecin, nav_visiter};
			this.activePane(nav_patient);
			this.deactivePane(panes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void openVisiterWindow(MouseEvent event) {
		try {
			this.fxml = FXMLLoader.load(getClass().getResource("/views/Visiter.fxml"));
			content.getChildren().removeAll();
			content.getChildren().setAll(this.fxml);


			Pane[] panes = {nav_accueil, nav_medecin, nav_patient};
			this.activePane(nav_visiter);
			this.deactivePane(panes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    
    void activePane(Pane pane) {
    	pane.pseudoClassStateChanged(PseudoClass.getPseudoClass("active"), true);
    }
    
    void deactivePane(Pane[] pane) {
    	for (Pane pane2 : pane) {
    		pane2.pseudoClassStateChanged(PseudoClass.getPseudoClass("active"), false);
		}
    }

	@FXML
	void onExitApp(MouseEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quitter");
		alert.setHeaderText("Arrêt d'application");
		alert.setContentText("Voulez-vous vraiment quitter l'application ?");

		if(alert.showAndWait().get() == ButtonType.OK) {
			stage = (Stage) app.getScene().getWindow();
			stage.close();
		}
	}
 
}
