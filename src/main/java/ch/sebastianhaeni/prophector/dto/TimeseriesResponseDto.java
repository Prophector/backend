package ch.sebastianhaeni.prophector.dto;

import ch.sebastianhaeni.prophector.model.TimeseriesType;
import lombok.Data;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Data
public class TimeseriesResponseDto {

    private String isoCode;
    private BigInteger population;
    private Map<TimeseriesType, TimeseriesDto> timeseries = new HashMap<>();

}
