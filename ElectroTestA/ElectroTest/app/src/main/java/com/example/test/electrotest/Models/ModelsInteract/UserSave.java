package com.example.test.electrotest.Models.ModelsInteract;

public class UserSave {
    private int id;
    private String nickname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserSave(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
