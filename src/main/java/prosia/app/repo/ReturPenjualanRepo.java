/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.repo;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import prosia.app.model.ReturPenjualan;

/**
 *
 * @author Owner
 */
@Repository
public interface ReturPenjualanRepo extends JpaRepository<ReturPenjualan, Integer>, JpaSpecificationExecutor<ReturPenjualan> {
    
}