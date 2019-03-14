/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Randy
 */
@Entity
@Table(name = "app_enumeration")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"enumId"})
@ToString
public class Enumeration extends AbstractAuditingEntity implements Serializable {

    public enum EnumerationType {
        APP_ENTITY,
        DATA_STATUS,
        TIPE_KANTOR,
        REGISTRASI_STATUS
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "enum_id")
    private Integer enumId;
    
    @Column(name = "type_", length = 50)
    @Enumerated(EnumType.STRING)
    private EnumerationType type;
    
    @Column(name = "enum_name", length = 200)
    private String enumName;
    
    @Column(name = "enum_name_en", length = 200)
    private String enumNameEn;
    
    @Column(name = "enabled")
    private Boolean enabled = true;
    
    @Column(name = "sequence_")
    private Short sequence;

    public Enumeration() {
    }

    public Enumeration(EnumerationType type, String enumName, String enumNameEn) {
        this.type = type;
        this.enumName = enumName;
        this.enumNameEn = enumNameEn;
    }
    
}
