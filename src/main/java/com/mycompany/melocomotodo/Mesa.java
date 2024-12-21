/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import static com.mycompany.melocomotodo.VariablesGenerales.generarNumeroAleatorio;
import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Jorge
 */
public class Mesa {

    private int nMesa;
    private CamareroSirviente camarero;
    private Cliente cliente;
    private String idMongoPedido;
    private Producto entrante;
    private Producto segundo;
    private Producto postre;
    private Producto vino;
    private Thread hiloMesa;
    private final Semaphore semaforo = new Semaphore(1);

    public void comer(int tipoPlato) {
        hiloMesa = new Thread(() -> {
            try {
                semaforo.acquire();
                switch (tipoPlato) {
                    case 1: // Entrante
                        entrante();
                        break;
                    case 2: // Segundo
                        segundo();
                        break;
                    case 3: // Postre
                        postre();
                        break;
                    default:
                        break;
                }
                semaforo.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(Mesa.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        hiloMesa.start();
    }

    private void entrante() throws InterruptedException {
        // Simular comer
        Thread.sleep(generarNumeroAleatorio(14000, 24000));
        // Poner al cliente como esperando
        JLabel textoCliente = (JLabel) cliente.getPanel().getComponent(0);
        textoCliente.setText("Recoger");
        textoCliente.setForeground(Color.YELLOW);
        // Llamar al camarero para recoger el plato
        Tarea tarea = new Tarea();
        tarea.setMesa(this);
        tarea.setAccion(3);
        ArrayList listaTareasCamarero = camarero.getListaTareas();
        listaTareasCamarero.add(tarea);
        camarero.setListaTareas(listaTareasCamarero);
    }

    private void segundo() throws InterruptedException {
        // Simular comer
        Thread.sleep(generarNumeroAleatorio(19000, 29000));
        // Poner al cliente como esperando
        JLabel textoCliente = (JLabel) cliente.getPanel().getComponent(0);
        textoCliente.setText("Recoger");
        textoCliente.setForeground(Color.YELLOW);
        // Llamar al camarero para recoger el plato
        Tarea tarea = new Tarea();
        tarea.setMesa(this);
        tarea.setAccion(5);
        ArrayList listaTareasCamarero = camarero.getListaTareas();
        listaTareasCamarero.add(tarea);
        camarero.setListaTareas(listaTareasCamarero);
    }

    private void postre() throws InterruptedException {
        // Simular comer
        Thread.sleep(generarNumeroAleatorio(15000, 20000));
        // Poner al cliente como esperando
        JLabel textoCliente = (JLabel) cliente.getPanel().getComponent(0);
        textoCliente.setText("Pagar...");
        textoCliente.setForeground(Color.YELLOW);
        // Llamar al camarero para recoger el plato
        Tarea tarea = new Tarea();
        tarea.setMesa(this);
        tarea.setAccion(7);
        ArrayList listaTareasCamarero = camarero.getListaTareas();
        listaTareasCamarero.add(tarea);
        camarero.setListaTareas(listaTareasCamarero);
    }

    public int getnMesa() {
        return nMesa;
    }

    public void setnMesa(int nMesa) {
        this.nMesa = nMesa;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public CamareroSirviente getCamarero() {
        return camarero;
    }

    public void setCamarero(CamareroSirviente camarero) {
        this.camarero = camarero;
    }

    public Producto getEntrante() {
        return entrante;
    }

    public void setEntrante(Producto entrante) {
        this.entrante = entrante;
    }

    public Producto getSegundo() {
        return segundo;
    }

    public void setSegundo(Producto segundo) {
        this.segundo = segundo;
    }

    public Producto getPostre() {
        return postre;
    }

    public void setPostre(Producto postre) {
        this.postre = postre;
    }

    public Producto getVino() {
        return vino;
    }

    public void setVino(Producto vino) {
        this.vino = vino;
    }

    public Semaphore getSemaforo() {
        return semaforo;
    }

    public String getIdMongoPedido() {
        return idMongoPedido;
    }

    public void setIdMongoPedido(String idMongoPedido) {
        this.idMongoPedido = idMongoPedido;
    }
}
