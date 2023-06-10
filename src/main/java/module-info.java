module com.example.cambioturnos {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;


    opens com.example.cambioturnos to javafx.fxml;
    exports com.example.cambioturnos;
    exports com.example.cambioturnos.entidades;
    opens com.example.cambioturnos.entidades to javafx.fxml;
    exports com.example.cambioturnos.controller;
    opens com.example.cambioturnos.controller to javafx.fxml;
}