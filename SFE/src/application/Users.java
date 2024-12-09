package application ;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {
    private int id;
    private String prenom;
    private String nom;
    private String telephone;
    private String cin;
    private String adresse;
    private String creePar;
    private String role ;
    private String password ;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String prenom) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCreePar() {
        return creePar;
    }

    public void setCreePar(String creePar) {
        this.creePar = creePar;
    }

    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }
    public Users(int id) {
        this.id = id;
    }
    public Users() {
    	
    }
    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", cin='" + cin + '\'' +
                ", adresse='" + adresse + '\'' +
                ", creePar='" + creePar + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    public boolean isAdmin(String username) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/v5";
        String user = "root";
        String password = "";
        
        // SQL query to retrieve the role of the user
        String sql = "SELECT role FROM users WHERE nom = ?";
        
        try (
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            // Set the parameter in the prepared statement
            stmt.setString(1, nom);
            
            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    // Check if the role is admin
                    return role.equals("admin");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL errors
        }
        
        // If the user is not found or an error occurs, return false
        return false;
    }

    
    
}
