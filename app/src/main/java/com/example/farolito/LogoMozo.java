package com.example.farolito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class LogoMozo extends AppCompatActivity {

    private Button btnloginusuario;
    private EditText txtloginUsuario;
    private EditText txtcontraseñaUsuario;

    private int usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_mozo);
        btnloginusuario = findViewById(R.id.logearseUsuario);
        txtloginUsuario = findViewById(R.id.usuarioLoginUsuario);
        txtcontraseñaUsuario = findViewById(R.id.contraseñaLoginUsuario);

        btnloginusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtloginUsuario.getText().toString().trim().isEmpty()){
                    Toast.makeText(LogoMozo.this, "Rellenar el campo usuario", Toast.LENGTH_SHORT).show();
                } else if (txtcontraseñaUsuario.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LogoMozo.this, "Rellenar al campo contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference("Usuario");

                    String usuario = txtloginUsuario.getText().toString();
                    String contraseña = txtcontraseñaUsuario.getText().toString();

                    dbref.orderByChild("usuarioUser").equalTo(usuario).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for (DataSnapshot Snapshot : snapshot.getChildren() ){
                                    Usuario user = Snapshot.getValue(Usuario.class);
                                    if(user.getContraseñaUsuario().equals(contraseña)){
                                        Toast.makeText(LogoMozo.this, "Inicio exitoso", Toast.LENGTH_SHORT).show();
                                        usuarioID = user.getIdUsuario();
                                        System.out.println(usuarioID);
                                        txtloginUsuario.setText("");
                                        txtcontraseñaUsuario.setText("");
                                        Intent ventanamesas = new Intent(getApplicationContext(),SalonPrincipal.class);
                                        startActivity(ventanamesas);

                                        return;
                                    }
                                }
                                Toast.makeText(LogoMozo.this, "Contraseña Erronea", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(LogoMozo.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
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