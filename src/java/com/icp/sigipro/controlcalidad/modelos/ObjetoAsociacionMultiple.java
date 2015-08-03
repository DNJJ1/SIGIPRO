/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icp.sigipro.controlcalidad.modelos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Boga
 */
public class ObjetoAsociacionMultiple {
    
    private List<Integer> ids;
    private Resultado resultado;
    
    public ObjetoAsociacionMultiple(){
        
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }
    
    public void agregarId(int id) {
        if (ids == null) {
            ids = new ArrayList<Integer>();
        }
        ids.add(id);
    }
    
}
