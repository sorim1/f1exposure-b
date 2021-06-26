package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "CURRENT_DRIVER_STANDINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverStanding {

    private Integer position;
    private String name;
    @Id
    private String code;
    private String nationality;
    private String car;
    private Integer points;

    @Transient
    private String firstName;
    @Transient
    private String lastName;
}
