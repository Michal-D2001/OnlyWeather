package com.onlyweather.OnlyWeather.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponseDto {
    private MainInfoDto main;
    private List<WeatherInfoDto> weather;
    private String name;
}
