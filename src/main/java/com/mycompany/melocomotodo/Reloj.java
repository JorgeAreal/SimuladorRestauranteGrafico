/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Jorge Areal Alberich
 */
public class Reloj {
    private JLabel textoHora;
    private JLabel textoHoraSombra;
    private String horas;
    private String minutos;
    private boolean activo;

    public Reloj(JLabel textoHora, JLabel textoHoraSombra, String horas, String minutos, boolean activo) {
        this.textoHora = textoHora;
        this.textoHoraSombra = textoHoraSombra;
        this.horas = horas;
        this.minutos = minutos;
        this.activo = activo;
    }
    
    public void refrescarContador() {
        new Thread (()->{
            while(activo){
                try {
                    Thread.sleep(4000);
                    // Convertir la hora actual en enteros para sumar
                    int hora = Integer.parseInt(horas);
                    int minuto = Integer.parseInt(minutos);
                    minuto++; // Simar
                    // Si el minuto es 60, cambiar de hora
                    if(minuto == 60){
                        minuto = 0;
                        hora++;
                        horas = hora+"";
                    }
                    // Si el minuto es menor a 10, cambiar de "5" a "05"
                    if(minuto < 10){
                        minutos = "0"+minuto;
                    } else {
                        minutos = minuto+"";
                    }
                    // Establecer la hora visualmente
                    textoHora.setText(hora+":"+minutos);
                    textoHoraSombra.setText(hora+":"+minutos);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public JLabel getTextoHora() {
        return textoHora;
    }

    public void setTextoHora(JLabel textoHora) {
        this.textoHora = textoHora;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getMinutos() {
        return minutos;
    }

    public void setMinutos(String minutos) {
        this.minutos = minutos;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
