/**
 *
 */
package prosia.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Basic;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;

/**
 * @author Owner
 *
 */
@Entity
@Table(name = "mst_supplier")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class MstSupplier {

    public enum Status {
        INACTIVE,
        ACTIVE
    }
    
    @Id
    @Basic(optional = false)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLIER_SEQ")
//    @SequenceGenerator(sequenceName = "SUPPLIER_SEQ", allocationSize = 1, name = "SUPPLIER_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUPPLIER_ID", nullable = false)
    private Integer supplierId;

    @Column(name = "kd_supplier", length = 5)
    private String kodeSupplier;

    @Column(name = "nama_supplier", length = 20)
    private String namaSupplier;

    @Column(name = "email", length = 20)
    private String email;

    @Column(name = "telepon", length = 15)
    private Integer telepon;

    @Column(name = "kota", length = 20)
    private String kota;

    @Column(name = "alamat", length = 50)
    private String alamat;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
    
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MstSupplier.Status status = MstSupplier.Status.ACTIVE;
}