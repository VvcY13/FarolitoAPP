package com.example.farolito;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Mesas extends AppCompatActivity {
    private TextView titulo;
    private Comidas_Fragment comidas;
    private  Bebidas_Fragment bebidas;
    private Comandas_Fragment comanda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);
        titulo = findViewById(R.id.tvTituloEncima);
        comidas = new Comidas_Fragment();
        bebidas = new Bebidas_Fragment();
        comanda = new Comandas_Fragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragments, comidas).commit();

        int idSeleccionado = getIntent().getIntExtra("idmesaseleccionado", 0);

        titulo.setText(String.valueOf("Mesa " + idSeleccionado));




    }
    public void onClick(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(view.getId()==R.id.btnComida){
            transaction.replace(R.id.contenedorFragments,comidas);
        } else if (view.getId()==R.id.btnBebidas) {
            transaction.replace(R.id.contenedorFragments,bebidas);
        } else if (view.getId()==R.id.btnComanda) {
            transaction.replace(R.id.contenedorFragments,comanda);
        }
        transaction.commit();
    }
}