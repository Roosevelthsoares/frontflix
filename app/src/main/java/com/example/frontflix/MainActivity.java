package com.example.frontflix;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText emailEditText, senhaEditText;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        senhaEditText = findViewById(R.id.editTextTextPassword);
        databaseManager = new DatabaseManager(this);

        Button loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autenticarUsuario();
            }
        });

        TextView cadastreSe = findViewById(R.id.cadastrese);
        cadastreSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });
    }

    private void autenticarUsuario() {
        String email = emailEditText.getText().toString();
        String senha = senhaEditText.getText().toString();

        if (databaseManager.authenticateUser(email, senha)) {
            Toast.makeText(MainActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HelloActivity.class);
            startActivity(intent);
            finish(); // Finaliza a MainActivity para que o usuário não possa voltar para a tela de login com o botão de voltar
        } else {
            Toast.makeText(MainActivity.this, "Erro ao autenticar. Verifique suas credenciais.", Toast.LENGTH_SHORT).show();
        }
    }
}