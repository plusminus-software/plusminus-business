package software.plusminus.business.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.LastModifiedDate;
import software.plusminus.audit.annotation.Auditable;
import software.plusminus.generator.BusinessTypescriptGenerator;
import software.plusminus.generator.Generate;
import software.plusminus.json.model.Classable;
import software.plusminus.json.model.Jsog;
import software.plusminus.softdelete.annotation.SoftDelete;
import software.plusminus.tenant.annotation.Tenant;

import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@SuppressWarnings("checkstyle:ClassFanOutComplexity")
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenant", type = "string"))
@Filter(name = "tenantFilter", condition = "tenant = :tenant")
@FilterDef(name = "softDeleteFilter")
@Filter(name = "softDeleteFilter", condition = "deleted = false")
@Auditable
@Generate(BusinessTypescriptGenerator.class)
public abstract class BusinessEntity implements Classable, Jsog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, updatable = false)
    @Type(type = "uuid-char")
    private UUID uuid;

    /* Can be used for branching like spokusasnu:master, spokusasnu:draft */
    /* Can be used for partitioning like spokusasnu(partition-1), spokusasnu(partition-2) */
    @Tenant
    @Column(updatable = false)
    private String tenant;

    @SoftDelete
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Boolean deleted = Boolean.FALSE;

    @Version
    private Long version;

    @LastModifiedDate
    private ZonedDateTime modifiedTime;

}