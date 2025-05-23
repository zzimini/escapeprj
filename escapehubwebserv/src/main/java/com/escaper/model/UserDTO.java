package com.escaper.model;

public class UserDTO {
    private String userId;
    private String password;

    public UserDTO(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getPassword() { return password; }
}
