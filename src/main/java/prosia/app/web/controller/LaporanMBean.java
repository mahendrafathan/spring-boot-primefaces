/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import prosia.app.model.MstBarang;
import prosia.app.model.Pembelian;
import prosia.app.model.Penjualan;
import prosia.app.model.ReturPembelian;
import prosia.app.model.ReturPenjualan;
import prosia.app.repo.BarangRepo;
import prosia.app.repo.PembelianRepo;
import prosia.app.repo.PenjualanRepo;
import prosia.app.repo.ReturPembelianRepo;
import prosia.app.repo.ReturPenjualanRepo;
import prosia.app.web.util.AbstractManagedBean;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
@Data
public class LaporanMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private PembelianRepo pembelianRepo;
    private Pembelian pembelian;
    private List<Pembelian> listPembelian;

    @Autowired
    private PenjualanRepo penjualanRepo;
    private Penjualan penjualan;
    private List<Penjualan> listPenjualan;

    @Autowired
    private ReturPembelianRepo returPembelianRepo;
    private ReturPembelian returPembelian;
    private List<ReturPembelian> listReturPembelian;

    @Autowired
    private ReturPenjualanRepo returPenjualanRepo;
    private ReturPenjualan returPenjualan;
    private List<ReturPenjualan> listReturPenjualan;

    @Autowired
    private BarangRepo barangRepo;
    private List<MstBarang> listBarang;
    private MstBarang mstBarang;

    private List<Tahun> listTahun;
    private Tahun objTahun;
    private Integer tahun;
    private List<Bulan> listBulan;
    private Bulan objBulan;
    private Integer bulan;

    private String periode;
    private Date tglAwal;
    private Date tglAkhir;

    public void init() {
        pembelian = new Pembelian();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listTahun = new ArrayList<>();
        for (Integer year = 2000; year <= Calendar.getInstance().get(Calendar.YEAR); year++) {
//            System.out.println("year = " + year);

            objTahun = new Tahun();
            objTahun.setTahun(year);
            listTahun.add(objTahun);
        }
    }

//    public void onChangePeriode() {
//        System.out.println("periode = " + periode);
//        tanggal = periode.equals("hari") ? tanggal : null;
//        bulan = periode.equals("bulan") ? bulan : null;
//    }

    public void onChangeTahun() {
        Date today = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(today);
        Integer yearNow = now.get(Calendar.YEAR);

        if (tahun.equals(yearNow)) {
            System.out.println("bulan calendar get instance month = " + now.get(Calendar.MONTH));
            System.out.println("bulan calendar get instance day = " + now.get(Calendar.DAY_OF_MONTH));
            listBulan = new ArrayList<>();
            for (Integer month = 0; month <= now.get(Calendar.MONTH); month++) {
                objBulan = new Bulan();
                objBulan.setBulan(month);
                listBulan.add(objBulan);
            }
        } else if (tahun < yearNow) {
            listBulan = new ArrayList<>();
            for (Integer month = 0; month <= 11; month++) {
                objBulan = new Bulan();
                objBulan.setBulan(month);
                listBulan.add(objBulan);
            }
        }
    }

    public void tampilkan() {
        String laporan = (String) getRequestParam("laporan");
        if (laporan.equals("pembelian")) {
            listPembelian = pembelianRepo.listPembelian(tglAwal, tglAkhir);
        } else if (laporan.equals("penjualan")) {
            listPenjualan = penjualanRepo.listPenjualan(tglAwal, tglAkhir);
        } else if (laporan.equals("returpembelian")) {
            listReturPembelian = returPembelianRepo.listReturPembelian(tglAwal, tglAkhir);
        } else if (laporan.equals("returpenjualan")) {
            listReturPenjualan = returPenjualanRepo.listReturPenjualan(tglAwal, tglAkhir);
        } else if (laporan.equals("inventory")) {
            listBarang = barangRepo.findAllByOrderByNamaBarangAsc();
        }
//        System.out.println("listPembelian = " + listPembelian);
        RequestContext.getCurrentInstance().reset("idDialogCetak");
        RequestContext.getCurrentInstance().update("idDialogCetak");
        RequestContext.getCurrentInstance().execute("PF('showDialogCetak').show()");
    }

    @Data
    public static class Tahun implements Serializable {

        private Integer tahun;
    }

    @Data
    public static class Bulan implements Serializable {

        private Integer bulan;
    }

}
