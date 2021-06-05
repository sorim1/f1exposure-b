package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "SS_EVENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SportSurgeEvent {

    @Id
    private Integer id;
    private String name;

    @Column(name = "event_group")
    private Integer group;
    @OneToMany(mappedBy = "event")
    private Set<SportSurgeStream> streams;

}
