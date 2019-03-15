/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.imi.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import prosia.app.model.AbstractAuditingEntity;

/**
 *
 * @author Ismail
 */
@Entity
@Table(name = "REFKEBANGSAAN", catalog = "", schema = "inventory_2")
@Data
public class Refkebangsaan extends AbstractAuditingEntity implements Serializable {
    
    public enum Status {
        INACTIVE,
        ACTIVE,
        CANCEL,
        REJECT,
        HOLD,
        DONE
    }
    

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @SequenceGenerator(sequenceName = "REFKEBANGSAAN#KEBANGSAANID", allocationSize = 1, name = "REFKEBANGSAAN_SEQ")
    @Column(name = "KEBANGSAANID", nullable = false, precision = 0, scale = -127)
    private Integer kebangsaanid;
    @Column(name = "KODENEGARA", length = 8)
    private String kodenegara;
    @Column(name = "KEBANGSAAN", length = 64)
    private String kebangsaan;
    @Column(name = "RAWAN")
    private Integer rawan;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Refkebangsaan.Status status = Refkebangsaan.Status.ACTIVE;
    @Column(name = "KETERANGAN", length = 256)
    private String keterangan;
    @Column(name = "NATIONALITY", length = 64)
    private String nationality;

}
