package id.mky.bewerbungsmanager.DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer
{
    public static void init() {
        try (Connection conn = DatabaseConnection.connect(); Statement stmt = conn.createStatement()) {

            // fiirma
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS firma (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    kontaktperson TEXT,
                    email TEXT
                );
            """);

            // bwerbung
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS bewerbung (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    firma_id INTEGER,
                    position TEXT,
                    art TEXT,
                    status TEXT,
                    rueckmeldung TEXT,
                    lebenslauf BOOLEAN,
                    anschreiben BOOLEAN,
                    zertifikate BOOLEAN,
                    zeugnisse BOOLEAN,
                    pdf_bundle BOOLEAN,
                    FOREIGN KEY (firma_id) REFERENCES firma(id)
                );
            """);

            System.out.println("Datenbank erfolgreich initialisiert.");

        } catch (SQLException e) {
            System.err.println("Fehler beim Initialisieren der DB: " + e.getMessage());
        }
    }
}
