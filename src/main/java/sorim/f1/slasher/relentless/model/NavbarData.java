package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NavbarData {
    private String sessionName = "";
    private String winner;
    private String radioUrl;
    private Integer tabNumber;
}
