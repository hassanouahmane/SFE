package application;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ajouterconsignationController {

    @FXML
    private Text date_c;

    @FXML
    private Text localisation_c;

    @FXML
    private Text exploitant;

    @FXML
    private Text n_boite;

    @FXML
    private TextField numero_boite;

    @FXML
    private Text n_attestation;

    @FXML
    private Text repere_mach;

    @FXML
    private TextField repere_machine;

    @FXML
    private Text n_cadnat;

    @FXML
    private Text charge_cons;

    @FXML
    private Text equipement_c;

    @FXML
    private ComboBox<String> equipement;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<String> equipement_a_consign;

    @FXML
    private TextField numero_atestation;

    @FXML
    private TextField numero_cadnat;

    @FXML
    private ComboBox<String> exploitant_combo;

    @FXML
    private PasswordField exploitant_pswd;

    @FXML
    private PasswordField cons_pswd;

    @FXML
    private ComboBox<String> cons_combo;

    @FXML
    private Button saveButton;

    @FXML
    private Text description_spc;

    @FXML
    private TextArea description;


    private int idAttestation;
    private int numBoite;
    private int idExploitant;
    private int idEquipement;
    private int statuts;
    private int numCadenat;
    private int idCadena;
    private int idSop;

    @FXML
    void initialize() {
        initializeComboBoxes();
        initializeEquipements();
    }

    private void initializeEquipements() {
        try (Connection connection = getConnection()) {
            String query = "SELECT nom_equipement, id_atelier FROM equipements";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                List<String> equipements = new ArrayList<>();
                List<String> localisations = new ArrayList<>();
                Map<String, Integer> equipementAtelierMap = new HashMap<>();
                while (resultSet.next()) {
                    String equipementName = resultSet.getString("nom_equipement");
                    String localisation = resultSet.getString("id_atelier");
                    equipements.add(equipementName);
                    localisations.add(localisation);
                    equipementAtelierMap.put(equipementName, Integer.parseInt(localisation));
                }
                ObservableList<String> equipementsList = FXCollections.observableArrayList(equipements);
                ObservableList<String> localisationsList = FXCollections.observableArrayList(localisations);
                equipement.setItems(equipementsList);

                equipement.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        String idAtelier = String.valueOf(equipementAtelierMap.get(newValue));
                        equipement_a_consign.setValue(idAtelier);
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'initialisation du ComboBox des équipements.");
        }
    }

    private void initializeComboBoxes() {
        initializeExploitantsComboBox();
        initializeConsignatairesComboBox();
    }

    private void initializeExploitantsComboBox() {
        try (Connection connection = getConnection()) {
            String query = "SELECT nom FROM users WHERE role = 'exploitant'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                List<String> exploitants = new ArrayList<>();
                while (resultSet.next()) {
                    String userName = resultSet.getString("nom");
                    exploitants.add(userName);
                }
                exploitant_combo.getItems().addAll(exploitants);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'initialisation du ComboBox des exploitants.");
        }
    }

    private void initializeConsignatairesComboBox() {
        try (Connection connection = getConnection()) {
            String query = "SELECT nom FROM users WHERE role = 'Charge de consignation'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                List<String> consignataires = new ArrayList<>();
                while (resultSet.next()) {
                    String userName = resultSet.getString("nom");
                    consignataires.add(userName);
                }
                cons_combo.getItems().addAll(consignataires);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'initialisation du ComboBox des consignataires.");
        }
    }

    @FXML
    void HandelValiderConsBtn(ActionEvent event) {
        String selectedCons = cons_combo.getValue();
        String consPassword = cons_pswd.getText();

        if (validateUser(selectedCons, consPassword)) {
            showAlert("Succès", "Consignataire vérifié.");
            insertDateCons();

            disableConsnputs() ;
                
        } else {
            showAlert("Erreur", "Nom de consignataire ou mot de passe incorrect.");
        }
    }

    @FXML
    void HandelValiderExploitantBtn(ActionEvent event) {
        String selectedExploitant = exploitant_combo.getValue();
        String exploitantPassword = exploitant_pswd.getText();

        if (validateUser(selectedExploitant, exploitantPassword)) {
            showAlert("Succès", "Exploitant vérifié.");
            insertDateExploitant();            

            disableExinputs();
        } else {
            showAlert("Erreur", "Nom d'exploitant ou mot de passe incorrect.");
        }
    }

    @FXML
    void savehadel(ActionEvent event) {
        insertData();
    }

    private boolean validateUser(String username, String password) {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM users WHERE nom = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la validation de l'utilisateur.");
            return false;
        }
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

    private int getIdAttestation(Connection connection, String numeroAttestation) throws SQLException {
        String query = "SELECT id_attestation FROM attestations WHERE id_attestation = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, numeroAttestation);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_attestation");
                } else {
                    throw new SQLException("Attestation non trouvée pour le numéro : " + numeroAttestation);
                }
            }
        }
    }

    private int getIdEquipement(Connection connection, String nomEquipement) throws SQLException {
        String query = "SELECT id_equipement FROM equipements WHERE nom_equipement = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nomEquipement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_equipement");
                } else {
                    throw new SQLException("Équipement non trouvé pour le nom : " + nomEquipement);
                }
            }
        }
    }

    private int getCadenatId(Connection connection, String numeroCadenat) throws SQLException {
        String query = "SELECT id_cadena FROM cadenats WHERE Num_cadenat = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, numeroCadenat);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_cadena");
                } else {
                    throw new SQLException("Cadenat non trouvé pour le numéro : " + numeroCadenat);
                }
            }
        }
    }

private void insertDateCons() {
    LocalDate currentDate = LocalDate.now();
    try (Connection connection = getConnection()) {
        String query = "UPDATE consignations SET chargecons_date = ? WHERE id_chargecons = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(currentDate));
            preparedStatement.setInt(2, idExploitant);
            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Erreur", "Une erreur s'est produite lors de l'insertion de la date de charge de consignation.");
    }
}

private void insertDateExploitant() {
    LocalDate currentDate = LocalDate.now();
    try (Connection connection = getConnection()) {
        String query = "UPDATE consignations SET exploitant_date = ? WHERE id_exploitant = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(currentDate));
            preparedStatement.setInt(2, idExploitant);
            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Erreur", "Une erreur s'est produite lors de l'insertion de la date de l'exploitant.");
    }
}
private void insertData() {
    LocalDate selectedDate = date.getValue();
    String selectedEquipement = equipement.getValue();
    String selectedEquipementConsign = equipement_a_consign.getValue();
    String numeroAttestation = numero_atestation.getText();
    String numeroCadenat = numero_cadnat.getText();
    String selectedExploitant = exploitant_combo.getValue();
    String selectedCons = cons_combo.getValue();
    String repereMachine = repere_machine.getText(); 
    String descriptionText = description.getText();

    if (selectedDate == null || selectedEquipement == null || selectedEquipementConsign == null || numeroAttestation.isEmpty() || numeroCadenat.isEmpty() || selectedExploitant == null || selectedCons == null) {
        showAlert("Erreur", "Veuillez remplir tous les champs.");
        return;
    }

    try (Connection connection = getConnection()) {
        idAttestation = getIdAttestation(connection, numeroAttestation);
        numBoite = Integer.parseInt(numero_boite.getText());
        idExploitant = getIdUser(connection, selectedExploitant, "exploitant");
        int idConsignataire = getIdUser(connection, selectedCons, "charge de consignation");
        idEquipement = getIdEquipement(connection, selectedEquipement);
        statuts = 0;
        numCadenat = Integer.parseInt(numeroCadenat);
        idCadena = getCadenatId(connection, numeroCadenat);
        idSop = 1;

        String query = "INSERT INTO consignations (date, numm_boite, id_exploitant, id_equipement, id_attestation, statuts, Num_cadenat, id_cadena, id_chargecons,repere_machine, description,id_sop) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(selectedDate));
            preparedStatement.setInt(2, numBoite);
            preparedStatement.setInt(3, idExploitant);
            preparedStatement.setInt(4, idEquipement);
            preparedStatement.setInt(5, idAttestation);
            preparedStatement.setInt(6, statuts);
            preparedStatement.setInt(7, numCadenat);
            preparedStatement.setInt(8, idCadena);
            preparedStatement.setInt(9, idConsignataire);
            preparedStatement.setString(10, repereMachine); // Lier repere_machine
            preparedStatement.setString(11, descriptionText); // Lier description
            preparedStatement.setInt(12, idSop);


            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Succès", "Données insérées avec succès.");
            } else {
                showAlert("Erreur", "Échec de l'insertion des données.");
            }
        }
    } catch (NumberFormatException e) {
        showAlert("Erreur", "Veuillez saisir des nombres valides pour le numéro de boîte et le numéro de cadenat.");
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Erreur", "Une erreur s'est produite lors de l'insertion des données.");
    }
}



    private void disableConsnputs() {
        numero_boite.setDisable(true);
        numero_atestation.setDisable(true);
        numero_cadnat.setDisable(true);
        cons_combo.setDisable(true);
        cons_pswd.setDisable(true);
        repere_machine.setDisable(true);
        description.setDisable(true);

    }
    private void disableExinputs() {
        date.setDisable(true);
        equipement_a_consign.setDisable(true);
        exploitant_combo.setDisable(true);
        exploitant_pswd.setDisable(true);
        equipement.setDisable(true);


       
    }

    private int getIdUser(Connection connection, String username, String role) throws SQLException {
        String query = "SELECT id_user FROM users WHERE nom = ? AND role = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, role);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_user");
                } else {
                    throw new SQLException("Utilisateur non trouvé pour le nom : " + username + " et le rôle : " + role);
                }
            }
        }
    }
}
