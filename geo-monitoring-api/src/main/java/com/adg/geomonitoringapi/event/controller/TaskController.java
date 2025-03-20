package com.adg.geomonitoringapi.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    @GetMapping("/object/{objectId}")
    public Object getAssignmentsByLocation(@PathVariable Long objectId) {
        return null;
    }


}
