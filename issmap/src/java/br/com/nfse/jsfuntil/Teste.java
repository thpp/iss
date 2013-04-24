/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

/**
 *
 * @author Thiago
 */
public class Teste {

    public static void main(String[] args) {

        Integer valor = 10;
        float total = 0;

        for (int i = 1; i <= valor; i++) {

            if (i == 1) {
                total = i * (i + 1);
                i = 2;
            } else {
                total = total * i;
            }
            System.out.println(total);
        }

       // System.out.println(total);

    }
}
