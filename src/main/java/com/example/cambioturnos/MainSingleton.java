package com.example.cambioturnos;

import com.example.cambioturnos.entidades.Grupos;
import com.example.cambioturnos.entidades.Peticiones;
import com.example.cambioturnos.entidades.Usuarios;
import com.mongodb.client.MongoCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class MainSingleton {

    private static MainSingleton instance;
    private Stage stage;
    private ObservableList<Usuarios> listauser;
    private ObservableList<Grupos> listagrupos;
    private ObservableList<Peticiones> listapeticiones;

    private MongoCollection<Usuarios> coleccionUser;
    private MongoCollection<Grupos> coleccionGrupos;
    private MongoCollection<Peticiones> coleccionPeticiones;

    private MainSingleton(){
        listauser = FXCollections.observableArrayList();
        listagrupos = FXCollections.observableArrayList();
        listapeticiones = FXCollections.observableArrayList();
    }
    public static MainSingleton getInstance(){
        if(instance == null) instance = new MainSingleton();
        return instance;
    }

    public ObservableList<Usuarios> getListauser() {
        return listauser;
    }
    public void setListauser(ObservableList<Usuarios> listauser) {
        this.listauser = listauser;
    }

    public ObservableList<Grupos> getListagrupos() {
        return listagrupos;
    }
    public void setListagrupos(ObservableList<Grupos> listagrupos) {
        this.listagrupos = listagrupos;
    }

    public ObservableList<Peticiones> getListapeticiones() {
        return listapeticiones;
    }
    public void setListapeticiones(ObservableList<Peticiones> listapeticiones) {
        this.listapeticiones = listapeticiones;
    }

    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public MongoCollection<Usuarios> getColeccionUser() {
        return coleccionUser;
    }

    public void setColeccionUser(MongoCollection<Usuarios> coleccionUser) {
        this.coleccionUser = coleccionUser;
    }

    public MongoCollection<Grupos> getColeccionGrupos() {
        return coleccionGrupos;
    }

    public void setColeccionGrupos(MongoCollection<Grupos> coleccionGrupos) {
        this.coleccionGrupos = coleccionGrupos;
    }

    public MongoCollection<Peticiones> getColeccionPeticiones() {
        return coleccionPeticiones;
    }

    public void setColeccionPeticiones(MongoCollection<Peticiones> coleccionPeticiones) {
        this.coleccionPeticiones = coleccionPeticiones;
    }
}
