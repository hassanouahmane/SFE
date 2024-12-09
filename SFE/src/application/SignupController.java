package application;

import java.io.IOException;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SignupController {

    @FXML
    private TextField tele;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField address;

    @FXML
    private TextField cin;

    @FXML
    private Button adduserbtn;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<String> role;

    @FXML
    public void initialize() {
        role.getItems().addAll("Charge de consignation", "Charge de travaux", "Exploitant", "admin");
        role.setValue("Charge de consignation"); 
    }

    @FXML
    void handleAddUserButtonAction(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String userAddress = address.getText();
        String telephone = tele.getText();
        String cinNumber = cin.getText();
        String userPassword = password.getText();
        String selectedRole = role.getValue();
        
        if (firstName == null || lastName == null || userAddress == null || telephone.isEmpty() || cinNumber.isEmpty() || userPassword.isEmpty() || selectedRole == null) {
            showAlert("Veuillez remplir tous les champs.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "INSERT INTO users (nom, prenom, address, tel, password, created_by, role, CIN) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, userAddress);
                preparedStatement.setString(4, telephone);
                preparedStatement.setString(5, userPassword);
                preparedStatement.setNull(6, java.sql.Types.VARCHAR); 
                preparedStatement.setString(7, selectedRole);
                preparedStatement.setString(8, cinNumber);

             int rowsInserted = preparedStatement.executeUpdate();

               
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors de l'enregistrement de l'utilisateur dans la base de données.");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/add_user_validation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            Stage validationStage = new Stage();
            validationStage.setScene(scene);
            validationStage.setTitle("Validation de l'inscription");
            validationStage.initModality(Modality.APPLICATION_MODAL); 
            validationStage.showAndWait(); 
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors du chargement de l'écran de validation.");
        }
    }
    
   
      

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
