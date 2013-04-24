/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

import br.com.nfse.dao.DAOServico;
import br.com.nfse.to.Servico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author ThiagoHenrique
 */
public class PopulaServico {

    public static void main(String args[]) {

        File f = new File("c:/servico.txt");
        if (!f.exists()) {
            System.exit(-1);
        }
        try {

            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            Integer contador = 0;           
            DAOServico daoServico = new DAOServico();
            
            while ((line = in.readLine()) != null) {

                String s[] = line.split(";");
                Servico servico = new Servico();
                
                if(contador == 0){
                    s[0] = s[0].substring(1);
                }
                
                System.out.println(s[6].trim());
                
                servico.setCodigoItem(s[0].trim());
                servico.setItemTabela(s[1].trim());
                servico.setValorJ(Double.parseDouble(s[2].replaceAll(",", ".").trim()));
                servico.setValorF(Double.parseDouble(s[3].replaceAll(",", ".").trim()));
                servico.setAliquotaJ(Double.parseDouble(s[4].trim()));
                servico.setAliquotaF(Double.parseDouble(s[5].trim()));
                if(s[6].trim().equals("N")){
                    servico.setVetado(false);
                }else{
                  servico.setVetado(true);
                }
                servico.setDescricao(s[7]);
                daoServico.salvar(servico);
                contador++;
            }

        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo texto erro ==> " + e);
        }
    }
}
