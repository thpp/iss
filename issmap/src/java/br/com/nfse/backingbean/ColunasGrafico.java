/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

/**
 *
 * @author ThiagoHenrique
 */
public class ColunasGrafico {

    private int  notasTomador, notasPrestador;
    private String nomeColuna;
    
    public ColunasGrafico(){
    }

    public ColunasGrafico(String nomeColuna, int notasPrestador, int notasTomador) {
        this.nomeColuna = nomeColuna;
        this.notasTomador = notasTomador;
        this.notasPrestador = notasPrestador;
    }

    public String getNomeColuna() {
        return nomeColuna;
    }

    public void setNomeColuna(String nomeColuna) {
        this.nomeColuna = nomeColuna;
    }

    public int getNotasPrestador() {
        return notasPrestador;
    }

    public void setNotasPrestador(int notasPrestador) {
        this.notasPrestador = notasPrestador;
    }

    public int getNotasTomador() {
        return notasTomador;
    }

    public void setNotasTomador(int notasTomador) {
        this.notasTomador = notasTomador;
    }
}
