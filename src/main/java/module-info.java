module com.example.chemistryproj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires static lombok;


    opens com.example.chemistryproj to javafx.fxml;
    exports com.example.chemistryproj;
    exports com.example.chemistryprojControllers;
    opens com.example.chemistryprojControllers to javafx.fxml;
}