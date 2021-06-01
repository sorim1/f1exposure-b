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
@Table(name = "EXPOSED")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exposed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    @Column(name="race_id")
    private Integer raceId;

    @Column(name="driver_id")
    private Integer driverId;

    private Integer counter;


}
