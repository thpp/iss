/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Thiago
 */
public class ConfiguracaoSyst {

    // configurações gerais
    public static final String NOME_CIDADE = "Prefeitura Municipal de Santa Cruz do Rio Pardo";
    public static final String CPFCNPJ_PREFEITURA = "46.231.890/0001-43";
    
    // FICHAS ISS DESMEMBRADO    
    public static final String FICHA_FIXO = "4";
    public static final String FICHA_VARIAVEL = "360";
    public static final String FICHA_RETIDO_FONTE = "362";
    public static final String FICHA_CASTELO_BRANCO = "237";
    public static final String FICHA_ORLANDO_QUAGLIATO = "238";
    
    // nº Conta SCruz = 00139
    // configuração banco
    public static final String CODIGO_CEDENTE = "4130448";
    public static final Integer CARTEIRA = 00;    
    public static final Integer NUMERO_AGENCIA = 1257; 
    //ublic static final String digitoAgencia = codigoCedente.substring(3,4); 
    public static final Integer NUMERO_CONTA = Integer.parseInt("413044"); 
    public static final String DIGITO_CONTA = "8";
    public static final String SERVIDOR_WEBSERVICE = "http://localhost:8o8o/RestGemmap";
    
    /* Prefeitura de Santa Cruz do Rio Pardo */
    //public static final String servidorWebService = "http://200.205.9.172:10252/RestGemmap";
    
    
    // public static final String anoEx = retornaAno();        
    public static String anoEx() {
        DateFormat dataf = new SimpleDateFormat("yyyy");
        Date data = Calendar.getInstance(Locale.getDefault()).getTime();
        String anoStr = dataf.format(data);
        return anoStr;
    }
}
