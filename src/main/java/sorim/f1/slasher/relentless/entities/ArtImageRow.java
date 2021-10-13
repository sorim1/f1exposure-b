package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ART_IMAGES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtImageRow {

    @Id
    private String code;
    @JsonIgnore
    private byte[] image;
    private Integer season;
    private Integer round;
    private String title;
    private Integer status;
    private String contractAddress;
    private String tokenId;

    public void update(ArtImageRow newData) {
        this.season = newData.getSeason();
        this.round = newData.getRound();
        this.status = newData.getStatus();
        this.title = newData.getTitle();
        this.contractAddress = newData.getContractAddress();
        this.tokenId = newData.getTokenId();
    }
}
