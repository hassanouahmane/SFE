package application;

public class DetailSourceEnergie {
    private int detailId;
    private int typeConsignationId;
    private int sourceEnergieId;
    private String identificationAppareil;
    private String numeroCadenas;
    private String nomExecutant;
    private String signature;
    private String commentaire;

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getTypeConsignationId() {
        return typeConsignationId;
    }

    public void setTypeConsignationId(int typeConsignationId) {
        this.typeConsignationId = typeConsignationId;
    }

    public int getSourceEnergieId() {
        return sourceEnergieId;
    }

    public void setSourceEnergieId(int sourceEnergieId) {
        this.sourceEnergieId = sourceEnergieId;
    }

    public String getIdentificationAppareil() {
        return identificationAppareil;
    }

    public void setIdentificationAppareil(String identificationAppareil) {
        this.identificationAppareil = identificationAppareil;
    }

    public String getNumeroCadenas() {
        return numeroCadenas;
    }

    public void setNumeroCadenas(String numeroCadenas) {
        this.numeroCadenas = numeroCadenas;
    }

    public String getNomExecutant() {
        return nomExecutant;
    }

    public void setNomExecutant(String nomExecutant) {
        this.nomExecutant = nomExecutant;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
