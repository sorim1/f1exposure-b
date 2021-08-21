package sorim.f1.slasher.relentless.handling;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.entities.Log;
import sorim.f1.slasher.relentless.repository.InstagramRepository;
import sorim.f1.slasher.relentless.repository.LogRepository;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Logger {

    private final LogRepository wiredRepository;

    private static LogRepository repository = null;
    private static String INFO = "INFO";

    @PostConstruct
    public void init() {
        this.repository = wiredRepository;
    }

    public static void raiseException(String info) throws Exception {
        log.error(info);
        repository.save(Log.builder().code("ERROR").message(info).build());
        throw new Exception(info);
    }

    public static void raiseException(String code, String message) throws Exception {
        log.error(message);
        repository.save(Log.builder().code(code).message(message).build());
        throw new Exception(message);
    }

    public static void log(String code, String message) {
        log.error(message);
        repository.save(Log.builder().code(code).message(message).build());
    }

    public static void log(String message) {
        log.info(message);
        repository.save(Log.builder().code(INFO).message(message).build());
    }
}
