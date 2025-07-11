package id.mky.bewerbungsmanager.Model;

public class UnterlagenModel
{
    private boolean lebenslauf;
    private boolean anschreiben;
    private boolean zertifikate;
    private boolean zeugnisse;
    private boolean pdfBundle;

    public UnterlagenModel(boolean lebenslauf, boolean anschreiben, boolean zertifikate, boolean zeugnisse, boolean pdfBundle) {
        this.lebenslauf = lebenslauf;
        this.anschreiben = anschreiben;
        this.zertifikate = zertifikate;
        this.zeugnisse = zeugnisse;
        this.pdfBundle = pdfBundle;
    }

    public boolean isLebenslauf() {
        return lebenslauf;
    }


    public boolean isAnschreiben() {
        return anschreiben;
    }

    public boolean isZertifikate() {
        return zertifikate;
    }

    public void setZertifikate(boolean zertifikate) {
        this.zertifikate = zertifikate;
    }

    public boolean isZeugnisse() {
        return zeugnisse;
    }

    public boolean isPdfBundle() {
        return pdfBundle;
    }

}
