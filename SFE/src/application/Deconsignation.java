package application;

public class Deconsignation {
    private int idConsignation;
    private String dateDeconsignation;
    private String heure;
    private String exploitant;
    private String dateExploitant;
    private String chargeConsignation;
    private String dateChargeConsignation;
    private String observation;
    private int id ;

    public int getIdConsignation() {
        return idConsignation;
    }

    public void setIdConsignation(int idConsignation) {
        this.idConsignation = idConsignation;
    }

    public String getDateDeconsignation() {
        return dateDeconsignation;
    }

    public void setDateDeconsignation(String dateDeconsignation) {
        this.dateDeconsignation = dateDeconsignation;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getExploitant() {
        return exploitant;
    }

    public void setExploitant(String exploitant) {
        this.exploitant = exploitant;
    }

    public String getDateExploitant() {
        return dateExploitant;
    }

    public void setDateExploitant(String dateExploitant) {
        this.dateExploitant = dateExploitant;
    }

    public String getChargeConsignation() {
        return chargeConsignation;
    }

    public void setChargeConsignation(String chargeConsignation) {
        this.chargeConsignation = chargeConsignation;
    }

    public String getDateChargeConsignation() {
        return dateChargeConsignation;
    }

    public void setDateChargeConsignation(String dateChargeConsignation) {
        this.dateChargeConsignation = dateChargeConsignation;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
    public Deconsignation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void setIddeconsignation( int id) {
    	this.id=id;
    }
}
