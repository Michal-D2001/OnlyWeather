package com.onlyweather.OnlyWeather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.onlyweather.OnlyWeather.dto.MainInfoDto;
import com.onlyweather.OnlyWeather.dto.WeatherInfoDto;
import com.onlyweather.OnlyWeather.dto.WeatherResponseDto;
import com.onlyweather.OnlyWeather.exception.CityNotFoundException;
import com.onlyweather.OnlyWeather.exception.InvalidApiKeyException;
import com.onlyweather.OnlyWeather.exception.WeatherServiceUnavailableException;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;
import java.util.Collections;

// Unit tests for the WeatherService. Let's verify its behavior.
@ExtendWith(MockitoExtension.class) // This tells JUnit to enable Mockito features.
public class WeatherServiceTest {

    // We need a mock RestTemplate, so we don't interact with the real OpenWeatherMap API during tests.
    // Actual API calls would be slow and might depend on external factors.
    @Mock
    private RestTemplate restTemplate;

    // This is the service instance we are going to test.
    private WeatherService weatherService;

    // This method runs before each test method executes.
    @BeforeEach
    void setUp() {
        // We manually create the WeatherService here because it requires constructor parameters (API key/URL).
        // Using @InjectMocks is trickier with constructor injection like this.
        // The actual values for key/URL don't matter here, as the restTemplate is mocked.
        String testApiKey = "dummy-api-key";
        String testApiUrl = "http://dummy-api.url";
        weatherService = new WeatherService(testApiKey, testApiUrl, restTemplate);
    }

    // Test the standard scenario: fetching weather for a known city.
    @Test
    public void getWeather_ShouldReturnWeatherData() {
        // --- Arrange --- 
        // First, prepare a sample response object, simulating what the API might return.
        WeatherResponseDto simulatedApiResponse = new WeatherResponseDto();
        MainInfoDto mainInfo = new MainInfoDto();
        mainInfo.setTemp(10.0);
        simulatedApiResponse.setMain(mainInfo);
        WeatherInfoDto weatherInfo = new WeatherInfoDto();
        weatherInfo.setDescription("clear sky");
        // The API returns weather info in a list, so we wrap our sample data in a list.
        simulatedApiResponse.setWeather(Collections.singletonList(weatherInfo));
        simulatedApiResponse.setName("London");

        // Configure the mock restTemplate: "WHEN getForEntity is called with any URL
        // and expects a WeatherResponseDto.class, THEN return our prepared simulated response."
        when(restTemplate.getForEntity(anyString(), eq(WeatherResponseDto.class)))
            .thenReturn(ResponseEntity.ok(simulatedApiResponse)); // ok() means HTTP 200 OK status

        // --- Act --- 
        // Execute the method under test.
        WeatherResponseDto actualResult = weatherService.getWeather("London");

        // --- Assert --- 
        // Verify that the received result matches our expectations.
        assertEquals("London", actualResult.getName());
        assertEquals(10.0, actualResult.getMain().getTemp());
        assertEquals(1, actualResult.getWeather().size()); // Expecting one weather entry
        assertEquals("clear sky", actualResult.getWeather().get(0).getDescription());
    }

    // Test handling of invalid input: null or empty city names.
    @ParameterizedTest
    @NullAndEmptySource // This annotation provides null and an empty string as input for the test.
    public void getWeather_ShouldThrowIllegalArgumentException_WhenCityIsNullOrEmpty(String invalidCityInput){
        // Expect that calling the service method with invalid input throws an IllegalArgumentException.
        IllegalArgumentException thrownException = assertThrows(
            IllegalArgumentException.class,
            () -> weatherService.getWeather(invalidCityInput) // Trigger the call with the invalid city
        );
        // Additionally, verify the exception message is as expected.
        assertEquals("City is empty or null, this is illegal", thrownException.getMessage());
    }

    // A generic test checking for any RuntimeException if the API call fails.
    // This might be less specific than the 404/500 tests, but covers general errors.
    @Test
    public void getWeather_ShouldReturnNullWhenApiCallFails(){
        // Configure the mock restTemplate to throw a generic RuntimeException when called.
        when(restTemplate.getForEntity(anyString(), eq(WeatherResponseDto.class)))
        .thenThrow(new RuntimeException("Generic API failure!")); // Example generic error

        // Assert that calling getWeather results in that RuntimeException being propagated.
        assertThrows(RuntimeException.class, () -> {
            weatherService.getWeather("London");
        });
    }

    // Test the scenario where the API indicates "City Not Found" (HTTP 404).
    @Test
    public void getWeather_ShouldThrowCityNotFoundException(){
        String nonExistentCityName = "CityThatDoesNotExist123";

        // Configure mock restTemplate: "WHEN called, simulate an HTTP 404 error."
        when(restTemplate.getForEntity(anyString(), eq(WeatherResponseDto.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)); // Simulate the 404 response

        // Assert that our service method throws the specific CityNotFoundException.
        assertThrows(CityNotFoundException.class, () -> {
            weatherService.getWeather(nonExistentCityName);
        });
    }

    // Test the scenario where the API server reports an internal error (HTTP 500).
    @Test
    public void getWeather_ShouldThrowWeatherServiceUnavailableException_WhenOpenWeatherApiIsUnavailable(){
        String anyValidCity = "London"; // The city name isn't the focus here

        // Configure mock restTemplate: "WHEN called, simulate an HTTP 500 error."
        when(restTemplate.getForEntity(anyString(), eq(WeatherResponseDto.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)); // Simulate the 500 response

        // Assert that our service method throws the specific WeatherServiceUnavailableException.
        assertThrows(WeatherServiceUnavailableException.class, () -> {
            weatherService.getWeather(anyValidCity);
        });
    }

    @Test
    public void getWeather_ShouldThrowInvalidApiKeyException(){
        String city = "London";

        when(restTemplate.getForEntity(anyString(), eq(WeatherResponseDto.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        InvalidApiKeyException exception = assertThrows(InvalidApiKeyException.class,
        () -> weatherService.getWeather(city));

        assertEquals("Invalid ApiKey, try to change it", exception.getMessage());
    }
}
