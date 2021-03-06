<%-- 
    Document   : Formulario
    Created on : 21-ene-2015, 20:28:28
    Author     : Walter
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<form class="form-horizontal" autocomplete="off" method="post" action="Activos">
    <div class="col-md-6">
        <input hidden="true" name="id_activo_fijo" value="${activofijo.getId_activo_fijo()}">
        <label for="placa" class="control-label">Placa del Activo Fijo</label>
        <div class="form-group">
            <div class="col-sm-12">
                <div class="input-group">
                    <input type="text" maxlength="45" placeholder="C234" class="form-control" name="placa" value="${activofijo.getPlaca()}"
                            > 
                </div>
            </div>
        </div>
        <label for="equipo" class="control-label">*Equipo</label>
        <div class="form-group">
            <div class="col-sm-12">
                <div class="input-group">
                    <input type="text" maxlength="45" placeholder="Equipo" class="form-control" name="equipo" value="${activofijo.getEquipo()}"
                           required
                           oninvalid="setCustomValidity('Este campo es requerido ')"
                           oninput="setCustomValidity('')" > 
                </div>
            </div>
        </div>
        <label for="marca" class="control-label"> Marca</label>
        <div class="form-group">
            <div class="col-sm-12">
                <div class="input-group">
                    <input type="text" maxlength="45" placeholder="Marca" class="form-control" name="marca" value="${activofijo.getMarca()}"
                </div>
            </div>
        </div>
    </div>
    <label for="fecha_movimiento" class="control-label">*Fecha de Movimiento</label>
    <div class="form-group">
        <div class="col-sm-12">
            <div class="input-group" style="display:table;">
                <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                <input type="text" value="${(activofijo.getFecha_movimientoAsDate() == null) ? helper.getFecha_hoy() : activofijo.getFecha_movimiento()}" pattern="\d{1,2}/\d{1,2}/\d{4}" id="datepicker" class="form-control sigiproDatePicker" name="fecha_movimiento" data-date-format="dd/mm/yyyy" required
                       oninvalid="setCustomValidity('Este campo es requerido ')"
                       onchange="setCustomValidity('')">
            </div>
        </div>
    </div>
    <label for="seccion" class="control-label">*Sección</label>
    <div class="form-group">
        <div class="col-sm-12">
            <div class="input-group">
                <%--<span class="input-group-addon"><i class="fa fa-at"></i></span>           SE ELIMINA EL ICONO --%>
                <select id="seleccionSeccion" class="select2" name="seccion"
                        style='background-color: #fff;' required
                        oninvalid="setCustomValidity('Este campo es requerido')"
                        onchange="setCustomValidity('')">
                    <option value=''></option>
                    <c:forEach items="${secciones}" var="seccion">
                        <c:choose>
                            <c:when test="${seccion.getID() == activofijo.getId_seccion()}" >
                                <option value=${seccion.getID()} selected> ${seccion.getNombre_seccion()}</option>
                            </c:when>
                            <c:otherwise>
                                <option value=${seccion.getID()}>${seccion.getNombre_seccion()}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
                <c:choose>
                        <c:when test="${accion.equals('Agregar')}">
                            <script>document.getElementById('seleccionSeccion').selectedIndex = -1;</script> 
                        </c:when>
                        <c:otherwise></c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<div class="col-md-6">
    <label for="ubicacion" class="control-label">*Ubicación</label>
    <div class="form-group">
        <div class="col-sm-12">
            <div class="input-group">
                <%--<span class="input-group-addon"><i class="fa fa-at"></i></span>           SE ELIMINA EL ICONO --%>
                <select id="seleccionUbicacion" class="select2" name="ubicacion"
                        style='background-color: #fff;' required
                        oninvalid="setCustomValidity('Por favor seleccione una ubicación')"
                        onchange="setCustomValidity('')">
                    <option value=''></option>
                    <c:forEach items="${ubicaciones}" var="ubicacion">
                        <c:choose>
                            <c:when test="${ubicacion.getId_ubicacion() == activofijo.getId_ubicacion()}" >
                                <option value=${ubicacion.getId_ubicacion()} selected> ${ubicacion.getNombre()}</option>
                            </c:when>
                            <c:otherwise>
                                <option value=${ubicacion.getId_ubicacion()}>${ubicacion.getNombre()}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>                 
    <label for="estado" class="control-label"> *Estado</label>
    <div class="form-group">
      <div class="col-sm-12">
        <div class="input-group">
          <select id="seleccionestado" class="select2" name="estado"
                  style='background-color: #fff;' required
                  oninvalid="setCustomValidity('Por favor seleccione un estado')"
                  onchange="setCustomValidity('')">
              <option value=''></option>
            <c:forEach items="${estados}" var="estado">
              <c:choose>
                <c:when test="${estado.equals(activofijo.getEstado())}" >
                  <option value="${estado}" selected> ${estado}</option>
                </c:when>
                <c:otherwise>
                  <option value="${estado}">${estado}</option>
                </c:otherwise>
              </c:choose>
            </c:forEach>
          </select>
        </div>
      </div>
    </div>
    <label for="responsable" class="control-label">Responsable</label>
    <div class="form-group">
      <div class="col-sm-12">
        <div class="input-group">
          <input type="text" minlength="5" maxlength="100" placeholder="Responsable" class="form-control" name="responsable" value="${activofijo.getResponsable()}"
                 oninvalid="setCustomValidity('Este campo debe ser de más de 5 caracteres')"
                 oninput="setCustomValidity('')" > 
        </div>
      </div>
    </div>
    <label for="serie" class="control-label"> Número de Serie</label>
    <div class="form-group">
      <div class="col-sm-12">
        <div class="input-group">
          <input type="text" maxlength="45" placeholder="Número de serie" class="form-control" name="serie" value="${activofijo.getSerie()}" > 
        </div>
      </div>
    </div>
</div>
       
        <div class="col-md-12">
<!-- Esta parte es la de los permisos de un rol -->
<p class="campos-requeridos">
    Los campos marcados con * son requeridos.
</p>  


<div class="form-group">
    <div class="modal-footer">
        <button type="button" class="btn btn-danger btn-volver"><i class="fa fa-times-circle"></i> Cancelar</button>
        <c:choose>
          <c:when test= "${accion.equals('Editar')}">
            <button type="submit" class="btn btn-primary"><i class="fa fa-check-circle"></i> Guardar Cambios</button>
          </c:when>
          <c:otherwise>
            <button type="submit" class="btn btn-primary"><i class="fa fa-check-circle"></i> ${accion} Activo Fijo</button>
          </c:otherwise>
        </c:choose>
    </div>
</div>

</div>
</form>