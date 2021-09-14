package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

//@Entity
//@Table(name = "FORCHAN_POSTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForchanPost {

    @Id
    private Long id;
    private String text;
    private String url;
    private String mediaUrl;
}
