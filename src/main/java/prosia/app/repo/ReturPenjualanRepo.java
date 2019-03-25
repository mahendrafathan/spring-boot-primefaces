/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.repo;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prosia.app.model.ReturPenjualan;

/**
 *
 * @author Owner
 */
@Repository
public interface ReturPenjualanRepo extends JpaRepository<ReturPenjualan, Integer>, JpaSpecificationExecutor<ReturPenjualan> {
    
    public ReturPenjualan findTop1ByNotaJual(String nota);
    
    @Query(value = "SELECT * FROM retur_pembelian rp where DATE_FORMAT(rp.tgl_retur,'%d/%m/%Y') between DATE_FORMAT(?1,'%d/%m/%Y') and DATE_FORMAT(?2,'%d/%m/%Y')", nativeQuery = true)
    public List<ReturPenjualan> listReturPenjualan(Date tglMulai, Date tglAkhir);
}
