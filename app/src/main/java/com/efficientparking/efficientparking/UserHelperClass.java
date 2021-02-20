package com.efficientparking.efficientparking;

public class UserHelperClass {

    String nome, username, email, telefono, password, targa;

    public UserHelperClass(String nome, String username, String email, String telefono, String password, String targa) {
        this.nome = nome;
        this.username = username;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
        this.targa = targa;
    }

    public UserHelperClass() {

    }

    public String getName() {
        return nome;
    }

    public void setName(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return telefono;
    }

    public void setPhonenumber(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }
}
