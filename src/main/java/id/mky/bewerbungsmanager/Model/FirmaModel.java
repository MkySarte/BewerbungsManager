package id.mky.bewerbungsmanager.Model;

public class FirmaModel
{
    private int firmenID;
    private String name;
    private String kontaktperson;
    private String email;

    public FirmaModel(String name, String kontaktperson, String email) {
        this.name = name;
        this.kontaktperson = kontaktperson;
        this.email = email;
    }

    public FirmaModel(int firmenID, String name, String kontaktperson, String email) {
        this.firmenID = firmenID;
        this.name = name;
        this.kontaktperson = kontaktperson;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getKontaktperson() {
        return kontaktperson;
    }

    public String getEmail() {
        return email;
    }

    public int getFirmenID() {
        return firmenID;
    }

    public void setFirmenID(int firmenID) {
        this.firmenID = firmenID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKontaktperson(String kontaktperson) {
        this.kontaktperson = kontaktperson;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
