package com.example.farolito;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.farolito.Entidades.Comanda;
import com.example.farolito.Entidades.DetalleComanda;
import com.example.farolito.Entidades.Producto;
import com.example.farolito.Interfaces.DetalleComandaAdapterCallback;
import com.example.farolito.Interfaces.productoSeleccionadoListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Comandas_Fragment extends Fragment implements productoSeleccionadoListener, DetalleComandaAdapterCallback {
    private List<DetalleComanda> listaProductosSeleccionados = new ArrayList<>();
    private List<Producto> listaProductos = new ArrayList<>();

    private Button enviarPedido;
    private int idMesa;
    private int idEmpleado;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comandas, container, false);
        ListView listViewComanda = view.findViewById(R.id.listViewComanda);
        enviarPedido = view.findViewById(R.id.btnenviarpedido);
        DetalleComandaAdapter detalleComandaAdapter = new DetalleComandaAdapter(getActivity(), listaProductosSeleccionados, listaProductos);
        listViewComanda.setAdapter(detalleComandaAdapter);


        enviarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comanda comanda = Comanda.getInstance();
                comanda.setIdEmpleado(idEmpleado);
                comanda.setIdMesa(idMesa);

                double total = 0.0;

                listaProductosSeleccionados.clear();
                for (DetalleComanda detalle : listaProductosSeleccionados) {
                    comanda.addDetalle(detalle);
                    total += detalle.getSubtotal();
                }

                comanda.setTotal(total);

                detalleComandaAdapter.notifyDataSetChanged();

                DatabaseReference comandasRef = FirebaseDatabase.getInstance().getReference("Comandas");
               comandasRef.push().setValue(comanda).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Comanda enviada con éxito", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




        return view;
     }
    public void productoSeleccionado(DetalleComanda detalleComanda) {
        if (!listaProductosSeleccionados.contains(detalleComanda)) {
            listaProductosSeleccionados.add(detalleComanda);
        }
        obtenerInformacionProducto(detalleComanda.getIdProducto());
        actualizarListaProductos(listaProductosSeleccionados);
        imprimirProductosRecibidos();
    }

    private void obtenerInformacionProducto(int idProducto) {

        for (Producto producto : listaProductos) {
            if (producto.getIdProducto() == idProducto) {
                DetalleComanda nuevoDetalle = new DetalleComanda();
                Log.d("obteniendo datos", "Información del producto: " + producto.getIdProducto() + ", " + producto.getPrecioProducto());
                nuevoDetalle.setIdProducto(producto.getIdProducto());
                nuevoDetalle.setSubtotal(producto.getPrecioProducto()* nuevoDetalle.getCantidad());
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
        this.idMesa = idMesa;
        this.idEmpleado = idEmpleado;
        System.out.println("Datos recibidos en el fragmentComanda - ID Mesa: " + idMesa);
        System.out.println("Datos recibidos en el fragmentComanda - ID Empleado: " + idEmpleado);

    }


}