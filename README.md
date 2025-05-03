# OnlyWeather

A simple REST application for retrieving current weather data for cities using the OpenWeatherMap API.

## Technologies
- Java 17
- Spring Boot 3.2.3
- Maven
- Lombok
- RestTemplate
- Thymeleaf

## Features
- Fetch current weather data for any city
- Return simple HTML view using the `/view/{city}` endpoint 
- Comprehensive exception handling with custom error responses
- Weather condition icons based on current weather state
- Clean separation of concerns (Controller, Service, DTO pattern)

## Project Structure

```bash
src/main/java/com/onlyweather/OnlyWeather/
├── config/               # Application configuration
│   └── AppConfig.java    # RestTemplate bean configuration
├── controller/           # REST controllers
│   └── WeatherController.java
├── dto/                  # Data transfer objects
│   ├── MainInfoDto.java
│   ├── WeatherInfoDto.java
│   └── WeatherResponseDto.java
├── enums/            # Enumeration types
│   └── WeatherIcons.java #Mapping of weather icons
├── exception/            # Exception handling
│   ├── CityNotFoundException.java
│   ├── ErrorDetails.java
│   ├── GlobalExceptionHandler.java
│   ├── InvalidApiKeyException.java
│   └── WeatherServiceUnavailableException.java
├── service/              # Business logic
│   └── WeatherService.java
└── OnlyWeatherApplication.java  # Main class
```

## How to run

### Prerequisites
- Java 17 or newer
- Maven
- API key from OpenWeatherMap (free registration required)

### Setup
1. Clone the repository
```bash 
git clone https://github.com/your-username/OnlyWeather.git
cd OnlyWeather
```

2. Create your `application.properties` file:
- Copy the example below to `src/main/resources/application.properties`
- Or rename/copy from `application.properties.example` if available
- Replace with your actual API key
```bash 
spring.application.name=OnlyWeather
openweathermap.api.key=YOUR_API_KEY_HERE
openweathermap.api.url=https://api.openweathermap.org/data/2.5/weather
```

3. Build and run the application 
```bash
mvn spring-boot:run
```

4. The application will be available at: http://localhost:8080

## API Endpoints

|Endpoint|Method|Description|
|--------|------|-----------|
|`/{city}`|GET|Returns current weather data for the specified city|
|`/view/{city}`|GET|Returns the same as `/{city}` but in a HTML view|

For detailed, interactive API documentation, run the application and visit:
http://localhost:8080/swagger-ui

### Example Request
```bash
GET /london
```
OR (to get the result in HTML)
```bash
GET /view/london
```

### Example Response
```json
{
  "name": "London",
  "main": {
    "temp": 15.2
  },
  "weather": [
    {
      "description": "scattered clouds"
    }
  ]
}
```

### Example Response with HTML
![screenshot-onlyweather-html-response](https://github.com/user-attachments/assets/461e5c45-8ab4-4b54-888a-467be62cd8c0)


## Future Development Plans
1. **Adding API documentation (Swagger/OpenAPI)**
   - Interactive API documentation
   - Endpoint testing capability

2. **Implementing unit and integration tests**
   - Service layer unit tests
   - Controller integration tests
   - Exception handling tests

3. **Implementing response caching**
   - Reducing external API calls
   - Improving response times

4. **Implementing scheduled weather updates**
   - Automatic background fetching of weather data
   - Support for configurable update intervals

5. **Extending DTO fields**
   - Adding more weather details (humidity, pressure, wind)
   - Including forecast information

6. **Adding user preferences system**
   - Endpoints for city subscriptions
   - Configurable update frequencies
   - User settings storage

## Author 
Michal-D2001

## License 
This project is a personal portfolio project created for learning and demonstration purposes.
