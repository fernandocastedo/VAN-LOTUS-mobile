package com.example.vanlotus.model;

public class LoginResponse {
    private boolean success;
    private String message;
    private LoginData data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public LoginData getData() { return data; }

    public static class LoginData {
        private String token;
        private Usuario usuario;
        private int expires_in;

        public String getToken() { return token; }
        public Usuario getUsuario() { return usuario; }
        public int getExpires_in() { return expires_in; }
    }

    public static class Usuario {
        private int usuario_id;
        private String nombre_usuario;

        public int getUsuario_id() { return usuario_id; }
        public String getNombre_usuario() { return nombre_usuario; }
    }
} 