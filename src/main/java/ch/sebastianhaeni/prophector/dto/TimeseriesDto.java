package ch.sebastianhaeni.prophector.dto;

import lombok.Data;

import java.util.List;

@Data
public class TimeseriesDto {

    private List<DataPointDto> values;

}
