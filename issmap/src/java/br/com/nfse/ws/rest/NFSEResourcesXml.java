/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.ws.rest;

import br.com.nfse.dao.DAONFSE;
import br.com.nfse.to.NFSE;
import br.com.nfse.ws.beans.NFSEWS;
import br.com.nfse.ws.beans.NotasVinculadas;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author ThiagoHenrique
 */
@Path("/nfse")
public class NFSEResourcesXml {

    @Path("/{documento}/{codigoVerifica}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public NFSEWS getNFSE(@PathParam("documento") String cnpjCpf, @PathParam("codigoVerifica") String codigo) {

        NFSEWS nfseWs = new NFSEWS();

        DAONFSE daoNfse = new DAONFSE();
        try {

            NFSE nfse = null;

            nfse = daoNfse.buscaNota(cnpjCpf, codigo);

            if (nfse != null) {

                //dados tomador
                nfseWs.setCpfCnpjTomador(nfse.getCpfCnpjTomador());
                nfseWs.setNomeRazaoTomador(nfse.getNomeRazaoTomador());
                nfseWs.setEnderecoTomador(nfse.getEnderecoTomador());
                nfseWs.setCidadeTomador(nfse.getCidadeTomador());
                nfseWs.setImTomador(nfse.getImTomador());
                nfseWs.setIeRgTomador(nfse.getIeRgTomador());
                nfseWs.setEstadoTomador(nfse.getEstadoTomador());
                nfseWs.setEmailTomador(nfse.getEmailTomador());

                //dados Prestador
                nfseWs.setCpfCnpjPrestador(nfse.getCpfCnpjPrestador());
                nfseWs.setNomeRazaoPrestador(nfse.getNomeRazaoPrestador());
                nfseWs.setEnderecoPrestador(nfse.getEnderecoPrestador());
                nfseWs.setCidadePrestador(nfse.getCidadePrestador());
                nfseWs.setImPrestador(nfse.getImPrestador());
                nfseWs.setIeRgPrestador(nfse.getIeRgPrestador());
                nfseWs.setEstadoPrestador(nfse.getEstadoPrestador());
                nfseWs.setTipoTributacao(nfse.getTipoTributacao());
                nfseWs.setNaturezaOperacao(nfse.getNaturezaOperacao());

                if (nfse.getEmailPrestador() != null) {
                    nfseWs.setEmailPrestador(nfse.getEmailPrestador());
                }

                //dados de notas
                nfseWs.setIdBanco(nfse.getIdBanco());
                nfseWs.setNumeroNota(nfse.getNumeroNota().toString());

                if (nfse.getDataEmissao() != null) {
                    nfseWs.setDataEmissao(nfse.getDataEmissao().toString());
                }
                nfseWs.setCodigoVerifica(nfse.getCogigoVerifica());
                nfseWs.setDescServico(nfse.getDescServico());
                nfseWs.setServicoPrestado(nfse.getServicoPrestado());
                nfseWs.setValorNota(nfse.getValorNota());
                nfseWs.setValorDeducoes(nfse.getValorDeducoes());
                nfseWs.setValorBaseCalculo(nfse.getValorBaseCalculo());
                nfseWs.setAliquota(nfse.getAliquota());
                nfseWs.setValorIss(nfse.getValorIss());
                nfseWs.setValorCredito(nfse.getValorCredito());

                if (nfse.getDataVencimento() != null) {
                    nfseWs.setDataVencimento(nfse.getDataVencimento().toString());
                }
                nfseWs.setValorIssPrefeituara(nfse.getValorIssPrefeituara());
                nfseWs.setLocalExecucao(nfse.getLocalExecucao());
                nfseWs.setRetidoFonte(nfse.getRetidoFonte());
                nfseWs.setHora(nfse.getHora());

                if (nfse.isNotaTerceiro()) {
                    
                    nfseWs.setDataLancamentoTerceiro(nfse.getDataLancamentoNotaTerceiro().toString());
                    nfseWs.setHoraLancamentoTerceiro(nfse.getHoraLancamentoNotaTerceiro());
                    nfseWs.setEnderecoValidacaoTerceiro(nfse.getEnderecoValidacaoNotaTerceiros());
                    nfseWs.setFlgTerceiro("S");
                }else{
                    nfseWs.setFlgTerceiro("N");
                }


}

        } catch (Exception ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        }

        return nfseWs;
    }

    @Path("/{dataInicial}/{dataFinal}/{cpfcnpj}/p")
        @GET
        @Produces(MediaType.APPLICATION_XML)
        public NotasVinculadas getNFSEVinculadas(@PathParam("dataInicial") String dataInicial, @PathParam("dataFinal") String dataFinal, @PathParam("cpfcnpj") String cpfCnpj) {

        NotasVinculadas notasVinculadas = new NotasVinculadas();
        DAONFSE daoNota = new DAONFSE();
        List<NFSE> listaNotas = new ArrayList<NFSE>();

        Date dataIni = null;
        Date dataFim = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {

            dataIni = formatter.parse(dataInicial.replaceAll("-", "/"));
            dataFim = formatter.parse(dataFinal.replaceAll("-", "/"));

            listaNotas = daoNota.buscaNotasPorFiltroBoleto(cpfCnpj, "", "", dataIni, dataFim, "P");

        

} catch (ParseException ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        } 

catch (Exception ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        }

        for (NFSE nfse : listaNotas) {
            notasVinculadas.setAtributo(nfse.getIdBanco().toString());
        }
        return notasVinculadas;
    }

    @Path("/{cpfcnpj}/p")
        @GET
        @Produces(MediaType.APPLICATION_XML)
        public NotasVinculadas getTodasNFSEVinculadas(@PathParam("cpfcnpj") String cpfCnpj) {

        NotasVinculadas notasVinculadas = new NotasVinculadas();
        DAONFSE daoNota = new DAONFSE();
        List<NFSE> listaNotas = new ArrayList<NFSE>();

        try {
            listaNotas = daoNota.buscaNotasAbertasPrestador(cpfCnpj);

        

} catch (ParseException ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        } 

catch (Exception ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        }

        for (NFSE nfse : listaNotas) {
            notasVinculadas.setAtributo(nfse.getIdBanco().toString());
        }
        return notasVinculadas;
    }

    @Path("/{dataInicial}/{dataFinal}/{cpfcnpj}/rf")
        @GET
        @Produces(MediaType.APPLICATION_XML)
        public NotasVinculadas getNFSERFVinculadas(@PathParam("dataInicial") String dataInicial, @PathParam("dataFinal") String dataFinal, @PathParam("cpfcnpj") String cpfCnpj) {

        NotasVinculadas notasVinculadas = new NotasVinculadas();
        DAONFSE daoNota = new DAONFSE();
        List<NFSE> listaNotas = new ArrayList<NFSE>();

        Date dataIni = null;
        Date dataFim = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {

            dataIni = formatter.parse(dataInicial.replaceAll("-", "/"));
            dataFim = formatter.parse(dataFinal.replaceAll("-", "/"));

            listaNotas = daoNota.buscaNotasAbertasRetidoFonte(cpfCnpj, dataIni, dataFim);

        

} catch (ParseException ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        } 

catch (Exception ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        }

        for (NFSE nfse : listaNotas) {
            notasVinculadas.setAtributo(nfse.getIdBanco().toString());
        }
        return notasVinculadas;
    }

    @Path("/{cpfcnpj}/rf")
        @GET
        @Produces(MediaType.APPLICATION_XML)
        public NotasVinculadas getTodasNFSERFVinculadas(@PathParam("cpfcnpj") String cpfCnpj) {

        NotasVinculadas notasVinculadas = new NotasVinculadas();
        DAONFSE daoNota = new DAONFSE();
        List<NFSE> listaNotas = new ArrayList<NFSE>();

        try {
            listaNotas = daoNota.buscaNotasAbertasRetidoFonte(cpfCnpj);

        

} catch (ParseException ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        } 

catch (Exception ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        }

        for (NFSE nfse : listaNotas) {
            notasVinculadas.setAtributo(nfse.getIdBanco().toString());
        }
        return notasVinculadas;
    }

    @Path("/{codigo}")
        @GET
        @Produces(MediaType.APPLICATION_XML)
        public NotasVinculadas getTodasNFSERFVinculadasUnica(@PathParam("codigo") String codigo) {

        NotasVinculadas notasVinculadas = new NotasVinculadas();
        DAONFSE daoNota = new DAONFSE();
        NFSE nota = new NFSE();

        try {
            nota = daoNota.buscaNotaRetidoFonte(codigo);

        

} catch (ParseException ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        } 

catch (Exception ex) {
            Logger.getLogger(NFSEResourcesXml.class  

.getName()).log(Level.SEVERE, null, ex);
        }

        notasVinculadas.setAtributo(nota.getIdBanco().toString());

        return notasVinculadas;
    }
}
