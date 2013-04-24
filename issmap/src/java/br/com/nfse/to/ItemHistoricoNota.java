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
public class ItemHistoricoNota {

    String data;
    String descricao;
    String valor;
    String numeroNota;

    ItemHistoricoNota(String data, String descricao, String valor, String numeroNota) {
        super();
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
        this.numeroNota = numeroNota;
    }

//    public static void main(String args[]) {
//        ArrayList<String> al = new ArrayList<String>();
//
//        System.out.println("Initial size of al: " + al.size());
//
//        al.add("C");
//        al.add("A");
//        al.add("E");
//        al.add("B");
//        al.add("D");
//        al.add("F");
//        al.add(1, "A2");
//
//        System.out.println("Tamanho da Al após adições: " + al.size());
//
//        System.out.println("Conteúdo da al: " + al);
//
//        al.remove("F");
//        al.remove(2);
//
//        System.out.println("Tamanho da al, após as eliminações: " + al.size());
//        System.out.println("Conteúdo da al: " + al);
//    }
}
