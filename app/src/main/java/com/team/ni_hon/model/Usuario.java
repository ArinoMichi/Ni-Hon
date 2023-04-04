package com.team.ni_hon.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    long id;
    String nombre;
    String correo;
    String contrasenia;
    byte icono;
    int nivel;

    public Usuario (){}

    public Usuario(long _id,String _nombre,String _correo,String _contrasenia,byte _icono,int _nivel){
        this.id=_id;
        this.nombre=_nombre;
        this.correo=_correo;
        this.contrasenia=_contrasenia;
        this.icono=_icono;
        this.nivel=_nivel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public byte getIcono() {
        return icono;
    }

    public void setIcono(byte icono) {
        this.icono = icono;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
