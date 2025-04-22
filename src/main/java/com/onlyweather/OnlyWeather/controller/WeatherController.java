package com.onlyweather.OnlyWeather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.onlyweather.OnlyWeather.service.WeatherService;
import com.onlyweather.OnlyWeather.dto.WeatherResponseDto;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    @ResponseBody
    public WeatherResponseDto getWeather(@PathVariable String city){
        return weatherService.getWeather(city);
    }

    @GetMapping("/view/{city}")
    public String showWeatherView(@PathVariable String city, Model model){
        WeatherResponseDto weatherData = weatherService.getWeather(city);
        model.addAttribute("city", weatherData.getName());
        model.addAttribute("temperature", weatherData.getMain().getTemp() + " °С");
        model.addAttribute("icon", weatherData.getIconFileName());
        model.addAttribute("description", weatherData.getWeather().get(0).getDescription());
        return "weather-view";
    }
}
