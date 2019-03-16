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
import prosia.app.model.Penjualan;

/**
 *
 * @author Owner
 */
@Repository
public interface PenjualanRepo extends JpaRepository<Penjualan, Integer>, JpaSpecificationExecutor<Penjualan>{
    
    public Penjualan findTop1ByNoFaktur(String noFaktur);
    
    @Query(value = "SELECT * FROM penjualan p WHERE MONTH(p.tgl_jual) = ?1", nativeQuery = true)
    public List<Penjualan> listPenjualanBulan(String bulan);

    @Query(value = "SELECT * FROM penjualan p where DATE_FORMAT(p.tgl_jual,'%d/%m/%Y') =  DATE_FORMAT(?1,'%d/%m/%Y')", nativeQuery = true)
    public List<Penjualan> listPenjualanHari(Date tanggal);
}
