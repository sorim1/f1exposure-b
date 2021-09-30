package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
import java.io.File;
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
    private final static Integer width = 2000;
    private final static Integer height = 2000;

    @Override
    public Boolean executeArt() throws IOException {

        int drag=2;
        int diameter=1;
        List<Integer> iterations = Arrays.asList(1000, 2000, 500);
        for(int i =1000;i<10000;i=i+1000){
            BufferedImage bi = generateBufferedImage(drag, drag, i, diameter);
            BufferedImage bi2 = resize(bi, 1000, 1000);
            byte[] byteArray = toByteArray(bi2);
            FileUtils.writeByteArrayToFile(new File("E:\\temp\\art\\" + i +".png"), byteArray);
        }

        return true;
    }

    @Override
    public byte[] generateImage(Integer xDrag, Integer yDrag, Integer maxIteration, Integer diameter) throws IOException {
        BufferedImage bi = generateBufferedImage(xDrag, yDrag, maxIteration, diameter);
        BufferedImage bi2 = resize(bi, 1000, 1000);
        return toByteArray(bi2);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    BufferedImage generateBufferedImage(Integer xDrag, Integer yDrag, Integer maxIteration, Integer diameter) {
        int width = this.width;
        int height = this.height;
        BufferedImage bufferedImage =
                new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        generateDriverPixels(bufferedImage, xDrag, yDrag, maxIteration, diameter);
        return bufferedImage;
    }

    private void generateDriverPixels(BufferedImage bufferedImage, Integer xDrag, Integer yDrag, Integer maxIteration, Integer diameter) {
        RaceAnalysis analysis = ergastService.getLatestAnalyzedRace().getRaceAnalysis();
        List<Driver> driverData = sortDriverList(analysis.getDriverData());
        this.lapCount = getLapCount(driverData);
        this.ratio = this.lapCount/17 ;
        List<ArtDriver> artDrivers = initializeArtDrivers(driverData);
        setBackgroundColor(bufferedImage, artDrivers.get(artDrivers.size()-1).getColor());
        generateArtDriversByDriver(artDrivers, bufferedImage, xDrag, yDrag, maxIteration, diameter);
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

    private void generateArtDriversByDriver(List<ArtDriver> artDrivers, BufferedImage bufferedImage, Integer xDrag, Integer yDrag, Integer iterationMax, Integer diameterRatio) {
//        int xDrag = 2;
//        int yDrag = 2;
//        int iterationMax = 1000;
//        int diameterRatio = 1;
        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
        for(ArtDriver artDriver: artDrivers){
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(artDriver.getColor());
            for (int j = 0; j < artDriver.getLapByLapData().getTotalTimeByLapMs().size(); j++) {
                artDriver.setDiameter(diameterRatio*artDriver.getLapByLapData().getPositions().get(j));
                for (int i = 0; i < iterationMax; i++) {
                    int x = artDriver.getX();
                    int y = artDriver.getY();
                    int randomNumPrime = ThreadLocalRandom.current().nextInt(0, 28);
                    if(randomNumPrime<10){
                        randomNum = randomNumPrime;
                    }
                    //int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
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
                    if (x > this.width) {
                        x = this.width-1;
                    }
                    if (y < 0) {
                        y = 1;
                    }
                    if (y > this.height) {
                        y = this.height-1;
                    }
                    artDriver.setX(x);
                    artDriver.setY(y);
                    drawSingleArtDriver(artDriver, bufferedImage);
                }
            }
            log.info("DRIVER: {}", artDriver.getName());
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
                        if (x > this.width) {
                            x = this.width-1;
                        }
                        if (y < 0) {
                            y = 1;
                        }
                        if (y > this.height) {
                            y = this.height-1;
                        }
                        artDriver.setX(x);
                        artDriver.setY(y);
                        drawArtDrivers(artDrivers, bufferedImage);
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

    private void drawSingleArtDriver(ArtDriver artDriver, BufferedImage bufferedImage) {
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(artDriver.getColor());
            g.drawOval(artDriver.getX(), artDriver.getY(), artDriver.getDiameter(), artDriver.getDiameter());
    }

    private void fillSingleArtDriver(ArtDriver artDriver, BufferedImage bufferedImage) {
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setColor(artDriver.getColor());
        g.fillOval(artDriver.getX(), artDriver.getY(), artDriver.getDiameter(), artDriver.getDiameter());
    }

    private void drawArtDrivers(List<ArtDriver> artDrivers, BufferedImage bufferedImage) {
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
        for(Driver driver: driverData) {
            int x = ThreadLocalRandom.current().nextInt(0, this.width);
            int y = ThreadLocalRandom.current().nextInt(0, this.height);
           ArtDriver newDriver = new ArtDriver(driver, x, y);

            //TODO alternative team colors - mapa?
            if(colors.contains(newDriver.getColorCode())){
                newDriver.setColor(newDriver.getColor().brighter());
            }
            colors.add(newDriver.getColorCode());
            artDrivers.add(newDriver);
        }
        return artDrivers;
    }

    private void setBackgroundColor(BufferedImage bufferedImage, Color bgColor) {
    //    Color bgColor = Color.black;
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
