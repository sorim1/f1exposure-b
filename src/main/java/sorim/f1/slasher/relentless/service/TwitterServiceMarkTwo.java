//package sorim.f1.slasher.relentless.service;
//
//// Import classes:
//import com.twitter.clientlib.*;
//import com.twitter.clientlib.auth.*;
//import com.twitter.clientlib.model.*;
//import com.twitter.clientlib.api.TwitterApi;
//
//import com.twitter.clientlib.api.TweetsApi;
//import java.io.InputStream;
//import com.google.common.reflect.TypeToken;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import sorim.f1.slasher.relentless.configuration.MainProperties;
//
//import javax.annotation.PostConstruct;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.lang.reflect.Type;
//import java.util.List;
//import java.util.Set;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.time.OffsetDateTime;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
//public class TwitterServiceMarkTwo {
//    private final MainProperties properties;
//    private static TwitterApi apiInstance;
//    public Boolean fetchTwitterPosts(){
//        try {
//            Integer backfillMinutes = 56; // Integer | The number of minutes of backfill requested.
//            Integer partition = 56; // Integer | The partition number.
//            apiInstance.getApiClient().e
//            InputStream result = apiInstance.tweets().getTweetsSample10Stream(partition)
//                    .backfillMinutes(backfillMinutes)
//                    .execute();
//            try{
//                JSON json = new JSON();
//                Type localVarReturnType = new TypeToken<Get2TweetsSample10StreamResponse>(){}.getType();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(result));
//                String line = reader.readLine();
//                while (line != null) {
//                    if(line.isEmpty()) {
//                        System.out.println("==> Empty line");
//                        line = reader.readLine();
//                        continue;
//                    }
//                    Object jsonObject = json.getGson().fromJson(line, localVarReturnType);
//                    System.out.println(jsonObject != null ? jsonObject.toString() : "Null object");
//                    line = reader.readLine();
//                }
//            }catch (Exception e) {
//                e.printStackTrace();
//                System.out.println(e);
//            }
//        } catch (ApiException e) {
//            System.err.println("Exception when calling TweetsApi#getTweetsSample10Stream");
//            System.err.println("Status code: " + e.getCode());
//            System.err.println("Reason: " + e.getResponseBody());
//            System.err.println("Response headers: " + e.getResponseHeaders());
//            e.printStackTrace();
//        }
//        return true;
//    };
//
//    @PostConstruct
//    private void init(){
//        String bearer = "AAAAAAAAAAAAAAAAAAAAAI8LRgEAAAAAGyFneMcgfE27oafbv7YuIxwyb5Y%3DHcM7r0McMActg6IUEsVCsWjGXEYwEpt1WyMqTc5n6XlzSk7euj";
//        TwitterApi apiInstance1 = new TwitterApi(new TwitterCredentialsBearer(bearer));
//        apiInstance = apiInstance1;
//
//    }
//}
