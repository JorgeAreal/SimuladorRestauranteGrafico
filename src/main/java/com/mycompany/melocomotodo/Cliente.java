/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import static com.mycompany.melocomotodo.VariablesGenerales.fuentePixel;
import java.awt.Color;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Jorge
 */
public class Cliente {

    private int nCliente;
    private JPanel panel;

    // Crear el panel del cliente
    public JPanel generarPanelCliente(int x, int y, int nCliente) {
        // Panel del nuevo cliente
        JPanel nuevoPanelCliente = new JPanel(null);
        nuevoPanelCliente.setOpaque(false);
        nuevoPanelCliente.setBounds(x, y, 200, 130);

        // Texto de estado (comiendo, esperando...) por defecto no pongo nada
        JLabel texto = new JLabel("Cliente" + nCliente, SwingConstants.CENTER);
        texto.setForeground(Color.WHITE);
        texto.setFont(fuentePixel.deriveFont(Font.PLAIN, 15));
        texto.setBounds(0, 110, 125, 20);
        nuevoPanelCliente.add(texto);

        // Icono del personaje
        JLabel icono = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/cliente/cliente_derecha.png", icono, 25, 0, 70, 100);
        nuevoPanelCliente.add(icono);
        return nuevoPanelCliente;
    }

    public void salirRestaurante(Mesa mesa) {
        new Thread(() -> {
            try {
                // Coordenadas donde se va mover primero, en este caso, la alfombra roja
                int xObjetivo = VariablesGenerales.alfombraRoja[0];
                int yObjetivo = VariablesGenerales.alfombraRoja[1];
                // Primero lo muevo donde está la alfombra roja antes de ir directo hacia la mesa
                VariablesGenerales.moverPanelACoordenadas(panel, xObjetivo, yObjetivo, 5);
                // Pausar un poco para simular que el cliente esta viendo el entorno
                Thread.sleep(VariablesGenerales.generarNumeroAleatorio(500, 1000));
                // Despedirse del personal
                JLabel textoCliente = (JLabel) panel.getComponent(0);
                textoCliente.setForeground(Color.CYAN);
                textoCliente.setText("¡Adios!");
                Thread.sleep(VariablesGenerales.generarNumeroAleatorio(800, 1000));
                // Restaurar nombre original
                textoCliente.setText("Cliente" + nCliente);
                // Salir de la pantalla definitivamente
                VariablesGenerales.moverPanelACoordenadas(panel, xObjetivo, 900, 5);
                Simulacion.panelSimulacion.remove(panel);
                panel = null;
                Simulacion.panelSimulacion.repaint();
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    
    public void salirRestauranteSinPagar(Mesa mesa) {
        new Thread(() -> {
            try {
                // Coordenadas donde se va mover primero, en este caso, la alfombra roja
                int xObjetivo = VariablesGenerales.alfombraRoja[0];
                int yObjetivo = VariablesGenerales.alfombraRoja[1];
                // Primero lo muevo donde está la alfombra roja antes de ir directo hacia la mesa
                VariablesGenerales.moverPanelACoordenadas(panel, xObjetivo, yObjetivo, 8);
                Thread.sleep(100);
                // Despedirse del personal
                JLabel textoCliente = (JLabel) panel.getComponent(0);
                textoCliente.setText("¡Adios!");
                Thread.sleep(VariablesGenerales.generarNumeroAleatorio(800, 1000));
                // Salir de la pantalla definitivamente
                VariablesGenerales.moverPanelACoordenadas(panel, xObjetivo, 900, 8);
                Simulacion.panelSimulacion.remove(panel);
                panel = null;
                Simulacion.panelSimulacion.repaint();
                JOptionPane.showMessageDialog(VariablesGenerales.frame, "Parece que el cliente "+nCliente+" se ha ido sin pagar. ¡Con lo dificil que es tener un negocio en españa!", "¡Vaya!", JOptionPane.INFORMATION_MESSAGE);
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    public int getnCliente() {
        return nCliente;
    }

    public void setnCliente(int nCliente) {
        this.nCliente = nCliente;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
}
