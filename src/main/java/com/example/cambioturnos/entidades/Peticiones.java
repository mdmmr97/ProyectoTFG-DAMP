package com.example.cambioturnos.entidades;

import org.bson.types.ObjectId;

public class Peticiones {
    private ObjectId id;
    private ObjectId grupo;
    private String fechapeticion;
    private String fechaturno;
    private String usuario;
    private String turno;
    private String descripcion;
    private String estado;

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getGrupo() {
        return grupo;
    }
    public void setGrupo(ObjectId grupo) {
        this.grupo = grupo;
    }

    public String getFechapeticion() {
        return fechapeticion;
    }
    public void setFechapeticion(String fechapeticion) {
        this.fechapeticion = fechapeticion;
    }

    public String getFechaturno() {
        return fechaturno;
    }
    public void setFechaturno(String fechaturno) {
        this.fechaturno = fechaturno;
    }

    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTurno() {
        return turno;
    }
    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
