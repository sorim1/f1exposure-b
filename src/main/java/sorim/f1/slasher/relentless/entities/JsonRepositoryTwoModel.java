package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Convert;
import org.hibernate.annotations.JdbcTypeCode;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "JSON_REPOSITORY_2023")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsonRepositoryTwoModel {

    @Id
    private String id;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Object json;

    private Integer status;
}
