/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jorge Areal Alberich
 */
public class Comanda {

    private Mesa mesaDestino;
    private int tipoPlato; // 1-> entrante, 2-> segundo, 3-> postre
    private int nCocinero;

    public void cocinar(int nCocinero) {
        this.nCocinero = nCocinero; // Atribuir el nCocinero
        try {
            switch (tipoPlato) {
                case 1:
                    entrante();
                    break;
                case 2:
                    segundo();
                    break;
                case 3:
                    postre();
                    break;
                default:
                    break;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Comanda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void entrante() throws InterruptedException {
        System.out.println("Cocinero " + nCocinero + " ha empezado a cocinar entrante para la mesa " + mesaDestino.getnMesa());
        Thread.sleep(VariablesGenerales.generarNumeroAleatorio(10000, 28000));
        CamareroSirviente camareroARecoger = mesaDestino.getCamarero();
        // Atribuirle la tarea al camarero de llevarle el plato
        Tarea tareaNueva = new Tarea();
        tareaNueva.setAccion(2); // 2 significa que el camarero debe llevar el entrante
        tareaNueva.setMesa(mesaDestino);
        // Mandar al camarero la orden de recoger el plato
        ArrayList tareas = camareroARecoger.getListaTareas();
        tareas.add(tareaNueva);
        camareroARecoger.setListaTareas(tareas);

        System.out.println("Cocinero " + nCocinero + " ha terminado de cocinar entrante para la mesa " + mesaDestino.getnMesa());
    }

    private void segundo() throws InterruptedException {
        System.out.println("Cocinero " + nCocinero + " ha empezado a cocinar segundo para la mesa " + mesaDestino.getnMesa());
        Thread.sleep(VariablesGenerales.generarNumeroAleatorio(20000, 30000));
        CamareroSirviente camareroARecoger = mesaDestino.getCamarero();
        // Atribuirle la tarea al camarero de llevarle el plato
        Tarea tareaNueva = new Tarea();
        tareaNueva.setAccion(4); // 4 significa que el camarero debe llevar el segundo plato
        tareaNueva.setMesa(mesaDestino);
        // Mandar al camarero la orden de recoger el plato
        ArrayList tareas = camareroARecoger.getListaTareas();
        tareas.add(tareaNueva);
        camareroARecoger.setListaTareas(tareas);

        System.out.println("Cocinero " + nCocinero + " ha terminado de cocinar segundo para la mesa " + mesaDestino.getnMesa());
    }

    private void postre() throws InterruptedException {
        System.out.println("Cocinero " + nCocinero + " ha empezado a cocinar el postre para la mesa " + mesaDestino.getnMesa());
        Thread.sleep(VariablesGenerales.generarNumeroAleatorio(4000, 8000));
        CamareroSirviente camareroARecoger = mesaDestino.getCamarero();
        // Atribuirle la tarea al camarero de llevarle el plato
        Tarea tareaNueva = new Tarea();
        tareaNueva.setAccion(6); // 6 significa que el camarero debe llevar el postre
        tareaNueva.setMesa(mesaDestino);
        // Mandar al camarero la orden de recoger el plato
        ArrayList tareas = camareroARecoger.getListaTareas();
        tareas.add(tareaNueva);
        camareroARecoger.setListaTareas(tareas);

        System.out.println("Cocinero " + nCocinero + " ha terminado de cocinar el postre para la mesa " + mesaDestino.getnMesa());
    }

    public Mesa getMesaDestino() {
        return mesaDestino;
    }

    public void setMesaDestino(Mesa mesaDestino) {
        this.mesaDestino = mesaDestino;
    }

    public int getTipoPlato() {
        return tipoPlato;
    }

    public void setTipoPlato(int tipoPlato) {
        this.tipoPlato = tipoPlato;
    }
}
