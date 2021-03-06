/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.repo;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import prosia.app.model.MstSupplier;

/**
 *
 * @author PROSIA
 */
public interface SupplierRepo extends JpaRepository<MstSupplier, BigDecimal>, JpaSpecificationExecutor<MstSupplier> {
    
    public List<MstSupplier> findAllByStatusOrderByNamaSupplierAsc(MstSupplier.Status s);
    
    public List<MstSupplier> findAllByOrderByNamaSupplierAsc();

    public MstSupplier findTop1ByNamaSupplierAndStatus(String namaSupplier, MstSupplier.Status s);
    
    public MstSupplier findTop1ByKodeSupplierAndStatus(String kodeSupplier, MstSupplier.Status s);

    public MstSupplier findTop1ByKodeSupplier(String kodeSupplier);
}
