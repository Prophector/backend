package ch.sebastianhaeni.prophector.dto.prediction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class PredictionModelDto extends PredictionModelRequestDto {
    private Long id;
    private String ownerId;
    private String owner;
    private Double lastScore;
    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mmZ")
    private ZonedDateTime lastPrediction;
}
