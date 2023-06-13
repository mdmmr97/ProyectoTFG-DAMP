package com.example.cambioturnos.controller;

import com.example.cambioturnos.Main;
import com.example.cambioturnos.MainSingleton;
import com.example.cambioturnos.MandarCorreo;
import com.example.cambioturnos.UserSingleton;
import com.example.cambioturnos.entidades.Grupos;
import com.example.cambioturnos.entidades.Peticiones;
import com.example.cambioturnos.entidades.Usuarios;
import com.mongodb.client.MongoCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CrearPeticionController implements Initializable {

    @FXML
    private Button botonCrear;
    @FXML
    private TextArea descripcion;

    @FXML
    private DatePicker fechaturno;

    @FXML
    private ComboBox<String> selectorTurno;

    private UserSingleton instanceUser;
    private MainSingleton instanceMain;
    private Usuarios usuario;
    private Grupos grupo;
    private Peticiones peticion;
    private MongoCollection<Peticiones> coleccionPeticiones;
    private MongoCollection<Grupos> coleccionGrupos;
    private ObservableList<Grupos> listagrupos;
    private ObservableList<Peticiones> listapeticiones;
    private BorderPane nodo;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instanceMain = MainSingleton.getInstance();
        instanceUser = UserSingleton.getInstance();

        usuario = instanceUser.getUsuarioLogin();
        grupo = instanceUser.getGrupo();
        peticion = new Peticiones();

        coleccionGrupos = instanceMain.getColeccionGrupos();
        coleccionPeticiones = instanceMain.getColeccionPeticiones();

        listagrupos = instanceMain.getListagrupos();
        listapeticiones = instanceMain.getListapeticiones();

        ObservableList<String> listaturnos = FXCollections.observableArrayList();
        listaturnos.addAll(grupo.getTurnos());
        selectorTurno.setItems(listaturnos);

        peticion.setFechapeticion(LocalDate.now().format(formatter));
        peticion.setUsuario(usuario.getCorreo());
        peticion.setGrupo(grupo.getId());
        peticion.setEstado("ABIERTA");

    }

    @FXML
    void AñadirPeticion(ActionEvent event) {
        boolean fechainvalida = false;
        boolean turnonull = false;

        if(fechaturno.getValue() != null && !fechaturno.getValue().equals(LocalDate.now()) && !fechaturno.getValue().isBefore(LocalDate.now())){
            peticion.setFechaturno(fechaturno.getValue().format(formatter));
            fechaturno.setStyle("");
            fechainvalida = false;
        }
        else {
            fechainvalida = true;
            fechaturno.setStyle("-fx-border-color: red");
        }

        if (selectorTurno.getValue() != null) {
            peticion.setTurno(selectorTurno.getValue());
            selectorTurno.setStyle("");
            turnonull = false;
        }
        else {
            turnonull = true;
            selectorTurno.setStyle("-fx-border-color: red");
        }

        peticion.setDescripcion(descripcion.getText());

        if (!fechainvalida  && !turnonull){
            coleccionPeticiones.insertOne(peticion);
            listapeticiones.add(peticion);
            instanceUser.setPeticion(peticion);

            Document viejogrupo = new Document("_id",grupo.getId());
            grupo.getPeticiones().add(peticion.getId());
            coleccionGrupos.replaceOne(viejogrupo,grupo);

            instanceUser.setTipoCorreo("NuevaPeticion");
            MandarCorreo correo = new MandarCorreo();
            //correo.HacerCorreo();
        }
        try {
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("InformacionPeticion.fxml"));
            nodo.setCenter(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void VolverPeticiones(ActionEvent event) {
        try {
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Peticiones.fxml"));
            nodo.setCenter(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
