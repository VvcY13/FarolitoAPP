package com.example.farolito;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button botonMozo = findViewById(R.id.boton_iniciar_sesionmozo);
        Button botonAdmin = findViewById(R.id.boton_iniciar_sesionadmin);


        botonMozo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LogoMozo.class);
                startActivity(intent);
            }
        });

        botonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Acción cuando se haga clic en el botón del administrador
                Intent intent = new Intent(MainActivity.this, LogoAdmin.class);
                startActivity(intent);
            }
        });
    }
}