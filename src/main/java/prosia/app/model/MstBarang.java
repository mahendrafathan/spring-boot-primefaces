/**
 *
 */
package prosia.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Basic;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Data;

/**
 * @author Owner
 *
 */
@Entity
@Table(name = "mst_barang")
@Data
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class MstBarang {

    public enum Status {
        INACTIVE,
        ACTIVE
    }
    
    @Id
    @Basic(optional = false)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BARANG_SEQ")
//    @SequenceGenerator(sequenceName = "BARANG_SEQ", allocationSize = 1, name = "BARANG_SEQ")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "BARANG_ID", nullable = false)
    private Integer barangId;
    
    @Column(name = "kd_barang", length = 15, unique = true)
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String kodeBarang;

    @Column(name = "nama_barang", length = 25)
    private String namaBarang;

    @Column(name = "spesifikasi", length = 30)
    private String spesifikasi;

    @Column(name = "harga_beli", length = 5)
    private Float hargaBeli;

    @Column(name = "harga_jual", length = 5)
    private Float hargaJual;

    @Column(name = "stok", length = 5)
    private Integer stok;

    @Column(name = "keterangan", length = 20)
    private String keterangan;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
    
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private MstBarang.Status status = MstBarang.Status.ACTIVE;

}
