package id.mky.bewerbungsmanager.Model;

import java.nio.file.FileStore;

public class BewerbungModel
{
    private int bewerbungID;
    private FirmaModel firma;
    private String position;
    private UnterlagenModel unterlagen;
    private String status;
    private String rueckmeldung;


    private Bewerbungsart artDerBewerbung;

    public BewerbungModel(int bewerbungID, FirmaModel firma, String position, UnterlagenModel unterlagen, String status, String rueckmeldung, Bewerbungsart artDerBewerbung) {
        this.bewerbungID = bewerbungID;
        this.firma = firma;
        this.position = position;
        this.unterlagen = unterlagen;
        this.status = status;
        this.rueckmeldung = rueckmeldung;
        this.artDerBewerbung = artDerBewerbung;
    }

    public FirmaModel getFirma() {
        return firma;
    }

    public String getPosition() {
        return position;
    }


    public UnterlagenModel getUnterlagen() {
        return unterlagen;
    }


    public String getStatus() {
        return status;
    }


    public String getRueckmeldung() {
        return rueckmeldung;
    }


    public Bewerbungsart getArtDerBewerbung() {
        return artDerBewerbung;
    }

    public int getBewerbungID() {
        return bewerbungID;
    }

    public void setBewerbungID(int bewerbungID) {
        this.bewerbungID = bewerbungID;
    }

}
