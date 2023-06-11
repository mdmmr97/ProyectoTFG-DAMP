package com.example.cambioturnos.controller;

import com.example.cambioturnos.Main;
import com.example.cambioturnos.MainSingleton;
import com.example.cambioturnos.UserSingleton;
import com.example.cambioturnos.entidades.Grupos;
import com.example.cambioturnos.entidades.Usuarios;
import com.mongodb.client.MongoCollection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GruposController implements Initializable {

    @FXML
    private Button añadirGrup;
    @FXML
    private Button dejarGrup;
    @FXML
    private Button eliminarGrup;
    @FXML
    private Button peticiones;
    @FXML
    private Button unirseGrup;
    @FXML
    private TableView<Grupos> tablagrupos;
    @FXML
    private TableColumn<Grupos, String> listaGrupos;
    @FXML
    private Label nombreUser;

    private UserSingleton instanceUser;
    private MainSingleton instanceMain;
    private Usuarios usuario;
    private Grupos grupo;
    private MongoCollection<Usuarios> coleccionUser;
    private MongoCollection<Grupos> coleccionGrupos;
    private ObservableList<Usuarios> listausuarios;
    private ObservableList<Grupos> listagrupos;
    private Stage myStage;
    private BorderPane nodo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instanceMain = MainSingleton.getInstance();
        instanceUser = UserSingleton.getInstance();

        usuario = instanceUser.getUsuarioLogin();
        coleccionUser = instanceMain.getColeccionUser();
        coleccionGrupos = instanceMain.getColeccionGrupos();
        listausuarios = instanceMain.getListauser();
        listagrupos = instanceMain.getListagrupos();

        nombreUser.setText(usuario.getCorreo());

        dejarGrup.setDisable(true);
        dejarGrup.setVisible(false);
        eliminarGrup.setDisable(true);
        eliminarGrup.setVisible(false);
        peticiones.setDisable(true);
        peticiones.setVisible(false);

        ActualizarGrupos();

        ObservableList<Grupos> gruposfiltrados = listagrupos.filtered(grupos -> usuario.getGrupos().contains(grupos.getId()));
        listaGrupos.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tablagrupos.setItems(gruposfiltrados);
    }

    @FXML
    void desloguearse(ActionEvent event) {
        try{
            instanceUser.setUsuarioLogin(null);
            instanceUser.setGrupo(null);

            myStage = instanceMain.getStage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            myStage.setTitle("Login");
            myStage.setScene(scene);
            myStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void crearGrupo(ActionEvent event) {
        try {
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("CrearGrupo.fxml"));
            nodo.setCenter(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void unirseGrupo(ActionEvent event) {
        try {
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("UnirGrupo.fxml"));
            nodo.setCenter(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void abandonarGrupo(ActionEvent event) {

    }
    @FXML
    void eliminarGrupo(ActionEvent event) {

    }

    @FXML
    void irPeticion(ActionEvent event) {
        try{
            instanceUser.setGrupo(grupo);
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Peticiones.fxml"));
            nodo.setCenter(fxmlloader.load());

        } catch (Exception e){
            System.out.println("error Peticion");
        }
    }

    public void SeleccionarGrupo(MouseEvent mouseEvent) {
        peticiones.setDisable(false);
        peticiones.setVisible(true);
        dejarGrup.setDisable(false);
        dejarGrup.setVisible(true);

        grupo = tablagrupos.getSelectionModel().getSelectedItem();
        if (grupo.getAdmin().equals(usuario.getCorreo())){
            eliminarGrup.setDisable(false);
            eliminarGrup.setVisible(true);
        }
        else{
            eliminarGrup.setDisable(true);
            eliminarGrup.setVisible(false);
        }
    }

    private void ActualizarGrupos(){
        ObservableList<Grupos> listgrupuser = listagrupos.filtered(grupos -> grupos.getUsuarios().contains(usuario.getCorreo()));
        Document viejouser = new Document("correo",usuario.getCorreo());
        if (usuario.getGrupos() != null) usuario.getGrupos().clear();
        else {
            List<ObjectId> listagrupId = new ArrayList<>();
            usuario.setGrupos(listagrupId);
        }
        for (Grupos grupo:listgrupuser){
            usuario.getGrupos().add(grupo.getId());
        }
        coleccionUser.replaceOne(viejouser,usuario);
    }
}

