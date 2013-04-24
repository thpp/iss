/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

/**
 *
 * @author ThiagoHenrique
 */
public abstract class CodigoVerificaNota {

    public static long decode(final String value) {
        return Long.parseLong(value, 36);
    }

    public static String encode(final long value) {
        return Long.toString(value, 36);
    }
}
