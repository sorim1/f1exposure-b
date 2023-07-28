package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArtDriver {
    public String name;
    public String initials;

    public String colorCode;
    public Color color;
    public Color teamColor;
    public List<Integer> conflictX = new ArrayList<>();
    public List<Integer> conflictY = new ArrayList<>();
    private Integer finalPosition;
    private Integer currentPosition;
    private Integer x;
    private Integer y;
    private Integer x1;
    private Integer y1;
    private Integer lapTime;
    private LapByLapData lapByLapData = new LapByLapData();
    private Integer diameter;

    public ArtDriver(Driver driver, int x, int y) {
        this.name = driver.getName();
        this.initials = driver.getInitials();
        this.colorCode = driver.getColor();
        this.color = Color.decode("#" + driver.getColor());
        this.teamColor = Color.decode("#" + driver.getColor());
        this.finalPosition = driver.getPosition();
        this.lapByLapData = driver.getLapByLapData();
        this.currentPosition = 20;
        this.x = x;
        this.y = y;
        this.x1 = x;
        this.y1 = y;
    }
}
