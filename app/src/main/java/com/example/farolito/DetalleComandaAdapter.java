package com.example.farolito;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.farolito.Entidades.DetalleComanda;
import com.example.farolito.Entidades.Producto;
import com.example.farolito.Interfaces.DetalleComandaAdapterCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DetalleComandaAdapter extends ArrayAdapter<DetalleComanda> {
    private DatabaseReference productoRef;

    private List<Producto> listaProductos;

    public DetalleComandaAdapter(Context context, List<DetalleComanda> detalleComandas, List<Producto> listaProductos) {
        super(context, 0, detalleComandas);
        this.listaProductos = listaProductos;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        productoRef = database.getReference("Producto");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DetalleComanda detalleComanda = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comanda, parent, false);
        }
        TextView nombreProductoTextView = convertView.findViewById(R.id.nombreProductoTextView);
        TextView precioProductoTextView = convertView.findViewById(R.id.precioProductoTextView);
        TextView cantidadProductoTextView = convertView.findViewById(R.id.cantidadProductoTextView);

        int idProducto = detalleComanda.getIdProducto();

        obtenerNombreProducto(idProducto, new DetalleComandaAdapterCallback() {
            @Override
            public void onNombreProductoObtenido(String nombreProducto) {
                nombreProductoTextView.setText(nombreProducto);
                precioProductoTextView.setText(String.valueOf(detalleComanda.getSubtotal()));
                cantidadProductoTextView.setText(String.valueOf(detalleComanda.getCantidad()));
            }
        });

        return convertView;
    }

    private void obtenerNombreProducto(int idProducto, DetalleComandaAdapterCallback callback) {
        productoRef.orderByChild("idProducto").equalTo(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Producto producto = dataSnapshot.getValue(Producto.class);
                        if (producto != null) {
                            String nombreProducto = producto.getNombreProducto();
                            callback.onNombreProductoObtenido(nombreProducto);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error al obtener el nombre del producto", error.toException());
            }
        });

    }

}
