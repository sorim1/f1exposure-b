package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.entities.ArtImageRow;
import sorim.f1.slasher.relentless.entities.ImageRow;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.livetiming.ArtDriver;
import sorim.f1.slasher.relentless.model.livetiming.Driver;
import sorim.f1.slasher.relentless.model.livetiming.RaceAnalysis;
import sorim.f1.slasher.relentless.repository.ArtImageRepository;
import sorim.f1.slasher.relentless.repository.ImageRepository;
import sorim.f1.slasher.relentless.service.ArtService;
import sorim.f1.slasher.relentless.service.ErgastService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArtServiceImpl implements ArtService {

    private final ErgastService ergastService;
    private final ArtImageRepository artImageRepository;
    private final ImageRepository imageRepository;
    private final Integer width = 2000;
    private final Integer height = 2000;
    private Integer lapCount = 0;

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    static byte[] toByteArray(BufferedImage bi)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        return baos.toByteArray();

    }

    @Override
    public Boolean executeArt() throws IOException {
        log.info("executeArt called");
        int drag = 2;
        int diameter = 1;
        RaceAnalysis analysis = ergastService.getLatestAnalyzedRace().getRaceAnalysis();

        for (int i = 1400; i < 2000; i = i + 100) {
            BufferedImage bi = generateBufferedImage(analysis, drag, drag, i, diameter, 1);
            BufferedImage bi2 = resize(bi, 1000, 1000);
            byte[] byteArray = toByteArray(bi2);
            FileUtils.writeByteArrayToFile(new File("C:\\Users\\sorim\\Pictures\\f1-exposure\\ddm1-" + i + ".png"), byteArray);
        }

        for (int i = 1400; i < 2000; i = i + 100) {
            BufferedImage bi = generateBufferedImage(analysis, drag, drag, i, diameter, 2);
            BufferedImage bi2 = resize(bi, 1000, 1000);
            byte[] byteArray = toByteArray(bi2);
            FileUtils.writeByteArrayToFile(new File("C:\\Users\\sorim\\Pictures\\f1-exposure\\ddm2-" + i + ".png"), byteArray);
        }

        return true;
    }

    @Override
    public Boolean generateLatestArt() throws IOException {

        int drag = 2;
        int diameter = 1;
        int iterations = 1700;
        RaceData raceData = ergastService.getLatestAnalyzedRace();
        RaceAnalysis analysis = raceData.getRaceAnalysis();
        if (analysis.getArt() != null) {
            return false;
        }
        BufferedImage bi = generateBufferedImage(analysis, drag, drag, iterations, diameter, 2);
        BufferedImage bi2 = resize(bi, 1000, 1000);
        byte[] image = toByteArray(bi2);
        String code = UUID.randomUUID().toString();
        ArtImageRow imageRow = ArtImageRow.builder()
                .code(code)
                .image(image)
                .season(analysis.getYear())
                .round(raceData.getRound())
                .title(raceData.getCircuit().getCircuitName() + " " + analysis.getYear())
                .build();
        artImageRepository.save(imageRow);
        analysis.setArt(code);
        raceData.setRaceAnalysis(analysis);
        ergastService.saveRace(raceData);
        return true;
    }

    @Override
    public Boolean setLatestArt(String code) {
        RaceData raceData = ergastService.getLatestAnalyzedRace();
        RaceAnalysis analysis = raceData.getRaceAnalysis();
        analysis.setArt(code);
        raceData.setRaceAnalysis(analysis);
        ergastService.saveRace(raceData);
        return true;
    }
    @Override
    public Boolean deleteArt(String code) {
        artImageRepository.deleteByCode(code);
        return true;
    }


    @Override
    public Boolean generateLatestArtForced() throws IOException {

        int drag = 2;
        int diameter = 1;
        int iterations = 4000;
        RaceData raceData = ergastService.getLatestAnalyzedRace();
        RaceAnalysis analysis = raceData.getRaceAnalysis();
        if (analysis.getArt() != null) {
            artImageRepository.deleteByCode(analysis.getArt());
        }
        BufferedImage bi = generateBufferedImage(analysis, drag, drag, iterations, diameter, 3);
        BufferedImage bi2 = resize(bi, 1000, 1000);
        byte[] image = toByteArray(bi2);
        String code = UUID.randomUUID().toString();
        ArtImageRow imageRow = ArtImageRow.builder()
                .code(code)
                .image(image)
                .season(analysis.getYear())
                .round(raceData.getRound())
                .title(raceData.getCircuit().getCircuitName() + " " + analysis.getYear())
                .build();
        artImageRepository.save(imageRow);
        analysis.setArt(code);
        raceData.setRaceAnalysis(analysis);
        ergastService.saveRace(raceData);
        return true;
    }

    @Override
    public byte[] generateImage(Integer xDrag, Integer yDrag, Integer maxIteration, Integer diameter) throws IOException {
        RaceAnalysis analysis = ergastService.getLatestAnalyzedRace().getRaceAnalysis();
        BufferedImage bi = generateBufferedImage(analysis, xDrag, yDrag, maxIteration, diameter, 1);
        BufferedImage bi2 = resize(bi, 1000, 1000);
        return toByteArray(bi2);
    }

    BufferedImage generateBufferedImage(RaceAnalysis analysis, Integer xDrag, Integer yDrag, Integer maxIteration,
                                        Integer diameter, Integer mode) {
        int width = this.width;
        int height = this.height;
        BufferedImage bufferedImage =
                new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        generateDriverPixels(analysis, bufferedImage, xDrag, yDrag, maxIteration, diameter, mode);
        return bufferedImage;
    }

    private void generateDriverPixels(RaceAnalysis analysis, BufferedImage bufferedImage, Integer xDrag, Integer yDrag, Integer maxIteration, Integer diameter, Integer mode) {
        this.lapCount = getLapCount(analysis.getDriverData());
        List<ArtDriver> artDrivers = initializeArtDrivers(analysis.getDriverData(), mode);
        setBackgroundColor(bufferedImage, artDrivers.get(4).getColor());
        generateArtDriversByDriverModeOne(artDrivers, bufferedImage, xDrag, yDrag, maxIteration, mode);
    }

    private List<Driver> sortDriverList(List<Driver> driverData) {
        List<Driver> driverDataSorted = driverData.stream()
                .sorted(Comparator.comparing(Driver::getPosition))
                .collect(Collectors.toList());
        driverDataSorted.forEach(driver -> {
        });
        return driverDataSorted;
    }

    private List<ArtDriver> sortArtDriverList(List<ArtDriver> driverData) {
        List<ArtDriver> driverDataSorted = driverData.stream()
                .sorted(Comparator.comparing(ArtDriver::getFinalPosition).reversed())
                .collect(Collectors.toList());

        return driverDataSorted;
    }

    private List<ArtDriver> sortArtDriverListByDarkness(List<ArtDriver> driverData, Integer mode) {
        //mode = asc ili desc
        Integer mode1 = mode;
        if (mode >= 2) {
            mode1 = -1;
        }
        Integer finalMode = mode1;
        driverData.sort((o1, o2) -> {
            if ((o1.getTeamColor().getRed() + o1.getTeamColor().getGreen() + o1.getTeamColor().getBlue())
                    > (o2.getTeamColor().getRed() + o2.getTeamColor().getGreen() + o2.getTeamColor().getBlue())) {
                return finalMode;
            } else {
                return 0 - finalMode;
            }
        });
        return driverData;
    }

    private void setConflictPoints(List<ArtDriver> artDrivers) {
        for (int lap = 0; lap < this.lapCount; lap++) {
            Map<Integer, ArtDriver> driversMap = new HashMap<>();
            for (ArtDriver artDriver : artDrivers) {
                if (artDriver.getLapByLapData().getTotalTimeByLapMs().size() > lap) {
                    artDriver.setCurrentPosition(artDriver.getLapByLapData().getPositions().get(lap));
                    driversMap.put(artDriver.getCurrentPosition(), artDriver);
                }
            }
            for (ArtDriver artDriver : artDrivers) {
                Integer pos = artDriver.getCurrentPosition();
                int conflictX = -1;
                int conflictY = -1;
                if (lap < 10) {
                    artDriver.getConflictX().add(conflictX);
                    artDriver.getConflictY().add(conflictY);
                } else {
                    if (driversMap.containsKey(pos) && driversMap.containsKey(pos - 1)) {
                        int difference = driversMap.get(pos).getLapByLapData().getTotalTimeByLapMs().get(lap) - driversMap.get(pos - 1).getLapByLapData().getTotalTimeByLapMs().get(lap);
                        if (difference < 2000) {
                            conflictX = driversMap.get(pos - 1).getX();
                            conflictY = driversMap.get(pos - 1).getY();
                        }
                    }
                    artDriver.getConflictX().add(conflictX);
                    artDriver.getConflictY().add(conflictY);
                }
            }
        }
    }

    private void generateArtDriversByDriverModeOne(List<ArtDriver> artDrivers, BufferedImage bufferedImage, Integer xDrag, Integer yDrag, Integer iterationMax, Integer mode) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
        log.info("generateArtDriversByDriver: {}", iterationMax);
        for (int j = 0; j < lapCount; j++) {
            for (ArtDriver artDriver : artDrivers) {
                Integer diameter = Math.min(4, lapCount - j + 1);
                artDriver.setX(artDriver.getX1());
                artDriver.setY(artDriver.getY1());
                if (artDriver.getLapByLapData().getTotalTimeByLapMs().size() > j) {
                    int interKoef = 20 - artDriver.getFinalPosition();
                    artDriver.setDiameter(diameter);

                    for (int i = 0; i < iterationMax * interKoef; i++) {

                        int randomNumPrime = ThreadLocalRandom.current().nextInt(0, 28);
                        if (randomNumPrime < 10) {
                            randomNum = randomNumPrime;
                        }
                        int x = artDriver.getX();
                        int y = artDriver.getY();
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
                        if (x < 0) {
                            x = 1;
                        }
                        if (x > this.width) {
                            x = this.width - 1;
                        }
                        if (y < 0) {
                            y = 1;
                        }
                        if (y > this.height) {
                            y = this.height - 1;
                        }
                        artDriver.setX(x);
                        artDriver.setY(y);
                        drawSingleArtDriver(artDriver, bufferedImage);
                    }
                }
            }
            //      log.info(diameter1);
        }
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

    private List<ArtDriver> initializeArtDrivers(List<Driver> driverDataUnsorted, Integer mode) {
        List<Driver> driverData = sortDriverList(driverDataUnsorted);
        List<ArtDriver> artDrivers = new ArrayList<>();
        //List<String> colors = new ArrayList<>();
        Map<String, int[]> colors = new HashMap<>();
        for (Driver driver : driverData) {
            int x = ThreadLocalRandom.current().nextInt(0, this.width);
            int y = ThreadLocalRandom.current().nextInt(0, this.height);
            ArtDriver newDriver = new ArtDriver(driver, x, y);

            if (colors.containsKey(newDriver.getColorCode())) {
                newDriver.setColor(newDriver.getColor().darker());
                newDriver.setX1(colors.get(newDriver.getColorCode())[0]);
                newDriver.setX(colors.get(newDriver.getColorCode())[0]);
                newDriver.setY1(colors.get(newDriver.getColorCode())[1]);
                newDriver.setY(colors.get(newDriver.getColorCode())[1]);
            } else {
                colors.put(newDriver.getColorCode(), new int[]{x, y});
            }
            artDrivers.add(newDriver);
        }
        return sortArtDriverListByDarkness(artDrivers, mode);
    }

    private void setBackgroundColor(BufferedImage bufferedImage, Color bgColor) {
        //    Color bgColor = Color.black;
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setColor(bgColor);
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    @Override
    public Boolean updateArt(String code, byte[] image) {
        ArtImageRow row = artImageRepository.findFirstByCode(code);
        row.setImage(image);
        artImageRepository.save(row);
        return true;
    }

    @Override
    public Boolean saveImage(String code, byte[] image) {
        ImageRow imageRow = ImageRow.builder().code(code).image(image).build();
        imageRepository.save(imageRow);
        return true;
    }

    @Override
    public List<ArtImageRow> getAllArt() {
        return artImageRepository.findAllByOrderBySeasonDescRoundDesc();
    }

    @Override
    public ArtImageRow postArt(ArtImageRow body) {
        ArtImageRow row = artImageRepository.findFirstByCode(body.getCode());
        if (row == null) {
            artImageRepository.save(body);
            return body;
        } else {
            row.update(body);
            artImageRepository.save(row);
            return row;
        }
    }

    @Override
    public void restoreAllArt(List<ArtImageRow> artBackup) {
        artImageRepository.saveAll(artBackup);
    }
}
