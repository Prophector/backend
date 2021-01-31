package ch.sebastianhaeni.prophector.dto.prediction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PredictionDataPointDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private double upperBound;
    private double yHat;
    private double lowerBound;
    private boolean isChangePoint;
}
