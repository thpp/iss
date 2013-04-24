/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

import br.com.nfse.to.Bairro;
import br.com.nfse.to.GeneroAtividade;
import br.com.nfse.to.Logradouro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

/**
 *
 * @author Thiago Henrique
 */
public class PopulaTabelaGeneroAtividade {

    public static void main(String[] args) {

        //      ArrayList<Logradouro> logradouros = new ArrayList<Logradouro>();

        File f = new File("c:/genero_ativ.txt");
        if (!f.exists()) {
            System.exit(-1);
        }
        try {

            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;  
            GeneroAtividadeDao dao = new GeneroAtividadeDao();
            int contador = 0;
            String codigo = null;

            while ((line = in.readLine()) != null) {
                
                codigo = line.substring(0,5);
                
                if(contador == 0){
                    codigo = codigo.substring(1);
                }
                
                System.out.println(codigo);

                String nome = line.substring(5).trim();
                
                GeneroAtividade gnA = new GeneroAtividade();                
                
                gnA.setId(Integer.parseInt(codigo.trim()));
                gnA.setNome(nome.trim());
                
                System.out.println(line);
                
                dao.insereGeneroAtividade(gnA);
                contador = contador+1;
            }

        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo texto erro ==> " + e);
        }
    }
}
