package ch.sebastianhaeni.prophector.mapper;

import ch.sebastianhaeni.prophector.dto.DataPointDto;
import ch.sebastianhaeni.prophector.dto.TimeseriesDto;
import ch.sebastianhaeni.prophector.dto.TimeseriesResponseDto;
import ch.sebastianhaeni.prophector.model.BaseCountry;
import ch.sebastianhaeni.prophector.model.DataPoint;
import ch.sebastianhaeni.prophector.model.TimeseriesType;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TimeseriesMapper {

    public TimeseriesResponseDto getCountryTimeseries(List<DataPoint> timeseries, BaseCountry countryData) {
        var response = new TimeseriesResponseDto();
        response.setIsoCode(countryData.getIsoCode());
        response.setPopulation(countryData.getPopulation());
        response.setTimeseries(getTimeseries(timeseries));
        return response;
    }

    public Map<TimeseriesType, TimeseriesDto> getTimeseries(List<DataPoint> timeseries) {
        Map<TimeseriesType, TimeseriesDto> response = new HashMap<>();
        addTimeseries(timeseries, response, TimeseriesType.CASES, DataPoint::getCases);
        addTimeseries(timeseries, response, TimeseriesType.DEATHS, DataPoint::getDeaths);
        addTimeseries(timeseries, response, TimeseriesType.TESTS, DataPoint::getTests);
        addTimeseries(timeseries, response, TimeseriesType.HOSPITALIZATIONS, DataPoint::getHospitalizations);
        addTimeseries(timeseries, response, TimeseriesType.VACCINATIONS, DataPoint::getVaccinations);
        return response;
    }

    private void addTimeseries(List<DataPoint> timeseries,
                               Map<TimeseriesType, TimeseriesDto> response,
                               TimeseriesType type,
                               Function<DataPoint, BigInteger> getter) {
        var points = extractValues(timeseries, getter);
        if (points.getValues().stream().anyMatch(s -> s.getValue() != null && s.getValue().signum() == 1)) {
            response.put(type, points);
        }
    }

    private TimeseriesDto extractValues(List<DataPoint> timeseriesPoint, Function<DataPoint, BigInteger> getter) {
        var timeseries = new TimeseriesDto();
        var values = timeseriesPoint.stream()
                .map(v -> mapDataPoint(getter, v))
                .collect(Collectors.toList());
        timeseries.setValues(values);
        return timeseries;
    }

    private DataPointDto mapDataPoint(Function<DataPoint, BigInteger> getter, DataPoint v) {
        var point = new DataPointDto();
        point.setDate(v.getDate());
        point.setValue(getter.apply(v));
        return point;
    }
}
