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
    System.out.println(permisos);
    if (!(permisos.contains(1) || permisos.contains(24) || permisos.contains(25) || permisos.contains(26))) {
        request.getRequestDispatcher("/").forward(request, response);
    }
%>

<t:plantilla_general title="Bodegas" direccion_contexto="/SIGIPRO">

  <jsp:attribute name="contenido">

    <jsp:include page="../../plantillas/barraFuncionalidad.jsp" />

    <!-- content-wrapper -->
    <div class="col-md-12 content-wrapper">
      <div class="row">
        <div class="col-md-4 ">
          <ul class="breadcrumb">
            <li>Bodegas</li>
            <li> 
              <a href="/SIGIPRO/Bodegas/Ubicaciones?">Ubicaciones</a>
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
              <h3><i class="fa fa-barcode"></i> Ubicaciones </h3>

              <c:set var="contienePermiso" value="false" />
              <c:forEach var="permiso" items="${sessionScope.listaPermisos}">
                <c:if test="${permiso == 1 || permiso == 24}">
                  <c:set var="contienePermiso" value="true" />
                </c:if>
              </c:forEach>
              <c:if test="${contienePermiso}">
                <div class="btn-group widget-header-toolbar">
                    <a class="btn btn-primary btn-sm boton-accion " href="/SIGIPRO/Bodegas/Ubicaciones?accion=agregar">Agregar Ubicaci�n</a>
                </div>
              </c:if>
            </div>
            ${mensaje}
            <div class="widget-content">
              <table id="datatable-column-filter-ubicaciones" class="table table-sorting table-striped table-hover datatable tablaSigipro">
                <!-- Columnas -->
                <thead> 
                  <tr>
                    <th>Nombre</th>
                    <th>Descripci�n</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${listaUbicaciones}" var="ubicacion">

                    <tr id ="${ubicacion.getId_ubicacion()}">
                      <td>
                        <a href="/SIGIPRO/Bodegas/Ubicaciones?accion=ver&id_ubicacion=${ubicacion.getId_ubicacion()}">
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
