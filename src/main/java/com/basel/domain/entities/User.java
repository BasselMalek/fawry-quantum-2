package com.basel.domain.entities;

public abstract class User {
    String username;
    String email;
    int hashedPassword;

    public User( String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.setPassword(password);
    }

    String getUsername(){
        return this.username;
    }
    String getEmail(){
        return this.email;
    }

    void setUsername(String newName){
        this.username = newName;
    }
    void setEmail(String newMail){
        this.email = newMail;
    }
    void setPassword(String plainPassword){
        this.hashedPassword = plainPassword.hashCode();
    }

    public boolean verifyLogin(String email, String inputPassword) {
        return this.email.equals(email) && this.hashedPassword == inputPassword.hashCode();
    }
}
