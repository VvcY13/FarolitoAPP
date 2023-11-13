package com.example.farolito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.farolito.Entidades.Mesa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registromesa extends AppCompatActivity {
    private EditText idmesa,capacidadmesa;

    private Button registrarMesa;
    private Spinner ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registromesa);
        idmesa = findViewById(R.id.txtmesaid);
        capacidadmesa = findViewById(R.id.txtcapacidad);
        ubicacion = findViewById(R.id.seleccioneUbicacion);

        registrarMesa = findViewById(R.id.registrarMesa);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Ubicacion, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubicacion.setAdapter(adapter);

        ubicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String ubicacionseleccionada = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Registromesa.this, "No se ha seleccionado", Toast.LENGTH_SHORT).show();
            }
        });
        btnRegistrarMesa();

    }
    public void btnRegistrarMesa(){
        registrarMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idmesa.getText().toString().trim().isEmpty()){
                    Toast.makeText(Registromesa.this, "Rellenar el campo ID", Toast.LENGTH_SHORT).show();
                } else if (capacidadmesa.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Registromesa.this, "Rellenar el campo Capacidad", Toast.LENGTH_SHORT).show();
                }else {
                    int IDmesa = Integer.parseInt(idmesa.getText().toString());
                    String newUbicacion = ubicacion.getSelectedItem().toString();
                    int capacimesa = Integer.parseInt(capacidadmesa.getText().toString());


                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Mesa.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean idRepetido = false;
                            String aux = Integer.toString(IDmesa);
                            for (DataSnapshot mesax : snapshot.getChildren()) {
                                if (mesax.child("idMesa").getValue().toString().equalsIgnoreCase(aux)) {
                                    idRepetido = true;
                                    Toast.makeText(Registromesa.this, "Error, El id " + aux + " ya existe", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            if(!idRepetido) {
                                Mesa mesa = new Mesa();
                                mesa.setIdMesa(IDmesa);
                                mesa.setUbicacionMesa(newUbicacion);
                                mesa.setCapacidadMesa(capacimesa);
                                mesa.setDisponibilidadMesa(true);
                                dbref.push().setValue(mesa);
                                Toast.makeText(Registromesa.this, "Mesa Registrada", Toast.LENGTH_SHORT).show();
                                idmesa.setText("");
                                capacidadmesa.setText("");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Registromesa.this, "Error al cargar Datos de las Mesa", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}