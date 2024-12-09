package application;

public class Consignation {
    private String date;
    private String localisation;
    private String exploitantDate;
    private String exploitant;
    private int numeroBoite;
    private int numeroAttestation;
    private String equipement;
    private String repereMachine;
    private int numeroCadnat;
    private String chargeConsignation;
    private String chargeConsDate;
    private String description;
    private int statut;
    private int id ;
   





    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getExploitantDate() {
        return exploitantDate;
    }

    public void setExploitantDate(String exploitantDate) {
        this.exploitantDate = exploitantDate;
    }

    public String getExploitant() {
        return exploitant;
    }

    public void setExploitant(String exploitant) {
        this.exploitant = exploitant;
    }
    
    

    public int getNumeroBoite() {
        return numeroBoite;
    }

    public void setNumeroBoite(int numeroBoite) {
        this.numeroBoite = numeroBoite;
    }

    public int getNumeroAttestation() {
        return numeroAttestation;
    }

    public void setNumeroAttestation(int numeroAttestation) {
        this.numeroAttestation = numeroAttestation;
    }

    public String getEquipement() {
        return equipement;
    }

    public void setEquipement(String equipement) {
        this.equipement = equipement;
    }

    public String getRepereMachine() {
        return repereMachine;
    }

    public void setRepereMachine(String repereMachine) {
        this.repereMachine = repereMachine;
    }

    public int getNumeroCadnat() {
        return numeroCadnat;
    }

    public void setNumeroCadnat(int numeroCadnat) {
        this.numeroCadnat = numeroCadnat;
    }

    public String getChargeConsignation() {
        return chargeConsignation;
    }

    public void setChargeConsignation(String chargeConsignation) {
        this.chargeConsignation = chargeConsignation;
    }

    public String getChargeConsDate() {
        return chargeConsDate;
    }

    public void setChargeConsDate(String chargeConsDate) {
        this.chargeConsDate = chargeConsDate;
    }

    public Consignation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setIdconsignation( int id) {
    	this.id=id;
    }
}
