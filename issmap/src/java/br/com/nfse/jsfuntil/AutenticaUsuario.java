package br.com.nfse.jsfuntil;

import br.com.nfse.to.Usuario;

public class AutenticaUsuario {
   
     //define o template que será usado na visão de acordo com o tipo de usuario
    public static String verificaTemplate(Usuario usu){    
        String template = null;
        if (usu.getTipoUsuario() == 1){
            template =  "./template_principal.xhtml";
        }else if(usu.getTipoUsuario() == 2){
            template =  "./template_funcionario.xhtml"; 
        }
        if(usu.getCcm().getCodigoEventualGemmap() != null){
            template =  "./template_eventual.xhtml"; 
        }
        return template;
    }
}
