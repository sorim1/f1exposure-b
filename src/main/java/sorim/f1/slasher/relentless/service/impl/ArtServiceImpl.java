package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.model.livetiming.ArtDriver;
import sorim.f1.slasher.relentless.model.livetiming.Driver;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.service.ArtService;
import sorim.f1.slasher.relentless.service.ErgastService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArtServiceImpl implements ArtService {

    private final ErgastService ergastService;
    private Integer lapCount = 0;
    private Integer ratio = 0;
    @Override
    public byte[] generateImage() throws IOException {
        BufferedImage bi = generateBufferedImage();
        return toByteArray(bi);
    }

    BufferedImage generateBufferedImage() {
        int width = 1000;
        int height = 1000;
        BufferedImage bufferedImage =
                new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        setBackgroundColor(bufferedImage);
        generateDriverPixels(bufferedImage);
        return bufferedImage;
    }

    private void generateDriverPixels(BufferedImage bufferedImage) {
        RaceAnalysis analysis = ergastService.getLatestAnalyzedRace().getRaceAnalysis();
        this.lapCount = getLapCount(analysis.getDriverData());
        this.ratio = this.lapCount/17 ;
        List<ArtDriver> artDrivers = initializeArtDrivers(analysis.getDriverData());
      //  drawArtDriversInitial(artDrivers, bufferedImage);
        generateArtDriversLapByLap(artDrivers, bufferedImage);
    }

    private void generateArtDriversLapByLap(List<ArtDriver> artDrivers, BufferedImage bufferedImage) {
        for(int i = 0; i<this.lapCount; i++){
            generateArtDriversOfLap(i, artDrivers, bufferedImage);
        }
    }

    private void generateArtDriversOfLap(Integer lap, List<ArtDriver> artDrivers, BufferedImage bufferedImage) {
         updateCoordinates(artDrivers, lap, bufferedImage);
    }

    private void updateCoordinates(List<ArtDriver> artDrivers, Integer lap, BufferedImage bufferedImage) {

        int iterationMax1 = 4;
        for(ArtDriver artDriver: artDrivers) {
            if (artDriver.getLapByLapData().getPositions().size() > lap) {
                artDriver.setCurrentPosition(artDriver.getLapByLapData().getPositions().get(lap));
                int iterationMax2 = 21-artDriver.getCurrentPosition();
                iterationMax2 = iterationMax2*10;
                Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
                g.setColor(artDriver.getColor());
                for (int j = 0; j < iterationMax2; j++) {
                    for (int i = 0; i < iterationMax1; i++) {
                        int x = artDriver.getX();
                        int y = artDriver.getY();
                        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
                        if (randomNum == 0) {
                            x += 2;
                        } else if (randomNum == 1) {
                            x += 2;
                            y += 2;
                        } else if (randomNum == 2) {
                            y += 2;
                        } else if (randomNum == 3) {
                            x -= 2;
                            y += 2;
                        } else if (randomNum == 4) {
                            x -= 2;
                        } else if (randomNum == 5) {
                            x -= 2;
                            y -= 2;
                        } else if (randomNum == 6) {
                            y -= 2;
                        } else if (randomNum == 7) {
                            x += 2;
                            y -= 2;
                        }
                        ;
                        if (x < 0) {
                            x = 1;
                        }
                        if (x > 1000) {
                            x = 999;
                        }
                        if (y < 0) {
                            y = 1;
                        }
                        if (y > 1000) {
                            y = 999;
                        }
                        artDriver.setX(x);
                        artDriver.setY(y);
                        drawArtDrivers(artDrivers, lap, bufferedImage);
                      }

                }
            }

        }
    }

    private Integer getLapCount(List<Driver> driverData) {
        Driver first = driverData.stream().filter(driver -> driver.getPosition() == 1).findFirst().get();
        log.info("prvi vozac je: {} - {}", first.getName(), first.getPosition());
        return first.getLapByLapData().getPositions().size();
    }

    private void drawArtDrivers(List<ArtDriver> artDrivers, Integer lap, BufferedImage bufferedImage) {
        for(ArtDriver artDriver: artDrivers) {
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(artDriver.getColor());
           // Integer diameter = lap - artDriver.getCurrentPosition();
            Integer diameter = lap/this.ratio;
            if(diameter<1){
                diameter = 1;
            }
           Integer diameter1 = 10;
            g.drawOval(artDriver.getX(), artDriver.getY(), diameter, diameter);
         //   g.drawRoundRect(artDriver.getX(), artDriver.getY(), diameter, diameter, 10, 10);

        }
    }

    private void drawArtDriversInitial(List<ArtDriver> artDrivers, BufferedImage bufferedImage) {
        for(ArtDriver artDriver: artDrivers) {
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(artDriver.getColor());
            g.fillRect(artDriver.getX()-100, artDriver.getY()-50, 200, 250);

        }
    }
    private List<ArtDriver> initializeArtDrivers(List<Driver> driverData) {
        List<ArtDriver> artDrivers= new ArrayList<>();
        List<String> colors= new ArrayList<>();
        int xCounter = 1;
        int yCounter = 1;
        for(Driver driver: driverData) {
            int x = (xCounter*200)-100;
            int y = (yCounter*250)-200;
            if(x>800){
                xCounter = 1;
                yCounter++;
            } else {
                xCounter++;
            }
            if(y>1000){
                yCounter = 1;
            }
           ArtDriver newDriver = new ArtDriver(driver, x, y);

            //TODO alternative team colors - mapa?
            if(colors.contains(newDriver.getColorCode())){
                newDriver.setColor(newDriver.getColor().darker());
            }
            colors.add(newDriver.getColorCode());
            artDrivers.add(newDriver);
        }
        return artDrivers;
    }

    private void setBackgroundColor(BufferedImage bufferedImage) {
        Color bgColor = Color.black;
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setColor(bgColor);
        g.fillRect ( 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight() );
    }

    static byte[] toByteArray(BufferedImage bi)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        return baos.toByteArray();

    }
}
