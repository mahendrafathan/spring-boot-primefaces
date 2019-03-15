/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.imi.repo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import prosia.imi.model.Refkebangsaan;

/**
 *
 * @author PROSIA
 */
public interface NegaraRepo extends JpaRepository<Refkebangsaan, BigDecimal>, JpaSpecificationExecutor<Refkebangsaan> {

    public List<Refkebangsaan> findAllByStatusOrderByKebangsaanAsc(Refkebangsaan.Status s);
    
    public List<Refkebangsaan> findAllByOrderByKebangsaanAsc();

    public Refkebangsaan findTop1ByKodenegaraAndStatus(String kodenegara, Refkebangsaan.Status s);
    
    public Refkebangsaan findTop1ByKebangsaanAndStatus(String kebangsaan, Refkebangsaan.Status s);
    
    public Refkebangsaan findTop1ByKebangsaanid(Integer kebangsaanid);
    
    public Refkebangsaan findTop1ByKodenegara(String kode);
}
