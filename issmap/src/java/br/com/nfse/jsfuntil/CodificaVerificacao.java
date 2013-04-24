/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

import com.sun.jmx.snmp.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author ThiagoHenrique
 */
public class CodificaVerificacao {

    public static void main(String args[]) {

        DateFormat diaf = new SimpleDateFormat("DD");
        DateFormat mesf = new SimpleDateFormat("MM");
        DateFormat anof = new SimpleDateFormat("yyyy");
        
        DateFormat horaf = new SimpleDateFormat("HH");
        DateFormat minutof = new SimpleDateFormat("mm");
        DateFormat segundof = new SimpleDateFormat("ss");
        
        DateFormat codf = new SimpleDateFormat("DDMMyyyyHHmmss");
        
        Date data = Calendar.getInstance(Locale.getDefault()).getTime();
        
        System.out.println(codf.format(data));
        
        Timestamp dataDeHoje = new Timestamp();  
        
        Date now = new Date();          
        Long longTime = new Long(now.getTime()/1000);
        
        System.out.println(longTime);
        
        

        CodificaVerificacao codigoVerificacao = new CodificaVerificacao();
        String resp = codigoVerificacao.encode(longTime);
        System.out.println(resp);

    }

    public long decode(final String value) {
        return Long.parseLong(value, 36);
    }

    public String encode(final long value) {
        return Long.toString(value, 36);
    }
}
