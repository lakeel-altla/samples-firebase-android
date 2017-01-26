package com.example.firebase.custom.authentication.data;

public final class User {

    public String userName;

    public String email;

    @Override
    public String toString() {
        return "userName:" + userName + " email:" + email;
    }
}
