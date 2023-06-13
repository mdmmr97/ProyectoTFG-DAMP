module com.example.cambioturnos {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires java.mail;
    requires jbcrypt;


    opens com.example.cambioturnos to javafx.fxml;
    exports com.example.cambioturnos;
    exports com.example.cambioturnos.entidades;
    opens com.example.cambioturnos.entidades to javafx.fxml;
    exports com.example.cambioturnos.controller.Login;
    opens com.example.cambioturnos.controller.Login to javafx.fxml;
    exports com.example.cambioturnos.controller.Grupos;
    opens com.example.cambioturnos.controller.Grupos to javafx.fxml;
    exports com.example.cambioturnos.controller.Peticiones;
    opens com.example.cambioturnos.controller.Peticiones to javafx.fxml;

}