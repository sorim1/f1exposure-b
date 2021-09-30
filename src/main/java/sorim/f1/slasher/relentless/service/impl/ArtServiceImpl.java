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
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
        List<Driver> driverData = sortDriverList(analysis.getDriverData());
        this.lapCount = getLapCount(driverData);
        this.ratio = this.lapCount/17 ;
        List<ArtDriver> artDrivers = initializeArtDrivers(driverData);
      //  drawArtDriversInitial(artDrivers, bufferedImage);
        generateArtDriversLapByLap(artDrivers, bufferedImage);
    }

    private List<Driver> sortDriverList(List<Driver> driverData) {

        List<Driver> driverDataSorted = driverData.stream()
                .sorted(Comparator.comparing(Driver::getPosition).reversed())
                .collect(Collectors.toList());
        driverDataSorted.forEach(driver->{
        });
        return driverDataSorted;
    }

    private void generateArtDriversLapByLap(List<ArtDriver> artDrivers, BufferedImage bufferedImage) {
        for(int i = 0; i<this.lapCount; i++){
            generateArtDriversOfLap(i, artDrivers, bufferedImage);
            log.info("LAP: {}", i);
        }
    }

    private void generateArtDriversOfLap(Integer lap, List<ArtDriver> artDrivers, BufferedImage bufferedImage) {
        updateDriverData(artDrivers, lap);
        drawDriverData(artDrivers, lap, bufferedImage);
    }

    private void updateDriverData(List<ArtDriver> artDrivers, Integer lap) {
        Map<Integer,ArtDriver> driversMap = new HashMap<>();
        for(ArtDriver artDriver: artDrivers) {
            if (artDriver.getLapByLapData().getTotalTimeByLapMs().size() > lap) {
                artDriver.setCurrentPosition(artDriver.getLapByLapData().getPositions().get(lap));
                driversMap.put(artDriver.getCurrentPosition(), artDriver);
            }
        }
        for(ArtDriver artDriver: artDrivers) {
            Integer pos = artDriver.getCurrentPosition();
            int conflictUp = 0;
            int conflictDown = 0 ;
            Integer upX = null;
            Integer upY = null ;
            Integer downX = null;
            Integer downY = null ;
            if(driversMap.containsKey(pos) && driversMap.containsKey(pos-1)){
              int difference =  driversMap.get(pos).getLapByLapData().getTotalTimeByLapMs().get(lap) - driversMap.get(pos-1).getLapByLapData().getTotalTimeByLapMs().get(lap);
              if(difference<1000){
                    conflictUp = 2;
                    upX = driversMap.get(pos-1).getX();
                    upY = driversMap.get(pos-1).getY();
                } else if(difference<2000){
                    conflictUp = 1;
                    upX = driversMap.get(pos-1).getX();
                    upY = driversMap.get(pos-1).getY();
                }
            }

            if(driversMap.containsKey(pos) && driversMap.containsKey(pos+1)){
                int difference =  driversMap.get(pos+1).getLapByLapData().getTotalTimeByLapMs().get(lap) - driversMap.get(pos).getLapByLapData().getTotalTimeByLapMs().get(lap);
                if(difference<1000){
                    conflictDown = 2;
                    downX = driversMap.get(pos+1).getX();
                    downY = driversMap.get(pos+1).getY();
                } else if(difference<2000){
                    conflictDown = 1;
                    downX = driversMap.get(pos+1).getX();
                    downY = driversMap.get(pos+1).getY();
                }
            }
            artDriver.setConflictUp(conflictUp);
            artDriver.setConflictDown(conflictDown);
            artDriver.setUpX(upX);
            artDriver.setUpY(upY);
            artDriver.setDownX(downX);
            artDriver.setDownY(downY);
            artDriver.setDiameter(lap+1/this.ratio);
            if(artDriver.getDiameter()<2){
                artDriver.setDiameter(2);
            }
            if(artDriver.getDiameter()>40){
                artDriver.setDiameter(40);
            }

        }
    }
    private void drawDriverData(List<ArtDriver> artDrivers, Integer lap, BufferedImage bufferedImage) {

        int iterationMax1 = 14;
        for(ArtDriver artDriver: artDrivers) {
            if (artDriver.getLapByLapData().getPositions().size() > lap) {
                int iterationMax2 = 21-artDriver.getCurrentPosition();
               // iterationMax2 = iterationMax2*10;

                int xDrag = calculateXDrag(artDriver);
                int yDrag = calculateYDrag(artDriver);
                Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
                g.setColor(artDriver.getColor());
                for (int j = 0; j < iterationMax2; j++) {
                    for (int i = 0; i < iterationMax1; i++) {
                        int x = artDriver.getX();
                        int y = artDriver.getY();
                        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
                        if (randomNum == 0) {
                            x += xDrag;
                        } else if (randomNum == 1) {
                            x += xDrag;
                            y += yDrag;
                        } else if (randomNum == 2) {
                            y += yDrag;
                        } else if (randomNum == 3) {
                            x -= xDrag;
                            y += yDrag;
                        } else if (randomNum == 4) {
                            x -= xDrag;
                        } else if (randomNum == 5) {
                            x -= xDrag;
                            y -= yDrag;
                        } else if (randomNum == 6) {
                            y -= yDrag;
                        } else if (randomNum == 7) {
                            x += xDrag;
                            y -= yDrag;
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

    private int calculateXDrag(ArtDriver artDriver) {
        int xDifference = 2;
        if(artDriver.getConflictUp()>0){
            xDifference = artDriver.getUpX() - artDriver.getX() ;
            if(Math.abs(xDifference)<artDriver.getDiameter()){
                xDifference = artDriver.getX() - artDriver.getUpX();
            }
            xDifference = xDifference/artDriver.getDiameter();
        }
//        if(artDriver.getConflictDown()>0){
//            xDifference = artDriver.getDownX() - artDriver.getX() ;
//            if(Math.abs(xDifference)<artDriver.getDiameter()){
//                xDifference = artDriver.getX() - artDriver.getDownX();
//            }
//            xDifference = xDifference/artDriver.getDiameter();
//        }
        if(xDifference>10){
            xDifference=10;
        }
        if(xDifference<-10){
            xDifference=-10;
        }
        return xDifference;
    }

    private int calculateYDrag(ArtDriver artDriver) {
        int yDifference = 2;
        if(artDriver.getConflictUp()>0){
            yDifference = artDriver.getUpY() - artDriver.getY() ;
            if(Math.abs(yDifference)<artDriver.getDiameter()){
                yDifference = artDriver.getY() - artDriver.getUpY();
            }
            yDifference = yDifference/artDriver.getDiameter();
        }
//        else if(artDriver.getConflictDown()>0){
//            yDifference = artDriver.getDownY() - artDriver.getY() ;
//            if(Math.abs(yDifference)<artDriver.getDiameter()){
//                yDifference = artDriver.getY() - artDriver.getDownY();
//            }
//            yDifference = yDifference/artDriver.getDiameter();
//        }
        if(yDifference>10){
            yDifference=10;
        }
        if(yDifference<-10){
            yDifference=-10;
        }
        return yDifference;
    }

    private Integer getLapCount(List<Driver> driverData) {
        Driver first = driverData.stream().filter(driver -> driver.getPosition() == 1).findFirst().get();
        return first.getLapByLapData().getPositions().size();
    }

    private void drawArtDrivers(List<ArtDriver> artDrivers, Integer lap, BufferedImage bufferedImage) {
        for(ArtDriver artDriver: artDrivers) {
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(artDriver.getColor());
            g.fillOval(artDriver.getX(), artDriver.getY(), artDriver.getDiameter(), artDriver.getDiameter());
        }
//        for(int i = 0; i<3;i++) {
//            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
//            g.setColor(artDrivers.get(i).getColor());
//            g.fillOval(artDrivers.get(i).getX(), artDrivers.get(i).getY(), artDrivers.get(i).getDiameter(), artDrivers.get(i).getDiameter());
//        }
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
