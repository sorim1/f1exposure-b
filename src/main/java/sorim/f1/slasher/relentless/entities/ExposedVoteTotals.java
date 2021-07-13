package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "EXPOSED_VOTE_TOTALS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExposedVoteTotals {

    @EmbeddedId
    private ExposedTotalsId id;
    private Integer voters;
    private Integer votes;


}
