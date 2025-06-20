package com.example.vanlotus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.vanlotus.api.ApiClient;
import com.example.vanlotus.api.AuthApi;
import com.example.vanlotus.model.LoginRequest;
import com.example.vanlotus.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        ImageView togglePassword = findViewById(R.id.togglePassword);
        Button loginButton = findViewById(R.id.loginButton);

        togglePassword.setOnClickListener(v -> {
            passwordVisible = !passwordVisible;
            if (passwordVisible) {
                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            passwordEditText.setSelection(passwordEditText.getText().length());
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                if (nombreUsuario.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    login(nombreUsuario, password);
                }
            }
        });
    }

    // Llama a este método cuando el usuario presione el botón de login
    private void login(String nombreUsuario, String password) {
        AuthApi api = ApiClient.getClient().create(AuthApi.class);
        LoginRequest request = new LoginRequest(nombreUsuario, password);
        api.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    String token = response.body().getData().getToken();
                    // Guardar el token en SharedPreferences
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    prefs.edit().putString("jwt_token", token).apply();
                    // Puedes guardar también el usuario si lo necesitas
                    // Navega a la siguiente pantalla
                    Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String msg = (response.body() != null) ? response.body().getMessage() : "Error desconocido";
                    Toast.makeText(LoginActivity.this, "Error: " + msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} 