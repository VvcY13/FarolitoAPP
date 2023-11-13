package com.example.farolito;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.farolito.Entidades.Producto;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Gestionproducto extends AppCompatActivity {
    private Button agregarproducto,modificarproducto,borrarproducto,buscarProducto;
    private EditText idproductobuscado, productoencontrado;
    private ListView listaproductos;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionproducto);
        agregarproducto = findViewById(R.id.AgregarProducto);
        modificarproducto = findViewById(R.id.ModificarProducto);
        borrarproducto = findViewById(R.id.BorrarProducto);
        listaproductos = findViewById(R.id.listaProducto);
        buscarProducto = findViewById(R.id.buscaproductoid);
        idproductobuscado = findViewById(R.id.txtingresaproductoId);
        productoencontrado = findViewById(R.id.txtproductoencontrado);

        context = this;
        listarProductos();
        BuscarProducto();
        BorrarProducto();

        agregarproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ventanaAgregarProductos = new Intent(getApplicationContext(),Registroproducto.class);
                startActivity(ventanaAgregarProductos);
            }
        });
    }
    public void listarProductos(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Producto.class.getSimpleName());
        ArrayList<Producto> listaproducto = new ArrayList<Producto>();
        ArrayAdapter<Producto> adapterproducto = new ArrayAdapter<Producto>(context, android.R.layout.simple_list_item_1,listaproducto);
        listaproductos.setAdapter(adapterproducto);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Producto producto = snapshot.getValue(Producto.class);
                listaproducto.add(producto);
                adapterproducto.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void BuscarProducto() {
        buscarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idproductobuscado.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Gestionproducto.this, "Ingresar el Id a Buscar", Toast.LENGTH_SHORT).show();
                } else {
                    int id = Integer.parseInt(idproductobuscado.getText().toString());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Producto.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String auxiliar = Integer.toString(id);
                            boolean respuesta = false;
                            for (DataSnapshot productox : snapshot.getChildren()) {
                                if (auxiliar.equalsIgnoreCase(productox.child("idProducto").getValue().toString())) {
                                    respuesta = true;
                                    productoencontrado.setText(productox.child("nombreProducto").getValue().toString());
                                    break;
                                }
                            }
                            if (respuesta == false) {
                                Toast.makeText(Gestionproducto.this, "Id no existente", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }
    public void BorrarProducto(){
        borrarproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idproductobuscado.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Gestionproducto.this, "Ingresar el Id a Borrar", Toast.LENGTH_SHORT).show();
                } else {
                    int id = Integer.parseInt(idproductobuscado.getText().toString());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Producto.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String auxiliar = Integer.toString(id);
                            boolean respuesta = false;
                            for (DataSnapshot productox : snapshot.getChildren()) {
                                if (auxiliar.equalsIgnoreCase(productox.child("idProducto").getValue().toString())) {
                                    respuesta = true;
                                    productox.getRef().removeValue();
                                    idproductobuscado.setText("");
                                    productoencontrado.setText("");
                                    listarProductos();

                                    break;
                                }
                            }
                            if (respuesta == false) {
                                Toast.makeText(Gestionproducto.this, "Id no existente", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

        });
    }
}