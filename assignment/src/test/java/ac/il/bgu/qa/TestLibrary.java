package ac.il.bgu.qa;

import ac.il.bgu.qa.errors.*;
import ac.il.bgu.qa.services.*;;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestLibrary {

    @Mock
    DatabaseService mockDatabaseService;

    @Mock
    ReviewService mockReviewService;

    @Mock
    NotificationService mockNotificationService;
    @Mock
    Library mockLibrary;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        mockLibrary = new Library(mockDatabaseService, mockReviewService);
    }

    @Test
    public void borrowBookSuccessfully_whenGetBookISBMAndUserID() {
        // 1. Arrange
        Book book = new Book("978-3-16-148410-0", "Test Book", "Test Author");
        String userId = "123456789012";


        // 1.2. Stubbing - Define behavior for mockApiClient
        when(mockDatabaseService.getBookByISBN(book.getISBN())).thenReturn(book);
        when(mockDatabaseService.getUserById(userId)).thenReturn(new User("John Doe", userId, mock(NotificationService.class)));

        // 2. Action
        // 2.1. Call the method under test
        mockLibrary.borrowBook(book.getISBN(), userId);

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(book.getISBN());
        verify(mockDatabaseService, times(1)).getUserById(userId);
        verify(mockDatabaseService, times(1)).borrowBook(book.getISBN(), userId);

        // 3.2. Assert the result
        assertTrue(book.isBorrowed());
    }

    @Test
    void registerUser_whenUserAlreadyExists() {
        // Arrange
        String userId = "123456789015";
        User existingUser = new User("John Doe", userId, mock(NotificationService.class));

        // Stubbing - Define behavior for mockDatabaseService
        when(mockDatabaseService.getUserById(userId)).thenReturn(existingUser);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(existingUser));

        // Verify interactions
        verify(mockDatabaseService, times(1)).getUserById(userId);

        // Assert the result
        assertEquals("User already exists.", exception.getMessage());
    }

}
