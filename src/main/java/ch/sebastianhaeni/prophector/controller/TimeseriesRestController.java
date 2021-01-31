package ch.sebastianhaeni.prophector.controller;

import ch.sebastianhaeni.prophector.dto.TimeseriesResponseDto;
import ch.sebastianhaeni.prophector.mapper.TimeseriesMapper;
import ch.sebastianhaeni.prophector.service.CountriesService;
import ch.sebastianhaeni.prophector.service.TimeseriesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/timeseries")
public class TimeseriesRestController {

    private final TimeseriesService timeseriesService;
    private final TimeseriesMapper timeseriesMapper;
    private final CountriesService countriesService;

    public TimeseriesRestController(TimeseriesService timeseriesService,
                                    TimeseriesMapper timeseriesMapper,
                                    CountriesService countriesService) {
        this.timeseriesService = timeseriesService;
        this.timeseriesMapper = timeseriesMapper;
        this.countriesService = countriesService;
    }

    @GetMapping
    public TimeseriesResponseDto getTimeseries(
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("region") String region
    ) {
        var fromDate = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
        var toDate = LocalDate.parse(to, DateTimeFormatter.ISO_DATE);
        var country = countriesService.getCountry(region)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Could not find country with code=%s", region)));
        return timeseriesMapper.getCountryTimeseries(timeseriesService.getTimeseries(fromDate, toDate, region), country);
    }
}
