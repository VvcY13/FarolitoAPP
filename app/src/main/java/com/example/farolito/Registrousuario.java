package com.example.farolito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farolito.Entidades.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registrousuario extends AppCompatActivity {
    private EditText idusuario,nombreusuario,apellidousuario,correousuario,usuariouser,contraseñausuario;
    private Button registrarusuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrousuario);
        idusuario = findViewById(R.id.txtuserid);
        nombreusuario = findViewById(R.id.txtusernombres);
        apellidousuario = findViewById(R.id.txtuserapellidos);
        correousuario = findViewById(R.id.txtusercorreo);
        usuariouser = findViewById(R.id.txtuserusuario);
        contraseñausuario = findViewById(R.id.txtusercontraseña);
        registrarusuario=findViewById(R.id.registrarUsuario);

        btnRegistrarUsuario();
    }
    public void btnRegistrarUsuario(){
        registrarusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idusuario.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Rellenar el campo id", Toast.LENGTH_SHORT).show();
                } else if (nombreusuario.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Rellenar el campo nombres", Toast.LENGTH_SHORT).show();
                } else if (apellidousuario.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Rellenar el campo apellidos", Toast.LENGTH_SHORT).show();
                } else if (correousuario.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Rellenar el campo correo", Toast.LENGTH_SHORT).show();
                } else if (usuariouser.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Rellenar el campo usuario", Toast.LENGTH_SHORT).show();
                } else if (contraseñausuario.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Rellenar el campo contraseña", Toast.LENGTH_SHORT).show();
                }else {
                    int IDusuario = Integer.parseInt(idusuario.getText().toString());
                    String nomuser = nombreusuario.getText().toString();
                    String apeuser = apellidousuario.getText().toString();
                    String correouser = correousuario.getText().toString();
                    String usuarioUser = usuariouser.getText().toString();
                    String contrauser = contraseñausuario.getText().toString();

                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Usuario.class.getSimpleName());
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean idUsuarioRepetido = false;
                            boolean usuarioUserRepetido = false;
                            String aux = Integer.toString(IDusuario);

                            for (DataSnapshot usuariox : snapshot.getChildren()) {
                                if (usuariox.child("idUsuario").getValue().toString().equalsIgnoreCase(aux)) {
                                    idUsuarioRepetido = true;
                                    Toast.makeText(Registrousuario.this, "Error, El id " + aux + " ya existe", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                if (usuariox.child("usuarioUser").getValue().toString().equals(usuarioUser)) {
                                    usuarioUserRepetido = true;
                                    Toast.makeText(Registrousuario.this, "Error, El usuario " + usuarioUser + " ya existe", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if (!idUsuarioRepetido && !usuarioUserRepetido) {
                                Usuario user = new Usuario();
                                user.setIdUsuario(IDusuario);
                                user.setNombreUsuario(nomuser);
                                user.setApellidoUsuario(apeuser);
                                user.setCorreoUsuario(correouser);
                                user.setUsuarioUser(usuarioUser);
                                user.setContraseñaUsuario(contrauser);
                                dbref.push().setValue(user);
                                Toast.makeText(getApplicationContext(), "Usuario Registrado", Toast.LENGTH_SHORT).show();
                                idusuario.setText("");
                                nombreusuario.setText("");
                                apellidousuario.setText("");
                                correousuario.setText("");
                                usuariouser.setText("");
                                contraseñausuario.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Registrousuario.this, "Error al insertar", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }
}