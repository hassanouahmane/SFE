package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SidemenuController {

    @FXML
    private HBox sidebar;

    @FXML
    private Button sidebtn_home;

    @FXML
    private Button sidebtn_consin;

    @FXML
    private Button sidebtn_equip;

    @FXML
    private Button sidebtn_settings;

    @FXML
    private Button account_btn;

    @FXML
    private Button sidebtn_users;

    @FXML
    void SwitchToAccount(ActionEvent event) {
        loadPage("/application/fxml/account.fxml", "Compte utilisateur", event);

    }

    @FXML
    void SwitchToConsignation(ActionEvent event) {
        loadPage("/application/fxml/consignation.fxml", "Consignations", event);


    }

    @FXML
    void SwitchToEquipement(ActionEvent event) {
        loadPage("/application/fxml/equipement.fxml", "Equipement", event);

    }

    @FXML
    void SwitchToHome(ActionEvent event) {
        loadPage("/application/fxml/dashboard.fxml", "Dashboard", event);

    }

    @FXML
    void SwitchToSettings(ActionEvent event) {
        loadPage("/application/fxml/settings.fxml", "settings", event);

    }

    @FXML
    void SwitchToUsers(ActionEvent event) {
        loadPage("/application/fxml/users.fxml", "users", event);

    }
    private void loadPage(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            
            // Centrer la fenêtre par rapport à l'écran principal
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - scene.getWidth()) / 2;
            double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - scene.getHeight()) / 2;
            stage.setX(centerX);
            stage.setY(centerY);
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur lors du chargement de la page " + title);
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
