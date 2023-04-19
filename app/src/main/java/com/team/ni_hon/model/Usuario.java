package com.team.ni_hon.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nombre;
    private String correo;
    private String contrasenia;
    private int icono;
    private int nivel;

    public Usuario (){}

    public Usuario(String _nombre,String _correo,String _contrasenia,int _icono,int _nivel){

        this.nombre=_nombre;
        this.correo=_correo;
        this.contrasenia=_contrasenia;
        this.icono=_icono;
        this.nivel=_nivel;
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

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
