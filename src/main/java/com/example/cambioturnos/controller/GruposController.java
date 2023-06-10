package com.example.cambioturnos.controller;

import com.example.cambioturnos.Main;
import com.example.cambioturnos.MainSingleton;
import com.example.cambioturnos.UserSingleton;
import com.example.cambioturnos.entidades.Grupos;
import com.example.cambioturnos.entidades.Usuarios;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GruposController implements Initializable {

    @FXML
    private TableColumn<Grupos, String> listaGrupos;
    @FXML
    private Label nombreUser;

    private UserSingleton instanceUser;
    private MainSingleton instanceMain;
    private Usuarios usuario;
    private ObservableList<Grupos> grupos;
    private Stage myStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instanceUser = UserSingleton.getInstance();
        usuario = instanceUser.getUsuarioLogin();
        /*grupos = instanceMain.getListagrupos();

        instanceMain = MainSingleton.getInstance();

        nombreUser.setText(usuario.getCorreo());

        ObservableList<Grupos> gruposfiltrados = grupos.filtered(grupos -> usuario.getGrupos().contains(grupos.getId()));
        for (Grupos grup:gruposfiltrados){
            System.out.println(grup.getNombre());
        }

        listaGrupos.setCellValueFactory(new PropertyValueFactory<>("nombre"));*/
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
    void abandonarGrupo(ActionEvent event) {

    }

    @FXML
    void crearGrupo(ActionEvent event) {

    }

    @FXML
    void eliminarGrupo(ActionEvent event) {

    }

    @FXML
    void irPeticion(ActionEvent event) {

    }

    @FXML
    void unirseGrupo(ActionEvent event) {

    }

}

