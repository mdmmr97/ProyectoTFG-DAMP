package com.example.cambioturnos.controller.Peticiones;

import com.example.cambioturnos.Main;
import com.example.cambioturnos.MainSingleton;
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
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModificarPeticionController implements Initializable {

    @FXML
    private TextArea descripcion;

    @FXML
    private ToggleButton estadoAbierto;

    @FXML
    private ToggleButton estadoCerrado;

    @FXML
    private TextField fechapeticion;

    @FXML
    private DatePicker fechaturno;

    @FXML
    private ComboBox<String> turno;

    @FXML
    private TextField usuario;

    private MainSingleton instanceMain;
    private MongoCollection<Peticiones> coleccionPeticiones;
    private ObservableList<Peticiones> listapeticiones;
    private UserSingleton instanceUser;
    private Peticiones peticion;
    private Grupos grupo;
    private BorderPane nodo;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instanceMain = MainSingleton.getInstance();
        instanceUser = UserSingleton.getInstance();

        grupo = instanceUser.getGrupo();
        peticion = instanceUser.getPeticion();
        coleccionPeticiones = instanceMain.getColeccionPeticiones();
        listapeticiones = instanceMain.getListapeticiones();

        ObservableList<String> listaturnos = FXCollections.observableArrayList();
        listaturnos.addAll(grupo.getTurnos());
        turno.setItems(listaturnos);

        ToggleGroup estado = new ToggleGroup();
        estadoAbierto.setToggleGroup(estado);
        estadoCerrado.setToggleGroup(estado);

        if (peticion.getEstado().equals("ABIERTA")) estadoAbierto.setSelected(true);
        else estadoCerrado.setSelected(true);

        AtomicBoolean seleccionado = new AtomicBoolean(false);

        estadoAbierto.setOnAction(actionEvent -> {
            if (estadoAbierto.isSelected()) {
                seleccionado.set(true);
                estadoCerrado.setSelected(false);
            } else if (!seleccionado.get() ) estadoAbierto.setSelected(true);
        });
        estadoCerrado.setOnAction(actionEvent -> {
            if (estadoCerrado.isSelected()) {
                seleccionado.set(false);
                estadoAbierto.setSelected(false);
            } else estadoCerrado.setSelected(true);
        });
        RellenarDatos();
    }

    @FXML
    void ModificarPeticion(ActionEvent event) {
        boolean fechainvalida = false;

        if(fechaturno.getValue() != null && !fechaturno.getValue().equals(LocalDate.now()) && !fechaturno.getValue().isBefore(LocalDate.now())){
            peticion.setFechaturno(fechaturno.getValue().format(formatter));
            fechaturno.setStyle("");
            fechainvalida = false;
        }
        else {
            fechainvalida = true;
            fechaturno.setStyle("-fx-border-color: red");
        }
        peticion.setTurno(turno.getValue());
        if (estadoAbierto.isSelected()) peticion.setEstado("ABIERTA");
        else peticion.setEstado("CERRADA");

        peticion.setDescripcion(descripcion.getText());

       if (!fechainvalida){

            Document viejapeticion = new Document("_id",peticion.getId());
            coleccionPeticiones.replaceOne(viejapeticion,peticion);

           try {
               nodo = instanceUser.getNodo();
               FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Peticiones/Peticiones.fxml"));
               nodo.setCenter(fxmlloader.load());
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }
    }

    @FXML
    void VolverPeticiones(ActionEvent event) {
        try {
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Peticiones/Peticiones.fxml"));
            nodo.setCenter(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void RellenarDatos(){
        fechapeticion.setText(peticion.getFechapeticion());
        LocalDate fecloc = LocalDate.parse(peticion.getFechaturno(),formatter);
        fechaturno.setValue(fecloc);

        usuario.setText(peticion.getUsuario());
        turno.setValue(peticion.getTurno());

        descripcion.setText(peticion.getDescripcion());
    }

}

