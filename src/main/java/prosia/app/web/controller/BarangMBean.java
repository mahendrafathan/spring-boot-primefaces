/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import prosia.app.model.MstBarang;
import prosia.app.repo.BarangRepo;
import prosia.app.web.util.AbstractManagedBean;
import static prosia.app.web.util.AbstractManagedBean.getRequestParam;
import static prosia.app.web.util.AbstractManagedBean.showGrowl;
import prosia.app.web.util.LazyDataModelFilterJPA;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
@Data
public class BarangMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private BarangRepo barangRepo;
    private LazyDataModelFilterJPA<MstBarang> listBarang;
    private MstBarang mstBarang;
    private String kodeBarang;

    public void init() {
        mstBarang = new MstBarang();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listBarang = new LazyDataModelFilterJPA(barangRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                mstBarang.setKodeBarang((String) filters.get("kodeBarang"));
                mstBarang.setNamaBarang((String) filters.get("namaBarang"));
                mstBarang.setSpesifikasi((String) filters.get("spesifikasi"));
                mstBarang.setHargaBeli((Float) filters.get("hargaBeli"));
                mstBarang.setHargaJual((Float) filters.get("hargaJual"));
                mstBarang.setStok((Integer) filters.get("stok"));
                return barangRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                mstBarang.setKodeBarang((String) filters.get("kodeBarang"));
                mstBarang.setNamaBarang((String) filters.get("namaBarang"));
                mstBarang.setSpesifikasi((String) filters.get("spesifikasi"));
                mstBarang.setHargaBeli((Float) filters.get("hargaBeli"));
                mstBarang.setHargaJual((Float) filters.get("hargaJual"));
                mstBarang.setStok((Integer) filters.get("stok"));
                return barangRepo.count(whereQuery());
            }
        };
    }

    public Specification<MstBarang> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstBarang>() {
            @Override
            public Predicate toPredicate(Root<MstBarang> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(mstBarang.getKodeBarang())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kodeBarang")),
                            getLikePattern(mstBarang.getKodeBarang())));
                }
                if (StringUtils.isNotBlank(mstBarang.getNamaBarang())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("namaBarang")),
                            getLikePattern(mstBarang.getNamaBarang())));
                }
                if (StringUtils.isNotBlank(mstBarang.getSpesifikasi())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("spesifikasi")),
                            getLikePattern(mstBarang.getSpesifikasi())));
                }
                if (mstBarang.getHargaBeli() != null) {
                    predicates.add(cb.equal(cb.lower(root.<String>get("hargaBeli")),
                            mstBarang.getHargaBeli()));
                }
                if (mstBarang.getHargaJual() != null) {
                    predicates.add(cb.equal(cb.lower(root.<String>get("hargaJual")),
                            mstBarang.getHargaJual()));
                }
                if (mstBarang.getStok() != null) {
                    predicates.add(cb.equal(cb.lower(root.<String>get("stok")),
                            mstBarang.getStok()));
                }
                predicates.add(cb.equal(root.<Integer>get("status"), MstBarang.Status.ACTIVE));
                query.orderBy(cb.asc(root.<BigDecimal>get("barangId")));
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

    public void cari() {
        log.debug("mstBarang : {}", mstBarang);
        kodeBarang = mstBarang.getKodeBarang();
        mstBarang = barangRepo.findTop1ByKodeBarangAndStatus(kodeBarang, MstBarang.Status.ACTIVE);
        log.debug("mstBarang setelah cari : {}", mstBarang);
        if (mstBarang == null) {
            showGrowl(FacesMessage.SEVERITY_WARN, "Informasi", "Data tidak ditemukan");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
            init();
            return;
        }
    }

    public void tambah() throws InterruptedException {
        try {
            log.debug("tambah");
            MstBarang pMstBarang = new MstBarang();
            pMstBarang = barangRepo.findTop1ByKodeBarang(mstBarang.getKodeBarang());
            if (pMstBarang != null) {
                if (pMstBarang.getStatus().equals(MstBarang.Status.ACTIVE)) {
                    showGrowl(FacesMessage.SEVERITY_WARN, "Informasi", "Barang dengan kode " + pMstBarang.getKodeBarang() + " sudah tersedia");
                    RequestContext.getCurrentInstance().update("growl");
                    return;
                } else {
                    pMstBarang.setKodeBarang(mstBarang.getKodeBarang());
                    pMstBarang.setNamaBarang(mstBarang.getNamaBarang());
                    pMstBarang.setHargaBeli(mstBarang.getHargaBeli());
                    pMstBarang.setHargaJual(mstBarang.getHargaJual());
                    pMstBarang.setSpesifikasi(mstBarang.getSpesifikasi());
                    pMstBarang.setKeterangan(mstBarang.getKeterangan());
                    pMstBarang.setStok(mstBarang.getStok());
                    pMstBarang.setCreatedAt(new Date());
                    pMstBarang.setCreatedAt(new Date());
                    pMstBarang.setStatus(MstBarang.Status.ACTIVE);
                    log.debug("mstBarang : {}", mstBarang);
                    barangRepo.save(pMstBarang);
                    showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
                    RequestContext.getCurrentInstance().update("idList");
                    RequestContext.getCurrentInstance().update("growl");
                    init();
                    return;
                }
            }
//        mstBarang.setBarangId(1);
            if (kodeBarang != null) {
                if (!kodeBarang.equals(mstBarang.getKodeBarang())) {
                    pMstBarang = new MstBarang();
                    pMstBarang.setKodeBarang(mstBarang.getKodeBarang());
                    pMstBarang.setNamaBarang(mstBarang.getNamaBarang());
                    pMstBarang.setHargaBeli(mstBarang.getHargaBeli());
                    pMstBarang.setHargaJual(mstBarang.getHargaJual());
                    pMstBarang.setSpesifikasi(mstBarang.getSpesifikasi());
                    pMstBarang.setKeterangan(mstBarang.getKeterangan());
                    pMstBarang.setStok(mstBarang.getStok());
                    pMstBarang.setCreatedAt(new Date());
                    pMstBarang.setCreatedAt(new Date());
                    pMstBarang.setStatus(MstBarang.Status.ACTIVE);
                    log.debug("mstBarang : {}", mstBarang);
                    barangRepo.save(pMstBarang);
                    showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
                    RequestContext.getCurrentInstance().update("idList");
                    RequestContext.getCurrentInstance().update("growl");
                    init();
                    return;
                }
            }
            mstBarang.setCreatedAt(new Date());
            mstBarang.setStatus(MstBarang.Status.ACTIVE);
            log.debug("mstBarang : {}", mstBarang);
            barangRepo.save(mstBarang);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            Logger.getLogger(BarangMBean.class.getName()).log(Level.SEVERE, null, ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan ubah data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void ubah() throws InterruptedException {
        try {
            if (kodeBarang == null) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Untuk ubah, cari terlebih dahulu Kode Barang");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            if (!kodeBarang.equals(mstBarang.getKodeBarang())) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Kode Barang tidak dapat diganti");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            log.debug("mstBarang : {}", mstBarang);
            MstBarang pMstBarang = new MstBarang();
            pMstBarang = barangRepo.findTop1ByKodeBarang(mstBarang.getKodeBarang());
            if (pMstBarang != null && !kodeBarang.equals(mstBarang.getKodeBarang())) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Barang dengan kode " + pMstBarang.getKodeBarang() + " sudah tersedia");
                RequestContext.getCurrentInstance().update("growl");
                return;
            } else if (pMstBarang.equals(mstBarang)) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Tidak ada perubahan data, silahkan ubah data barang");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            mstBarang.setUpdatedAt(new Date());
            mstBarang.setStatus(MstBarang.Status.ACTIVE);
            log.debug("mstBarang : {}", mstBarang);
            barangRepo.save(mstBarang);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            Logger.getLogger(BarangMBean.class.getName()).log(Level.SEVERE, null, ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan ubah data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void hapus() throws InterruptedException {
        try {
            if (!kodeBarang.equals(mstBarang.getKodeBarang())) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Kode Barang tidak dapat diganti");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            log.debug("mstBarang : {}", mstBarang);
            mstBarang.setUpdatedAt(new Date());
            mstBarang.setStatus(MstBarang.Status.INACTIVE);
            barangRepo.save(mstBarang);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            Logger.getLogger(BarangMBean.class.getName()).log(Level.SEVERE, null, ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan hapus data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void tutup() throws IOException {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BarangMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
