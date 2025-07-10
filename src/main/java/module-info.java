module id.mky.bewerbungsmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Ikonli für FontIcons
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens id.mky.bewerbungsmanager to javafx.fxml;
    opens id.mky.bewerbungsmanager.view to javafx.fxml;
    opens id.mky.bewerbungsmanager.Controller to javafx.fxml;
    opens id.mky.bewerbungsmanager.Model to javafx.fxml;

    exports id.mky.bewerbungsmanager;
    exports id.mky.bewerbungsmanager.Controller;
}
