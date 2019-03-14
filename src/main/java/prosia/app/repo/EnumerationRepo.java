/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prosia.app.model.Enumeration;

/**
 *
 * @author Randy
 */
@Repository
public interface EnumerationRepo extends JpaRepository<Enumeration, Integer> {
    
    /**
     * @param enumName
     * @param type
     * @return enumeration with specific name and type.
     */
    public Enumeration findTop1ByEnumNameAndType(String enumName, Enumeration.EnumerationType type);
    
    /**
     * @param enumNameEn
     * @param type
     * @return enumeration with specific name and type.
     */
    public Enumeration findTop1ByEnumNameEnAndType(String enumNameEn, Enumeration.EnumerationType type);
    
}
