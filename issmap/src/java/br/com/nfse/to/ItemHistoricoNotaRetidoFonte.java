/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import java.util.ArrayList;

/**
 *
 * @author Thiago
 */
public class ItemHistoricoNotaRetidoFonte {

    String data;
    String descricao;
    String valor;
    String numeroNota;
    String aliquota;
    String cidade;    
    String valorIss;

    public ItemHistoricoNotaRetidoFonte(String data, String descricao, String valor, String numeroNota, String aliquota, String cidade, String valorIss) {
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
        this.numeroNota = numeroNota;
        this.aliquota = aliquota;
        this.cidade = cidade;
        this.valorIss = valorIss;
    }
}
