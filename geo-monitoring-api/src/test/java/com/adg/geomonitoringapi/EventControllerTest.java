package com.adg.geomonitoringapi;

import com.adg.geomonitoringapi.event.CompletionCriteria;
import com.adg.geomonitoringapi.event.Worker;
import com.adg.geomonitoringapi.event.entity.TaskAssignedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Test for creating TaskAssignedEvent
    @Test
    void createTaskAssignedEvent(@Value("classpath:/create_event.json") Resource json) throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.getContentAsByteArray())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    // Test for applying a TaskAssignedEvent to the system state
    @Test
    void applyTaskAssignedEventToSystemState() throws Exception {
        // Create a TaskAssignedEvent instance (in the test's code, not via controller)
        TaskAssignedEvent taskAssignedEvent = new TaskAssignedEvent();
        taskAssignedEvent.setDescription("Test Task Description");
        taskAssignedEvent.setAssignedWorkers(new HashSet<>(List.of(new Worker("Worker 1"), new Worker("Worker 2"))));
        taskAssignedEvent.setCompletionCriteria(List.of(new CompletionCriteria(true, false, "Completion Criterion 1", "Description 1")));
        taskAssignedEvent.setLocationId(1L);
        taskAssignedEvent.setActiveFrom(Instant.parse("2025-03-18T00:00:00Z"));
        taskAssignedEvent.setActiveTo(Instant.parse("2025-03-19T00:00:00Z"));

        // Use MockMvc to simulate the event being applied to the system state
        mockMvc.perform(post("/api/events/apply")  // Assuming you have a controller method for applying events
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"eventType\": \"TaskAssignedEvent\",\n" +
                                "  \"description\": \"Test Task Description\",\n" +
                                "  \"assignedWorkers\": [{ \"id\": 1, \"name\": \"Worker 1\" }, { \"id\": 2, \"name\": \"Worker 2\" }],\n" +
                                "  \"completionCriteria\": [{ \"name\": \"Completion Criterion 1\", \"description\": \"Description 1\", \"isCommentRequired\": true, \"isPhotoProofRequired\": false }],\n" +
                                "  \"locationId\": 1,\n" +
                                "  \"activeFrom\": \"2025-03-18T00:00:00Z\",\n" +
                                "  \"activeTo\": \"2025-03-19T00:00:00Z\"\n" +
                                "}"))
                .andExpect(status().isOk())  // Expecting 200 OK status
                .andExpect(MockMvcResultMatchers.jsonPath("$.tasks").exists())  // Check if tasks are included in the state
                .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[0].description").value("Test Task Description"));  // Ensure the task description is correct
    }
}
