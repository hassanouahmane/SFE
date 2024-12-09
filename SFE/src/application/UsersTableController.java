package application;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UsersTableController {

    @FXML
    private TableView<Users> tableView;
    @FXML
    private TextField search;
    @FXML
    private TableColumn<Users, String> nomColomn;

    @FXML
    private TableColumn<Users, String> prenomColomn;

    @FXML
    private TableColumn<Users, String> cinColomn;

    @FXML
    private TableColumn<Users, String> teleColomn;

    @FXML
    private TableColumn<Users, String> addresseColomn;

    @FXML
    private TableColumn<Users, String> createdbyColomn;

    @FXML
    private TableColumn<Users, String> rolrColomn;

    @FXML
    private void initialize() {
    	nomColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        prenomColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        cinColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCin()));
        teleColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        addresseColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        createdbyColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreePar()));
        rolrColomn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        initializeTableView();
    }

    private void initializeTableView() {
        tableView.getItems().clear();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT * FROM users";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Users user = new Users(0);
                
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setCin(resultSet.getString("cin"));
                user.setTelephone(resultSet.getString("tel"));
                user.setAdresse(resultSet.getString("address"));
                user.setCreePar(resultSet.getString("created_by"));
                user.setRole(resultSet.getString("role"));

                tableView.getItems().addAll(user);
            }


            resultSet.close();
            preparedStatement.close();
            connection.close();

           

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void refreshTableView() {
       initializeTableView();
    }
    @FXML
    void HandelAddUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/users.fxml"));
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
        }
    }

    @FXML
    void SearchText(KeyEvent event) {
        String searchTerm = search.getText().trim();
        if (!searchTerm.isEmpty()) {
            searchUserByName(searchTerm);
        } else {
            // Afficher une alerte ou effectuer une autre action en cas de terme de recherche vide
        }
    }

    private void searchUserByName(String nom) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");

            String query = "SELECT * FROM users WHERE nom LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + nom + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            tableView.getItems().clear();

            while (resultSet.next()) {
                		Users user = new Users(0);
                        
                        user.setNom(resultSet.getString("nom"));
                        user.setPrenom(resultSet.getString("prenom"));
                        user.setCin(resultSet.getString("cin"));
                        user.setTelephone(resultSet.getString("tel"));
                        user.setAdresse(resultSet.getString("address"));
                        user.setCreePar(resultSet.getString("created_by"));
                        user.setRole(resultSet.getString("role"));
                

                tableView.getItems().add(user);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void menue(MouseEvent event) {

    }

}
