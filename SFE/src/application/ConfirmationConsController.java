package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConfirmationConsController {

    @FXML
    private TextField password;

    @FXML
    private Button validation_btn;

    @FXML
    private ComboBox<String> user_cre;

    @FXML
    private TextField numero_atestation;

    @FXML
    private TextField numero_cadnat;

    private boolean dataSaved = false;

    @FXML
    void initialize() {
        initializeComboBox();
    }

    private void initializeComboBox() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "SELECT nom FROM users WHERE role = 'Charge de consignation'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                List<String> exploitants = new ArrayList<>();
                while (resultSet.next()) {
                    String userName = resultSet.getString("nom");
                    exploitants.add(userName);
                }
                user_cre.getItems().addAll(exploitants);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'initialisation du ComboBox.");
        }
    }
    
    @FXML
    void valider(ActionEvent event) {
        saveData();
    }

    private void saveData() {
        String enteredPassword = password.getText();
        String selectedUser = user_cre.getValue();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "INSERT INTO consignations (id_user, id_chargecons, chargecons_date) VALUES (?, ?, CURRENT_DATE)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, selectedUser);
                preparedStatement.setInt(2, 0); 

                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Donn�es enregistr�es avec succ�s dans la table consignations.");
                    dataSaved = true;
                    disableFields();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'enregistrement des donn�es dans la base de donn�es.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void disableFields() {
        password.setEditable(false);
        numero_atestation.setEditable(false);
        numero_cadnat.setEditable(false);
    }
}
