/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import static com.mycompany.melocomotodo.VariablesGenerales.fuentePixel;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Jorge Areal Alberich
 */
public class Sommelier {
    private Thread hilo;
    private int idSommelier;
    private String nombre;
    private String apellidos;
    private JPanel panel;
    private ArrayList<Tarea> listaTareas;
    private boolean parar;
    
    // Crear el panel del cliente
    public void generarPanelSommelier(int x, int y) {
        // Panel del nuevo cliente
        JPanel nuevoPanel = new JPanel(null);
        nuevoPanel.setOpaque(false);
        nuevoPanel.setBounds(x, y, 120, 130);

        // Texto de estado (comiendo, esperando...) por defecto no pongo nada
        JLabel texto = new JLabel((nombre + " " + apellidos).trim(), SwingConstants.CENTER);
        texto.setForeground(Color.WHITE);
        texto.setFont(fuentePixel.deriveFont(Font.PLAIN, 15));
        texto.setBounds(0, 110, 120, 20);
        nuevoPanel.add(texto);

        // Icono del personaje
        JLabel icono = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/sommelier/sommelier_izquierda.png", icono, 25, 0, 70, 100);
        nuevoPanel.add(icono);
        panel = nuevoPanel;
    }

    // Comprueba cada cierto tiempo si hay una nueva tarea en la lista de tareas de este camarero
    public void comprobarSiHayTareaNueva() {
        hilo = new Thread(() -> {
            // Mientras no este parar comprobar las nuevas tareas
            while (!parar) {
                try {
                    // Si la lista no está vacia, acceder al hueco "0" y eliminarlo cuando termine la tarea
                    if(!listaTareas.isEmpty()){
                        Tarea tarea = listaTareas.get(0);
                        tarea.realizarTareaSommelier(this);
                        listaTareas.remove(0); // Eliminar la tarea de la lista una vez finalizada
                    }
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CamareroSirviente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        hilo.start();
    }

    public Thread getHilo() {
        return hilo;
    }

    public void setHilo(Thread hilo) {
        this.hilo = hilo;
    }

    public int getIdSommelier() {
        return idSommelier;
    }

    public void setIdSommelier(int idSommelier) {
        this.idSommelier = idSommelier;
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

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public ArrayList<Tarea> getListaTareas() {
        return listaTareas;
    }

    public void setListaTareas(ArrayList<Tarea> listaTareas) {
        this.listaTareas = listaTareas;
    }

    public boolean isParado() {
        return parar;
    }

    public void setParado(boolean parar) {
        this.parar = parar;
    }

    public boolean isParar() {
        return parar;
    }

    public void setParar(boolean parar) {
        this.parar = parar;
    }
    
    
}
