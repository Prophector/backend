package ch.sebastianhaeni.prophector.dto.prediction;

import lombok.Data;

import java.util.List;

@Data
public class PredictionResponse {

    private List<PredictionDataPointDto> dataPoints;
    private Double score;
}
