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
import lombok.Data;

/**
 * @author Owner
 *
 */
@Entity
@Table(name = "pembelian")
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Pembelian {

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEMBELIAN_ID", nullable = false)
    private Integer pembelianId;

    @Column(name = "nota_beli", length = 10)
    private String notaBeli;

    @Column(name = "tgl_beli")
    private Date tglBeli;

    @JoinColumn(name = "BARANG_ID", referencedColumnName = "BARANG_ID")
    @ManyToOne
    private MstBarang kodeBarang;

    @Column(name = "nama_barang", length = 15)
    private String namaBarang;

    @JoinColumn(name = "SUPPLIER_ID", referencedColumnName = "SUPPLIER_ID")
    @ManyToOne
    private MstSupplier kodeSupplier;

    @Column(name = "nama_supplier", length = 15)
    private String namaSupplier;

    @Column(name = "harga_satuan", length = 15)
    private Float hargaSatuan;

    @Column(name = "jumlah_beli", length = 15)
    private Integer jumlahBeli;

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
