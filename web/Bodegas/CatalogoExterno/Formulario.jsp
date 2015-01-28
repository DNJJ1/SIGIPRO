<%-- 
    Document   : Formulario
    Created on : Jan 27, 2015, 2:08:39 PM
    Author     : Amed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<form id="formCatalogoExterno" class="form-horizontal" autocomplete="off" method="post" action="CatalogoExterno">

    <input hidden="true" name="id_producto" value="${producto.getId_producto_ext()}">
    <input id="productosinternos" hidden="true" name="listaProductosInternos" value="">
    <label for="producto" class="control-label">* Nombre del Producto</label>
    <div class="form-group">
      <div class="col-sm-12">
        <div class="input-group">
          <input type="text" maxlength="45" placeholder="Nombre de Producto" class="form-control" name="producto" value="${producto.getProducto()}"
                 required
                 oninvalid="setCustomValidity('Este campo es requerido ')"
                 oninput="setCustomValidity('')" > 
        </div>
      </div>
    </div>
    <label for="codigoExterno" class="control-label"> Código Externo</label>
    <div class="form-group">
      <div class="col-sm-12">
        <div class="input-group">
          <input type="text" maxlength="45" placeholder="Ejemplo: 73b" class="form-control" name="codigoExterno" value="${producto.getCodigo_Externo()}"
                 > 
        </div>
      </div>
    </div>
    <label for="marca" class="control-label">Marca</label>
    <div class="form-group">
      <div class="col-sm-12">
        <div class="input-group">
          <input type="text" maxlength="45" placeholder="" class="form-control" name="marca" value="${producto.getMarca()}"
                 > 
        </div>
      </div>
    </div>  
    <label for="proveedor" class="control-label">Proveedor</label>
    <div class="form-group">
      <div class="col-sm-12">
        <div class="input-group">
          <select id="proveedor" class="form-control" name="proveedor" style='background-color: #fff;' >  
            <c:forEach items="${proveedores}" var="pr">
              <option value=${pr.getId_proveedor()}>${pr.getNombre_proveedor()}</option>
            </c:forEach>
            <option value=${producto.getId_Proveedor()} selected>${producto.getNombreProveedor()}</option> 
          </select>
        </div>
      </div>
    </div>

    <!-- Esta parte es la de los interno del catalogo externo -->
    <div class="widget widget-table">
      <div class="widget-header">
        <h3><i class="fa fa-check"></i> Productos del Catálogo Interno Asociados</h3>
        <div class="btn-group widget-header-toolbar">
          <a class="btn btn-primary btn-sm boton-accion" data-toggle="modal" data-target="#modalAgregarCatalogoInterno">Agregar</a>
        </div>
      </div>
      <div class="widget-content">
        <table id="datatable-column-filter-permisos" class="table table-sorting table-striped table-hover datatable">
          <thead>
            <tr>
              <th>Nombre y Codigo del Producto</th>
              <th>Eliminar</th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${productos_internos}" var="interno">
            <tr id="${interno.getId_producto()}">
              <td>${interno.getNombre()}</td>
              <td>
                <button type="button" class="btn btn-primary btn-sm boton-accion" onclick="eliminarProductoInterno(${interno.getId_producto()})">Eliminar</button>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
    <p>
      Los campos marcados con * son requeridos.
    </p>  

    <div class="form-group">
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" onclick="history.back()"><i class="fa fa-times-circle"></i> Cancelar</button>
        <button type="submit" class="btn btn-primary"><i class="fa fa-check-circle"></i> ${accion} Producto</button>
      </div>
    </div>
 


</form>