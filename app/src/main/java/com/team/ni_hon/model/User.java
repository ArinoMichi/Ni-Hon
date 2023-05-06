package com.team.ni_hon.model;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String email;
    private String password;
    private int icon;
    private int level;

    public User(){}

    public User(String _nombre, String _correo, String _contrasenia, int _icono, int _nivel){

        this.name=_nombre;
        this.email=_correo;
        this.password=_contrasenia;
        this.icon=_icono;
        this.level=_nivel;
    }


    public String getNombre() {
        return name;
    }

    public void setNombre(String nombre) {
        this.name = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String _email) {
        this.email = _email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String _password) {
        this.password = _password;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int _icon) {
        this.icon = _icon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int _level) {
        this.level = _level;
    }
}
