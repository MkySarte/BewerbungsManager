package id.mky.bewerbungsmanager.Controller;

import id.mky.bewerbungsmanager.Model.BewerbungModel;
import id.mky.bewerbungsmanager.Service.BewerbungService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    @FXML private TextField searchField;
    @FXML private Button neueBewerbungButton;
    @FXML private VBox bewerbungList;
    @FXML private Label gesamtLabel;
    @FXML private Label abgeschicktLabel;
    @FXML private Label absagenLabel;
    @FXML private Label erfolgeLabel;

    private BewerbungService service;
    private List<BewerbungModel> alleBewerbungen; // Zwischenspeicher für Suche

    @FXML
    public void initialize() {
        service = new BewerbungService();

        neueBewerbungButton.setOnAction(event -> offeneBewerbungDialog());
        searchField.textProperty().addListener((obs, alt, neu) -> filtereBewerbungen(neu));

        ladeAlleBewerbungen();
    }

    private void offeneBewerbungDialog() {
        try {
            URL fxmlUrl = getClass().getResource("/id/mky/bewerbungsmanager/view/add-bewerbung-view.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Neue Bewerbung");

            AddBewerbungController controller = loader.getController();

            dialog.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    BewerbungModel bewerbung = controller.getBewerbung();
                    service.bewerbungSpeichern(bewerbung);
                    ladeAlleBewerbungen();
                }
            });

        } catch (IOException e) {
            showAlert("Fehler", "Dialog konnte nicht geladen werden.\n" + e.getMessage());
        }
    }

    private void offeneBewerbungBearbeiten(BewerbungModel original) {
        try {
            URL fxmlUrl = getClass().getResource("/id/mky/bewerbungsmanager/view/add-bewerbung-view.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Bewerbung bearbeiten");

            AddBewerbungController controller = loader.getController();
            controller.setBewerbung(original);

            dialog.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    BewerbungModel bearbeitet = controller.getBewerbung();
                    service.bewerbungAktualisieren(bearbeitet);
                    ladeAlleBewerbungen();
                }
            });

        } catch (IOException e) {
            showAlert("Fehler", "Dialog konnte nicht geöffnet werden.");
        }
    }

    public void ladeAlleBewerbungen() {
        bewerbungList.getChildren().clear();
        alleBewerbungen = service.alleBewerbungenLaden(); // Zwischenspeicher
        zeigeBewerbungen(alleBewerbungen);
        aktuelleStatistik();
    }

    private void zeigeBewerbungen(List<BewerbungModel> bewerbungen) {
        bewerbungList.getChildren().clear();

        for (BewerbungModel b : bewerbungen) {
            try {
                URL fxmlUrl = getClass().getResource("/id/mky/bewerbungsmanager/view/bewerbung-card.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent card = loader.load();

                BewerbungCardController controller = loader.getController();
                controller.setBewerbung(b);
                controller.setMainController(this);
                controller.initClickHandler(() -> offeneBewerbungBearbeiten(b));

                bewerbungList.getChildren().add(card);

            } catch (IOException e) {
                System.err.println("zeigeBewerbungen() ->  Fehler beim Laden der Karte: " + e.getMessage());
            }
        }
    }

    private void filtereBewerbungen(String suchbegriff) {
        if (suchbegriff == null || suchbegriff.isBlank()) {
            zeigeBewerbungen(alleBewerbungen);
            return;
        }

        String lower = suchbegriff.toLowerCase();

        List<BewerbungModel> gefiltert = alleBewerbungen.stream()
                .filter(b -> b.getPosition().toLowerCase().contains(lower)
                        || b.getStatus().toLowerCase().contains(lower)
                        || b.getFirma().getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());

        zeigeBewerbungen(gefiltert);
    }

    public void aktuelleStatistik() {
        this.alleBewerbungen = service.alleBewerbungenLaden();

        long abgeschickt = alleBewerbungen.stream().filter(b -> "Abgeschickt".equalsIgnoreCase(b.getStatus())).count();
        long absagen = alleBewerbungen.stream().filter(b -> "Absage".equalsIgnoreCase(b.getStatus())).count();
        long zusagen = alleBewerbungen.stream().filter(b -> "Zusage".equalsIgnoreCase(b.getStatus())).count();

        gesamtLabel.setText(String.valueOf(alleBewerbungen.size()));
        abgeschicktLabel.setText(String.valueOf(abgeschickt));
        absagenLabel.setText(String.valueOf(absagen));
        erfolgeLabel.setText(String.valueOf(zusagen));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}