package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.configuration.MainProperties;
import sorim.f1.slasher.relentless.entities.Log;
import sorim.f1.slasher.relentless.handling.Logger;
import sorim.f1.slasher.relentless.repository.BanlistRepository;
import sorim.f1.slasher.relentless.service.SecurityService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityServiceImpl implements SecurityService {

    private final static String AUTHORIZATION_CLIENT_FAILURE = "AUTHORIZATION_CLIENT_FAILURE";
    private final static String AUTHORIZATION_ADMIN_FAILURE = "AUTHORIZATION_ADMIN_FAILURE";
    private final static String AUTHORIZATION_ADMIN_OK = "AUTHORIZATION_ADMIN_OK";
    private final static String ART_CODE_WRONG = "ART_CODE_WRONG";
    private final MainProperties properties;
    private final BanlistRepository banlistRepository;

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
        String ipAddress = request.getRemoteAddr();
        return ipAddress;
//        Boolean banned = checkBanStatus(ipAddress);
//        if(banned) {
//            return null;
//        } else {
//            return ipAddress;
//        }

    }

    private Boolean checkBanStatus(String ipAddress) {
        return banlistRepository.existsBanByIp(ipAddress);
    }

    @Override
    public List<Log> getLogs(Integer mode, String filter) {
        return Logger.getLogs(mode, filter);
    }

    @Override
    public String validateCode(String code) throws Exception {
        String imageCode = code.replaceFirst("f1exposure.com_", "").replaceFirst(".png", "");
        if (code.equals(imageCode)) {
            Logger.raiseException(AUTHORIZATION_CLIENT_FAILURE, code);
        }
        return imageCode;
    }
}
