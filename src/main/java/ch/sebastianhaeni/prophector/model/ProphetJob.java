package ch.sebastianhaeni.prophector.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ProphetJob {

    @Id
    @GeneratedValue(generator = "pro_prophet_job_id_seq")
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private ProphetModel model;

    @Enumerated(EnumType.STRING)
    private ProphetJobStatus jobStatus;

    private LocalDateTime submittedTimestamp;

    private LocalDateTime startedTimestamp;

    private LocalDateTime finishedTimestamp;

    /**
     * Specifies if the prediction engine should compute a score or not (because it takes much longer with score).
     */
    private boolean withScore;

    private Double score;

}
