package br.com.nfse.jsfuntil;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;

public class JSFUtil {

    public static void addInfoMessage(String msg) {
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(msg, facesMessage);
    }

//    public static String getMessage(String codigo) {
//        try {
//            ResourceBundle rb = ResourceBundle.getBundle(Constantes.NAME_BUNDLE);
//            return rb.getString(codigo);
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static HttpServletResponse getResponse() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse resp = (HttpServletResponse) externalContext.getResponse();        
        return resp;
    }

    public static void responseComplete() {
        FacesContext.getCurrentInstance().responseComplete();
    }
}
