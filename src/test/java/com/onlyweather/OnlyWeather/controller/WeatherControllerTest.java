package com.onlyweather.OnlyWeather.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyweather.OnlyWeather.dto.MainInfoDto;
import com.onlyweather.OnlyWeather.dto.WeatherInfoDto;
import com.onlyweather.OnlyWeather.dto.WeatherResponseDto;
import com.onlyweather.OnlyWeather.exception.CityNotFoundException;
import com.onlyweather.OnlyWeather.exception.InvalidApiKeyException;
import com.onlyweather.OnlyWeather.service.WeatherService;

// This annotation sets up everything for testing only the web layer (our controller),
// without loading the entire application. It includes Mockito out of the box.
@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    // A tool to convert Java objects to JSON (and vice versa).
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // We create a "mock" version of WeatherService.
    // This makes the controller think it's talking to the real service,
    // but we can control what this mock service returns.
    @MockBean
    WeatherService weatherService;

    // Spring injects the MockMvc tool, which allows us to
    // simulate sending HTTP requests (like GET) to our controller.
    @Autowired
    MockMvc mockMvc;

    private WeatherResponseDto createSampleWeatherResponseDto(String city, double temp, String description, String iconCode){
        WeatherResponseDto weatherResponseDto = new WeatherResponseDto();

        MainInfoDto mainInfoDto = new MainInfoDto(temp);
        WeatherInfoDto weatherInfoDto = new WeatherInfoDto(description, iconCode);

        weatherResponseDto.setMain(mainInfoDto);
        weatherResponseDto.setWeather(Collections.singletonList(weatherInfoDto));
        weatherResponseDto.setName(city);

        return weatherResponseDto;
    }

    @Test
    public void testGetWeather() throws Exception{

        // --- Prepare Test Data ---
        // Create a sample response object that our mock service will return.
        WeatherResponseDto weatherResponseDto = createSampleWeatherResponseDto("London", 5.0, "clear sky", "01n");

        // --- Configure the Mock Service (Mockito) ---
        // We say: "When someone calls the getWeather method on our mock weatherService
        // with any string (anyString()), then return the prepared weatherResponseDto object".
        when(weatherService.getWeather(anyString()))
        .thenReturn(weatherResponseDto);

        // --- Prepare Expected JSON Response ---
        // Convert our sample Java object into a JSON string.
        String expectedJson = objectMapper.writeValueAsString(weatherResponseDto);

        // --- Execute and Verify ---
        // Use MockMvc to simulate sending a GET request to "/London".
        mockMvc.perform(get("/{city}", "London"))
        // Check if the server's response has a status of 200 (OK).
        .andExpect(status().isOk())
        // Check if the response body (in JSON format) is exactly
        // what we expected (our expectedJson).
        .andExpect(content().json(expectedJson));
    }

    @Test
    public void testGetWeather_View_WhenCityNotFound_ShouldReturnNotFound() throws Exception{
        String city = "UnknownCity";
        when(weatherService.getWeather(city))
        .thenThrow(new CityNotFoundException("City not found: " + city));

        mockMvc.perform(get("/{city}", city))
        .andExpect(status().isNotFound());

    }

    @Test
    public void testGetWeather_View_WhenApiInvalid_ShouldReturnUnauthorized() throws Exception{
        when(weatherService.getWeather("London"))
        .thenThrow(new InvalidApiKeyException("Invalid ApiKey, try to change it"));

        mockMvc.perform(get("/{city}", "London"))
        .andExpect(status().isUnauthorized());

    }

    @Test
    public void testShowWeather_WhenCalled_ShouldReturnCorrectWeatherView() throws Exception{
        WeatherResponseDto weatherResponseDto = createSampleWeatherResponseDto("London", 5.5, "clear sky", "01n");
        weatherResponseDto.setIconFileName("01d@2x.png");

        when(weatherService.getWeather(anyString()))
                .thenReturn(weatherResponseDto);

        mockMvc.perform(get("/view/{city}", "London"))
                .andExpect(status().isOk())
                .andExpect(view().name("weather-view"))
                .andExpect(model().attribute("city", "London"))
                .andExpect(model().attribute("temperature", "5.5 °С"))
                .andExpect(model().attribute("description", "clear sky"))
                .andExpect(model().attribute("icon", "01d@2x.png"));
    }
}
