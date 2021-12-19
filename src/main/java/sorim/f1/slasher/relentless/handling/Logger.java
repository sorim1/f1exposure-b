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
import java.util.Date;
import java.util.List;

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
        repository.save(Log.builder().code("ERROR").message(info).created(new Date()).build());
        throw new Exception(info);
    }

    public static void raiseException(String code, String message) throws Exception {
        log.error(message);
        repository.save(Log.builder().code(code).message(message).created(new Date()).build());
        throw new Exception(message);
    }

    public static void log(String code, String message) {
        log.info(code + " - " + message);
        repository.save(Log.builder().code(code).message(message).created(new Date()).build());
    }

    public static void logAdmin(String message) {
        log.info(message);
        try {
            repository.save(Log.builder().code("ADMIN").message(message).created(new Date()).build());
        } catch(Exception e ){
            log.error(message);
            log.error(e.getMessage());
        }
    }

    public static void log(String message) {
        log.info(message);
//        repository.save(Log.builder().code(INFO).message(message).created(new Date())
//                .build());
    }

    public static List<Log> getLogs(Integer mode, String filter) {
        Date yesterday = new Date(System.currentTimeMillis()-24*60*60*1000);
        return repository.findAllByCreatedAfterOrderByIdDesc(yesterday);
    }
}
