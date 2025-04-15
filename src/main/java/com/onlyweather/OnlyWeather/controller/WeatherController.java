package com.onlyweather.OnlyWeather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.onlyweather.OnlyWeather.service.WeatherService;
import com.onlyweather.OnlyWeather.dto.WeatherResponseDto;

@RestController
@RequestMapping
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public WeatherResponseDto getWeather(@PathVariable String city){
        return weatherService.getWeather(city);
    }
}
