package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.AwsContent;
import sorim.f1.slasher.relentless.entities.RedditPostNew;
import sorim.f1.slasher.relentless.entities.RedditPostTop;
import sorim.f1.slasher.relentless.entities.Replay;

import java.util.List;

public interface RacingfkService {

    Boolean fetchReplayLinks();

    List<Replay> getReplays(Integer page);
}
