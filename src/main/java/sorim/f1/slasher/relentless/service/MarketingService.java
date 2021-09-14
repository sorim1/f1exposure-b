package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.Marketing;

import java.util.List;

public interface MarketingService {

    Marketing getRandomMarketing();

    Marketing getMarketing(Integer id);

    void deleteMarketing(Integer id);

    Marketing saveMarketing(Marketing marketing);

    List<Marketing> backupMarketing();

    Boolean restoreMarketing(List<Marketing> marketings);
}
