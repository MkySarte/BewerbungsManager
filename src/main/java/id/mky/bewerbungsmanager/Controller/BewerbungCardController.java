package id.mky.bewerbungsmanager.Controller;

import id.mky.bewerbungsmanager.Model.BewerbungModel;
import id.mky.bewerbungsmanager.Model.FirmaModel;
import id.mky.bewerbungsmanager.Model.UnterlagenModel;
import id.mky.bewerbungsmanager.Service.BewerbungService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BewerbungCardController {

    @FXML private Label firmennameLabel;
    @FXML private Label statusLabel;
    @FXML private Label pdfLabel;
    @FXML private Label positionLabel;
    @FXML private Label kontaktpersonLabel;
    @FXML private Label emailLabel;
    @FXML private Label datumLabel;
    @FXML private Label unterlagenInfoLabel;
    @FXML private ProgressBar unterlagenProgress;
    @FXML private Label rueckmeldungLabel;
    @FXML private AnchorPane root;
    @FXML private Button loeschenButton;

    private BewerbungModel bewerbung;
    private BewerbungService service = new BewerbungService();
    private MainController mc;

    public void setMainController(MainController mc) {
        this.mc = mc;
    }

    public void setBewerbung(BewerbungModel bewerbung) {
        this.bewerbung = bewerbung;


        FirmaModel firma = bewerbung.getFirma();
        firmennameLabel.setText(firma.getName());
        kontaktpersonLabel.setText(firma.getKontaktperson());
        emailLabel.setText(firma.getEmail());


        positionLabel.setText(bewerbung.getPosition());
        datumLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));


        String status = bewerbung.getStatus();
        statusLabel.setText(status);
        statusLabel.getStyleClass().add("status-" + status.toLowerCase().replace(" ", "-"));


        UnterlagenModel u = bewerbung.getUnterlagen();
        int vorhanden = 0;
        if (u.isLebenslauf()) vorhanden++;
        if (u.isAnschreiben()) vorhanden++;
        if (u.isZertifikate()) vorhanden++;
        if (u.isZeugnisse()) vorhanden++;
        unterlagenInfoLabel.setText("Unterlagen (" + vorhanden + "/4)");
        unterlagenProgress.setProgress(vorhanden / 4.0);


        pdfLabel.setVisible(u.isPdfBundle());


        String rm = bewerbung.getRueckmeldung();
        rueckmeldungLabel.setText(rm);
        rueckmeldungLabel.setVisible(rm != null && !rm.isBlank());


        loeschenButton.setOnAction(event -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Bewerbung löschen");
            confirm.setHeaderText("Bewerbung wirklich löschen?");
            confirm.setContentText(bewerbung.getFirma().getName() + " – " + bewerbung.getPosition());

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    service.deleteBewerbungUndFirmaWennLetzte(bewerbung);
                    ((VBox) root.getParent()).getChildren().remove(root);

                    if (mc != null) {
                        mc.aktuelleStatistik();
                    }
                }
            });
        });
    }

    public BewerbungModel getBewerbung() {
        return bewerbung;
    }

    public void initClickHandler(Runnable onClick) {
        root.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                onClick.run();
            }
        });

        root.setCursor(javafx.scene.Cursor.HAND);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
