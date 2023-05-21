package com.hitsabo.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
//import java.util.Date;

import com.hitsabo.dao.MedecinDAO;
import com.hitsabo.entity.Medecin;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
//import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


import static javafx.animation.Animation.*;

public class MedecinController implements Initializable{
   @FXML
    private TextField txt_codemed,txt_nom,txt_prenom,recherche_medecin;

    
    @FXML
    private ChoiceBox<Integer> combo_box_affiche;
    
    @FXML
    private ChoiceBox<String> combo_box_grade;
    
    @FXML
    private Button btn_ajouter,btn_modifier,btn_supprimer,btn_reinitialiser,btn_annuler;

	@FXML
    private TableView<Medecin> tableMedecin;

	@FXML
	private TableColumn<Medecin, Integer> codemedTv;

	@FXML
	private TableColumn<Medecin, String> gradeTv;

	@FXML
	private TableColumn<Medecin, String> nomTv;

	@FXML
	private TableColumn<Medecin, String> prenomTv;

	@FXML
	private ImageView medecinImage;

	private Integer[] number = {25, 50, 100, 250, 500};
	@FXML
	private String[] grade= {"Spécialiste","Généraliste","Professeur"};

	private ObservableList medecinList;
	private final MedecinDAO medecinDAO;

	public MedecinController() {
		medecinDAO = new MedecinDAO();
	}


	// A N I M A T I O N +++++++++++++++++++++++++++++++++++

	public void animateImage(ImageView imageView){
		RotateTransition rotate = new RotateTransition();
		rotate.setNode(imageView);
		rotate.setDuration(Duration.millis(12000));
		rotate.setCycleCount(TranslateTransition.INDEFINITE);
		rotate.setInterpolator(Interpolator.LINEAR);
		rotate.setByAngle(360);
		rotate.setAxis(Rotate.Y_AXIS);
		rotate.play();
	}

	//F I N A N I M A T I O N +++++++++++++++++++++++++++++++++++

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.startup();
	}
	
	public void startup() {
		loadMedecins();
		this.showBtnAddEditDeleteResetSkip(true, false, false, true,false);
		this.loadComboBox();
		this.initializeTextField();
		this.animateImage(medecinImage);
	}
	private void loadMedecins() {
		ObservableList<Medecin> medecinList = FXCollections.observableArrayList(medecinDAO.getAllMedecin());
		codemedTv.setCellValueFactory(new PropertyValueFactory<Medecin, Integer>("codemed"));
		nomTv.setCellValueFactory(new PropertyValueFactory<Medecin, String>("nom"));
		prenomTv.setCellValueFactory(new PropertyValueFactory<Medecin, String>("prenom"));
		gradeTv.setCellValueFactory(new PropertyValueFactory<Medecin, String>("grade"));
		tableMedecin.setItems(medecinList);
	}

	@FXML
	void searchMedecin(KeyEvent event) {
		if(recherche_medecin.getText().isBlank()) {
			this.loadMedecins();
			return;
		}

		String texteRecherche = recherche_medecin.getText().trim();
		ObservableList<Medecin> allMedecin = FXCollections.observableArrayList(medecinDAO.getAllMedecin());
		ObservableList<Medecin> medecinFiltered = FXCollections.observableArrayList();

		for (Object medecinObj : allMedecin) {
			if (medecinObj instanceof Medecin) {
				Medecin medecin = (Medecin) medecinObj;
				if (medecin.getNom().toLowerCase().contains(texteRecherche.toLowerCase())
						|| Integer.toString(medecin.getCodemed()).contains(texteRecherche)
						|| medecin.getPrenom().toLowerCase().contains(texteRecherche.toLowerCase())
						|| medecin.getGrade().toLowerCase().contains(texteRecherche.toLowerCase())) {
					medecinFiltered.add(medecin);
				}
			}
		}

		tableMedecin.setItems(medecinFiltered);
	}

	@FXML
	private void addMedecin() {
		int codemed = medecinDAO.getNewCodeMed();
		String nom = txt_nom.getText().trim();
		String prenom = txt_prenom.getText().trim();
		String grade = combo_box_grade.getValue(); // Obtient la valeur sélectionnée du ComboBox

		// Vérifier si les champs obligatoires sont vides
		if (nom.isEmpty() || prenom.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires.", ButtonType.OK);
			alert.showAndWait();
			return;
		}

		Medecin medecin = new Medecin();
		medecin.setCodemed(codemed);
		medecin.setNom(nom);
		medecin.setPrenom(prenom);
		medecin.setGrade(grade);


		if (medecinDAO.create(medecin)) {
			loadMedecins();
			reinitialiser();
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "Médecin enregistrer avec succès", ButtonType.OK);
			alert.showAndWait();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Opération non réussie !", ButtonType.OK);
			alert.showAndWait();
		}

	}

	@FXML
	private void deleteMedecin() {
		Medecin selectedMedecin = tableMedecin.getSelectionModel().getSelectedItem();
		System.out.println("selection is + +++++++++++");
		System.out.println(selectedMedecin);
		if (selectedMedecin != null) {
			Alert alertC = new Alert(Alert.AlertType.CONFIRMATION, "Voulez vous vraiment poursuivre l'action ?",
					ButtonType.YES, ButtonType.NO);
			alertC.showAndWait();

			if (alertC.getResult()!=ButtonType.NO) {
				if (medecinDAO.delete(selectedMedecin)){
					loadMedecins();
					reinitialiser();
					Alert alertI = new Alert(Alert.AlertType.INFORMATION, "Médecin supprimer avec succès", ButtonType.OK);
					alertI.showAndWait();
				}else{
					Alert alert = new Alert(Alert.AlertType.ERROR, "Opération non réussie !", ButtonType.OK);
					alert.showAndWait();
				}


			} else {
				loadMedecins();
			}

		}
		else{
			Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez selectionner une ligne", ButtonType.CLOSE);
			alert.showAndWait();
		}
	}

	@FXML
	private void updateMedecin() {
		Medecin selectedMedecin = tableMedecin.getSelectionModel().getSelectedItem();
		if (selectedMedecin != null) {
			// Récupérer les valeurs modifiées depuis les champs de saisie
			String nom = txt_nom.getText().trim();
			String prenom = txt_prenom.getText().trim();
			String grade = combo_box_grade.getValue();

			// Créer une alerte de confirmation
			Alert alertC = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir modifier ce médecin ?", ButtonType.YES, ButtonType.NO);
			alertC.showAndWait();

			if (alertC.getResult() == ButtonType.YES) {
				// Mettre à jour les valeurs du médecin sélectionné
				selectedMedecin.setNom(nom);
				selectedMedecin.setPrenom(prenom);
				selectedMedecin.setGrade(grade);

				if (medecinDAO.update(selectedMedecin)) {
					loadMedecins();
					reinitialiser();
					Alert alert = new Alert(Alert.AlertType.INFORMATION, "Médecin mis à jour avec succès", ButtonType.OK);
					alert.showAndWait();

				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR, "Opération non réussie !", ButtonType.OK);
					alert.showAndWait();
				}
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une ligne", ButtonType.CLOSE);
			alert.showAndWait();
		}
	}


	public void handleComboBox(ActionEvent event) {
		//this.reinitialiser();
		//this.lister();
	}

	public void loadComboBox() {
		combo_box_affiche.getItems().addAll(number);
		combo_box_affiche.setValue(number[0]);

		//grade
		combo_box_grade.getItems().addAll(grade);
		combo_box_grade.setValue(grade[0]);
    }

	public void reinitialiser() {
		int newCodeMed = medecinDAO.getNewCodeMed();
		txt_codemed.setText(String.valueOf(newCodeMed));
    	txt_nom.setText("");
    	txt_prenom.setText("");
    	recherche_medecin.setText("");
    	this.showBtnAddEditDeleteResetSkip(true, false, false, true,false);
	}

	
	public void initializeTextField(){
		int newCodeMed = medecinDAO.getNewCodeMed();
		txt_codemed.setText(String.valueOf(newCodeMed));
		txt_codemed.setEditable(false);
	}

	public void getSelectedTableView() {
		int index=tableMedecin.getSelectionModel().getSelectedIndex();
		if(index<=-1) {return;}
		else {
			txt_codemed.setText(codemedTv.getCellData(index).toString());
			txt_nom.setText(nomTv.getCellData(index).toString());
			txt_prenom.setText(prenomTv.getCellData(index).toString());
			combo_box_grade.setValue(gradeTv.getCellData(index).toString());

		}
		this.showBtnAddEditDeleteResetSkip(false, true, true, true,true);
	}



	public void showBtnAddEditDeleteResetSkip(Boolean valueAdd,Boolean valuEdit,Boolean valueDelete,Boolean valueReset,Boolean valueSkip) {
		btn_ajouter.setVisible(valueAdd);
		btn_modifier.setVisible(valuEdit);
		btn_supprimer.setVisible(valueDelete);
		btn_reinitialiser.setVisible(valueReset);
		btn_annuler.setVisible(valueSkip);
	}
	
	public void handleKeyEventOnTableView(KeyEvent event) {
		if(event.getCode().toString()=="UP" ||event.getCode().toString()=="RIGHT" || event.getCode().toString()=="LEFT" || event.getCode().toString()=="DOWN") {
			this.getSelectedTableView();
		}
	}

}

	
