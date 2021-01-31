package ch.sebastianhaeni.prophector.service;

import ch.sebastianhaeni.prophector.model.ProphetJob;
import ch.sebastianhaeni.prophector.model.ProphetJobStatus;
import ch.sebastianhaeni.prophector.model.ProphetModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModelService {

    private final EntityManager entityManager;

    @Transactional
    public void forJobToFinish(ProphetJob job) {
        while (job.getJobStatus() == ProphetJobStatus.IN_QUEUE || job.getJobStatus() == ProphetJobStatus.PROCESSING) {
            entityManager.refresh(job);
            waitABit();
        }
    }

    private void waitABit() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    @Transactional // TODO remove transactional for performance gains
    public int modelScoreComparator(ProphetModel a, ProphetModel b) {
        var jobA = getLastSuccessfulJob(a);
        var jobB = getLastSuccessfulJob(b);

        if (jobA.isEmpty()) {
            return jobB.isEmpty() ? 0 : 1;
        } else if (jobB.isEmpty()) {
            return -1;
        }

        var scoreA = jobA.get().getScore();
        var scoreB = jobB.get().getScore();

        if (scoreA == null) {
            return scoreB == null ? 0 : 1;
        } else if (scoreB == null) {
            return -1;
        }

        return scoreA.compareTo(scoreB);
    }

    @NotNull
    @Transactional
    public Optional<ProphetJob> getLastSuccessfulJob(ProphetModel entity) {
        entityManager.refresh(entity);
        return entity.getJobs().stream()
                .filter(j -> j.getJobStatus() == ProphetJobStatus.DONE)
                .max(Comparator.comparing(ProphetJob::getFinishedTimestamp));
    }
}
