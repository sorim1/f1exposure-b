package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "IMAGES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageRow {

    @Id
    private String code;
    private byte[] image;
    private String type;

}
