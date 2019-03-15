/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import prosia.app.config.UserValidator;
import prosia.app.model.User;
import prosia.app.repo.UserRepository;
import prosia.app.service.UserService;

/**
 *
 * @author Owner
 */
@Controller
@Scope("view")
@Data
public class RegistrationController implements InitializingBean {

    @Autowired
    private UserService userService;
    private User newUser;

    @Autowired
    private UserValidator userValidator;

    @Override
    public void afterPropertiesSet() throws Exception {
        newUser = new User();
    }

    public void registration(BindingResult bindingResult) {

        if (userService.findByUsername(newUser.getUsername()) != null) {
            System.out.println("sudah ada");
        } else {
            System.out.println("belum ada");
            userService.save(newUser);
        }
    }
}
