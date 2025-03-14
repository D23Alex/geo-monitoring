package com.adg.geomonitoringapi.event.controller;

import com.adg.geomonitoringapi.snapshot.service.StateService;
import com.adg.geomonitoringapi.event.SystemState;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StateController {
    private final StateService stateService;

    @GetMapping("/api/current-state")
    public SystemState getCurrentState() {
        return stateService.getCurrentState();
    }
}
