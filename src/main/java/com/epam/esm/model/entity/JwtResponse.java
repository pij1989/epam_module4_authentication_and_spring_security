package com.epam.esm.model.entity;

public class JwtResponse {
    private final String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JwtResponse{");
        sb.append("token='").append(token).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
