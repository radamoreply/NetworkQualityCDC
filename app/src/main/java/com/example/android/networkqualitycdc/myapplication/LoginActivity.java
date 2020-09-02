package com.example.android.networkqualitycdc.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import services.BankRepositoryDefault;

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton button;
    private AppCompatEditText nomeUtente;
    private AppCompatEditText password;
    private AppCompatTextView label;
    private BankRepositoryDefault bankRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bankRepository = new BankRepositoryDefault(this);
        init();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
        }
    }

    private void init() {
        nomeUtente = findViewById(R.id.nome_utente);
        password = findViewById(R.id.password);
        label = findViewById(R.id.label);
        button = findViewById(R.id.confirm_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFieldsOk()) {
                    resetFields();
                    handleIntent();
                }
            }
        });
    }


    private void handleIntent() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private boolean isFieldsOk() {
        try{
            bankRepository.makeLogin(nomeUtente.getText().toString(),password.getText().toString());
            return true;
        }catch (Exception loginException){
            if(loginException.getMessage().equals("Campi vuoti")){
                label.setText(R.string.label_empty_text);
            }
            if(loginException.getMessage().equals("Credenziali non corrette")){
                label.setText(R.string.label_wrong_text);
            }
        }
        return false;
    }

    private void resetFields() {
        nomeUtente.setText("");
        password.setText("");
        label.setText("");
    }

}
