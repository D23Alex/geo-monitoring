package com.adg.geomonitoringapi.state.controller;

import com.adg.geomonitoringapi.state.service.StateService;
import com.adg.geomonitoringapi.state.SystemState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StateController {
    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping("/api/current-state")
    public SystemState getCurrentState() {
        return stateService.getCurrentState();
    }
}
