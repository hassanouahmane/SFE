package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Optional; // Assurez-vous d'importer java.util.Optional

import java.io.IOException;
import java.sql.*;

public class ProfilController {

    @FXML
    private TextField prenom_txt;

    @FXML
    private TextField nom_txt;

    @FXML
    private TextField phone_txt;

    @FXML
    private TextField cin_txt1;

    @FXML
    private TextField address_txt;

    @FXML
    private TextField created_by_text;

    @FXML
    private TextField role_text;

    @FXML
    private Button logout_btn;

    private int idUtilisateur; 
    Main main  = Main.getInstance();
    Users user = main.getUser() ;
    	

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    @FXML
    void initialize() {
        fillUserInfoFields();
    }

    
    private void fillUserInfoFields() {
        if (user != null) {
        	System.out.println(user);
            prenom_txt.setText(user.getPrenom());
            nom_txt.setText(user.getNom());
            phone_txt.setText(user.getTelephone());
            cin_txt1.setText(user.getCin());
            address_txt.setText(user.getAdresse());
            created_by_text.setText(user.getCreePar());
            role_text.setText(user.getRole());
        }
    }

    @FXML
    void handellogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de déconnexion");
        alert.setHeaderText("Voulez-vous vraiment vous déconnecter ?");
        alert.setContentText("Cliquez sur OK pour vous déconnecter ou sur Annuler pour rester connecté.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/fxml/login.fxml"));
                Stage stage = (Stage) logout_btn.getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }   }

