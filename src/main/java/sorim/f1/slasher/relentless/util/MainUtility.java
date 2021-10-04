package sorim.f1.slasher.relentless.util;

import lombok.extern.slf4j.Slf4j;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.livetiming.Driver;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@Slf4j
public class MainUtility {

    public static Map<String, String> driverMap = new HashMap<>();
    public static Map<String, String> colorMap = new HashMap<>() {{
        put("mercedes", "#00D2BE");
        put("aston_martin", "#006F62");
        put("williams", "#005AFF");
        put("ferrari", "#DC0004");
        put("haas", "#888888");
        put("alfa", "#900000");
        put("mclaren", "#FF9800");
        put("alphatauri", "#2B4562");
        put("red_bull", "#0600EF");
        put("alpine", "#0090FF");
    }};

    public static List<String> extractDriverCodes(Set<String> keySet) {
        List<String> response = new ArrayList<>();
        List<String> list = new ArrayList<>(keySet);
        list.forEach(code -> {
            response.add(code.substring(1));
        });
        return response;
    }

    public static List<String> extractDriverCodesOrdered(List<Driver> drivers) {
        driverMap = new HashMap<>();
        List<String> response = new ArrayList<>();
        drivers.forEach(driver -> {
            response.add(driver.initials);
            driverMap.put(driver.initials, driver.fullName);
        });
        return response;
    }

    public static List<String> extractDriverCodes(List<Driver> drivers) {
        List<String> response = new ArrayList<>();
        drivers.forEach(driver -> {
            response.add(driver.initials);
        });
        return response;
    }

    public static Map<Integer, String> connectDriverCodesWithNumber(List<Driver> drivers) {
        Map<Integer, String> response = new HashMap<>();
        drivers.forEach(driver -> {
            response.put(Integer.valueOf(driver.num), driver.initials);
        });
        return response;
    }

    public static String getDriverNameFromCode(String code) {
        return driverMap.get(code);
    }

    public static List<Integer> orderDriverCodes(List<String> driverCodes, List<String> orderedDriverCodes) {
        List<Integer> response = new ArrayList<>();
        for(int i=0; i<driverCodes.size(); i++){
            int index = orderedDriverCodes.indexOf(driverCodes.get(i));
            response.add(index);
        }
        return response;
    }

    public static List<Integer> extractDataFields(Map<String, Object> dataFields, List<Integer> order) {
        Integer size = 20;
        if(order.size()>size){
            size=order.size();
        }
        Integer[] output= new Integer[size];
        AtomicReference<Integer> counter = new AtomicReference<>(0);
        dataFields.values().forEach(row -> {
            List<Integer> list = (List<Integer>) row;
            log.info(String.valueOf(output.length));
            log.info(String.valueOf(order.get(counter.get())));
            output[order.get(counter.get())]=list.get(1);
            counter.set(counter.get() + 1);
        });
        return Arrays.asList(output);
    }

    public static String getTeamColor(String team) {
        return colorMap.get(team);
    }


    public static String getHashCode(String string) {
        return String.valueOf(string.hashCode());
    }

    public static String handleUsername(String username) {
        String finalUsername = username;
        Integer passwordStart = username.indexOf("#");
        if(passwordStart>=0){
            String password = String.valueOf(Math.abs(username.hashCode()));
            if(password.length()>6){
                password = password.substring(0,6);
            }
            if(passwordStart>20){
                finalUsername = username.substring(0,20) +"#"+ password;
            } else {
                finalUsername = username.substring(0,passwordStart+1) + password;
            }
        }

        return finalUsername;
    }

    public static String subtractDays(String date, Integer days) {
        LocalDate localDate = LocalDate.parse(date);
        return localDate.minusDays(days).toString();
    }

    public static Integer lapTimeToMiliseconds(String time) {
        String[] lapTime = time.split(":");
        int miliseconds = Integer.parseInt(lapTime[0])*60000;
        String[] lapTime2 = lapTime[1].split(Pattern.quote("."));
        miliseconds += Integer.parseInt(lapTime2[0])*1000;
        miliseconds += Integer.parseInt(lapTime2[1]);
        return miliseconds;
    }

    public static int getWeekDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        log.info("weekDay1");
        log.info(String.valueOf(Calendar.DAY_OF_WEEK));
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static void logTime(String code, Integer delayInMiliseconds) {
        long nextTick = System.currentTimeMillis()+delayInMiliseconds;
        Instant instant = Instant.ofEpochMilli(nextTick);
        LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        Logger.log(code, "NEXT TICK: " + date);
    }

    public static String generateCodeFromTitle(String title) {
       String uuid =  UUID.randomUUID().toString();
        String code = title.replaceAll(
                "[^a-zA-Z0-9 ]", "").replaceAll(" ", "-");
        code = code.substring(0, Math.min(40, code.length()))+"-"+uuid.substring(0,5);
        return code;
    }

    public static String generateCodeFromTitleAndId(String title, String id) {
        String hashId =  String.valueOf(id.hashCode()).substring(0,5);
        String code = title.replaceAll(
                "[^a-zA-Z0-9 ]", "").replaceAll(" ", "-");
        code = code.substring(0, Math.min(40, code.length()))+hashId;
        return code;
    }

    public static String getDomain(String fullUrl) {
        int start = 0;
        String domainUrl = "";
        if(fullUrl.indexOf("//")>0){
            start =fullUrl.indexOf("//")+2;
        }
        String prefix = fullUrl.substring(0, start);
        domainUrl = fullUrl.substring(start);
        if(domainUrl.indexOf("/")>0){
            domainUrl = domainUrl.substring(0, domainUrl.indexOf("/"));
        }
        domainUrl = prefix + domainUrl;
        log.info("domainUrl: "+ domainUrl);
        return domainUrl;
    }

}
