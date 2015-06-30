/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icp.sigipro.controlcalidad.controladores;

import com.icp.sigipro.bitacora.dao.BitacoraDAO;
import com.icp.sigipro.bitacora.modelo.Bitacora;
import com.icp.sigipro.controlcalidad.dao.EquipoDAO;
import com.icp.sigipro.controlcalidad.modelos.CertificadoEquipo;
import com.icp.sigipro.controlcalidad.modelos.Equipo;
import com.icp.sigipro.core.SIGIPROServlet;
import com.icp.sigipro.utilidades.HelpersHTML;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author ld.conejo
 */
@WebServlet(name = "ControladorEquipo", urlPatterns = {"/ControlCalidad/Equipo"})
public class ControladorEquipo extends SIGIPROServlet {

    //Falta implementar
    private final int[] permisos = {1,510};
    //-----------------
    private EquipoDAO dao = new EquipoDAO();
    
    HelpersHTML helper = HelpersHTML.getSingletonHelpersHTML();
    BitacoraDAO bitacora = new BitacoraDAO(); 

    protected final Class clase = ControladorEquipo.class;
    protected final List<String> accionesGet = new ArrayList<String>()
    {
        {
            add("index");
            add("ver");
            add("agregar");
            add("eliminar");
            add("editar");
            add("certificado");
        }
    };
    protected final List<String> accionesPost = new ArrayList<String>()
    {
        {
            add("agregareditar");
        }
    };

  // <editor-fold defaultstate="collapsed" desc="Métodos Get">
  
    protected void getCertificado(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermisos(permisos, listaPermisos);
        
        int id_certificado_equipo = Integer.parseInt(request.getParameter("id_certificado_equipo"));
        String equipo = request.getParameter("nomhre");
        CertificadoEquipo certificado = dao.obtenerCertificado(id_certificado_equipo);
        
        String filename = certificado.getPath();
        File file = new File(filename);
        
        if(file.exists()){
            ServletContext ctx = getServletContext();
            InputStream fis = new FileInputStream(file);
            String mimeType = ctx.getMimeType(file.getAbsolutePath());
            
            response.setContentType(mimeType != null? mimeType:"application/octet-stream");
            response.setContentLength((int) file.length());
            String nombre = "certificado-"+equipo+"."+this.getFileExtension(filename);
            response.setHeader("Content-Disposition", "attachment; filename=\""+nombre+"\"");
            
            ServletOutputStream os = response.getOutputStream();
            byte[] bufferData = new byte[1024];
            int read=0;
            while ((read=fis.read(bufferData))!=-1){
                os.write(bufferData,0,read);
            }
            os.flush();
            os.close();
            fis.close();
            
        }
        
    }
    
    protected void getAgregar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermiso(510, listaPermisos);

        String redireccion = "Equipo/Agregar.jsp";
        Equipo tr = new Equipo();
        request.setAttribute("equipo", tr);
        request.setAttribute("accion", "Agregar");
        redireccionar(request, response, redireccion);
    }

    protected void getIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermisos(permisos, listaPermisos);
        String redireccion = "Equipo/index.jsp";
        List<Equipo> equipos = dao.obtenerEquipos();
        request.setAttribute("listaTipos", equipos);
        redireccionar(request, response, redireccion);
    }

    protected void getVer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println(request.getServletContext().getAttribute("FILES_DIR"));
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermisos(permisos, listaPermisos);
        String redireccion = "Equipo/Ver.jsp";
        int id_equipo = Integer.parseInt(request.getParameter("id_equipo"));
        try {
            Equipo tr = dao.obtenerEquipo(id_equipo);
            request.setAttribute("equipo", tr);
            redireccionar(request, response, redireccion);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    protected void getEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermiso(510, listaPermisos);
        String redireccion = "Equipo/Editar.jsp";
        int id_equipo = Integer.parseInt(request.getParameter("id_equipo"));
        Equipo equipo = dao.obtenerEquipo(id_equipo);
        request.setAttribute("equipo", equipo);
        request.setAttribute("accion", "Editar");
        redireccionar(request, response, redireccion);

    }

    protected void getEliminar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermiso(510, listaPermisos);
        int id_equipo = Integer.parseInt(request.getParameter("id_equipo"));
        boolean resultado = false;
        try{
            resultado = dao.eliminarEquipo(id_equipo);
            if (resultado){
                //Funcion que genera la bitacora 
                bitacora.setBitacora(id_equipo,Bitacora.ACCION_ELIMINAR,request.getSession().getAttribute("usuario"),Bitacora.TABLA_EQUIPO,request.getRemoteAddr()); 
                //----------------------------
                request.setAttribute("mensaje", helper.mensajeDeExito("Tipo de Reactivo eliminado correctamente")); 
            }
            else{
               request.setAttribute("mensaje", helper.mensajeDeError("Tipo de Reactivo no pudo ser eliminado ya que tiene reactivos asociados."));  
            }
            this.getIndex(request, response);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("mensaje", helper.mensajeDeError("Tipo de Reactivo no pudo ser eliminado ya que tiene reactivos asociados."));  
            this.getIndex(request, response);
        }
        
    }
    // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Métodos Post">
  
    protected void postAgregareditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        boolean resultado = false;
        try{
            //Se crea el Path en la carpeta del Proyecto
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");
            fullPath = pathArr[0];
            String ubicacion = new File(fullPath).getPath() + File.separatorChar + "Documentos" + File.separatorChar + "Equipo" + File.separatorChar + "Machotes";
            //-------------------------------------------
            System.out.println(ubicacion);
            //Crea los directorios si no estan creados aun
            this.crearDirectorio(ubicacion);
            //--------------------------------------------
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File(ubicacion));
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            Equipo tr = construirObjeto(items,request,ubicacion);
            
            if (tr.getId_equipo()==0){
                resultado = dao.insertarEquipo(tr);
                if (resultado){
                    request.setAttribute("mensaje", helper.mensajeDeExito("Tipo de Reactivo agregado correctamente"));        
                    //Funcion que genera la bitacora
                    bitacora.setBitacora(tr.parseJSON(),Bitacora.ACCION_AGREGAR,request.getSession().getAttribute("usuario"),Bitacora.TABLA_EQUIPO,request.getRemoteAddr());
                    //*----------------------------*
                    this.getIndex(request, response);
                }else{
                    request.setAttribute("mensaje", helper.mensajeDeError("Tipo de Reactivo no pudo ser agregado. Inténtelo de nuevo."));        
                    this.getAgregar(request, response);
                }
            }else{
                resultado = dao.editarEquipo(tr);
                if (resultado){
                    //Funcion que genera la bitacora
                    bitacora.setBitacora(tr.parseJSON(),Bitacora.ACCION_EDITAR,request.getSession().getAttribute("usuario"),Bitacora.TABLA_EQUIPO,request.getRemoteAddr());
                    //*----------------------------*
                    request.setAttribute("mensaje", helper.mensajeDeExito("Tipo de Reactivo editado correctamente"));
                    this.getIndex(request, response);
                }
                else{
                    request.setAttribute("mensaje", helper.mensajeDeError("Tipo de Reactivo no pudo ser editado. Inténtelo de nuevo."));
                    request.setAttribute("id_equipo",tr.getId_equipo());
                    this.getEditar(request, response);
                }
            }
        }catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
        }

    }
    
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Métodos Modelo">
  
    private Equipo construirObjeto(List<FileItem> items,HttpServletRequest request,String ubicacion) {
        Equipo tr = new Equipo();
        for (FileItem item : items) {
            if (item.isFormField()) {
                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                String fieldName = item.getFieldName();
                String fieldValue;
                try {
                    fieldValue = item.getString("UTF-8").trim();
                }
                catch (UnsupportedEncodingException ex) {
                    fieldValue = item.getString();
                }
                switch(fieldName){
                    case "nombre":
                        tr.setNombre(fieldValue);
                        break;
                    case "descripcion":
                        tr.setDescripcion(fieldValue);
                        break;
                    case "id_equipo":
                        int id_equipo = Integer.parseInt(fieldValue);
                        tr.setId_equipo(id_equipo);
                        break;
                }    
            } else {
                try {
                    System.out.println(item.getSize());
                    if(item.getSize() != 0){
                        this.crearDirectorio(ubicacion);
                        //Creacion del nombre
                        Date dNow = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddhhmm");
                        String fecha = ft.format(dNow);
                        String extension = this.getFileExtension(item.getName());
                        String nombre = tr.getNombre()+"-"+fecha+"."+extension;
                        //---------------------
                        File archivo = new File(ubicacion,nombre);
                        item.write(archivo);
                        tr.setMachote(archivo.getAbsolutePath());
                    }else{
                        tr.setMachote("");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }
    }
        return tr;
    }
    
    private boolean crearDirectorio(String path){
        boolean resultado = false;
        File directorio = new File(path);
        if (!directorio.exists()) {
            System.out.println("Creando directorio: " + path);
            resultado = false;
            try{
                directorio.mkdirs();
                resultado = true;
            } 
            catch(SecurityException se){
                se.printStackTrace();
            }        
            if(resultado) {    
                System.out.println("Directorio Creado");  
            }
        }else{
            resultado=true;
        }
        return resultado;
    }
    
    private String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
  
  // </editor-fold>
  
  // <editor-fold defaultstate="collapsed" desc="Métodos abstractos sobreescritos">
  @Override
  protected void ejecutarAccion(HttpServletRequest request, HttpServletResponse response, String accion, String accionHTTP) throws ServletException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
      List<String> lista_acciones;
      if (accionHTTP.equals("get")){
          lista_acciones = accionesGet; 
      } else {
          lista_acciones = accionesPost;
      }
    if (lista_acciones.contains(accion.toLowerCase())) {
      String nombreMetodo = accionHTTP + Character.toUpperCase(accion.charAt(0)) + accion.substring(1);
      Method metodo = clase.getDeclaredMethod(nombreMetodo, HttpServletRequest.class, HttpServletResponse.class);
      metodo.invoke(this, request, response);
    }
    else {
      Method metodo = clase.getDeclaredMethod(accionHTTP + "Index", HttpServletRequest.class, HttpServletResponse.class);
      metodo.invoke(this, request, response);
    }
  }

  @Override
  protected int getPermiso()
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  // </editor-fold>
}