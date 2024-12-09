package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;

public class SettingsController {
	private Main main=Main.getInstance();
    private Users user = main.getUser();

    @FXML
    private TextField username_txt;

    @FXML
    private PasswordField current_password_txt;

    @FXML
    private PasswordField new_password_txt;

    @FXML
    private PasswordField confirm_password_txt;

    @FXML
    private Button save;

    private int idUtilisateur; // ID de l'utilisateur, à définir lors de l'initialisation

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
        initializeLoginInfo();
    }

    @FXML
    void initialize() {
        // Cette méthode est appelée lors de l'initialisation du contrôleur
    }

    private void initializeLoginInfo() {
        // Récupérer les informations de connexion de l'utilisateur depuis la base de données
    }

    @FXML
    void handleSaveLoginInfo() {
        String newPassword = new_password_txt.getText();
        String confirmPassword = confirm_password_txt.getText();

        // Validate the new password and confirmation
        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nouveau mot de passe et la confirmation ne correspondent pas.");
            return;
        }

        // Attempt to update the user information
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");
            String query = "UPDATE users SET password = ? WHERE nom = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, user.getNom());
            preparedStatement.setString(3, current_password_txt.getText()); 
            // Validate current password
            System.out.println(newPassword);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Informations de connexion mises à jour avec succès !");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La mise à jour a échoué. Mot de passe actuel incorrect ou utilisateur introuvable.");
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la mise à jour des informations.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
