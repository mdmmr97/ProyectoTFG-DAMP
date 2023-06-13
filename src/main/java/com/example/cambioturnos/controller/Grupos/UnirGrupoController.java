package com.example.cambioturnos.controller.Grupos;

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
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UnirGrupoController implements Initializable {

    @FXML
    private TextField codigo;

    private UserSingleton instanceUser;
    private MainSingleton instanceMain;
    private Usuarios usuario;
    private Grupos grupo;
    private MongoCollection<Usuarios> coleccionUser;
    private MongoCollection<Grupos> coleccionGrupos;
    private ObservableList<Usuarios> listausuarios;
    private ObservableList<Grupos> listagrupos;
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

        codigo.textProperty().addListener((observable, viejo, nuevo) ->{
            if(!nuevo.matches("[A-Za-z0-9]{0,5}")){
                codigo.setText(viejo);
            }
        });
    }

    @FXML
    void UnirseGrupo(ActionEvent event) {
        grupo = listagrupos.stream()
                .filter(grupo -> grupo.getCodigo().equals(codigo.getText()))
                .findFirst().orElse(null);

        if (grupo != null){
            Document viejouser = new Document("correo",usuario.getCorreo());
            Document viejogrupo = new Document("codigo",grupo.getCodigo());

            usuario.getGrupos().add(grupo.getId());
            grupo.getUsuarios().add(usuario.getCorreo());

            coleccionUser.replaceOne(viejouser,usuario);
            coleccionGrupos.replaceOne(viejogrupo,grupo);

            listausuarios.removeIf(usuarios -> usuarios.getCorreo().equals(usuario.getCorreo()));
            listausuarios.add(usuario);
            listagrupos.removeIf(grupos -> grupos.getCodigo().equals(grupo.getCodigo()));
            listagrupos.add(grupo);

            CambiarEscena();
        }
        else{
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Alerta");
            alerta.setHeaderText("Información importante");
            alerta.setContentText("El codigo de grupo que ha introducido no esta ligado a ninguno de los grupos " +
                                  "de los que se disponen en la base de datos");
            alerta.showAndWait();
        }
    }
    @FXML
    void VolverGrupos(ActionEvent event) {
        try {
            Stage myStage = instanceMain.getStage();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Grupo/Grupos.fxml"));
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
    private void CambiarEscena(){
        try{
            instanceUser.setGrupo(grupo);
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Peticiones/Peticiones.fxml"));
            nodo.setCenter(fxmlloader.load());

        } catch (Exception e){
            System.out.println("error Peticion");
        }
    }

}
