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
import prosia.app.model.Penjualan;

/**
 *
 * @author Owner
 */
@Repository
public interface PenjualanRepo extends JpaRepository<Penjualan, Integer>, JpaSpecificationExecutor<Penjualan>{
    
    public Penjualan findTop1ByNoFaktur(String noFaktur);
}