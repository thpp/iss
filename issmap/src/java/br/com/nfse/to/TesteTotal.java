/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ThiagoHenrique
 */
@XmlRootElement
public class TesteTotal {

    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public static void main(String args[]) {

        Date hoje = new Date();  
      int dias_a_avancar = 2; // se quiser diminuir, basta por -2  
      Date nova_data = new Date(hoje.getTime()+((1000*24*60*60)*dias_a_avancar));  
      SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");  
      System.out.println(df.format(hoje));  
      System.out.println(df.format(nova_data)); 

    }
}
