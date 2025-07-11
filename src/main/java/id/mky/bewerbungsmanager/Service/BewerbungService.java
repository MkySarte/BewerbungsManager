package id.mky.bewerbungsmanager.Service;

import id.mky.bewerbungsmanager.DAO.BewerbungDAO;
import id.mky.bewerbungsmanager.DAO.FirmaDAO;
import id.mky.bewerbungsmanager.Model.BewerbungModel;
import id.mky.bewerbungsmanager.Model.FirmaModel;

import java.util.List;

public class BewerbungService
{
    private final FirmaDAO firmaDAO;
    private final BewerbungDAO bewerbungDAO;

    public BewerbungService() {
        this.firmaDAO = new FirmaDAO();
        this.bewerbungDAO = new BewerbungDAO();
    }


    public boolean bewerbungSpeichern(BewerbungModel bewerbung) {
        if (!validiereBewerbung(bewerbung)) {
            System.err.println("bewerbungSpeichern ->  BewerbungsService");
            return false;
        }

        FirmaModel firma = bewerbung.getFirma();
        FirmaModel vorhandeneFirma = firmaDAO.findByName(firma.getName());

        int firmaId;

        if (vorhandeneFirma != null) {
            firmaId = vorhandeneFirma.getFirmenID();
        } else {
            firmaId = firmaDAO.save(firma);
        }

        if (firmaId <= 0) {
            System.err.println("firmaId");
            return false;
        }

        int bewerbungId = bewerbungDAO.save(bewerbung, firmaId);

        return bewerbungId != -1;
    }


    public List<BewerbungModel> alleBewerbungenLaden() {
        return bewerbungDAO.findAll();
    }


    public List<BewerbungModel> bewerbungenNachStatus(String status) {
        return bewerbungDAO.findByStatus(status);
    }


    public boolean bewerbungAktualisieren(BewerbungModel bewerbung) {
        if (!validiereBewerbung(bewerbung)) {
            System.err.println("bewerbungAktualisieren -> BewerbungsService");
            return false;
        }

        //GEÄNDERT

        BewerbungModel geaendert = bewerbungDAO.findById(bewerbung.getBewerbungID());
        System.err.println("Daten wurden aktualisiert (BewerbungService - bewerbungAktualisieren");

        //GEÄNDERT

        return bewerbungDAO.update(geaendert);
        //return bewerbungDAO.update(bewerbung); //geändert!
    }

    // wird bei deleteBewerbungUndFirmaWennLetzte mit
    public boolean bewerbungLöschen(int id) {
        return bewerbungDAO.delete(id);
    }

    public boolean deleteBewerbungUndFirmaWennLetzte(BewerbungModel bewerbung) {
        int bewerbungId = bewerbung.getBewerbungID();
        int firmaId = bewerbung.getFirma().getFirmenID();


        boolean deleted = bewerbungDAO.delete(bewerbungId);

        if (!deleted) {
            System.err.println("deleteBewerbungUndFirmaWennLetzte -> BewerbungsService");
            return false;
        }


        int count = bewerbungDAO.countByFirmaId(firmaId);
        if (count == 0) {
            boolean firmaDeleted = firmaDAO.delete(firmaId);
            if (firmaDeleted) {
                System.out.println("DelErfolgt -> deleteBewerbungUndFirmaWennLetzte -> BewerbungsService");
            } else {
                System.err.println("DelNichtErfolgreich -> deleteBewerbungUndFirmaWennLetzte -> BewerbungsService");
            }
        }

        return true;
    }


    private boolean validiereBewerbung(BewerbungModel b) {
        if (b == null || b.getFirma() == null) return false;

        FirmaModel f = b.getFirma();
        if (istLeer(f.getName()) || istLeer(f.getEmail())) return false;
        if (!f.getEmail().contains("@")) return false;

        if (istLeer(b.getPosition()) || b.getArtDerBewerbung() == null || istLeer(b.getStatus())) return false;

        return true;
    }

    private boolean istLeer(String text) {
        return text == null || text.trim().isEmpty();
    }
}
