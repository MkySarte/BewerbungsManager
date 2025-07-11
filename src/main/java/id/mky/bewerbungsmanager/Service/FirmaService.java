package id.mky.bewerbungsmanager.Service;

import id.mky.bewerbungsmanager.DAO.FirmaDAO;
import id.mky.bewerbungsmanager.Model.FirmaModel;

import java.util.List;

public class FirmaService
{
    private final FirmaDAO firmaDAO;

    public FirmaService() {
        this.firmaDAO = new FirmaDAO();
    }


    public boolean firmaSpeichern(FirmaModel firma) {
        if (!validiereFirma(firma)) {
            System.err.println("Ungültige Firmendaten.");
            return false;
        }

        FirmaModel vorhanden = firmaDAO.findByName(firma.getName());
        if (vorhanden != null) {
            System.out.println("Firma existiert bereits.");
            firma.setFirmenID(vorhanden.getFirmenID());
            return true;
        }

        int id = firmaDAO.save(firma);
        if (id != -1) {
            firma.setFirmenID(id);
            System.out.println("Neue Firma gespeichert mit ID: " + id);
            return true;
        }

        return false;
    }


    public List<FirmaModel> alleFirmen() {
        return firmaDAO.findAll();
    }


    public FirmaModel findeFirma(int id) {
        return firmaDAO.findById(id);
    }


    public FirmaModel findeFirma(String name) {
        return firmaDAO.findByName(name);
    }


    public boolean firmaAktualisieren(FirmaModel firma) {
        if (!validiereFirma(firma)) {
            System.err.println("Ungültige Firmendaten.");
            return false;
        }

        return firmaDAO.update(firma);
    }


    public boolean firmaLöschen(int id) {
        return firmaDAO.delete(id);
    }


    private boolean validiereFirma(FirmaModel f) {
        if (f == null) return false;
        if (istLeer(f.getName()) || istLeer(f.getEmail())) return false;
        if (!f.getEmail().contains("@")) return false;

        return true;
    }

    private boolean istLeer(String text) {
        return text == null || text.trim().isEmpty();
    }
}
