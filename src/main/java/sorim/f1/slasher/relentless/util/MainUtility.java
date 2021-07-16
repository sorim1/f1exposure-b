package sorim.f1.slasher.relentless.util;

import lombok.extern.slf4j.Slf4j;
import sorim.f1.slasher.relentless.model.livetiming.Driver;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class MainUtility {

    public static Map<String, String> driverMap = new HashMap<>();
    public static Map<String, String> colorMap = new HashMap<>() {{
        put("mercedes", "#00D2BE");
        put("aston_martin", "#006F62");
        put("williams", "#005AFF");
        put("ferrari", "#DC0004");
        put("haas", "#FFFFFF");
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


}
