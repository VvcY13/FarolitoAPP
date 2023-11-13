package com.example.farolito;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.farolito.Entidades.DetalleComanda;
import com.example.farolito.Interfaces.productoSeleccionadoListener;

public class Mesas extends AppCompatActivity implements productoSeleccionadoListener {
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
        String identificadorID = getIntent().getStringExtra("idusuario");
        int identificadorUsuario = Integer.parseInt(identificadorID);
        System.out.println("aqui el identificador de mesa " +idSeleccionado);
        System.out.println("aqui en identificador del empleado " +identificadorUsuario );
        titulo.setText(String.valueOf("Mesa " + idSeleccionado));

        comanda.setDatos(idSeleccionado, identificadorUsuario);

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

    @Override
    public void productoSeleccionado(DetalleComanda detalleComanda) {
        if (comanda != null) {
            comanda.productoSeleccionado(detalleComanda);
        }
    }
}