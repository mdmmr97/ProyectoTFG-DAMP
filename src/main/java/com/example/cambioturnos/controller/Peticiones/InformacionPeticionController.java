package com.example.cambioturnos.controller.Peticiones;

import com.example.cambioturnos.Main;
import com.example.cambioturnos.MandarCorreo;
import com.example.cambioturnos.UserSingleton;
import com.example.cambioturnos.entidades.Peticiones;
import com.example.cambioturnos.entidades.Usuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class InformacionPeticionController implements Initializable {

    @FXML
    private TextArea descripcion;
    @FXML
    private ToggleButton estadoAbierto;
    @FXML
    private ToggleButton estadoCerrado;
    @FXML
    private TextField fechapeticion;
    @FXML
    private TextField fechaturno;
    @FXML
    private TextField usuario;
    @FXML
    private TextField turno;
    @FXML
    private Button respuesta;

    private UserSingleton instanceUser;
    private Peticiones peticion;
    private Usuarios usuarioLogin;
    private BorderPane nodo;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        instanceUser = UserSingleton.getInstance();
        peticion = instanceUser.getPeticion();
        usuarioLogin = instanceUser.getUsuarioLogin();

        fechapeticion.setText(peticion.getFechapeticion());
        fechaturno.setText(peticion.getFechaturno());
        usuario.setText(peticion.getUsuario());
        turno.setText(peticion.getTurno());
        descripcion.setText(peticion.getDescripcion());

        ToggleGroup estado = new ToggleGroup();
        estadoAbierto.setToggleGroup(estado);
        estadoCerrado.setToggleGroup(estado);

        if (peticion.getEstado().equals("ABIERTA")) {
            estadoAbierto.setSelected(true);
            estadoCerrado.setDisable(true);
        }
        if (peticion.getEstado().equals("CERRADA")) {
            estadoCerrado.setSelected(true);
            estadoAbierto.setDisable(true);
        }

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

        if (usuarioLogin.getCorreo().equals(peticion.getUsuario())) respuesta.setDisable(true);

    }

    @FXML
    void EnviarCorreo(ActionEvent event) {

        instanceUser.setTipoCorreo("RespuestaPeticion");
        MandarCorreo correo = new MandarCorreo();
        //correo.HacerCorreo();

        try {
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Peticiones.fxml"));
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
