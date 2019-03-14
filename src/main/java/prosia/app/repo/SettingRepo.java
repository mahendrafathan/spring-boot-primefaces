/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.app.model.Setting;

/**
 *
 * @author Randy
 */
@Repository
public interface SettingRepo extends JpaRepository<Setting, String> {

    @Query(value = "SELECT s FROM Setting s WHERE s.prefixName IN (?1, ?2, ?3, ?4, ?5, ?6, ?7)")
    List<Setting> findAllByTujuhValue(String input, String input2, String input3, String input4,
            String input5, String input6, String input7);
    
    public Setting findTop1ByPrefixName (String name);
}
