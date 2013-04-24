/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.backingbean;

import br.com.nfse.dao.DAOCcm;
import br.com.nfse.to.Ccm;
import br.com.nfse.to.Usuario;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Thiago Henrique
 */
@ManagedBean(name = "MBUpload")
@SessionScoped
public class TesteUpload implements java.io.Serializable {

    private static Logger logger = Logger.getLogger(PerfilBaking.class.getName());
    private String arquivo = null;

    public TesteUpload() {
        System.out.println("Gerando Teste");
    }
    private String nomeArquivoSelecionado;
    private StreamedContent imagem;
    byte[] foto;

    public void fileUploadAction(FileUploadEvent event) {

        try {
            // Retorna informacoes sobre a sessao
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = request.getSession();
            Usuario currentUser = (Usuario) session.getAttribute("usuario");
            Ccm ccm = currentUser.getCcm();
            String nomeArquivo = ccm.getIm();
            
            // Seta o nome do arquivo de acordo como IM  
            setNomeArquivoSelecionado(nomeArquivo);

            // Seta o caminho da logo
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
            arquivo = scontext.getRealPath("/WEB-INF/upload/" + nomeArquivoSelecionado + ".png");
            
            // Seta o campo do CCM
            ccm.setLogo(nomeArquivo);

            // Atualiza as informacoes da tabela CCm no banco
            DAOCcm daoCcm = new DAOCcm();
            try {
                daoCcm.salvarMerge(ccm);
            } catch (SQLException ex) {
                Logger.getLogger(TesteUpload.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            BufferedImage img;
            try {
                img = ImageIO.read(event.getFile().getInputstream());
                File outputfile = new File(arquivo);

                BufferedImage aux = new BufferedImage(128, 128, img.getType());//cria um buffer auxiliar com o tamanho desejado
                Graphics2D g = aux.createGraphics();//pega a classe graphics do aux para edicao
                AffineTransform at = AffineTransform.getScaleInstance((double) 128 / img.getWidth(), (double) 128 / img.getHeight());//cria a transformacao
                g.drawRenderedImage(img, at);//pinta e transforma a imagem real no auxiliar

                ImageIO.write(aux, "png", outputfile);

            } catch (IOException e) {
                e.printStackTrace();
            }

            setFoto(event.getFile().getContents());

            InputStream is = null;
            byte[] buffer = null;
            is = new FileInputStream(arquivo);
            buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            imagem = new DefaultStreamedContent(new ByteArrayInputStream(buffer));
            
        } catch (IOException ex) {
            Logger.getLogger(TesteUpload.class.getName()).log(Level.SEVERE, null, ex);
        }

//        setNomeArquivoSelecionado("Nome da imagem " + event.getFile().getFileName());
//        BufferedImage img;
//
//        System.out.println("Antes do try");
//
//        try {
//
//            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//            HttpSession session = request.getSession();
//            Usuario currentUser = (Usuario) session.getAttribute("usuario");
//            Ccm ccm = currentUser.getCcm();
//            String nomeArquivo = ccm.getIm();
//
//            System.out.println("Antes do set arquivo");
//            
//            setNomeArquivoSelecionado(nomeArquivo);
//
//            // getClass().getClassLoader().getResource(caminho).toString();
//
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
//            arquivo = scontext.getRealPath("/WEB-INF/upload/" + nomeArquivoSelecionado);
//
//            ccm.setLogo(arquivo);
//
//            DAOCcm daoCcm = new DAOCcm();
//            daoCcm.salvar(ccm);
//
//            System.out.println(arquivo);
//
//            try {
//                img = ImageIO.read(event.getFile().getInputstream());
//                File outputfile = new File(arquivo);
//
//                BufferedImage aux = new BufferedImage(128, 128, img.getType());//cria um buffer auxiliar com o tamanho desejado
//                Graphics2D g = aux.createGraphics();//pega a classe graphics do aux para edicao
//                AffineTransform at = AffineTransform.getScaleInstance((double) 128 / img.getWidth(), (double) 128 / img.getHeight());//cria a transformacao
//                g.drawRenderedImage(img, at);//pinta e transforma a imagem real no auxiliar
//
//                ImageIO.write(aux, "png", outputfile);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            System.out.println(nomeArquivoSelecionado);
//            setFoto(event.getFile().getContents());
//
//            InputStream is = null;
//            byte[] buffer = null;
//            is = new FileInputStream(arquivo);
//            buffer = new byte[is.available()];
//            is.read(buffer);
//            is.close();
//            imagem = new DefaultStreamedContent(new ByteArrayInputStream(buffer));
//
//        } catch (Exception ex) {
//            System.out.println("Erro... " + ex);
//        }
    }

    public String onFlowProcess(FlowEvent event) {
        logger.info("Current wizard step:" + event.getOldStep());
        logger.info("Next step:" + event.getNewStep());

        if (event.getNewStep().equals("logotipo") && event.getOldStep().equals("empresa") || event.getOldStep().equals("local")) {

            if (arquivo == null) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();


                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                HttpSession session = request.getSession();
                Usuario currentUser = (Usuario) session.getAttribute("usuario");
                Ccm ccm = currentUser.getCcm();
                String nomeArquivo = ccm.getLogo();
                String arquivo2 = "";
                if (nomeArquivo == null) {
                    arquivo2 = scontext.getRealPath("/WEB-INF/upload/logotipo.jpg");
                } else {
                    arquivo2 = scontext.getRealPath("/WEB-INF/upload/" + nomeArquivo + ".png");
                }

                try {
                    InputStream is = null;
                    byte[] buffer = null;
                    is = new FileInputStream(arquivo2);
                    buffer = new byte[is.available()];
                    is.read(buffer);
                    is.close();
                    imagem = new DefaultStreamedContent(new ByteArrayInputStream(buffer));
                } catch (Exception e) {
                    System.out.println(e);
                }

            } else {

                FacesContext facesContext = FacesContext.getCurrentInstance();
                ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
                // String arquivo2 = scontext.getRealPath("/WEB-INF/upload/download1.png");

                try {
                    InputStream is = null;
                    byte[] buffer = null;
                    is = new FileInputStream(arquivo);
                    buffer = new byte[is.available()];
                    is.read(buffer);
                    is.close();
                    imagem = new DefaultStreamedContent(new ByteArrayInputStream(buffer));
                } catch (Exception e) {
                    System.out.println("Erro maldito aparece!!! " + e);
                }
            }
        }

        if (event.getNewStep().equals("local") && event.getOldStep().equals("logotipo") || event.getOldStep().equals("contatos")) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ATANÇÃO! ", "Esses dados não poderão ser alterados, somente com solicitação á preferitura"));
        }
        String retorno = null;
        try {
            retorno = event.getNewStep();
            System.out.println(retorno);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return retorno;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public StreamedContent getImagem() {
        return imagem;
    }

    public void setImagem(StreamedContent imagem) {
        this.imagem = imagem;
    }

    public String getNomeArquivoSelecionado() {
        return nomeArquivoSelecionado;
    }

    public void setNomeArquivoSelecionado(String nomeArquivoSelecionado) {
        this.nomeArquivoSelecionado = nomeArquivoSelecionado;
    }
}
