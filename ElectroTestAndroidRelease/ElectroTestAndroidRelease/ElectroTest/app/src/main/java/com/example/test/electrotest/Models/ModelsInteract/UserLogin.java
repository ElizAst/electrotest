package com.example.test.electrotest.Models.ModelsInteract;

public class UserLogin {

    public UserLogin(String login, String password) {
        Login = login;
        Password = password;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String Login) {
        Login = Login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String Login;
    private String Password;
}
