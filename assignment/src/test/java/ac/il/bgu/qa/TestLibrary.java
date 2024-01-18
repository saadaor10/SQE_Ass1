package ac.il.bgu.qa;

import ac.il.bgu.qa.services.*;;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void init() {
        MockitoAnnotations.openMocks(this);
        mockLibrary = new Library(mockDatabaseService, mockReviewService);
    }

    @Test
    public void registerSuccessfullyUser_whenGetUser() {
        // 1. Arrange
        Book book = new Book("978-3-16-148410-0", "Test Book", "Test Author");
        String userId = "123456789012";


        // 1.2. Stubbing - Define behavior for mockApiClient
        when(mockDatabaseService.getBookByISBN(book.getISBN())).thenReturn(book);
        when(mockDatabaseService.getUserById(userId)).thenReturn(new User("John Doe", userId, mock(NotificationService.class)));
//
//        // Act

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
}
