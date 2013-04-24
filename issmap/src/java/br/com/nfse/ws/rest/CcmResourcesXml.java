package br.com.nfse.ws.rest;

import br.com.nfse.dao.DAOCcm;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.Socio;
import br.com.nfse.ws.beans.CcmWsPF;
import br.com.nfse.ws.beans.Ccmws;
import br.com.nfse.ws.beans.Sociows;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import javax.ws.rs.PathParam;

@Path("/ccm")
public class CcmResourcesXml {

    @Path("/pj/{documento}/{codigoVerifica}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Ccmws getCcmPJ(@PathParam("documento") String cnpjCpf, @PathParam("codigoVerifica") String codigo) {

        Ccmws ccmws = null;

        DAOCcm dao = new DAOCcm();
        Ccm ccm = dao.buscaPorCnpjCpf(cnpjCpf);

        if (ccm != null) {

            ccmws = new Ccmws();

            if (ccm.getCodigoLiberacaoSenhaWeb().equals(codigo)) {

                ccmws.setCodigoValido(true);
                ccmws.setEmail(ccm.getEmail());
                ccmws.setCpfCnpj(ccm.getCnpjCpf());
                ccmws.setNomeRazao(ccm.getNomeRazao());
                ccmws.setRepresentanteLegal(ccm.getRepresentanteLegal());
                ccmws.setTelefone(ccm.getTelefone());
                ccmws.setContador(ccm.getContadorProfissional());
                ccmws.setCrc(ccm.getCrc());                 

                String rua = ccm.getRua();

                if (rua == null) {
                    
                    ccmws.setCep(ccm.getLogradouro().getCep());
                    ccmws.setRua(ccm.getLogradouro().getNome());
                    ccmws.setNumero(ccm.getNumeroPredio().toString());
                    ccmws.setBairro(ccm.getLogradouro().getBairro().getNome());
                    ccmws.setCidade(ccm.getLogradouro().getBairro().getCidade().getNome());
                    ccmws.setEstado(ccm.getLogradouro().getBairro().getCidade().getEstado().getSigla());
                                      
                } else {
                    
                    ccmws.setCep(ccm.getLogradouro().getCep());
                    ccmws.setRua(ccm.getRua());
                    ccmws.setNumero(ccm.getNumeroPredio().toString());
                    ccmws.setBairro(ccm.getBairro());
                    ccmws.setCidade(ccm.getLogradouro().getBairro().getCidade().getNome());
                    ccmws.setEstado(ccm.getLogradouro().getBairro().getCidade().getEstado().getSigla());
                    
                    
                }

               

                List<Sociows> listasocio = new ArrayList<Sociows>();
                for (int i = 0; i < ccm.getSocios().size(); i++) {
                    Sociows sociows = new Sociows();
                    Socio socio = new Socio();
                    socio = ccm.getSocios().get(i);
                    sociows.setCpf(socio.getCpf());
                    sociows.setNome(socio.getNome());
                    listasocio.add(sociows);
                }
                ccmws.setSocio(listasocio);
            }

        }

        return ccmws;
    }

    @Path("/pf/{documento}/{codigoVerifica}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public CcmWsPF getCcmPF(@PathParam("documento") String cnpjCpf, @PathParam("codigoVerifica") String codigo) {

        CcmWsPF ccmws = null;

        DAOCcm dao = new DAOCcm();
        Ccm ccm = dao.buscaPorCnpjCpf(cnpjCpf);

        if (ccm != null) {

            ccmws = new CcmWsPF();

            if (ccm.getCodigoLiberacaoSenhaWeb().equals(codigo)) {

                ccmws.setCodigoValido(true);
                ccmws.setEmail(ccm.getEmail());
                ccmws.setCpfCnpj(ccm.getCnpjCpf());
                ccmws.setNomeRazao(ccm.getNomeRazao());
                ccmws.setTelefone(ccm.getTelefone());
                ccmws.setInscricaoMunicipal(ccm.getIm());
                
                ccmws.setContador(ccm.getContadorProfissional());
                ccmws.setCrc(ccm.getCrc());
                
                String rua = ccm.getRua();

                if (rua == null) {
                    
                    ccmws.setCep(ccm.getLogradouro().getCep());
                    ccmws.setRua(ccm.getLogradouro().getNome());
                    ccmws.setNumero(ccm.getNumeroPredio().toString());
                    ccmws.setBairro(ccm.getLogradouro().getBairro().getNome());
                    ccmws.setCidade(ccm.getLogradouro().getBairro().getCidade().getNome());
                    ccmws.setEstado(ccm.getLogradouro().getBairro().getCidade().getEstado().getSigla());
                                      
                } else {
                    
                    ccmws.setCep(ccm.getLogradouro().getCep());
                    ccmws.setRua(ccm.getRua());
                    ccmws.setNumero(ccm.getNumeroPredio().toString());
                    ccmws.setBairro(ccm.getBairro());
                    ccmws.setCidade(ccm.getLogradouro().getBairro().getCidade().getNome());
                    ccmws.setEstado(ccm.getLogradouro().getBairro().getCidade().getEstado().getSigla());
                    
                    
                }

                if (ccm.isProfissionalLiberal()) {
                    ccmws.setTipoAutoPL("Profissional Liberal");
                } else if (ccm.isAutonomo()) {
                    ccmws.setTipoAutoPL("Autonomo");
                }
            }
        }
        return ccmws;

    }
}
