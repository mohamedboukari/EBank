package com.ebank.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.ebank.application.models.OffreEmploi;

public class UpdateJobDialogController {

    @FXML
    private TextField posteField;

    @FXML
    private TextField sujetField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField emplacementField;

    @FXML
    private TextField dateExpirationField;

    private Stage dialogStage;
    private OffreEmploi offreEmploi;
    private JobListControllerAdmin jobListController;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOffreEmploi(OffreEmploi offreEmploi) {
        this.offreEmploi = offreEmploi;
        // Populate fields with existing data
        posteField.setText(offreEmploi.getPoste());
        sujetField.setText(offreEmploi.getSujet());
        typeField.setText(offreEmploi.getType());
        emplacementField.setText(offreEmploi.getEmplacement());
        dateExpirationField.setText(offreEmploi.getDate_expiration().toString()); // Assuming LocalDate or similar
    }

    public void setJobListController(JobListControllerAdmin jobListController) {
        this.jobListController = jobListController;
    }

    @FXML
    private void handleUpdateJob() {
        // Update the OffreEmploi object with new values
        offreEmploi.setPoste(posteField.getText());
        offreEmploi.setSujet(sujetField.getText());
        offreEmploi.setType(typeField.getText());
        offreEmploi.setEmplacement(emplacementField.getText());
        // Parse or handle date update as per your application's needs
        // Example:
        // offreEmploi.setDateExpiration(LocalDate.parse(dateExpirationField.getText()));

        // Update in database or service
        jobListController.updateJob(offreEmploi);

        // Close the dialog
        dialogStage.close();
    }
}