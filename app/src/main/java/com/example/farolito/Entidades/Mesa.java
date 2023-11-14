package com.example.farolito.Entidades;

public class Mesa {
    private int idMesa;
    private String ubicacionMesa;
    private int capacidadMesa;
    private Boolean disponibilidadMesa;

    public Mesa() {
    }

    public Mesa(int idMesa, String ubicacionMesa, int capacidadMesa, Boolean disponibilidadMesa) {
        this.idMesa = idMesa;
        this.ubicacionMesa = ubicacionMesa;
        this.capacidadMesa = capacidadMesa;
        this.disponibilidadMesa = disponibilidadMesa;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public String getUbicacionMesa() {
        return ubicacionMesa;
    }

    public void setUbicacionMesa(String ubicacionMesa) {
        this.ubicacionMesa = ubicacionMesa;
    }

    public int getCapacidadMesa() {
        return capacidadMesa;
    }

    public void setCapacidadMesa(int capacidadMesa) {
        this.capacidadMesa = capacidadMesa;
    }

    public Boolean getDisponibilidadMesa() {
        return disponibilidadMesa;
    }

    public void setDisponibilidadMesa(Boolean disponibilidadMesa) {
        this.disponibilidadMesa = disponibilidadMesa;
    }

    @Override
    public String toString() {
        String disponibilidadTexto = disponibilidadMesa ? "Disponible" : "Ocupada";
        return  "      "+idMesa + "                 " + ubicacionMesa + "             " + capacidadMesa + " Personas      " + disponibilidadTexto;
    }
}
