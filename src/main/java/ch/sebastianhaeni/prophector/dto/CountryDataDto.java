package ch.sebastianhaeni.prophector.dto;

import ch.sebastianhaeni.prophector.model.TimeseriesType;
import lombok.Data;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Data
public class CountryDataDto {

    private String name;
    private String isoCode;

    private BigInteger population;
    private BigInteger totalCases;
    private BigInteger totalDeaths;
    private BigInteger totalTests;
    private BigInteger totalVaccinations;

    private Map<TimeseriesType, TimeseriesDto> historicalData = new HashMap<>();
}
