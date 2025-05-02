package com.onlyweather.OnlyWeather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Weather", description = "API for retrieving weather data")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @Operation(summary = "Get weather data for a city", description = "Returns weather data in JSON format for the specific city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved weather data",
                        content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = WeatherResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "City not found"),
            @ApiResponse(responseCode = "500", description = "Weather service unavailable")
    })

    @GetMapping("/{city}")
    @ResponseBody
    public WeatherResponseDto getWeather(@Parameter(description = "City name") @PathVariable String city){
        return weatherService.getWeather(city);
    }

    @Operation(summary = "Display HTML view with weather data", description = "Returns an HTML page with weather data for the specified city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved weather data and generated HTML view"),
            @ApiResponse(responseCode = "404", description = "City not found"),
            @ApiResponse(responseCode = "500", description = "Weather service unavailable")
    })

    @GetMapping("/view/{city}")
    public String showWeatherView(@Parameter(description = "City name") @PathVariable String city, Model model){
        WeatherResponseDto weatherData = weatherService.getWeather(city);
        model.addAttribute("city", weatherData.getName());
        model.addAttribute("temperature", weatherData.getMain().getTemp() + " °С");
        model.addAttribute("icon", weatherData.getIconFileName());
        model.addAttribute("description", weatherData.getWeather().get(0).getDescription());
        return "weather-view";
    }
}
