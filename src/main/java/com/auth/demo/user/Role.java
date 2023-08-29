package com.auth.demo.user;

public enum Role {
    USER("user"),
    ADMIN("admin"),
    EXTERNAL("external");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
