package sorim.f1.slasher.relentless.service.impl;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.model.*;
import sorim.f1.slasher.relentless.repository.*;
import sorim.f1.slasher.relentless.service.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

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
}
