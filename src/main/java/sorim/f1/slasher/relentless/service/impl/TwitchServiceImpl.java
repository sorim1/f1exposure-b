package sorim.f1.slasher.relentless.service.impl;

import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.UserList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sorim.f1.slasher.relentless.service.TwitchService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TwitchServiceImpl implements TwitchService {


    private static final List<String> defaultTwitchStreamer = List.of("644967731", "mda2mjzy9y3lw");
    private static List<String> currentStreamer;
    private TwitchHelix twitchHelixclient;

    @Override
    public Boolean setStreamer(String username) {
        String userId = getUserId(username);
        if (userId != null && isUserOnline(userId)) {
            currentStreamer = new ArrayList<>();
            currentStreamer.add(userId);
            currentStreamer.add(username);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean checkCurrentStream() throws IOException {
        Boolean isOnline = isUserOnline(currentStreamer.get(0));
        if (!isOnline) {
            currentStreamer = topIracingStream();
        }
        return isOnline;
    }

    @Override
    public String getStreamer() {
        return currentStreamer.get(1);
    }

    private String getUserId(String username) {
        UserList userList = twitchHelixclient.getUsers(null, null, List.of(username)).execute();
        if (userList.getUsers().size() > 0) {
            log.info("getUserId: " + username + " - " + userList.getUsers().get(0).getId());
            return userList.getUsers().get(0).getId();
        }
        return null;
    }

    @PostConstruct
    public void onInit() {
        twitchHelixclient = TwitchHelixBuilder.builder()
                .withClientId("c0r4sbsc7uiz3r0g8z7q2sdyayyrlj")
                .withClientSecret("6gdm7tw99mt3kjxkta7xhvbxytw8de")
                .build();
        currentStreamer = topIracingStream();
    }

    private Boolean isUserOnline(String userId) {
        StreamList streamList = twitchHelixclient.getStreams(null, null, null, 1, null, null, List.of(userId), null).execute();
        return streamList.getStreams().size() > 0;
    }

    private List<String> topIracingStream() {
        try {
            List<String> streamer = new ArrayList<>();
            StreamList streamList = twitchHelixclient.getStreams(null, null, null, 1, List.of("19554"), null, null, null).execute();
            streamer.add(streamList.getStreams().get(0).getId());
            streamer.add(streamList.getStreams().get(0).getUserName());
            return streamer;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
