package ac.il.bgu.qa;

import ac.il.bgu.qa.services.*;;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestLibrary {

    @Mock
    DatabaseService mockDatabaseService;

    @Mock
    ReviewService mockReviewService;

    @Mock
    Library mockLibrary;

    @BeforeEach
    void init() { MockitoAnnotations.openMocks(this); }

    @Test
    public void registerSuccessfullyUser_whenGetUser() {
        // 1. Arrange
        // 1.1. Create an instance of WeatherService with mocks
//        WeatherService weatherService = new WeatherService(mockWeatherApiClient, mockLogger, mockHistory);

        // 1.2. Stubbing - Define behavior for mockApiClient
//        when(mockWeatherApiClient.fetchWeather("Berlin")).thenReturn("Rainy");

        // 2. Action
        // 2.1. Call the method under test
//        String forecast = weatherService.getWeatherForecast("Berlin");

        // 3. Assertion
        // 3.1. Verify interactions
//        verify(mockWeatherApiClient).fetchWeather("Berlin");
//        verify(mockLogger).log("Weather data retrieved for Berlin");
//        verify(mockLogger, never()).error(anyString());
//        verify(mockHistory).add("Berlin");

        // 3.2. Assert the result
//        assertEquals("Rainy", forecast);
    }
}
