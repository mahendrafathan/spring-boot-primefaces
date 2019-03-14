/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prosia.app.model.Task;

/**
 *
 * @author Randy
 */
@Repository
public interface TaskRepo extends JpaRepository<Task, String> {
    
    /**
     * @param taskType
     * @return list of tasks with specific task_type and no status filter
     */
    public List<Task> findAllByTaskType(Task.Type taskType);
    
    public List<Task> findAllByTaskTypeAndStatus(Task.Type taskType , Task.Status status);
    
    /**
     * @param outcome
     * @return 
     */
    public Task findOneByOutcome(String outcome);
    
    /**
     * @param taskType
     * @param status
     * @return list of tasks with specific task_type, enabled status and outcome is not null.
     */
    public List<Task> findAllByTaskTypeAndStatusAndOutcomeNotNull(Task.Type taskType, Task.Status status);
    
}
