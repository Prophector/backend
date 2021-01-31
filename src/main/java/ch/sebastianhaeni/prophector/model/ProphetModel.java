package ch.sebastianhaeni.prophector.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ProphetModel {

    @Id
    @GeneratedValue(generator = "pro_prophet_model_id_seq")
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private ModelStatus status;

    @ManyToOne
    private User owner;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "model", fetch = FetchType.EAGER)
    private List<ProphetJob> jobs = new ArrayList<>();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.DETACH)
    private Country country;

    @Enumerated(EnumType.STRING)
    private TimeseriesType type;

    /**
     * 'daily' or 'absolute' or 'rollingSum'
     */
    private String displayType;

    /**
     * One of 7, 14. Only relevant if displayType = 'rollingSum'
     */
    private int rollingSumWindow;

    /**
     * One of 0, 7, 14
     */
    private int smoothing;

    /**
     * Range: 30 - 365
     */
    private int daysToLookBack;

    /**
     * Defining any will not use {@link ProphetModel#numChangePoints}
     */
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "model", fetch = FetchType.EAGER)
    private Set<ProphetModelChangePoint> changePoints = new HashSet<>();

    /**
     * Select how many change points should maximally be automatically detected.
     */
    private int numChangePoints;

    /**
     * 'additive' or 'multiplicative'
     */
    private String seasonalityMode;

    /**
     * Range: 0.01 - 1
     */
    private double changePointPriorScale;

    /**
     * Range: 0 - 1000
     */
    private double seasonalityPriorScale;

    /**
     * Range: 0 - 1000
     */
    private double holidaysPriorScale;

    /**
     * Range: 0 - 1
     * 1.0 = select changepoints in all data
     * 0.8 = only select changepoints in the first 80% of data
     */
    private double changePointRange;

    private boolean addCountryHolidays;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "model")
    private Set<DataPointPrediction> predictions = new HashSet<>();

}
