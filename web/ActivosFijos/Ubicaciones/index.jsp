<%-- 
    Document   : index
    Created on : Nov 26, 2014, 10:16:57 PM
    Author     : Walter
--%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%

    List<Integer> permisos = (List<Integer>) session.getAttribute("listaPermisos");
    if (!(permisos.contains(1) || permisos.contains(9999) || permisos.contains(34) || permisos.contains(35) || permisos.contains(36))) {
        request.getRequestDispatcher("/").forward(request, response);
    }
%>

<t:plantilla_general title="Activos Fijos" direccion_contexto="/SIGIPRO">

  <jsp:attribute name="contenido">

    <jsp:include page="../../plantillas/barraFuncionalidad.jsp" />

    <!-- content-wrapper -->
    <div class="col-md-12 content-wrapper">
      <div class="row">
        <div class="col-md-4 ">
          <ul class="breadcrumb">
            <li>Activos Fijos</li>
            <li> 
              <a href="/SIGIPRO/ActivosFijos/Ubicaciones?">Ubicaciones de Activos Fijos</a>
            </li>
          </ul>
        </div>
        <div class="col-md-8 ">
          <div class="top-content">

          </div>
        </div>
      </div>
      <!-- main -->
      <div class="content">
        <div class="main-content">
          <!-- COLUMN FILTER DATA TABLE -->
          <div class="widget widget-table">
            <div class="widget-header">
              <h3><i class="fa fa-barcode"></i> Ubicaciones de Activos Fijos</h3>

              <c:set var="contienePermiso" value="false" />
              <c:forEach var="permiso" items="${sessionScope.listaPermisos}">
                <c:if test="${permiso == 1 || permiso == 34}">
                  <c:set var="contienePermiso" value="true" />
                </c:if>
              </c:forEach>
              <c:if test="${contienePermiso}">
                <div class="btn-group widget-header-toolbar">
                    <a class="btn btn-primary btn-sm boton-accion " href="/SIGIPRO/ActivosFijos/Ubicaciones?accion=agregar">Agregar Ubicación</a>
                </div>
              </c:if>
            </div>
            ${mensaje}
            <div class="widget-content">
              <table class="table table-sorting table-striped table-hover datatable tablaSigipro sigipro-tabla-filter">
                <!-- Columnas -->
                <thead> 
                  <tr>
                    <th>Nombre</th>
                    <th>Descripción</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${listaUbicaciones}" var="ubicacion">

                    <tr id ="${ubicacion.getId_ubicacion()}">
                      <td>
                        <a href="/SIGIPRO/ActivosFijos/Ubicaciones?accion=ver&id_ubicacion=${ubicacion.getId_ubicacion()}">
                          <div style="height:100%;width:100%">
                            ${ubicacion.getNombre()}
                          </div>
                        </a>
                      </td>
                      <td>${ubicacion.getDescripcion()}</td>
                    </tr>

                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
          <!-- END COLUMN FILTER DATA TABLE -->
        </div>
        <!-- /main-content -->
      </div>
      <!-- /main -->

    </jsp:attribute>

  </t:plantilla_general>
