/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

import java.math.BigDecimal;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Thiago
* Efetua a validação de um valor monetário. 
*/  
@FacesValidator("MoedaValidator") 
public class MoedaValidator implements Validator {
    
     /** 
    * Método responsável por validar os campos com moeda. Caso ocorra algum erro lança uma ValidatorException. 
    */  
    @Override
   public void validate(FacesContext ctx, UIComponent comp, Object val) throws ValidatorException {  
         
       Double valor = (Double) val;  
         
       if(val == null)  
            return;               
         
        if (valor.intValue() == 0) {  
           FacesMessage message = new FacesMessage("Preencha com o valor válido maior que zero");  
           message.setSeverity(FacesMessage.SEVERITY_ERROR);  
           throw new ValidatorException(message);  
       }  
         
   }  
    
}
