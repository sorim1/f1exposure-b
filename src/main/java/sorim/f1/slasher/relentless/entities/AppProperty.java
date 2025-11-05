package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
