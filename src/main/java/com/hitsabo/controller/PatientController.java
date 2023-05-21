package com.hitsabo.controller;

import java.net.URL;
import java.util.ResourceBundle;
//import java.util.Date;

import com.hitsabo.dao.PatientDAO;
import com.hitsabo.entity.Patient;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class PatientController implements Initializable{
    @FXML
    private TextField txt_codepat,txt_nom,txt_prenom,txt_adresse,recherche_patient;


    @FXML
    private ChoiceBox<Integer> combo_box_affiche;

    @FXML
    private ChoiceBox<String> combo_box_sexe;

    @FXML
    private Button btn_ajouter,btn_modifier,btn_supprimer,btn_reinitialiser,btn_annuler;

    @FXML
    private TableView<Patient> tablePatient;

    @FXML
    private TableColumn<Patient, Integer> codepatTv;

    @FXML
    private TableColumn<Patient, String> adresseTv;

    @FXML
    private TableColumn<Patient, String> sexeTv;

    @FXML
    private TableColumn<Patient, String> nomTv;

    @FXML
    private TableColumn<Patient, String> prenomTv;

    @FXML
    private ImageView patientImage;

    private Integer[] number = {25, 50, 100, 250, 500};
    @FXML
    private String[] sexe= {"Masculin","Féminin","Autres"};

    private ObservableList patientList;
    private final PatientDAO patientDAO;

    public PatientController() {
        patientDAO = new PatientDAO();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.startup();
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

    public void startup() {
        loadPatients();
        this.showBtnAddEditDeleteResetSkip(true, false, false, true,false);
        this.loadComboBox();
        this.initializeTextField();
        this.animateImage(patientImage);
    }
    private void loadPatients() {
        ObservableList<Patient> patientList = FXCollections.observableArrayList(patientDAO.getAllPatient());
        codepatTv.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("codepat"));
        nomTv.setCellValueFactory(new PropertyValueFactory<Patient, String>("nom"));
        prenomTv.setCellValueFactory(new PropertyValueFactory<Patient, String>("prenom"));
        sexeTv.setCellValueFactory(new PropertyValueFactory<Patient, String>("sexe"));
        adresseTv.setCellValueFactory(new PropertyValueFactory<Patient, String>("adresse"));
        tablePatient.setItems(patientList);
    }

    @FXML
    void searchPatient(KeyEvent event) {
        if(recherche_patient.getText().isBlank()) {
            this.loadPatients();
            return;
        }

        String texteRecherche = recherche_patient.getText().trim();
        ObservableList<Patient> allMedecin = FXCollections.observableArrayList(patientDAO.getAllPatient());
        ObservableList<Patient> patientFiltered = FXCollections.observableArrayList();

        for (Object patientObj : allMedecin) {
            if (patientObj instanceof Patient) {
                Patient patient = (Patient) patientObj;
                if (patient.getNom().toLowerCase().contains(texteRecherche.toLowerCase())
                        || Integer.toString(patient.getCodepat()).contains(texteRecherche)
                        || patient.getPrenom().toLowerCase().contains(texteRecherche.toLowerCase())
                        || patient.getAdresse().toLowerCase().contains(texteRecherche.toLowerCase())
                        || patient.getSexe().toLowerCase().contains(texteRecherche.toLowerCase())) {
                    patientFiltered.add(patient);
                }
            }
        }

        tablePatient.setItems(patientFiltered);
    }

    @FXML
    private void addPatient() {
        int codepat = patientDAO.getNewCodePat();
        String nom = txt_nom.getText().trim();
        String prenom = txt_prenom.getText().trim();
        String adresse = txt_adresse.getText().trim();
        String sexe = combo_box_sexe.getValue(); // Obtient la valeur sélectionnée du ComboBox

        // Vérifier si les champs obligatoires sont vides
        if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Patient patient = new Patient();
        patient.setCodepat(codepat);
        patient.setNom(nom);
        patient.setPrenom(prenom);
        patient.setAdresse(adresse);
        patient.setSexe(sexe);


        if (patientDAO.create(patient)) {
            loadPatients();
            reinitialiser();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Patient enregistrer avec succès", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Opération non réussie !", ButtonType.OK);
            alert.showAndWait();
        }

    }

    @FXML
    private void deletePatient() {
        Patient selectedPatient = tablePatient.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            Alert alertC = new Alert(Alert.AlertType.CONFIRMATION, "Voulez vous vraiment poursuivre l'action ?",
                    ButtonType.YES, ButtonType.NO);
            alertC.showAndWait();

            if (alertC.getResult()!=ButtonType.NO) {
                if (patientDAO.delete(selectedPatient)){
                    loadPatients();
                    reinitialiser();
                    Alert alertI = new Alert(Alert.AlertType.INFORMATION, "Patient supprimer avec succès", ButtonType.OK);
                    alertI.showAndWait();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Opération non réussie !", ButtonType.OK);
                    alert.showAndWait();
                }


            } else {
                loadPatients();
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez selectionner une ligne", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    @FXML
    private void updatePatient() {
        Patient selectedPatient = tablePatient.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            // Récupérer les valeurs modifiées depuis les champs de saisie
            String nom = txt_nom.getText().trim();
            String prenom = txt_prenom.getText().trim();
            String adresse = txt_adresse.getText().trim();
            String sexe = combo_box_sexe.getValue();

            // Créer une alerte de confirmation
            Alert alertC = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir modifier ce patient ?", ButtonType.YES, ButtonType.NO);
            alertC.showAndWait();

            if (alertC.getResult() == ButtonType.YES) {
                // Mettre à jour les valeurs du patient sélectionné
                selectedPatient.setNom(nom);
                selectedPatient.setPrenom(prenom);
                selectedPatient.setAdresse(adresse);
                selectedPatient.setSexe(sexe);

                if (patientDAO.update(selectedPatient)) {
                    loadPatients();
                    reinitialiser();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Patient mise à jour avec succès", ButtonType.OK);
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
        combo_box_sexe.getItems().addAll(sexe);
        combo_box_sexe.setValue(sexe[0]);
    }

    public void reinitialiser() {
        int newCodePat = patientDAO.getNewCodePat();
        txt_codepat.setText(String.valueOf(newCodePat));
        txt_nom.setText("");
        txt_prenom.setText("");
        txt_adresse.setText("");
        recherche_patient.setText("");
        this.showBtnAddEditDeleteResetSkip(true, false, false, true,false);
    }


    public void initializeTextField(){
        int newCodePat = patientDAO.getNewCodePat();
        txt_codepat.setText(String.valueOf(newCodePat));
        txt_codepat.setEditable(false);
    }

    public void getSelectedTableView() {
        int index=tablePatient.getSelectionModel().getSelectedIndex();
        if(index<=-1) {return;}
        else {
            txt_codepat.setText(codepatTv.getCellData(index).toString());
            txt_nom.setText(nomTv.getCellData(index).toString());
            txt_prenom.setText(prenomTv.getCellData(index).toString());
            txt_adresse.setText(adresseTv.getCellData(index).toString());
            combo_box_sexe.setValue(sexeTv.getCellData(index).toString());

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


