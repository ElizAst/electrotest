package com.example.test.electrotest.Models.ModelsInteract;

public class RegisterModel {

    public RegisterModel(String login, String password, String passwordConfirmed) {
        Login = login;
        Password = password;
        PasswordConfirmed = passwordConfirmed;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPasswordConfirmed() {
        return PasswordConfirmed;
    }

    public void setPasswordConfirmed(String passwordConfirmed) {
        PasswordConfirmed = passwordConfirmed;
    }

    private String Login;
    private String Password;
    private String PasswordConfirmed;
}
