/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import prosia.app.model.MstSupplier;
import prosia.app.repo.SupplierRepo;
import prosia.app.web.util.AbstractManagedBean;
import static prosia.app.web.util.AbstractManagedBean.showGrowl;
import prosia.app.web.util.LazyDataModelFilterJPA;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
@Data
public class SupplierMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private SupplierRepo supplierRepo;
    private LazyDataModelFilterJPA<MstSupplier> listSupplier;
    private List<MstSupplier> listSupplier2;
    private MstSupplier mstSupplier;
    private String kodeSupplier;

    public void init() {
        mstSupplier = new MstSupplier();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listSupplier2 = new ArrayList<>();
        listSupplier2 = supplierRepo.findAllByStatusOrderByNamaSupplierAsc(MstSupplier.Status.ACTIVE);
        listSupplier = new LazyDataModelFilterJPA(supplierRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                mstSupplier.setKodeSupplier((String) filters.get("kodeSupplier"));
                mstSupplier.setNamaSupplier((String) filters.get("namaSupplier"));
                mstSupplier.setEmail((String) filters.get("email"));
                mstSupplier.setAlamat((String) filters.get("alamat"));
                mstSupplier.setKota((String) filters.get("kota"));
                mstSupplier.setTelepon((BigInteger) filters.get("telepon"));
                return supplierRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                mstSupplier.setKodeSupplier((String) filters.get("kodeSupplier"));
                mstSupplier.setNamaSupplier((String) filters.get("namaSupplier"));
                mstSupplier.setEmail((String) filters.get("email"));
                mstSupplier.setAlamat((String) filters.get("alamat"));
                mstSupplier.setKota((String) filters.get("kota"));
                mstSupplier.setTelepon((BigInteger) filters.get("telepon"));
                return supplierRepo.count(whereQuery());
            }
        };
    }

    public Specification<MstSupplier> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstSupplier>() {
            @Override
            public Predicate toPredicate(Root<MstSupplier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(mstSupplier.getKodeSupplier())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kodeSupplier")),
                            getLikePattern(mstSupplier.getKodeSupplier())));
                }
                if (StringUtils.isNotBlank(mstSupplier.getNamaSupplier())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("namaSupplier")),
                            getLikePattern(mstSupplier.getNamaSupplier())));
                }
                if (StringUtils.isNotBlank(mstSupplier.getEmail())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("email")),
                            getLikePattern(mstSupplier.getEmail())));
                }
                if (StringUtils.isNotBlank(mstSupplier.getAlamat())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("alamat")),
                            getLikePattern(mstSupplier.getAlamat())));
                }
                if (StringUtils.isNotBlank(mstSupplier.getKota())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kota")),
                            getLikePattern(mstSupplier.getKota())));
                }
                if (mstSupplier.getTelepon() != null) {
                    predicates.add(cb.equal(cb.lower(root.<String>get("telepon")),
                            mstSupplier.getTelepon()));
                }
                predicates.add(cb.equal(root.<Integer>get("status"), MstSupplier.Status.ACTIVE));
                query.orderBy(cb.asc(root.<BigDecimal>get("supplierId")));
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
        kodeSupplier = mstSupplier.getKodeSupplier();
        mstSupplier = supplierRepo.findTop1ByKodeSupplierAndStatus(kodeSupplier, MstSupplier.Status.ACTIVE);
        log.debug("mstSupplier setelah cari : {}", mstSupplier);
        if (mstSupplier == null) {
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
            MstSupplier pMstSupplier = new MstSupplier();
            pMstSupplier = supplierRepo.findTop1ByKodeSupplier(mstSupplier.getKodeSupplier());
            if (pMstSupplier != null) {
                if (pMstSupplier.getStatus().equals(MstSupplier.Status.ACTIVE)) {
                    showGrowl(FacesMessage.SEVERITY_WARN, "Informasi", "Supplier dengan kode " + pMstSupplier.getKodeSupplier() + " sudah tersedia");
                    RequestContext.getCurrentInstance().update("growl");
                    return;
                } else {
                    pMstSupplier.setKodeSupplier(mstSupplier.getKodeSupplier());
                    pMstSupplier.setNamaSupplier(mstSupplier.getNamaSupplier());
                    pMstSupplier.setEmail(mstSupplier.getEmail());
                    pMstSupplier.setAlamat(mstSupplier.getAlamat());
                    pMstSupplier.setKota(mstSupplier.getKota());
                    pMstSupplier.setTelepon(mstSupplier.getTelepon());
                    pMstSupplier.setCreatedAt(new Date());
                    pMstSupplier.setUpdatedAt(new Date());
                    pMstSupplier.setStatus(MstSupplier.Status.ACTIVE);
                    supplierRepo.save(pMstSupplier);
                    showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
                    RequestContext.getCurrentInstance().update("idList");
                    RequestContext.getCurrentInstance().update("growl");
                    init();
                    return;
                }
            }
            if (kodeSupplier != null) {
                if (!kodeSupplier.equals(mstSupplier.getKodeSupplier())) {
                    pMstSupplier = new MstSupplier();
                    pMstSupplier.setKodeSupplier(mstSupplier.getKodeSupplier());
                    pMstSupplier.setNamaSupplier(mstSupplier.getNamaSupplier());
                    pMstSupplier.setEmail(mstSupplier.getEmail());
                    pMstSupplier.setAlamat(mstSupplier.getAlamat());
                    pMstSupplier.setKota(mstSupplier.getKota());
                    pMstSupplier.setTelepon(mstSupplier.getTelepon());
                    pMstSupplier.setCreatedAt(new Date());
                    pMstSupplier.setUpdatedAt(new Date());
                    pMstSupplier.setStatus(MstSupplier.Status.ACTIVE);
                    supplierRepo.save(pMstSupplier);
                    showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
                    RequestContext.getCurrentInstance().update("idList");
                    RequestContext.getCurrentInstance().update("growl");
                    init();
                    return;
                }
            }
//        mstSupplier.setSupplierId(1);
            mstSupplier.setCreatedAt(new Date());
            mstSupplier.setStatus(MstSupplier.Status.ACTIVE);
            supplierRepo.save(mstSupplier);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            Logger.getLogger(SupplierMBean.class.getName()).log(Level.SEVERE, null, ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan ubah data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void ubah() throws InterruptedException {
        try {
            if (kodeSupplier == null) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Untuk ubah, cari terlebih dahulu Kode Supplier");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            if (!kodeSupplier.equals(mstSupplier.getKodeSupplier())) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Kode Supplier tidak dapat diganti");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            MstSupplier pMstSupplier = new MstSupplier();
            pMstSupplier = supplierRepo.findTop1ByKodeSupplier(mstSupplier.getKodeSupplier());
            if (pMstSupplier != null && !kodeSupplier.equals(mstSupplier.getKodeSupplier())) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Supplier dengan kode " + pMstSupplier.getKodeSupplier() + " sudah tersedia");
                RequestContext.getCurrentInstance().update("growl");
                return;
            } else if (pMstSupplier.equals(mstSupplier)) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Tidak ada perubahan data, silahkan ubah data supplier");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            mstSupplier.setUpdatedAt(new Date());
            mstSupplier.setStatus(MstSupplier.Status.ACTIVE);
            supplierRepo.save(mstSupplier);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil diubah");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            Logger.getLogger(SupplierMBean.class.getName()).log(Level.SEVERE, null, ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan ubah data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void hapus() throws InterruptedException {
        try {
            if (!kodeSupplier.equals(mstSupplier.getKodeSupplier())) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Kode Supplier tidak dapat diganti");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            mstSupplier.setUpdatedAt(new Date());
            mstSupplier.setStatus(MstSupplier.Status.INACTIVE);
            supplierRepo.save(mstSupplier);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            Logger.getLogger(SupplierMBean.class.getName()).log(Level.SEVERE, null, ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan ubah data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void tutup() throws IOException {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(SupplierMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showDialogCetak() {
        RequestContext.getCurrentInstance().reset("idDialogCetak");
        RequestContext.getCurrentInstance().update("idDialogCetak");
        RequestContext.getCurrentInstance().execute("PF('showDialogCetak').show()");
    }
}
