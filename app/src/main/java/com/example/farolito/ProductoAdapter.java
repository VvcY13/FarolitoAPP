package com.example.farolito;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.farolito.Entidades.Producto;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<Producto> {
    private List<Producto> listaDeProductos;

    public ProductoAdapter(Context context, List<Producto> listaDeProductos){
        super(context, 0, listaDeProductos);
        this.listaDeProductos = listaDeProductos;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        if (!listaDeProductos.isEmpty() && position < listaDeProductos.size()) {
            Producto producto = listaDeProductos.get(position);
            textView.setText(producto.getNombreProducto());
        } else {
            Log.e("ProductoAdapter", "Lista de productos vacía o posición inválida");
        }
        return convertView;
    }

}
