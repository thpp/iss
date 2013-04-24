package br.com.nfse.jsfuntil;

/**
 *
 * @author Thiago Henrique
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "emailValidator")
public class EmailValidator implements Validator {

    @Override
    public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {

        String enteredEmail = (String) object;
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(enteredEmail);

        boolean matchFound = m.matches();

        if (!matchFound) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"ERRO! ","E-mail Incorreto"));
        }
    }
}
