package com.example.cambioturnos.controller.Grupos;

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
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InformacionGrupoController implements Initializable {

    @FXML
    private TextField admin;
    @FXML
    private Button borrarUser;
    @FXML
    private TextField codigo;
    @FXML
    private ListView<String> miembros;
    @FXML
    private TextField nombre;

    private UserSingleton instanceUser;
    private MainSingleton instanceMain;
    private Usuarios usuario;
    private Grupos grupo;
    private MongoCollection<Usuarios> coleccionUser;
    private MongoCollection<Grupos> coleccionGrupos;
    private ObservableList<Usuarios> listausuarios;
    private ObservableList<Grupos> listagrupos;
    private BorderPane nodo;

    private String usuarioSelect;
    private ObservableList<String> listamiembros;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instanceMain = MainSingleton.getInstance();
        instanceUser = UserSingleton.getInstance();

        usuario = instanceUser.getUsuarioLogin();
        grupo = instanceUser.getGrupo();
        coleccionUser = instanceMain.getColeccionUser();
        coleccionGrupos = instanceMain.getColeccionGrupos();
        listausuarios = instanceMain.getListauser();
        listagrupos = instanceMain.getListagrupos();

        nombre.setText(grupo.getNombre());
        codigo.setText(grupo.getCodigo());
        admin.setText(grupo.getAdmin());

        miembros.setDisable(true);
        borrarUser.setDisable(true);
        borrarUser.setVisible(false);

        listamiembros = FXCollections.observableArrayList(grupo.getUsuarios());
         miembros.setItems(listamiembros);

        if (usuario.getCorreo().equals(grupo.getAdmin())) {
            miembros.setDisable(false);
            borrarUser.setVisible(true);
        }
    }

    public void SeleccionarUsuario(MouseEvent mouseEvent) {
        usuarioSelect = miembros.getSelectionModel().getSelectedItem();
        borrarUser.setDisable(false);
    }
    @FXML
    void BorrarUsuarios(ActionEvent event) {

        Usuarios userdel = new Usuarios();
        for(Usuarios user : listausuarios){
            if (user.getCorreo().equals(usuarioSelect)) userdel = user;
        }

        Document viejouser = new Document("correo",userdel.getCorreo());
        Document viejogrupo = new Document("codigo",grupo.getCodigo());

        userdel.getGrupos().remove(grupo.getId());
        grupo.getUsuarios().remove(userdel.getCorreo());

        coleccionUser.replaceOne(viejouser, userdel);
        coleccionGrupos.replaceOne(viejogrupo, grupo);

        borrarUser.setDisable(true);
        listamiembros.remove(usuarioSelect);
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

