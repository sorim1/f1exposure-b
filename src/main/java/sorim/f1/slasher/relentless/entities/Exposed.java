package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String raceId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_code", referencedColumnName = "code")
    private Driver driver;

    private Integer counter;


}
