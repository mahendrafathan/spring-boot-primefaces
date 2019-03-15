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

@Entity
@Table(name = "retur_penjualan")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class ReturPenjualan {

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
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RETUR_PENJUALAN_SEQ")
//    @SequenceGenerator(sequenceName = "RETUR_PENJUALAN_SEQ", allocationSize = 1, name = "RETUR_PENJUALAN_SEQ")

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RETUR_PENJUALAN_SEQ", nullable = false)
    private Integer returPenjualanId;

    @Column(name = "nota_jual", length = 10)
    private String notaJual;

    @Column(name = "tgl_retur")
    private Date tglRetur;

    @JoinColumn(name = "kd_barang", referencedColumnName = "kd_barang")
    @ManyToOne
    private MstBarang mstBarang;

    @Column(name = "nama_barang", length = 15)
    private String namaBarang;

    @JoinColumn(name = "kd_pelanggan", referencedColumnName = "kd_pelanggan")
    @ManyToOne
    private MstPelanggan kodePelanggan;

    @Column(name = "nama_pelanggan", length = 15)
    private String namaPelanggan;

    @Column(name = "jumlah", length = 15)
    private Integer jumlah;

    @Column(name = "keterangan", length = 15)
    private String keterangan;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
