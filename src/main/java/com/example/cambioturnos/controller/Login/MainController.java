package com.example.cambioturnos.controller.Login;

import com.example.cambioturnos.Main;
import com.example.cambioturnos.MainSingleton;
import com.example.cambioturnos.UserSingleton;
import com.example.cambioturnos.entidades.Usuarios;
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
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField correo;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Button registro;

    private ObservableList<Usuarios> listauser;
    private MessageDigest md;
    private byte[] concif;
    private MainSingleton instanceMain;
    private UserSingleton instanceUser;

    private static BorderPane nodo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instanceMain = MainSingleton.getInstance();
        listauser = instanceMain.getListauser();

        instanceUser = UserSingleton.getInstance();
    }

    @FXML
    void Registrarse(ActionEvent event) {
        try{
            Stage myStage = instanceMain.getStage();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Login/Registro.fxml"));
            Scene escena2 = new Scene(fxmlloader.load());
            myStage.setTitle("Registrar Usuario");
            myStage.setScene(escena2);
            myStage.show();

        } catch (Exception e){
            System.out.println("error Usuario");
        }
    }

    @FXML
    void comprobarLogin(ActionEvent event) {
        try{
            String email = correo.getText();
            Usuarios usuario = listauser.stream()
                                .filter(usuarios -> usuarios.getCorreo().equals(email))
                                .findFirst().orElse(null);

            if (usuario == null){
                correo.getStyleClass().add("error-user");
            }
            else{

                if (BCrypt.checkpw(password.getText(), usuario.getPassword())) {

                    instanceUser.setUsuarioLogin(usuario);
                    Stage myStage = instanceMain.getStage();
                    FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Grupo/Grupos.fxml"));
                    nodo = fxmlloader.load();
                    instanceUser.setNodo(nodo);
                    Scene escena2 = new Scene(nodo);
                    myStage.setTitle("Intercambiar Turnos");
                    myStage.setScene(escena2);
                    myStage.show();

                }
                else{
                    password.getStyleClass().add("error-user");
                }
            }
        } catch (Exception e){

        }
    }
}
