package br.com.nfse.jsfuntil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.Currency;  

/** 
 * @author Thiago Henrique
 */
public class Moeda {

    //private static Currency currency = Currency.getInstance("BRL"); 
    Locale ptBR = new Locale("pt", "BR");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(ptBR);
    private DecimalFormat formato = new DecimalFormat("#,##0.00", dfs);
    private static DecimalFormat formatoPorcentagem = new DecimalFormat("0.00");

    public static Double convert(String valorFormatado) {
        Double valor = 0.0;
        return valor = Double.parseDouble(valorFormatado.replaceAll("\\.", "").replaceAll(",", "."));
    }

    public static Double convertPorcentagem(String valorFormatado) {
        Double valor = 0.0;
        valorFormatado = valorFormatado.replaceAll("\\.", "");
        valorFormatado = "0.0" + valorFormatado;
        return valor = Double.parseDouble(valorFormatado);
    }

    public static Double convertPorcentagemMelhorado(String valorS) throws Exception {

        valorS = valorS.replace(",", ".");

        return (Double.parseDouble(valorS)/100);
    }

    public String formata(Double valor) {
        return formato.format(valor);
    }

    public static void main(String[] args) {
        try {
            System.out.println(Moeda.convertPorcentagemMelhorado("3,5"));
        } catch (Exception ex) {
            Logger.getLogger(Moeda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}