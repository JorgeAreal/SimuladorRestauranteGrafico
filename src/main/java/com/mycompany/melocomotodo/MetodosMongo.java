/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

/**
 *
 * @author Jorge Areal Alberich
 */
public class MetodosMongo {

    // Recibe la mesa y manda el pedido a mongoDB
    public static String insertarNuevoPedido(Mesa mesa) {
        try {
            // Codificar los parámetros de la URL
            String entrante = URLEncoder.encode(mesa.getEntrante().getNombre(), StandardCharsets.UTF_8.toString());
            String segundo = URLEncoder.encode(mesa.getSegundo().getNombre(), StandardCharsets.UTF_8.toString());
            String postre = URLEncoder.encode(mesa.getPostre().getNombre(), StandardCharsets.UTF_8.toString());
            String vinoNombre = URLEncoder.encode(mesa.getVino().getNombre(), StandardCharsets.UTF_8.toString());

            // Construir la URL con los parámetros codificados
            String urlAPIparaMONGO = VariablesGenerales.urlAPI + ":"+VariablesGenerales.puertoAPI+"/insertarPedidoMongo?"
                    + "nMesa=" + mesa.getnMesa()
                    + "&nCliente=" + mesa.getCliente().getnCliente()
                    + "&nCamarero=" + mesa.getCamarero().getIdCamarero()
                    + "&entrante=" + entrante
                    + "&precioEntrante=" + mesa.getEntrante().getPrecio()
                    + "&segundo=" + segundo
                    + "&precioSegundo=" + mesa.getSegundo().getPrecio()
                    + "&postre=" + postre
                    + "&precioPostre=" + mesa.getPostre().getPrecio()
                    + "&vinoNombre=" + vinoNombre
                    + "&vinoPrecio=" + mesa.getVino().getPrecio();
            
            // Ejecutar la consulta a la API
            JSONObject json = VariablesGenerales.almacenarJsonConUrl(urlAPIparaMONGO);

            if (json == null) {
                System.out.println("Hubo un error insertando el nuevo pedido :/");
                return null;
            } else {
                // Obtener el mensaje recibido
                String idNuevoPedido = json.getString("pedidoId");
                return idNuevoPedido;
            }
        } catch (Exception ex) {
            System.out.println("Error al construir o enviar la URL: " + ex.getMessage());
            return null;
        }
    }
    
    public static boolean actualizarEstadoComidaMongo(Mesa mesa, String tipoComida){
        // Construir el enlace a la API a traves de la información extraida del XML
        String urlAPIconValidacionSQL = VariablesGenerales.urlAPI+":"+VariablesGenerales.puertoAPI+"/actualizarEstadoComida?id="+mesa.getIdMongoPedido()+"&tipoComida="+tipoComida+"&estado=true";
        // Ejecutar la consulta a la API
        JSONObject json = VariablesGenerales.almacenarJsonConUrl(urlAPIconValidacionSQL);
        if (json == null) {
            System.out.println("Hubo un error cambiando el estado del plato "+tipoComida+" :/");
            return false;
        } else {
            //System.out.println("Estado cambiado correctamente");
            return true;
        }
    }
}
