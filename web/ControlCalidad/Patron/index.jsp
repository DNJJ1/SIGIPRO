<%-- 
    Document   : index
    Created on : Jun 29, 2015, 4:39:43 PM
    Author     : ld.conejo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:plantilla_general title="Control de Calidad" direccion_contexto="/SIGIPRO">

  <jsp:attribute name="contenido">

    <jsp:include page="../../plantillas/barraFuncionalidad.jsp" />

    <!-- content-wrapper -->
    <div class="col-md-12 content-wrapper">
      <div class="row">
        <div class="col-md-12 ">
          <ul class="breadcrumb">
            <li>Control de Calidad</li>
            <li> 
              <a href="/SIGIPRO/ControlCalidad/Patron?">Patrones y Controles</a>
            </li>
          </ul>
        </div>
      </div>
      <!-- main -->
      <div class="content">
        <div class="main-content">
          <!-- COLUMN FILTER DATA TABLE -->
          <div class="widget widget-table">
            <div class="widget-header">
              <h3><i class="fa fa-gears"></i> Patrones y Controles </h3>
              <c:if test="${helper_permisos.validarPermiso(sessionScope.listaPermisos, 571)}">
                <div class="btn-group widget-header-toolbar">
                    <a class="btn btn-primary btn-sm boton-accion " href="/SIGIPRO/ControlCalidad/Patron?accion=agregar">Agregar Patr�n</a>
                </div>
              </c:if>
            </div>
            ${mensaje}
            <div class="widget-content">
              <table class="table table-sorting table-striped table-hover datatable tablaSigipro sigipro-tabla-filter">
                <!-- Columnas -->
                <thead> 
                  <tr>
                    <th>N�m. Lote/Identificador</th>
                    <th>Tipo</th>
                    <th>Fecha de Vencimiento</th>
                    <th>Lugar de Almacenamiento</th>                    
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${lista_patrones}" var="patron">

                    <tr id ="${patron.getId_patron()}">
                      <td>
                        <a href="/SIGIPRO/ControlCalidad/Patron?accion=ver&id_patron=${patron.getId_patron()}">
                          <div style="height:100%;width:100%">
                            ${patron.getNumero_lote()}
                          </div>
                        </a>
                      </td>
                      <td>${patron.getTipo()}</td>
                      <td>${patron.getFecha_vencimientoAsString()}</td>
                      <td>${patron.getLugar_almacenamiento()}</td>
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
    </div>

    </jsp:attribute>

  </t:plantilla_general>