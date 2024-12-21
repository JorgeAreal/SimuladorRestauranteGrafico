/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jorge Métodos que tienen acceso a la base de datos SQL
 */
public class MetodosSQL {

    public static String URL;
    public static String PUERTO;
    public static String NOMBRE_BBDD;
    public static String USUARIO;
    public static String CONTRASENIA;
    public static String URLcompletaSQL;

    public static boolean probarConexionSQLdirecto() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URLcompletaSQL, USUARIO, CONTRASENIA)) {
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean comprobarConexionApiSQL() {
        // Construir el enlace a la API a traves de la información extraida del XML
        String urlAPIconValidacionSQL = VariablesGenerales.urlAPI + ":" + VariablesGenerales.puertoAPI + "/comprobarConexion/?url=" + MetodosSQL.URL + "&puerto=" + MetodosSQL.PUERTO + "&nombre=" + MetodosSQL.NOMBRE_BBDD + "&usuario=" + MetodosSQL.USUARIO + "&contrasenia=" + MetodosSQL.CONTRASENIA;
        // Ejecutar la consulta a la API
        JSONObject json = VariablesGenerales.almacenarJsonConUrl(urlAPIconValidacionSQL);
        // Si es nulo 100% no hay conexion
        if (json == null) {
            return false;
        }
        // Si el estado devuelto por el json es "true", la conexion es válida
        return json.getBoolean("estado");
    }

    public static boolean descargarCartaCompleta() {
        // Inicializar las listas donde se guardaran los platos y vinos
        VariablesGenerales.entrantes = new ArrayList<>();
        VariablesGenerales.segundos = new ArrayList<>();
        VariablesGenerales.postres = new ArrayList<>();
        VariablesGenerales.vinos = new ArrayList<>();

        String urlAPIconValidacionSQL = VariablesGenerales.urlAPI + ":" + VariablesGenerales.puertoAPI + "/obtenerCarta/?url=" + MetodosSQL.URL + "&puerto=" + MetodosSQL.PUERTO + "&nombre=" + MetodosSQL.NOMBRE_BBDD + "&usuario=" + MetodosSQL.USUARIO + "&contrasenia=" + MetodosSQL.CONTRASENIA;
        JSONArray jsonArray = VariablesGenerales.almacenarJsonArrayConUrl(urlAPIconValidacionSQL);
        if (jsonArray == null) {
            System.out.println("Error al descargar la carta: respuesta nula");
            return false;
        }
        try {
            // Iterar sobre los elementos del JSONArray
            for (int i = 0; i < jsonArray.length(); i++) {
                // Obtener cada objeto dentro del array
                JSONObject plato = jsonArray.getJSONObject(i);

                // Extraer los valores
                int id = plato.getInt("id");
                String nombrePlato = plato.getString("nombre_plato");
                String precio = plato.getString("precio");
                int tipo = plato.getInt("tipo");

                try {
                    // Crear producto y agregarlo a la lista
                    Producto producto = new Producto();
                    producto.setNombre(nombrePlato);
                    producto.setPrecio(Double.parseDouble(precio));
                    producto.setTipo(1);
                    // Segun su tipo, agregarlo a una lista u otra
                    switch (tipo) {
                        case 1: // cuando es entrante, agregar a la lista entrante
                            VariablesGenerales.entrantes.add(producto);
                            break;
                        case 2: // cuando es segundo
                            VariablesGenerales.segundos.add(producto);
                            break;
                        case 3: // cuando es postre
                            VariablesGenerales.postres.add(producto);
                            break;
                        case 4: // cuando es vino
                            VariablesGenerales.vinos.add(producto);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error poniendo los platos en las listas");
                }

            }
            return true;
        } catch (Exception e) {
            System.out.println("Error procesando la carta: " + e.getMessage());
            return false;
        }
    }

    public static HashMap<Integer, Integer> obtenerEstadoMesas() {
        try {
            // Construir el enlace a la API a traves de la información extraida del XML
            String urlAPIconValidacionSQL = VariablesGenerales.urlAPI + ":" + VariablesGenerales.puertoAPI + "/obtenerMesas?url=" + MetodosSQL.URL + "&puerto=" + MetodosSQL.PUERTO + "&nombre=" + MetodosSQL.NOMBRE_BBDD + "&usuario=" + MetodosSQL.USUARIO + "&contrasenia=" + MetodosSQL.CONTRASENIA;
            JSONObject json = VariablesGenerales.almacenarJsonConUrl(urlAPIconValidacionSQL);
            HashMap<Integer, Integer> estadosMesas = new HashMap<>();
            // Iterar por las claves del JSONObject
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                try {
                    // Convertir la clave y el valor a Integer
                    int nMesa = Integer.parseInt(key);
                    int estado = json.getInt(key);
                    estadosMesas.put(nMesa, estado);
                } catch (NumberFormatException | org.json.JSONException e) {
                    System.err.println("Error al procesar clave/valor: " + key + " - " + e.getMessage());
                }
            }
            // Mostrar el HashMap
            /*estadosMesas.forEach((nMesa, estado) -> {
            System.out.println("Mesa: " + nMesa + ", Estado: " + estado);
        });*/
            return estadosMesas;
        } catch (Exception e) {
            return null;
        }
    }

    // Recibe el ID de la mesa y le intercambia el valor en la base de datos
    public static boolean cambiarEstadoMesa(int numeroMesa) {
        // Construir el enlace a la API a traves de la información extraida del XML
        int nuevoEstado = VariablesGenerales.estadosMesas.get(numeroMesa) == 0 ? 1 : 0;
        String urlAPIconValidacionSQL = VariablesGenerales.urlAPI + ":" + VariablesGenerales.puertoAPI + "/actualizarMesa/?url=" + MetodosSQL.URL + "&puerto=" + MetodosSQL.PUERTO + "&nombre=" + MetodosSQL.NOMBRE_BBDD + "&usuario=" + MetodosSQL.USUARIO + "&contrasenia=" + MetodosSQL.CONTRASENIA + "&idMesa=" + numeroMesa + "&estado=" + nuevoEstado;
        // Ejecutar la consulta a la API
        JSONObject json = VariablesGenerales.almacenarJsonConUrl(urlAPIconValidacionSQL);
        if (json == null) {
            System.out.println("Hubo un error cambiando el estado :/");
            return false;
        } else {
            return true;
        }
    }

    public static boolean restaurarMesas() {
        String urlAPIconValidacionSQL = VariablesGenerales.urlAPI + ":" + VariablesGenerales.puertoAPI + "/restaurarMesas/?url=" + MetodosSQL.URL + "&puerto=" + MetodosSQL.PUERTO + "&nombre=" + MetodosSQL.NOMBRE_BBDD + "&usuario=" + MetodosSQL.USUARIO + "&contrasenia=" + MetodosSQL.CONTRASENIA;
        // Ejecutar la consulta a la API
        JSONObject json = VariablesGenerales.almacenarJsonConUrl(urlAPIconValidacionSQL);
        if (json == null) {
            System.out.println("Hubo un error cambiando el estado :/");
            return false;
        } else {
            // Obtener el resultado de la consulta
            //String resultado = json.getString("message");
            //System.out.println(resultado);
            return true;
        }
    }
    
    public static boolean obtenerDia() {
        String urlAPIconValidacionSQL = VariablesGenerales.urlAPI + ":" + VariablesGenerales.puertoAPI + "/comprobarDia/?url=" + MetodosSQL.URL + "&puerto=" + MetodosSQL.PUERTO + "&nombre=" + MetodosSQL.NOMBRE_BBDD + "&usuario=" + MetodosSQL.USUARIO + "&contrasenia=" + MetodosSQL.CONTRASENIA;
        JSONObject json = VariablesGenerales.almacenarJsonConUrl(urlAPIconValidacionSQL);
        if (json == null) {
            System.out.println("Hubo un error obteniendo el dia :/");
            VariablesGenerales.diaActual = 1;
            return false;
        } else {
            // Obtener el resultado de la consulta
            //String resultado = json.getString("message");
            //System.out.println(resultado);
            VariablesGenerales.diaActual = json.getInt("dia");
            //System.out.println(VariablesGenerales.diaActual);
            return true;
        }
    }
    
    public static boolean sumarDia() {
        String urlAPIconValidacionSQL = VariablesGenerales.urlAPI + ":" + VariablesGenerales.puertoAPI + "/actualizarDia/?url=" + MetodosSQL.URL + "&puerto=" + MetodosSQL.PUERTO + "&nombre=" + MetodosSQL.NOMBRE_BBDD + "&usuario=" + MetodosSQL.USUARIO + "&contrasenia=" + MetodosSQL.CONTRASENIA + "&diaActual=" + VariablesGenerales.diaActual;
        // Ejecutar la consulta a la API
        JSONObject json = VariablesGenerales.almacenarJsonConUrl(urlAPIconValidacionSQL);
        if (json == null) {
            System.out.println("Hubo un error cambiando el dia :/");
            return false;
        } else {
            // Obtener el resultado de la consulta
            //String resultado = json.getString("message");
            //System.out.println(resultado);
            return true;
        }
    }
    
    public static boolean realizarFacturaCliente(Mesa mesa, double importe, String diaRealizacion, String horaRealizacion, int pagado) {
        String urlAPIconValidacionSQL = VariablesGenerales.urlAPI + ":" + VariablesGenerales.puertoAPI + "/insertarFactura/?url=" + MetodosSQL.URL + "&puerto=" + MetodosSQL.PUERTO + "&nombre=" + MetodosSQL.NOMBRE_BBDD + "&usuario=" + MetodosSQL.USUARIO + "&contrasenia=" + MetodosSQL.CONTRASENIA + "&num_cliente="+mesa.getCliente().getnCliente()+"&importe="+importe+"&DiaRealizacion="+diaRealizacion+"&HoraRealizacion="+horaRealizacion+"&pagado="+pagado;
        // Ejecutar la consulta a la API
        JSONObject json = VariablesGenerales.almacenarJsonConUrl(urlAPIconValidacionSQL);
        if (json == null) {
            System.out.println("Hubo un error insertando la factura :/");
            return false;
        } else {
            //System.out.println("Factura realizada correctamente");
            return true;
        }
    }

    /*public static void obtenerCarta() {
        try {
            String sql = "SELECT * FROM Carta";

            Connection conn = DriverManager.getConnection(URLcompletaSQL, USUARIO, CONTRASENIA);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // Recorremos el ResultSet y añadimos cada valor al ArrayList
            while (rs.next()) {
                String nombrePlato = rs.getString("nombre_plato");
                System.out.println(nombrePlato);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Imprime el error en la consola
        }
    }*/
}
