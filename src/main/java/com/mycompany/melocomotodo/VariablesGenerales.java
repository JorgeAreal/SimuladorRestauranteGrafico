/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import java.awt.Font;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

/**
 *
 * @author Jorge Areal Alberich
 */
public class VariablesGenerales {

    // Elementos principales de la interfaz grafica
    public static JFrame frame;
    public static JPanel panelGeneral;

    // Fuente que se utilizara a lo largo de todo el programa
    public static Font fuentePixel;

    // Conexion a la API
    public static String urlAPI;
    public static String puertoAPI;
    
    // Dia actual del simulador (esta informacion se extrae de SQL) y hora
    public static int diaActual;
    public static Reloj reloj;

    // Mapa donde se almacenará el estado de las mesas
    public static Mesa[] mesas;
    public static HashMap<Integer, Integer> estadosMesas;
    public static ArrayList<Cliente> clientesEnCola;

    // La carta completa de la base de datos SQL
    public static ArrayList<Producto> entrantes;
    public static ArrayList<Producto> segundos;
    public static ArrayList<Producto> postres;
    public static ArrayList<Producto> vinos;

    // Personal del restaurante
    public static JPanel panelCamareroSala;
    public static Sommelier sommelier;
    public static CamareroSirviente[] camareros;
    public static Cocinero[] cocineros;

    // Coordenadas de lugares importantes
    public static int[] alfombraRoja; // Coordenadas de la alfomrba roja del restaurante x = [0] y = [1]
    public static HashMap<Integer, int[]> coordenadasMesasCliente; // Todas las coordenadas donde se pondrá el cliente en la mesa
    public static HashMap<Integer, Integer> coordenadasMesasCamarero; // Todas las coordenadas de cada mesa donde se pondrá el camarero
    public static HashMap<Integer, int[]> coordenadasInicialesCamareros; // Todas las coordenadas posibles desde donde salen los camareros

    public static void calcularNuevoTamanioImagen(String urlImagen, JLabel labelAVonvertir, int x, int y, int width, int height) {
        // Actualizar el tamaño del label proporcionalmente al tamaño de la ventana
        ImageIcon icono = new ImageIcon(urlImagen);
        Image imagen = icono.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        labelAVonvertir.setIcon(new ImageIcon(imagen));
        labelAVonvertir.setBounds(x, y, width, height);
    }

    public static int centrarElementoGrafico(JPanel panel, int anchoObjeto) {
        int coordenadaCentrada = (panel.getWidth() - anchoObjeto) / 2;
        return coordenadaCentrada;
    }

    // Obtiene los valores del XML para la conexion a la base de datos
    public static void obtenerConfiguracionXML() {
        try {
            // Crear una instancia del parser SAX
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            // Crear una instancia del manejador
            LectorXML lector = new LectorXML();
            // Parsear el archivo XML
            saxParser.parse("conf.xml", lector);

            // Obtener la configuración y mostrarla
            HashMap<String, String> config = lector.getConfiguracion();

            // Atribuir cada valor del hashmap a variables estaticas y accesibles por todas las clases
            MetodosSQL.URL = config.get("url");
            MetodosSQL.PUERTO = config.get("puerto");
            MetodosSQL.NOMBRE_BBDD = config.get("nombre");
            MetodosSQL.USUARIO = config.get("usuario");
            MetodosSQL.CONTRASENIA = config.get("contrasenia");
            MetodosSQL.URLcompletaSQL = config.get("url") + ":" + MetodosSQL.PUERTO + "/" + config.get("nombre");
            urlAPI = config.get("urlAPI");
            puertoAPI = config.get("puertoAPI");

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(MeLoComoTodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Le mandas el panel que quieres mover, las coordenadas a las que quieres que vaya, la velocidad, Y LISTO
    // no recomendado para elemtos que no estan en el panel "simulacion"
    public static void moverPanelACoordenadas(JPanel panelAMover, int xObjetivo, int yObjetivo, int velocidad) {
        // Obtener las coordenadas de partida del panel
        int xActual = panelAMover.getX();
        int yActual = panelAMover.getY();
        while (xActual != xObjetivo || yActual != yObjetivo) {
            try {
                // Moverse en el eje X
                if (xActual < xObjetivo) {
                    xActual = Math.min(xActual + velocidad, xObjetivo);
                } else if (xActual > xObjetivo) {
                    xActual = Math.max(xActual - velocidad, xObjetivo);
                }

                // Moverse en el eje Y
                if (yActual < yObjetivo) {
                    yActual = Math.min(yActual + velocidad, yObjetivo);
                } else if (yActual > yObjetivo) {
                    yActual = Math.max(yActual - velocidad, yObjetivo);
                }

                // Actualizar la posición del panel
                panelAMover.setBounds(xActual, yActual, panelAMover.getWidth(), panelAMover.getHeight());
                Simulacion.panelSimulacion.repaint();

                // Pausa entre cada paso para que el movimiento sea visible
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Le pasas una URL y devuelve un JSONObject
    public static JSONObject almacenarJsonConUrl(String url) {
        try {
            String linea;
            String jsonText = "";
            InputStream is = new URL(url).openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while ((linea = br.readLine()) != null) {
                jsonText += linea;
            }
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (MalformedURLException ex) {
            System.out.println("Hubo un error json :("+ex);
            return null;
        } catch (IOException ex) {
            System.out.println("Hubo un error json :("+ex);
            return null;
        }
    }

    // Le pasas una URL y devuelve un JSONArray
    public static JSONArray almacenarJsonArrayConUrl(String url) {
        try {
            String linea;
            String jsonText = "";
            InputStream is = new URL(url).openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while ((linea = br.readLine()) != null) {
                jsonText += linea;
            }
            JSONArray json = new JSONArray(jsonText);
            return json;
        } catch (MalformedURLException ex) {
            System.out.println("Hubo un error json :("+ex);
            return null;
        } catch (IOException ex) {
            System.out.println("Hubo un error json :("+ex);
            return null;
        }
    }

    // Devolver la suma de los precios de cada producto pedido
    public static double calcularPrecioMenu(Mesa mesa) {
        double precioEntrante = mesa.getEntrante().getPrecio();
        double precioSegundo = mesa.getSegundo().getPrecio();
        double precioPostre = mesa.getPostre().getPrecio();
        double precioVino = mesa.getVino().getPrecio();
        double total = precioEntrante + precioSegundo + precioPostre + precioVino;

        // Redondear a dos decimales
        BigDecimal totalRedondeado = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);

        return totalRedondeado.doubleValue();
    }

    public static int generarNumeroAleatorioDesdeProceso(int minimo, int maximo) {
        try {
            // Ejecutar proceso mandando como parametros el rango de valores
            ProcessBuilder ejecutarComando = new ProcessBuilder("java", "scripts/GeneradorAleatorio.java", minimo + "", maximo + "");
            ejecutarComando.redirectErrorStream(true);
            Process proceso = ejecutarComando.start();
            proceso.waitFor();

            BufferedReader salidaProceso = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String resultado = salidaProceso.readLine().trim();
            return Integer.parseInt(resultado);
        } catch (InterruptedException e) {
            System.out.println("El proceso ha sido interrumpido de forma inesperada :(");
            return -1;
        } catch (Exception e) {
            System.out.println("¡Debes ejecutar un programa que exista!");
            return -1;
        }
    }

    // genera un numero aleatorio dentro del ragno especificado
    public static int generarNumeroAleatorio(int minimo, int maximo) {
        if (minimo > maximo) {
            throw new IllegalArgumentException("El valor mínimo no puede ser mayor que el valor máximo.");
        }
        return (int) (Math.random() * (maximo - minimo + 1)) + minimo;
    }
}
