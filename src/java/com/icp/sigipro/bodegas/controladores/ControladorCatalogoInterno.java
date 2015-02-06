/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icp.sigipro.bodegas.controladores;

import com.icp.sigipro.bodegas.dao.ProductoExternoDAO;
import com.icp.sigipro.bodegas.dao.ProductoInternoDAO;
import com.icp.sigipro.bodegas.dao.UbicacionBodegaDAO;
import com.icp.sigipro.bodegas.modelos.ProductoExterno;
import com.icp.sigipro.bodegas.modelos.ProductoInterno;
import com.icp.sigipro.bodegas.modelos.Reactivo;
import com.icp.sigipro.bodegas.modelos.UbicacionBodega;
import com.icp.sigipro.core.SIGIPROServlet;
import com.icp.sigipro.utilidades.HelpersHTML;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.security.sasl.AuthenticationException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Boga
 */
@WebServlet(name = "ControladorCatalogoInterno", urlPatterns = {"/Bodegas/CatalogoInterno"})
public class ControladorCatalogoInterno extends SIGIPROServlet
{

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
      /* TODO output your page here. You may use following sample code. */
      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servlet ControladorCatalogoInterno</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Servlet ControladorCatalogoInterno at " + request.getContextPath() + "</h1>");
      out.println("</body>");
      out.println("</html>");
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    try {
      String redireccion = "";
      String accion = request.getParameter("accion");
      ProductoInternoDAO dao = new ProductoInternoDAO();
      ProductoExternoDAO daoProductosExternos = new ProductoExternoDAO();
      UbicacionBodegaDAO daoUbicaciones = new UbicacionBodegaDAO();
      HttpSession sesion = request.getSession();
      List<Integer> listaPermisos = (List<Integer>) sesion.getAttribute("listaPermisos");
      int[] permisos = {11, 12, 13};

      if (accion != null) {
        if (accion.equalsIgnoreCase("ver")) {
          validarPermisos(permisos, listaPermisos);
          redireccion = "CatalogoInterno/Ver.jsp";
          int id_producto = Integer.parseInt(request.getParameter("id_producto"));
          ProductoInterno producto = dao.obtenerProductoInterno(id_producto);
          request.setAttribute("producto", producto);
        }
        else if (accion.equalsIgnoreCase("agregar")) {
          validarPermiso(11, listaPermisos);
          redireccion = "CatalogoInterno/Agregar.jsp";
          ProductoInterno producto = new ProductoInterno();
          List<UbicacionBodega> ubicaciones = daoUbicaciones.obtenerUbicacionesLimitado();
          List<ProductoExterno> productos = daoProductosExternos.obtenerProductosLimitado();
          request.setAttribute("producto", producto);
          request.setAttribute("ubicacionesRestantes", ubicaciones);
          request.setAttribute("productosExternosRestantes", productos);
          request.setAttribute("accion", "Agregar");
        }
        else if (accion.equalsIgnoreCase("eliminar")) {
          validarPermiso(13, listaPermisos);
          int id_producto = Integer.parseInt(request.getParameter("id_producto"));
          dao.eliminarProductoInterno(id_producto);
          redireccion = "CatalogoInterno/index.jsp";
          List<ProductoInterno> productos = dao.obtenerProductos();
          request.setAttribute("listaProductos", productos);
        }
        else if (accion.equalsIgnoreCase("editar")) {
          validarPermiso(12, listaPermisos);
          redireccion = "CatalogoInterno/Editar.jsp";
          int id_producto = Integer.parseInt(request.getParameter("id_producto"));
          ProductoInterno producto = dao.obtenerProductoInterno(id_producto);
          List<ProductoExterno> productosExternos = daoProductosExternos.obtenerProductos(id_producto);
          List<ProductoExterno> productosExternosRestantes = daoProductosExternos.obtenerProductosRestantes(id_producto);
          List<UbicacionBodega> ubicaciones = daoUbicaciones.obtenerUbicaciones(id_producto);
          List<UbicacionBodega> ubicacionesRestantes = daoUbicaciones.obtenerUbicacionesRestantes(id_producto);
          request.setAttribute("ubicacionesProducto", ubicaciones);
          request.setAttribute("ubicacionesRestantes", ubicacionesRestantes);
          request.setAttribute("productosExternos", productosExternos);
          request.setAttribute("productosExternosRestantes", productosExternosRestantes);
          request.setAttribute("producto", producto);
          request.setAttribute("accion", "Editar");
        }
        else {
          validarPermisos(permisos, listaPermisos);
          redireccion = "CatalogoInterno/index.jsp";
          List<ProductoInterno> productos = dao.obtenerProductos();
          request.setAttribute("listaProductos", productos);
        }
      }
      else {
        validarPermisos(permisos, listaPermisos);
        redireccion = "CatalogoInterno/index.jsp";
        List<ProductoInterno> productos = dao.obtenerProductos();
        request.setAttribute("listaProductos", productos);
      }

      RequestDispatcher vista = request.getRequestDispatcher(redireccion);
      vista.forward(request, response);
    }
    catch (AuthenticationException ex) {
      RequestDispatcher vista = request.getRequestDispatcher("/index.jsp");
      vista.forward(request, response);
    }

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    HelpersHTML helper = HelpersHTML.getSingletonHelpersHTML();
    try {
      request.setCharacterEncoding("UTF-8");
      boolean resultado = false;

      ProductoInterno productoInterno = new ProductoInterno();

      productoInterno.setNombre(request.getParameter("nombre"));
      productoInterno.setCodigo_icp(request.getParameter("codigoICP"));
      productoInterno.setStock_minimo(Integer.parseInt(request.getParameter("stockMinimo")));
      productoInterno.setStock_maximo(Integer.parseInt(request.getParameter("stockMaximo")));
      productoInterno.setUbicacion(request.getParameter("ubicacion"));
      productoInterno.setPresentacion(request.getParameter("presentacion"));
      productoInterno.setDescripcion(request.getParameter("descripcion"));

      String ubicaciones = request.getParameter("ubicaciones");
      String productosExternos = request.getParameter("productosExternos");
      if (request.getParameter("cuarentena") != null) {
        productoInterno.setCuarentena(true);
      }
      else {
        productoInterno.setCuarentena(false);
      }

      if (request.getParameter("reactivo") != null) {
        Reactivo r = new Reactivo();
        r.setNumero_cas(request.getParameter("numero_cas"));
        r.setFormula_quimica(request.getParameter("formula_quimica"));
        r.setFamilia(request.getParameter("familia"));
        r.setCantidad_botella_bodega(Integer.parseInt(request.getParameter("cantidad_botella_bodega")));
        r.setCantidad_botella_lab(Integer.parseInt(request.getParameter("cantidad_botella_lab")));
        r.setVolumen_bodega(request.getParameter("volumen_bodega"));
        r.setVolumen_lab(request.getParameter("volumen_lab"));
        productoInterno.setReactivo(r);
      }

      ProductoInternoDAO dao = new ProductoInternoDAO();
      String id = request.getParameter("id_producto");
      String redireccion;
      String mensajeResultado;

      if (id.isEmpty() || id.equals("0")) {
        resultado = dao.insertarProductoInterno(productoInterno, ubicaciones, productosExternos);
        mensajeResultado = "agregado";
        redireccion = "CatalogoInterno/Agregar.jsp";
      }
      else {
        productoInterno.setId_producto(Integer.parseInt(id));
        resultado = dao.editarProductoInterno(productoInterno, ubicaciones, productosExternos);
        mensajeResultado = "editado";
        redireccion = "CatalogoInterno/Editar.jsp";
      }
      String mensajeFinal;

      if (resultado) {
        mensajeResultado += " con éxito.";
        mensajeFinal = String.format("Producto interno %s", mensajeResultado);
        mensajeFinal = helper.mensajeDeExito(mensajeFinal);
        redireccion = "CatalogoInterno/index.jsp";
        List<ProductoInterno> productos = dao.obtenerProductos();
        
        request.setAttribute("listaProductos", productos);
      }
      else {
        mensajeResultado += " sin éxito.";
        mensajeFinal = String.format("Producto interno %s", mensajeResultado);
        mensajeFinal = helper.mensajeDeError(mensajeFinal);
        
        ProductoExternoDAO daoProductosExternos = new ProductoExternoDAO();
        UbicacionBodegaDAO daoUbicaciones = new UbicacionBodegaDAO();
        request.setAttribute("producto", productoInterno);
        request.setAttribute("ubicacionesProducto", daoUbicaciones.obtenerUbicaciones(productoInterno.getId_producto()));
        request.setAttribute("ubicacionesRestantes", daoUbicaciones.obtenerUbicacionesRestantes(productoInterno.getId_producto()));
        request.setAttribute("productosExternos", daoProductosExternos.obtenerProductos(productoInterno.getId_producto()));
        request.setAttribute("productosExternosRestantes", daoProductosExternos.obtenerProductosRestantes(productoInterno.getId_producto()));
        request.setAttribute("accion", "Agregar");
      }      
      request.setAttribute("mensaje", mensajeFinal);
      RequestDispatcher vista = request.getRequestDispatcher(redireccion);
      vista.forward(request, response);
    }
    catch (Exception ex) {
      request.setAttribute("mensaje", helper.mensajeDeError("Ha ocurrido un error inesperado. Por favor verifique que los datos estén correctos."));
      RequestDispatcher vista = request.getRequestDispatcher("CatalogoInterno/index.jsp");
      vista.forward(request, response);
    }
  }

  @Override
  public String getServletInfo()
  {
    return "Short description";
  }

  @Override
  protected int getPermiso()
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
