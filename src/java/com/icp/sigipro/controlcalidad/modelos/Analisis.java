/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icp.sigipro.controlcalidad.modelos;

import java.lang.reflect.Field;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author ld.conejo
 */
public class Analisis {
    private int id_analisis;
    private String nombre;
    private int cantidad_pendiente;
    //Debe ser de tipo XML pero se define luego
    private String estructura; 
    //PATH de la ubicacion del archivo XSL
    private String machote;
    
    private List<TipoEquipo> tipos_equipos_analisis;
    private List<TipoReactivo> tipos_reactivos_analisis;
    private List<TipoMuestra> tipos_muestras_analisis;

    public Analisis() {
    }

    public int getCantidad_pendiente() {
        return cantidad_pendiente;
    }

    public void setCantidad_pendiente(int cantidad_pendiente) {
        this.cantidad_pendiente = cantidad_pendiente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_analisis() {
        return id_analisis;
    }

    public void setId_analisis(int id_analisis) {
        this.id_analisis = id_analisis;
    }

    public String getEstructura() {
        return estructura;
    }

    public void setEstructura(String estructura) {
        this.estructura = estructura;
    }

    public String getMachote() {
        return machote;
    }

    public void setMachote(String machote) {
        this.machote = machote;
    }

    public List<TipoEquipo> getTipos_equipos_analisis() {
        return tipos_equipos_analisis;
    }

    public void setTipos_equipos_analisis(List<TipoEquipo> tipos_equipos_analisis) {
        this.tipos_equipos_analisis = tipos_equipos_analisis;
    }

    public List<TipoReactivo> getTipos_reactivos_analisis() {
        return tipos_reactivos_analisis;
    }

    public void setTipos_reactivos_analisis(List<TipoReactivo> tipos_reactivos_analisis) {
        this.tipos_reactivos_analisis = tipos_reactivos_analisis;
    }

    public List<TipoMuestra> getTipos_muestras_analisis() {
        return tipos_muestras_analisis;
    }

    public void setTipos_muestras_analisis(List<TipoMuestra> tipos_muestras_analisis) {
        this.tipos_muestras_analisis = tipos_muestras_analisis;
    }
    
    public String parseJSON(){
        Class _class = this.getClass();
        JSONObject JSON = new JSONObject();
        try{
            Field properties[] = _class.getDeclaredFields();
            for (int i = 0; i < properties.length; i++) {
                Field field = properties[i];
                if (i != 0){
                    JSON.put(field.getName(), field.get(this));
                }else{
                    JSON.put("id_objeto", field.get(this));
                }
            }
                    
        }catch (Exception e){
            
        }
        return JSON.toString();
    }
    
}
