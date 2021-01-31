package ch.sebastianhaeni.prophector.controller;

import ch.sebastianhaeni.prophector.dto.auth.LocalUser;
import ch.sebastianhaeni.prophector.dto.auth.UserInfo;
import ch.sebastianhaeni.prophector.dto.prediction.PredictionModelSearchResponse;
import ch.sebastianhaeni.prophector.mapper.ModelMapper;
import ch.sebastianhaeni.prophector.repository.ProphetModelRepository;
import ch.sebastianhaeni.prophector.service.ModelService;
import ch.sebastianhaeni.prophector.util.GeneralUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final ProphetModelRepository prophetModelRepository;
    private final ModelService modelService;
    private final ModelMapper modelMapper;

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserInfo getCurrentUser(Authentication authentication) {
        return GeneralUtils.buildUserInfo((LocalUser) authentication.getPrincipal());
    }

    @GetMapping("/models")
    @PreAuthorize("hasRole('ROLE_USER')")
    public PredictionModelSearchResponse getUserModels(Authentication authentication) {
        LocalUser principal = (LocalUser) authentication.getPrincipal();
        var modelDtos = prophetModelRepository.findAllByOwner(principal.getUser()).stream()
                .sorted(modelService::modelScoreComparator)
                .map(modelMapper::toDto)
                .collect(Collectors.toList());
        var dto = new PredictionModelSearchResponse();
        dto.setModels(modelDtos);
        return dto;
    }
}
