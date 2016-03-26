<%-- 
    Document   : index
    Created on : Jun 29, 2015, 4:39:43 PM
    Author     : ld.conejo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:plantilla_general title="Producci�n" direccion_contexto="/SIGIPRO">

    <jsp:attribute name="contenido">

        <jsp:include page="../../plantillas/barraFuncionalidad.jsp" />

        <!-- content-wrapper -->
        <div class="col-md-12 content-wrapper">
            <div class="row">
                <div class="col-md-12 ">
                    <ul class="breadcrumb">
                        <li>Producci�n</li>
                        <li> 
                            <a href="/SIGIPRO/Produccion/Lote?">Lotes de Producci�n</a>
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
                            <h3><i class="fa fa-gears"></i> Historial de Lotes de Producci�n </h3>
                            <c:if test="${helper_permisos.validarPermiso(sessionScope.listaPermisos, 660)}">
                                <div class="btn-group widget-header-toolbar">
                                    <a class="btn btn-primary btn-sm boton-accion " href="/SIGIPRO/Produccion/Lote">Inicio</a>
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
                                        <th>Protocolo</th>
                                        <th>Acci�n</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${listaLotes}" var="lote">

                                        <tr id ="${lote.getId_lote()}">
                                            <td>
                                                <a href="/SIGIPRO/Produccion/Lote?accion=ver&id_lote=${lote.getId_lote()}">
                                                    <div style="height:100%;width:100%">
                                                        ${lote.getNombre()}
                                                    </div>
                                                </a>
                                            </td>
                                            <td>
                                                ${lote.getProtocolo().getNombre()}
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${lote.getFecha_vencimiento()==null}">
                                                        <a class="btn btn-primary btn-sm boton-accion vencimiento-Modal" data-id='${lote.getId_lote()}' data-toggle="modal" data-target="#modalVencimientoLote">Fecha de Vencimiento</a>
                                                    </c:when>
                                                    <c:when test="${lote.getUsuario_distribucion().getId_usuario()==0}">
                                                        <a class="btn btn-primary btn-sm boton-accion distribucion-Modal" data-id='${lote.getId_lote()}' data-toggle="modal" data-target="#modalDistribucionLote">Distribuci�n</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a class="btn btn-warning btn-sm boton-accion" disabled>Distribuido</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
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
