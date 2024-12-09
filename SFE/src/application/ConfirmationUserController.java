package application ;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConfirmationUserController {

    @FXML
    private PasswordField password;

    @FXML
    private Button validation_btn;

    @FXML
    private ComboBox<String> user_cre;

    private Connection connection;

    @FXML
    public void initialize() {
        // Connexion à la base de données
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "");
            fillComboBox();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de connexion à la base de données.");
        }
    }

    private void fillComboBox() {
        List<String> adminNames = new ArrayList<>();
        String query = "SELECT nom FROM users WHERE role = 'admin'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("nom");
                adminNames.add(name);
            }
            user_cre.getItems().addAll(adminNames);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de la récupération des noms d'administrateurs depuis la base de données.");
        }
    }

    @FXML
    void valider(ActionEvent event) {
        String selectedAdmin = user_cre.getValue();
        String enteredPassword = password.getText();
        if (selectedAdmin == null) {
            showAlert("Veuillez sélectionner un administrateur.");
            return;
        }
        String query = "SELECT password FROM users WHERE nom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, selectedAdmin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String adminPassword = resultSet.getString("password");
                if (enteredPassword.equals(adminPassword)) {
                    // Mot de passe correct, insérer le nom de l'administrateur dans la colonne "created_by"
                    insertCreatedBy(selectedAdmin);
                } else {
                    showAlert("Mot de passe incorrect.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de la vérification du mot de passe dans la base de données.");
        }
    }

    private void insertCreatedBy(String adminName) {
        String updateQuery = "UPDATE users SET created_by = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, adminName);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
               showAlert("L'utilisateur a était cré par succes.");
            } else {
                showAlert("La colonne 'created_by' a été mise à jour pour tous les administrateurs.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'insertion du nom de l'administrateur dans la colonne 'created_by'.");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}