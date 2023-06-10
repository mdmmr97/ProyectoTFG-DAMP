package com.example.cambioturnos.entidades;

import org.bson.types.ObjectId;

import java.util.List;

public class Grupos {
    private ObjectId id;
    private String nombre;
    private List<String> usuarios;
    private String codigo;
    private String admin;
    private List<ObjectId> peticiones;
    private List<String> turnos;

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getUsuarios() {
        return usuarios;
    }
    public void setUsuarios(List<String> usuarios) {
        this.usuarios = usuarios;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAdmin() {
        return admin;
    }
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public List<ObjectId> getPeticiones() {
        return peticiones;
    }
    public void setPeticiones(List<ObjectId> peticiones) {
        this.peticiones = peticiones;
    }

    public List<String> getTurnos() {
        return turnos;
    }
    public void setTurnos(List<String> turnos) {
        this.turnos = turnos;
    }
}
