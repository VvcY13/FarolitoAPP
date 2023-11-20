package com.example.farolito;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.farolito.Entidades.Comanda;
import com.example.farolito.Entidades.DetalleComanda;
import com.example.farolito.Entidades.Producto;
import com.example.farolito.Interfaces.productoSeleccionadoListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Bebidas_Fragment extends Fragment implements  productoSeleccionadoListener{
    private LinearLayout linearLayoutBebidas;
    private DatabaseReference ProductosRef;
    private List<Producto> listaDeBebidas = new ArrayList<>();
    private List<DetalleComanda> listaBebidasSeleccionados = new ArrayList<>();
    private StorageReference storageReference;

    public Bebidas_Fragment() {

    }


    public static Bebidas_Fragment newInstance(String param1, String param2) {
        Bebidas_Fragment fragment = new Bebidas_Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bebidas, container, false);
        linearLayoutBebidas = view.findViewById(R.id.linear_layout_bebidas);
        ProductosRef = FirebaseDatabase.getInstance().getReference().child("Producto");
        ProductoAdapter productoAdapter = new ProductoAdapter(getActivity(), listaDeBebidas);
        ProductosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaDeBebidas.clear();
                linearLayoutBebidas.removeAllViews();
                for (DataSnapshot Dsnapshot : snapshot.getChildren()) {
                    Producto producto = Dsnapshot.getValue(Producto.class);
                    if (producto != null && "Bebidas".equals(producto.getTipoProducto())) {
                        Button button = new Button(getActivity());
                        int newWidth = 700;
                        int newHeight = 300;
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(newWidth, newHeight);
                        layoutParams.setMargins(10, 10, 10, 25);
                        button.setLayoutParams(layoutParams);

                        storageReference = FirebaseStorage.getInstance().getReference();
                        String imageUrl = producto.getUrl();
                        Log.e("URL", "es  " + imageUrl + "");

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

                        linearLayoutBebidas.addView(button);
                        listaDeBebidas.add(producto);

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
                Log.e("MesafragmentBebidas", "Error al obtener bebidas", error.toException());
            }
        });
        return view;
    }

    private void mostrarDetallesProducto(Producto producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_detalles_producto, null);
        builder.setView(dialogView);

        TextView nombreProductoTextView = dialogView.findViewById(R.id.nombreProductoTextView);
        TextView precioProductoTextView = dialogView.findViewById(R.id.precioProductoTextView);
        EditText cantidadEditText = dialogView.findViewById(R.id.cantidadEditText);
        EditText comentarioEditText = dialogView.findViewById(R.id.comentarioEditText);


        nombreProductoTextView.setText(producto.getNombreProducto());
        precioProductoTextView.setText("Precio: S/" + producto.getPrecioProducto());

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Cerrando alerta", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String cantidadString = cantidadEditText.getText().toString();
                int cantidad;
                String comentario = comentarioEditText.getText().toString();

                if (comentario.isEmpty()) {
                    comentario = "normal";
                }
                if (!cantidadString.isEmpty() && TextUtils.isDigitsOnly(cantidadString)) {
                    cantidad = Integer.parseInt(cantidadString);
                } else {
                    Toast.makeText(getContext(), "Tiene que ingresar una cantidad Numerica", Toast.LENGTH_SHORT).show();
                    return;
                }

                DetalleComanda detalleComanda = new DetalleComanda(producto.getIdProducto(), producto.getNombreProducto(), cantidad, comentario, producto.getPrecioProducto() * cantidad);
                Comanda comanda = Comanda.getInstance();
                comanda.getDetalle().add(detalleComanda);
                comanda.setTotal(comanda.getTotal() + detalleComanda.getSubtotal());

                Toast.makeText(getContext(), "Agregado a Comanda", Toast.LENGTH_SHORT).show();

                if (getActivity() instanceof productoSeleccionadoListener) {
                    ((productoSeleccionadoListener) getActivity()).productoSeleccionado(detalleComanda);
                }

                Log.d("DetallesProducto", "Producto agregado a la comanda:\n" +
                        "ID: " + producto.getIdProducto() + "\n" +
                        "Nombre: " + producto.getNombreProducto() + "\n" +
                        "Precio: S/" + producto.getPrecioProducto() + "\n" +
                        "Cantidad: " + cantidad + "\n" +
                        "Comentario: " + comentario);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void productoSeleccionado(DetalleComanda detalleComanda) {
        listaBebidasSeleccionados.add(detalleComanda);

        Log.d("MesafragmentComidas", "Producto seleccionado: " + detalleComanda.toString());

        actualizarListaProductos(listaBebidasSeleccionados);

        Log.d("MesafragmentComidas", "Lista de productos actualizada");
        if (getActivity() instanceof productoSeleccionadoListener) {
            ((productoSeleccionadoListener) getActivity()).productoSeleccionado(detalleComanda);
        }
    }
    public void actualizarListaProductos(List<DetalleComanda> nuevaListaProductos) {
        for (DetalleComanda detalleComanda : nuevaListaProductos) {
            Log.d("MesafragmentComidas", "Producto: " + detalleComanda.toString());
        }
    }
}
