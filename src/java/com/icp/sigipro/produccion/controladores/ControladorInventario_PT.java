/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icp.sigipro.produccion.controladores;

import com.icp.sigipro.produccion.controladores.ControladorInventario_PT;
import com.icp.sigipro.produccion.modelos.Inventario_PT;
import com.icp.sigipro.bitacora.dao.BitacoraDAO;
import com.icp.sigipro.bitacora.modelo.Bitacora;
import com.icp.sigipro.core.SIGIPROException;
import com.icp.sigipro.core.SIGIPROServlet;
import com.icp.sigipro.produccion.dao.DespachoDAO;
import com.icp.sigipro.produccion.dao.Despachos_inventarioDAO;
import com.icp.sigipro.produccion.dao.Inventario_PTDAO;
import com.icp.sigipro.produccion.modelos.Despacho;
import com.icp.sigipro.produccion.modelos.Despachos_inventario;
import com.icp.sigipro.utilidades.HelperFechas;
import com.icp.sigipro.utilidades.HelpersHTML;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Amed
 */
@WebServlet(name = "ControladorInventario_PT", urlPatterns = {"/Produccion/Inventario_PT"})
public class ControladorInventario_PT extends SIGIPROServlet {

  private final int[] permisos = {602, 607, 603};
  private final Inventario_PTDAO dao = new Inventario_PTDAO();
  private final DespachoDAO despacho_dao = new DespachoDAO();
  private final Despachos_inventarioDAO despachos_inventario_dao = new Despachos_inventarioDAO();
  private final HelpersHTML helper = HelpersHTML.getSingletonHelpersHTML();

  protected final Class clase = ControladorInventario_PT.class;
  protected final List<String> accionesGet = new ArrayList<String>() {
    {
      add("index");
      add("ver_inventario");
      add("ver_despacho");
      add("ver_reservacion");
      add("ver_salida");
      add("agregar_despacho");
      add("agregar_inventario");
      add("agregar_reservacion");
      add("agregar_salida");
      add("editar_despacho");
      add("editar_inventario");
      add("editar_salida");
      add("editar_reservacion");
      add("firmar_despacho");
    }
  };
  protected final List<String> accionesPost = new ArrayList<String>() {
    {
      add("agregar_despacho");
      add("agregar_inventario");
      add("agregar_reservacion");
      add("agregar_salida");
      add("editar_despacho");
      add("editar_inventario");
      add("editar_salida");
      add("editar_reservacion");
      add("eliminar_despacho");
      add("eliminar_inventario");
      add("eliminar_reservacion");
      add("eliminar_salida");
    }
  };

  // <editor-fold defaultstate="collapsed" desc="Método Index">
  protected void getIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("inv_tab", "active");
    request = request_index(request);
    String redireccion = "Inventario_PT/index.jsp"; 
    redireccionar(request, response, redireccion);

  }

  protected HttpServletRequest request_index(HttpServletRequest request) throws ServletException, IOException {
    List<Integer> listaPermisos = getPermisosUsuario(request);
    Boolean admin = true;
    Boolean coordinador = false;
    Boolean regente = false;
    if ((verificarPermiso(603, listaPermisos) || (verificarPermiso(607, listaPermisos))) && !(verificarPermiso(602, listaPermisos))) {
      admin = false;
      if (verificarPermiso(603, listaPermisos)) {
        regente = true;
        request.setAttribute("inv_tab", "");
        request.setAttribute("des_tab", "active");
      }
      if (verificarPermiso(607, listaPermisos)) {
        coordinador = true;
        request.setAttribute("inv_tab", "");
        request.setAttribute("des_tab", "active");
      }
    };
    request.setAttribute("admin", admin);
    request.setAttribute("regente", regente);
    request.setAttribute("coordinador", coordinador);

    try {
      List<Inventario_PT> inventario = dao.obtenerInventario_PTs();
      request.setAttribute("inventario", inventario);
      List<Despacho> despachos = despacho_dao.obtenerDespachos();
      request.setAttribute("despachos", despachos);
    } catch (SIGIPROException sig_ex) {
      request.setAttribute("mensaje", helper.mensajeDeError(sig_ex.getMessage()));
    }

    return request;
  }

  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Métodos Agregar">
  protected void getAgregar_inventario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Integer> listaPermisos = getPermisosUsuario(request);
    validarPermisos(permisos, listaPermisos);
    String redireccion = "Inventario_PT/Agregar_inventario.jsp";
    Inventario_PT inventario = new Inventario_PT();
    request.setAttribute("inventario", inventario);
    request.setAttribute("accion", "agregar_inventario");

    redireccionar(request, response, redireccion);
  }

  protected void getAgregar_despacho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SIGIPROException {
    List<Integer> listaPermisos = getPermisosUsuario(request);
    validarPermisos(permisos, listaPermisos);
    String redireccion = "Inventario_PT/Agregar_despacho.jsp";
    Despacho despacho = new Despacho();
    List<Inventario_PT> lotes = dao.obtenerInventario_PTs();
    request.setAttribute("lotes", lotes);
    request.setAttribute("despacho", despacho);
    request.setAttribute("accion", "agregar_despacho");

    redireccionar(request, response, redireccion);
  }

  protected void getAgregar_reservacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Integer> listaPermisos = getPermisosUsuario(request);
    validarPermisos(permisos, listaPermisos);
    String redireccion = "Inventario_PT/Agregar_reservacion.jsp";
//        Inventario_PT pie = new Inventario_PT();
//        request.setAttribute("pie", pie);
    request.setAttribute("accion", "agregar_reservacion");

    redireccionar(request, response, redireccion);
  }

  protected void getAgregar_salida(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Integer> listaPermisos = getPermisosUsuario(request);
    validarPermisos(permisos, listaPermisos);
    String redireccion = "Inventario_PT/Agregar_salida.jsp";
//        Inventario_PT pie = new Inventario_PT();
//        request.setAttribute("pie", pie);
    request.setAttribute("accion", "agregar_salida");

    redireccionar(request, response, redireccion);
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Métodos Editar">

  protected void getEditar_inventario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Integer> listaPermisos = getPermisosUsuario(request);
    validarPermisos(permisos, listaPermisos);
    String redireccion = "Inventario_PT/Editar_inventario.jsp";
    int id_inventario_pt = Integer.parseInt(request.getParameter("id_inventario_pt"));
    request.setAttribute("accion", "Editar_inventario");
    try {
      Inventario_PT inventario_pt = dao.obtenerInventario_PT(id_inventario_pt);
      request.setAttribute("inventario", inventario_pt);
    } catch (SIGIPROException ex) {
      request.setAttribute("mensaje", helper.mensajeDeError(ex.getMessage()));
    }
    redireccionar(request, response, redireccion);
  }

  protected void getEditar_despacho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Integer> listaPermisos = getPermisosUsuario(request);
    validarPermisos(permisos, listaPermisos);
    String redireccion = "Inventario_PT/Editar_despacho.jsp";
    int id_despacho = Integer.parseInt(request.getParameter("id_despacho"));
    request.setAttribute("accion", "Editar_despacho");
    try {
      Despacho despacho = despacho_dao.obtenerDespacho(id_despacho);
      request.setAttribute("despacho", despacho);
      List<Inventario_PT> lotes = dao.obtenerInventario_PTs();
      request.setAttribute("lotes", lotes);
      List<Despachos_inventario> despachos_inventarios = despachos_inventario_dao.obtenerDespachos_inventarios(id_despacho);
      request.setAttribute("despachos_inventarios", despachos_inventarios);
    } catch (SIGIPROException ex) {
      request.setAttribute("mensaje", helper.mensajeDeError(ex.getMessage()));
    }
    redireccionar(request, response, redireccion);
  }
  
  protected void getFirmar_despacho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    boolean resultado = false;
    String redireccion = "Inventario_PT/Index.jsp";
    int id_despacho = Integer.parseInt(request.getParameter("id_despacho"));
    int id_usuario = getIdUsuario(request);
    String tipo = request.getParameter("tipo");
    try {
      if (tipo.equals("c")) {
        despacho_dao.aprobar_Coordinador(id_usuario, id_despacho);
      } else {
        despacho_dao.aprobar_Regente(id_usuario, id_despacho);
      }
      BitacoraDAO bitacora = new BitacoraDAO();
      bitacora.setBitacora(id_despacho, Bitacora.ACCION_APROBAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_DESPACHOS, request.getRemoteAddr());

      redireccion = "Inventario_PT/index.jsp";
      request = request_index(request);
      request.setAttribute("des_tab", "active");
      request.setAttribute("mensaje", helper.mensajeDeExito("Despacho firmado correctamente."));
    } catch (SIGIPROException ex) {
      request.setAttribute("mensaje", helper.mensajeDeError(ex.getMessage()));
    }

    redireccionar(request, response, redireccion);
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Métodos Ver">

  protected void getVer_despacho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Integer> listaPermisos = getPermisosUsuario(request);
    validarPermisos(permisos, listaPermisos);
    String redireccion = "Inventario_PT/Ver.jsp";
    int id_pie = Integer.parseInt(request.getParameter("id_pie"));
//        try {
//            Inventario_PT pie = dao.obtenerInventario_PT(id_pie);
//            request.setAttribute("pie", pie);
//        }
//        catch (SIGIPROException ex) {
//            request.setAttribute("mensaje", helper.mensajeDeError(ex.getMessage()));
//        }
    redireccionar(request, response, redireccion);
  }

  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Métodos Post Agregar">
  protected void postAgregar_inventario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    boolean resultado = false;
    String redireccion = "Inventario_PT/Agregar_inventario.jsp";
//        try {
//            Inventario_PT pie = construirObjeto(request);
//
//            dao.insertarInventario_PT(pie);
//
//            BitacoraDAO bitacora = new BitacoraDAO();
//            bitacora.setBitacora(pie.parseJSON(), Bitacora.ACCION_AGREGAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_SOLICITUD, request.getRemoteAddr());
//
//            redireccion = "Inventario_PT/index.jsp";
//            request.setAttribute("mensaje", helper.mensajeDeExito("Inventario_PT agregado correctamente."));
//        }
//        catch (SIGIPROException ex) {
//            request.setAttribute("mensaje", helper.mensajeDeError(ex.getMessage()));
//        }
//
//        try {
//            List<Inventario_PT> pies = dao.obtenerInventario_PT();
//            request.setAttribute("pies", pies);
//        }
//        catch (SIGIPROException sig_ex) {
//            request.setAttribute("mensaje", helper.mensajeDeError(sig_ex.getMessage()));
//        }
    redireccionar(request, response, redireccion);
  }

  protected void postAgregar_despacho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    boolean resultado = false;
    String redireccion = "Inventario_PT/Agregar_despacho.jsp";
    try {
      Despacho despacho = construirDespacho(request);

      int id_despacho = despacho_dao.insertarDespacho(despacho);
      BitacoraDAO bitacora = new BitacoraDAO();
      bitacora.setBitacora(despacho.parseJSON(), Bitacora.ACCION_AGREGAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_DESPACHOS, request.getRemoteAddr());

      ArrayList<int[]> lotes = construirLotes(request);
      despachos_inventario_dao.insertarDespachos_inventario(lotes, id_despacho);

      redireccion = "Inventario_PT/index.jsp";
      request = request_index(request);
      request.setAttribute("des_tab", "active");
      request.setAttribute("mensaje", helper.mensajeDeExito("Despacho agregado correctamente."));
    } catch (SIGIPROException ex) {
      request.setAttribute("mensaje", helper.mensajeDeError(ex.getMessage()));
    } catch (NumberFormatException ex) {
      redireccion = "Inventario_PT/index.jsp";
      request = request_index(request);
      request.setAttribute("des_tab", "active");
      request.setAttribute("mensaje", helper.mensajeDeAdvertencia("Despacho agregado sin Lotes de Producto"));
    }

    redireccionar(request, response, redireccion);
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Métodos Post Editar">

  protected void postEditar_inventario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    boolean resultado = false;
    String redireccion = "Inventario_PT/Editar_inventario.jsp";
//        try {
//            Inventario_PT pie = construirObjeto(request);
//            dao.editarInventario_PT(pie);
//
//            BitacoraDAO bitacora = new BitacoraDAO();
//            bitacora.setBitacora(pie.parseJSON(), Bitacora.ACCION_EDITAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_SOLICITUD, request.getRemoteAddr());
//
//            redireccion = "Inventario_PT/index.jsp";
//            request.setAttribute("mensaje", helper.mensajeDeExito("Inventario_PT editado correctamente."));
//        }
//        catch (SIGIPROException ex) {
//            request.setAttribute("mensaje", helper.mensajeDeError(ex.getMessage()));
//        }
//
//        try {
//            List<Inventario_PT> pies = dao.obtenerInventario_PT();
//            request.setAttribute("pies", pies);
//        }
//        catch (SIGIPROException sig_ex) {
//            request.setAttribute("mensaje", helper.mensajeDeError(sig_ex.getMessage()));
//        }
    redireccionar(request, response, redireccion);
  }

  protected void postEditar_despacho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    boolean resultado = false;
    String redireccion = "Inventario_PT/Editar_despacho.jsp";
    try {
      Despacho despacho = construirDespacho(request);
      ArrayList<int[]> lotes = construirLotes(request);
      despacho_dao.editarDespacho(despacho);
      despacho_dao.reset_total(despacho.getId_despacho());
      despachos_inventario_dao.eliminarDespachos_inventario(despacho.getId_despacho());
      despachos_inventario_dao.insertarDespachos_inventario(lotes, despacho.getId_despacho());

      BitacoraDAO bitacora = new BitacoraDAO();
      bitacora.setBitacora(despacho.parseJSON(), Bitacora.ACCION_EDITAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_DESPACHOS, request.getRemoteAddr());

      redireccion = "Inventario_PT/index.jsp";
      request = request_index(request);
      request.setAttribute("des_tab", "active");
      request.setAttribute("mensaje", helper.mensajeDeExito("Despacho editado correctamente."));
    } catch (SIGIPROException ex) {
      request.setAttribute("mensaje", helper.mensajeDeError(ex.getMessage()));
    } catch (NumberFormatException ex) {
      redireccion = "Inventario_PT/index.jsp";
      request = request_index(request);
      request.setAttribute("des_tab", "active");
      request.setAttribute("mensaje", helper.mensajeDeError("Error al editar despacho: Debe seleccionar uno o varios Lotes de Producto"));
    }

    redireccionar(request, response, redireccion);
  }



  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Métodos Post Eliminar">
  protected void postEliminar_inventario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Integer> listaPermisos = getPermisosUsuario(request);
    validarPermisos(permisos, listaPermisos);
    int id_inventario = Integer.parseInt(request.getParameter("id_eliminar"));
    String redireccion = "Inventario_PT/index.jsp";
    try {
      dao.eliminarInventario_PT(id_inventario);

      BitacoraDAO bitacora = new BitacoraDAO();
      bitacora.setBitacora(id_inventario, Bitacora.ACCION_ELIMINAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_INVENTARIO_PT, request.getRemoteAddr());

      request.setAttribute("mensaje", helper.mensajeDeExito("Inventario eliminado correctamente."));
    } catch (SIGIPROException ex) {
      request.setAttribute("mensaje", helper.mensajeDeError(ex.getMessage()));
      redireccionar(request, response, redireccion);
    }
    request = request_index(request);
    request.setAttribute("inv_tab", "active");

    redireccionar(request, response, redireccion);
  }

  protected void postEliminar_despacho(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Integer> listaPermisos = getPermisosUsuario(request);
    validarPermisos(permisos, listaPermisos);
    int id_despacho = Integer.parseInt(request.getParameter("id_eliminar"));
    String redireccion = "Inventario_PT/index.jsp";
    try {
      //Despacho despacho = despacho_dao.obtenerDespacho(id_despacho);
      despacho_dao.eliminarDespacho(id_despacho);

      BitacoraDAO bitacora = new BitacoraDAO();
      bitacora.setBitacora(id_despacho, Bitacora.ACCION_ELIMINAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_DESPACHOS, request.getRemoteAddr());

      request.setAttribute("mensaje", helper.mensajeDeExito("Despacho eliminado correctamente."));
    } catch (SIGIPROException ex) {
      request.setAttribute("mensaje", helper.mensajeDeError(ex.getMessage()));
      redireccionar(request, response, redireccion);
    }
    request = request_index(request);
    request.setAttribute("des_tab", "active");

    redireccionar(request, response, redireccion);
  }

  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Métodos Modelo">
  private Inventario_PT construirObjeto(HttpServletRequest request) throws SIGIPROException {
    Inventario_PT pie = new Inventario_PT();

//        int id_pie = Integer.parseInt(request.getParameter("id_pie"));
//        pie.setId_pie(id_pie);
//        pie.setCodigo(request.getParameter("codigo"));
//        pie.setFuente(request.getParameter("fuente"));
//        String fecha_ingreso_str = request.getParameter("fecha_ingreso");
//        String fecha_retiro_str = request.getParameter("fecha_retiro");
//
//        try {
//            HelperFechas helper_fechas = HelperFechas.getSingletonHelperFechas();
//            pie.setFecha_ingreso(helper_fechas.formatearFecha(fecha_ingreso_str));
//            pie.setFecha_retiro(helper_fechas.formatearFecha(fecha_retiro_str));
//        }
//        catch (ParseException ex) {
//            ex.printStackTrace();
//        }
    return pie;
  }

  private Despacho construirDespacho(HttpServletRequest request) throws SIGIPROException {
    Despacho despacho = new Despacho();

    int id_despacho = Integer.parseInt(request.getParameter("id_despacho"));
    despacho.setId_despacho(id_despacho);
    despacho.setDestino(request.getParameter("destino"));
    String fecha = request.getParameter("fecha");

    try {
      HelperFechas helper_fechas = HelperFechas.getSingletonHelperFechas();
      despacho.setFecha(helper_fechas.formatearFecha(fecha));
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    return despacho;
  }

  private ArrayList<int[]> construirLotes(HttpServletRequest request) throws NumberFormatException {
    ArrayList<int[]> resultado = new ArrayList<>();
    String lotes = request.getParameter("rolesUsuario");
    String[] split_lotes_1 = lotes.split("#r#");
    for (int i = 0; i < split_lotes_1.length; i++) {
      int[] sub_lista;
      sub_lista = new int[2];
      String[] split_lote = split_lotes_1[i].split("#c#");
      sub_lista[0] = Integer.parseInt(split_lote[0]);
      sub_lista[1] = Integer.parseInt(split_lote[1]);
      resultado.add(sub_lista);

    }
    return resultado;
  }

  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Métodos abstractos sobreescritos">
  @Override
  protected void ejecutarAccion(HttpServletRequest request, HttpServletResponse response, String accion, String accionHTTP) throws ServletException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    List<String> lista_acciones;
    if (accionHTTP.equals("get")) {
      lista_acciones = accionesGet;
    } else {
      lista_acciones = accionesPost;
    }
    if (lista_acciones.contains(accion.toLowerCase())) {
      String nombreMetodo = accionHTTP + Character.toUpperCase(accion.charAt(0)) + accion.substring(1);
      Method metodo = clase.getDeclaredMethod(nombreMetodo, HttpServletRequest.class, HttpServletResponse.class);
      metodo.invoke(this, request, response);
    } else {
      Method metodo = clase.getDeclaredMethod(accionHTTP + "Index", HttpServletRequest.class, HttpServletResponse.class);
      metodo.invoke(this, request, response);
    }
  }

  @Override
  protected int getPermiso() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  // </editor-fold>
}
