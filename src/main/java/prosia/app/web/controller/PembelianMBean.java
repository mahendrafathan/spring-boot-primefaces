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
import prosia.app.model.MstSupplier;
import prosia.app.model.Pembelian;
import prosia.app.repo.BarangRepo;
import prosia.app.repo.PembelianRepo;
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
public class PembelianMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private PembelianRepo pembelianRepo;
    private Pembelian pembelian;
    private LazyDataModelFilterJPA<Pembelian> listPembelian;

    @Autowired
    private BarangRepo barangRepo;
    private List<MstBarang> listBarang;

    @Autowired
    private SupplierRepo supplierRepo;
    private List<MstSupplier> listSupplier;

    public void init() {
        pembelian = new Pembelian();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listBarang = barangRepo.findAll();
        listSupplier = supplierRepo.findAll();

        listPembelian = new LazyDataModelFilterJPA(pembelianRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                return pembelianRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                return pembelianRepo.count(whereQuery());
            }

        };
    }

    public Specification<Pembelian> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<Pembelian>() {
            @Override
            public Predicate toPredicate(Root<Pembelian> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.orderBy(cb.asc(root.<BigDecimal>get("pembelianId")));
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
        pembelianRepo.save(pembelian);
        init();
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
    }

}
