package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "APP_PROPERTIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppProperty {

    @Id
    private String name;
    private String value;
}
