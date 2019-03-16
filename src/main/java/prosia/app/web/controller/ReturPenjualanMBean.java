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
import prosia.app.model.Penjualan;
import prosia.app.model.ReturPenjualan;
import prosia.app.repo.BarangRepo;
import prosia.app.repo.PelangganRepo;
import prosia.app.repo.ReturPenjualanRepo;
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
public class ReturPenjualanMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private ReturPenjualanRepo returPenjualanRepo;
    private ReturPenjualan returPenjualan;
    private LazyDataModelFilterJPA<ReturPenjualan> listReturPenjualan;

    @Autowired
    private BarangRepo barangRepo;
    private List<MstBarang> listBarang;

    @Autowired
    private PelangganRepo pelangganRepo;
    private List<MstPelanggan> listPelanggan;

    public void init() {
        returPenjualan = new ReturPenjualan();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listBarang = barangRepo.findAll();
        listPelanggan = pelangganRepo.findAll();

        listReturPenjualan = new LazyDataModelFilterJPA(returPenjualanRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                return returPenjualanRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                return returPenjualanRepo.count(whereQuery());
            }

        };
    }

    public Specification<ReturPenjualan> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<ReturPenjualan>() {
            @Override
            public Predicate toPredicate(Root<ReturPenjualan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.orderBy(cb.asc(root.<BigDecimal>get("returPenjualanId")));
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
        returPenjualanRepo.save(returPenjualan);
        init();
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
    }

}
