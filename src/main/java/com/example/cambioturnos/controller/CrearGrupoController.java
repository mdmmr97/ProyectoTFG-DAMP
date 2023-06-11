package com.example.cambioturnos.controller;

import com.example.cambioturnos.Main;
import com.example.cambioturnos.MainSingleton;
import com.example.cambioturnos.UserSingleton;
import com.example.cambioturnos.entidades.Grupos;
import com.example.cambioturnos.entidades.Usuarios;
import com.mongodb.client.MongoCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class CrearGrupoController implements Initializable {
    @FXML
    private Button borrarTurno;
    @FXML
    private ListView<String> tablaTurnos;
    @FXML
    private TextField nombre;
    @FXML
    private TextField turno;

    private MainSingleton instanceMain;
    private UserSingleton instanceUser;
    private MongoCollection<Grupos> coleccionGrupos;
    private ObservableList<Grupos> listagrupos;
    private Usuarios usuario;
    private Grupos grupo;
    private int idTurno;
    private ObservableList<String> listaTurnos = FXCollections.observableArrayList();
    private List<String> listaUser = new ArrayList<>();
    private static BorderPane nodo;

    private String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int LENGTH = 5;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instanceMain = MainSingleton.getInstance();
        coleccionGrupos = instanceMain.getColeccionGrupos();
        listagrupos = instanceMain.getListagrupos();

        instanceUser = UserSingleton.getInstance();
        usuario = instanceUser.getUsuarioLogin();

        tablaTurnos.setItems(listaTurnos);
    }

    @FXML
    void AñadirTurno(ActionEvent event) {
        listaTurnos.add(turno.getText());
        turno.setText("");
    }

    @FXML
    void EliminarTurno(ActionEvent event) {
        listaTurnos.remove(idTurno);
        if (listaTurnos.size() == 1) borrarTurno.setDisable(true);
    }

    @FXML
    void SeleccionarTurno(MouseEvent event) {
        idTurno = tablaTurnos.getSelectionModel().getSelectedIndex();
        borrarTurno.setDisable(false);
    }

    public void CrearGrupo(ActionEvent actionEvent) {
        try {
            grupo = new Grupos();
            grupo.setNombre(nombre.getText());
            grupo.setAdmin(usuario.getCorreo());
            grupo.setCodigo(GenerarCodigoGrupo());
            grupo.setTurnos(listaTurnos);
            if (listaUser.size() == 0){
                listaUser.add(usuario.getCorreo());
                grupo.setUsuarios(listaUser);
            }
            grupo.setUsuarios(listaUser);

            if (listaTurnos.size()>1 && !nombre.getText().equals("")) {
                coleccionGrupos.insertOne(grupo);
                listagrupos.add(grupo);
                instanceUser.setGrupo(grupo);

                nodo = instanceUser.getNodo();
                FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Peticiones.fxml"));
                nodo.setCenter(fxmlloader.load());
            } else if (nombre.getText().equals("")) {
                nombre.getStyleClass().add("-fx-border-color: red");
            } else{
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Alerta");
                alerta.setHeaderText("Información importante");
                alerta.setContentText("Es necesario que la lista de turnos cuente con al menos 2 turnos");
                alerta.showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Cerrar(ActionEvent actionEvent) {
        try {
            Stage myStage = instanceMain.getStage();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Grupos.fxml"));
            nodo = fxmlloader.load();
            instanceUser.setNodo(nodo);
            Scene escena2 = new Scene(nodo);
            myStage.setTitle("Intercambiar Turnos");
            myStage.setScene(escena2);
            myStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String GenerarCodigoGrupo(){
        String cadena = "";
        Random random = new Random();
        for (int i = 0; i < LENGTH; i++){
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            cadena = cadena + randomChar;
        }
        return cadena;
    }
}

