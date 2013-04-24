/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

import br.com.nfse.to.Bairro;
import br.com.nfse.to.Logradouro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

/**
 *
 * @author Thiago Henrique
 */
public class PopulaTabelaLogradouro {

    public String populaTabela() {

        //      ArrayList<Logradouro> logradouros = new ArrayList<Logradouro>();
        
        String retorno = "";
        
        File f = new File("home/issmapco/public_ftp/rua.txt");
        if (!f.exists()) {
            System.exit(-1);
        }
        try {

            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            String comando = "";
            Integer contador = 0;
            LogradouroDao dao = new LogradouroDao();           
            

            while ((line = in.readLine()) != null) {

                contador++;

                comando = comando + line;

                if (contador % 2 == 0) {

                    String sbcomando = comando.substring(115);
                    sbcomando = sbcomando.replaceAll("\\(", "");
                    sbcomando = sbcomando.replaceAll("\\)", "");
                    sbcomando = sbcomando.replaceAll("\\;", "");
                    String s[] = sbcomando.split("\\,");
                    Logradouro logradouro = new Logradouro();
                    Bairro bairro = new Bairro();
                    logradouro.setCodigo(Integer.parseInt(s[0]));
                    bairro.setCodigo(Integer.parseInt(s[1]));
                    logradouro.setBairro(bairro);
                    logradouro.setCdTipoLogradouro(Integer.parseInt(s[2]));
                    logradouro.setNome(s[3].replaceAll("'", ""));
                    s[4] = s[4].replaceAll("'", "");
                    s[4] = s[4].replaceAll("\\)", "");
                    s[4] = s[4].replaceAll(";", "");
                    s[4] = s[4].trim();
                    
                    logradouro.setCep(s[4]);

                    try {                       
                        dao.insereLogradouro(logradouro);
                    } catch (Exception e) {
                        retorno = "Erro ao salvar!!" + e;
                    }

                    comando = "";
                }
            }
            
        } catch (Exception e) {
            retorno = "Erro ao ler o arquivo texto erro ==> " + e;
        }
        return retorno;
    }
}
