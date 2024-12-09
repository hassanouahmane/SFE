package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class loginController {
	private Main main=Main.getInstance();
    private Users user = main.getUser();
    

    @FXML
    private Pane ButtonSignup;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField nom_input;

    @FXML
    private TextField password_input;

    @FXML
    private Button signupButton;

    @FXML
    void handleSignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/signup.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors du chargement de l'écran d'inscription.");
        }
    }

    private boolean isValidCredentials(String nom, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "SELECT * FROM users WHERE nom = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nom);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void login(ActionEvent event) {
        String nom = nom_input.getText();
        String password = password_input.getText();

        if (isValidCredentials(nom, password)) {
            try {
            	setUserInfo(user, nom);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/main.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1351, 837); 
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Une erreur s'est produite lors du chargement de la page d'accueil.");
            }
            System.out.println("Connexion réussie. Redirection vers la page d'accueil...");
        } else {
            showAlert("Identifiants incorrects. Veuillez réessayer.");
        }
    }
    private void setUserInfo(Users user, String nom) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "");

            String selectQuery = "SELECT * FROM users WHERE nom=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, nom);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        user.setNom("nom");
                        user.setId(resultSet.getInt("id_user"));
                        user.setNom(resultSet.getString("nom"));
                        user.setPrenom(resultSet.getString("prenom"));
                        user.setPassword(resultSet.getString("password"));
                        user.setRole(resultSet.getString("role"));
                        user.setTelephone(resultSet.getString("tel"));
                        user.setCin(resultSet.getString("cin"));
                        user.setCreePar(resultSet.getString("created_by"));
                        user.setAdresse(resultSet.getString("address"));




                        System.out.println(user);
                    } else {
                        System.out.println("User not found. Login failed.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
