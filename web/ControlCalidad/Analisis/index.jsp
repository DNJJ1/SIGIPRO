<%-- 
    Document   : index
    Created on : Jul 2, 2015, 8:43:50 PM
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
                            <a href="/SIGIPRO/ControlCalidad/Analisis?">An�lisis</a>
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
                            <h3><i class="fa fa-gears"></i> An�lisis </h3>
                            <c:set var="contienePermiso" value="false" />
                            <c:forEach var="permiso" items="${sessionScope.listaPermisos}">
                                <c:if test="${permiso == 1 || permiso == 540}">
                                    <c:set var="contienePermiso" value="true" />
                                </c:if>
                            </c:forEach>
                            <c:set var="contienePermisoRealizar" value="false" />
                            <c:forEach var="permiso" items="${sessionScope.listaPermisos}">
                                <c:if test="${permiso == 1 || permiso == 541}">
                                    <c:set var="contienePermisoRealizar" value="true" />
                                </c:if>
                            </c:forEach>
                            <c:if test="${contienePermiso}">
                                <div class="btn-group widget-header-toolbar">
                                    <a class="btn btn-primary btn-sm boton-accion " href="/SIGIPRO/ControlCalidad/Analisis?accion=agregar">Agregar An�lisis</a>
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
                                        <th>Estado</th>
                                        <th>An�lisis Pendientes</th>
                                        <th>Acci�n</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${listaAnalisis}" var="analisis">
                                        <tr id ="${analisis.getId_analisis()}">
                                            <td>
                                                <a href="/SIGIPRO/ControlCalidad/Analisis?accion=ver&id_analisis=${analisis.getId_analisis()}">
                                                    <div style="height:100%;width:100%">
                                                        ${analisis.getNombre()}
                                                    </div>
                                                </a>
                                            </td>
                                            <c:choose>
                                                <c:when test="${analisis.isAprobado()}">
                                                    <td>Aprobado</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>Pendiente</td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td>${analisis.getCantidad_pendiente()}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${analisis.isAprobado()}">
                                                        <c:if test="${contienePermisoRealizar}">
                                                            <a class="btn btn-primary btn-sm boton-accion " href="/SIGIPRO/ControlCalidad/Analisis?accion=agregar">Realizar</a>
                                                        </c:if>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a class="btn btn-primary btn-sm boton-accion aprobar-Modal" data-id='${analisis.getId_analisis()}' data-toggle="modal" data-target="#modalAprobarAnalisis">Aprobar</a>
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
    <jsp:attribute name="scripts">
        <script src="/SIGIPRO/recursos/js/sigipro/Analisis.js"></script>
    </jsp:attribute>
</t:plantilla_general>

<t:modal idModal="modalAprobarAnalisis" titulo="Aprobar An�lisis">

    <jsp:attribute name="form">
        <form class="form-horizontal" id="form_modalautorizar" method="post" data-show-auth="${show_modal_auth}" action="Analisis">
            ${mensaje_auth}
            <h4> Informaci�n sobre el an�lisis </h4>

            <h5>Para validar la aprobaci�n, el usuario que recibe la solicitud debe iniciar sesi�n. </h5>

            <input hidden="true" name="id_analisis_aprobar" id="id_analisis_aprobar">
            <input hidden="true" name="accion" id="accion" value="Aprobar">

            <label for="usr" class="control-label">Usuario</label>
            <div class="form-group">
                <div class="col-sm-12">
                    <div class="input-group" style="display:table;">
                        <input type="text" id="usr"  name="usuario_aprobacion" required
                               oninvalid="setCustomValidity('Este campo es requerido ')"
                               onchange="setCustomValidity('')">
                    </div>
                </div>
            </div>
            <label for="passw" class="control-label">Contrase�a</label>
            <div class="form-group">
                <div class="col-sm-12">
                    <div class="input-group" style="display:table;">
                        <input type="password" id="passw" name="passw" required
                               oninvalid="setCustomValidity('Este campo es requerido ')"
                               onchange="setCustomValidity('')">
                    </div>
                    <p id='mensajeValidaci�n' style='color:red;'><p>
                </div>
            </div>

            <div class="form-group">
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-times-circle"></i> Cancelar</button>
                    <button type="submit" class="btn btn-primary"><i class="fa fa-check-circle"></i> Aprobar An�lisis</button>
                </div>
            </div>
        </form>


    </jsp:attribute>

</t:modal>