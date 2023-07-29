package sorim.f1.slasher.relentless.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JSON_REPOSITORY")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsonRepositoryModel {

    @Id
    private String id;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Object json;

    private Integer status;
}
