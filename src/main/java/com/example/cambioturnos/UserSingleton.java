package com.example.cambioturnos;

import com.example.cambioturnos.entidades.Grupos;
import com.example.cambioturnos.entidades.Peticiones;
import com.example.cambioturnos.entidades.Usuarios;
import javafx.scene.layout.BorderPane;

public class UserSingleton {
    private static UserSingleton instance;

    private Usuarios usuarioLogin;
    private Grupos grupo;
    private Peticiones peticion;
    private BorderPane nodo;
    private String tipoCorreo;

    private UserSingleton(){}
    public static UserSingleton getInstance(){
        if(instance == null) instance = new UserSingleton();
        return instance;
    }

    public Usuarios getUsuarioLogin() {
        return usuarioLogin;
    }
    public void setUsuarioLogin(Usuarios usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public Grupos getGrupo() {
        return grupo;
    }
    public void setGrupo(Grupos grupo) {
        this.grupo = grupo;
    }

    public Peticiones getPeticion() {
        return peticion;
    }
    public void setPeticion(Peticiones peticion) {
        this.peticion = peticion;
    }

    public BorderPane getNodo() {
        return nodo;
    }
    public void setNodo(BorderPane nodo) {
        this.nodo = nodo;
    }

    public String getTipoCorreo() {
        return tipoCorreo;
    }
    public void setTipoCorreo(String tipoCorreo) {
        this.tipoCorreo = tipoCorreo;
    }
}
