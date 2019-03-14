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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Randy
 */
@Entity
@Table(name = "app_setting")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"prefixName"})
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Setting implements Serializable {

    public static final String X_AUDIT_TRAIL_LOG_SEPARATOR = "audit_trail_log_separator";
    public static final String X_DATE_FORMAT = "date_format";
    public static final String X_DATE_TIME_FORMAT = "date_time_format";
    public static final String X_DECIMAL_FORMAT = "decimal_format";
    public static final String X_DECIMAL_MIN_VALUE = "decimal_min_value";
    public static final String X_FILE_UPLOAD_SIZE_LIMIT = "file_upload_size_limit";
    public static final String X_FILE_UPLOAD_TYPE = "file_upload_type";
    public static final String X_INTEGER_FORMAT = "integer_format";
    public static final String X_INTEGER_MIN_VALUE = "integer_min_value";
    public static final String X_LOCATION_SEPARATOR = "location_separator";
    public static final String X_MULTI_TENANCY = "multi_tenancy";
    public static final String X_MULTI_ROLES = "multi_roles";
    public static final String X_PAGINATOR_LOCATION = "paginator_location";
    public static final String X_ROWS_PER_PAGE_DEFAULT = "rows_per_page_default";
    public static final String X_ROWS_PER_PAGE_TEMPLATE = "rows_per_page_template";

    public static final String X_THRESHOLD_PHOTO = "minimum_threshold_photo";
    public static final String X_THRESHOLD_FINGER_PRINT = "minimum_threshold_finger_print";

    public static final String X_THRESHOLD_FACE_IDENTIC = "score_face_identic";
    public static final String X_THRESHOLD_FACE_NOT_IDENTIC = "score_face_not_identic";
    public static final String X_THRESHOLD_FP_IDENTIC = "score_fp_identic";
    public static final String X_THRESHOLD_FP_ALMOST_IDENTIC = "score_fp_almost_identic";
    public static final String X_THRESHOLD_FP_NOT_IDENTIC = "score_fp_not_identic";

    public static final String X_THRESHOLD_ECS_MIN_SCORE = "ecs_min_score";
    public static final String X_THRESHOLD_ECS_MAX_RESULT = "ecs_max_result";

    public static final String X_OFFICE_TYPE_BY_NAME = "office_type_by_name";
    public static final String X_OFFICE_IP_KANWIL = "office_ip_kanwil";

    @Id
    @Basic(optional = false)
    @Column(name = "prefix_name", length = 50)
    private String prefixName;

    @Size(max = 100)
    @Column(name = "value_", length = 100)
    private String value;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;
}
