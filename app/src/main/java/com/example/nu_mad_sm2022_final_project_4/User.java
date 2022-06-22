package com.example.nu_mad_sm2022_final_project_4;

public class User {
    private String displayName;
    private String email;

    public User() {

    }

    public User(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }
}
