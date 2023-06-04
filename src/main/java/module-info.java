module com.example.demo16 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo16 to javafx.fxml;
    exports com.example.demo16;
}