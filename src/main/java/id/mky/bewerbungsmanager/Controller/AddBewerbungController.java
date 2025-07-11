package id.mky.bewerbungsmanager.Controller;

import id.mky.bewerbungsmanager.Model.BewerbungModel;
import id.mky.bewerbungsmanager.Model.Bewerbungsart;
import id.mky.bewerbungsmanager.Model.FirmaModel;
import id.mky.bewerbungsmanager.Model.UnterlagenModel;
import id.mky.bewerbungsmanager.Service.BewerbungService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddBewerbungController {
    @FXML private TextField firmennameField;
    @FXML private TextField kontaktpersonField;
    @FXML private TextField emailField;

    @FXML private TextField positionField;
    @FXML private ComboBox<Bewerbungsart> bewerbungsartBox;

    @FXML private CheckBox lebenslaufCheck;
    @FXML private CheckBox anschreibenCheck;
    @FXML private CheckBox zertifikateCheck;
    @FXML private CheckBox zeugnisseCheck;
    @FXML private CheckBox pdfBundleCheck;

    @FXML private ComboBox<String> statusBox;
    @FXML private TextArea rueckmeldungArea;

    private BewerbungModel bewerbung;
    private BewerbungService bs;

    @FXML
    public void initialize() {
        bewerbungsartBox.getItems().addAll(Bewerbungsart.values());

        statusBox.getItems().addAll(
                "Offen", "Abgeschickt", "Absage", "Zusage", "Erledigt"
        );

        statusBox.setValue("Abgeschickt");

        // GEÄNDERT!
        //bs.bewerbungAktualisieren(getBewerbung());
    }


    public BewerbungModel getBewerbung() {
        if (!validiereEingaben()) {
            showAlert("Fehlerhafte Eingabe", "Bitte fülle alle Pflichtfelder korrekt aus.");
            return null;
        }

        FirmaModel firma = new FirmaModel(
                firmennameField.getText().trim(),
                kontaktpersonField.getText().trim(),
                emailField.getText().trim()
        );

        UnterlagenModel unterlagen = new UnterlagenModel(
                lebenslaufCheck.isSelected(),
                anschreibenCheck.isSelected(),
                zertifikateCheck.isSelected(),
                zeugnisseCheck.isSelected(),
                pdfBundleCheck.isSelected()
        );

        BewerbungModel b = new BewerbungModel(
                (bewerbung != null ? bewerbung.getBewerbungID() : 0),
                firma,
                positionField.getText().trim(),
                unterlagen,
                statusBox.getValue(),
                rueckmeldungArea.getText().trim(),
                bewerbungsartBox.getValue()
        );

        return b;
    }


    public void setBewerbung(BewerbungModel bewerbung) {
        this.bewerbung = bewerbung;

        firmennameField.setText(bewerbung.getFirma().getName());
        kontaktpersonField.setText(bewerbung.getFirma().getKontaktperson());
        emailField.setText(bewerbung.getFirma().getEmail());

        positionField.setText(bewerbung.getPosition());
        bewerbungsartBox.setValue(bewerbung.getArtDerBewerbung());
        statusBox.setValue(bewerbung.getStatus());
        rueckmeldungArea.setText(bewerbung.getRueckmeldung());

        UnterlagenModel u = bewerbung.getUnterlagen();
        lebenslaufCheck.setSelected(u.isLebenslauf());
        anschreibenCheck.setSelected(u.isAnschreiben());
        zertifikateCheck.setSelected(u.isZertifikate());
        zeugnisseCheck.setSelected(u.isZeugnisse());
        pdfBundleCheck.setSelected(u.isPdfBundle());
    }

    private boolean validiereEingaben() {
        return !istLeer(firmennameField.getText())
                && !istLeer(emailField.getText())
                && emailField.getText().contains("@")
                && !istLeer(positionField.getText())
                && bewerbungsartBox.getValue() != null
                && statusBox.getValue() != null;
    }

    private boolean istLeer(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void showAlert(String titel, String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
