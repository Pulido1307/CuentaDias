package com.polar.industries.cuentadias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.proto.MaybeDocument;

public class SplashScreen extends AppCompatActivity {
    private ConstraintLayout fondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        fondo = findViewById(R.id.fondo);
        manejoTemaOscuro();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialogUser();
            }
        }, 2000);


    }

    private void showDialogUser(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view =inflater.inflate(R.layout.dialog_user, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        final MaterialButton materialButton_hermano  = dialog.findViewById(R.id.materialButton_hermano);
        final MaterialButton materialButton_padres = dialog.findViewById(R.id.materialButton_padres);
        final CardView cardView_fondo = dialog.findViewById(R.id.cardView_fondo);

        materialButton_hermano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, MainActivity_Hermano.class);
                startActivity(intent);
                finish();
            }
        });

        materialButton_padres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        int nightModeFlags = getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {

            case Configuration.UI_MODE_NIGHT_YES:
                cardView_fondo.setBackgroundColor(Color.rgb(33, 33, 33));
                materialButton_hermano.setBackgroundColor(Color.rgb(33, 33,33));
                materialButton_padres.setBackgroundColor(Color.rgb(33,33,33));
                break;

        }

    }


    private void manejoTemaOscuro(){

        int nightModeFlags = getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {

            case Configuration.UI_MODE_NIGHT_YES:
                fondo.setBackgroundColor(Color.rgb(48, 48, 48));
                break;

        }
    }
}