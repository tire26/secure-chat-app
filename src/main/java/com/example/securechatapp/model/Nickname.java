package com.example.securechatapp.model;

import lombok.Getter;

@Getter
public class Nickname {

    private String nickname;

    public Nickname() {
    }

    public Nickname(String nickname) {
        this.nickname = nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
