package ch.sebastianhaeni.prophector.dto;

import lombok.Data;

import java.util.List;

@Data
public class CountriesResponseDto {

    private List<CountryDataDto> countries;

}
