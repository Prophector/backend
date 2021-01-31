package ch.sebastianhaeni.prophector.controller;


import ch.sebastianhaeni.prophector.dto.CountriesResponseDto;
import ch.sebastianhaeni.prophector.mapper.CountryMapper;
import ch.sebastianhaeni.prophector.service.CountriesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/countries")
public class CountryRestController {

    private final CountriesService countriesService;
    private final CountryMapper countryMapper;

    public CountryRestController(CountriesService countriesService, CountryMapper countryMapper) {
        this.countriesService = countriesService;
        this.countryMapper = countryMapper;
    }

    @GetMapping
    public CountriesResponseDto getCountries() {
        return countryMapper.mapCountries(countriesService.getCountries());
    }

    @GetMapping(value = "/historical")
    public CountriesResponseDto getCountriesWithHistoricalData(@RequestParam(value = "days", defaultValue = "14") String daysParam) {
        var days = 14;
        try {
            days = Integer.parseInt(daysParam);
            if (days > 14) {
                // perf: do not allow for more
                days = 14;
            }
        } catch (NumberFormatException ignore) {
            // nop
        }

        var countriesWithHistorical = countriesService.getCountriesWithHistorical(LocalDate.now(), days);
        return countryMapper.mapCountriesWithHistoricalData(countriesWithHistorical);
    }
}
