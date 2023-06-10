package com.example.cambioturnos.controller;

import com.example.cambioturnos.Main;
import com.example.cambioturnos.MainSingleton;
import com.example.cambioturnos.UserSingleton;
import com.example.cambioturnos.entidades.Usuarios;
import com.mongodb.client.MongoCollection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroController implements Initializable {

    @FXML
    private TextField correo;
    @FXML
    private Button login;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField repassword;

    private MongoCollection<Usuarios> coleccionUser;
    private ObservableList<Usuarios> listauser;
    private MainSingleton instanceMain;
    private UserSingleton instanceUser;
    private static BorderPane nodo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instanceMain = MainSingleton.getInstance();
        coleccionUser = instanceMain.getColeccionUser();
        listauser = instanceMain.getListauser();

        instanceUser = UserSingleton.getInstance();
    }

    @FXML
    void LoginUsuario(ActionEvent event) {
        Usuarios newuser = new Usuarios();
        newuser.setCorreo(correo.getText());
        if (password.getText().equals(repassword.getText())) {
            newuser.setPassword(password.getText());

            coleccionUser.insertOne(newuser);
            listauser.add(newuser);
            instanceMain.setListauser(listauser);

            instanceUser.setUsuarioLogin(newuser);

            CambiarEscena();
        }
        else{
            repassword.getStyleClass().add("error-user");
        }
    }

    private void CambiarEscena(){
        try{
            Stage myStage = instanceMain.getStage();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Grupos.fxml"));
            nodo = fxmlloader.load();
            instanceUser.setNodo(nodo);
            Scene escena2 = new Scene(nodo);
            myStage.setTitle("Intercambiar Turnos");
            myStage.setScene(escena2);
            myStage.show();
        } catch (Exception e){
            System.out.println("Error grupos");
        }
    }
}
