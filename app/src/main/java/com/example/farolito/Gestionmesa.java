package com.example.farolito;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.farolito.Entidades.Administrador;
import com.example.farolito.Entidades.Mesa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Gestionmesa extends AppCompatActivity {
    private Button agregarmesa,modificarmesa,borrarmesa,buscarMesa;
    private EditText idmesa,getubicacionmesa;
    private ArrayList<Mesa> listamesa;
    private ListView listamesas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionmesa);
        agregarmesa = findViewById(R.id.AgregarMesa);
        modificarmesa = findViewById(R.id.ModificarMesa);
        borrarmesa = findViewById(R.id.BorrarMesa);
        listamesas = findViewById(R.id.listaMesa);
        buscarMesa = findViewById(R.id.buscarmesaid);
        idmesa = findViewById(R.id.txtingresarmesaId);
        getubicacionmesa = findViewById(R.id.txtmesaencontrado);


        listarMesa();
        BuscarMesa();
        BorrarMesa();


        agregarmesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ventanaAgregarMesa = new Intent(getApplicationContext(), Registromesa.class);
                startActivity(ventanaAgregarMesa);
            }
        });
    }
    public void listarMesa(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Mesa.class.getSimpleName());
        ArrayList<Mesa> listamesa = new ArrayList<Mesa>();
        ArrayAdapter<Mesa> adaptermesa = new ArrayAdapter<Mesa>(getApplicationContext(), android.R.layout.simple_list_item_1,listamesa);
        listamesas.setAdapter(adaptermesa);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mesa mesa = snapshot.getValue(Mesa.class);
                listamesa.add(mesa);
                adaptermesa.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adaptermesa.notifyDataSetChanged();
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
      listamesas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Mesa mesa = listamesa.get(i);
              AlertDialog.Builder a = new AlertDialog.Builder(Gestionmesa.this);
              a.setCancelable(true);
              a.setTitle("Mesa");
              String msg = "ID : " +mesa.getIdMesa() + "\n\n";
              msg += "Ubicacion : " + mesa.getUbicacionMesa() + "\n\n";
              msg += "Capacidad : " + mesa.getCapacidadMesa() + " Personas "+ "\n\n";

              a.setMessage(msg);
              a.show();
          }
      });
    }
    public void BuscarMesa() {
        buscarMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idmesa.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Gestionmesa.this, "Ingresar el Id a Buscar", Toast.LENGTH_SHORT).show();
                } else {
                    int id = Integer.parseInt(idmesa.getText().toString());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Mesa.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String auxiliar = Integer.toString(id);
                            boolean respuesta = false;
                            for(DataSnapshot adminx : snapshot.getChildren()){
                                if(auxiliar.equalsIgnoreCase(adminx.child("idMesa").getValue().toString())){
                                    respuesta = true;
                                    getubicacionmesa.setText(adminx.child("ubicacionMesa").getValue().toString());
                                    break;
                                }
                            }
                            if (respuesta == false){
                                Toast.makeText(Gestionmesa.this, "Id no existente", Toast.LENGTH_SHORT).show();
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
    public void BorrarMesa(){
        borrarmesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idmesa.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Gestionmesa.this, "Ingresar el Id a Eliminar", Toast.LENGTH_SHORT).show();
                } else {
                    int id = Integer.parseInt(idmesa.getText().toString());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Mesa.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String auxiliar = Integer.toString(id);
                            boolean respuesta = false;
                            for(DataSnapshot mesax : snapshot.getChildren()){
                                if(auxiliar.equalsIgnoreCase(mesax.child("idMesa").getValue().toString())){
                                    respuesta = true;
                                    mesax.getRef().removeValue();
                                    idmesa.setText("");
                                    getubicacionmesa.setText("");
                                    Toast.makeText(Gestionmesa.this, "Mesa Eliminada", Toast.LENGTH_SHORT).show();
                                    listarMesa();

                                    break;
                                }
                            }
                            if (respuesta == false){
                                Toast.makeText(Gestionmesa.this, "Id no existente", Toast.LENGTH_SHORT).show();
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