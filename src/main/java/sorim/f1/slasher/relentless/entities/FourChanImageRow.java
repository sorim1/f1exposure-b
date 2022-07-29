package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FOURCHAN_IMAGES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FourChanImageRow {

    @Id
    private Integer id;
    private byte[] image;
    private Integer status;
}
