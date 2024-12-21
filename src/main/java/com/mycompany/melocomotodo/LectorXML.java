/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.melocomotodo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;

/**
 *
 * @author Jorge
 */
public class LectorXML extends DefaultHandler {

    private boolean bUrl;
    private boolean bPuerto;
    private boolean bNombre;
    private boolean bUsuario;
    private boolean bContrasenia;
    private boolean bUrlAPI;
    private boolean bPuertoAPI;

    private String url;
    private String puerto;
    private String nombre;
    private String usuario;
    private String contrasenia;
    private String urlAPI;
    private String puertoAPI;

    // Almacena las configuraciones leídas del XML
    private HashMap<String, String> configuracion = new HashMap<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("url")) {
            bUrl = true;
        } else if (qName.equalsIgnoreCase("puerto")) {
            bPuerto = true;
        } else if (qName.equalsIgnoreCase("nombre")) {
            bNombre = true;
        } else if (qName.equalsIgnoreCase("usuario")) {
            bUsuario = true;
        } else if (qName.equalsIgnoreCase("contrasenia")) {
            bContrasenia = true;
        } else if (qName.equalsIgnoreCase("urlAPI")) {
            bUrlAPI = true;
        } else if (qName.equalsIgnoreCase("puertoAPI")) {
            bPuertoAPI = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String contenido = new String(ch, start, length).trim();
        if (bUrl) {
            url = contenido;
            configuracion.put("url", url);
            bUrl = false;
        } else if (bPuerto) {
            puerto = contenido;
            configuracion.put("puerto", puerto);
            bPuerto = false;
        } else if (bNombre) {
            nombre = contenido;
            configuracion.put("nombre", nombre);
            bNombre = false;
        } else if (bUsuario) {
            usuario = contenido;
            configuracion.put("usuario", usuario);
            bUsuario = false;
        } else if (bContrasenia) {
            contrasenia = contenido;
            configuracion.put("contrasenia", contrasenia);
            bContrasenia = false;
        } else if (bUrlAPI) {
            urlAPI = contenido;
            configuracion.put("urlAPI", urlAPI);
            bUrlAPI = false;
        } else if (bPuertoAPI) {
            puertoAPI = contenido;
            configuracion.put("puertoAPI", puertoAPI);
            bPuertoAPI = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // No se realiza acción adicional al cerrar elementos.
    }

    public HashMap<String, String> getConfiguracion() {
        return configuracion;
    }
}
