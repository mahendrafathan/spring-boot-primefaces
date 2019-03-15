/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.imi.web.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import static prosia.app.web.util.AbstractManagedBean.showGrowl;
import prosia.app.web.util.LazyDataModelFilterJPA;
import prosia.imi.model.Refkebangsaan;
import prosia.imi.repo.NegaraRepo;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
@Data
public class NegaraMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private NegaraRepo negaraRepo;
    private LazyDataModelFilterJPA<Refkebangsaan> listKebangsaan;
    private Refkebangsaan refKebangsaan;
    private String dalogHeader;

    @Override
    protected List<SecureItem> getSecureItems() {
        return null; 
    }
    
    public void init() {
        refKebangsaan = new Refkebangsaan();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listKebangsaan = new LazyDataModelFilterJPA(negaraRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                refKebangsaan.setKodenegara((String) filters.get("kodenegara"));
                refKebangsaan.setKebangsaan((String) filters.get("kebangsaan"));
                refKebangsaan.setRawan((Integer) filters.get("rawan"));
                return negaraRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                refKebangsaan.setKodenegara((String) filters.get("kodenegara"));
                refKebangsaan.setKebangsaan((String) filters.get("kebangsaan"));
                refKebangsaan.setRawan((Integer) filters.get("rawan"));
                return negaraRepo.count(whereQuery());
            }
        };
    }

    public Specification<Refkebangsaan> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<Refkebangsaan>() {
            @Override
            public Predicate toPredicate(Root<Refkebangsaan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(refKebangsaan.getKebangsaan())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kebangsaan")),
                            getLikePattern(refKebangsaan.getKebangsaan())));
                }
                if (StringUtils.isNotBlank(refKebangsaan.getKodenegara())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kodenegara")),
                            getLikePattern(refKebangsaan.getKodenegara())));
                }
                if (refKebangsaan.getRawan() != null) {
                    predicates.add(cb.equal(cb.lower(root.<String>get("rawan")),
                            refKebangsaan.getRawan()));
                }
                predicates.add(cb.equal(root.<Integer>get("status"), Refkebangsaan.Status.ACTIVE));
                query.orderBy(cb.asc(root.<BigDecimal>get("kebangsaanid")));
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
        Refkebangsaan pKebangsaan = (Refkebangsaan) getRequestParam("refKebangsaan");
        refKebangsaan = new Refkebangsaan();
        if (pKebangsaan == null) {
            dalogHeader = "Tambah Negara";
        } else {
            dalogHeader = "Ubah Negara";
            refKebangsaan = pKebangsaan;
        }
        RequestContext.getCurrentInstance().reset("idDialocAct");
        RequestContext.getCurrentInstance().update("idDialocAct");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').show()");
    }

    public void saveRecord() throws InterruptedException {
        refKebangsaan.setStatus(Refkebangsaan.Status.ACTIVE);
        negaraRepo.save(refKebangsaan);
        init();
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
    }

    public void deleteRecord() throws InterruptedException {
        Refkebangsaan pKebangsaan = (Refkebangsaan) getRequestParam("refKebangsaan");
        pKebangsaan.setStatus(Refkebangsaan.Status.INACTIVE);
        negaraRepo.save(pKebangsaan);
        init();
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
    }
}
