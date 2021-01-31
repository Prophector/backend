package ch.sebastianhaeni.prophector.service;

import ch.sebastianhaeni.prophector.model.DataPoint;
import ch.sebastianhaeni.prophector.repository.DataPointRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeseriesService {

    private final DataPointRepository dataPointRepository;

    public TimeseriesService(DataPointRepository dataPointRepository) {
        this.dataPointRepository = dataPointRepository;
    }

    public List<DataPoint> getTimeseries(LocalDate from, LocalDate to, String region) {
        return dataPointRepository.findAllByRegionAndDateAfterAndDateBeforeOrderByDate(region, from, to);
    }
}
