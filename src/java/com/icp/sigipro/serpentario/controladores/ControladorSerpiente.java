/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icp.sigipro.serpentario.controladores;

import com.icp.sigipro.bitacora.dao.BitacoraDAO;
import com.icp.sigipro.bitacora.modelo.Bitacora;
import com.icp.sigipro.core.SIGIPROServlet;
import com.icp.sigipro.seguridad.dao.UsuarioDAO;
import com.icp.sigipro.seguridad.modelos.Usuario;
import com.icp.sigipro.serpentario.dao.CatalogoTejidoDAO;
import com.icp.sigipro.serpentario.dao.ColeccionHumedaDAO;
import com.icp.sigipro.serpentario.dao.EspecieDAO;
import com.icp.sigipro.serpentario.dao.EventoDAO;
import com.icp.sigipro.serpentario.dao.SerpienteDAO;
import com.icp.sigipro.serpentario.modelos.CatalogoTejido;
import com.icp.sigipro.serpentario.modelos.ColeccionHumeda;
import com.icp.sigipro.serpentario.modelos.Especie;
import com.icp.sigipro.serpentario.modelos.Evento;
import com.icp.sigipro.serpentario.modelos.HelperSerpiente;
import com.icp.sigipro.serpentario.modelos.Serpiente;
import com.icp.sigipro.utilidades.HelpersHTML;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.postgresql.util.Base64;

/**
 *
 * @author ld.conejo
 */
@WebServlet(name = "ControladorSerpiente", urlPatterns = {"/Serpentario/Serpiente"})
public class ControladorSerpiente extends SIGIPROServlet {

    //Agregar, editar, eventos, decesos
    private final int[] permisos = {1, 310, 311, 312, 313, 314, 315,316,317,318, 319};
    private SerpienteDAO dao = new SerpienteDAO();
    private EventoDAO eventodao = new EventoDAO();
    private EspecieDAO especiedao = new EspecieDAO();
    private BitacoraDAO bitacora = new BitacoraDAO();
    private CatalogoTejidoDAO ctdao = new CatalogoTejidoDAO();
    private ColeccionHumedaDAO chdao = new ColeccionHumedaDAO();

    HelpersHTML helper = HelpersHTML.getSingletonHelpersHTML();

    protected final Class clase = ControladorSerpiente.class;
    protected final List<String> accionesGet = new ArrayList<String>() {
        {
            add("index");
            add("ver");
            add("agregar");
            add("editar");
            add("editarevento");
            add("eliminarevento");
        }
    };
    protected final List<String> accionesPost = new ArrayList<String>() {
        {
            add("evento");
            add("agregareditar");
            add("reversarcv");
            add("descartar");
            add("reversardeceso");
            add("deceso");
            add("coleccionviva");
            add("editarevento");
        }
    };
    protected final List<String> sexo = new ArrayList<String>() {
        {
            add("Macho");
            add("Hembra");
            add("Indefinido");
        }
    };

    protected final List<String> tipo_Eventos = new ArrayList<String>() {
        {
            add("Defecación");
            add("CambioPiel");
            add("Desparasitación");
            add("Alimentación");
            add("Otros");

        }
    };

  // <editor-fold defaultstate="collapsed" desc="Métodos Get">
    protected void getAgregar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermiso(310, listaPermisos);

        String redireccion = "Serpiente/Agregar.jsp";
        Serpiente s = new Serpiente();
        EspecieDAO especiedao = new EspecieDAO();
        List<Especie> especies = especiedao.obtenerEspecies();
        request.setAttribute("helper", HelpersHTML.getSingletonHelpersHTML());
        request.setAttribute("serpiente", s);
        request.setAttribute("especies", especies);
        request.setAttribute("imagenSerpiente", this.obtenerImagen(s));
        request.setAttribute("accion", "Agregar");
        request.setAttribute("siguiente", dao.obtenerProximoId());
        request.setAttribute("sexos", sexo);
        redireccionar(request, response, redireccion);
    }

    protected void getIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermisos(permisos, listaPermisos);
        String redireccion = "Serpiente/index.jsp";
        List<Serpiente> serpientes = dao.obtenerSerpientes();
        request.setAttribute("listaSerpientes", serpientes);
        redireccionar(request, response, redireccion);
    }

    protected void getVer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermisos(permisos, listaPermisos);
        String redireccion = "Serpiente/Ver.jsp";
        int id_serpiente = Integer.parseInt(request.getParameter("id_serpiente"));
        try {
            Serpiente s = dao.obtenerSerpiente(id_serpiente);
            request.setAttribute("serpiente", s);
            request.setAttribute("helper", HelpersHTML.getSingletonHelpersHTML());
            request.setAttribute("imagenSerpiente", this.obtenerImagen(s));
            List<Evento> eventos = eventodao.obtenerEventos(id_serpiente);
            request.setAttribute("listaEventos", eventos);
            request.setAttribute("listaTipoEventos", tipo_Eventos);
            request.setAttribute("siguienteCH", dao.obtenerProximoIdCH());
            request.setAttribute("siguienteCT", dao.obtenerProximoIdCT());
            request.setAttribute("coleccionhumeda", null);
            request.setAttribute("catalogotejido", null);
            request.setAttribute("descarte", false);
            for (Evento i : eventos) {
                if (i.getId_categoria() == 5) {
                    request.setAttribute("coleccionViva", i);
                } else if (i.getId_categoria() == 6) {
                    request.setAttribute("deceso", i);
                } else if (i.getId_categoria() == 7) {
                    request.setAttribute("coleccionhumeda", i);
                } else if (i.getId_categoria() == 8) {
                    request.setAttribute("catalogotejido", i);
                } else if (i.getId_categoria() == 14) {
                    request.setAttribute("descarte", true);
                }
            }

            redireccionar(request, response, redireccion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected void getEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        if (verificarPermiso(315, listaPermisos) || verificarPermiso(311, listaPermisos)) {
            String redireccion = "Serpiente/Editar.jsp";
            int id_serpiente = Integer.parseInt(request.getParameter("id_serpiente"));
            Serpiente serpiente = dao.obtenerSerpiente(id_serpiente);
            List<Especie> especies = especiedao.obtenerEspecies();
            request.setAttribute("especies", especies);
            request.setAttribute("serpiente", serpiente);
            request.setAttribute("imagenSerpiente", this.obtenerImagen(serpiente));
            request.setAttribute("accion", "Editar");
            request.setAttribute("sexos", sexo);
            redireccionar(request, response, redireccion);
        }

    }

    protected void getEditarevento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        if (verificarPermiso(318, listaPermisos)) {
            String redireccion = "Serpiente/EditarEvento.jsp";
            int id_evento = Integer.parseInt(request.getParameter("id_evento"));
            Evento evento = eventodao.obtenerEvento(id_evento);
            request.setAttribute("serpiente", evento.getSerpiente());
            request.setAttribute("evento", evento);
            request.setAttribute("accion", "Editarevento");
            request.setAttribute("listaTipoEventos", tipo_Eventos);
            redireccionar(request, response, redireccion);
        }

    }

    protected void getEliminarevento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermiso(319, listaPermisos);
        int id_evento = Integer.parseInt(request.getParameter("id_evento"));
        String redireccion = "Serpiente/index.jsp";
        boolean resultado = false;
        try {
            resultado = eventodao.eliminarEvento(id_evento);
            if (resultado) {
                //Funcion que genera la bitacora 
                bitacora.setBitacora(id_evento, Bitacora.ACCION_ELIMINAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_EVENTO, request.getRemoteAddr());
                //----------------------------
                request.setAttribute("mensaje", helper.mensajeDeExito("Evento de Serpiente eliminada correctamente"));
            } else {
                request.setAttribute("mensaje", helper.mensajeDeError("Evento de Serpiente no pudo ser eliminada ya que tiene otros elementos asociados."));
            }
            this.getIndex(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("mensaje", helper.mensajeDeError("Evento de Serpiente no pudo ser eliminada ya que tiene otros elementos asociados."));
            this.getIndex(request, response);
        }

    }

    // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Métodos Post">
    protected void postColeccionviva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id_serpiente = Integer.parseInt(request.getParameter("id_serpiente_pasocv"));
        boolean pasoCV = eventodao.validarPasoCV(id_serpiente);
        if (!pasoCV) {
            List<Integer> listaPermisos = getPermisosUsuario(request);
            validarPermiso(312, listaPermisos);
            Serpiente s = dao.obtenerSerpiente(id_serpiente);
            String fecha = request.getParameter("fecha_evento");
            Evento e = this.setEvento(s, 5, fecha, request);
            e.setObservaciones(request.getParameter("observacionesModal"));
            //----Agregar el Evento al Sistema
            boolean resultado = eventodao.insertarEvento(e);

            if (resultado) {
                dao.actualizarColeccionViva(id_serpiente);
                request.setAttribute("mensaje", helper.mensajeDeExito("Serpiente pasada a Colección Viva con éxito."));
                //Funcion que genera la bitacora
                bitacora.setBitacora(e.parseJSON(), Bitacora.ACCION_AGREGAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_EVENTO, request.getRemoteAddr());
                //*----------------------------*
            } else {
                request.setAttribute("mensaje", helper.mensajeDeError("Error en la Base de Datos. Serpiente no pudo pasarse a Coleccion Viva."));
            }
            this.getIndex(request, response);
        } else {
            request.setAttribute("mensaje", helper.mensajeDeError("Error en el Sistema. La serpiente ya fue registrada como Colección Viva."));
            this.getIndex(request, response);
        }

    }

    protected void postDescartar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id_serpiente = Integer.parseInt(request.getParameter("id_serpiente_descarte"));
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermiso(314, listaPermisos);
        Serpiente s = dao.obtenerSerpiente(id_serpiente);

        CatalogoTejido isCT = ctdao.obtenerCatalogoTejido(s);
        ColeccionHumeda isCH = chdao.obtenerColeccionHumeda(s);

        if ((isCT == null) && (isCH == null)) {
            String fecha = request.getParameter("fecha_evento");
            Evento e = this.setEvento(s, 14, fecha, request);
            e.setObservaciones(request.getParameter("observacionesModal"));
            //----Agregar el Evento al Sistema
            boolean resultado = eventodao.insertarEvento(e);
            if (resultado) {
                request.setAttribute("mensaje", helper.mensajeDeExito("Serpiente descartada con éxito."));
                //Funcion que genera la bitacora
                bitacora.setBitacora(e.parseJSON(), Bitacora.ACCION_AGREGAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_EVENTO, request.getRemoteAddr());
                //*----------------------------*
            } else {
                request.setAttribute("mensaje", helper.mensajeDeError("Error en la Base de Datos. Serpiente no pudo descartarse."));
            }
            this.getIndex(request, response);
        } else {
            request.setAttribute("mensaje", helper.mensajeDeError("Serpiente ya fue pasada a CH o CT."));
            this.getIndex(request, response);

        }

    }

    protected void postReversarcv(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermiso(316, listaPermisos);
        int id_serpiente = Integer.parseInt(request.getParameter("id_serpiente_reversar_cv"));
        boolean pasoCV = eventodao.validarPasoCV(id_serpiente);
        if (pasoCV) {
            boolean resultado = eventodao.reversarPasoCV(id_serpiente);
            if (resultado) {
                dao.reversarColeccionViva(id_serpiente);
                request.setAttribute("mensaje", helper.mensajeDeExito("Evento de Serpiente reversado correctamente."));
                Serpiente s = dao.obtenerSerpiente(id_serpiente);
                //Funcion que genera la bitacora
                bitacora.setBitacora(s.parseJSON(), Bitacora.ACCION_EDITAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_SERPIENTE, request.getRemoteAddr());
                //*----------------------------*
            } else {
                request.setAttribute("mensaje", helper.mensajeDeError("Error en la Base de Datos. Evento de Serpiente no pudo ser reversado."));
            }
            this.getIndex(request, response);
        } else {
            request.setAttribute("mensaje", helper.mensajeDeError("Error en el Sistema. La serpiente no ha sido pasada a Colección Viva."));
            this.getIndex(request, response);
        }

    }

    protected void postReversardeceso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Integer> listaPermisos = getPermisosUsuario(request);
        validarPermiso(317, listaPermisos);
        int id_serpiente = Integer.parseInt(request.getParameter("id_serpiente_reversar_deceso"));
        boolean deceso = eventodao.validarDeceso(id_serpiente);
        List<Evento> eventos = eventodao.obtenerEventos(id_serpiente);
        for (Evento i : eventos) {
            if (i.getId_categoria() == 7 || i.getId_categoria() == 8) {
                deceso = false;
                break;
            }
        }
        if (deceso) {
            boolean resultado = eventodao.reversarDeceso(id_serpiente);
            if (resultado) {
                dao.reversarEstado(id_serpiente);
                request.setAttribute("mensaje", helper.mensajeDeExito("Evento de Serpiente reversado correctamente."));
                Serpiente s = dao.obtenerSerpiente(id_serpiente);
                //Funcion que genera la bitacora
                bitacora.setBitacora(s.parseJSON(), Bitacora.ACCION_EDITAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_SERPIENTE, request.getRemoteAddr());
                //*----------------------------*
            } else {
                request.setAttribute("mensaje", helper.mensajeDeError("Error en la Base de Datos. Evento de Serpiente no pudo ser reversado."));
            }
            this.getIndex(request, response);
        } else {
            request.setAttribute("mensaje", helper.mensajeDeError("Error en Lógica del Sistema. No se puede reversar el deceso a la Serpiente."));
            this.getIndex(request, response);
        }

    }

    protected void postDeceso(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id_serpiente = Integer.parseInt(request.getParameter("id_serpiente_deceso"));
        boolean deceso = eventodao.validarDeceso(id_serpiente);
        if (!deceso) {
            Serpiente serpiente = dao.obtenerSerpiente(id_serpiente);
            Evento e = this.setDeceso(serpiente, 6, request);
            //----Agregar el Evento al Sistema
            boolean resultado = eventodao.insertarEvento(e);
            if (resultado) {
                dao.actualizarEstado(id_serpiente);
                request.setAttribute("mensaje", helper.mensajeDeExito("Serpiente registrada como Deceso con éxito."));
                //Funcion que genera la bitacora
                bitacora.setBitacora(e.parseJSON(), Bitacora.ACCION_AGREGAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_EVENTO, request.getRemoteAddr());
                //*----------------------------*
            } else {
                request.setAttribute("mensaje", helper.mensajeDeError("Error en la Base de Datos. Serpiente no pudo ser registrada como Deceso."));
            }
            this.getIndex(request, response);
        } else {
            request.setAttribute("mensaje", helper.mensajeDeError("Error en el Sistema. La serpiente ya fue registrada como Deceso."));
            this.getIndex(request, response);
        }

    }

    protected void postAgregareditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String redireccion = "Serpiente/index.jsp";
        boolean resultado = false;
        try {
            Serpiente s = new Serpiente();
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            s = construirObjeto(items);

            UsuarioDAO usuariodao = new UsuarioDAO();
            Usuario usuario = usuariodao.obtenerUsuario((String) request.getSession().getAttribute("usuario"));
            s.setRecibida(usuario);

            if (s.isAccion()) {
                resultado = dao.insertarSerpiente(s);
                if (resultado) {
                    //Funcion que genera la bitacora
                    bitacora.setBitacora(s.parseJSON(), Bitacora.ACCION_AGREGAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_SERPIENTE, request.getRemoteAddr());
                    //*----------------------------*
                    if (s.getImagen() != null) {
                        ByteArrayInputStream bais = new ByteArrayInputStream(s.getImagen());
                        dao.insertarImagen(bais, s.getId_serpiente(), s.getImagenTamano());
                    }
                    request.setAttribute("mensaje", helper.mensajeDeExito("Serpiente agregada correctamente"));
                    redireccion = "Serpiente/index.jsp";
                } else {
                    request.setAttribute("mensaje", helper.mensajeDeError("Serpiente no pudo ser agregada por problemas con el Número de Ingreso. Este debe ser único."));
                }
                request.setAttribute("listaSerpientes", dao.obtenerSerpientes());
                redireccionar(request, response, redireccion);
            } else {
                List<HelperSerpiente> cambios = dao.editarSerpiente(s);
                resultado = true;
                for (HelperSerpiente j : cambios) {
                    Evento e = new Evento();
                    java.sql.Date fecha_evento = new java.sql.Date(new Date().getTime());
                    e.setSerpiente(s);
                    e.setFecha_evento(fecha_evento);
                    Usuario usuario_evento = usuariodao.obtenerUsuario(request.getSession().getAttribute("usuario").toString());
                    e.setUsuario(usuario_evento);
                    if (j.getCampo_cambiado().equals("sexo")) {
                        e.setEvento("Sexo");
                        e.setId_categoria(9);
                        e.setValor_cambiado(j.getValor_cambiado());
                        eventodao.insertarCambio(e);
                    }
                    if (j.getCampo_cambiado().equals("talla_cabeza")) {
                        e.setEvento("Talla CabezaCloaca");
                        e.setId_categoria(10);
                        e.setValor_cambiado(j.getValor_cambiado());
                        eventodao.insertarCambio(e);
                    }
                    if (j.getCampo_cambiado().equals("talla_cola")) {
                        e.setEvento("Talla Cola");
                        e.setId_categoria(11);
                        e.setValor_cambiado(j.getValor_cambiado());
                        eventodao.insertarCambio(e);
                    }
                    if (j.getCampo_cambiado().equals("peso")) {
                        e.setEvento("Peso");
                        e.setId_categoria(12);
                        e.setValor_cambiado(j.getValor_cambiado());
                        eventodao.insertarCambio(e);
                    }
                }
                if (resultado) {
                    //Funcion que genera la bitacora        
                    bitacora.setBitacora(s.parseJSON(), Bitacora.ACCION_EDITAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_SERPIENTE, request.getRemoteAddr());
                    //*----------------------------*
                    if (s.getImagen() != null) {
                        ByteArrayInputStream bais = new ByteArrayInputStream(s.getImagen());
                        dao.insertarImagen(bais, s.getId_serpiente(), s.getImagenTamano());

                    }
                    request.setAttribute("mensaje", helper.mensajeDeExito("Serpiente editada correctamente"));
                    redireccion = "Serpiente/index.jsp";
                }
                request.setAttribute("listaSerpientes", dao.obtenerSerpientes());
                redireccionar(request, response, redireccion);
            }

        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
        }
    }

    protected void postEvento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean resultado = false;
        String redireccion = "Serpiente/index.jsp";
        int id_serpiente = Integer.parseInt(request.getParameter("id_serpiente"));

        Serpiente serpiente = dao.obtenerSerpiente(id_serpiente);
        String fecha = request.getParameter("fecha_evento");
        Evento evento = this.setEvento(serpiente, fecha, request);

        resultado = eventodao.insertarEvento(evento);

        if (resultado) {
            //Funcion que genera la bitacora
            bitacora.setBitacora(evento.parseJSON(), Bitacora.ACCION_AGREGAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_EVENTO, request.getRemoteAddr());
            //*----------------------------*
            request.setAttribute("mensaje", helper.mensajeDeExito("Evento agregado correctamente"));
        }
        this.getVer(request, response);
    }

    protected void postEditarevento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean resultado = false;
        Evento evento = this.construirEvento(request);
        resultado = eventodao.editarEvento(evento);
        if (resultado) {
            //Funcion que genera la bitacora
            bitacora.setBitacora(evento.parseJSON(), Bitacora.ACCION_EDITAR, request.getSession().getAttribute("usuario"), Bitacora.TABLA_EVENTO, request.getRemoteAddr());
            //*----------------------------*
            request.setAttribute("mensaje", helper.mensajeDeExito("Evento editado correctamente"));
        }
        this.getVer(request, response);
    }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Métodos Modelo">
    private Serpiente construirObjeto(List<FileItem> items) {
        Serpiente s = new Serpiente();
        for (FileItem item : items) {
            if (item.isFormField()) {
                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                String fieldName = item.getFieldName();
                String fieldValue;
                try {
                    fieldValue = item.getString("UTF-8").trim();
                } catch (UnsupportedEncodingException ex) {
                    fieldValue = item.getString();
                }
                switch (fieldName) {
                    case "id_serpiente":
                        int id_serpiente = Integer.parseInt(fieldValue);
                        s.setId_serpiente(id_serpiente);
                        break;
                    case "numero_serpiente":
                        int numero_ingreso = Integer.parseInt(fieldValue);
                        s.setNumero_serpiente(numero_ingreso);
                        break;
                    case "especie":
                        Especie especie = especiedao.obtenerEspecie(Integer.parseInt(fieldValue));
                        s.setEspecie(especie);
                        break;
                    case "accion":
                        if (fieldValue.equals("Agregar")) {
                            s.setAccion(true);
                        } else {
                            s.setAccion(false);
                        }
                        break;
                    case "fecha_ingreso":
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                        java.util.Date fecha_ingreso;
                        java.sql.Date fecha_ingresoSQL;
                        try {
                            fecha_ingreso = formatoFecha.parse(fieldValue);
                            fecha_ingresoSQL = new java.sql.Date(fecha_ingreso.getTime());
                            s.setFecha_ingreso(fecha_ingresoSQL);
                        } catch (ParseException ex) {

                        }
                        break;
                    case "localidad_origen":
                        s.setLocalidad_origen(fieldValue);
                        break;
                    case "colectada":
                        s.setColectada(fieldValue);
                        break;
                    case "sexo":
                        s.setSexo(fieldValue);
                        break;
                    case "talla_cabeza":
                        s.setTalla_cabeza(Float.parseFloat(fieldValue));
                        break;
                    case "talla_cola":
                        s.setTalla_cola(Float.parseFloat(fieldValue));
                        break;
                    case "peso":
                        s.setPeso(Float.parseFloat(fieldValue));
                        break;
                }
            } else {
                // Process form file field (input type="file").
                byte[] data = item.get();
                long size = item.getSize();
                if (size == 0) {
                    s.setImagen(null);
                    s.setImagenTamano(0);
                } else {
                    s.setImagen(data);
                    s.setImagenTamano(size);
                }
            }
        }
        return s;
    }

    //Para Coleccion Viva, Deceso
    private Evento setEvento(Serpiente serpiente, int id_categoria, HttpServletRequest request) {
        Evento e = new Evento();
        e.setId_categoria(id_categoria);
        java.sql.Date date = new java.sql.Date(new Date().getTime());
        e.setFecha_evento(date);
        e.setSerpiente(serpiente);
        UsuarioDAO usuariodao = new UsuarioDAO();
        e.setUsuario(usuariodao.obtenerUsuario(request.getSession().getAttribute("usuario").toString()));

        return e;
    }

    private Evento construirEvento(HttpServletRequest request) {
        Evento e = new Evento();
        e.setId_evento(Integer.parseInt(request.getParameter("id_evento")));
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha_evento;
        java.sql.Date fecha_eventoSQL;
        try {
            fecha_evento = formatoFecha.parse(request.getParameter("fecha_evento"));
            fecha_eventoSQL = new java.sql.Date(fecha_evento.getTime());
            e.setFecha_evento(fecha_eventoSQL);
        } catch (ParseException ex) {

        }
        e.setObservaciones(request.getParameter("observaciones"));
        return e;
    }

    //Para Coleccion Viva, Deceso

    private Evento setEvento(Serpiente serpiente, int id_categoria, String fecha, HttpServletRequest request) {
        Evento e = new Evento();
        e.setId_categoria(id_categoria);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha_evento;
        java.sql.Date fecha_eventoSQL;
        try {
            fecha_evento = formatoFecha.parse(fecha);
            fecha_eventoSQL = new java.sql.Date(fecha_evento.getTime());
            e.setFecha_evento(fecha_eventoSQL);
        } catch (ParseException ex) {

        }
        e.setSerpiente(serpiente);
        UsuarioDAO usuariodao = new UsuarioDAO();
        e.setUsuario(usuariodao.obtenerUsuario(request.getSession().getAttribute("usuario").toString()));

        return e;
    }

    //Para Coleccion Viva, Deceso
    private Evento setDeceso(Serpiente serpiente, int id_categoria, HttpServletRequest request) {
        Evento e = new Evento();
        e.setId_categoria(id_categoria);

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha_ingreso;
        java.sql.Date fecha_ingresoSQL;
        try {
            fecha_ingreso = formatoFecha.parse(request.getParameter("fecha_deceso"));
            fecha_ingresoSQL = new java.sql.Date(fecha_ingreso.getTime());
            e.setFecha_evento(fecha_ingresoSQL);
        } catch (ParseException ex) {

        }
        e.setObservaciones(request.getParameter("observacionesModal"));
        e.setSerpiente(serpiente);
        UsuarioDAO usuariodao = new UsuarioDAO();
        e.setUsuario(usuariodao.obtenerUsuario(request.getSession().getAttribute("usuario").toString()));

        return e;
    }

    private String obtenerImagen(Serpiente s) {
        if (s.getImagen() != null) {
            return "data:image/jpeg;base64," + Base64.encodeBytes(s.getImagen());
        } else {
            return "";
        }

    }

    //Para Evento
    private Evento setEvento(Serpiente serpiente, String fecha, HttpServletRequest request) {
        Evento e = new Evento();
        String evento = request.getParameter("eventoModal");
        switch (evento) {
            case "Defecación":
                e.setId_categoria(1);
                break;
            case "CambioPiel":
                e.setId_categoria(2);
                break;
            case "Desparasitación":
                e.setId_categoria(3);
                break;
            case "Alimentación":
                e.setId_categoria(4);
                break;
            default:
                e.setId_categoria(15);
                break;
        }
        e.setObservaciones(request.getParameter("observacionesModal"));
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha_evento;
        java.sql.Date fecha_eventoSQL;
        try {
            fecha_evento = formatoFecha.parse(fecha);
            fecha_eventoSQL = new java.sql.Date(fecha_evento.getTime());
            e.setFecha_evento(fecha_eventoSQL);
        } catch (ParseException ex) {

        }
        e.setSerpiente(serpiente);
        UsuarioDAO usuariodao = new UsuarioDAO();
        e.setUsuario(usuariodao.obtenerUsuario(request.getSession().getAttribute("usuario").toString()));

        return e;
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
