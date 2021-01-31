package ch.sebastianhaeni.prophector.controller;

import ch.sebastianhaeni.prophector.dto.auth.LocalUser;
import ch.sebastianhaeni.prophector.dto.prediction.PredictionModelDto;
import ch.sebastianhaeni.prophector.dto.prediction.PredictionModelRequestDto;
import ch.sebastianhaeni.prophector.dto.prediction.PredictionModelSearchResponse;
import ch.sebastianhaeni.prophector.dto.prediction.PredictionResponse;
import ch.sebastianhaeni.prophector.mapper.ModelMapper;
import ch.sebastianhaeni.prophector.model.ModelStatus;
import ch.sebastianhaeni.prophector.model.ProphetJob;
import ch.sebastianhaeni.prophector.model.ProphetJobStatus;
import ch.sebastianhaeni.prophector.model.ProphetModel;
import ch.sebastianhaeni.prophector.repository.CountryRepository;
import ch.sebastianhaeni.prophector.repository.DataPointPredictionRepository;
import ch.sebastianhaeni.prophector.repository.ProphetJobRepository;
import ch.sebastianhaeni.prophector.repository.ProphetModelRepository;
import ch.sebastianhaeni.prophector.service.ModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static ch.sebastianhaeni.prophector.util.AuthUtil.checkModelPermission;

@RestController
@RequestMapping("/api/model")
@RequiredArgsConstructor
@Slf4j
public class ModelRestController {

    private final ProphetModelRepository prophetModelRepository;
    private final DataPointPredictionRepository dataPointPredictionRepository;
    private final ProphetJobRepository prophetJobRepository;
    private final CountryRepository countryRepository;
    private final ModelService modelService;
    private final ModelMapper modelMapper;

    @GetMapping
    public PredictionModelSearchResponse getMatchingModels(Authentication authentication,
                                                           @RequestParam("region") @NotNull String region) {
        var country = countryRepository.findCountryByIsoCode(region).orElseThrow();

        var models = prophetModelRepository.findAllByCountryAndStatus(country, ModelStatus.PUBLISHED).stream()
                .sorted(modelService::modelScoreComparator)
                .collect(Collectors.toList());

        if (authentication != null) {
            var principal = (LocalUser) authentication.getPrincipal();
            var userModels = prophetModelRepository.findAllByCountryAndOwner(country, principal.getUser()).stream()
                    .sorted(modelService::modelScoreComparator)
                    .collect(Collectors.toList());
            userModels.addAll(models);
            models = userModels.stream().distinct().collect(Collectors.toList());
        }

        var dto = new PredictionModelSearchResponse();
        dto.setModels(models.stream()
                .map(modelMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    @GetMapping("/{id}")
    public PredictionModelDto getModel(@PathVariable("id") String id) {
        var model = getModelById(id);
        return modelMapper.toDto(model);
    }

    @GetMapping("/{id}/predict")
    public PredictionResponse getPrediction(@PathVariable("id") String id) {
        var model = getModelById(id);
        var dataPoints = dataPointPredictionRepository.findAllByModelOrderByDate(model);

        return modelMapper.toDto(dataPoints, model);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public PredictionModelDto createModel(Authentication authentication, @RequestBody @Valid PredictionModelRequestDto dto) {
        var entity = new ProphetModel();

        var principal = (LocalUser) authentication.getPrincipal();
        var country = countryRepository.findCountryByIsoCode(dto.getRegion()).orElseThrow();

        modelMapper.toEntity(dto, entity, principal.getUser(), country);
        prophetModelRepository.save(entity);

        return modelMapper.toDto(entity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void updateModel(Authentication authentication,
                            @PathVariable("id") String id,
                            @RequestBody @Valid PredictionModelRequestDto dto) {
        update(authentication, id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteModel(Authentication authentication, @PathVariable("id") String id) {
        ProphetModel model = getModelById(id);
        checkModelPermission(authentication, model);
        prophetModelRepository.delete(model);
    }

    private ProphetModel update(Authentication authentication, String id, PredictionModelRequestDto dto) {
        var model = getModelById(id);
        checkModelPermission(authentication, model);

        var principal = (LocalUser) authentication.getPrincipal();
        var country = countryRepository.findCountryByIsoCode(dto.getRegion()).orElseThrow();
        modelMapper.toEntity(dto, model, principal.getUser(), country);
        prophetModelRepository.save(model);
        return model;
    }

    @PostMapping("/{id}/predict")
    @PreAuthorize("hasRole('ROLE_USER')")
    public PredictionResponse triggerPrediction(
            Authentication authentication,
            @PathVariable("id") String id,
            @RequestBody @Valid PredictionModelRequestDto dto,
            @RequestParam(value = "withScore", required = false, defaultValue = "false") boolean withScore
    ) {
        var model = update(authentication, id, dto);
        var job = new ProphetJob();
        job.setModel(model);
        job.setJobStatus(ProphetJobStatus.IN_QUEUE);
        job.setSubmittedTimestamp(LocalDateTime.now());
        job.setWithScore(withScore);
        prophetJobRepository.save(job);

        modelService.forJobToFinish(job);

        return getPrediction(id);
    }

    private ProphetModel getModelById(String id) {
        return prophetModelRepository.findById(Long.valueOf(id)).orElseThrow();
    }
}
