package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArtDriver {
    public String name;
    public String initials;

    public String colorCode;
    public Color color;
    private Integer finalPosition;
    private Integer currentPosition;
    private Integer x;
    private Integer y;
    private Integer lapTime;
    private LapByLapData lapByLapData = new LapByLapData();
    private Integer conflictUp;
    private Integer upX;
    private Integer upY;
    private Integer conflictDown;
    private Integer downX;
    private Integer downY;
    private Integer diameter;

    public ArtDriver(Driver driver, int x, int y) {
        this.name = driver.getName();
        this.initials = driver.getInitials();
        this.colorCode = driver.getColor();
        this.color = Color.decode("#" + driver.getColor());
        this.finalPosition = driver.getPosition();
        this.lapByLapData = driver.getLapByLapData() ;
        this.currentPosition = 20;
        this.x = x;
        this.y = y;
    }
}
