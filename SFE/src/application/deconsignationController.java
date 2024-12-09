
package application;

import javafx.beans.property.SimpleIntegerProperty;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class deconsignationController {

    @FXML
    private FlowPane modibtn;

    @FXML
    private Button addDeconsBtn;

    @FXML
    private Button csv;

    @FXML
    private DatePicker datepickerconsigne;

    @FXML
    private Button filtrer;

    @FXML
    private TextField search;


    @FXML
    private TableView<Deconsignation> tableView;

    @FXML
    private TableColumn<Deconsignation, Integer> idconsignationColumn;

    @FXML
    private TableColumn<Deconsignation, String> DateColumn;

    @FXML
    private TableColumn<Deconsignation, String> heureColomn;

    @FXML
    private TableColumn<Deconsignation, String> ExploitantColumn;

    @FXML
    private TableColumn<Deconsignation, String> exploitantdateColomn;

    @FXML
    private TableColumn<Deconsignation, String> chargeconsdateColomn;

    @FXML
    private TableColumn<Deconsignation, String> ChargeConsignationColomn;

    @FXML
    private TableColumn<Deconsignation, String> descriptionColomn;

    @FXML
    private DatePicker datepickerconsigneAu;
    @FXML
    void initialize() {
    	idconsignationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdConsignation()).asObject());
        DateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDeconsignation()));
        heureColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHeure()));
        ExploitantColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExploitant()));
        exploitantdateColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateExploitant()));
        chargeconsdateColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateChargeConsignation()));
        ChargeConsignationColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChargeConsignation()));
        descriptionColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObservation()));

        loadData();
    }

    private void loadData() {
        tableView.getItems().clear();
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM deconsignations";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Deconsignation deconsignation = new Deconsignation(0);
                    deconsignation.setIdConsignation(resultSet.getInt("id_consignation"));
                    deconsignation.setDateDeconsignation(resultSet.getString("date_deconsignation"));
                    deconsignation.setHeure(resultSet.getString("heure"));
                    deconsignation.setExploitant(resultSet.getString("exploitant"));
                    deconsignation.setDateExploitant(resultSet.getString("date_exploitant"));
                    deconsignation.setChargeConsignation(resultSet.getString("charge_consignation"));
                    deconsignation.setDateChargeConsignation(resultSet.getString("date_charge_consignation"));
                    deconsignation.setObservation(resultSet.getString("observation"));
                    tableView.getItems().add(deconsignation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");
    }

    @FXML
    void handleAddConsBtn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/DeconsignationForme.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage validationStage = new Stage();
            validationStage.setScene(scene);
            validationStage.setTitle("Ajouter Consignation ");
            validationStage.initModality(Modality.APPLICATION_MODAL);
            validationStage.showAndWait();

            refreshTableView();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors du chargement de l'écran.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshTableView() {
        loadData();
    }
    @FXML
    void HandelAddConsBTN(ActionEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/DeconsignationForme.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage validationStage = new Stage();
            validationStage.setScene(scene);
            validationStage.setTitle("Ajouter Consignation ");
            validationStage.initModality(Modality.APPLICATION_MODAL);
            validationStage.showAndWait();

            loadData();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors du chargement de l'écran.");
        }

    }
    @FXML
    void SearchText(KeyEvent event) {
    	String searchTerm = search.getText().trim();
        if (!searchTerm.isEmpty()) {
            searchDeconsignationByID(Integer.parseInt(searchTerm));
        } else {
            //showAlert("Veuillez entrer un ID de consignation pour la recherche.");
        }

    }
    private void searchDeconsignationByID(int id) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT deconsignations.* " +
                    "FROM deconsignations " + 
                    "WHERE deconsignations.id_consignation = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            tableView.getItems().clear();

            while (resultSet.next()) {
                Deconsignation deconsignation = new Deconsignation(0);
                deconsignation.setIdConsignation(resultSet.getInt("id_consignation"));
                deconsignation.setDateDeconsignation(resultSet.getString("date_deconsignation")); 
                deconsignation.setDateExploitant(resultSet.getString("date_exploitant"));
                deconsignation.setExploitant(resultSet.getString("id_exploitant"));
                deconsignation.setChargeConsignation(resultSet.getString("id_chargecons"));
                deconsignation.setDateChargeConsignation(resultSet.getString("date_charge_consignation"));
                deconsignation.setObservation(resultSet.getString("observation"));

                tableView.getItems().add(deconsignation);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void filtrer(ActionEvent event) {
        LocalDate startDate = datepickerconsigne.getValue();
        LocalDate endDate = datepickerconsigneAu.getValue();
        
        if (startDate != null && endDate != null) {
        	searchDeconsignationByDateRange(startDate, endDate);
        } else {
            showAlert("Veuillez sélectionner une plage de dates pour filtrer les consignations.");
        }
    }
    private void searchDeconsignationByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT deconsignations.* FROM deconsignations " +
                           
                           "WHERE DATE(deconsignations.date_deconsignation) BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet resultSet = preparedStatement.executeQuery();

            tableView.getItems().clear();

            while (resultSet.next()) {
                Deconsignation deconsignation = new Deconsignation(0);
                deconsignation.setIdConsignation(resultSet.getInt("id_consignation"));
                deconsignation.setDateDeconsignation(resultSet.getString("date_deconsignation"));
                deconsignation.setHeure(resultSet.getString("heure"));
                deconsignation.setExploitant(resultSet.getString("exploitant"));
                deconsignation.setDateExploitant(resultSet.getString("date_exploitant"));
                deconsignation.setChargeConsignation(resultSet.getString("charge_consignation"));
                deconsignation.setDateChargeConsignation(resultSet.getString("date_charge_consignation"));
                deconsignation.setObservation(resultSet.getString("observation"));
                tableView.getItems().add(deconsignation);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors de la récupération des déconsignations.");
        }
    }


  
}
