<%-- 
    Document   : Agregar
    Created on : Jun 30, 2015, 8:39:20 PM
    Author     : ld.conejo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:plantilla_general title="Control de Calidad" direccion_contexto="/SIGIPRO">

    <jsp:attribute name="contenido">



        <!-- content-wrapper -->
        <div class="col-md-12 content-wrapper">
            <div class="row">
                <div class="col-md-12 ">
                    <ul class="breadcrumb">
                        <li>Control de Calidad</li>
                        <li> 
                            <a href="/SIGIPRO/ControlCalidad/Equipo?">Equipos</a>
                        </li>
                        <li class="active"> Agregar Nuevo Equipo </li>

                    </ul>
                </div>
            </div>

            <!-- main -->
            <div class="content">
                <div class="main-content">
                    <!-- COLUMN FILTER DATA TABLE -->
                    <div class="widget widget-table">
                        <div class="widget-header">
                            <h3><i class="fa fa-gears"></i> Agregar Nuevo Equipo </h3>
                        </div>
                        ${mensaje}
                        <div class="widget-content">

                            <jsp:include page="Formulario.jsp"></jsp:include>

                            </div>
                        </div>
                        <!-- END WIDGET TICKET TABLE -->
                    </div>
                    <!-- /main-content -->
                </div>
                <!-- /main -->
            </div>

    </jsp:attribute>

    <jsp:attribute name="scripts">
        <script src="/SIGIPRO/recursos/js/sigipro/Equipo.js"></script>
    </jsp:attribute>

</t:plantilla_general>

