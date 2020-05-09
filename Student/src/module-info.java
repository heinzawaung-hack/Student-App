module Student {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens sample;
    opens sample.Database;
    opens sample.Edit;
    exports sample.Database;
    exports sample.Edit;
}