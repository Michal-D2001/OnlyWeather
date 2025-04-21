package com.onlyweather.OnlyWeather.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeatherIcons {
    CLEAR_SKY("01d", "01d@2x.png"),
    CLEAR_SKY_NIGHT("01n", "01n@2x.png"),

    FEW_CLOUDS("02d", "02d@2x.png"),
    FEW_CLOUDS_NIGHT("02n", "02n@2x.png"),

    SCATTERED_CLOUDS("03d", "03d@2x.png"),
    SCATTERED_CLOUDS_NIGHT("03n", "03n@2x.png"),

    BROKEN_CLOUDS("04d", "04d@2x.png"),
    BROKEN_CLOUDS_NIGHT("04n", "04n@2x.png"),

    SHOWER_RAIN("09d", "09d@2x.png"),
    SHOWER_RAIN_NIGHT("09n", "09n@2x.png"),

    RAIN("01d", "10d@2x.png"),
    RAIN_NIGHT("10n", "10n@2x.png"),

    THUNDERSTORM("11d", "11d@2x.png"),
    THUNDERSTORM_NIGHT("11n", "11n@2x.png"),

    SNOW("13d", "13d@2x.png"),
    SNOW_NIGHT("13n", "13n@2x.png"),

    MIST("50d", "50d@2x.png"),
    MIST_NIGHT("50n", "50n@2x.png");

    private String apiIconCode;
    private String iconFileName;

    public static WeatherIcons findByApiIconCode(String codeToFind){
        for (WeatherIcons icon: values()){
            if (icon.getApiIconCode().equalsIgnoreCase(codeToFind)){
                return icon;
            }
        }
        return null;
    }
}
