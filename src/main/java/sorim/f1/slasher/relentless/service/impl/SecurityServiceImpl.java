package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.Log;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.service.SecurityService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityServiceImpl implements SecurityService {

    private final MainProperties properties;
    private final static String AUTHORIZATION_CLIENT_FAILURE = "AUTHORIZATION_CLIENT_FAILURE";
    private final static String AUTHORIZATION_ADMIN_FAILURE = "AUTHORIZATION_ADMIN_FAILURE";
    private final static String AUTHORIZATION_ADMIN_OK = "AUTHORIZATION_ADMIN_OK";

    @Override
    public void validateHeader(String authorization) throws Exception {
        boolean valid = Arrays.asList(properties.getClients()).contains(authorization) || Arrays.asList(properties.getAdmins()).contains(authorization);
        if (!valid) {
            Logger.raiseException(AUTHORIZATION_CLIENT_FAILURE, authorization);
        }
    }

    @Override
    public void validateAdminHeader(String authorization) throws Exception {
        boolean valid = Arrays.asList(properties.getAdmins()).contains(authorization);
        if (!valid) {
            Logger.raiseException(AUTHORIZATION_ADMIN_FAILURE, authorization);
        } else {
            Logger.log(AUTHORIZATION_ADMIN_OK, authorization);
        }
    }

    @Override
    public String validateIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    @Override
    public List<Log> getLogs(Integer mode, String filter) {
        return Logger.getLogs(mode, filter);
    }
}
