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
import prosia.app.model.MstPelanggan;
import prosia.app.repo.PelangganRepo;
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
public class PelangganMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private PelangganRepo pelangganRepo;
    private LazyDataModelFilterJPA<MstPelanggan> listPelanggan;
    private MstPelanggan mstPelanggan;
    private String kodePelanggan;

    public void init() {
        mstPelanggan = new MstPelanggan();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listPelanggan = new LazyDataModelFilterJPA(pelangganRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                mstPelanggan.setKodePelanggan((String) filters.get("kodePelanggan"));
                mstPelanggan.setNamaPelanggan((String) filters.get("namaPelanggan"));
                mstPelanggan.setAlamat((String) filters.get("alamat"));
                mstPelanggan.setKota((String) filters.get("kota"));
                mstPelanggan.setTelepon((Integer) filters.get("telepon"));
                return pelangganRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                mstPelanggan.setKodePelanggan((String) filters.get("kodePelanggan"));
                mstPelanggan.setNamaPelanggan((String) filters.get("namaPelanggan"));
                mstPelanggan.setAlamat((String) filters.get("alamat"));
                mstPelanggan.setKota((String) filters.get("kota"));
                mstPelanggan.setTelepon((Integer) filters.get("telepon"));
                return pelangganRepo.count(whereQuery());
            }
        };
    }

    public Specification<MstPelanggan> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstPelanggan>() {
            @Override
            public Predicate toPredicate(Root<MstPelanggan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(mstPelanggan.getKodePelanggan())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kodePelanggan")),
                            getLikePattern(mstPelanggan.getKodePelanggan())));
                }
                if (StringUtils.isNotBlank(mstPelanggan.getNamaPelanggan())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("namaPelanggan")),
                            getLikePattern(mstPelanggan.getNamaPelanggan())));
                }
                if (StringUtils.isNotBlank(mstPelanggan.getAlamat())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("alamat")),
                            getLikePattern(mstPelanggan.getAlamat())));
                }
                if (StringUtils.isNotBlank(mstPelanggan.getKota())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kota")),
                            getLikePattern(mstPelanggan.getKota())));
                }
                if (mstPelanggan.getTelepon() != null) {
                    predicates.add(cb.equal(cb.lower(root.<String>get("telepon")),
                            mstPelanggan.getTelepon()));
                }
                predicates.add(cb.equal(root.<Integer>get("status"), MstPelanggan.Status.ACTIVE));
                query.orderBy(cb.asc(root.<BigDecimal>get("pelangganId")));
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
        log.debug("mstPelanggan : {}", mstPelanggan);
        kodePelanggan = mstPelanggan.getKodePelanggan();
        mstPelanggan = pelangganRepo.findTop1ByKodePelangganAndStatus(kodePelanggan, MstPelanggan.Status.ACTIVE);
        log.debug("mstPelanggan setelah cari : {}", mstPelanggan);
        if (mstPelanggan == null) {
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
            MstPelanggan pMstPelanggan = new MstPelanggan();
            pMstPelanggan = pelangganRepo.findTop1ByKodePelanggan(mstPelanggan.getKodePelanggan());
            if (pMstPelanggan != null) {
                if (pMstPelanggan.getStatus().equals(MstPelanggan.Status.ACTIVE)) {
                    showGrowl(FacesMessage.SEVERITY_WARN, "Informasi", "Pelanggan dengan kode " + pMstPelanggan.getKodePelanggan() + " sudah tersedia");
                    RequestContext.getCurrentInstance().update("growl");
                    return;
                } else {
                    pMstPelanggan.setKodePelanggan(mstPelanggan.getKodePelanggan());
                    pMstPelanggan.setNamaPelanggan(mstPelanggan.getNamaPelanggan());
                    pMstPelanggan.setAlamat(mstPelanggan.getAlamat());
                    pMstPelanggan.setKota(mstPelanggan.getKota());
                    pMstPelanggan.setTelepon(mstPelanggan.getTelepon());
                    pMstPelanggan.setCreatedAt(new Date());
                    pMstPelanggan.setCreatedAt(new Date());
                    pMstPelanggan.setStatus(MstPelanggan.Status.ACTIVE);
                    log.debug("mstPelanggan : {}", mstPelanggan);
                    pelangganRepo.save(pMstPelanggan);
                    showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
                    RequestContext.getCurrentInstance().update("idList");
                    RequestContext.getCurrentInstance().update("growl");
                    init();
                    return;
                }
            }
            if (kodePelanggan != null) {
                if (!kodePelanggan.equals(mstPelanggan.getKodePelanggan())) {
                    pMstPelanggan = new MstPelanggan();
                    pMstPelanggan.setKodePelanggan(mstPelanggan.getKodePelanggan());
                    pMstPelanggan.setNamaPelanggan(mstPelanggan.getNamaPelanggan());
                    pMstPelanggan.setAlamat(mstPelanggan.getAlamat());
                    pMstPelanggan.setKota(mstPelanggan.getKota());
                    pMstPelanggan.setTelepon(mstPelanggan.getTelepon());
                    pMstPelanggan.setCreatedAt(new Date());
                    pMstPelanggan.setCreatedAt(new Date());
                    pMstPelanggan.setStatus(MstPelanggan.Status.ACTIVE);
                    log.debug("mstPelanggan : {}", mstPelanggan);
                    pelangganRepo.save(pMstPelanggan);
                    showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
                    RequestContext.getCurrentInstance().update("idList");
                    RequestContext.getCurrentInstance().update("growl");
                    init();
                    return;
                }
            }
//        mstPelanggan.setPelangganId(1);
            mstPelanggan.setCreatedAt(new Date());
            mstPelanggan.setStatus(MstPelanggan.Status.ACTIVE);
            log.debug("mstPelanggan : {}", mstPelanggan);
            pelangganRepo.save(mstPelanggan);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            Logger.getLogger(PelangganMBean.class.getName()).log(Level.SEVERE, null, ex);
            showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Terjadi kesalahan simpan data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void ubah() throws InterruptedException {
        try {
            if (kodePelanggan == null) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Untuk ubah, cari terlebih dahulu Kode Pelanggan");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            if (!kodePelanggan.equals(mstPelanggan.getKodePelanggan())) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Kode Pelanggan tidak dapat diganti");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            log.debug("mstPelanggan : {}", mstPelanggan);
            MstPelanggan pMstPelanggan = new MstPelanggan();
            pMstPelanggan = pelangganRepo.findTop1ByKodePelanggan(mstPelanggan.getKodePelanggan());
            if (pMstPelanggan != null && !kodePelanggan.equals(mstPelanggan.getKodePelanggan())) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Pelanggan dengan kode " + pMstPelanggan.getKodePelanggan() + " sudah tersedia");
                RequestContext.getCurrentInstance().update("growl");
                return;
            } else if (pMstPelanggan.equals(mstPelanggan)) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Tidak ada perubahan data, silahkan ubah data pelanggan");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            mstPelanggan.setUpdatedAt(new Date());
            mstPelanggan.setStatus(MstPelanggan.Status.ACTIVE);
            log.debug("mstPelanggan : {}", mstPelanggan);
            pelangganRepo.save(mstPelanggan);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil diubah");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            Logger.getLogger(PelangganMBean.class.getName()).log(Level.SEVERE, null, ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan ubah data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void hapus() throws InterruptedException {
        try {
            if (!kodePelanggan.equals(mstPelanggan.getKodePelanggan())) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Kode Pelanggan tidak dapat diganti");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            mstPelanggan.setUpdatedAt(new Date());
            mstPelanggan.setStatus(MstPelanggan.Status.INACTIVE);
            pelangganRepo.save(mstPelanggan);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            Logger.getLogger(PelangganMBean.class.getName()).log(Level.SEVERE, null, ex);
            showGrowl(FacesMessage.SEVERITY_INFO, "Peringatan", "Terjadi kesalahan hapus data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void tutup() throws IOException {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(PelangganMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
