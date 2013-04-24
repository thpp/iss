package br.com.nfse.email;

import br.com.nfse.jsfuntil.ConfiguracaoSyst;
import java.net.MalformedURLException;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class CommonsMail {

    public CommonsMail() throws EmailException, MalformedURLException {
        //enviaEmailSimples();
        //enviaEmailComAnexo();
        //enviaEmailFormatoHtml();
    }

    /**
     * envia email simples(somente texto)
     * @throws EmailException
     */
    public void enviaEmailSimples(String emailDestinatario, String nomeDestinatario, String empresaDestinatario) throws EmailException {

        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
        email.addTo(emailDestinatario, nomeDestinatario); //destinatário
        email.setFrom("thiago.henrique.paiv@gmail.com", ConfiguracaoSyst.NOME_CIDADE); // remetente
        email.setSubject("Ativar Acesso IssMap"); // assunto do e-mail
        email.setMsg("Olá "+nomeDestinatario +", foi efetuado o seu cadastramento ao IssMap,"
                + " sistema de emissão de nota fiscal eletrônica de serviço da "+ConfiguracaoSyst.NOME_CIDADE+","
                + " junto a empresa "+empresaDestinatario+", segue abaixo o link que o levará a pagina de cadastro"
                + " de sua senha de acesso, lembrando que a senha é um item individual que por sua vez é intransferível e"
                + " representa a sua assinatura eletrônica dentro do sistema, assegurando sua total responsabilidade decorrente do"
                + " uso indevido de sua senha. "
                + "\n \n Dicas de segurança "
                + "\n -	 A "+ConfiguracaoSyst.NOME_CIDADE+" não envia arquivos anexados aos e-mails e não pede senha ou login através de comunicações com você."
                + "\n -	 Verifique sempre os links antes de clicá-los passando o mouse. Se o link terminar com .exe/.com/.pif/.scr/.bat/.cmd/.msi/…, trata-se de um vírus que pode contaminar seu computador."
                + "\n \n \n Ative seu acesso: \n http://http://201.43.6.22:9090/IssMap/cadastro_senha_usuario.xhtml "); //conteudo do e-mail
        email.setAuthentication("micromapweb@gmail.com", "micromap007");// ususario e senha
        email.setSmtpPort(465);
        email.setSSL(true);
        email.setTLS(true);
        email.send();
    }

    /**
     * @param args
     * @throws EmailException
     * @throws MalformedURLException
     */
    public static void main(String[] args) throws EmailException, MalformedURLException {
        new CommonsMail();
    }

}