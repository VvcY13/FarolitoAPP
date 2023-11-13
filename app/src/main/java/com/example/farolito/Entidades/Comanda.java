package com.example.farolito.Entidades;

import java.util.ArrayList;
import java.util.List;

public class Comanda {
    private  static Comanda instance;
    private int idComanda;
    private double total;
    private  int idEmpleado;
    private int idMesa;
    private List<DetalleComanda> detalle;

    private Comanda() {
        detalle = new ArrayList<>();
    }
    public static Comanda getInstance() {
        if (instance == null) {
            synchronized (Comanda.class) {
                if (instance == null) {
                    instance = new Comanda();
                }
            }
        }
        return instance;
    }

    public Comanda(int idComanda, double total, int idEmpleado, int idMesa, List<DetalleComanda> detalle) {
        this.idComanda = idComanda;
        this.total = total;
        this.idEmpleado = idEmpleado;
        this.idMesa = idMesa;
        this.detalle = detalle;
    }

    public int getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(int idComanda) {
        this.idComanda = idComanda;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public List<DetalleComanda> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleComanda> detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "idComanda=" + idComanda +
                ", total=" + total +
                ", idEmpleado=" + idEmpleado +
                ", idMesa=" + idMesa +
                ", detalle=" + detalle +
                '}';
    }
}
