/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import static com.mycompany.melocomotodo.VariablesGenerales.*; // importar todos los metodos y variables de variables generales
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Jorge Areal Alberich
 */
public class Simulacion {

    public static JPanel panelSimulacion; // Panel sobre el que se colocaran el resto de paneles y elementos de la simulacion
    private JPanel panelFondoRestaurante; // Panel cuyo unico proposito es el de poner la textura del restaurante

    private JLabel fondoCespedExterior;
    private JLabel fondoRestaurante;
    private JLabel contadorTiempo;
    private JLabel contadorTiempoSombra;
    private JLabel contadorDia;
    private JLabel contadorDiaSombra;

    private JLabel botonAtras;
    private JLabel botonPausa;

    private int numeroCliente;

    private Thread hiloGeneradorClientes;
    private Thread hiloAsignadorMesa;

    int xUltimoCliente;
    int yUltimoCliente;

    //private final Semaphore controladorGeneradorCliente = new Semaphore(1);
    private boolean generarClientes;
    private boolean temporal;

    public Simulacion() {
        inicializarVariables(); // Inicializar todas las variables del programa que no sean de interfaz
        iniciarListas(); // Inicializar todas las listas del programa
        configurarPaneles();
        configurarPanelesDePersonas();
        colocarElementosPrincipales();
        atribuirFuncionalidadesBotones();
        iniciarPersonal(); // Iniciar los camareros y mesas
        colocarNuevosClientes(); // Coloca clientes cada cierto tiempo
        asignarMesaACliente(); // Responsable de colocar un cliente en una mesa
        comenzarReloj();
        panelGeneral.repaint();
    }

    private void configurarPaneles() {
        // Poner primero el panel simulacion para que se vea por encima de todo
        panelSimulacion = new JPanel(null);
        panelSimulacion.setOpaque(false);
        panelSimulacion.setBounds(0, 0, panelGeneral.getWidth(), panelGeneral.getHeight());
        panelGeneral.add(panelSimulacion);

        // Detras del panel de simulacion, el panel del fondo del restaurante
        panelFondoRestaurante = new JPanel(null);
        panelFondoRestaurante.setOpaque(false);
        int centrarRestaurante = (panelSimulacion.getWidth() - 1000) / 2; // Centrar el panel de forma analitica
        panelFondoRestaurante.setBounds(centrarRestaurante, 60, 1000, 530);
        panelGeneral.add(panelFondoRestaurante);
    }

    private void configurarPanelesDePersonas() {
        // Panel camarero de sala
        panelCamareroSala = new JPanel(null);
        panelCamareroSala.setOpaque(false);
        //panelCamareroSala.setBackground(Color.RED);
        panelCamareroSala.setBounds(685, 530, 120, 130);
        panelSimulacion.add(panelCamareroSala);
        // Texto de estado (comiendo, esperando...) por defecto no pongo nada
        JLabel textoPanelCamarero = new JLabel("Portero", SwingConstants.CENTER);
        textoPanelCamarero.setForeground(Color.WHITE);
        textoPanelCamarero.setFont(fuentePixel.deriveFont(Font.PLAIN, 15));
        textoPanelCamarero.setBounds(0, 110, 120, 20);
        panelCamareroSala.add(textoPanelCamarero);
        // Icono del personaje
        JLabel iconoCamareroSala = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/personas/camarero_sala/sala_posicion.png", iconoCamareroSala, 25, 0, 70, 100);
        panelCamareroSala.add(iconoCamareroSala);
    }

    private void inicializarVariables() {
        generarClientes = true;
        temporal = true;
        numeroCliente = 1;
        xUltimoCliente = 600;
        yUltimoCliente = 580;
    }

    private void iniciarPersonal() {
        // Iniciar cada uno de los camareros. Lo hago con bucle for para automatizar el proceso
        camareros = new CamareroSirviente[4];
        for (int i = 0; i < camareros.length; i++) {
            camareros[i] = new CamareroSirviente();
            camareros[i].setIdCamarero(i);
            camareros[i].setNombre("Camarero");
            camareros[i].setApellidos((i + 1) + "");
            camareros[i].setListaTareas(new ArrayList<>());
            camareros[i].generarPanelCamarero(1070, 180); // Las coordenadas iniciales donde se colocan los camareros (se modificará en seguida)
            camareros[i].setParado(false); // Inicialmente colocar como no ocupado al nuevo camarero
            panelSimulacion.add(camareros[i].getPanel());
            camareros[i].getPanel().setVisible(false); // Quitar visibilidad de los paneles camarero inicialmente
            camareros[i].comprobarSiHayTareaNueva(); // Comprueba ciclicamente si hay uan nueva tarea
        }

        // Matriz de objetos mesa
        mesas = new Mesa[10];
        for (int i = 0; i < mesas.length; i++) {
            mesas[i] = new Mesa();
            mesas[i].setnMesa(i + 1); // El nMesa empieza en 1, mientras que la "i" empieza en 0, por eso el +1
        }

        // Matriz de objetos cocinero
        cocineros = new Cocinero[2];
        for (int i = 0; i < cocineros.length; i++) {
            cocineros[i] = new Cocinero();
            cocineros[i].setnCocinero(i + 1);
            cocineros[i].setNombre("Cocinero");
            cocineros[i].setApellidos(cocineros[i].getnCocinero() + "");
            cocineros[i].setComandas(new ArrayList<>());
            cocineros[i].comprobarComandas(); // Comenzar a comprobar comandas ciclicamente
        }

        // Sommelier
        sommelier = new Sommelier();
        sommelier.setIdSommelier(1); // El id del camarero "4" para el sommelier
        sommelier.setNombre("Sommelier");
        sommelier.setApellidos("");
        sommelier.setListaTareas(new ArrayList<>());
        sommelier.generarPanelSommelier(1070, 180); // Las coordenadas iniciales donde se colocan los camareros (se modificará en seguida)
        sommelier.setParado(false); // Inicialmente colocar como no ocupado al nuevo camarero
        panelSimulacion.add(sommelier.getPanel());
        sommelier.getPanel().setVisible(false); // Quitar visibilidad de los paneles camarero inicialmente
        sommelier.comprobarSiHayTareaNueva(); // Comprueba ciclicamente si hay uan nueva tarea
    }

    private void iniciarListas() {
        clientesEnCola = new ArrayList<>(); // que estan en cola

        // Coordenadas donde se van a parar los personajes
        // Este hashmap contiene <int, int[]>, donde int es el numero de mesa, y donde int[]: int[0] = xMesa y int[1] yMesa
        coordenadasMesasCliente = new HashMap<>();
        int[] matrizCoordenadasMesa1Cliente = new int[]{330, 430}; // Coordenadas mesa 1...
        coordenadasMesasCliente.put(1, matrizCoordenadasMesa1Cliente);
        int[] matrizCoordenadasMesa2Cliente = new int[]{170, 320};
        coordenadasMesasCliente.put(2, matrizCoordenadasMesa2Cliente);
        int[] matrizCoordenadasMesa3Cliente = new int[]{390, 230};
        coordenadasMesasCliente.put(3, matrizCoordenadasMesa3Cliente);
        int[] matrizCoordenadasMesa4Cliente = new int[]{280, 130};
        coordenadasMesasCliente.put(4, matrizCoordenadasMesa4Cliente);
        int[] matrizCoordenadasMesa5Cliente = new int[]{560, 130};
        coordenadasMesasCliente.put(5, matrizCoordenadasMesa5Cliente);
        int[] matrizCoordenadasMesa6Cliente = new int[]{845, 130};
        coordenadasMesasCliente.put(6, matrizCoordenadasMesa6Cliente);
        int[] matrizCoordenadasMesa7Cliente = new int[]{730, 230};
        coordenadasMesasCliente.put(7, matrizCoordenadasMesa7Cliente);
        int[] matrizCoordenadasMesa8Cliente = new int[]{958, 320};
        coordenadasMesasCliente.put(8, matrizCoordenadasMesa8Cliente);
        int[] matrizCoordenadasMesa9Cliente = new int[]{790, 430};
        coordenadasMesasCliente.put(9, matrizCoordenadasMesa9Cliente);
        int[] matrizCoordenadasMesa10Cliente = new int[]{560, 320};
        coordenadasMesasCliente.put(10, matrizCoordenadasMesa10Cliente);

        // Coordenadas de salida de los camareros (parte de arriba (1) y parte de abajo (2))
        coordenadasInicialesCamareros = new HashMap<>();
        int[] matrizCoordenadasInicialesCamarerosArriba = new int[]{1070, 180};
        coordenadasInicialesCamareros.put(1, matrizCoordenadasInicialesCamarerosArriba);
        int[] matrizCoordenadasInicialesCamarerosAbajo = new int[]{1070, 380};
        coordenadasInicialesCamareros.put(2, matrizCoordenadasInicialesCamarerosAbajo);

        // Coordenadas iniciales de los camareros. Los camareros solamente se mueven por el ejeX, asi que no tengo que guardar Y como los clientes
        coordenadasMesasCamarero = new HashMap<>(); // Funciona igual que las coordenadas mesas cliente
        coordenadasMesasCamarero.put(1, 400); // Para la mesa 1, posicion x = 400...
        coordenadasMesasCamarero.put(2, 230);
        coordenadasMesasCamarero.put(3, 460);
        coordenadasMesasCamarero.put(4, 340);
        coordenadasMesasCamarero.put(5, 630);
        coordenadasMesasCamarero.put(6, 910);
        coordenadasMesasCamarero.put(7, 790);
        coordenadasMesasCamarero.put(8, 1020);
        coordenadasMesasCamarero.put(9, 850);
        coordenadasMesasCamarero.put(10, 625);

        // Coordenadas de la alfombra roja. Los clientes se paran ahi unos milisegundos para entrar y salir
        alfombraRoja = new int[2];
        alfombraRoja[0] = 615;
        alfombraRoja[1] = 480;
    }

    private void comenzarReloj() {
        // Instancia de reloj y le paso el label contador tiempo y la hora inicial
        reloj = new Reloj(contadorTiempo, contadorTiempoSombra, "12", "30", true);
        reloj.refrescarContador();
    }

    // Coloca nuevos clientes cada cierto tiempo
    private void colocarNuevosClientes() {
        hiloGeneradorClientes = new Thread(() -> {
            try {
                Thread.sleep(generarNumeroAleatorio(2000, 4000)); // Inicialmente esperar un poco para que no salga el cliente nada mas empezar
                while (generarClientes) {
                    // Abrir instancia del cliente
                    Cliente nuevoCliente = new Cliente();
                    // Generar el nuevo panel del cliente y meterlo en la lista de clientes en cola
                    JPanel nuevoClientePanel = nuevoCliente.generarPanelCliente(xUltimoCliente, yUltimoCliente, numeroCliente);
                    // Colocar atributos al cliente
                    nuevoCliente.setnCliente(numeroCliente);
                    nuevoCliente.setPanel(nuevoClientePanel);
                    clientesEnCola.add(nuevoCliente); // Meter al cliente en la cola

                    // Mostrar el cliente en pantalla
                    panelSimulacion.add(nuevoClientePanel);
                    panelSimulacion.repaint();

                    // Rotar cuando el cliente llega a la esquina de la pantalla
                    if (yUltimoCliente <= -20) {
                        xUltimoCliente += 80;
                    } else if (xUltimoCliente <= 40) {
                        yUltimoCliente -= 100;
                    } else {
                        xUltimoCliente -= 80;
                    }
                    numeroCliente++;
                    //Thread.sleep(generarNumeroAleatorio(20000, 25000));
                    //Thread.sleep(generarNumeroAleatorio(5000, 5500)); // Tiempo para practicas
                    Thread.sleep(generarNumeroAleatorio(500, 600)); // Tiempo rapido para practicas
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        hiloGeneradorClientes.start();
    }

    private void asignarMesaACliente() {
        hiloAsignadorMesa = new Thread(() -> {
            while (true) {
                if (!clientesEnCola.isEmpty()) {
                    generarClientes = false; // No generar clientes mientras se recoloca la fila (evita bugs importantes)
                    // Obtener el primer cliente de la cola
                    Cliente clienteAEntrar = clientesEnCola.get(0);

                    // Obtener el numero de mesa disponible
                    int mesaLibre = obtenerMesaLibre();
                    // Si hay mesa libre...
                    if (mesaLibre != -1) {
                        xUltimoCliente = clientesEnCola.get(clientesEnCola.size() - 1).getPanel().getX();
                        yUltimoCliente = clientesEnCola.get(clientesEnCola.size() - 1).getPanel().getY();
                        clientesEnCola.remove(0); // Eliminar cliente de la lista

                        // Recolocar la fila (porque se va a meter el cliente y va a dejar un hueco)
                        recolocarFilaEnCadena();
                        // Meter el cliente en posicion 0 de la cola a su mesa
                        meterClienteEnMesa(clienteAEntrar, mesaLibre);
                    } else if (mesaLibre == -1) {
                        System.out.println("No hay mesa libre :/");
                        generarClientes = true;
                    } else {
                        System.out.println("Error con la obtencion de las mesas");
                        generarClientes = true;
                    }

                } else {
                    // Si no hay nadie en la cola, el primero debe colocarse en el punto incial de la cola (esto lo pongo por si acaso)
                    xUltimoCliente = 600;
                    yUltimoCliente = 580;
                    //System.out.println("No hay ni dios");
                }
                // Poner un tiempo de espera al camarero de sala
                try {
                    Thread.sleep(generarNumeroAleatorio(2000, 4000));
                } catch (InterruptedException ex) {
                    Logger.getLogger(Simulacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        hiloAsignadorMesa.start();
    }

    private int obtenerMesaLibre() {
        estadosMesas = MetodosSQL.obtenerEstadoMesas();
        if (estadosMesas != null) {
            // Recorrer el mapa para encontrar una mesa libre
            for (Map.Entry<Integer, Integer> entry : estadosMesas.entrySet()) {
                int numeroMesa = entry.getKey();
                int estadoMesa = entry.getValue();

                // Si la mesa está libre (estado 0), poner el estado a 1 y devolver su número
                if (estadoMesa == 0) {
                    MetodosSQL.cambiarEstadoMesa(numeroMesa);
                    return numeroMesa;
                }
            }
            // Si no hay mesas libres, retornar un valor indicador (por ejemplo, -1)
            return -1;
        } else { // Si estadoMesas es nulo, significa que no hay conexion
            return -2;
        }
    }

    private void meterClienteEnMesa(Cliente cliente, int nMesa) {
        // Obtener el panel del cliente y las coordenadas objetivas para mandar al cliente donde yo quiera con animacion
        JPanel panelCliente = cliente.getPanel();
        // Coordenadas donde se va mover primero, en este caso, la alfombra roja
        int xObjetivo = alfombraRoja[0];
        int yObjetivo = alfombraRoja[1];
        // Primero lo muevo donde está la alfombra roja antes de ir directo hacia la mesa
        moverPanelACoordenadas(panelCliente, xObjetivo, yObjetivo, 5);

        // Pausar un poco para simular que el cliente esta viendo el entorno
        try {
            Thread.sleep(generarNumeroAleatorio(500, 1000));
        } catch (InterruptedException ex) {
            Logger.getLogger(Simulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Mover el personaje hacia la mesa correspondiente
        xObjetivo = coordenadasMesasCliente.get(nMesa)[0];
        yObjetivo = coordenadasMesasCliente.get(nMesa)[1];
        // Relizar el movimiento
        moverPanelACoordenadas(panelCliente, xObjetivo, yObjetivo, 5);
        // Cambiar el texto del cliente a "esperando" y mandar un camarero disponible
        JLabel textoCliente = (JLabel) panelCliente.getComponent(0); // Componente 0 es el label texto del cliente
        textoCliente.setForeground(Color.YELLOW);
        textoCliente.setText("Nuevo");
        // Asignar al camarero la mesa
        asignarMesaACamarero(cliente, nMesa);
    }

    private void recolocarFilaEnCadena() {
        new Thread(() -> {
            try {
                for (int i = 0; i < clientesEnCola.size(); i++) {
                    JPanel panelCliente = clientesEnCola.get(i).getPanel();
                    int xCliente = panelCliente.getX();
                    int yCliente = panelCliente.getY();
                    xUltimoCliente = xCliente;
                    yUltimoCliente = yCliente;
                    if (yCliente <= -20 && xCliente <= 40) { // Si está en la esquina superior izquierda
                        yCliente += 100;
                    } else if (yCliente <= -20) { // Si está la fila de arriba
                        xCliente -= 80;
                    } else if (yCliente < 580) { // Si está en la columna izquierda
                        yCliente += 100;
                    } else { // Si está en la fila de abajo
                        xCliente += 80;
                    }
                    moverPanelACoordenadas(panelCliente, xCliente, yCliente, 18);
                    Thread.sleep(10);
                }
                // Volver a encender el generador de clientes
                Thread.sleep(100);
                generarClientes = true;
                if (!hiloGeneradorClientes.isAlive()) { // Si el hilo se ha cerrado, volver a abrirlo
                    colocarNuevosClientes();
                }
            } catch (Exception e) {
                System.out.println("Este tio no existe!");
            }
        }).start();
    }

    // El criterio es: el camarero que tenga menos tareas en su lista de tareas, se le asigna la mesa donde se acaba de sentar el usuario
    private void asignarMesaACamarero(Cliente cliente, int nMesa) {
        if (camareros == null || camareros.length < 3) {
            return; // Manejar el caso en el que no haya camareros iniciados (no deberia ocurrir nunca)
        }

        /* Asignacion de mesas por camarero
        Camarero 1: 1, 5, 9
        Camarero 2: 2, 6, 10
        Camarero 3: 3, 7
        Camarero 4: 4, 8
         */
        CamareroSirviente camarero;
        if (nMesa == 1 || nMesa == 5 || nMesa == 9) {
            camarero = camareros[0];
        } else if (nMesa == 2 || nMesa == 6 || nMesa == 10) {
            camarero = camareros[1];
        } else if (nMesa == 3 || nMesa == 7) {
            camarero = camareros[2];
        } else {
            camarero = camareros[3];
        }
        // Asignar a la mesa el camarero
        mesas[nMesa - 1].setCamarero(camarero); // El nMesa empieza en 1 y la matriz de mesas empieza en 0, por eso el -1
        mesas[nMesa - 1].setCliente(cliente);
        // Crear la nueva tarea y asignarla a al camarero
        Tarea nuevaTarea = new Tarea();
        nuevaTarea.setMesa(mesas[nMesa - 1]);
        nuevaTarea.setAccion(1); // Mandar la accion "1" que significa atender y preguntar la carta al cliente
        // Agrego la tarea al camarero
        ArrayList listaTareas = camarero.getListaTareas();
        listaTareas.add(nuevaTarea);
        // Una vez añadida la tarea, solamente hay que esperar a que el camarero compruebe la tarea y la realice cuando pueda
        camarero.setListaTareas(listaTareas);
    }

    //  COLOCCION DE ELEMENTOS GENERALES
    private void colocarElementosPrincipales() {
        // Fondo cesped exterior (al panel principal)
        fondoCespedExterior = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/fondos/cesped.png", fondoCespedExterior, 0, 0, panelSimulacion.getWidth(), panelSimulacion.getHeight());
        panelGeneral.add(fondoCespedExterior);

        // Fondo restaurante (al panel del restaurante)
        fondoRestaurante = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/fondos/restaurante_sinfondo.png", fondoRestaurante, 0, 0, panelFondoRestaurante.getWidth(), panelFondoRestaurante.getHeight());
        panelFondoRestaurante.add(fondoRestaurante);

        // Contador de tiempo (al panel simulador)
        contadorTiempo = new JLabel("12:30");
        contadorTiempo.setFont(fuentePixel.deriveFont(Font.PLAIN, 60));
        contadorTiempo.setForeground(Color.WHITE);
        //contadorTiempo.setBounds(1050, 615, 300, 80);
        contadorTiempo.setBounds(1110, 615, 300, 80);
        panelSimulacion.add(contadorTiempo);

        // Sombra del contador de tiempo (al panel simulador)
        contadorTiempoSombra = new JLabel("12:30");
        contadorTiempoSombra.setFont(VariablesGenerales.fuentePixel.deriveFont(Font.PLAIN, 60));
        contadorTiempoSombra.setForeground(Color.DARK_GRAY);
        //contadorTiempoSombra.setBounds(1060, 620, 300, 80);
        contadorTiempoSombra.setBounds(1120, 620, 300, 80);
        panelSimulacion.add(contadorTiempoSombra);
        
        // Contador del dia
        contadorDia = new JLabel("D"+diaActual + ",", SwingConstants.RIGHT);
        contadorDia.setFont(fuentePixel.deriveFont(Font.PLAIN, 60));
        contadorDia.setForeground(Color.WHITE);
        contadorDia.setBounds(740, 615, 350, 80);
        panelSimulacion.add(contadorDia);
        
        // Contador del dia sombra
        contadorDiaSombra = new JLabel("D"+diaActual + ",", SwingConstants.RIGHT);
        contadorDiaSombra.setFont(fuentePixel.deriveFont(Font.PLAIN, 60));
        contadorDiaSombra.setForeground(Color.DARK_GRAY);
        contadorDiaSombra.setBounds(750, 620, 350, 80);
        panelSimulacion.add(contadorDiaSombra);

        // Boton retroceder
        botonAtras = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/atras.png", botonAtras, 12, 12, 140, 65);
        panelSimulacion.add(botonAtras);

        // Boton pausa (al final no lo agrego)
        botonPausa = new JLabel();
        VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/boton_pausa.png", botonPausa, 930, 605, 90, 90);
        //panelSimulacion.add(botonPausa);
    }

    private void atribuirFuncionalidadesBotones() {
        // Para el boton atras
        botonAtras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Cambiar la imagen cuando el cursor está encima
                VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/atras_pressed.png", botonAtras, 12, 12, 140, 65);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Restaurar la imagen original cuando el cursor sale
                VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/atras.png", botonAtras, 12, 12, 140, 65);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                panelGeneral.removeAll();
                new MeLoComoTodo(true); //Llamar al constructor encargado de colocar la interfaz del menu inicial
            }
        });

        // Para el boton pausa
        botonPausa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Cambiar la imagen cuando el cursor está encima
                VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/boton_pausa_pressed.png", botonPausa, 930, 605, 90, 90);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Restaurar la imagen original cuando el cursor sale
                VariablesGenerales.calcularNuevoTamanioImagen("recursos/botones/boton_pausa.png", botonPausa, 930, 605, 90, 90);
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }
}
