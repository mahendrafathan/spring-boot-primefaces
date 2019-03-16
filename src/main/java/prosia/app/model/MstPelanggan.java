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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Owner
 *
 */
@Entity
@Table(name = "mst_pelanggan")
@Data
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class MstPelanggan {

    public enum Status {
        INACTIVE,
        ACTIVE
    }

    @Id
    @Basic(optional = false)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PELANGGAN_SEQ")
//    @SequenceGenerator(sequenceName = "PELANGGAN_SEQ", allocationSize = 1, name = "PELANGGAN_SEQ")

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PELANGGAN_ID", nullable = false)
    private Integer pelangganId;

    @Column(name = "kd_pelanggan", length = 15, unique = true)
    private String kodePelanggan;

    @Column(name = "nama_pelanggan", length = 30)
    private String namaPelanggan;

    @Column(name = "alamat", length = 50)
    private String alamat;

    @Column(name = "kota", length = 30)
    private String kota;

    @Column(name = "telepon", length = 15)
    private Integer telepon;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
    
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MstPelanggan.Status status = MstPelanggan.Status.ACTIVE;
}
