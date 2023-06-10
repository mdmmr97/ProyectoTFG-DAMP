package com.example.cambioturnos.entidades;

import org.bson.types.ObjectId;

import java.util.List;

public class Usuarios {
    private ObjectId id;
    private String correo;
    //private Byte[] password;
    private String password;
    private List<ObjectId> grupos;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

   /*public Byte[] getPassword() {
        return password;
    }

    public void setPassword(Byte[] password) {
        this.password = password;
    }*/
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ObjectId> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<ObjectId> grupos) {
        this.grupos = grupos;
    }
}
