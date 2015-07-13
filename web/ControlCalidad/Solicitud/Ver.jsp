<%-- 
    Document   : Ver
    Created on : Jul 11, 2015, 12:19:00 PM
    Author     : ld.conejo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                            <a href="/SIGIPRO/ControlCalidad/Solicitud?">Solicitudes de Control de Calidad</a>
                        </li>
                        <li class="active"> ${solicitud.getNumero_solicitud()} </li>
                    </ul>
                </div>
            </div>
            <!-- main -->
            <div class="content">
                <div class="main-content">
                    <!-- COLUMN FILTER DATA TABLE -->
                    <div class="widget widget-table">
                        <div class="widget-header">
                            <h3><i class="fa fa-gears"></i> ${solicitud.getNumero_solicitud()} </h3>
                            <div class="btn-group widget-header-toolbar">
                                <c:choose>
                                    <c:when test="${solicitud.getEstado().equals('Solicitado')}">
                                        <c:if test="${booleditar}">
                                            <a class="btn btn-warning btn-sm boton-accion" href="/SIGIPRO/ControlCalidad/Solicitud?accion=editar&id_solicitud=${solicitud.getId_solicitud()}">Editar</a>

                                        </c:if>
                                        <c:if test="${boolrecibir}">
                                            <a class="btn btn-primary btn-sm boton-accion recibir-Modal" data-id='${solicitud.getId_solicitud()}' data-toggle="modal" data-target="#modalRecibirSolicitud">Recibir</a>
                                            <a class="btn btn-danger btn-sm boton-accion anular-Modal" data-id='${solicitud.getId_solicitud()}' data-toggle="modal" data-target="#modalAnularSolicitud">Anular</a>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>

                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        ${mensaje}
                        <div class="widget-content">
                            <table>
                                <tr><td> <strong>Número de Solicitud: </strong></td> <td>${solicitud.getNumero_solicitud()} </td></tr>
                                <tr><td> <strong>Usuario Solicitante: </strong> <td>${solicitud.getUsuario_solicitante().getNombre_completo()} </td></tr>
                                <tr><td> <strong>Fecha de Solicitud: </strong> <td>${solicitud.getFecha_solicitudAsString()} </td></tr>
                                <c:if test="${solicitud.getUsuario_recibido()!=null}">
                                    <tr><td> <strong>Usuario Receptor: </strong> <td>${solicitud.getUsuario_recibido().getNombre_completo()} </td></tr>
                                    <tr><td> <strong>Fecha de Recepción: </strong> <td>${solicitud.getFecha_recibidoAsString()} </td></tr>
                                </c:if>
                                <tr><td> <strong>Estado: </strong> <td>${solicitud.getEstado()} </td></tr>
                                <c:if test="${!solicitud.getObservaciones().equals('')}">
                                    <tr><td> <strong>Observaciones: </strong> <td>${solicitud.getObservaciones()} </td></tr>
                                </c:if>
                            </table>
                            <br>
                        </div>
                        <div class="col-md-12">
                            <div class="widget widget-table">
                                <div class="widget-header">
                                    <h3><i class="fa fa-calendar"></i> Análisis Solicitados</h3>
                                </div>
                                <div class="widget-content">
                                    <table class="table table-sorting table-striped table-hover datatable tablaSigipro sigipro-tabla-filter">
                                        <!-- Columnas -->
                                        <thead> 
                                            <tr>
                                                <th>Nombre del Análisis</th>
                                                <th>Tipo de Muestras</th>
                                                <th>Muestras</th>
                                                <th>Acción</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${solicitud.getAnalisis_solicitud()}" var="ags">
                                                <tr id='${ags.getId_analisis_grupo_solicitud()}'>
                                                    <td>
                                                        ${ags.getAnalisis().getNombre()}
                                                    </td>
                                                    <td>
                                                        ${ags.getGrupo().getGrupos_muestras().get(0).getTipo_muestra().getNombre()}
                                                    </td>
                                                    <td>
                                                        <c:forEach items="${ags.getGrupo().getGrupos_muestras()}" var="muestra">
                                                            ${muestra.getIdentificador()} <br>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                        <c:if test="${solicitud.getEstado().equals('Recibido')}">
                                                            <a class="btn btn-primary btn-sm boton-accion" href="/SIGIPRO/ControlCalidad/Solicitud?accion=realizar&id_solicitud=${solicitud.getId_solicitud()}">Realizar</a>

                                                        </c:if>

                                                    </td>
                                                </tr>



                                            </c:forEach>


                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <!-- END WIDGET TICKET TABLE -->
                    </div>
                    <!-- /main-content -->
                </div>
                <!-- /main -->
            </div>
        </div>

    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script src="/SIGIPRO/recursos/js/sigipro/SolicitudCC.js"></script>
    </jsp:attribute>
</t:plantilla_general>

<t:modal idModal="modalAnularSolicitud" titulo="Anular Solicitud">
    <jsp:attribute name="form">
        <div class="widget-content">
            <form class="form-horizontal" id="anularSolicitud" autocomplete="off" method="post" action="Solicitud">
                <input hidden="true" name="accion" value="Anular">
                <input hidden="true" id='id_solicitud_anular' name='id_solicitud_anular' value="">
                <label for="observaciones" class="control-label">¿Razones por las cuales anula la solicitud?</label>
                <div class="form-group">
                    <div class="col-sm-12">
                        <div class="input-group">
                            <textarea rows="5" cols="50" maxlength="500" placeholder="Observaciones" class="form-control" name="observaciones" ></textarea>
                        </div>
                    </div>
                </div>


                <div class="form-group">
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-times-circle"></i>  Cancelar</button>
                        <button type="submit" class="btn btn-primary"><i class="fa fa-check-circle"></i> Anular Solicitud</button>            </div>
                </div>
            </form>
        </div>

    </jsp:attribute>

</t:modal>

<t:modal idModal="modalRecibirSolicitud" titulo="Recibir Solicitud">

    <jsp:attribute name="form">
        <form class="form-horizontal" id="form_modalautorizar" method="post" data-show-auth="${show_modal_auth}" action="Solicitud">
            ${mensaje_auth}
            <h4> Información sobre la solicitud </h4>

            <h5>Para validar la recepción, el usuario que recibe la solicitud debe iniciar sesión. </h5>

            <input hidden="true" name="id_solicitud_recibir" id="id_solicitud_recibir">
            <input hidden="true" name="accion" id="accion" value="Recibir">

            <label for="usr" class="control-label">Usuario</label>
            <div class="form-group">
                <div class="col-sm-12">
                    <div class="input-group" style="display:table;">
                        <input type="text" id="usr"  name="usuario_recibo" required
                               oninvalid="setCustomValidity('Este campo es requerido ')"
                               onchange="setCustomValidity('')">
                    </div>
                </div>
            </div>
            <label for="passw" class="control-label">Contraseña</label>
            <div class="form-group">
                <div class="col-sm-12">
                    <div class="input-group" style="display:table;">
                        <input type="password" id="passw" name="passw" required
                               oninvalid="setCustomValidity('Este campo es requerido ')"
                               onchange="setCustomValidity('')">
                    </div>
                    <p id='mensajeValidación' style='color:red;'><p>
                </div>
            </div>

            <div class="form-group">
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-dismiss="modal"><i class="fa fa-times-circle"></i> Cancelar</button>
                    <button type="submit" class="btn btn-primary"><i class="fa fa-check-circle"></i> Recibir Solicitud</button>
                </div>
            </div>
        </form>


    </jsp:attribute>

</t:modal>