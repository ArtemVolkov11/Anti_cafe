module com.example.anti_cafe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j;

    opens com.example.anti_cafe to javafx.fxml;
    exports com.example.anti_cafe;
}