package com.onlyweather.OnlyWeather.service;

import com.onlyweather.OnlyWeather.enums.WeatherIcons;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import com.onlyweather.OnlyWeather.dto.WeatherResponseDto;

import com.onlyweather.OnlyWeather.exception.CityNotFoundException;
import com.onlyweather.OnlyWeather.exception.InvalidApiKeyException;
import com.onlyweather.OnlyWeather.exception.WeatherServiceUnavailableException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiUrl;

    public WeatherService(@Value("${openweathermap.api.key}") String apiKey, 
    @Value("${openweathermap.api.url}") String apiUrl, 
    RestTemplate restTemplate){
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
    }

    public WeatherResponseDto getWeather(String city){
        if(city == null || city.trim().isEmpty()){
            throw new IllegalArgumentException("City is empty or null, this is illegal");
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
        .queryParam("q", city)
        .queryParam("appid", apiKey)
        .queryParam("units", "metric");

        String url = builder.toUriString();
        
        try{
            ResponseEntity<WeatherResponseDto> response = restTemplate.getForEntity(url, WeatherResponseDto.class);
            WeatherResponseDto weatherResponseDto = response.getBody();

            if (weatherResponseDto != null &&
                weatherResponseDto.getWeather() != null &&
                !weatherResponseDto.getWeather().isEmpty()){

                    String apiIconCode = weatherResponseDto.getWeather().get(0).getIcon();
                    WeatherIcons weatherIcon = WeatherIcons.findByApiIconCode(apiIconCode);
                    if (weatherIcon != null) {
                        weatherResponseDto.setIconFileName(weatherIcon.getIconFileName());
                    }
            }
            return weatherResponseDto;
        } catch (HttpClientErrorException e){
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new CityNotFoundException("City not found: " + city);
            }
            if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                throw new WeatherServiceUnavailableException("Service unavailable, try again later");
            }
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new InvalidApiKeyException("Invalid ApiKey, try to change it");
            }
            throw e;
        }
    }
}