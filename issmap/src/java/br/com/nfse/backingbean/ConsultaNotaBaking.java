package br.com.nfse.backingbean;

import br.com.nfse.dao.DAONFSE;
import br.com.nfse.jsfuntil.NotaUtil;
import br.com.nfse.to.NFSE;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name = "MBNConsultaNota")
public class ConsultaNotaBaking {

    private String cpfCnpjtomador;
    private String codigoVerificacao;

    public String geraNota() throws Exception {

        NFSE nota = new NFSE();
        DAONFSE dao = new DAONFSE();
        NotaUtil geranota = new NotaUtil();

        nota.setCpfCnpjTomador(cpfCnpjtomador);
        nota.setCogigoVerifica(codigoVerificacao);
        nota = dao.buscaNota(cpfCnpjtomador, codigoVerificacao);
        if (nota == null) {
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nota nao encontrada! ", "Verifique se o CPF/CNPJ do Tomador ou o codigo de verificaçao estao corretos "));
            return "semnota";
            
//            FacesContext context = FacesContext.getCurrentInstance();
//            ViewHandler viewHandler = context.getApplication().getViewHandler();
//            UIViewRoot viewRoot = viewHandler.createView(context, login.xhtml);
//            context.setViewRoot(viewRoot);
        }else{
            geranota.imprimeNota(nota);
            return "nada";
        }
       

        //  inserirNome();

    }

//   public void inserirNome() {
//         // TODO: inserir nome em alguma lista ou base de dados
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        UIViewRoot uiViewRoot = facesContext.getViewRoot();
//        HtmlInputText inputText = (HtmlInputText) uiViewRoot.findComponent("idForm:idNome");
//        inputText.setSubmittedValue("");
//        setCodigoVerificacao("");
//        setCpfCnpjtomador("");
//    }
    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public String getCpfCnpjtomador() {
        return cpfCnpjtomador;
    }

    public void setCpfCnpjtomador(String cpfCnpjtomador) {
        this.cpfCnpjtomador = cpfCnpjtomador;
    }
}
