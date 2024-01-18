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

    @Mock
    Book mockBook;

    @Mock
    User mockUser;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        mockLibrary = new Library(mockDatabaseService, mockReviewService);
//        mockBook = new Book("978-3-16-148410-0", "Test Book", "Test Author");
        mockBook = Mockito.mock(Book.class);
        mockUser = Mockito.mock(User.class);
    }

    @AfterEach
    public void cleanup() {
        // Clean up resources or delete the object
        mockBook = null;
    }

    //Test borrowBook functionality
    @Test
    public void borrowBookSuccessfully_whenGetBookISBMAndUserID() {
        // 1. Arrange
        String userId = "123456789012";

        // 1.2. Stubbing - Define behavior for mockApiClient
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
//        when(mockBook.isBorrowed()).thenReturn(true);

        // 2. Action
        // 2.1. Call the method under test
        assertDoesNotThrow(() -> mockLibrary.borrowBook(mockBook.getISBN(), userId));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(mockBook.getISBN());
        verify(mockDatabaseService, times(1)).getUserById(userId);
        verify(mockDatabaseService, times(1)).borrowBook(mockBook.getISBN(), userId);
        verifyNoMoreInteractions(mockDatabaseService);
//
        // 3.2. Assert the result
//        assertTrue(mockBook.isBorrowed());
    }

    @Test
    void borrowBook_withIncorrectISBNAndUserID() {
        // Arrange
        String incorrectISBN = "1234567890123"; // Incorrect ISBN
        String userId = "123456789012";

        // Stubbing - Define behavior for mockDatabaseService

        // Act and Assert
        IllegalArgumentException exception =  assertThrows(IllegalArgumentException.class,
                () -> mockLibrary.borrowBook(incorrectISBN, userId));

        // Verify interactions
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    void borrowBook_withISBNAndUserID_returnBookNull() {
        // Arrange
        String userId = "123456789012";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        BookNotFoundException exception =  assertThrows(BookNotFoundException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), userId));

        // Verify interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(mockBook.getISBN());
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Book not found!", exception.getMessage());
    }

    @Test
    void borrowBook_withISBNAndUserIDNotFound() {
        // Arrange
        String incorrectUserID = "123456789012"; // Incorrect UserID

        // Stubbing - Define behavior for mockDatabaseService
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(incorrectUserID)).thenReturn(null);

        // Act and Assert
        UserNotRegisteredException exception =  assertThrows(UserNotRegisteredException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), incorrectUserID));

        // Verify interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(mockBook.getISBN());
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    void borrowBook_withISBNAndInvalidUserID() {
        // Arrange
        String incorrectUserID = "123"; // Incorrect UserID

        // Stubbing - Define behavior for mockDatabaseService
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);

        // Act and Assert
        IllegalArgumentException exception =  assertThrows(IllegalArgumentException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), incorrectUserID));

        // Verify interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(mockBook.getISBN());
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    void borrowBook_withISBNAndUserID_returnBookAlreadyBorrow() {
        // Arrange
        String userId = "123456789012";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        when(mockBook.isBorrowed()).thenReturn(true);

        // Act and Assert
        BookAlreadyBorrowedException exception =  assertThrows(BookAlreadyBorrowedException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), userId));

        // Verify interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(mockBook.getISBN());
        verify(mockDatabaseService, times(1)).getUserById(userId);
        verify(mockDatabaseService, times(0)).borrowBook(mockBook.getISBN(), userId);
        verify(mockBook,times(0)).borrow();
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Book is already borrowed!", exception.getMessage());
    }

    //Test registerUser functionality
    @Test
    void registerUserSuccessfully_whenGetValidUser() {
        // Arrange
        String userName = "Or Saada";
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockUser.getId()).thenReturn(userId);
        when(mockUser.getName()).thenReturn(userName);
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);

        // Act and Assert;
        assertDoesNotThrow(() -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        verify(mockDatabaseService, times(1)).getUserById(mockUser.getId());
        verify(mockDatabaseService, times(1)).registerUser(mockUser.getId(),mockUser);
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
    }
    @Test
    void registerUser_whenUserAlreadyExists() {
        // Arrange
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockUser.getId()).thenReturn(userId);
        when(mockUser.getName()).thenReturn("Or Saada");
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
        when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        verify(mockDatabaseService, times(1)).getUserById(userId);
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("User already exists.", exception.getMessage());
    }

    @Test
    void registerUser_whenUserIsNull() {
        // Arrange
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(null));

        // Verify interactions
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Invalid user.", exception.getMessage());
    }

    @Test
    void registerUser_whenUserIdInvalid() {
        // Arrange
        String userId = "123";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockUser.getId()).thenReturn(userId);
        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    void registerUser_whenUserNameInvalid() {
        // Arrange
        String userName = "";
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockUser.getId()).thenReturn(userId);
        when(mockUser.getName()).thenReturn(userName);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Invalid user name.", exception.getMessage());
    }

    @Test
    void registerUser_whenUserNotificationServiceInvalid() {
        // Arrange
        String userName = "Or Saada";
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockUser.getId()).thenReturn(userId);
        when(mockUser.getName()).thenReturn(userName);
        when(mockUser.getNotificationService()).thenReturn(null);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Invalid notification service.", exception.getMessage());
    }

    // Test isAuthorValid functionality
    @Test
    void addBookValidSuccessfully_whenGetBook() {
        // Arrange
        String authorName = "Smadar Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockBook.getISBN()).thenReturn(bookISBN);
        when(mockBook.getAuthor()).thenReturn(authorName);
        when(mockBook.getTitle()).thenReturn(bookTitle);
        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        assertDoesNotThrow(() -> mockLibrary.addBook(mockBook));

        // Verify interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(mockBook.getISBN());
        verify(mockDatabaseService, times(1)).addBook(mockBook.getISBN(),mockBook);
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result

    }

    @Test
    void addBookValid_whenGetBookAlreadyExist() {
        // Arrange
        String authorName = "Smadar Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockBook.getISBN()).thenReturn(bookISBN);
        when(mockBook.getAuthor()).thenReturn(authorName);
        when(mockBook.getTitle()).thenReturn(bookTitle);
        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        verify(mockDatabaseService, times(1)).getBookByISBN(mockBook.getISBN());
        verify(mockDatabaseService, times(0)).addBook(mockBook.getISBN(),mockBook);
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Book already exists.", exception.getMessage());

    }

    @Test
    void addBookValid_whenGetBorrowedBook() {
        // Arrange
        String authorName = "Smadar Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockBook.getISBN()).thenReturn(bookISBN);
        when(mockBook.getAuthor()).thenReturn(authorName);
        when(mockBook.getTitle()).thenReturn(bookTitle);
        when(mockBook.isBorrowed()).thenReturn(true);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        verify(mockDatabaseService, times(0)).getBookByISBN(mockBook.getISBN());
        verify(mockDatabaseService, times(0)).addBook(mockBook.getISBN(),mockBook);
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Book with invalid borrowed state.", exception.getMessage());

    }

    @Test
    void addBookValid_whenGetInvalidAuthor() {
        // Arrange
        String authorName = "";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockBook.getISBN()).thenReturn(bookISBN);
        when(mockBook.getAuthor()).thenReturn(authorName);
        when(mockBook.getTitle()).thenReturn(bookTitle);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        verify(mockDatabaseService, times(0)).getBookByISBN(mockBook.getISBN());
        verify(mockDatabaseService, times(0)).addBook(mockBook.getISBN(),mockBook);
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Invalid author.", exception.getMessage());

    }

    @Test
    void addBookValid_whenGetInvalidTitle() {
        // Arrange
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "";

        // Stubbing - Define behavior for mockDatabaseService
        when(mockBook.getISBN()).thenReturn(bookISBN);
        when(mockBook.getTitle()).thenReturn(bookTitle);

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        verify(mockDatabaseService, times(0)).getBookByISBN(mockBook.getISBN());
        verify(mockDatabaseService, times(0)).addBook(mockBook.getISBN(),mockBook);
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Invalid title.", exception.getMessage());

    }

    @Test
    void addBookValid_whenBookIsNull() {
        // Arrange

        // Stubbing - Define behavior for mockDatabaseService

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(null));

        // Verify interactions
        verify(mockDatabaseService, times(0)).getBookByISBN(mockBook.getISBN());
        verify(mockDatabaseService, times(0)).addBook(mockBook.getISBN(),mockBook);
        verifyNoMoreInteractions(mockDatabaseService);

        // Assert the result
        assertEquals("Invalid book.", exception.getMessage());

    }

}
