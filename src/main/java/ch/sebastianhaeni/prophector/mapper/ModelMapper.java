package ch.sebastianhaeni.prophector.mapper;

import ch.sebastianhaeni.prophector.dto.prediction.PredictionDataPointDto;
import ch.sebastianhaeni.prophector.dto.prediction.PredictionModelDto;
import ch.sebastianhaeni.prophector.dto.prediction.PredictionModelRequestDto;
import ch.sebastianhaeni.prophector.dto.prediction.PredictionResponse;
import ch.sebastianhaeni.prophector.model.*;
import ch.sebastianhaeni.prophector.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelMapper {

    private final ModelService modelService;

    public void toEntity(PredictionModelRequestDto dto, ProphetModel entity, User currentUser, Country country) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setOwner(currentUser);
        entity.setCountry(country);
        entity.setType(dto.getType());
        entity.setDisplayType(dto.getDisplayType());
        entity.setRollingSumWindow(dto.getRollingSumWindow());
        entity.setSmoothing(dto.getSmoothing());
        entity.setDaysToLookBack(dto.getDaysToLookBack());
        entity.setChangePoints(dto.getChangePoints().stream()
                .map(c -> toEntity(entity, c))
                .collect(Collectors.toSet()));
        entity.setNumChangePoints(dto.getNumChangePoints());
        entity.setSeasonalityMode(dto.getSeasonalityMode());
        entity.setChangePointPriorScale(dto.getChangePointPriorScale());
        entity.setHolidaysPriorScale(dto.getHolidaysPriorScale());
        entity.setSeasonalityPriorScale(dto.getSeasonalityPriorScale());
        entity.setChangePointRange(dto.getChangePointRange());
        entity.setAddCountryHolidays(dto.isAddCountryHolidays());
    }

    @NotNull
    private ProphetModelChangePoint toEntity(ProphetModel entity, java.time.LocalDate c) {
        var changePoint = new ProphetModelChangePoint();
        changePoint.setModel(entity);
        changePoint.setDate(c);
        return changePoint;
    }

    public PredictionModelDto toDto(ProphetModel entity) {
        var dto = new PredictionModelDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setOwner(entity.getOwner().getDisplayName());
        dto.setOwnerId(entity.getOwner().getId().toString());
        var lastJob = modelService.getLastSuccessfulJob(entity);
        dto.setLastScore(lastJob.map(ProphetJob::getScore).orElse(null));
        dto.setLastPrediction(lastJob
                .map(d -> d.getFinishedTimestamp().atZone(TimeZone.getDefault().toZoneId()))
                .orElse(null));
        dto.setRegion(entity.getCountry().getIsoCode());
        dto.setType(entity.getType());
        dto.setDisplayType(entity.getDisplayType());
        dto.setRollingSumWindow(entity.getRollingSumWindow());
        dto.setSmoothing(entity.getSmoothing());
        dto.setDaysToLookBack(entity.getDaysToLookBack());
        dto.setChangePoints(entity.getChangePoints().stream()
                .map(ProphetModelChangePoint::getDate)
                .collect(Collectors.toSet()));
        dto.setNumChangePoints(entity.getNumChangePoints());
        dto.setSeasonalityMode(entity.getSeasonalityMode());
        dto.setChangePointPriorScale(entity.getChangePointPriorScale());
        dto.setSeasonalityPriorScale(entity.getSeasonalityPriorScale());
        dto.setHolidaysPriorScale(entity.getHolidaysPriorScale());
        dto.setChangePointRange(entity.getChangePointRange());
        dto.setAddCountryHolidays(entity.isAddCountryHolidays());

        return dto;
    }

    public PredictionResponse toDto(List<DataPointPrediction> dataPoints, ProphetModel model) {
        var dto = new PredictionResponse();
        dto.setScore(modelService.getLastSuccessfulJob(model)
                .map(ProphetJob::getScore)
                .orElse(null));
        dto.setDataPoints(dataPoints.stream()
                .map(this::toDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public PredictionDataPointDto toDto(DataPointPrediction entity) {
        var dto = new PredictionDataPointDto();
        dto.setDate(entity.getDate());
        dto.setUpperBound(entity.getUpperBound());
        dto.setYHat(entity.getYHat());
        dto.setLowerBound(entity.getLowerBound());
        dto.setChangePoint(entity.isChangePoint());
        return dto;
    }
}
