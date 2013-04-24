/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOLogradouro;
import br.com.nfse.dao.DAOServico;
import br.com.nfse.jsfuntil.LogradouroDao;
import br.com.nfse.to.Bairro;
import br.com.nfse.to.Logradouro;
import br.com.nfse.to.Servico;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Thiago
 */
@ManagedBean(name = "MBConfig")
public class ConfiguraBaking {

    public ConfiguraBaking() {
    }

    public void populaServico() {
        
        System.out.println("vaiiiiiiiiiiiiiiiiiiiiiiiiiii");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Populado com sucesso!!", ""));
//        File f = new File("c:/servico.txt");
//        if (!f.exists()) {
//            System.exit(-1);
//        }
//        try {
//
//            BufferedReader in = new BufferedReader(new FileReader(f));
//            String line;
//            Integer contador = 0;
//            DAOServico daoServico = new DAOServico();
//
//            while ((line = in.readLine()) != null) {
//
//                String s[] = line.split(";");
//                Servico servico = new Servico();
//
//                if (contador == 0) {
//                    s[0] = s[0].substring(1);
//                }
//
//                System.out.println(s[6].trim());
//
//                servico.setCodigoItem(s[0].trim());
//                servico.setItemTabela(s[1].trim());
//                servico.setValorJ(Double.parseDouble(s[2].replaceAll(",", ".").trim()));
//                servico.setValorF(Double.parseDouble(s[3].replaceAll(",", ".").trim()));
//                servico.setAliquotaJ(Double.parseDouble(s[4].trim()));
//                servico.setAliquotaF(Double.parseDouble(s[5].trim()));
//                if (s[6].trim().equals("N")) {
//                    servico.setVetado(false);
//                } else {
//                    servico.setVetado(true);
//                }
//                servico.setDescricao(s[7]);
//                daoServico.salvar(servico);
//                contador++;
//            }
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Populado com sucesso!!", ""));
//        } catch (Exception e) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO!", "Erro ao ler o arquivo texto erro ==> " + e));
//        }

    }

    public void populaTabela() {
        
        //"/home/issmapco/public_html/teste.txt"
        //"c:/teste.txt"
        File f = new File("/home/issmapco/public_html/teste.txt");
        if (!f.exists()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO!", "Arquivo não encontrado!!"));
            return;
            //System.exit(-1);
        }
        try {

            BufferedReader in = new BufferedReader(new FileReader(f));
            String line;
            String comando = "";
            Integer contador = 0;
            DAOLogradouro dao = new DAOLogradouro();


            while ((line = in.readLine()) != null) {

                contador++;

                comando = comando + line;

                if (contador % 2 == 0) {
                    
                    
                    
                    String sbcomando = line.substring(66);
                    
                    System.out.println("ANTES ====>"+sbcomando);
                    
                    sbcomando = sbcomando.replaceAll("\\(", "");
                    sbcomando = sbcomando.replaceAll("\\)", "");
                    sbcomando = sbcomando.replaceAll("\\;", "");
                    String s[] = sbcomando.split("\\,");
                    
                    System.out.println("DEPOIS ===> "+sbcomando);
                    
                    
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
                    
                    System.out.println("Codigo "+logradouro.getCodigo());
                    System.out.println("Codigo Bairro "+logradouro.getBairro().getCodigo());
                    System.out.println("tipo logradouro "+logradouro.getCdTipoLogradouro());
                    System.out.println("nome "+logradouro.getNome());
                    System.out.println("CEP "+logradouro.getCep());
                    
                    
                    
                    

//                    String sbcomando = comando.substring(115);
//                    sbcomando = sbcomando.replaceAll("\\(", "");
//                    sbcomando = sbcomando.replaceAll("\\)", "");
//                    sbcomando = sbcomando.replaceAll("\\;", "");
//                    String s[] = sbcomando.split("\\,");
//                    Logradouro logradouro = new Logradouro();
//                    Bairro bairro = new Bairro();
//                    logradouro.setCodigo(Integer.parseInt(s[0]));
//                    bairro.setCodigo(Integer.parseInt(s[1]));
//                    logradouro.setBairro(bairro);
//                    logradouro.setCdTipoLogradouro(Integer.parseInt(s[2]));
//                    logradouro.setNome(s[3].replaceAll("'", ""));
//                    s[4] = s[4].replaceAll("'", "");
//                    s[4] = s[4].replaceAll("\\)", "");
//                    s[4] = s[4].replaceAll(";", "");
//                    s[4] = s[4].trim();
//
//                    logradouro.setCep(s[4]);

                    try {
                        dao.salvar(logradouro);
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO!", "Erro ao salvar!!" + e));
                    }

                    comando = "";
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "LOGRADOURO populado com sucesso!!!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERRO!", "Erro ao ler o arquivo texto erro ==> " + e));
        }
    }
}
