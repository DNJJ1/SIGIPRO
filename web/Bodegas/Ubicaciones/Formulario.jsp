<%-- 
    Document   : Formulario
    Created on : 21-ene-2015, 20:28:28
    Author     : Walter
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<form class="form-horizontal" autocomplete="off" method="post" action="Ubicaciones">
  <div class="col-md-6">
    <input hidden="true" name="id_ubicacion" value="${ubicacion.getId_ubicacion()}">
    <label for="nombre" class="control-label">* Nombre de la Ubicación</label>
    <div class="form-group">
        <div class="col-sm-12">
            <div class="input-group">
              <input type="text" maxlength="45" placeholder="Nombre de Ubicación" class="form-control" name="nombre" value="${ubicacion.getNombre()}"
                       required
                       oninvalid="setCustomValidity('Este campo es requerido ')"
                       oninput="setCustomValidity('')" > 
            </div>
        </div>
    <label for="descripcion" class="control-label">Descripción</label>
    <div class="form-group">
        <div class="col-sm-12">
            <div class="input-group">
              <textarea rows="5" cols="50" maxlength="500" placeholder="Descripción" class="form-control" name="descripcion" >${ubicacion.getDescripcion()}</textarea>
            </div>
        </div>
    </div>
  </div>
  <!-- Esta parte es la de los permisos de un rol -->
  <p>
    Los campos marcados con * son requeridos.
  </p>  

  <div class="form-group">
    <div class="modal-footer">
      <button type="button" class="btn btn-danger" onclick="history.back()"><i class="fa fa-times-circle"></i> Cancelar</button>
      <button type="submit" class="btn btn-primary"><i class="fa fa-check-circle"></i> ${accion} Ubicación</button>
    </div>
  </div>
                       
  
</form>