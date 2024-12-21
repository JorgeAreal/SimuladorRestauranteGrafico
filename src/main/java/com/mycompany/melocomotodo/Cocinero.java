/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import java.util.ArrayList;

/**
 *
 * @author Jorge Areal Alberich
 */
public class Cocinero {

    private int nCocinero;
    private String nombre;
    private String apellidos;
    private ArrayList<Comanda> comandas;

    public void comprobarComandas() {
        new Thread(() -> {
            try {
                while (true) {
                    if (!comandas.isEmpty()) {
                        Comanda comanda = comandas.get(0);
                        comanda.cocinar(this.nCocinero);
                        comandas.remove(0);
                    }
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }).start();
    }

    public int getnCocinero() {
        return nCocinero;
    }

    public void setnCocinero(int nCocinero) {
        this.nCocinero = nCocinero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public ArrayList<Comanda> getComandas() {
        return comandas;
    }

    public void setComandas(ArrayList<Comanda> comandas) {
        this.comandas = comandas;
    }
}
