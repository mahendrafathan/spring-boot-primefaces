/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import javax.faces.application.FacesMessage;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.app.config.UserValidator;
import prosia.app.model.User;
import prosia.app.service.UserService;
import prosia.app.web.util.AbstractManagedBean;
import static prosia.app.web.util.AbstractManagedBean.showGrowl;

/**
 *
 * @author Owner
 */
@Controller
@Scope("view")
@Data
public class RegistrationController extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private UserService userService;
    private User newUser;

    @Autowired
    private UserValidator userValidator;

    @Override
    public void afterPropertiesSet() throws Exception {
        newUser = new User();
    }

    public void registration() {

        if (userService.findByUsername(newUser.getUsername()) != null) {
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "User sudah terdaftar");
            RequestContext.getCurrentInstance().update("growl");
        } else {
            userService.save(newUser);
            newUser = new User();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "User berhasil didaftarkan");
            RequestContext.getCurrentInstance().update("growl");
        }
    }
}
