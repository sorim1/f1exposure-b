package sorim.f1.slasher.relentless.entities;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Entity
@Table(name = "EXPOSED_VOTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = StringArrayType.class
        )
})
public class ExposedVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    @Type(type = "string-array")
    @Column(
            name = "drivers",
            columnDefinition = "text[]"
    )
    private String[] drivers;

    private String ipAddress;

    private Integer season;
    private Integer round;

}
