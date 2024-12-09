module SFE {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
	requires java.desktop;

    exports application;

    opens application to javafx.fxml;
}
