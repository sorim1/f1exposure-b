package sorim.f1.slasher.relentless.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "JSON_REPOSITORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsonRepositoryModel {

    @Id
    private String id;
    @Type(value = JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Object json;

    private Integer status;
}
