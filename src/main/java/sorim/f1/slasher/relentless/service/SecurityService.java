package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SecurityService {

    void validateHeader(String authorization) throws Exception;

    void validateAdminHeader(String authorization) throws Exception;

    String validateIp(HttpServletRequest request);

    List<Log> getLogs(Integer mode, String filter);

}
