package com.example.cambioturnos.controller;

import com.example.cambioturnos.MainSingleton;
import com.example.cambioturnos.UserSingleton;
import com.example.cambioturnos.entidades.Grupos;
import com.example.cambioturnos.entidades.Usuarios;
import com.mongodb.client.MongoCollection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class CrearGrupoController implements Initializable {
    @FXML
    private Button borrarTurno;
    @FXML
    private ListView<String> listaTurnos;
    @FXML
    private TextField nombre;
    @FXML
    private TextField turno;

    private MainSingleton instanceMain;
    private UserSingleton instanceUser;
    private MongoCollection<Usuarios> coleccionUser;
    private MongoCollection<Grupos> coleccionGrupos;
    private ObservableList<Usuarios> listausuarios;
    private ObservableList<Grupos> listagrupos;
    private Usuarios usuario;
    private Grupos grupo;
    private int idTurno;
    private ObservableList<String> listTurn;

    private String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int LENGTH = 5;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instanceMain = MainSingleton.getInstance();
        instanceUser = UserSingleton.getInstance();
        coleccionUser = instanceMain.getColeccionUser();
        coleccionGrupos = instanceMain.getColeccionGrupos();
        listausuarios = instanceMain.getListauser();
        listagrupos = instanceMain.getListagrupos();

        usuario = instanceUser.getUsuarioLogin();
        listaTurnos.setItems(listTurn);
    }

    @FXML
    void AñadirTurno(ActionEvent event) {
        listTurn.add(turno.getText());
        listaTurnos.setItems(listTurn);
    }

    @FXML
    void EliminarTurno(ActionEvent event) {
        listTurn.remove(idTurno);
        listaTurnos.setItems(listTurn);
    }

    @FXML
    void SeleccionarTurno(MouseEvent event) {
        idTurno = listaTurnos.getSelectionModel().getSelectedIndex();
    }

    public void CrearGrupo(ActionEvent actionEvent) {
        grupo.setNombre(nombre.getText());
        grupo.setAdmin(usuario.getCorreo());
        grupo.setCodigo(GenerarCodigoGrupo());
        grupo.setTurnos(listTurn);
        grupo.getUsuarios().add(usuario.getCorreo());

        coleccionGrupos.insertOne(grupo);
        listagrupos.add(grupo);
        instanceUser.setGrupo(grupo);

        
    }

    public void Cerrar(ActionEvent actionEvent) {

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

