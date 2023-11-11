package com.example.farolito;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.farolito.Entidades.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Comidas_Fragment extends Fragment {

    private LinearLayout linearLayoutComidas;

    private DatabaseReference ProductosRef;
    private List<Producto> listaDeComidas = new ArrayList<>();
    private StorageReference storageReference;


    public Comidas_Fragment() {
        // Required empty public constructor
    }


    public static Comidas_Fragment newInstance(String param1, String param2) {
        Comidas_Fragment fragment = new Comidas_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comidas, container, false);
        linearLayoutComidas = view.findViewById(R.id.linear_layout_comidas);
        ProductosRef = FirebaseDatabase.getInstance().getReference().child("Producto");
        Log.d("MesafragmentComidas", "ProductosRef inicializado correctamente.");


        ProductoAdapter productoAdapter = new ProductoAdapter(getActivity(), listaDeComidas);


        ProductosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaDeComidas.clear();
                linearLayoutComidas.removeAllViews();
                for (DataSnapshot Dsnapshot : snapshot.getChildren()) {
                    Producto producto = Dsnapshot.getValue(Producto.class);
                    if (producto != null && "Comidas".equals(producto.getTipoProducto())) {
                        Button button = new Button(getActivity());
                        int newWidth = 700;
                        int newHeight = 300;
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(newWidth, newHeight);
                        layoutParams.setMargins(10, 10, 10, 25);
                        button.setLayoutParams(layoutParams);


                        storageReference = FirebaseStorage.getInstance().getReference();
                        // Obtener la URL de la imagen desde el producto
                        String imageUrl = producto.getUrl();
                        Log.e("URL", "es  " +imageUrl+"");

                        Glide.with(getActivity())
                                .load(imageUrl)
                                .centerCrop()
                                .into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        button.setBackground(resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });

                        linearLayoutComidas.addView(button);

                        listaDeComidas.add(producto);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mostrarDetallesProducto(producto);
                            }

                        });

                    }
                }

                productoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MesafragmentComidas", "Error al obtener platos", error.toException());
            }
        });

        return view;
    }
    private void mostrarDetallesProducto(Producto producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Detalles del Producto");

        builder.setMessage("Nombre: " + producto.getNombreProducto() + "\nPrecio: S/" + producto.getPrecioProducto());
        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Cerrando alerta", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Agregar a Comanda", Toast.LENGTH_SHORT).show();
                System.out.println(producto.getIdProducto());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}