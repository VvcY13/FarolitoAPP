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

import com.example.farolito.Entidades.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Gestionusuario extends AppCompatActivity {
    private Button agregarUser,borrarUser,buscarusuariouser;
    private ListView listaUsuarios;
    private EditText txtbuscarid,txtusuarioencontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionusuario);
        agregarUser = findViewById(R.id.AgregarUser);
        borrarUser = findViewById(R.id.BorrarUser);
        listaUsuarios = findViewById(R.id.listaUser);
        buscarusuariouser = findViewById(R.id.buscarusuarioid);
        txtbuscarid = findViewById(R.id.txtingresarusuarioId);
        txtusuarioencontrado = findViewById(R.id.txtusuariouserencontrado);

        listarUsuario();
        buscarUsuario();
        borrarUsuario();

        agregarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ventanaAgregarUsuario = new Intent(getApplicationContext(),Registrousuario.class);
                startActivity(ventanaAgregarUsuario);
            }
        });
    }
    private void listarUsuario(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Usuario.class.getSimpleName());
        ArrayList<Usuario> listausuarios = new ArrayList<Usuario>();
        ArrayAdapter<Usuario> adapterusuarios = new ArrayAdapter<Usuario>(getApplicationContext(), android.R.layout.simple_list_item_1,listausuarios);
        listaUsuarios.setAdapter(adapterusuarios);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Usuario user = snapshot.getValue(Usuario.class);
                listausuarios.add(user);
                adapterusuarios.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapterusuarios.notifyDataSetChanged();
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
        listaUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Usuario user = listausuarios.get(i);
                AlertDialog.Builder a = new AlertDialog.Builder(Gestionusuario.this);
                a.setCancelable(true);
                a.setTitle("Usuario Seleccionado");
                String msg = "Nombres : " +user.getNombreUsuario() + "\n\n";
                msg += "Apellidos : " +user.getApellidoUsuario() + "\n\n";
                msg += "Correo : " +user.getCorreoUsuario() + "\n\n";

                a.setMessage(msg);
                a.show();
            }
        });
    }
    public void buscarUsuario(){
        buscarusuariouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtbuscarid.getText().toString().trim().isEmpty()){
                    Toast.makeText(Gestionusuario.this, "Ingresa Id a buscar", Toast.LENGTH_SHORT).show();
                }else{
                    int id = Integer.parseInt(txtbuscarid.getText().toString());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Usuario.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String auxiliar = Integer.toString(id);
                            boolean respuesta = false;
                            for(DataSnapshot usuariox : snapshot.getChildren()){
                                if(auxiliar.equalsIgnoreCase(usuariox.child("idUsuario").getValue().toString())){
                                    respuesta = true;
                                    txtusuarioencontrado.setText(usuariox.child("usuarioUser").getValue().toString());
                                    break;
                                }
                            }
                            if (respuesta == false){
                                Toast.makeText(Gestionusuario.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
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
    public void borrarUsuario(){
        borrarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtbuscarid.getText().toString().trim().isEmpty()){
                    Toast.makeText(Gestionusuario.this, "Ingresa Id a Eliminar", Toast.LENGTH_SHORT).show();
                }else{
                    int id = Integer.parseInt(txtbuscarid.getText().toString());
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Usuario.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = Integer.toString(id);
                            boolean res = false;
                            for (DataSnapshot x : snapshot.getChildren()){
                                if(aux.equalsIgnoreCase(x.child("idUsuario").getValue().toString())){
                                    res = true;
                                    x.getRef().removeValue();
                                    txtbuscarid.setText("");
                                    txtusuarioencontrado.setText("");
                                    listarUsuario();
                                    break;
                                }
                            }
                            if(res == false){
                                Toast.makeText(Gestionusuario.this, "El Id No se Encuentra", Toast.LENGTH_SHORT).show();
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