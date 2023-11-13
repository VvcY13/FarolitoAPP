package com.example.farolito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OpcionesAdmin extends AppCompatActivity {

    private Button mesas,productos,administrador,usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_admin);
        mesas = findViewById(R.id.btnmesas);
        productos = findViewById(R.id.btnproductos);
        administrador = findViewById(R.id.btnadministrador);
        usuarios = findViewById(R.id.btnusuarios);

        mesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ventanamesas = new Intent(getApplicationContext(),Gestionmesa.class);
                startActivity(ventanamesas);
            }
        });
        productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ventanaproductos = new Intent(getApplicationContext(),Gestionproducto.class);
                startActivity(ventanaproductos);
            }
        });
        administrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ventanaAdministrador = new Intent(getApplicationContext(),Gestionadministrador.class);
                startActivity(ventanaAdministrador);
            }
        });
        usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ventanausuarios = new Intent(getApplicationContext(),Gestionusuario.class);
                startActivity(ventanausuarios);
            }
        });

    }
}