package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class ConsignationController {

    @FXML
    private FlowPane modibtn;

    @FXML
    private Button AddConsBtn;

    @FXML
    private TextField search;

    @FXML
    private TableView<Consignation> tableView;

    @FXML
    private TableColumn<Consignation, Integer> idConsignationColumn;

    @FXML
    private TableColumn<Consignation, String> DateColumn;

    @FXML
    private TableColumn<Consignation, String> LocalisationColumn;

    @FXML
    private TableColumn<Consignation, String> exploitantdateColomn;

    @FXML
    private TableColumn<Consignation, String> ExploitantColumn;

    @FXML
    private TableColumn<Consignation, Integer> NumeroBoiteColumn;

    @FXML
    private TableColumn<Consignation, Integer> NumeroAttestationColumn;

    @FXML
    private TableColumn<Consignation, String> EquipementColumn;

    @FXML
    private TableColumn<Consignation, String> RepereMachineColumn;

    @FXML
    private TableColumn<Consignation, Integer> NumeroCadnatColumn;

    @FXML
    private TableColumn<Consignation, String> ChargeConsignationColomn;

    @FXML
    private TableColumn<Consignation, String> chargeconsdateColomn;

    @FXML
    private TableColumn<Consignation, String> descriptionColomn;

    @FXML
    private DatePicker datepickerconsigne;

    @FXML
    private Button filtrer;

    @FXML
    private RadioButton consignaucour;

    @FXML
    private DatePicker datepickerconsigneAu;

    @FXML
    private TextField searchByEquipement;

    @FXML
    private TextField searchByAttestation;

    @FXML
    private TextField searchByRepere;

    @FXML
    private TextField searchByAtelier;

    @FXML
    void initialize() {
        initializeTableView();
        setupContextMenu();
    }

    @FXML
    void filtrer(ActionEvent event) {
        LocalDate startDate = datepickerconsigne.getValue();
        LocalDate endDate = datepickerconsigneAu.getValue();

        if (startDate != null && endDate != null) {
            searchConsignationByDateRange(startDate, endDate);
        } else {
            showAlert("Veuillez sélectionner une plage de dates pour filtrer les consignations.");
        }
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem remplirAttestationItem = new MenuItem("Remplir l'attestation");
        remplirAttestationItem.setOnAction(event -> handleRemplirAttestation());
        
        MenuItem afficherAttestationItem = new MenuItem("Afficher attestation");
        afficherAttestationItem.setOnAction(event -> handleAfficherAttestation());
        
        contextMenu.getItems().addAll(remplirAttestationItem, afficherAttestationItem);
        
        tableView.setContextMenu(contextMenu);
        
        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                Consignation selectedConsignation = tableView.getSelectionModel().getSelectedItem();
                if (selectedConsignation != null) {
                    contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }

    private void handleAfficherAttestation() {
        Consignation selectedConsignation = tableView.getSelectionModel().getSelectedItem();
        if (selectedConsignation != null) {
            int consignationId = selectedConsignation.getId();
            
            String query = "SELECT * FROM attestations WHERE intervention = ?";
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "");
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, consignationId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int attestationId = resultSet.getInt("id_attestation");
                        String nom = resultSet.getString("nom");
                        String lieuTravail = resultSet.getString("lieu_travail");
                        String Date = resultSet.getString("date");

                        
                        
                       
                        showAlert("ID Attestation : " + attestationId + "\nNom : " + nom + "\nLieu de travail : " + lieuTravail +"\nDate : " + Date);
                    } else {
                        showAlert("Aucune attestation trouvée pour la consignation sélectionnée.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur lors de la récupération de l'attestation.");
            }
        } else {
            showAlert("Aucune consignation sélectionnée.");
        }
    }



    private void handleRemplirAttestation() {
        Consignation selectedConsignation = tableView.getSelectionModel().getSelectedItem();
        if (selectedConsignation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/AttestationForm.fxml"));
                Parent root = loader.load();

                AttestationController attestationController = loader.getController();
                attestationController.initializeFieldsFromConsignation(selectedConsignation);

                Stage attestationStage = new Stage();
                attestationStage.setScene(new Scene(root));
                attestationStage.setTitle("Remplir l'attestation");
                attestationStage.initModality(Modality.APPLICATION_MODAL);
                attestationStage.showAndWait();

                tableView.getItems().clear();
                initializeTableView();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Une erreur s'est produite lors du chargement de l'écran d'attestation.");
            }
        } else {
            showAlert("Aucune consignation sélectionnée.");
        }
    }

    private void initializeTableView() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT consignations.*, equipements.nom_equipement FROM consignations " +
                    "INNER JOIN equipements ON consignations.id_equipement = equipements.id_equipement " +
                    "WHERE consignations.statuts = 0";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Consignation consignation = new Consignation(0);
                consignation.setIdconsignation(resultSet.getInt("id_consignation"));
                consignation.setDate(resultSet.getString("date"));
                consignation.setLocalisation(resultSet.getString("id_equipement"));
                consignation.setExploitantDate(resultSet.getString("exploitant_date"));
                consignation.setExploitant(resultSet.getString("id_exploitant"));
                consignation.setNumeroBoite(resultSet.getInt("numm_boite"));
                consignation.setNumeroAttestation(resultSet.getInt("id_attestation"));
                consignation.setEquipement(resultSet.getString("nom_equipement"));
                consignation.setRepereMachine(resultSet.getString("repere_machine"));
                consignation.setNumeroCadnat(resultSet.getInt("Num_cadenat"));
                consignation.setChargeConsignation(resultSet.getString("id_chargecons"));
                consignation.setChargeConsDate(resultSet.getString("chargecons_date"));
                consignation.setDescription(resultSet.getString("description"));
                consignation.setStatut(resultSet.getInt("statuts"));

                tableView.getItems().add(consignation);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            idConsignationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            DateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
            LocalisationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocalisation()));
            exploitantdateColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExploitantDate()));
            ExploitantColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExploitant()));
            NumeroBoiteColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumeroBoite()).asObject());
            NumeroAttestationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumeroAttestation()).asObject());
            EquipementColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEquipement()));
            RepereMachineColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRepereMachine()));
            NumeroCadnatColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumeroCadnat()).asObject());
            ChargeConsignationColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChargeConsignation()));
            chargeconsdateColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChargeConsDate()));
            descriptionColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   

    @FXML
    void HandelAddConsBTN(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/FormeAjouterConsignation.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage validationStage = new Stage();
            validationStage.setScene(scene);
            validationStage.setTitle("Ajouter Consignation ");
            validationStage.initModality(Modality.APPLICATION_MODAL);
            validationStage.showAndWait();

            tableView.getItems().clear();
            initializeTableView();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors du chargement de l'écran.");
        }
    }


    private void searchConsignationByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT consignations.*, equipements.nom_equipement FROM consignations " +
                           "INNER JOIN equipements ON consignations.id_equipement = equipements.id_equipement " +
                           "WHERE DATE(consignations.date) BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet resultSet = preparedStatement.executeQuery();

            tableView.getItems().clear();

            while (resultSet.next()) {
                Consignation consignation = new Consignation(0);
                consignation.setIdconsignation(resultSet.getInt("id_consignation"));
                consignation.setDate(resultSet.getString("date"));
                consignation.setLocalisation(resultSet.getString("id_equipement"));
                consignation.setExploitantDate(resultSet.getString("exploitant_date"));
                consignation.setExploitant(resultSet.getString("id_exploitant"));
                consignation.setNumeroBoite(resultSet.getInt("numm_boite"));
                consignation.setNumeroAttestation(resultSet.getInt("id_attestation"));
                consignation.setEquipement(resultSet.getString("nom_equipement"));
                consignation.setRepereMachine(resultSet.getString("repere_machine"));
                consignation.setNumeroCadnat(resultSet.getInt("Num_cadenat"));
                consignation.setChargeConsignation(resultSet.getString("id_chargecons"));
                consignation.setChargeConsDate(resultSet.getString("chargecons_date"));
                consignation.setDescription(resultSet.getString("description"));
                consignation.setStatut(resultSet.getInt("statuts"));

                tableView.getItems().add(consignation);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors de la récupération des consignations.");
        }
    }
    @FXML
    void SearchText(KeyEvent event) {
        String searchTerm = search.getText().trim();
        if (!searchTerm.isEmpty()) {
            searchConsignationByID(Integer.parseInt(searchTerm));
        } else {
            //showAlert("Veuillez entrer un ID de consignation pour la recherche.");
        }
    }

    private void searchConsignationByID(int id) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT consignations.*, equipements.nom_equipement FROM consignations " +
                           "INNER JOIN equipements ON consignations.id_equipement = equipements.id_equipement " +
                           "WHERE consignations.id_consignation = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            tableView.getItems().clear();

            while (resultSet.next()) {
                Consignation consignation = new Consignation(0);
                consignation.setIdconsignation(resultSet.getInt("id_consignation"));
                consignation.setDate(resultSet.getString("date"));
                consignation.setLocalisation(resultSet.getString("id_equipement"));
                consignation.setExploitantDate(resultSet.getString("exploitant_date"));
                consignation.setExploitant(resultSet.getString("id_exploitant"));
                consignation.setNumeroBoite(resultSet.getInt("numm_boite"));
                consignation.setNumeroAttestation(resultSet.getInt("id_attestation"));
                consignation.setEquipement(resultSet.getString("nom_equipement"));
                consignation.setRepereMachine(resultSet.getString("repere_machine"));
                consignation.setNumeroCadnat(resultSet.getInt("Num_cadenat"));
                consignation.setChargeConsignation(resultSet.getString("id_chargecons"));
                consignation.setChargeConsDate(resultSet.getString("chargecons_date"));
                consignation.setDescription(resultSet.getString("description"));
                consignation.setStatut(resultSet.getInt("statuts"));

                tableView.getItems().add(consignation);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void filterByConsignaucour(ActionEvent event) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT consignations.*, equipements.nom_equipement FROM consignations " +
                           "INNER JOIN equipements ON consignations.id_equipement = equipements.id_equipement " +
                           "WHERE consignations.statuts = 1 OR consignations.statuts = 0";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            tableView.getItems().clear();

            while (resultSet.next()) {
                Consignation consignation = new Consignation(0);
                consignation.setIdconsignation(resultSet.getInt("id_consignation"));
                consignation.setDate(resultSet.getString("date"));
                consignation.setLocalisation(resultSet.getString("id_equipement"));
                consignation.setExploitantDate(resultSet.getString("exploitant_date"));
                consignation.setExploitant(resultSet.getString("id_exploitant"));
                consignation.setNumeroBoite(resultSet.getInt("numm_boite"));
                consignation.setNumeroAttestation(resultSet.getInt("id_attestation"));
                consignation.setEquipement(resultSet.getString("nom_equipement"));
                consignation.setRepereMachine(resultSet.getString("repere_machine"));
                consignation.setNumeroCadnat(resultSet.getInt("Num_cadenat"));
                consignation.setChargeConsignation(resultSet.getString("id_chargecons"));
                consignation.setChargeConsDate(resultSet.getString("chargecons_date"));
                consignation.setDescription(resultSet.getString("description"));
                consignation.setStatut(resultSet.getInt("statuts"));

                tableView.getItems().add(consignation);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors de la récupération des consignations.");
        }
    }
    
    @FXML
    void searchAttestation(KeyEvent event) {
        String searchTerm = searchByAttestation.getText().trim();
        if (!searchTerm.isEmpty()) {
            searchByAttestation(Integer.parseInt(searchTerm));
        } else {
            //showAlert("Veuillez entrer un ID d'attestation pour la recherche.");
        }
    }

    private void searchByAttestation(int id) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT consignations.*, equipements.nom_equipement FROM consignations " +
                           "INNER JOIN equipements ON consignations.id_equipement = equipements.id_equipement " +
                           "WHERE consignations.id_attestation = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            tableView.getItems().clear();

            while (resultSet.next()) {
            	 while (resultSet.next()) {
                     Consignation consignation = new Consignation(0);
                     consignation.setIdconsignation(resultSet.getInt("id_consignation"));
                     consignation.setDate(resultSet.getString("date"));
                     consignation.setLocalisation(resultSet.getString("id_equipement"));
                     consignation.setExploitantDate(resultSet.getString("exploitant_date"));
                     consignation.setExploitant(resultSet.getString("id_exploitant"));
                     consignation.setNumeroBoite(resultSet.getInt("numm_boite"));
                     consignation.setNumeroAttestation(resultSet.getInt("id_attestation"));
                     consignation.setEquipement(resultSet.getString("nom_equipement"));
                     consignation.setRepereMachine(resultSet.getString("repere_machine"));
                     consignation.setNumeroCadnat(resultSet.getInt("Num_cadenat"));
                     consignation.setChargeConsignation(resultSet.getString("id_chargecons"));
                     consignation.setChargeConsDate(resultSet.getString("chargecons_date"));
                     consignation.setDescription(resultSet.getString("description"));
                     consignation.setStatut(resultSet.getInt("statuts"));

                     tableView.getItems().add(consignation);
                 }
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void SearchRepere(KeyEvent event) {
        String searchTerm = searchByRepere.getText().trim();
        if (!searchTerm.isEmpty()) {
            searchByRepere(searchTerm);
        } else {
            // showAlert("Veuillez entrer un repère machine pour la recherche.");
        }
    }

    private void searchByRepere(String repere) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT consignations.*, equipements.nom_equipement FROM consignations " +
                           "INNER JOIN equipements ON consignations.id_equipement = equipements.id_equipement " +
                           "WHERE consignations.repere_machine = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, repere);
            ResultSet resultSet = preparedStatement.executeQuery();

            tableView.getItems().clear();

            while (resultSet.next()) {
                Consignation consignation = new Consignation(0);
                consignation.setIdconsignation(resultSet.getInt("id_consignation"));
                consignation.setDate(resultSet.getString("date"));
                consignation.setLocalisation(resultSet.getString("id_equipement"));
                consignation.setExploitantDate(resultSet.getString("exploitant_date"));
                consignation.setExploitant(resultSet.getString("id_exploitant"));
                consignation.setNumeroBoite(resultSet.getInt("numm_boite"));
                consignation.setNumeroAttestation(resultSet.getInt("id_attestation"));
                consignation.setEquipement(resultSet.getString("nom_equipement"));
                consignation.setRepereMachine(resultSet.getString("repere_machine"));
                consignation.setNumeroCadnat(resultSet.getInt("Num_cadenat"));
                consignation.setChargeConsignation(resultSet.getString("id_chargecons"));
                consignation.setChargeConsDate(resultSet.getString("chargecons_date"));
                consignation.setDescription(resultSet.getString("description"));
                consignation.setStatut(resultSet.getInt("statuts"));

                     tableView.getItems().add(consignation);
                 }
            

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void SearchEquipement(KeyEvent event) {
        String searchTerm = searchByEquipement.getText().trim();
        if (!searchTerm.isEmpty()) {
            searchByEquipement(searchTerm);
        } else {
            // showAlert("Veuillez entrer un nom d'équipement pour la recherche.");
        }
    }

    private void searchByEquipement(String equipement) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT consignations.*, equipements.nom_equipement FROM consignations " +
                           "INNER JOIN equipements ON consignations.id_equipement = equipements.id_equipement " +
                           "WHERE equipements.nom_equipement LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + equipement + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            tableView.getItems().clear();

            while (resultSet.next()) {
                Consignation consignation = new Consignation(0);
                consignation.setIdconsignation(resultSet.getInt("id_consignation"));
                consignation.setDate(resultSet.getString("date"));
                consignation.setLocalisation(resultSet.getString("id_equipement"));
                consignation.setExploitantDate(resultSet.getString("exploitant_date"));
                consignation.setExploitant(resultSet.getString("id_exploitant"));
                consignation.setNumeroBoite(resultSet.getInt("numm_boite"));
                consignation.setNumeroAttestation(resultSet.getInt("id_attestation"));
                consignation.setEquipement(resultSet.getString("nom_equipement"));
                consignation.setRepereMachine(resultSet.getString("repere_machine"));
                consignation.setNumeroCadnat(resultSet.getInt("Num_cadenat"));
                consignation.setChargeConsignation(resultSet.getString("id_chargecons"));
                consignation.setChargeConsDate(resultSet.getString("chargecons_date"));
                consignation.setDescription(resultSet.getString("description"));
                consignation.setStatut(resultSet.getInt("statuts"));

                tableView.getItems().add(consignation);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
