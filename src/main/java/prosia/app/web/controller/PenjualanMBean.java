/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import prosia.app.model.MstBarang;
import prosia.app.model.MstPelanggan;
import prosia.app.model.MstSupplier;
import prosia.app.model.Pembelian;
import prosia.app.model.Penjualan;
import prosia.app.repo.BarangRepo;
import prosia.app.repo.PelangganRepo;
import prosia.app.repo.PenjualanRepo;
import prosia.app.repo.SupplierRepo;
import prosia.app.web.util.AbstractManagedBean;
import static prosia.app.web.util.AbstractManagedBean.showGrowl;
import prosia.app.web.util.LazyDataModelFilterJPA;

/**
 *
 * @author Owner
 */
@Controller
@Scope("view")
@Data
public class PenjualanMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private PenjualanRepo penjualanRepo;
    private Penjualan penjualan;
    private LazyDataModelFilterJPA<Penjualan> listPenjualan;

    @Autowired
    private BarangRepo barangRepo;
    private List<MstBarang> listBarang;

    @Autowired
    private PelangganRepo pelangganRepo;
    private List<MstPelanggan> listPelanggan;

    public void init() {
        penjualan = new Penjualan();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listBarang = barangRepo.findAll();
        listPelanggan = pelangganRepo.findAll();

        listPenjualan = new LazyDataModelFilterJPA(penjualanRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                return penjualanRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                return penjualanRepo.count(whereQuery());
            }

        };
    }

    public Specification<Penjualan> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<Penjualan>() {
            @Override
            public Predicate toPredicate(Root<Penjualan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.orderBy(cb.asc(root.<BigDecimal>get("penjualanId")));
                return andTogether(predicates, cb);
            }
        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    public void tambah() {
        Penjualan penjualanTmp = penjualanRepo.findTop1ByNoFaktur(penjualan.getNoFaktur());
        if (penjualanTmp != null) {
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data sudah ada, klik ubah");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
            return;
        }
        penjualanRepo.save(penjualan);
        init();
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
    }

    public void cari() {
        penjualan = penjualanRepo.findTop1ByNoFaktur(penjualan.getNoFaktur());
        if (penjualan == null) {
            penjualan = new Penjualan();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data tidak ditemukan");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
        }
    }

    public void ubah() {
        Penjualan penjualanTmp = penjualanRepo.findTop1ByNoFaktur(penjualan.getNoFaktur());
        if (penjualanTmp == null) {
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Cari data terlebih dahulu");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
            return;
        }
        penjualanRepo.save(penjualan);
        init();
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
    }

    public void hapus() {
        Penjualan penjualanTmp = penjualanRepo.findTop1ByNoFaktur(penjualan.getNoFaktur());
        if (penjualanTmp == null) {
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Cari data terlebih dahulu");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
            return;
        }
        penjualanRepo.delete(penjualan);
        init();
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
    }

    public void cetak() {
        Penjualan penjualanTmp = penjualanRepo.findTop1ByNoFaktur(penjualan.getNoFaktur());
        if (penjualanTmp == null) {
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Cari data terlebih dahulu");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
            return;
        }
        RequestContext.getCurrentInstance().reset("idDialogCetak");
        RequestContext.getCurrentInstance().update("idDialogCetak");
        RequestContext.getCurrentInstance().execute("PF('showDialogCetak').show()");
    }

    public void onChangeBarang() {
        if (penjualan != null && penjualan.getKodeBarang() != null) {
            penjualan.setNamaBarang(penjualan.getKodeBarang().getNamaBarang());
            penjualan.setHargaSatuan(penjualan.getKodeBarang().getHargaJual());
            penjualan.setStok(penjualan.getKodeBarang().getStok());
        } else {
            penjualan.setNamaBarang(null);
            penjualan.setHargaSatuan(null);
            penjualan.setStok(null);
        }
    }

    public void onChangePelanggan() {
        if (penjualan != null && penjualan.getKodePelanggan() != null) {
            penjualan.setNamaPelanggan(penjualan.getKodePelanggan().getNamaPelanggan());
        } else {
            penjualan.setNamaPelanggan(null);
        }
    }

    public void totalHarga() {
        if (penjualan.getHargaSatuan() != null && penjualan.getDiskon() != null && penjualan.getJumlahJual()!= null) {
            penjualan.setTotalHarga((penjualan.getHargaSatuan() * (100 - penjualan.getDiskon()) / 100) * penjualan.getJumlahJual());
            penjualan.setStok(penjualan.getStok() - penjualan.getJumlahJual());
        }
    }
}
