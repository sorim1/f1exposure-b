package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CURRENT_CONSTRUCTOR_STANDINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstructorStanding {

    @Id
    private Integer id;
    private Integer position;
    private String name;
    private Integer points;
}
