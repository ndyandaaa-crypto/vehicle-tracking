package com.example.tracking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TrackingApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void listsVehiclesWithPositionDriverAndRoute() throws Exception {
        mvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].registrationNumber").exists())
                .andExpect(jsonPath("$[0].latitude").exists());
    }

    @Test
    void filtersVehiclesByStatus() throws Exception {
        mvc.perform(get("/api/vehicles").param("status", "IDLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("IDLE"));
    }

    @Test
    void viewsAVehicleWithDriverAndRouteStops() throws Exception {
        mvc.perform(get("/api/vehicles").param("status", "MOVING"))
                .andExpect(status().isOk());
        mvc.perform(get("/api/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicle.id").value(1))
                .andExpect(jsonPath("$.driver.licenseNumber").exists())
                .andExpect(jsonPath("$.route.waypoints.length()").isNumber());
    }

    @Test
    void listsRoutesAndViewsOne() throws Exception {
        mvc.perform(get("/api/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
        mvc.perform(get("/api/routes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.waypoints[0].name").exists())
                .andExpect(jsonPath("$.vehicles").isArray());
    }

    @Test
    void listsDriversAndViewsOne() throws Exception {
        mvc.perform(get("/api/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6));
        mvc.perform(get("/api/drivers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").exists());
    }

    @Test
    void returns404ForUnknownIds() throws Exception {
        mvc.perform(get("/api/vehicles/9999")).andExpect(status().isNotFound());
        mvc.perform(get("/api/routes/9999")).andExpect(status().isNotFound());
        mvc.perform(get("/api/drivers/9999")).andExpect(status().isNotFound());
    }
}
