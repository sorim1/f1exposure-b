package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Driver {
    @JsonProperty("Name")
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
