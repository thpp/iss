
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

import br.com.nfse.to.Usuario;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thiago Henrique
 */
public class AutenticaSessao implements PhaseListener {

    private static final String homepage = "aviso_sem_sessao.xhtml";

    @Override
    public void afterPhase(PhaseEvent event) {

        FacesContext facesContext = event.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();

        boolean isCadastroAvisoPage = (currentPage.lastIndexOf("cadastro_previo_aviso.xhtml") > -1);
        boolean isCadastroSenhaUsuario = (currentPage.lastIndexOf("cadastro_senha_usuario.xhtml") > -1);
        boolean isCadastroJuridicoPage = (currentPage.lastIndexOf("previo_juridico.xhtml") > -1);
        boolean isCadastroContadorPage = (currentPage.lastIndexOf("previo_contadores.xhtml") > -1);
        boolean isCadastrofisicoPage = (currentPage.lastIndexOf("previo_fisica.xhtml") > -1);
        boolean isLoginPage = (currentPage.lastIndexOf("login.xhtml") > -1);
        boolean isAvisoBloqueioPage = (currentPage.lastIndexOf("aviso_bloqueado.xhtml") > -1);
        boolean isAvisoAguardandoDesbloqueioPage = (currentPage.lastIndexOf("aviso_aguardando_desbloqueio.xhtml") > -1);
        boolean isAvisoSemnotaPage = (currentPage.lastIndexOf("aviso_semnota.xhtml") > -1);
        boolean isConfPage = (currentPage.lastIndexOf("aviso_config.xhtml") > -1);
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        Object currentUser = session.getAttribute("usuario");

        if (!isLoginPage && currentUser == null && !isCadastroAvisoPage && !isCadastroJuridicoPage && !isCadastroJuridicoPage && !isCadastroContadorPage && !isCadastroSenhaUsuario && !isAvisoBloqueioPage && !isAvisoAguardandoDesbloqueioPage && !isCadastrofisicoPage && !isAvisoSemnotaPage && !isConfPage) {
            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
            nh.handleNavigation(facesContext, null, "loginPage");
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null) {
            if (usuario.getTipoUsuario() == 2) {
                boolean isInicialPage = (currentPage.lastIndexOf("inicial.xhtml") > -1);
                boolean isNfseConsulta = (currentPage.lastIndexOf("nfse_consulta.xhtml") > -1);
                boolean isNfseLanca = (currentPage.lastIndexOf("nfse_lanca.xhtml") > -1);

                if (!isInicialPage && !isNfseConsulta && !isNfseLanca) {
                    NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                    nh.handleNavigation(facesContext, null, "inicial");
                }

            }
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        ExternalContext ext = context.getExternalContext();
        HttpSession session = (HttpSession) ext.getSession(false);
        boolean newSession = (session == null) || (session.isNew());
        boolean postback = !ext.getRequestParameterMap().isEmpty();
        boolean timedout = postback && newSession;
        if (timedout) {
            Application app = context.getApplication();
            ViewHandler viewHandler = app.getViewHandler();
            UIViewRoot view = viewHandler.createView(context, "/" + homepage);
            context.setViewRoot(view);
            context.renderResponse();
            try {
                viewHandler.renderView(context, view);
                context.responseComplete();
            } catch (Throwable t) {
                throw new FacesException("Session timed out", t);
            }
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
