/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import static com.mycompany.melocomotodo.VariablesGenerales.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Jorge
 */
public class Tarea {

    private Mesa mesa;
    private int accion; // 1, 2, 3, 4... Dependiendo el numero, se llama a una funcoin u otra

    // Segun el valor numerico de la tarea, se va a realizar una accion especifica
    // 1: atender cliente y mandar comanda  2: entregar primero  3: recoger primero  4: entregar segundo 5: recoger segundo 6: entregar postre 7: recoger postre
    // Este método se llamará desde fuera, y este ejecutará la acción marcada
    public void realizarTarea(CamareroSirviente camarero) {
        try {
            // Para cualquier accion del camarero, reservar permiso con el sommelier para no coincidir en la misma mesa al mismo tiempo
            mesa.getSemaforo().acquire();
            switch (accion) {
                case 1:
                    preguntarPorMenu(camarero);
                    break;
                case 2:
                    llevarEntrante(camarero);
                    break;
                case 3:
                    recogerEntrante(camarero);
                    break;
                case 4:
                    llevarSegundo(camarero);
                    break;
                case 5:
                    recogerSegundo(camarero);
                    break;
                case 6:
                    llevarPostre(camarero);
                    break;
                case 7:
                    recogerPostre(camarero);
                    break;
                default:
                    break;
            }
            mesa.getSemaforo().release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void realizarTareaSommelier(Sommelier sommelier) {
        try {
            mesa.getSemaforo().acquire();
            switch (accion) {
                case 1:
                    llevarVino(sommelier);
                    break;
                default:
                    break;
            }
            mesa.getSemaforo().release();
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Atiende al cliente por primera vez
    private void preguntarPorMenu(CamareroSirviente camarero) {
        try {
            // Obtener numero mesa que se va a atender
            int nMesa = mesa.getnMesa();
            // Primero mover al camarero de un punto A a un punto B
            int y;
            int xInicial;
            int xDestino = coordenadasMesasCamarero.get(nMesa); // Obtener la coordenada X de la mesa pasando el numero de mesa
            // Ahora decido la coordenada "yInicial" desde donde va a salir el camarero dependiendo de la mesa que tenga que atender
            if (nMesa == 3 || nMesa == 4 || nMesa == 5 || nMesa == 6 || nMesa == 7) {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(1); // clave "1" son las coordenadas desde arriba
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            } else {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(2); // clave "2" son las coordenadas desde abajo
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            }
            // Colocar al camarero en la posicion inicial
            JPanel panelCamarero = camarero.getPanel();
            panelCamarero.setBounds(xInicial, y, panelCamarero.getWidth(), panelCamarero.getHeight());
            panelCamarero.setVisible(true);
            // Iniciar ida del camarero
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xDestino, y, 5);

            // Simular conversacion (para ello obtener los textos de cada persona)
            JLabel textoCamarero = (JLabel) panelCamarero.getComponent(0); // getComponent(0) coge el label
            JLabel textoCliente = (JLabel) mesa.getCliente().getPanel().getComponent(0);
            Thread.sleep(700);
            textoCamarero.setForeground(Color.CYAN);
            textoCamarero.setText("¡HOLA!");
            Thread.sleep(1500);
            textoCliente.setText("¡HOLA!");
            Thread.sleep(1000);
            textoCamarero.setText("Dime");
            Thread.sleep(1000);
            textoCliente.setText("Pidiendo...");
            Thread.sleep(600);
            textoCamarero.setText("Anotando...");
            // LLAMAR AQUI AL PROCESO DE ELECCION DE PLATO
            Thread.sleep(generarNumeroAleatorio(2000, 5000));
            textoCamarero.setForeground(Color.GREEN);
            textoCamarero.setText("OK!");
            textoCliente.setText("");
            Thread.sleep(1000);
            textoCliente.setForeground(Color.YELLOW);
            textoCliente.setText("Esperando...");
            textoCamarero.setForeground(Color.WHITE);
            textoCamarero.setText(camarero.getNombre() + " " + camarero.getApellidos());

            // Comenzar animacion de el camarero yendose
            JLabel imagenCamarero = (JLabel) panelCamarero.getComponent(1); // El componente 1 es la imagen del camarero
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_derecha.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xInicial, y, 7); // Volver al xInicial
            panelCamarero.setVisible(false);
            // Volver a colocarle el icono mirando a la izquierda para cuando vuelva a aparecer
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_izquierda.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());

            // Obtener un menu aleatorio
            // Para cada producto, como tengo una lista de cada tipo de plato descargado, el proceso de numeros aleatorios me dirá un indice aleatorio de la lista de cada comida
            Producto entrante = entrantes.get(generarNumeroAleatorioDesdeProceso(1, entrantes.size() - 1));
            Producto segundo = segundos.get(generarNumeroAleatorioDesdeProceso(1, segundos.size() - 1));
            Producto postre = postres.get(generarNumeroAleatorioDesdeProceso(1, postres.size() - 1));
            Producto vino = vinos.get(generarNumeroAleatorioDesdeProceso(1, vinos.size() - 1));
            // Asignar el menú generado 
            mesa.setEntrante(entrante);
            mesa.setSegundo(segundo);
            mesa.setPostre(postre);
            mesa.setVino(vino);

            // Generar la comanda
            generarComanda(1); // 1 significa cocinar entrante
            
            // Mandar a mongoDB el nuevo pedido
            String idPedidoMongo = MetodosMongo.insertarNuevoPedido(mesa);
            mesa.setIdMongoPedido(idPedidoMongo);
            

            // Decirle al Sommelier de llevarle el vino a la mesa nueva
            Tarea entregarVino = new Tarea();
            entregarVino.setMesa(mesa);
            entregarVino.setAccion(1); // Accion "1" en el sommelier significa llevar el vino a la mesa (la unica tarea del sommelier por el momento)
            ArrayList tareasSommelier = VariablesGenerales.sommelier.getListaTareas();
            tareasSommelier.add(entregarVino);
            VariablesGenerales.sommelier.setListaTareas(tareasSommelier);
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llevarVino(Sommelier sommelier) {
        try {
            int nMesa = mesa.getnMesa();
            // Primero mover al camarero de un punto A a un punto B
            int y;
            int xInicial;
            int xDestino = coordenadasMesasCamarero.get(nMesa); // Obtener la coordenada X de la mesa pasando el numero de mesa
            // Ahora decido la coordenada "yInicial" desde donde va a salir el camarero dependiendo de la mesa que tenga que atender
            if (nMesa == 3 || nMesa == 4 || nMesa == 5 || nMesa == 6 || nMesa == 7) {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(1); // clave "1" son las coordenadas desde arriba
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            } else {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(2); // clave "2" son las coordenadas desde abajo
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            }
            // Colocar al camarero en la posicion inicial
            JPanel panelSommelier = sommelier.getPanel();
            panelSommelier.setBounds(xInicial, y, panelSommelier.getWidth(), panelSommelier.getHeight());
            panelSommelier.setVisible(true);
            // Iniciar ida del camarero
            VariablesGenerales.moverPanelACoordenadas(panelSommelier, xDestino, y, 6);

            // Simular conversacion
            Thread.sleep(300);
            JLabel textoSommelier = (JLabel) VariablesGenerales.sommelier.getPanel().getComponent(0); // El componente 0 del panel sommelier es el texto
            JLabel textoCliente = (JLabel) mesa.getCliente().getPanel().getComponent(0); // El componente 0 del panel cliente es el texto
            
            textoSommelier.setForeground(Color.WHITE);
            textoSommelier.setText("¡HOLA!");
            Thread.sleep(900);
            textoCliente.setForeground(Color.WHITE);
            textoCliente.setText("¡BUENAS!");
            Thread.sleep(900);
            textoSommelier.setText("¡TOMA!");
            Thread.sleep(900);
            textoCliente.setText("¡ULALA!");
            Thread.sleep(900);
            textoSommelier.setText("JEJE");
            Thread.sleep(900);
            textoCliente.setText("¡Gracias!");
            Thread.sleep(900);
            textoSommelier.setText("¡A ti!");
            Thread.sleep(1200);
            // Colocar el nombre del sommelier
            textoSommelier.setText((VariablesGenerales.sommelier.getNombre() + " " + VariablesGenerales.sommelier.getApellidos()).trim());
            textoCliente.setForeground(Color.green);
            textoCliente.setText("Bebiendo..."); // Simular que el cliente es un borracho
            
            // Actualizar el estado del cliente en mongodb
            MetodosMongo.actualizarEstadoComidaMongo(mesa, "vino");

            // Por ultimo poner el estado del cleinte en "esperando..." porque esta esperando el entrante
            // Lo hago en un hilo diferente para que el camarero no espere a que beba el cliente
            new Thread(() -> {
                try {
                    Thread.sleep(VariablesGenerales.generarNumeroAleatorio(800, 1000));
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
                }
                textoCliente.setForeground(Color.YELLOW);
                textoCliente.setText("Esperando...");
            }).start();

            // Comenzar animacion de el camarero yendose
            JLabel imagenCamarero = (JLabel) panelSommelier.getComponent(1); // El componente 1 es la imagen del camarero
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/sommelier/sommelier_derecha.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            VariablesGenerales.moverPanelACoordenadas(panelSommelier, xInicial, y, 7); // Volver al xInicial
            panelSommelier.setVisible(false);
            // Volver a colocarle el icono mirando a la izquierda para cuando vuelva a aparecer
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/sommelier/sommelier_izquierda.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llevarEntrante(CamareroSirviente camarero) {
        try {
            // Obtener numero mesa que se va a atender
            int nMesa = mesa.getnMesa();
            // Primero mover al camarero de un punto A a un punto B
            int y;
            int xInicial;
            int xDestino = coordenadasMesasCamarero.get(nMesa); // Obtener la coordenada X de la mesa pasando el numero de mesa
            // Ahora decido la coordenada "yInicial" desde donde va a salir el camarero dependiendo de la mesa que tenga que atender
            if (nMesa == 3 || nMesa == 4 || nMesa == 5 || nMesa == 6 || nMesa == 7) {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(1); // clave "1" son las coordenadas desde arriba
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            } else {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(2); // clave "2" son las coordenadas desde abajo
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            }
            // Colocar al camarero en la posicion inicial
            JPanel panelCamarero = camarero.getPanel();
            panelCamarero.setBounds(xInicial, y, panelCamarero.getWidth(), panelCamarero.getHeight());
            panelCamarero.setVisible(true);
            // Iniciar ida del camarero
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xDestino, y, 5);

            // Simular conversacion (para ello obtener los textos de cada persona)
            JLabel textoCamarero = (JLabel) panelCamarero.getComponent(0); // getComponent(0) coge el label
            JLabel textoCliente = (JLabel) mesa.getCliente().getPanel().getComponent(0);
            Thread.sleep(100);
            textoCamarero.setForeground(Color.CYAN);
            textoCamarero.setText("¡Tome!");
            Thread.sleep(1500);
            textoCliente.setText("¡Gracias!");
            Thread.sleep(1500);
            textoCamarero.setText(":)");
            Thread.sleep(1500);
            textoCliente.setText(":)");
            Thread.sleep(1500);
            textoCliente.setForeground(Color.green);
            textoCliente.setText("Comiendo...");
            textoCamarero.setForeground(Color.WHITE);
            textoCamarero.setText(camarero.getNombre() + " " + camarero.getApellidos());
            // IMPORTANTE: Poner al cliente como comiendo
            mesa.comer(1); // 1 significa que va a comer el entrante
            // Actualizar el estado del cliente en mongodb
            MetodosMongo.actualizarEstadoComidaMongo(mesa, "entrante");
            // Comenzar animacion de el camarero yendose
            JLabel imagenCamarero = (JLabel) panelCamarero.getComponent(1); // El componente 1 es la imagen del camarero
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_derecha.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xInicial, y, 7); // Volver al xInicial
            panelCamarero.setVisible(false);
            // Volver a colocarle el icono mirando a la izquierda para cuando vuelva a aparecer
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_izquierda.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void recogerEntrante(CamareroSirviente camarero) {
        try {
            // Obtener numero mesa que se va a atender
            int nMesa = mesa.getnMesa();
            // Primero mover al camarero de un punto A a un punto B
            int y;
            int xInicial;
            int xDestino = coordenadasMesasCamarero.get(nMesa); // Obtener la coordenada X de la mesa pasando el numero de mesa
            // Ahora decido la coordenada "yInicial" desde donde va a salir el camarero dependiendo de la mesa que tenga que atender
            if (nMesa == 3 || nMesa == 4 || nMesa == 5 || nMesa == 6 || nMesa == 7) {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(1); // clave "1" son las coordenadas desde arriba
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            } else {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(2); // clave "2" son las coordenadas desde abajo
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            }
            // Colocar al camarero en la posicion inicial
            JPanel panelCamarero = camarero.getPanel();
            panelCamarero.setBounds(xInicial, y, panelCamarero.getWidth(), panelCamarero.getHeight());
            panelCamarero.setVisible(true);
            // Iniciar ida del camarero
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xDestino, y, 5);

            // Simular conversacion (para ello obtener los textos de cada persona)
            JLabel textoCamarero = (JLabel) panelCamarero.getComponent(0); // getComponent(0) coge el label
            JLabel textoCliente = (JLabel) mesa.getCliente().getPanel().getComponent(0);
            Thread.sleep(100);
            textoCamarero.setForeground(Color.CYAN);
            textoCliente.setText("");
            textoCamarero.setText("¿Puedo?");
            Thread.sleep(1500);
            textoCliente.setText("¡Si!");
            Thread.sleep(1500);
            textoCliente.setText("¡Muy rico!");
            Thread.sleep(1500);
            textoCamarero.setText("¡Gracias!");
            Thread.sleep(1000);
            textoCliente.setText("Esperando...");
            textoCliente.setForeground(Color.YELLOW);
            textoCamarero.setForeground(Color.WHITE);
            textoCamarero.setText(camarero.getNombre() + " " + camarero.getApellidos());
            // Comenzar animacion de el camarero yendose
            JLabel imagenCamarero = (JLabel) panelCamarero.getComponent(1); // El componente 1 es la imagen del camarero
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_derecha.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xInicial, y, 7); // Volver al xInicial
            panelCamarero.setVisible(false);
            // Volver a colocarle el icono mirando a la izquierda para cuando vuelva a aparecer
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_izquierda.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            // Generar la comanda
            generarComanda(2); // 2 significa cocinar segundo
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llevarSegundo(CamareroSirviente camarero) {
        try {
            // Obtener numero mesa que se va a atender
            int nMesa = mesa.getnMesa();
            // Primero mover al camarero de un punto A a un punto B
            int y;
            int xInicial;
            int xDestino = coordenadasMesasCamarero.get(nMesa); // Obtener la coordenada X de la mesa pasando el numero de mesa
            // Ahora decido la coordenada "yInicial" desde donde va a salir el camarero dependiendo de la mesa que tenga que atender
            if (nMesa == 3 || nMesa == 4 || nMesa == 5 || nMesa == 6 || nMesa == 7) {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(1); // clave "1" son las coordenadas desde arriba
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            } else {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(2); // clave "2" son las coordenadas desde abajo
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            }
            // Colocar al camarero en la posicion inicial
            JPanel panelCamarero = camarero.getPanel();
            panelCamarero.setBounds(xInicial, y, panelCamarero.getWidth(), panelCamarero.getHeight());
            panelCamarero.setVisible(true);
            // Iniciar ida del camarero
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xDestino, y, 5);

            // Simular conversacion (para ello obtener los textos de cada persona)
            JLabel textoCamarero = (JLabel) panelCamarero.getComponent(0); // getComponent(0) coge el label
            JLabel textoCliente = (JLabel) mesa.getCliente().getPanel().getComponent(0);
            Thread.sleep(100);
            textoCamarero.setForeground(Color.CYAN);
            textoCamarero.setText("¡Segundo!");
            Thread.sleep(1500);
            textoCliente.setText("¡Gracias!");
            Thread.sleep(1500);
            textoCamarero.setText(":)");
            Thread.sleep(1500);
            textoCliente.setText(":)");
            Thread.sleep(1500);
            textoCliente.setForeground(Color.green);
            textoCliente.setText("Comiendo...");
            textoCamarero.setForeground(Color.WHITE);
            textoCamarero.setText(camarero.getNombre() + " " + camarero.getApellidos());
            // IMPORTANTE: Poner al cliente como comiendo
            mesa.comer(2); // 2 significa que va a comer el segundo
            // Actualizar el estado del cliente en mongodb
            MetodosMongo.actualizarEstadoComidaMongo(mesa, "segundo");
            // Comenzar animacion de el camarero yendose
            JLabel imagenCamarero = (JLabel) panelCamarero.getComponent(1); // El componente 1 es la imagen del camarero
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_derecha.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xInicial, y, 7); // Volver al xInicial
            panelCamarero.setVisible(false);
            // Volver a colocarle el icono mirando a la izquierda para cuando vuelva a aparecer
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_izquierda.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void recogerSegundo(CamareroSirviente camarero) {
        try {
            // Obtener numero mesa que se va a atender
            int nMesa = mesa.getnMesa();
            // Primero mover al camarero de un punto A a un punto B
            int y;
            int xInicial;
            int xDestino = coordenadasMesasCamarero.get(nMesa); // Obtener la coordenada X de la mesa pasando el numero de mesa
            // Ahora decido la coordenada "yInicial" desde donde va a salir el camarero dependiendo de la mesa que tenga que atender
            if (nMesa == 3 || nMesa == 4 || nMesa == 5 || nMesa == 6 || nMesa == 7) {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(1); // clave "1" son las coordenadas desde arriba
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            } else {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(2); // clave "2" son las coordenadas desde abajo
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            }
            // Colocar al camarero en la posicion inicial
            JPanel panelCamarero = camarero.getPanel();
            panelCamarero.setBounds(xInicial, y, panelCamarero.getWidth(), panelCamarero.getHeight());
            panelCamarero.setVisible(true);
            // Iniciar ida del camarero
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xDestino, y, 5);

            // Simular conversacion (para ello obtener los textos de cada persona)
            JLabel textoCamarero = (JLabel) panelCamarero.getComponent(0); // getComponent(0) coge el label
            JLabel textoCliente = (JLabel) mesa.getCliente().getPanel().getComponent(0);
            Thread.sleep(100);
            textoCamarero.setForeground(Color.CYAN);
            textoCliente.setText("");
            textoCamarero.setText("¿Puedo?");
            Thread.sleep(1500);
            textoCliente.setText("¡Si!");
            Thread.sleep(1500);
            textoCliente.setText("¡Muy rico!");
            Thread.sleep(1500);
            textoCamarero.setText("¡Gracias!");
            Thread.sleep(1000);
            textoCliente.setText("Esperando...");
            textoCliente.setForeground(Color.YELLOW);
            textoCamarero.setForeground(Color.WHITE);
            textoCamarero.setText(camarero.getNombre() + " " + camarero.getApellidos());
            // Comenzar animacion de el camarero yendose
            JLabel imagenCamarero = (JLabel) panelCamarero.getComponent(1); // El componente 1 es la imagen del camarero
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_derecha.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xInicial, y, 7); // Volver al xInicial
            panelCamarero.setVisible(false);
            // Volver a colocarle el icono mirando a la izquierda para cuando vuelva a aparecer
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_izquierda.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            // Generar la comanda
            generarComanda(3); // 3 significa cocinar postre
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llevarPostre(CamareroSirviente camarero) {
        try {
            // Obtener numero mesa que se va a atender
            int nMesa = mesa.getnMesa();
            // Primero mover al camarero de un punto A a un punto B
            int y;
            int xInicial;
            int xDestino = coordenadasMesasCamarero.get(nMesa); // Obtener la coordenada X de la mesa pasando el numero de mesa
            // Ahora decido la coordenada "yInicial" desde donde va a salir el camarero dependiendo de la mesa que tenga que atender
            if (nMesa == 3 || nMesa == 4 || nMesa == 5 || nMesa == 6 || nMesa == 7) {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(1); // clave "1" son las coordenadas desde arriba
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            } else {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(2); // clave "2" son las coordenadas desde abajo
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            }
            // Colocar al camarero en la posicion inicial
            JPanel panelCamarero = camarero.getPanel();
            panelCamarero.setBounds(xInicial, y, panelCamarero.getWidth(), panelCamarero.getHeight());
            panelCamarero.setVisible(true);
            // Iniciar ida del camarero
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xDestino, y, 5);

            // Simular conversacion (para ello obtener los textos de cada persona)
            JLabel textoCamarero = (JLabel) panelCamarero.getComponent(0); // getComponent(0) coge el label
            JLabel textoCliente = (JLabel) mesa.getCliente().getPanel().getComponent(0);
            Thread.sleep(100);
            textoCamarero.setForeground(Color.CYAN);
            textoCamarero.setText("¡Postre!");
            Thread.sleep(1500);
            textoCliente.setText("¡Gracias!");
            Thread.sleep(1500);
            textoCamarero.setText(":)");
            Thread.sleep(1500);
            textoCliente.setText(":)");
            Thread.sleep(1500);
            textoCliente.setForeground(Color.green);
            textoCliente.setText("Comiendo 3...");
            textoCamarero.setForeground(Color.WHITE);
            textoCamarero.setText(camarero.getNombre() + " " + camarero.getApellidos());
            // IMPORTANTE: Poner al cliente como comiendo
            mesa.comer(3); // 3 significa que va a comer el postre
            // Actualizar el estado del cliente en mongodb
            MetodosMongo.actualizarEstadoComidaMongo(mesa, "postre");
            // Comenzar animacion de el camarero yendose
            JLabel imagenCamarero = (JLabel) panelCamarero.getComponent(1); // El componente 1 es la imagen del camarero
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_derecha.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xInicial, y, 7); // Volver al xInicial
            panelCamarero.setVisible(false);
            // Volver a colocarle el icono mirando a la izquierda para cuando vuelva a aparecer
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_izquierda.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void recogerPostre(CamareroSirviente camarero) {
        try {
            // Obtener numero mesa que se va a atender
            int nMesa = mesa.getnMesa();
            // Primero mover al camarero de un punto A a un punto B
            int y;
            int xInicial;
            int xDestino = coordenadasMesasCamarero.get(nMesa); // Obtener la coordenada X de la mesa pasando el numero de mesa
            // Ahora decido la coordenada "yInicial" desde donde va a salir el camarero dependiendo de la mesa que tenga que atender
            if (nMesa == 3 || nMesa == 4 || nMesa == 5 || nMesa == 6 || nMesa == 7) {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(1); // clave "1" son las coordenadas desde arriba
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            } else {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(2); // clave "2" son las coordenadas desde abajo
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            }
            // Colocar al camarero en la posicion inicial
            JPanel panelCamarero = camarero.getPanel();
            panelCamarero.setBounds(xInicial, y, panelCamarero.getWidth(), panelCamarero.getHeight());
            panelCamarero.setVisible(true);
            // Iniciar ida del camarero
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xDestino, y, 5);

            // Simular conversacion (para ello obtener los textos de cada persona)
            JLabel textoCamarero = (JLabel) panelCamarero.getComponent(0); // getComponent(0) coge el label
            JLabel textoCliente = (JLabel) mesa.getCliente().getPanel().getComponent(0);
            Thread.sleep(100);
            textoCamarero.setForeground(Color.CYAN);
            textoCliente.setText("");
            textoCamarero.setText("¿Puedo?");
            Thread.sleep(1200);
            textoCliente.setText("¡Si!");
            Thread.sleep(1200);
            textoCamarero.setText("¿Que tal?");
            Thread.sleep(1200);
            textoCliente.setText("¡Genial!");
            Thread.sleep(1200);
            textoCliente.setText("¿Cuenta?");
            Thread.sleep(1000);
            textoCamarero.setText("Claro!");
            Thread.sleep(1000);
            textoCliente.setText("Pagar...");
            textoCliente.setForeground(Color.YELLOW);
            textoCamarero.setForeground(Color.WHITE);
            textoCamarero.setText(camarero.getNombre() + " " + camarero.getApellidos());

            // Comenzar animacion de el camarero yendose
            JLabel imagenCamarero = (JLabel) panelCamarero.getComponent(1); // El componente 1 es la imagen del camarero
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_derecha.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xInicial, y, 9); // Volver al xInicial
            panelCamarero.setVisible(false);
            // Volver a colocarle el icono mirando a la izquierda para cuando vuelva a aparecer
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_izquierda.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            Thread.sleep(500);
            // Llevarle la cuenta al empleado para que pague
            llevarCuenta(camarero);
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llevarCuenta(CamareroSirviente camarero) {
        try {
            double precioTotal = calcularPrecioMenu(mesa); // Calcular el precio total a pagar por el cliente
            // Obtener numero mesa que se va a atender
            int nMesa = mesa.getnMesa();
            // Primero mover al camarero de un punto A a un punto B
            int y;
            int xInicial;
            int xDestino = coordenadasMesasCamarero.get(nMesa); // Obtener la coordenada X de la mesa pasando el numero de mesa
            // Ahora decido la coordenada "yInicial" desde donde va a salir el camarero dependiendo de la mesa que tenga que atender
            if (nMesa == 3 || nMesa == 4 || nMesa == 5 || nMesa == 6 || nMesa == 7) {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(1); // clave "1" son las coordenadas desde arriba
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            } else {
                int[] coordenadasIniciales = coordenadasInicialesCamareros.get(2); // clave "2" son las coordenadas desde abajo
                xInicial = coordenadasIniciales[0];
                y = coordenadasIniciales[1];
            }
            // Colocar al camarero en la posicion inicial
            JPanel panelCamarero = camarero.getPanel();
            panelCamarero.setBounds(xInicial, y, panelCamarero.getWidth(), panelCamarero.getHeight());
            panelCamarero.setVisible(true);
            // Iniciar ida del camarero
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xDestino, y, 6);

            // Simular conversacion
            Thread.sleep(100);
            JLabel textoCamarero = (JLabel) panelCamarero.getComponent(0); // getComponent(0) coge el label
            JLabel textoCliente = (JLabel) mesa.getCliente().getPanel().getComponent(0);
            textoCamarero.setForeground(Color.CYAN);
            textoCliente.setText("");
            textoCamarero.setText("Son " + precioTotal + "€");
            Thread.sleep(1000);
            int numeroAleatorio = VariablesGenerales.generarNumeroAleatorio(0, 100);
            // Con un 20% de probabilidad (aprox), el cliente se irá sin pagar
            if (numeroAleatorio <= 20) {
                // Tener conversacion extraña y salir corriendo
                conversacionSimpa(textoCamarero, textoCliente);
                mesa.getCliente().salirRestauranteSinPagar(mesa);
                textoCamarero.setText("!!!");
                // Camarero se queda en shock
                Thread.sleep(200);
                textoCamarero.setText("Canallas...");
                Thread.sleep(3000);
            } else {
                conversacionPagoNormal(textoCamarero, textoCliente);
                // Hacer salir con normalidad al cliente (nuevo hilo)
                mesa.getCliente().salirRestaurante(mesa);
            }

            // Realizar factura (despues)
            // Poner a limpiar al camarero
            textoCamarero.setForeground(Color.WHITE);
            textoCamarero.setText(camarero.getNombre() + " " + camarero.getApellidos());
            // Comenzar animacion de el camarero yendose a por el producto de limpieza
            JLabel imagenCamarero = (JLabel) panelCamarero.getComponent(1); // El componente 1 es la imagen del camarero
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_derecha.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xInicial, y, 9); // Volver al xInicial
            panelCamarero.setVisible(false);
            // Volver a colocarle el icono mirando a la izquierda para cuando vuelva a aparecer
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_izquierda_limpiando.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            Thread.sleep(500);
            panelCamarero.setVisible(true);
            // Camarero volver a mesa
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xDestino, y, 6);
            textoCamarero.setForeground(Color.MAGENTA);
            textoCamarero.setText("Limpiando...");
            Thread.sleep(generarNumeroAleatorio(1000, 2000));

            // Cuando termina de limpiar, liberar mesa
            mesa.setCliente(null);
            mesa.setEntrante(null);
            mesa.setSegundo(null);
            mesa.setPostre(null);
            mesa.setVino(null);
            MetodosSQL.cambiarEstadoMesa(mesa.getnMesa());

            // Camarero vuelve
            textoCamarero.setForeground(Color.WHITE);
            textoCamarero.setText(camarero.getNombre() + " " + camarero.getApellidos());
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_derecha_limpiando.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
            VariablesGenerales.moverPanelACoordenadas(panelCamarero, xInicial, y, 9); // Volver al xInicial
            panelCamarero.setVisible(false);
            // Volver a colocarle el icono mirando a la izquierda para cuando vuelva a aparecer
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sirviente/sirviente_izquierda.png", imagenCamarero, imagenCamarero.getX(), imagenCamarero.getY(), imagenCamarero.getWidth(), imagenCamarero.getHeight());
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void conversacionPagoNormal(JLabel textoCamarero, JLabel textoCliente) {
        try {
            textoCliente.setForeground(Color.green);
            textoCliente.setText("¡Toma!");
            Thread.sleep(1000);
            textoCamarero.setText("¡Gracias!");
            Thread.sleep(1000);
            textoCliente.setForeground(Color.GREEN);
            textoCliente.setText("Adios");
            Thread.sleep(1000);
            textoCamarero.setForeground(Color.GREEN);
            textoCamarero.setText("¡Adios!");
            Thread.sleep(1000);
            textoCliente.setForeground(Color.WHITE);
            textoCamarero.setForeground(Color.WHITE);
            textoCliente.setText("Cliente" + mesa.getCliente().getnCliente());
            generarFactura(1); // 1 significa que la factura se da como pagada en la base de datos
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void conversacionSimpa(JLabel textoCamarero, JLabel textoCliente) {
        try {
            // Avisar con un JOptionPane la maldad de alguna gente. Lo hago en un hilo aparte para no detener el ciclo del camarero
            new Thread(() -> {
                JOptionPane.showMessageDialog(VariablesGenerales.frame, "Parece que el cliente de la mesa " + mesa.getnMesa() + " está tramando algo raro...", "¡OH!", JOptionPane.INFORMATION_MESSAGE);
            }).start();
            Thread.sleep(3500);
            textoCliente.setForeground(Color.red);
            textoCliente.setText("Que caro");
            Thread.sleep(1000);
            textoCamarero.setText("...");
            Thread.sleep(1000);
            textoCliente.setText("...");
            JLabel imagenCliente = (JLabel) mesa.getCliente().getPanel().getComponent(1); // Obtener imagen del cliente para cambiarla
            // Cambiar imagen del cliente
            VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/cliente/cliente_derecha_simpa.png", imagenCliente, imagenCliente.getX(), imagenCliente.getY(), imagenCliente.getWidth(), imagenCliente.getHeight());
            Thread.sleep(1200);
            textoCliente.setText("JUAS");
            textoCamarero.setText("???");
            Thread.sleep(1200);
            generarFactura(0); // 0 significa que la factura se da como no pagada en la base de datos (simpa)
        } catch (InterruptedException ex) {
            Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Crea una comanda y se la manda al cocinero con menos carga de trabajo
    private void generarComanda(int tipoPlato) {
        Comanda comanda = new Comanda();
        comanda.setMesaDestino(mesa);
        comanda.setTipoPlato(tipoPlato);

        // Ahora decidir que cocinero tiene menos demanda, y atribuir esa tarea a ese cocinero
        Cocinero cocineroConMenosTareas = cocineros[0];
        int menorNumeroComandas = cocineroConMenosTareas.getComandas().size();
        for (int i = 1; i < cocineros.length; i++) {
            int numeroTareas = cocineros[i].getComandas().size();
            if (numeroTareas < menorNumeroComandas) {
                cocineroConMenosTareas = cocineros[i];
                menorNumeroComandas = numeroTareas;
            }
        }
        ArrayList comandas = cocineroConMenosTareas.getComandas();
        comandas.add(comanda);
    }
    
    // Una vez termina de comer el cliente, genera la factura que sera enviada a la base de datos
    // Lo hago en un hilo aparte para que no afecte al flujo del programa
    private void generarFactura(int pagado){
        new Thread(()->{
            // Obtener el precio que pagó el cliente
            double precioTotal = calcularPrecioMenu(mesa);
            String diaRealizacion = VariablesGenerales.diaActual + "";
            String horaRealizacion = VariablesGenerales.reloj.getHoras() + ":" + VariablesGenerales.reloj.getMinutos();
            MetodosSQL.realizarFacturaCliente(mesa, precioTotal, diaRealizacion, horaRealizacion, pagado);
        }).start();
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }
}
