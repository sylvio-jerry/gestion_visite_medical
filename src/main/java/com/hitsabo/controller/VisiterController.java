package com.hitsabo.controller;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
//import java.util.Date;

import com.hitsabo.dao.MedecinDAO;
import com.hitsabo.dao.PatientDAO;
import com.hitsabo.dao.VisiterDAO;
import com.hitsabo.entity.Medecin;
import com.hitsabo.entity.Patient;
import com.hitsabo.entity.Visiter;
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

public class VisiterController implements Initializable{
    @FXML
    private TextField recherche_visiter;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<Integer> combo_box_affiche;

    @FXML
    private ChoiceBox<Medecin> combo_box_medecin;

    @FXML
    private ChoiceBox<Patient> combo_box_patient;

    @FXML
    private Button btn_ajouter,btn_modifier,btn_supprimer,btn_reinitialiser,btn_annuler;

    @FXML
    private TableView<Visiter> tableVisiter;

    //@FXML
    //private TableColumn<Visiter, Integer> idTv;

    @FXML
    private TableColumn<Visiter, String> nomMedecinTv;

    @FXML
    private TableColumn<Visiter, String> nomPatientTv;

    @FXML
    private TableColumn<Visiter, String> dateTv;

    @FXML
    private ImageView visiteImage;

    private Integer[] number = {25, 50, 100, 250, 500};
    @FXML
    private String[] grade= {"Spécialiste","Généraliste","Professeur"};

    private ObservableList visiterList;
    private MedecinDAO medecinDAO;
    private PatientDAO patientDAO;
    private VisiterDAO visiterDAO;

    public VisiterController() {
        visiterDAO = new VisiterDAO();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        this.startup();
    }


    public void startup() {
        loadVisiters();
        this.showBtnAddEditDeleteResetSkip(true, false, false, true,false);
        this.loadComboBox();
        this.initializeTextField();
        this.loadVisiters();
        this.loadMedecins();
        this.loadPatients();
        this.getSelectedTableView();
        this.reinitialiser();
        this.animateImage(visiteImage);
    }

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

    public LocalDate LOCAL_DATE(String Date) {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateFormat=LocalDate.parse(Date, formatter);
        return dateFormat;
    }
    private void loadVisiters() {
        ObservableList<Visiter> visiterList = FXCollections.observableArrayList(visiterDAO.getAllVisits());
        System.out.println("resultat :::: +++ :  " +visiterList);
        //idTv.setCellValueFactory(new PropertyValueFactory<Visiter, Integer>("id"));
        nomMedecinTv.setCellValueFactory(new PropertyValueFactory<Visiter, String>("nomMedecin"));
        nomPatientTv.setCellValueFactory(new PropertyValueFactory<Visiter, String>("nomPatient"));
        dateTv.setCellValueFactory(new PropertyValueFactory<Visiter, String>("date"));
        tableVisiter.setItems(visiterList);
    }

    @FXML
    void searchVisiter(KeyEvent event) {
        if(recherche_visiter.getText().isBlank()) {
            this.loadVisiters();
            return;
        }

        String texteRecherche = recherche_visiter.getText().trim();
        ObservableList<Visiter> allVisiter = FXCollections.observableArrayList(visiterDAO.getAllVisits());
        ObservableList<Visiter> visiterFiltered = FXCollections.observableArrayList();

        for (Object visiterObj : allVisiter) {
            if (visiterObj instanceof Visiter) {
                Visiter visiter = (Visiter) visiterObj;
                if (visiter.getNomMedecin().toLowerCase().contains(texteRecherche.toLowerCase())
                        || Integer.toString(visiter.getId()).contains(texteRecherche)
                        || visiter.getNomPatient().toLowerCase().contains(texteRecherche.toLowerCase())
                        || visiter.getDate().contains(texteRecherche.toLowerCase())) {
                    visiterFiltered.add(visiter);
                }
            }
        }

        tableVisiter.setItems(visiterFiltered);
    }

    @FXML
    private void addVisiter() {
        // Récupérer les valeurs des champs
        String selectedMedecin = String.valueOf(combo_box_medecin.getSelectionModel().getSelectedItem());
        String selectedPatient = String.valueOf(combo_box_patient.getSelectionModel().getSelectedItem());
        //LocalDate stringDate = datePicker.getValue();
        String stringDate = datePicker.getEditor().getText();

        // Vérifier si les champs obligatoires sont vides
        if ( selectedPatient == null || selectedMedecin == null || stringDate.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        else {
            // Créer un objet Visiter
            Visiter visiter = new Visiter();
            String codemedString = selectedMedecin.replaceAll("[^0-9]", "");
            String codepatString = selectedPatient.replaceAll("[^0-9]", "");

            int codemed = Integer.parseInt(codemedString);
            int codepat = Integer.parseInt(codepatString);
            Medecin codemedecin = medecinDAO.getByCodeMed(codemed);
            Patient codepatient = patientDAO.getByCodePat(codepat);
            int id = visiterDAO.getNewIdVisiter();

            visiter.setDate(Date.valueOf(LOCAL_DATE(stringDate)));
            visiter.setPatientByCodepat(codepatient);
            visiter.setMedecinByCodemed(codemedecin);
            visiter.setId(id);


            // Appeler la méthode create du VisiterDAO pour ajouter la visite dans la base de données
            if (visiterDAO.create(visiter)){
                // Recharger la table des visites
                loadVisiters();

                // Effacer les champs
                reinitialiser();

                // Afficher une notification de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Visite ajoutée avec succès", ButtonType.OK);
                alert.showAndWait();



            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Opération non réussie !", ButtonType.OK);
                alert.showAndWait();
            }

        }

    }

    private void loadMedecins(){
        MedecinDAO medecinDAO = new MedecinDAO();
        ObservableList<Medecin> medecins = FXCollections.observableArrayList(medecinDAO.getAllMedecin());
        combo_box_medecin.setItems(medecins);
    }

    private void loadPatients(){
        PatientDAO patientDAO = new PatientDAO();
        ObservableList<Patient> patients = FXCollections.observableArrayList(patientDAO.getAllPatient());
        combo_box_patient.setItems(patients);
    }


    @FXML
    private void deleteVisiter() {
        Visiter selectedVisiter = tableVisiter.getSelectionModel().getSelectedItem();
        if (selectedVisiter != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette visite ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                // Appeler la méthode delete du VisiterDAO pour supprimer la visite de la base de données
                visiterDAO.delete(selectedVisiter);
                // Recharger la table des visites
                loadVisiters();

                //clear
                reinitialiser();

                // Afficher une notification de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Visite supprimée avec succès",ButtonType.OK);
                successAlert.showAndWait();

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une visite.", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    @FXML
    private void updateMedecin() {
        Visiter selectedVisiter = tableVisiter.getSelectionModel().getSelectedItem();
        if (selectedVisiter != null) {
            // Récupérer les valeurs modifiées depuis les champs de saisie

            String stringDate = datePicker.getEditor().getText();
            Medecin medecin = combo_box_medecin.getValue();
            Patient patient = combo_box_patient.getValue();

            // Créer une alerte de confirmation
            Alert alertC = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir modifier cette visite ?", ButtonType.YES, ButtonType.NO);
            alertC.showAndWait();

            if (alertC.getResult() == ButtonType.YES) {
                // Mettre à jour les valeurs de la visite sélectionnée
                selectedVisiter.setMedecinByCodemed(medecin);
                selectedVisiter.setPatientByCodepat(patient);
                selectedVisiter.setDate(Date.valueOf(LOCAL_DATE(stringDate)));

                System.out.println("VISITER TO U ++++++++++++++: "+selectedVisiter);
                System.out.println("VISITER TO U id: "+selectedVisiter.getId());
                System.out.println("VISITER TO U patient: "+selectedVisiter.getPatientByCodepat());
                System.out.println("VISITER TO U medecin: "+selectedVisiter.getMedecinByCodemed());
                System.out.println("VISITER TO U date: "+selectedVisiter.getDate());


                if (visiterDAO.update(selectedVisiter)) {
                    System.out.println("SUCCESS +++ : "+selectedVisiter);
                    loadVisiters();
                    reinitialiser();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Visite mise à jour avec succès", ButtonType.OK);
                    alert.showAndWait();

                } else {
                    System.out.println("SUCCESS +++ : "+selectedVisiter);
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
    }

    public void reinitialiser() {
        datePicker.setValue(null);
        combo_box_patient.setValue(null);
        combo_box_medecin.setValue(null);
        this.showBtnAddEditDeleteResetSkip(true, false, false, true,false);
    }


    public void initializeTextField(){
        //int newCodeMed = medecinDAO.getNewCodeMed();
        //txt_codemed.setText(String.valueOf(newCodeMed));
        //txt_codemed.setEditable(false);
    }

    public void displaySelectedVisiter(Visiter visiter) {

        datePicker.setValue(LOCAL_DATE(visiter.getDate()));
        combo_box_medecin.setValue(visiter.getMedecinByCodemed());
        combo_box_patient.setValue(visiter.getPatientByCodepat());

    }
    public void getSelectedTableView() {

        tableVisiter.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Appeler une méthode pour afficher les valeurs du visite sélectionné dans les champs de saisie
                displaySelectedVisiter(newSelection);
            }else{
                return;
            }
        });

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


