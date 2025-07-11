package id.mky.bewerbungsmanager.DAO;

import id.mky.bewerbungsmanager.DB.DatabaseConnection;
import id.mky.bewerbungsmanager.Model.FirmaModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//CRUD
public class FirmaDAO {


    public int save(FirmaModel firma) {
        String sql = "INSERT INTO firma (name, kontaktperson, email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, firma.getName());
            pstmt.setString(2, firma.getKontaktperson());
            pstmt.setString(3, firma.getEmail());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("Firma wurde nicht eingefügt – keine Zeilen betroffen.");
                return -1;
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    if (id == 0) {
                        System.err.println("Achtung: Generierte Firma-ID ist 0 – das sollte nicht vorkommen.");
                        return -1;
                    }
                    return id;
                } else {
                    System.err.println("Firma gespeichert, aber keine ID zurückgegeben.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Fehler beim Speichern der Firma: " + e.getMessage());
        }

        return -1;

    }


    public FirmaModel findById(int id) {
        String sql = "SELECT * FROM firma WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new FirmaModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("kontaktperson"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<FirmaModel> findAll() {
        List<FirmaModel> list = new ArrayList<>();
        String sql = "SELECT * FROM firma";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new FirmaModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("kontaktperson"),
                        rs.getString("email")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public FirmaModel findByName(String name) {
        String sql = "SELECT * FROM firma WHERE name = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new FirmaModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("kontaktperson"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public boolean update(FirmaModel firma) {
        String sql = "UPDATE firma SET name = ?, kontaktperson = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firma.getName());
            pstmt.setString(2, firma.getKontaktperson());
            pstmt.setString(3, firma.getEmail());
            pstmt.setInt(4, firma.getFirmenID());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean delete(int id) {
        String sql = "DELETE FROM firma WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
