$(function(){ /* DOM ready */ //
    $("#id_cliente").change(function () {
        //Agregar solo las opciones que contienen el data-cliente que corresponde a id_cliente[selectedindex].value
        var select_cliente = document.getElementById("id_cliente");
        var id_cliente = select_cliente[select_cliente.selectedIndex].value;
        
        var telefono = document.getElementById("telefono");
        var telefono_label = document.getElementById("telefono_label");
        var correo = document.getElementById("correo_electronico");
        var correo_label = document.getElementById("correo_electronico_label");
        var nombre = document.getElementById("nombre_cliente");
        var nombre_label = document.getElementById("nombre_cliente_label");
        
        if (id_cliente === "0"){ //muestre teléfono y correo
            telefono.required = true;
            correo.required = true;
            nombre.required = true;
            telefono.disabled = false;
            correo.disabled= false;
            telefono_label.disabled = false;
            correo_label.disabled = false;
            nombre.disabled = false;
            nombre_label.disabled = false;
        }
        else{ //hide teléfono y correo
            telefono.required = false;
            correo.required = false;
            nombre.required = false;
            telefono.disabled = true;
            correo.disabled= true;
            telefono_label.disabled = true;
            correo_label.disabled = true;
            nombre.disabled = true;
            nombre_label.disabled = true;
        }
    }).change();
    
});

function comprobarFechas(){
    
    var fecha_i = document.getElementById("fecha_solicitud").value.split("/");
    var fecha_r = document.getElementById("fecha_atencion").value.split("/");
    
    if ((parseInt(fecha_i[0]) + parseInt(fecha_i[1]) * 100 + parseInt(fecha_i[2]) * 10000) > (parseInt(fecha_r[0]) + parseInt(fecha_r[1]) * 100 + parseInt(fecha_r[2]) * 10000))
    {
        document.getElementById("fecha_atencion").setCustomValidity("La fecha de atención o despacho debe ser mayor que la fecha de solicitud. ");
    }
    
    else{
       
       if (!$('#id_cliente').valid || !$('#fecha_solicitud').valid) {
            $('<input type="submit">').hide().appendTo($('#formLista')).click();
}
       else{
            $("#formLista").submit();}
    }
}

$(document).ready(function() {
    
    //**La tabla no tiene la clase datatable en el index.jsp**
    
    //Se ordena la tabla en relación a la fecha. Por defecto, datatable no ordena por formato de fecha dd/mm/yyyy
    //Por ello, se crea el atributo 'data-order' en la primera columna de cada fila para luego asignarle la clase datatable a la tabla
    
    var table = document.getElementById("tabla_lista_espera");
    for (var i = 1; i < table.rows.length ; i++) {
        var row = 0;
        row = table.rows[i];
        var fecha = row.cells[0].firstChild.childNodes;
        
        fecha = fecha[1].innerHTML; //obtener la fecha de la solicitud en la lista de espera
        
        var fecha_array = fecha.split("/"); 
        var dia = fecha_array[0];
        var mes = fecha_array[1];
        var ano = fecha_array[2];
        
        var fecha_formateada = ano + '-' + mes + '-' + dia;
        
        row.cells[0].setAttribute("data-order", fecha_formateada);
     }
    
    document.getElementById("tabla_lista_espera").className = "table table-sorting table-striped table-hover datatable tablaSigipro sigipro-tabla-filter";
    //$('#tabla_lista_espera').DataTable();
} );