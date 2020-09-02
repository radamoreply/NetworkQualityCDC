package com.example.android.networkqualitycdc.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;

import services.BankRepository;
import services.BankRepositoryDefault;

public class BonificoActivity extends AppCompatActivity {

    private AppCompatEditText iban;
    private AppCompatEditText intestatario;
    private AppCompatEditText importo;
    private AppCompatEditText causale;
    private AppCompatCheckBox istantaneo;
    private AppCompatButton conferma;
    private BankRepository bankRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonifico);
        bankRepository = new BankRepositoryDefault(this);
        init();
    }

    public static Intent openBonificoIntent(Context context) {
        Intent intent = new Intent(context, BonificoActivity.class);
        return intent;
    }

    private void init() {
        iban = findViewById(R.id.iban_et);
        intestatario = findViewById(R.id.intestatario_et);
        importo = findViewById(R.id.importo_et);
        causale = findViewById(R.id.causale_et);
        istantaneo = findViewById(R.id.istantaneo_cb);
        conferma = findViewById(R.id.confirm_button);
        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFieldsAndDoBonifico(v.getContext());
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Bonifico");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void checkFieldsAndDoBonifico(Context context) {
        try {
            bankRepository.makeBonifico(iban.getText().toString(),intestatario.getText().toString(),importo.getText().toString());
            if(istantaneo.isChecked()){
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(R.string.bonifico_title);
                alertDialog.setMessage(getResources().getString(R.string.bonifico_instant_completed));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                onBackPressed();
                            }
                        });
                alertDialog.show();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(R.string.bonifico_title);
                alertDialog.setMessage(getResources().getString(R.string.bonifico_completed));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                onBackPressed();
                            }
                        });
                alertDialog.show();
            }
        }catch (Exception e){
            if (e.getMessage().equals("Campi vuoti")) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle(R.string.bonifico_title);
                alertDialog.setMessage(getResources().getString(R.string.bonifico_mandatory_fields));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
