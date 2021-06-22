package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrontendGraphDriverData {
    public String name;
    @JsonProperty("Initials")
    public String initials;
    @JsonProperty("FullName")
    public String fullName;
    @JsonProperty("FirstName")
    public String firstName;
    @JsonProperty("LastName")
    public String lastName;
    @JsonProperty("Color")
    public String color;
    @JsonProperty("Team")
    public String team;
    @JsonProperty("Num")
    public String num;

}
