package application;

public class Attestation {
    private int id;
    private int idChargeConsignation;
    private int idUser;
    private int idSoutraiteur;
    private String nom;
    private int tele;
    private String date;
    private int typeConsignationId;
    private int idChargeTravaux;
    private String consChargeConsDate;
    private String chargeTravauxConsDate;
    private int deconsChargeConsId;
    private int deconsChargeTravId;
    private String deconsChargeConsDate;
    private String chargeTravauxDeconsDate;
    private DetailSourceEnergie detailSourceEnergie;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdChargeConsignation() {
        return idChargeConsignation;
    }

    public void setIdChargeConsignation(int idChargeConsignation) {
        this.idChargeConsignation = idChargeConsignation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdSoutraiteur() {
        return idSoutraiteur;
    }

    public void setIdSoutraiteur(int idSoutraiteur) {
        this.idSoutraiteur = idSoutraiteur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTele() {
        return tele;
    }

    public void setTele(int tele) {
        this.tele = tele;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTypeConsignationId() {
        return typeConsignationId;
    }

    public void setTypeConsignationId(int typeConsignationId) {
        this.typeConsignationId = typeConsignationId;
    }

    public int getIdChargeTravaux() {
        return idChargeTravaux;
    }

    public void setIdChargeTravaux(int idChargeTravaux) {
        this.idChargeTravaux = idChargeTravaux;
    }

    public String getConsChargeConsDate() {
        return consChargeConsDate;
    }

    public void setConsChargeConsDate(String consChargeConsDate) {
        this.consChargeConsDate = consChargeConsDate;
    }

    public String getChargeTravauxConsDate() {
        return chargeTravauxConsDate;
    }

    public void setChargeTravauxConsDate(String chargeTravauxConsDate) {
        this.chargeTravauxConsDate = chargeTravauxConsDate;
    }

    public int getDeconsChargeConsId() {
        return deconsChargeConsId;
    }

    public void setDeconsChargeConsId(int deconsChargeConsId) {
        this.deconsChargeConsId = deconsChargeConsId;
    }

    public int getDeconsChargeTravId() {
        return deconsChargeTravId;
    }

    public void setDeconsChargeTravId(int deconsChargeTravId) {
        this.deconsChargeTravId = deconsChargeTravId;
    }

    public String getDeconsChargeConsDate() {
        return deconsChargeConsDate;
    }

    public void setDeconsChargeConsDate(String deconsChargeConsDate) {
        this.deconsChargeConsDate = deconsChargeConsDate;
    }

    public String getChargeTravauxDeconsDate() {
        return chargeTravauxDeconsDate;
    }

    public void setChargeTravauxDeconsDate(String chargeTravauxDeconsDate) {
        this.chargeTravauxDeconsDate = chargeTravauxDeconsDate;
    }

    public DetailSourceEnergie getDetailSourceEnergie() {
        return detailSourceEnergie;
    }

    public void setDetailSourceEnergie(DetailSourceEnergie detailSourceEnergie) {
        this.detailSourceEnergie = detailSourceEnergie;
    }
}
