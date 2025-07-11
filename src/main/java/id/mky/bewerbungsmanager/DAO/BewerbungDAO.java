package id.mky.bewerbungsmanager.DAO;

import id.mky.bewerbungsmanager.DB.DatabaseConnection;
import id.mky.bewerbungsmanager.Model.BewerbungModel;
import id.mky.bewerbungsmanager.Model.Bewerbungsart;
import id.mky.bewerbungsmanager.Model.FirmaModel;
import id.mky.bewerbungsmanager.Model.UnterlagenModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/*
 * CRUD
 *
 * save()
 * findById()
 * findAll()
 * update()
 * delete()
 *
 * */
public class BewerbungDAO {

    private final FirmaDAO firmaDAO = new FirmaDAO();


    public int save(BewerbungModel bewerbung, int firmaId) {
        String sql = """
        INSERT INTO bewerbung (firma_id, position, art, status, rueckmeldung, lebenslauf, anschreiben, zertifikate, zeugnisse, pdf_bundle)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, firmaId);
            pstmt.setString(2, bewerbung.getPosition());
            pstmt.setString(3, bewerbung.getArtDerBewerbung().name());
            pstmt.setString(4, bewerbung.getStatus());
            pstmt.setString(5, bewerbung.getRueckmeldung());

            UnterlagenModel u = bewerbung.getUnterlagen();
            pstmt.setBoolean(6, u.isLebenslauf());
            pstmt.setBoolean(7, u.isAnschreiben());
            pstmt.setBoolean(8, u.isZertifikate());
            pstmt.setBoolean(9, u.isZeugnisse());
            pstmt.setBoolean(10, u.isPdfBundle());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // mit Hilfe der Firmen ID suchen
    public BewerbungModel findById(int id) {
        String sql = "SELECT * FROM bewerbung WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int firmaId = rs.getInt("firma_id");
                FirmaModel firma = firmaDAO.findById(firmaId);

                UnterlagenModel unterlagen = new UnterlagenModel(
                        rs.getBoolean("lebenslauf"),
                        rs.getBoolean("anschreiben"),
                        rs.getBoolean("zertifikate"),
                        rs.getBoolean("zeugnisse"),
                        rs.getBoolean("pdf_bundle")
                );

                return new BewerbungModel(
                        rs.getInt("id"),
                        firma,
                        rs.getString("position"),
                        unterlagen,
                        rs.getString("status"),
                        rs.getString("rueckmeldung"),
                        Bewerbungsart.fromString(rs.getString("art"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<BewerbungModel> findAll() {
        List<BewerbungModel> list = new ArrayList<>();
        String sql = "SELECT * FROM bewerbung";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            FirmaDAO firmaDAO = new FirmaDAO();

            while (rs.next()) {
                int firmaId = rs.getInt("firma_id");

                FirmaModel firma = firmaDAO.findById(firmaId);
                if (firma == null) {
                    System.err.println("Firma mit ID " + firmaId + " wurde nicht gefunden (Bewerbung ID: " + rs.getInt("id") + ")");
                    continue;
                }

                UnterlagenModel unterlagen = new UnterlagenModel(
                        rs.getBoolean("lebenslauf"),
                        rs.getBoolean("anschreiben"),
                        rs.getBoolean("zertifikate"),
                        rs.getBoolean("zeugnisse"),
                        rs.getBoolean("pdf_bundle")
                );

                BewerbungModel b = new BewerbungModel(
                        rs.getInt("id"),
                        firma,
                        rs.getString("position"),
                        unterlagen,
                        rs.getString("status"),
                        rs.getString("rueckmeldung"),
                        Bewerbungsart.fromString(rs.getString("art"))
                );

                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public List<BewerbungModel> findByStatus(String status) {
        List<BewerbungModel> list = new ArrayList<>();
        String sql = "SELECT * FROM bewerbung WHERE status = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int firmaId = rs.getInt("firma_id");
                FirmaModel firma = firmaDAO.findById(firmaId);

                UnterlagenModel unterlagen = new UnterlagenModel(
                        rs.getBoolean("lebenslauf"),
                        rs.getBoolean("anschreiben"),
                        rs.getBoolean("zertifikate"),
                        rs.getBoolean("zeugnisse"),
                        rs.getBoolean("pdf_bundle")
                );

                BewerbungModel b = new BewerbungModel(
                        rs.getInt("id"),
                        firma,
                        rs.getString("position"),
                        unterlagen,
                        rs.getString("status"),
                        rs.getString("rueckmeldung"),
                        Bewerbungsart.fromString(rs.getString("art"))
                );

                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // bricht nach update ab
    public boolean update(BewerbungModel bewerbung) {
        String sql = """
        UPDATE bewerbung SET
            firma_id = ?,
            position = ?,
            art = ?,
            status = ?,
            rueckmeldung = ?,
            lebenslauf = ?,
            anschreiben = ?,
            zertifikate = ?,
            zeugnisse = ?,
            pdf_bundle = ?
        WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bewerbung.getFirma().getFirmenID());
            pstmt.setString(2, bewerbung.getPosition());
            pstmt.setString(3, bewerbung.getArtDerBewerbung().name());
            pstmt.setString(4, bewerbung.getStatus());
            pstmt.setString(5, bewerbung.getRueckmeldung());

            UnterlagenModel u = bewerbung.getUnterlagen();
            pstmt.setBoolean(6, u.isLebenslauf());
            pstmt.setBoolean(7, u.isAnschreiben());
            pstmt.setBoolean(8, u.isZertifikate());
            pstmt.setBoolean(9, u.isZeugnisse());
            pstmt.setBoolean(10, u.isPdfBundle());

            pstmt.setInt(11, bewerbung.getBewerbungID());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean delete(int id) {
        String sql = "DELETE FROM bewerbung WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int countByFirmaId(int firmaId) {
        String sql = "SELECT COUNT(*) FROM bewerbung WHERE firma_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, firmaId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}