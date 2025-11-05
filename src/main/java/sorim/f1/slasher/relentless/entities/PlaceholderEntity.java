package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MERCURY_TABLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceholderEntity {

    @Id
    private String id;
    private String text1;
    private String text2;
    private String text3;
    private Long number1;
    private Integer number2;
}
