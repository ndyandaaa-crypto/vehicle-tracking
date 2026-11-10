package com.example.tracking.config;

import com.example.tracking.model.*;
import com.example.tracking.repository.DriverRepository;
import com.example.tracking.repository.RouteRepository;
import com.example.tracking.repository.VehicleRepository;
import com.example.tracking.util.RouteGeometry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/** Loads sample routes, drivers and vehicles so the tracker has something to show on first run. */
@Configuration
public class SeedDataLoader {

    private static final double DEPOT_LAT = -17.8340;
    private static final double DEPOT_LON = 31.0500;

    @Bean
    CommandLineRunner seedTrackingData(RouteRepository routes, DriverRepository drivers, VehicleRepository vehicles) {
        return args -> {
            if (vehicles.count() > 0) return;

            Route north = routes.save(new Route("Harare to Bulawayo", "Harare", "Bulawayo", 439, 330, List.of(
                    new Waypoint("Harare", -17.8252, 31.0335),
                    new Waypoint("Chegutu", -18.1333, 30.1500),
                    new Waypoint("Kadoma", -18.3328, 29.9150),
                    new Waypoint("Kwekwe", -18.9281, 29.8149),
                    new Waypoint("Gweru", -19.4500, 29.8167),
                    new Waypoint("Bulawayo", -20.1325, 28.6265))));

            Route west = routes.save(new Route("Bulawayo to Victoria Falls", "Bulawayo", "Victoria Falls", 440, 330, List.of(
                    new Waypoint("Bulawayo", -20.1325, 28.6265),
                    new Waypoint("Lupane", -18.9300, 27.8100),
                    new Waypoint("Hwange", -18.3693, 26.5019),
                    new Waypoint("Victoria Falls", -17.9243, 25.8572))));

            Route east = routes.save(new Route("Harare to Mutare", "Harare", "Mutare", 263, 190, List.of(
                    new Waypoint("Harare", -17.8252, 31.0335),
                    new Waypoint("Marondera", -18.1853, 31.5519),
                    new Waypoint("Rusape", -18.5333, 32.1167),
                    new Waypoint("Mutare", -18.9707, 32.6709))));

            Driver tendai = drivers.save(new Driver("Tendai", "Moyo", "+263 77 100 2001", "tendai.moyo@example.com", "DL-482913", LocalDate.of(2028, 3, 14)));
            Driver blessing = drivers.save(new Driver("Blessing", "Ncube", "+263 77 100 2002", "blessing.ncube@example.com", "DL-519274", LocalDate.of(2027, 9, 30)));
            Driver lindiwe = drivers.save(new Driver("Lindiwe", "Dube", "+263 77 100 2003", "lindiwe.dube@example.com", "DL-603185", LocalDate.of(2029, 1, 8)));
            Driver farai = drivers.save(new Driver("Farai", "Chikwanha", "+263 77 100 2004", "farai.chikwanha@example.com", "DL-377042", LocalDate.of(2026, 12, 20)));
            Driver rudo = drivers.save(new Driver("Rudo", "Sibanda", "+263 77 100 2005", "rudo.sibanda@example.com", "DL-290856", LocalDate.of(2026, 5, 2)));
            drivers.save(new Driver("Kudzai", "Mutasa", "+263 77 100 2006", "kudzai.mutasa@example.com", "DL-745619", LocalDate.of(2028, 11, 25)));

            vehicles.saveAll(List.of(
                    onRoute("AGH 4521", "Isuzu", "FTR 850", 2021, VehicleType.TRUCK, VehicleStatus.MOVING, tendai, north, 0.35, 72),
                    onRoute("AFK 2290", "Scania", "R450", 2020, VehicleType.TRUCK, VehicleStatus.MOVING, blessing, west, 0.60, 78),
                    onRoute("AHB 7713", "Yutong", "ZK6122", 2022, VehicleType.BUS, VehicleStatus.MOVING, lindiwe, east, 0.20, 84),
                    onRoute("AGT 1068", "Toyota", "Hiace", 2023, VehicleType.VAN, VehicleStatus.MOVING, farai, north, 0.80, 66),
                    onRoute("AFR 9342", "Ford", "Transit", 2019, VehicleType.VAN, VehicleStatus.IDLE, rudo, west, 0.0, 0),
                    new Vehicle("AEV 5586", "Mercedes-Benz", "Actros", 2018, VehicleType.TRUCK, VehicleStatus.MAINTENANCE,
                            null, null, 0.0, DEPOT_LAT, DEPOT_LON, 0, Instant.now())));
        };
    }

    private Vehicle onRoute(String registration, String make, String model, int year, VehicleType type,
                            VehicleStatus status, Driver driver, Route route, double progress, double speed) {
        double[] position = RouteGeometry.positionAt(route.getWaypoints(), progress);
        return new Vehicle(registration, make, model, year, type, status, driver, route,
                progress, position[0], position[1], speed, Instant.now());
    }
}
