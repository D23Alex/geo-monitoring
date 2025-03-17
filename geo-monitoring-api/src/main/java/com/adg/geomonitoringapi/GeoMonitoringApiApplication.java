package com.adg.geomonitoringapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.adg.geomonitoringapi.worker.mapper")
public class GeoMonitoringApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeoMonitoringApiApplication.class, args);
    }

}
