package com.ebank.application.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Date;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import com.ebank.application.models.CharityCampaignModel;
import com.ebank.application.models.AdminUser;
import com.ebank.application.services.LoginService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

import com.ebank.application.models.User;
import com.ebank.application.utils.MaConnexion;

public class loginController implements Initializable {

    // private final Image closedEye = new
    // Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/eyes_closed.png")));
    // private final Image openEye = new
    // Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/eyes_open.png")));
    @FXML
    ImageView eyesImageView;

    @FXML
    private Pane loginPane;

    @FXML
    private Pane signUpPane;

    @FXML
    private TextField email;

    @FXML
    private PasswordField signupPassword;

    @FXML
    private Label signupConfirmationText;

    @FXML
    private Label loginLabel;

    @FXML
    private TextField signupName;

    @FXML
    private TextField signupEmail;

    @FXML
    private TextField signupAccountNumber;

    @FXML
    private ToggleButton loginToggleButton;

    @FXML
    private DatePicker signupDOB;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField shownPassword;

    @FXML
    private TextField shownLoginPassword;

    @FXML
    private ToggleButton toggleButton;

    private final LoginService loginService = new LoginService();


    public void showLoginPane() {
        loginPane.setVisible(true);
        signUpPane.setVisible(false);
    }

    public void showSignUPPane() {
        loginPane.setVisible(false);
        signUpPane.setVisible(true);
    }

    @FXML
    void switchToLoginPane() {
        loginPane.setVisible(true);
        signUpPane.setVisible(false);
    }

    @FXML
    void showPassword() {
        if (toggleButton.isSelected()) {
            shownPassword.setText(signupPassword.getText());
            shownPassword.visibleProperty().unbind();
            shownPassword.setVisible(true);
            signupPassword.setVisible(false);
        } else {
            signupPassword.setText(shownPassword.getText());
            signupPassword.setVisible(true);
            shownPassword.setVisible(false);
        }
    }

    @FXML
    void showLoginPassword() {
        if (loginToggleButton.isSelected()) {
            shownLoginPassword.setText(loginPassword.getText());
            shownLoginPassword.visibleProperty().unbind();
            shownLoginPassword.setVisible(true);
            loginPassword.setVisible(false);
        } else {
            loginPassword.setText(shownLoginPassword.getText());
            loginPassword.setVisible(true);
            shownLoginPassword.setVisible(false);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.close();
    }

    private void launchDashboard(CharityCampaignModel c) throws IOException {
        URL location = getClass().getResource("/com/ebank/application/CharityCampDashboard.fxml");
        if (location == null) {
            throw new IOException("Cannot find CharityCampDashboard.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root1 = fxmlLoader.load();
        CharityController dController = fxmlLoader.getController();
        dController.currentUser = c;
        dController.setLabels();
        dController.showHomePane();
        Stage stage = new Stage();
        stage.setTitle("E-Bank");
        stage.getIcons().add(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/ebank/application/icons/icon.png"))));
        stage.setScene(new Scene(root1));
        stage.show();
    }

    private void launchDashboard(User c) throws IOException {
        URL location = getClass().getResource("/com/ebank/application/dashboard.fxml");
        if (location == null) {
            throw new IOException("Cannot find dashboard.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root1 = fxmlLoader.load();
        DashboardController dController = fxmlLoader.getController();
        dController.currentUser = c;
        dController.setLabels();
        dController.showHomePane();
        Stage stage = new Stage();
        stage.setTitle("E-Bank");
        stage.getIcons().add(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/ebank/application/icons/icon.png"))));
        stage.setScene(new Scene(root1));
        stage.show();
    }

    private void launchDashboard(AdminUser c) throws IOException {
        URL location = getClass().getResource("/com/ebank/application/adminDashboard.fxml");
        if (location == null) {
            throw new IOException("Cannot find adminDashboard.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root1 = fxmlLoader.load();
        AdminController dController = fxmlLoader.getController();
        dController.currentUser = c;
        dController.setLabels();
        dController.showHomePane();
        Stage stage = new Stage();
        stage.setTitle("E-Bank");
        stage.getIcons().add(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/com/ebank/application/icons/icon.png"))));
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    void Login() throws SQLException, IOException {
        String emailText = email.getText();
        String passwordText = loginPassword.getText();

        if (emailText.isBlank() || passwordText.isBlank()) {
            JOptionPane.showMessageDialog(null, "Email or Password is empty");
            return;
        }

        User regularUser = loginService.getRegularUser(emailText, passwordText);
        if (regularUser != null && "USER".equals(regularUser.getRole())) {
            closeWindow();
            launchDashboard(regularUser);
            return;
        }

        CharityCampaignModel charityUser = loginService.getCharityUser(emailText, passwordText);
        if (charityUser != null && "CHARITY".equals(charityUser.getRole())) {
            closeWindow();
            launchDashboard(charityUser);
            return;
        }

        AdminUser adminUser = loginService.getAdminUser(emailText, passwordText);
        if (adminUser != null && "ADMIN".equals(adminUser.getRole())) {
            closeWindow();
            launchDashboard(adminUser);
            return;
        }


        JOptionPane.showMessageDialog(null, "Invalid email or password");
    }




    @FXML
    void signUp() throws SQLException {
        String name = signupName.getText();
        String email = signupEmail.getText();
        String accountNumber = signupAccountNumber.getText();
        LocalDate dob = signupDOB.getValue();
        String password = signupPassword.getText();

        if (!loginService.isValid(name, email, accountNumber, password, dob)) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields correctly.");
            return;
        }

        loginService.addUser(name, email, accountNumber, dob, password);
        JOptionPane.showMessageDialog(null, "User added successfully!");
        switchToLoginPane();
    }

    public static void limitTextField(TextField tf) {
        final int max = 8;
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("\\D", ""));
            }
            if (tf.getText().length() > max) {
                String s = tf.getText().substring(0, max);
                tf.setText(s);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shownPassword.visibleProperty().bind(Bindings.createBooleanBinding(() -> toggleButton.isSelected(), toggleButton.selectedProperty()));
        shownLoginPassword.visibleProperty().bind(Bindings.createBooleanBinding(() -> loginToggleButton.isSelected(), loginToggleButton.selectedProperty()));
    }
}