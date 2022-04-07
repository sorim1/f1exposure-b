package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastDriver;

import javax.persistence.*;

@Entity
@Table(name = "DRIVERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    private String code;
    private String ergastCode;
    private String fullName;
    private Integer status;

    @Transient
    private boolean exposure;

    public Driver(ErgastDriver ed) {
        this.code = ed.getCode();
        this.ergastCode = ed.getDriverId();
        this.fullName = ed.getFamilyName();
        this.status=1;
    }
}
