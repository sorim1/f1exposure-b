package sorim.f1.slasher.relentless.handling;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
public class ExceptionHandling {

    public static void raiseException(String info) throws Exception {
        log.error(info);
        throw new Exception(info);
    };

}
