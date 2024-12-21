/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.melocomotodo;

import static com.mycompany.melocomotodo.VariablesGenerales.frame;
import static com.mycompany.melocomotodo.VariablesGenerales.panelGeneral;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Jorge Areal Alberich Esta clase carga el menu de inicio jiji
 */
public class MeLoComoTodo {

    private JPanel panelInicio;
    private JPanel panelCarga; // panel que va a contener una imagen semitransparente para oscurecer la pantalla durante la carga

    private JLabel fondo;
    private JLabel semitransparente;
    private JLabel iconoCarga;
    private JLabel botonComenzar;
    private JLabel botonCerrar;
    private JLabel letreroBienvenida;
    private JLabel letraBienvenidaSombra; // Genero otro label identico a "letreroBienvenida" pero en color negro y un poco desplazado para dar efecto de sombra

    private boolean repetirAnimacionLetras; // Controla cuando se va a hacer la animacion de las letras
    private boolean realizarOperacion; // Controla cuando se puede pulsar el boton "comenzar"

    public MeLoComoTodo() {
        // Inicializar variables
        repetirAnimacionLetras = true;
        realizarOperacion = true;
        // Inicializar toda la interfaz grafica
        configurarFrame();
        configurarPaneles();
        configurarFuentes();
        configurarElementos();
        atribuirFuncionalidadesBotones();
        frame.setVisible(true);
    }

    // Le meto un parametro cualquiera para poder hacer un segundo constructor
    // Este constructor es para reconstruir el menu inicial sin rehacer el frame
    public MeLoComoTodo(boolean volver) {
        repetirAnimacionLetras = true;
        realizarOperacion = true;
        configurarPaneles();
        configurarFuentes();
        configurarElementos();
        atribuirFuncionalidadesBotones();
    }

    private void configurarFrame() {
        frame = new JFrame("Me lo Como Todo");
        frame.setSize(1360, 768); // Tamaño del JFrame
        frame.setLayout(null);
        frame.setLocationRelativeTo(null); // Centro la ventana
        frame.setResizable(false);

        // Inicializo la variable que contiene la imagen del icono de la base de datos
        ImageIcon iconoPrincipal = new ImageIcon("recursos/icono.png");
        // Aplico la variable que contiene la imagen del icono y la pongo como icono del frame
        frame.setIconImage(iconoPrincipal.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void configurarPaneles() {
        // Panel general, donde se van a colocar el resto de cosas de todo el programa
        panelGeneral = new JPanel(null);
        panelGeneral.setOpaque(false);
        panelGeneral.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.add(panelGeneral);

        // Panel especifico de la pantalla de inicio
        panelInicio = new JPanel(null);
        panelInicio.setOpaque(false);
        panelInicio.setBounds(0, 0, panelGeneral.getWidth(), panelGeneral.getHeight());
        panelGeneral.add(panelInicio);

        // Panel donde se van a colocar los elementos de la pantalla de carga. Este panel debe estar por encima de todo el resto de elementos
        panelCarga = new JPanel(null);
        panelCarga.setOpaque(false);
        panelCarga.setBounds(0, 0, panelGeneral.getWidth(), panelGeneral.getHeight());
        panelCarga.setBackground(Color.BLACK);
        panelInicio.add(panelCarga);
        panelCarga.setVisible(false); // Ocultar inicialmente
    }

    private void configurarElementos() {
        // Fondo
        fondo = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/fondos/fondo3.gif", fondo, 0, 0, panelGeneral.getWidth(), panelGeneral.getHeight());
        panelGeneral.add(fondo);

        // fondo semitransparente e icono de carga para la pantalla de carga
        semitransparente = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/fondos/semitransparente.png", semitransparente, 0, 0, panelCarga.getWidth(), panelCarga.getHeight());
        iconoCarga = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/icono_carga.gif", iconoCarga, 562, 200, 200, 200);

        // El boton comenzar
        botonComenzar = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/boton_comenzar.png", botonComenzar, 520, 240, 290, 120);
        panelInicio.add(botonComenzar);
        // Boton cerrar
        botonCerrar = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/boton_cerrar.png", botonCerrar, 15, 15, 80, 80);
        panelInicio.add(botonCerrar);

        // Letrero de bienvenida (tanto el original como la sombra)
        // Letrero original blanco
        letreroBienvenida = new JLabel("Bienvenido a 'Me Lo Como Todo'");
        letreroBienvenida.setFont(VariablesGenerales.fuentePixel.deriveFont(Font.PLAIN, 45));
        letreroBienvenida.setForeground(Color.WHITE);
        letreroBienvenida.setBounds(200, 30, 1100, 80);
        panelInicio.add(letreroBienvenida);
        // Letrero con sombra negro
        letraBienvenidaSombra = new JLabel("Bienvenido a 'Me Lo Como Todo'");
        letraBienvenidaSombra.setFont(VariablesGenerales.fuentePixel.deriveFont(Font.PLAIN, 45));
        letraBienvenidaSombra.setForeground(Color.BLACK);
        letraBienvenidaSombra.setBounds(205, 34, 1100, 80);
        panelInicio.add(letraBienvenidaSombra);
        // Inicia la animacion en los letreros blanco y negro
        iniciarEfectoBarrido(letreroBienvenida, letreroBienvenida.getText());
        iniciarEfectoBarrido(letraBienvenidaSombra, letraBienvenidaSombra.getText());
    }

    private void mostrarPantallaCarga() {
        panelCarga.add(iconoCarga);
        panelCarga.add(semitransparente);
        panelCarga.setVisible(true);
    }

    private void ocultarPantallaCarga() {
        panelCarga.setVisible(false);
        panelCarga.remove(semitransparente);
        panelCarga.remove(iconoCarga);
    }

    // Genera un hilo que va colocando letra a letra dentro del letrero "Bienvenido a 'Me Lo Como Todo'"
    // Recibe el label a modificar y lo hace
    private void iniciarEfectoBarrido(JLabel label, String textoCompleto) {
        new Thread(() -> {
            // Bucle do-while para en cualquier momento detener la ejecucion de los hilos a través de un boolean
            do {
                try {
                    label.setText(""); // Empezar con el texto vacío
                    for (int i = 0; i <= textoCompleto.length(); i++) {
                        // Actualizar el texto letra por letra
                        String textoActual = textoCompleto.substring(0, i);
                        label.setText(textoActual);

                        // Pausar para crear el efecto de barrido
                        Thread.sleep(80);
                    }
                    // Repetir la animacion cada 10 segundos
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (repetirAnimacionLetras);
        }).start();
    }

    private void atribuirFuncionalidadesBotones() {
        // Para el boton cerrar
        botonCerrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Cambiar la imagen cuando el cursor está encima
                VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/boton_cerrar_pressed.png", botonCerrar, 15, 15, 80, 80);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Restaurar la imagen original cuando el cursor sale
                VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/boton_cerrar.png", botonCerrar, 15, 15, 80, 80);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        // Para el boton comenzar
        botonComenzar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Cambiar la imagen cuando el cursor está encima
                VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/boton_comenzar_pressed.png", botonComenzar, 520, 240, 290, 120);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Restaurar la imagen original cuando el cursor sale
                VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/boton_comenzar.png", botonComenzar, 520, 240, 290, 120);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (realizarOperacion) {
                    realizarOperacion = false; // Bloquear el resto de clicks al boton
                    //obtenerConfiguracionXML(); // Obtener los datos de conexion a traves del xml
                    mostrarPantallaCarga(); // Mostrar pantalla de carga
                    // La comprobacion se va a ejecutar en un nuevo hilo para que el resto de la aplicacion pueda funcionar igualmente
                    new Thread(() -> {
                        VariablesGenerales.obtenerConfiguracionXML(); // Obtener los datos de conexion del XML antes de realizar el test de conexion
                        if (MetodosSQL.comprobarConexionApiSQL()) {
                            // Si el test es positivo, comenzar  cargar los datos necesarios de la base de datos para empezar la simulacion
                            MetodosSQL.restaurarMesas(); // Si quedaron mesas ocupadas en SQL, liberarlas
                            MetodosSQL.descargarCartaCompleta(); // Descargar la carta de la base de datos SQL
                            MetodosSQL.obtenerDia(); // Obtener el dia de hoy
                            MetodosSQL.sumarDia(); // Una vez obtenido el dia, sumarla en la base de datos para que en la proxima ejecucion haya un nuevo dia
                            ocultarPantallaCarga();
                            repetirAnimacionLetras = false; // Para que no se repita la animacion de las letras y se terminen los hilos
                            realizarOperacion = true; // Poder volver a darle al boton "comenzar" (en este caso no seria posible, pero por si las moscas)
                            panelGeneral.removeAll();
                            new Simulacion();
                        } else {
                            ocultarPantallaCarga();
                            JOptionPane.showMessageDialog(frame, "Error con la conexion :(", "Error", JOptionPane.ERROR_MESSAGE);
                            realizarOperacion = true; // Poder volver a darle al boton "comenzar"
                        }
                    }).start();
                }
            }
        });
    }

    // Carga las fuentes que hay en HerramientasInterfazGrafica para poder utilizarlas a lo largo de las clases del programa
    private void configurarFuentes() {
        try {
            // Cargar la fuente desde el archivo .ttf
            VariablesGenerales.fuentePixel = Font.createFont(Font.TRUETYPE_FONT, new File("recursos/fuentes/pixel.ttf"));
        } catch (FontFormatException ex) {
            Logger.getLogger(MeLoComoTodo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MeLoComoTodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new MeLoComoTodo();
    }
}
