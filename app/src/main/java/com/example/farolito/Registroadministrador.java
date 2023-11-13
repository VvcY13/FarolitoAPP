package com.example.farolito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farolito.Entidades.Administrador;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registroadministrador extends AppCompatActivity {
    private EditText id,nombre,apellido,correo,usuario,contraseña;
    private Button registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroadministrador);
        id = findViewById(R.id.txtuserid);
        nombre = findViewById(R.id.txtusernombres);
        apellido = findViewById(R.id.txtuserapellidos);
        correo = findViewById(R.id.txtusercorreo);
        usuario = findViewById(R.id.txtuserusuario);
        contraseña = findViewById(R.id.txtusercontraseña);
        registrar = findViewById(R.id.registrarUsuario);

        btnRegistrarAdministrador();
    }
    public void  btnRegistrarAdministrador(){
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().trim().isEmpty()){
                    Toast.makeText(Registroadministrador.this, "Rellenar el campo id", Toast.LENGTH_SHORT).show();
                } else if (nombre.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Registroadministrador.this, "Rellenar el campo nombres", Toast.LENGTH_SHORT).show();
                } else if (apellido.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Registroadministrador.this, "Rellenar el campo apellidos", Toast.LENGTH_SHORT).show();
                } else if (correo.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Registroadministrador.this, "Rellenar el campo correo", Toast.LENGTH_SHORT).show();
                } else if (usuario.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Registroadministrador.this, "Rellenar el campo usuario", Toast.LENGTH_SHORT).show();
                } else if (contraseña.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Registroadministrador.this, "Rellenar el campo contraseña", Toast.LENGTH_SHORT).show();
                }else {
                    int ID = Integer.parseInt(id.getText().toString());
                    String nomAdmin = nombre.getText().toString();
                    String apeAdmin = apellido.getText().toString();
                    String correoAdmin = correo.getText().toString();
                    String usuarioAdmin = usuario.getText().toString();
                    String contraAdmin = contraseña.getText().toString();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Administrador.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean idRepetido = false;
                            boolean usuarioRepetido= false;
                            String aux = Integer.toString(ID);

                            for (DataSnapshot administradorx : snapshot.getChildren()) {
                                if (administradorx.child("id").getValue().toString().equalsIgnoreCase(aux)) {
                                    idRepetido = true;
                                    Toast.makeText(Registroadministrador.this, "Error, El id " + aux + " ya existe", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                if (administradorx.child("usuario").getValue().toString().equals(usuarioAdmin)) {
                                    usuarioRepetido = true;
                                    Toast.makeText(Registroadministrador.this, "Error, El usuario " + usuarioAdmin + " ya existe", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            if (!idRepetido && !usuarioRepetido) {
                                Administrador admin = new Administrador();
                                admin.setId(ID);
                                admin.setNombre(nomAdmin);
                                admin.setApellido(apeAdmin);
                                admin.setCorreo(correoAdmin);
                                admin.setUsuario(usuarioAdmin);
                                admin.setContraseña(contraAdmin);
                                dbref.push().setValue(admin);
                                Toast.makeText(Registroadministrador.this, "Administrador Registrado", Toast.LENGTH_SHORT).show();
                                id.setText("");
                                nombre.setText("");
                                apellido.setText("");
                                correo.setText("");
                                usuario.setText("");
                                contraseña.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Registroadministrador.this, "Error en el insertado", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });


    }
}