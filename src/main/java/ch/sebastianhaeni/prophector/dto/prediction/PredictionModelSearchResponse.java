package ch.sebastianhaeni.prophector.dto.prediction;

import lombok.Data;

import java.util.List;

@Data
public class PredictionModelSearchResponse {

    private List<PredictionModelDto> models;

}
