/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icp.sigipro.seguridad.modelos;

import com.icp.sigipro.core.IModelo;

/**
 *
 * @author Amed
 */
public class Seccion extends IModelo{
  int id_seccion;
  String nombre_seccion;
  String descripcion;
  
  public Seccion( Integer pid, String pnombre, String pdesc){
  id_seccion = pid;
  nombre_seccion = pnombre;
  descripcion = pdesc;
  }

  public int getId_seccion()
  {
    return id_seccion;
  }

  public void setId_seccion(int id_seccion)
  {
    this.id_seccion = id_seccion;
  }

  public String getNombre_seccion()
  {
    return nombre_seccion;
  }

  public void setNombre_seccion(String nombre_seccion)
  {
    this.nombre_seccion = nombre_seccion;
  }
  
  public int getID()                 {return id_seccion;}
  public String getNombreSeccion()   {return nombre_seccion;}
  public String getDescripcion()          {return descripcion;}
}
