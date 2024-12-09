package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class DeconsignationFormeController implements Initializable {

    @FXML
    private Text date_c;

    @FXML
    private Text exploitant;

    @FXML
    private Text charge_cons;

    @FXML
    private DatePicker date;

    @FXML
    private Button exploitant_validation;

    @FXML
    private Button chargecons_validation;

    @FXML
    private Button save;

    @FXML
    private ComboBox<String> exploitant_combo;

    @FXML
    private PasswordField exploitant_pswd;

    @FXML
    private PasswordField cons_pswd;

    @FXML
    private ComboBox<String> cons_combo;

    @FXML
    private Text description_spc;

    @FXML
    private TextArea observation;

    @FXML
    private ComboBox<String> idconsignation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComboBoxes();
    }

    private void initializeComboBoxes() {
        try (Connection connection = getConnection()) {
            fillComboBoxWithQuery(exploitant_combo, "SELECT nom FROM users WHERE role = 'exploitant'");
            fillComboBoxWithQuery(cons_combo, "SELECT nom FROM users WHERE role = 'Charge de consignation'");
            fillComboBoxWithQuery(idconsignation, "SELECT id_consignation FROM consignations WHERE statuts = '0'");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'initialisation des comboboxes.");
        }
    }

    private void fillComboBoxWithQuery(ComboBox<String> comboBox, String query) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                comboBox.getItems().add(resultSet.getString(1));
            }
        }
    }

    @FXML
    void HandelValiderConsBtn(ActionEvent event) {
        validateUserAndUpdate(cons_combo, cons_pswd, "Charge de consignation");
        disableConsnputs();
    }

    @FXML
    void HandelValiderExploitantBtn(ActionEvent event) {
        validateUserAndUpdate(exploitant_combo, exploitant_pswd, "exploitant");
        disableExinputs();
    }

    private void validateUserAndUpdate(ComboBox<String> comboBox, PasswordField passwordField, String role) {
        String selectedUser = comboBox.getValue();
        String password = passwordField.getText();

        if (validateFields(selectedUser, password)) {
            showAlert("Succès", role + " vérifié.");
            insertDateAndUpdate(selectedUser, role);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un " + role + " et entrer un mot de passe.");
        }
    }

    private boolean validateFields(String username, String password) {
        return username != null && !username.isEmpty() && password != null && !password.isEmpty() && validateUser(username, password);
    }

    private boolean validateUser(String username, String password) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE nom = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la validation de l'utilisateur.");
            return false;
        }
    }

    private void insertDateAndUpdate(String selectedUser, String role) {
        LocalDate currentDate = LocalDate.now();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE deconsignations SET date_" + role + " = ? WHERE " + role + " = ?")) {
            statement.setDate(1, java.sql.Date.valueOf(currentDate));
            statement.setString(2, selectedUser);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
           // showAlert("Erreur", "Une erreur s'est produite lors de l'insertion de la date de l'" + role + ".");
        }
    }

    @FXML
    void savehadel(ActionEvent event) {
        saveDataToDatabase();
    }

    private void saveDataToDatabase() {
        String selectedExploitant = exploitant_combo.getValue();
        String selectedCons = cons_combo.getValue();
        String selectedDate = date.getValue().toString();
        String observationText = observation.getText();
        String idConsignationText = idconsignation.getValue();
        int idConsignation = 0;

        try {
            idConsignation = Integer.parseInt(idConsignationText);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un ID de consignation valide.");
            return;
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO deconsignations (id_consignation, date_deconsignation, heure, exploitant, charge_consignation, id_exploitant, id_chargecons, observation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, idConsignation);
            statement.setDate(2, java.sql.Date.valueOf(selectedDate));
            statement.setTime(3, java.sql.Time.valueOf(LocalTime.now()));
            statement.setString(4, selectedExploitant);
            statement.setString(5, selectedCons);
            
            // Remplacer les valeurs null par les bonnes valeurs (id_exploitant et id_chargeconsignation)
            int idExploitant = getIdFromUser(selectedExploitant);
            int idChargeConsignation = getIdFromUser(selectedCons);
            
            statement.setInt(6, idExploitant);
            statement.setInt(7, idChargeConsignation);
            
            statement.setString(8, observationText);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Succès", "Enregistrement des données réussi.");
                insertDateChargeConsignation(selectedCons, idConsignation);
                updateConsignationStatut(idConsignation);
            } else {
                showAlert("Erreur", "Échec de l'ajout de la consignation.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'enregistrement des données.");
        }
    }

    private int getIdFromUser(String username) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id_user FROM users WHERE nom = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_user");
                }
            }
        }
        return -1; // Retourne -1 si aucun utilisateur n'est trouvé
    }
    private void updateConsignationStatut(int consignationId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE consignations SET statuts = 1 WHERE id_consignation = ?")) {
            statement.setInt(1, consignationId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la mise à jour du statut de la consignation.");
        }
    }

    private void insertDateChargeConsignation(String selectedCons, int consignationId) {
        LocalDate currentDate = LocalDate.now();
        try (Connection connection = getConnection()) {
            String query = "UPDATE deconsignations SET date_charge_consignation = ? WHERE charge_consignation = ? AND id_consignation = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(currentDate));
                preparedStatement.setString(2, selectedCons);
                preparedStatement.setInt(3, consignationId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'insertion de la date de charge de consignation.");
        }
    }
    
    private void insertDateExploitant(String selectedExploitant) {
        LocalDate currentDate = LocalDate.now();
        try (Connection connection = getConnection()) {
            String query = "UPDATE deconsignations SET date_exploitant = ? WHERE exploitant = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(currentDate));
                preparedStatement.setString(2, selectedExploitant);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'insertion de la date de l'exploitant.");
        }
    }
    private void disableExinputs() {
        exploitant_combo.setDisable(true);
        exploitant_pswd.setDisable(true);
    }
    private void disableConsnputs() {
        cons_combo.setDisable(true);
        cons_pswd.setDisable(true);
        idconsignation.setDisable(true);
        date.setDisable(true);
        observation.setDisable(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/v5", "root", "");
    }
}
