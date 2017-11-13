package com.mobilesiri.model;
/**
 * Created by vicentico on 9/16/17.
 */

public class Dispositivo {
    private int id;
    private String nombre;
    private String detalles;
    private int estado; //0 apagado, 1 prendido, 2 desconectado
    private String ip;
    private String MAC;

    public Dispositivo(){}

    public Dispositivo(int id, String nombre, String detalles, int estado, String ip, String MAC) {
        this.id = id;
        this.nombre = nombre;
        this.detalles = detalles;
        this.estado = estado;
        this.ip = ip;
        this.MAC = MAC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }
}
