/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to.relatorio;

import java.util.List;

/**
 *
 * @author Thiago
 */
public class Teste {
    
    private String nome;
    private List<TesteObjeto> listaNumeros;

    public List<TesteObjeto> getListaNumeros() {
        return listaNumeros;
    }

    public void setListaNumeros(List<TesteObjeto> listaNumeros) {
        this.listaNumeros = listaNumeros;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
