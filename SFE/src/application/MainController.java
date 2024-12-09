package application;
import javafx.event.ActionEvent;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;

public class MainController {
  Main main  = Main.getInstance();
  Users user = main.getUser() ;
  
  @FXML
    private BorderPane main_border;
    @FXML
    private Button account_btn;
    @FXML 
    void initialize() {
    	 account_btn.setText(user.getNom());
    	    
    	    
    }
    
    Users users = new Users();
    boolean isAdmin = users.isAdmin("nom");


    @FXML
    void SwitchToAccount(ActionEvent event) throws IOException {
        switchCenter("/application/fxml/profile.fxml");
    }

    @FXML
    void SwitchToConsignation(ActionEvent event) throws IOException {
        switchCenter("/application/fxml/Consignation.fxml");
    }

    @FXML
    void SwitchToDeconsignation(ActionEvent event) throws IOException {
        switchCenter("/application/fxml/Deconsignation.fxml");
    }

    @FXML
    void SwitchToSettings(ActionEvent event) throws IOException {
        switchCenter("/application/fxml/Settings.fxml");
    }

    @FXML
    void SwitchToUsers(ActionEvent event) throws IOException {
        // Check if the user is an admin before allowing access
        if (user.isAdmin("nom")) {
            switchCenter("/application/fxml/UsersTable.fxml");
        } else {
            showAlert("Access Denied", "Insufficient Privileges", "Only administrators have access to this feature.");
        }
    }

    @FXML
    void SwitchToAttestion(ActionEvent event) throws IOException {
        switchCenter("/application/fxml/AttestationForm.fxml");

    }

    @FXML
    void SwitchToVoirplus(ActionEvent event) {
        try {
            // Path to your .pptx file
        	String filePath = "C:/Users/huawei/OneDrive/Desktop/LOTOTO Présentation.pptx";
            openPPTXFile(filePath);
        } catch (IOException e) {
            showAlert("Error", "File not found", "The specified file could not be found.");
        }
    }

    private void switchCenter(String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent root = loader.load();

        if (main_border != null) {
            main_border.setCenter(root);
        } else {
            System.out.println("BorderPane with ID 'mainBorderPane' not found in the loaded FXML.");
        }
    }

    private void openPPTXFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            // Display alert if Desktop is not supported
            if (!java.awt.Desktop.isDesktopSupported()) {
                showAlert("Error", "Desktop not supported", "Opening files is not supported on this platform.");
                return;
            }

            // Try to open the file
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.OPEN)) {
                desktop.open(file);
            } else {
                showAlert("Error", "Action not supported", "Opening files is not supported on this platform.");
            }
        } else {
            showAlert("Error", "File not found", "The specified file could not be found.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
