package com.example.android.networkqualitycdc.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import services.BankRepositoryDefault;

public class LoginActivity extends AppCompatActivity {

    public static String KEY_CONNECTION_SPEED = "KEY_CONNECTION_SPEED";

    private AppCompatButton button;
    private AppCompatEditText nomeUtente;
    private AppCompatEditText password;
    private AppCompatTextView label;
    private TextView speedValue;
    private BankRepositoryDefault bankRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bankRepository = new BankRepositoryDefault(this);
        init();
        ChooseConnectivityCheckActivity.SPEED speedResult = (ChooseConnectivityCheckActivity.SPEED) getIntent().getSerializableExtra(KEY_CONNECTION_SPEED);
        String text = speedResult.toString() + " SPEED";
        speedValue.setText(text);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
        }
    }

    private void init() {
        nomeUtente = findViewById(R.id.nome_utente);
        password = findViewById(R.id.password);
        label = findViewById(R.id.label);
        speedValue = findViewById(R.id.speed_value);
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

    public static Intent createOpenIntent(Context context, ChooseConnectivityCheckActivity.SPEED connectionSpeed) {
        Intent i = new Intent(context, LoginActivity.class);
        i.putExtra(KEY_CONNECTION_SPEED, connectionSpeed);
        return i;
    }

    private void handleIntent() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private boolean isFieldsOk() {
        try {
            bankRepository.makeLogin(nomeUtente.getText().toString(), password.getText().toString());
            return true;
        } catch (Exception loginException) {
            if (loginException.getMessage().equals("Campi vuoti")) {
                label.setText(R.string.label_empty_text);
            }
            if (loginException.getMessage().equals("Credenziali non corrette")) {
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
