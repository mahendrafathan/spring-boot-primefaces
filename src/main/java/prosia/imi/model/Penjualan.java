/**
 *
 */
package prosia.imi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Basic;
import javax.persistence.SequenceGenerator;

/**
 * @author Owner
 *
 */
@Entity
@Table(name = "penjualan")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Penjualan {

    public enum Status {
        INACTIVE,
        ACTIVE,
        CANCEL,
        REJECT,
        HOLD,
        DONE
    }
    
    @Id
    @Basic(optional = false)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PENJUALAN_SEQ")
//    @SequenceGenerator(sequenceName = "PENJUALAN_SEQ", allocationSize = 1, name = "PENJUALAN_SEQ")

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PENJUALAN_ID", nullable = false)
    private Integer penjualanId;

    @Column(name = "no_faktur", length = 10)
    private String noFaktur;

    @Column(name = "nota_jual", length = 10)
    private String notaJual;

    @Column(name = "tgl_jual")
    private Date tglJual;

    @JoinColumn(name = "kd_barang", referencedColumnName = "kd_barang")
    @ManyToOne
    private MstBarang kodeBarang;

    @Column(name = "nama_barang", length = 15)
    private String namaBarang;

    @JoinColumn(name = "kd_pelanggan", referencedColumnName = "kd_pelanggan")
    @ManyToOne
    private MstPelanggan kodePelanggan;

    @Column(name = "nama_pelanggan", length = 15)
    private String namaPelanggan;

    @Column(name = "jumlah_jual", length = 15)
    private Integer jumlahJual;

    @Column(name = "harga_satuan", length = 15)
    private Float hargaSatuan;

    @Column(name = "stok", length = 15)
    private Integer stok;

    @Column(name = "total_harga", length = 15)
    private Float totalHarga;

    @Column(name = "diskon", length = 5)
    private Float diskon;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

}
