package ch.sebastianhaeni.prophector.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity(name = "datapoint_prediction")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class DataPointPrediction {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    private ProphetModel model;

    private LocalDate date;
    private double upperBound;
    private double yHat;
    private double lowerBound;
    private boolean isChangePoint;
}
