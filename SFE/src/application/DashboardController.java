package application ;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class DashboardController {

    @FXML
    private AnchorPane mainAnchorPane;

    // Fonction pour charger une nouvelle page dans le centre de la page d'accueil
    private void loadPage(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        try {
            Node page = loader.load();
            mainAnchorPane.getChildren().clear();
            mainAnchorPane.getChildren().add(page);
            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setBottomAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switchToAccount() {
        loadPage("/application/fxml/account.fxml");
    }

    @FXML
    void switchToConsignation() {
        loadPage("/application/fxml/consignation.fxml");
    }

    @FXML
    void switchToEquipement() {
        loadPage("/application/fxml/equipement.fxml");
    }

    @FXML
    void switchToSettings() {
        loadPage("/application/fxml/settings.fxml");
    }

    @FXML
    void switchToUsers() {
        loadPage("/application/fxml/users.fxml");
    }
}
