package ch.sebastianhaeni.prophector.dto.prediction;

import ch.sebastianhaeni.prophector.model.ModelStatus;
import ch.sebastianhaeni.prophector.model.TimeseriesType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
public class PredictionModelRequestDto {
    @NotNull
    @Length(max = 30)
    private String name;
    @NotNull
    @Length(max = 300)
    private String description;
    @NotNull
    private ModelStatus status;
    @NotNull
    @Length(max = 5)
    private String region;
    @NotNull
    private TimeseriesType type;
    /**
     * 'daily' or 'absolute'
     */
    @NotNull
    @Length(max = 10)
    private String displayType;

    @Positive
    @Max(14)
    private int rollingSumWindow;

    /**
     * One of 0, 7, 14
     */
    @PositiveOrZero
    @Max(14)
    private int smoothing;

    /**
     * Range: 30 - 365
     */
    @Min(30)
    @Max(365)
    private int daysToLookBack;

    @NotNull
    @Size(max = 20)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Set<LocalDate> changePoints;

    @Positive
    @Max(100)
    private int numChangePoints;

    @NotNull
    @Length(max = 20)
    private String seasonalityMode;

    @Positive
    @Max(1)
    private double changePointPriorScale;

    @Positive
    @Max(1000)
    private double holidaysPriorScale;

    @Positive
    @Max(1000)
    private double seasonalityPriorScale;

    @Positive
    @Max(1)
    private double changePointRange;

    private boolean addCountryHolidays;
}
