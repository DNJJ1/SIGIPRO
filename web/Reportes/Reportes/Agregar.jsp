<%-- 
    Document   : Agregar
    Created on : Dec 14, 2014, 1:43:27 PM
    Author     : Boga
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:plantilla_general title="Reportes" direccion_contexto="/SIGIPRO">

    <jsp:attribute name="contenido">



        <!-- content-wrapper -->
        <div class="col-md-12 content-wrapper">
            <div class="row">
                <div class="col-md-8 ">
                    <ul class="breadcrumb">
                        <li>Reportes</li>
                        <li> 
                            <a href="/SIGIPRO/Reportes/Reportes?">Reportes</a>
                        </li>
                        <li class="active"> Agregar Reporte</li>

                    </ul>
                </div>
                <div class="col-md-4 ">
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
                            <h3><i class="fa fa-table"></i> Agregar Nuevo Reporte</h3>
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
        <script src="/SIGIPRO/recursos/js/codemirror/lib/codemirror.js"></script>
        <script src="/SIGIPRO/recursos/js/codemirror/mode/sql/sql.js"></script>
        <script src="/SIGIPRO/recursos/js/sigipro/Reportes/reportes.js"></script>
    </jsp:attribute>

    <jsp:attribute name="css">
        <link href="/SIGIPRO/recursos/css/codemirror/theme/neat.css" rel="stylesheet" type="text/css" />
        <link href="/SIGIPRO/recursos/css/codemirror/codemirror.css" rel="stylesheet" type="text/css" />
    </jsp:attribute>

</t:plantilla_general>
