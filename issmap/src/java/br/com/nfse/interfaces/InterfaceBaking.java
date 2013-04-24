/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.nfse.interfaces;

import javax.faces.event.ActionEvent;


/**
 *
 * @author Administrador
 */
public interface InterfaceBaking {

    public void salvar(ActionEvent actionEvent);
    public void excluir(ActionEvent actionEvent);
    public void alterar(ActionEvent actionEvent);

}
