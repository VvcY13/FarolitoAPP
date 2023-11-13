package com.example.farolito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.farolito.Entidades.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Registroproducto extends AppCompatActivity {
    private EditText idproducto,nombreproducto,precioproducto;
    private Button registrarProducto;
    private Spinner categoriaproducto;
    private Spinner tipoproducto;
    private Uri imagenUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroproducto);
        idproducto = findViewById(R.id.txtproductoid);
        nombreproducto = findViewById(R.id.txtproductonombre);
        precioproducto = findViewById(R.id.txtprecio);
        categoriaproducto = findViewById(R.id.txtcategoria);
        tipoproducto = findViewById(R.id.txttipo);
        registrarProducto = findViewById(R.id.registrarProducto);
        Button seleccionarImagen = findViewById(R.id.seleccionarImagen);

        seleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriaproducto.setAdapter(adapter);

        categoriaproducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String CategoriaSeleccionada = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Registroproducto.this, "no se ha seleccionado categoria", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<CharSequence> adapterdos = ArrayAdapter.createFromResource(this, R.array.Tipo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoproducto.setAdapter(adapterdos);

        tipoproducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String TipoSelecionado = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Registroproducto.this, "no se ha seleccionado Tipo", Toast.LENGTH_SHORT).show();
            }
        });
        btnRegistrarProducto();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            this.imagenUri = data.getData();
            Toast.makeText(this, "Imagen cargada", Toast.LENGTH_SHORT).show();
        }
    }
    public void btnRegistrarProducto(){
        registrarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idproducto.getText().toString().trim().isEmpty()){
                    Toast.makeText(Registroproducto.this, "Llenar el campo ID Producto", Toast.LENGTH_SHORT).show();
                } else if (nombreproducto.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Registroproducto.this, "Llenar el campo Nombre de Producto", Toast.LENGTH_SHORT).show();
                } else if (precioproducto.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Registroproducto.this, "Llenar el campo Precio del Producto", Toast.LENGTH_SHORT).show();
                } else if (imagenUri == null) {
                    Toast.makeText(Registroproducto.this, "Seleccionar una imagen", Toast.LENGTH_SHORT).show();
                } else {
                    int IdProducto = Integer.parseInt(idproducto.getText().toString());
                    String nomProducto = nombreproducto.getText().toString();
                    Double precproducto = Double.parseDouble(precioproducto.getText().toString());
                    String cateproducto = categoriaproducto.getSelectedItem().toString();
                    String tipproducto = tipoproducto.getSelectedItem().toString();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference imagenRef = storageRef.child("imagenes/" + imagenUri.getLastPathSegment());

                    imagenRef.putFile(imagenUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                imagenRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String urlImagen = uri.toString();

                                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                                    DatabaseReference dbref = db.getReference(Producto.class.getSimpleName());

                                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            boolean idRepetido = false;
                                            boolean nombreRepetido = false;
                                            String aux = Integer.toString(IdProducto);

                                            for (DataSnapshot productox : snapshot.getChildren()){
                                                if(productox.child("idProducto").getValue().toString().equalsIgnoreCase(aux)){
                                                    idRepetido = true;
                                                    Toast.makeText(Registroproducto.this, "Error, El id " +aux+ " ya existe", Toast.LENGTH_SHORT).show();
                                                    break;
                                                }

                                                if(productox.child("nombreProducto").getValue().toString().equals(nomProducto)){
                                                    nombreRepetido = true;
                                                    Toast.makeText(Registroproducto.this, "Error, El nombre " +nomProducto+ " ya existe", Toast.LENGTH_SHORT).show();
                                                    break;
                                                }
                                            }
                                            if (!idRepetido && !nombreRepetido){
                                                Producto producto = new Producto();
                                                producto.setIdProducto(IdProducto);
                                                producto.setNombreProducto(nomProducto);
                                                producto.setPrecioProducto(precproducto);
                                                producto.setCategoriaProducto(cateproducto);
                                                producto.setTipoProducto(tipproducto);
                                                producto.setDisponibilidadProducto(true);
                                                producto.setUrl(urlImagen);
                                                dbref.push().setValue(producto);
                                                Toast.makeText(Registroproducto.this, "Producto Registrado", Toast.LENGTH_SHORT).show();
                                                idproducto.setText("");
                                                nombreproducto.setText("");
                                                precioproducto.setText("");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(Registroproducto.this, "Error en la carga de Datos del Producto", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Registroproducto.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }
}