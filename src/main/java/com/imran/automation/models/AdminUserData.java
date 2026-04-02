package com.imran.automation.models;

public class AdminUserData {

    private final String username;
    private final String password;
    private final String userRole;
    private final String status;

    public AdminUserData(String username, String password, String userRole, String status) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getStatus() {
        return status;
    }
}
