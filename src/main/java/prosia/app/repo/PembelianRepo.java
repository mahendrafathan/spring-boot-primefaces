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
import prosia.app.model.Pembelian;

/**
 *
 * @author Owner
 */
@Repository
public interface PembelianRepo extends JpaRepository<Pembelian, Integer>, JpaSpecificationExecutor<Pembelian> {

    public Pembelian findTop1ByNotaBeli(String nota);

    @Query(value = "SELECT * FROM pembelian p WHERE MONTH(p.tgl_beli) = ?1", nativeQuery = true)
    public List<Pembelian> listPembelianBulan(String bulan);

    @Query(value = "SELECT * FROM pembelian p where DATE_FORMAT(p.tgl_beli,'%d/%m/%Y') =  DATE_FORMAT(?1,'%d/%m/%Y')", nativeQuery = true)
    public List<Pembelian> listPembelianHari(Date tanggal);
}
