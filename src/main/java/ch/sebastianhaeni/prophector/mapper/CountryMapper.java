package ch.sebastianhaeni.prophector.mapper;

import ch.sebastianhaeni.prophector.dto.CountriesResponseDto;
import ch.sebastianhaeni.prophector.dto.CountryDataDto;
import ch.sebastianhaeni.prophector.model.CountryData;
import ch.sebastianhaeni.prophector.model.DataPoint;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CountryMapper {

    private final TimeseriesMapper timeseriesMapper;

    public CountryMapper(TimeseriesMapper timeseriesMapper) {
        this.timeseriesMapper = timeseriesMapper;
    }

    public CountriesResponseDto mapCountries(List<CountryData> countries) {
        var response = new CountriesResponseDto();
        var data = countries.stream()
                .map(this::mapCountryData)
                .collect(Collectors.toList());
        response.setCountries(data);
        return response;
    }

    public CountriesResponseDto mapCountriesWithHistoricalData(Map<CountryData, List<DataPoint>> countries) {
        var response = new CountriesResponseDto();
        var data = countries.entrySet().stream()
                .map(entry -> mapCountryData(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        response.setCountries(data);
        return response;
    }

    private CountryDataDto mapCountryData(CountryData entry) {
        return mapCountryData(entry, Collections.emptyList());
    }

    private CountryDataDto mapCountryData(CountryData entry, List<DataPoint> timeseries) {
        var r = new CountryDataDto();
        r.setName(entry.getName());
        r.setIsoCode(entry.getIsoCode());
        r.setPopulation(entry.getPopulation());
        r.setTotalCases(entry.getCases());
        r.setTotalDeaths(entry.getDeaths());
        r.setTotalTests(entry.getTests());
        r.setTotalVaccinations(entry.getVaccinations());
        if (!timeseries.isEmpty()) {
            r.setHistoricalData(timeseriesMapper.getTimeseries(timeseries));
        }
        return r;
    }
}
