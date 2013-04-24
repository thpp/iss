/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

/**
 *
 * @author Thiago
 */
public class TesteCampoLivre  implements org.jrimum.bopepo.campolivre.CampoLivre {

    String campoLivre;
    
    @Override
    public String write() {
        return campoLivre;
                //"1754500669347362100003334";
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(String g) {
        campoLivre = g;
    }
    
}
