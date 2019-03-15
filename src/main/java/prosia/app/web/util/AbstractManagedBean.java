/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import prosia.app.model.Role;
import prosia.app.web.model.SecureItem;
import prosia.app.model.Task;
import prosia.app.model.User;
import prosia.app.repo.TaskRepo;
import prosia.app.web.controller.ApplicationData;

/**
 * Extends this class for all the ManagedBean classes that handles menu view. That class will be registered as 'task' 
 * with 'TopLevelMenu' parent and the secure objects that been declared will also be registered as 'task' 
 * with '{managed_bean_package_name}' parent.
 * @author Randy
 */
public abstract class AbstractManagedBean implements Serializable {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    protected Task currentTask;
    
    private User currentUser;
    
    private List<SecureItem> secureItems;
    
    @Autowired
    protected ApplicationData applicationData;
    
    @Autowired
    protected TaskRepo taskRepo;
    
    public AbstractManagedBean() {
    }
    
    @PostConstruct
    public void initialize() {
        // get current user
        try {
            this.currentUser = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e) {
            this.currentUser = null;
        }
        
        // get menu_task
        this.currentTask = taskRepo.findOne(getTaskId());
        
        // insert child_task
        insertChildTask();
    }
    
    /**
     * @return application from id parameter.
     */
    
    
    /**
     * @return the current user that has been login to system.
     */
    protected User getCurrentUser() {
        return this.currentUser;
    }
    
    /**
     * Insert child_task.
     */
    private void insertChildTask() {
        // load secure_item
        this.secureItems = getSecureItems();
        
        if (this.currentTask == null || this.secureItems == null) {
            return;
        }
        
        short sequence = 0;
        
        for (SecureItem secureItem : this.secureItems) {
            // get task_type
            String taskId = this.currentTask.getTaskId() + "." + secureItem.getItemName();
            
            Task.Type taskType = null;
            
            if (secureItem.getItemType().equals(Task.Type.ACTION) &&
                    !isTaskExistInList(taskId, this.applicationData.getListTaskAction())) {
                taskType = Task.Type.ACTION;
                
            } else if (secureItem.getItemType().equals(Task.Type.FIELD) && 
                    !isTaskExistInList(taskId, this.applicationData.getListTaskField())) {
                taskType = Task.Type.FIELD;
            }
            
            // add sequence
            sequence++;

            // insert navigation_field if not exists
//            if (taskType != null) {
//                Task childTask = new Task(taskId, 
//                        viewBean.addWhiteSpaceBetweenCapitalLetters(secureItem.getItemName()), taskType);
//                childTask.setStatus(Task.Status.ACTIVE);
//                
//                try {
//                    taskService.insertTask(childTask, getCurrentUser().getRoles(), this.currentTask, sequence);
//                    
//                    log.info("A new task had been inserted : {}", taskId);
//                } catch (Exception e) {
//                    log.error("Failed to insert a new task {} : {}", taskId, e);
//                }
//            }
        }
    }
    
    /**
     * Get the access_right for the specific secure_item_name.
     * @param secureItem
     * @return 
     */
   
    
    /**
     * Validate if the secure_item is visible or not.
     * @param secureItem
     * @return 'true' if visible and 'false' if hide/collapse.
     */
  
    
    /**
     * Validate if the secure_item could be edited or not. Use this result 
     * to define editable secure_item for form component.
     * @param secureItem
     * @return 'true' if editable and 'false' if not editable.
     */
    
    
    /**
     * Validate if the secure_item is read_only or not.
     * @param secureItem
     * @return 'true' if read_only and 'false' if could be access.
     */
   

    protected void addMessage(String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    protected void addMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary, detail));
    }

    protected void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }
    
    protected void addPopUpMessage(String summary) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(summary));
    }

    protected void addPopUpMessage(String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(summary, detail));
    }

    protected void addPopUpMessage(FacesMessage.Severity severity, String summary, String detail) {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(severity, summary, detail));
    }
    
    /**
     * @param key
     * @return message locale.
     */
    
    /**
     * @param taskId
     * @param source
     * @return true if taskId already exists in the source list.
     */
    protected boolean isTaskExistInList(String taskId, List<Task> source) {
        boolean result = false;
        for (Task task : source) {
            if (task.getTaskId().equals(taskId)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    /**
     * @return the full package with class name.
     */
    protected String getTaskId() {
        return getClass().getName();
    }
    
    /**
     * @return the class name without its package.
     */
    protected String getTaskName() {
        return getClass().getSimpleName();
    }
    
    /**
     * @return the secure_items from the current task.
     */
    protected abstract List<SecureItem> getSecureItems();
    
    public static void showMessageInDialog(FacesMessage.Severity severity, String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary,
                detail);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
}

    public static void showGrowl(FacesMessage.Severity severity, String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary,
                detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public static Object getRequestParam(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = UIComponent.getCurrentComponent(context);
        Map<String, Object> map = component.getAttributes();

        return map.get(name);
    }
    
}
