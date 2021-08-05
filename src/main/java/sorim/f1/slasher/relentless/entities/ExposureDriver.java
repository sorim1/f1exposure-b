package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "DRIVERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExposureDriver {

    @Id
    private String code;
    private String ergastCode;
    private String fullName;
    @Transient
    private boolean exposure;
}
