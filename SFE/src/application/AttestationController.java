package application;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttestationController {

    @FXML
    private TextField textFieldIntervention, textFieldLieuTravail, entrepriseexterieurnom, entrepriseexterieurtele, textfieladattestaion;
    @FXML
    private TextArea textAreaDescription, textAreaCommentaires;
    @FXML
    private ComboBox<String> hydrolique, pneumatique, airchaud, mecanique, chimique, gaz, energipotentielle, typeenergiecombx, typeenergiecomb;
    @FXML
    private ComboBox<String> typeconscombo;
    @FXML
    private ComboBox<String> comboBoxChargeConsignation, comboBoxChargeTravaux, comboBoxChargeConsignationValidation;
    @FXML
    private ComboBox<String> comboBoxChargeTravauxValidation, comboBoxChargeDeconsignation, comboBoxChargeTravauxDeconsignation;
    @FXML
    private DatePicker datePickerChargeConsignation, datePickerChargeTravaux, datePickerChargeDeconsignation, datePickerChargeTravauxDeconsignation;
    @FXML
    private Button buttonValiderConsignation, buttonValiderTravaux, buttonValiderDeconsignation, buttonEnregistrer;

    @FXML
    private TextField textFieldElectriqueAppareil, textFieldElectriqueCadenas, textFieldElectriqueExecutant, textFieldElectriqueSignature;
    @FXML
    private TextField textFieldHydrauliqueAppareil, textFieldHydrauliqueCadenas, textFieldHydrauliqueExecutant, textFieldHydrauliqueSignature;
    @FXML
    private TextField textFieldPneumatiqueAppareil, textFieldPneumatiqueCadenas, textFieldPneumatiqueExecutant, textFieldPneumatiqueSignature;
    @FXML
    private TextField textFieldAirChaudAppareil, textFieldAirChaudCadenas, textFieldAirChaudExecutant, textFieldAirChaudSignature;
    @FXML
    private TextField textFieldMecaniqueAppareil, textFieldMecaniqueCadenas, textFieldMecaniqueExecutant, textFieldMecaniqueSignature;
    @FXML
    private TextField textFieldProduitsChimiquesAppareil, textFieldProduitsChimiquesCadenas, textFieldProduitsChimiquesExecutant, textFieldProduitsChimiquesSignature;
    @FXML
    private TextField textFieldGazVapeurAppareil, textFieldGazVapeurCadenas, textFieldGazVapeurExecutant, textFieldGazVapeurSignature;
    @FXML
    private TextField textFieldEnergiePotentielleAppareil, textFieldEnergiePotentielleCadenas, textFieldEnergiePotentielleExecutant, textFieldEnergiePotentielleSignature;
    @FXML
    private TextField textFieldArriveeMatiereAppareil, textFieldArriveeMatiereCadenas, textFieldArriveeMatiereExecutant, textFieldArriveeMatiereSignature;


    @FXML
    void initialize() {
        initializeComboBox();
    }

    private void initializeComboBox() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            initializeRoleComboBox(connection, "Charge de consignation", comboBoxChargeConsignation, comboBoxChargeConsignationValidation, comboBoxChargeDeconsignation);
            initializeRoleComboBox(connection, "Charge de travaux", comboBoxChargeTravaux, comboBoxChargeTravauxValidation, comboBoxChargeTravauxDeconsignation);
            initializeTypeConsignationComboBox(connection, typeconscombo);
            initializeTypeEnergieComboBox(connection, typeenergiecomb, typeenergiecombx, hydrolique, pneumatique, airchaud, mecanique, chimique, gaz, energipotentielle);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'initialisation des ComboBox.");
        }
    }

    private void initializeRoleComboBox(Connection connection, String role, ComboBox<String>... comboBoxes) throws SQLException {
        String query = "SELECT nom FROM users WHERE role = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, role);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<String> names = new ArrayList<>();
                while (resultSet.next()) {
                    names.add(resultSet.getString("nom"));
                }
                for (ComboBox<String> comboBox : comboBoxes) {
                    comboBox.setItems(FXCollections.observableArrayList(names));
                }
            }
        }
    }

    public void initializeFieldsFromConsignation(Consignation consignation) {
        textFieldIntervention.setText(String.valueOf(consignation.getId()));
        textFieldLieuTravail.setText(consignation.getLocalisation());
        textfieladattestaion.setText(String.valueOf(consignation.getNumeroAttestation()));
        comboBoxChargeConsignation.setValue(consignation.getChargeConsignation());
    }

    private void initializeTypeConsignationComboBox(Connection connection, ComboBox<String> comboBox) throws SQLException {
        String query = "SELECT type_consignation FROM typeonsignation";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            List<String> types = new ArrayList<>();
            while (resultSet.next()) {
                types.add(resultSet.getString("type_consignation"));
            }
            comboBox.setItems(FXCollections.observableArrayList(types));
        }
    }

    private void initializeTypeEnergieComboBox(Connection connection, ComboBox<String>... comboBoxes) throws SQLException {
        String query = "SELECT nom_source_energie FROM sourcesdenergie";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            List<String> types = new ArrayList<>();
            while (resultSet.next()) {
                types.add(resultSet.getString("nom_source_energie"));
            }
            for (ComboBox<String> comboBox : comboBoxes) {
                comboBox.setItems(FXCollections.observableArrayList(types));
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
    private String[] getSourceEnergieDetails(int sourceEnergieId) {
        switch (sourceEnergieId) {
            case 1:
                return new String[]{
                    textFieldElectriqueAppareil.getText(),
                    textFieldElectriqueCadenas.getText(),
                    textFieldElectriqueExecutant.getText(),
                    textFieldElectriqueSignature.getText()
                };
            case 2:
                return new String[]{
                    textFieldHydrauliqueAppareil.getText(),
                    textFieldHydrauliqueCadenas.getText(),
                    textFieldHydrauliqueExecutant.getText(),
                    textFieldHydrauliqueSignature.getText()
                };
            case 3:
                return new String[]{
                    textFieldPneumatiqueAppareil.getText(),
                    textFieldPneumatiqueCadenas.getText(),
                    textFieldPneumatiqueExecutant.getText(),
                    textFieldPneumatiqueSignature.getText()
                };
            case 4:
                return new String[]{
                    textFieldAirChaudAppareil.getText(),
                    textFieldAirChaudCadenas.getText(),
                    textFieldAirChaudExecutant.getText(),
                    textFieldAirChaudSignature.getText()
                };
            case 5:
                return new String[]{
                    textFieldMecaniqueAppareil.getText(),
                    textFieldMecaniqueCadenas.getText(),
                    textFieldMecaniqueExecutant.getText(),
                    textFieldMecaniqueSignature.getText()
                };
            case 6:
                return new String[]{
                    textFieldProduitsChimiquesAppareil.getText(),
                    textFieldProduitsChimiquesCadenas.getText(),
                    textFieldProduitsChimiquesExecutant.getText(),
                    textFieldProduitsChimiquesSignature.getText()
                };
            case 7:
                return new String[]{
                    textFieldGazVapeurAppareil.getText(),
                    textFieldGazVapeurCadenas.getText(),
                    textFieldGazVapeurExecutant.getText(),
                    textFieldGazVapeurSignature.getText()
                };
            case 8:
                return new String[]{
                    textFieldEnergiePotentielleAppareil.getText(),
                    textFieldEnergiePotentielleCadenas.getText(),
                    textFieldEnergiePotentielleExecutant.getText(),
                    textFieldEnergiePotentielleSignature.getText()
                };
            case 9:
                return new String[]{
                    textFieldArriveeMatiereAppareil.getText(),
                    textFieldArriveeMatiereCadenas.getText(),
                    textFieldArriveeMatiereExecutant.getText(),
                    textFieldArriveeMatiereSignature.getText()
                };
            default:
                return new String[4];
        }
    }

    @FXML
    void handleSave(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            connection.setAutoCommit(false); // Begin transaction

            String queryAttestation = "INSERT INTO attestations (id_chargecons, id_user, id_soutraiteur, nom, tele, statut, systeme, date, typeconsignation_id, id_charge_travaux, cons_charcons_date, charge_travaux_cons_date, decons_chargecons_id, decons_charg_trav_id, decons_charcons_date, charge_travaux_decons_date, intervention, lieu_travail) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatementAttestation = connection.prepareStatement(queryAttestation, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatementAttestation.setInt(1, getUserIdFromComboBox(comboBoxChargeConsignation));
                preparedStatementAttestation.setInt(2, getUserIdFromComboBox(comboBoxChargeTravaux));
                preparedStatementAttestation.setInt(3, getSubcontractorId(entrepriseexterieurnom.getText(), Integer.parseInt(entrepriseexterieurtele.getText())));
                preparedStatementAttestation.setString(4, entrepriseexterieurnom.getText());
                preparedStatementAttestation.setInt(5, Integer.parseInt(entrepriseexterieurtele.getText()));
                preparedStatementAttestation.setInt(6, 0); // Placeholder for 'statut'
                preparedStatementAttestation.setInt(7, 0); // Placeholder for 'systeme'
                preparedStatementAttestation.setDate(8, Date.valueOf(LocalDate.now()));
                preparedStatementAttestation.setInt(9, getTypeConsignationId(typeconscombo.getValue()));
                preparedStatementAttestation.setInt(10, getUserIdFromComboBox(comboBoxChargeTravaux));
                preparedStatementAttestation.setDate(11, Date.valueOf(datePickerChargeConsignation.getValue()));
                preparedStatementAttestation.setDate(12, Date.valueOf(datePickerChargeTravaux.getValue()));
                preparedStatementAttestation.setInt(13, getUserIdFromComboBox(comboBoxChargeDeconsignation));
                preparedStatementAttestation.setInt(14, getUserIdFromComboBox(comboBoxChargeTravauxDeconsignation));
                preparedStatementAttestation.setDate(15, Date.valueOf(datePickerChargeDeconsignation.getValue()));
                preparedStatementAttestation.setDate(16, Date.valueOf(datePickerChargeTravauxDeconsignation.getValue()));
                preparedStatementAttestation.setString(17, textFieldIntervention.getText());
                preparedStatementAttestation.setString(18, textFieldLieuTravail.getText());

                preparedStatementAttestation.executeUpdate();

                try (ResultSet generatedKeys = preparedStatementAttestation.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int attestationId = generatedKeys.getInt(1);

                        // Insert into detail_source_denergie table
                        String queryDetailSourceEnergie = "INSERT INTO detailssourcedenergie (attestation_id, typeconsignation_id, source_energie_id, identification_appareil, numero_cadenas, nom_executant, signature, commentaire) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement preparedStatementDetailSourceEnergie = connection.prepareStatement(queryDetailSourceEnergie)) {
                            int typeConsignationId = getTypeConsignationId(typeconscombo.getValue());
                            for (int sourceEnergieId = 1; sourceEnergieId <= 9; sourceEnergieId++) { // Assuming there are 9 source energies
                                String[] details = getSourceEnergieDetails(sourceEnergieId);
                                preparedStatementDetailSourceEnergie.setInt(1, attestationId);
                                preparedStatementDetailSourceEnergie.setInt(2, typeConsignationId);
                                preparedStatementDetailSourceEnergie.setInt(3, sourceEnergieId);
                                preparedStatementDetailSourceEnergie.setString(4, details[0]);
                                preparedStatementDetailSourceEnergie.setString(5, details[1]);
                                preparedStatementDetailSourceEnergie.setString(6, details[2]);
                                preparedStatementDetailSourceEnergie.setString(7, details[3]);
                                preparedStatementDetailSourceEnergie.setString(8, textAreaCommentaires.getText());

                                preparedStatementDetailSourceEnergie.addBatch();
                            }
                            preparedStatementDetailSourceEnergie.executeBatch();
                        }

                        // Update the consignation with the new attestation ID
                        String queryUpdateConsignation = "UPDATE consignations SET id_attestation = ? WHERE id_consignation = ?";
                        try (PreparedStatement preparedStatementUpdateConsignation = connection.prepareStatement(queryUpdateConsignation)) {
                            preparedStatementUpdateConsignation.setInt(1, attestationId);
                            preparedStatementUpdateConsignation.setInt(2, Integer.parseInt(textFieldIntervention.getText()));
                            preparedStatementUpdateConsignation.executeUpdate();
                        }

                    } else {
                        throw new SQLException("Failed to retrieve the generated attestation ID.");
                    }
                }
            }

            connection.commit(); // Commit transaction

            showAlert("Attestation enregistrée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'enregistrement de l'attestation.");
        }
    }


    private String getNumeroCadenas(int sourceEnergieId) throws SQLException {
        String numeroCadenas = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "SELECT numero_cadenas FROM detailssourcedenergie WHERE source_energie_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, sourceEnergieId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        numeroCadenas = resultSet.getString("numero_cadenas");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur appropriée, par exemple afficher une alerte ou renvoyer une valeur par défaut
        }
        return numeroCadenas;
    }

    private String getNomExecutant(int sourceEnergieId) throws SQLException {
        String nomExecutant = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "SELECT nom_executant FROM detailssourcedenergie WHERE source_energie_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, sourceEnergieId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        nomExecutant = resultSet.getString("nom_executant");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur appropriée, par exemple afficher une alerte ou renvoyer une valeur par défaut
        }
        return nomExecutant;
    }

    private String getSignature(int sourceEnergieId) throws SQLException {
        String signature = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "SELECT signature FROM detailssourcedenergie WHERE source_energie_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, sourceEnergieId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        signature = resultSet.getString("signature");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur appropriée, par exemple afficher une alerte ou renvoyer une valeur par défaut
        }
        return signature;
    }

    private String getCommentaire(int sourceEnergieId) throws SQLException {
        String commentaire = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "SELECT commentaire FROM detailssourcedenergie WHERE source_energie_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, sourceEnergieId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        commentaire = resultSet.getString("commentaire");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur appropriée, par exemple afficher une alerte ou renvoyer une valeur par défaut
        }
        return commentaire;
    }

    private String getIdentificationAppareil(int sourceEnergieId) throws SQLException {
        String identificationAppareil = null;
        // Establish a database connection
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            // Prepare the SQL query
            String query = "SELECT identification_appareil FROM detailssourcedenergie WHERE source_energie_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Set the parameter in the prepared statement
                preparedStatement.setInt(1, sourceEnergieId);
                // Execute the query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if a result was found
                    if (resultSet.next()) {
                        // Retrieve the identification appareil from the result set
                        identificationAppareil = resultSet.getString("identification_appareil");
                    }
                }
            }
        }
        return identificationAppareil;
    }

    private int getUserIdFromComboBox(ComboBox<String> comboBox) throws SQLException {
        String userName = comboBox.getValue();
        if (userName == null || userName.isEmpty()) {
            return 0; // Or handle appropriately
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "SELECT id_user FROM users WHERE nom = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id_user");
                    } else {
                        throw new SQLException("User not found.");
                    }
                }
            }
        }
    }

    private int getSubcontractorId(String name, int tele) throws SQLException {
        if (name == null || name.isEmpty()) {
            return 0; // Or handle appropriately
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "SELECT id_soutraiteur FROM soutraiteurs WHERE nom = ? AND tele = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, tele);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id_soutraiteur");
                    } else {
                        // Insert new subcontractor
                        String insertQuery = "INSERT INTO soutraiteurs (nom, tele) VALUES (?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                            insertStatement.setString(1, name);
                            insertStatement.setInt(2, tele);
                            insertStatement.executeUpdate();

                            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    return generatedKeys.getInt(1);
                                } else {
                                    throw new SQLException("Failed to retrieve the generated subcontractor ID.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int getTypeConsignationId(String typeConsignation) throws SQLException {
        if (typeConsignation == null || typeConsignation.isEmpty()) {
            return 0; // Or handle appropriately
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v5", "root", "")) {
            String query = "SELECT typeconsignation_id FROM typeonsignation WHERE type_consignation = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, typeConsignation);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("typeconsignation_id");
                    } else {
                        throw new SQLException("Type de consignation not found.");
                    }
                }
            }
        }
    }

    private String getSourceEnergieDetails(String type) {
        String details = "";
        switch (type) {
            case "electrique":
                details = textFieldElectriqueAppareil.getText() + "," + textFieldElectriqueCadenas.getText() + "," + textFieldElectriqueExecutant.getText() + "," + textFieldElectriqueSignature.getText();
                break;
            case "hydraulique":
                details = textFieldHydrauliqueAppareil.getText() + "," + textFieldHydrauliqueCadenas.getText() + "," + textFieldHydrauliqueExecutant.getText() + "," + textFieldHydrauliqueSignature.getText();
                break;
            case "pneumatique":
                details = textFieldPneumatiqueAppareil.getText() + "," + textFieldPneumatiqueCadenas.getText() + "," + textFieldPneumatiqueExecutant.getText() + "," + textFieldPneumatiqueSignature.getText();
                break;
            case "air_chaud":
                details = textFieldAirChaudAppareil.getText() + "," + textFieldAirChaudCadenas.getText() + "," + textFieldAirChaudExecutant.getText() + "," + textFieldAirChaudSignature.getText();
                break;
            case "mecanique":
                details = textFieldMecaniqueAppareil.getText() + "," + textFieldMecaniqueCadenas.getText() + "," + textFieldMecaniqueExecutant.getText() + "," + textFieldMecaniqueSignature.getText();
                break;
            case "produits_chimiques":
                details = textFieldProduitsChimiquesAppareil.getText() + "," + textFieldProduitsChimiquesCadenas.getText() + "," + textFieldProduitsChimiquesExecutant.getText() + "," + textFieldProduitsChimiquesSignature.getText();
                break;
            case "gaz_vapeur":
                details = textFieldGazVapeurAppareil.getText() + "," + textFieldGazVapeurCadenas.getText() + "," + textFieldGazVapeurExecutant.getText() + "," + textFieldGazVapeurSignature.getText();
                break;
            case "energie_potentielle":
                details = textFieldEnergiePotentielleAppareil.getText() + "," + textFieldEnergiePotentielleCadenas.getText() + "," + textFieldEnergiePotentielleExecutant.getText() + "," + textFieldEnergiePotentielleSignature.getText();
                break;
            case "arrivee_matiere":
                details = textFieldArriveeMatiereAppareil.getText() + "," + textFieldArriveeMatiereCadenas.getText() + "," + textFieldArriveeMatiereExecutant.getText() + "," + textFieldArriveeMatiereSignature.getText();
                break;
          
        }
        return details;
    }
}
