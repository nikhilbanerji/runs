package com.nikhilbanerji.runs.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunController.class)
class RunControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RunRepository runRepository;

    private final List<Run> runs = new ArrayList<>();

    @BeforeEach
    void setUp() {
        runs.add(new Run(
                1,
                "Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                4,
                Location.OUTDOOR,
                null
        ));
    }

    @Test
    void shouldFindAll() throws Exception {
        when(runRepository.findAll()).thenReturn(runs);
        mvc.perform(get("/api/runs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(runs.size())));
    }

    @Test
    void shouldFindById() throws Exception {
        Run run = runs.get(0);
        when(runRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(run));
        mvc.perform(get("/api/runs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(run.id())))
                .andExpect(jsonPath("$.title", is(run.title())))
                .andExpect(jsonPath("$.startedOn", is(run.startedOn().toString())))
                .andExpect(jsonPath("$.completedOn", is(run.completedOn().toString())))
                .andExpect(jsonPath("$.miles", is(run.miles())))
                .andExpect(jsonPath("$.location", is(run.location().toString())));
    }

    @Test
    void shouldReturnNotFoundWithInvalidId() throws Exception {
        mvc.perform(get("/api/runs/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindAllByLocation() throws Exception {
        Run run = runs.get(0);
        when(runRepository.findAllByLocation(run.location().toString())).thenReturn(runs);
        mvc.perform(get("/api/runs/location/{location}", run.location().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(runs.size())));
    }

    @Test
    void shouldCreate() throws Exception {
        Run run = new Run(null, "test", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1, Location.INDOOR, null);
        mvc.perform(post("/api/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(run))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdate() throws Exception {
        Run run = new Run(null, "test", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1, Location.INDOOR, null);
        mvc.perform(put("/api/runs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(run))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDelete() throws Exception {
        when(runRepository.findById(1)).thenReturn(Optional.of(runs.get(0)));
        mvc.perform(delete("/api/runs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(runs.get(0))))
                .andExpect(status().isNoContent());
    }
}