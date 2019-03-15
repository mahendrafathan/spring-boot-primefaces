/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.imi.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
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
import prosia.app.web.model.SecureItem;
import prosia.app.web.util.AbstractManagedBean;
import static prosia.app.web.util.AbstractManagedBean.getRequestParam;
import static prosia.app.web.util.AbstractManagedBean.showGrowl;
import prosia.app.web.util.LazyDataModelFilterJPA;
import prosia.imi.model.MstBarang;
import prosia.imi.repo.BarangRepo;

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
    private String dalogHeader;

    @Override
    protected List<SecureItem> getSecureItems() {
        return null;
    }

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

    public void showDialogAction() {
        MstBarang pBarang = (MstBarang) getRequestParam("mstBarang");
        mstBarang = new MstBarang();
        if (pBarang == null) {
            dalogHeader = "Tambah Barang";
        } else {
            dalogHeader = "Ubah Barang";
            mstBarang = pBarang;
        }
        RequestContext.getCurrentInstance().reset("idDialocAct");
        RequestContext.getCurrentInstance().update("idDialocAct");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').show()");
    }

    public void cari() {
        log.debug("mstBarang : {}", mstBarang);
        mstBarang = barangRepo.findTop1ByKodeBarangAndStatus(mstBarang.getKodeBarang(), MstBarang.Status.ACTIVE);
    }

    public void tambah() throws InterruptedException {
        log.debug("tambah");
        MstBarang pMstBarang = new MstBarang();
        pMstBarang = barangRepo.findTop1ByKodeBarangAndStatus(mstBarang.getKodeBarang(), MstBarang.Status.ACTIVE);
        if (pMstBarang != null) {
            showGrowl(FacesMessage.SEVERITY_WARN, "Informasi", "Barang dengan kode " + pMstBarang.getKodeBarang() + " sudah tersedia");
            RequestContext.getCurrentInstance().update("growl");
            return;
        }
//        mstBarang.setBarangId(1);
        mstBarang.setCreatedAt(new Date());
        mstBarang.setUpdatedAt(new Date());
        mstBarang.setStatus(MstBarang.Status.ACTIVE);
        log.debug("mstBarang : {}", mstBarang);
        barangRepo.save(mstBarang);
        init();
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
    }

    public void ubah() throws InterruptedException {
        log.debug("mstBarang : {}", mstBarang);
        mstBarang = new MstBarang();
    }

    public void deleteRecord() throws InterruptedException {
        MstBarang pKebangsaan = (MstBarang) getRequestParam("mstBarang");
        pKebangsaan.setStatus(MstBarang.Status.INACTIVE);
        barangRepo.save(pKebangsaan);
        init();
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
    }
}
