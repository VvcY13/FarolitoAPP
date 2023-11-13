package com.example.farolito;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.farolito.Entidades.DetalleComanda;
import com.example.farolito.Entidades.Producto;
import com.example.farolito.Interfaces.DetalleComandaAdapterCallback;
import com.example.farolito.Interfaces.productoSeleccionadoListener;

import java.util.ArrayList;
import java.util.List;

public class Comandas_Fragment extends Fragment implements productoSeleccionadoListener, DetalleComandaAdapterCallback {
    private List<DetalleComanda> listaProductosSeleccionados = new ArrayList<>();
    private List<Producto> listaProductos = new ArrayList<>();
    private DetalleComanda detalleComanda;

      public Comandas_Fragment() {

    }

    public static Comandas_Fragment newInstance(String param1, String param2) {
        Comandas_Fragment fragment = new Comandas_Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comandas, container, false);
        ListView listViewComanda = view.findViewById(R.id.listViewComanda);
        DetalleComandaAdapter detalleComandaAdapter = new DetalleComandaAdapter(getActivity(), listaProductosSeleccionados, listaProductos);
        listViewComanda.setAdapter(detalleComandaAdapter);
        detalleComanda = new DetalleComanda();
        return view;
    }
    public void productoSeleccionado(DetalleComanda detalleComanda) {
        listaProductosSeleccionados.add(detalleComanda);
        obtenerInformacionProducto(detalleComanda.getIdProducto());
        actualizarListaProductos(listaProductosSeleccionados);
        imprimirProductosRecibidos();
    }

    private void obtenerInformacionProducto(int idProducto) {
        DetalleComanda nuevoDetalle = new DetalleComanda();
        for (Producto producto : listaProductos) {
            if (producto.getIdProducto() == idProducto) {
                Log.d("obteniendo datos", "Información del producto: " + producto.getIdProducto() + ", " + producto.getPrecioProducto());
                nuevoDetalle.setIdProducto(producto.getIdProducto());
                nuevoDetalle.setSubtotal(producto.getPrecioProducto()* detalleComanda.getCantidad());
                break;
            }
        }

    }
    public void actualizarListaProductos(List<DetalleComanda> nuevaListaProductos) {
        for (DetalleComanda detalleComanda : nuevaListaProductos) {
        }
    }
    private void imprimirProductosRecibidos() {
        Log.d("MesafragmentComanda", "Productos recibidos:");
        for (DetalleComanda detalleComanda : listaProductosSeleccionados) {
            Log.d("MesafragmentComanda", detalleComanda.toString());
        }
    }
    @Override
    public void onNombreProductoObtenido(String nombreProducto) {
        Log.d(TAG, "Nombre del producto obtenido: " + nombreProducto);
    }
    public void setDatos(int idMesa, int idEmpleado) {
        System.out.println("Datos recibidos en el fragmentComanda - ID Mesa: " + idMesa);
        System.out.println("Datos recibidos en el fragmentComanda - ID Empleado: " + idEmpleado);
    }

}