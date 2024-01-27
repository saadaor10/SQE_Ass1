package ac.il.bgu.qa;

import ac.il.bgu.qa.errors.*;
import ac.il.bgu.qa.services.*;;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

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
        mockLibrary = Mockito.spy(new Library(mockDatabaseService, mockReviewService));
//        mockLibrary = new Library(mockDatabaseService, mockReviewService);
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
    public void givenSuccessfulBorrowBook_whenBorrowBook_thenNoExceptionThrown() {
        // 1. Arrange
        String userId = "123456789012";
        String ISBN = "978-3-16-148410-0";

        // 1.2. Stubbing - Define behavior for mockApiClient
        Mockito.when(mockBook.getISBN()).thenReturn(ISBN);
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);

        // 2. Action
        // 2.1. Call the method under test
        Assertions.assertDoesNotThrow(() -> mockLibrary.borrowBook(mockBook.getISBN(), mockUser.getId()));

        // 3. Assertion
        // 3.1. Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getUserById(userId);
        Mockito.verify(mockDatabaseService, Mockito.times(1)).borrowBook(mockBook.getISBN(), userId);

        // 3.2. Assert the result
    }

    @Test
    public void givenWrongSumISBNAndUserID_whenBorrowBook_thenThrownExceptionInvalidISBN() {
        // 1. Arrange
        String userId = "123456789012";
        String incorrectISBN = "978-3-16-148410-5";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(incorrectISBN);
        Mockito.when(mockUser.getId()).thenReturn(userId);

        // Act and Assert
        IllegalArgumentException exception =  Assertions.assertThrows(IllegalArgumentException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).borrowBook(mockBook.getISBN(), mockUser.getId());

        // 3.2. Assert the result
        Assertions.assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    void givenLettersInISBNAndUserID_whenBorrowBook_thenThrownExceptionInvalidISBN() {
        // Arrange
        String incorrectISBN = "AAAAAAAAAAAAA"; // Incorrect ISBN
        String userId = "123456789012";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(incorrectISBN);
        Mockito.when(mockUser.getId()).thenReturn(userId);


        // Act and Assert
        IllegalArgumentException exception =  Assertions.assertThrows(IllegalArgumentException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).borrowBook(mockBook.getISBN(), mockUser.getId());


        // Assert the result
        Assertions.assertEquals("Invalid ISBN.", exception.getMessage());
    }
    @Test
    void givenTooShortISBNAndUserID_whenBorrowBook_thenThrownExceptionInvalidISBN() {
        // Arrange
        String incorrectISBN = "1234"; // Incorrect ISBN
        String userId = "123456789012";

        // Stubbing - Define behavior for mockDatabaseService

        // Act and Assert
        IllegalArgumentException exception =  Assertions.assertThrows(IllegalArgumentException.class,
                () -> mockLibrary.borrowBook(incorrectISBN, userId));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(userId);
        Mockito.verify(mockDatabaseService, Mockito.times(0)).borrowBook(mockBook.getISBN(), userId);


        // Assert the result
        Assertions.assertEquals("Invalid ISBN.", exception.getMessage());
    }
    @Test
    void givenIncorrectISBNAndUserID_whenBorrowBook_thenThrownExceptionInvalidISBN() {
        // Arrange
        String incorrectISBN = "1234567890123"; // Incorrect ISBN
        String userId = "123456789012";

        // Stubbing - Define behavior for mockDatabaseService

        // Act and Assert
        IllegalArgumentException exception =  Assertions.assertThrows(IllegalArgumentException.class,
                () -> mockLibrary.borrowBook(incorrectISBN, userId));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(userId);
        Mockito.verify(mockDatabaseService, Mockito.times(0)).borrowBook(mockBook.getISBN(), userId);


        // Assert the result
        Assertions.assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    void givenNotFoundBook_whenBorrowBook_thenThrownExceptionBookNotFound() {
        // Arrange
        String userId = "123456789012";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        BookNotFoundException exception =  Assertions.assertThrows(BookNotFoundException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), userId));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(userId);
        Mockito.verify(mockDatabaseService, Mockito.times(0)).borrowBook(mockBook.getISBN(), userId);

        // Assert the result
        Assertions.assertEquals("Book not found!", exception.getMessage());
    }

    @Test
    void givenUserIDNotFound_whenBorrowBook_thenThrownExceptionUserNotFound() {
        // Arrange
        String incorrectUserID = "123456789012"; // Incorrect UserID

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(incorrectUserID)).thenReturn(null);

        // Act and Assert
        UserNotRegisteredException exception = Assertions.assertThrows(UserNotRegisteredException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), incorrectUserID));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).borrowBook(mockBook.getISBN(), mockUser.getId());

        // Assert the result
        Assertions.assertEquals("User not found!", exception.getMessage());
    }

    @Test
    void givenShorterUserID_whenBorrowBook_thenThrownExceptionInvalidUserID() {
        // Arrange
        String incorrectUserID = null; // Incorrect UserID

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        Mockito.when(mockUser.getId()).thenReturn(incorrectUserID);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(incorrectUserID)).thenReturn(mockUser);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).borrowBook(mockBook.getISBN(), mockUser.getId());

        // Assert the result
        Assertions.assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    void givenInvalidUserID_whenBorrowBook_thenThrownExceptionInvalidUserID() {
        // Arrange
        String incorrectUserID = "123"; // Incorrect UserID

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);

        // Act and Assert
        IllegalArgumentException exception =  Assertions.assertThrows(IllegalArgumentException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), incorrectUserID));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).borrowBook(mockBook.getISBN(), mockUser.getId());

        // Assert the result
        Assertions.assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    void givenBorrowedBook_whenBorrowBook_thenThrownExceptionBookAlreadyBorrow() {
        // Arrange
        String userId = "123456789012";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn("978-3-16-148410-0");
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(userId)).thenReturn(mockUser);
        Mockito.when(mockBook.isBorrowed()).thenReturn(true);

        // Act and Assert
        BookAlreadyBorrowedException exception =  Assertions.assertThrows(BookAlreadyBorrowedException.class,
                () -> mockLibrary.borrowBook(mockBook.getISBN(), userId));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getUserById(userId);
        Mockito.verify(mockDatabaseService, Mockito.times(0)).borrowBook(mockBook.getISBN(), userId);
        Mockito.verify(mockBook,Mockito.times(0)).borrow();

        // Assert the result
        Assertions.assertEquals("Book is already borrowed!", exception.getMessage());
    }

    //Test registerUser functionality
    @Test
    void givenSuccessfulUser_whenRegisterUser_thenNoExceptionThrown() {
        // Arrange
        String userName = "Or Saada";
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockUser.getName()).thenReturn(userName);
        Mockito.when(mockUser.getNotificationService()).thenReturn(mockNotificationService);

        // Act and Assert;
        Assertions.assertDoesNotThrow(() -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).registerUser(mockUser.getId(),mockUser);

        // Assert the result
    }
    @Test
    void givenExistingUser_whenRegisterUser_thenThrownException() {
        // Arrange
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockUser.getName()).thenReturn("Or Saada");
        Mockito.when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
        Mockito.when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getUserById(userId);
        Mockito.verify(mockDatabaseService, Mockito.times(0)).registerUser(mockUser.getId(),mockUser);

        // Assert the result
        Assertions.assertEquals("User already exists.", exception.getMessage());
    }

    @Test
    void givenNullUser_whenRegisterUser_thenThrownExceptionInvalidUser() {
        // Arrange
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(null));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).registerUser(mockUser.getId(),mockUser);

        // Assert the result
        Assertions.assertEquals("Invalid user.", exception.getMessage());
    }

    @Test
    void givenInvalidId_whenRegisterUser_thenThrownException() {
        // Arrange
        String userId = "123";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).registerUser(mockUser.getId(),mockUser);

        // Assert the result
        Assertions.assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    void givenInvalidName_whenRegisterUser_thenThrownException() {
        // Arrange
        String userName = "";
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockUser.getName()).thenReturn(userName);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).registerUser(mockUser.getId(),mockUser);

        // Assert the result
        Assertions.assertEquals("Invalid user name.", exception.getMessage());
    }

    @Test
    void givenNullUserName_whenRegisterUser_thenThrownException() {
        // Arrange
        String userName = null;
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockUser.getName()).thenReturn(userName);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).registerUser(mockUser.getId(),mockUser);

        // Assert the result
        Assertions.assertEquals("Invalid user name.", exception.getMessage());
    }

    @Test
    void givenUserNotificationServiceInvalid_whenRegisterUser_thenThrownException() {
        // Arrange
        String userName = "Or Saada";
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockUser.getName()).thenReturn(userName);
        Mockito.when(mockUser.getNotificationService()).thenReturn(null);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.registerUser(mockUser));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).registerUser(mockUser.getId(),mockUser);

        // Assert the result
        Assertions.assertEquals("Invalid notification service.", exception.getMessage());
    }

    // Test isAuthorValid functionality
    @Test
    void givenSuccessfulBook_whenAddBook_thenNoExceptionThrown() {
        // Arrange
        String authorName = "Smadar Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);
        Mockito.when(mockBook.isBorrowed()).thenReturn(false);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        Assertions.assertDoesNotThrow(() -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result

    }

    @Test
    void givenExistingBook_whenAddBook_thenThrownException() {
        // Arrange
        String authorName = "Smadar Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);
        Mockito.when(mockBook.isBorrowed()).thenReturn(false);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Book already exists.", exception.getMessage());
    }

    @Test
    void giveBorrowedBook_whenAddBook_thenThrownException() {
        // Arrange
        String authorName = "Smadar Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);
        Mockito.when(mockBook.isBorrowed()).thenReturn(true);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Book with invalid borrowed state.", exception.getMessage());

    }

    @Test
    void givenInvalidAuthor_whenAddBook_thenThrownException() {
        // Arrange
        String authorName = "";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Invalid author.", exception.getMessage());

    }

    @Test
    void givenNullAuthor_whenAddBook_thenThrownException() {
        // Arrange
        String authorName = null;
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Invalid author.", exception.getMessage());
    }

    @Test
    void givenAuthorValidNameOption2_whenAddBook_thenNoExceptionThrown() {
        // Arrange
        String authorName = "Smadar-Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);
        Mockito.when(mockBook.isBorrowed()).thenReturn(false);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        Assertions.assertDoesNotThrow(() -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result

    }

    @Test
    void givenAuthorValidNameOption3_whenAddBook_thenNoExceptionThrown() {
        // Arrange
        String authorName = "S.M Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);
        Mockito.when(mockBook.isBorrowed()).thenReturn(false);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        Assertions.assertDoesNotThrow(() -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result

    }

    @Test
    void givenAuthorValidNameOption4_whenAddBook_thenNoExceptionThrown() {
        // Arrange
        String authorName = "S' Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);
        Mockito.when(mockBook.isBorrowed()).thenReturn(false);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        Assertions.assertDoesNotThrow(() -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result

    }
    @Test
    void givenNotAlphabeticOnlyAuthor_whenAddBook_thenThrownException() {
        // Arrange
        String authorName = "5madar Shi33";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Invalid author.", exception.getMessage());
    }

    @Test
    void givenNotAlphabeticOnlyAuthorOption2_whenAddBook_thenThrownException() {
        // Arrange
        String authorName = "smada''Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Invalid author.", exception.getMessage());
    }

    @Test
    void givenNotAlphabeticOnlyAuthorOption3_whenAddBook_thenThrownException() {
        // Arrange
        String authorName = "smada--Shir";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Invalid author.", exception.getMessage());
    }

    @Test
    void givenAuthorWitSpecialChar_whenAddBook_thenThrownException() {
        // Arrange
        String authorName = "Smadar Shir@@";
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "Cinderella";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getAuthor()).thenReturn(authorName);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Invalid author.", exception.getMessage());
    }
    @Test
    void givenInvalidTitle_whenAddBook_thenThrownException() {
        // Arrange
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = "";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Invalid title.", exception.getMessage());
    }

    @Test
    void givenNullTitle_whenAddBook_thenThrownException() {
        // Arrange
        String bookISBN = "978-3-16-148410-0";
        String bookTitle = null;

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockBook.getTitle()).thenReturn(bookTitle);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(mockBook));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Invalid title.", exception.getMessage());
    }

    @Test
    void givenNullBook_whenAddBook_thenThrownException() {
        // Arrange

        // Stubbing - Define behavior for mockDatabaseService

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.addBook(null));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).addBook(mockBook.getISBN(),mockBook);

        // Assert the result
        Assertions.assertEquals("Invalid book.", exception.getMessage());
    }

    // Notify with book reviews
    @Test
    void givenInvalidISBN_whenNotifyUserWithBookReviews_thenThrowIllegalArgumentException() {
        // Arrange
        String bookISBN = "12345";
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockUser.getId()).thenReturn(userId);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService,Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());


        // Assert the result
        Assertions.assertEquals("Invalid ISBN.", exception.getMessage());

    }

    @Test
    void givenUserIdNull_whenNotifyUserWithBookReviews_thenThrowIllegalArgumentException() {
        // Arrange
        String userId = null;
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(bookISBN);
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());

        // Assert the result
        Assertions.assertEquals("Invalid user Id.", exception.getMessage());

    }

    @Test
    void givenUserIdNot12Digits_whenNotifyUserWithBookReviews_thenThrowIllegalArgumentException() {
        // Arrange
        String userId = "1234567890";
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(bookISBN);
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());

        // Assert the result
        Assertions.assertEquals("Invalid user Id.", exception.getMessage());
    }

    @Test
    void givenBookIsNull_whenNotifyUserWithBookReviews_thenThrowBookNotFoundException() {
        // Arrange
        String userId = "123456789015";
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        BookNotFoundException exception = Assertions.assertThrows(BookNotFoundException.class, () -> mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getUserById(mockUser.getId());

        // Assert the result
        Assertions.assertEquals("Book not found!", exception.getMessage());
    }

    @Test
    void givenUserIsNull_whenNotifyUserWithBookReviews_thenThrowUserNotRegisteredException() {
        // Arrange
        String userId = "123456789015";
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(null);

        // Act and Assert
        UserNotRegisteredException exception = Assertions.assertThrows(UserNotRegisteredException.class, () -> mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getUserById(mockUser.getId());

        // Assert the result
        Assertions.assertEquals("User not found!", exception.getMessage());
    }

    @Test
    void givenReviewsIsEmpty_whenNotifyUserWithBookReviews_thenThrowNoReviewsFoundException() {
        // Arrange
        String userId = "123456789015";
        String bookISBN = "978-3-16-148410-0";
        List<String> reviews = new ArrayList<>();

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        Mockito.when(mockReviewService.getReviewsForBook(mockBook.getISBN())).thenReturn(reviews);

        // Act and Assert
        NoReviewsFoundException exception = Assertions.assertThrows(NoReviewsFoundException.class, () -> mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getUserById(mockUser.getId());
        Mockito.verify(mockReviewService, Mockito.times(1)).getReviewsForBook(mockBook.getISBN());
        Mockito.verify(mockReviewService, Mockito.times(1)).close();

        // Assert the result
        Assertions.assertEquals("No reviews found!", exception.getMessage());
    }


    @Test
    void givenReviewsIsNull_whenNotifyUserWithBookReviews_thenThrowNoReviewsFoundException() {
        // Arrange
        String userId = "123456789015";
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        Mockito.when(mockReviewService.getReviewsForBook(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        NoReviewsFoundException exception = Assertions.assertThrows(NoReviewsFoundException.class, () -> mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getUserById(mockUser.getId());
        Mockito.verify(mockReviewService, Mockito.times(1)).getReviewsForBook(mockBook.getISBN());
        Mockito.verify(mockReviewService, Mockito.times(1)).close();

        // Assert the result
        Assertions.assertEquals("No reviews found!", exception.getMessage());
    }


    @Test
    void givenReviewFetchingFails_whenNotifyUserWithBookReviews_thenThrowReviewServiceUnavailableException() {
        // Arrange
        String userId = "123456789015";
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService and mockReviewService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        Mockito.when(mockReviewService.getReviewsForBook(mockBook.getISBN())).thenThrow(new ReviewException(""));

        // Act and Assert
        ReviewServiceUnavailableException exception = Assertions.assertThrows(ReviewServiceUnavailableException.class, () -> mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getUserById(mockUser.getId());
        Mockito.verify(mockReviewService, Mockito.times(1)).getReviewsForBook(mockBook.getISBN());
        Mockito.verify(mockReviewService, Mockito.times(1)).close();

        // Assert the result
        Assertions.assertEquals("Review service unavailable!", exception.getMessage());
    }


    @Test
    void given5UnsuccessfulAttempts_whenNotifyUserWithBookReviews_thenThrowNotificationFailedException() {
        // Arrange
        String userId = "123456789015";
        String bookISBN = "978-3-16-148410-0";
        int maxAttempts = 5;

        // Stubbing - Define behavior for mockDatabaseService, mockReviewService, and mockUser
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        Mockito.when(mockReviewService.getReviewsForBook(mockBook.getISBN())).thenReturn(List.of("Review A", "Review B"));
        Mockito.doThrow(new NotificationException("Notification failed!"))
                .when(mockUser).sendNotification(Mockito.anyString());
        // Act and Assert
        Assertions.assertThrows(NotificationException.class,
                () -> mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId()));

        // Verify that sendNotification was retried 5 times
        Mockito.verify(mockUser, Mockito.times(maxAttempts)).sendNotification(Mockito.anyString());
    }


    @Test
    void given2UnsuccessfulAttemptsThenSuccessful_whenNotifyUserWithBookReviews_thenSuccessfulNotificationMessage() {
        // Arrange
        String userId = "123456789015";
        String bookISBN = "978-3-16-148410-0";
        //int maxAttempts = 5;

        // Stubbing - Define behavior for mockDatabaseService, mockReviewService, and mockUser
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        Mockito.when(mockReviewService.getReviewsForBook(mockBook.getISBN())).thenReturn(List.of("Review A", "Review B"));
        Mockito.doThrow(new NotificationException("Notification failed!")).doThrow(new NotificationException("Notification failed!"))
                .doNothing().when(mockUser).sendNotification(Mockito.anyString());
        //!!!

        // Act and Assert
        mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId());

        // Verify that sendNotification was retried 5 times
        Mockito.verify(mockUser, Mockito.times(3)).sendNotification(Mockito.anyString());
    }


    @Test
    void givenSuccessfulReviewsList_whenNotifyUserWithBookReviews_thenSuccessfulNotificationMessage() {
        // Arrange
        String userId = "123456789015";
        String bookISBN = "978-3-16-148410-0";
        //int maxAttempts = 5;

        // Stubbing - Define behavior for mockDatabaseService, mockReviewService, and mockUser
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockDatabaseService.getUserById(mockUser.getId())).thenReturn(mockUser);
        Mockito.when(mockReviewService.getReviewsForBook(mockBook.getISBN())).thenReturn(List.of("Review A", "Review B"));
        Mockito.doNothing().when(mockUser).sendNotification(Mockito.anyString());

        // Act
        mockLibrary.notifyUserWithBookReviews(mockBook.getISBN(), mockUser.getId());

        // Assert
        Mockito.verify(mockUser, Mockito.times(1)).sendNotification(Mockito.anyString());
    }



    // ReturnBook Tests

    @Test
    void givenInvalidISBN_whenReturnBook_thenThrowIllegalArgumentException() {
        // Arrange
        String bookISBN = "12345";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.returnBook(mockBook.getISBN()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).returnBook(mockBook.getISBN());


        // Assert the result
        Assertions.assertEquals("Invalid ISBN.", exception.getMessage());

    }

    @Test
    void givenBookIsNull_whenReturnBook_thenThrowBookNotFoundException() {
        // Arrange
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        BookNotFoundException exception = Assertions.assertThrows(BookNotFoundException.class, () -> mockLibrary.returnBook(mockBook.getISBN()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).returnBook(mockBook.getISBN());

        // Assert the result
        Assertions.assertEquals("Book not found!", exception.getMessage());

    }

    @Test
    void givenBookWasntBorrowed_whenReturnBook_thenThrowBookNotBorrowedException() {
        // Arrange
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockBook.isBorrowed()).thenReturn(false);

        // Act and Assert
        BookNotBorrowedException exception =  Assertions.assertThrows(BookNotBorrowedException.class,
                () -> mockLibrary.returnBook(mockBook.getISBN()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockDatabaseService, Mockito.times(0)).returnBook(mockBook.getISBN());

        // Assert the result
        Assertions.assertEquals("Book wasn't borrowed!", exception.getMessage());
    }

    @Test
    void givenValid_whenReturnBook_thenReturnBookSuccessfully() {
        // Arrange
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockBook.isBorrowed()).thenReturn(true);

        // Act and Assert
        Assertions.assertDoesNotThrow(() -> mockLibrary.returnBook(mockBook.getISBN()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());
        Mockito.verify(mockBook, Mockito.times(1)).returnBook();
        Mockito.verify(mockDatabaseService, Mockito.times(1)).returnBook(mockBook.getISBN());
        //!!!

        // Assert the result
    }


    // Test getBookByISBN functionality

    @Test
    void givenInvalidISBN_whenGetBookByISBN_thenThrowIllegalArgumentException() {
        // Arrange
        String bookISBN = "12345";
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockUser.getId()).thenReturn(userId);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.getBookByISBN(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());

        // Assert the result
        Assertions.assertEquals("Invalid ISBN.", exception.getMessage());
    }

    @Test
    void givenUserIdNull_whenGetBookByISBN_thenThrowIllegalArgumentException() {
        // Arrange
        String userId = null;
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.getBookByISBN(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());


        // Assert the result
        Assertions.assertEquals("Invalid user Id.", exception.getMessage());

    }

    @Test
    void givenUserIdNot12Digits_whenGetBookByISBN_thenThrowIllegalArgumentException() {
        // Arrange
        String userId = "1234567890";
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);

        // Act and Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mockLibrary.getBookByISBN(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(0)).getBookByISBN(mockBook.getISBN());


        // Assert the result
        Assertions.assertEquals("Invalid user Id.", exception.getMessage());
    }


    @Test
    void givenBookIsNull_whenGetBookByISBN_thenThrowBookNotFoundException() {
        // Arrange
        String bookISBN = "978-3-16-148410-0";
        String userId = "123456789015";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(null);

        // Act and Assert
        BookNotFoundException exception = Assertions.assertThrows(BookNotFoundException.class, () -> mockLibrary.getBookByISBN(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());

        // Assert the result
        Assertions.assertEquals("Book not found!", exception.getMessage());

    }

    @Test
    void givenBookAlreadyBorrowed_whenGetBookByISBN_thenThrowBookAlreadyBorrowedException() {
        // Arrange
        String userId = "123456789012";
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockBook.isBorrowed()).thenReturn(true);

        // Act and Assert
        BookAlreadyBorrowedException exception =  Assertions.assertThrows(BookAlreadyBorrowedException.class,
                () -> mockLibrary.getBookByISBN(mockBook.getISBN(), mockUser.getId()));

        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());

        // Assert the result
        Assertions.assertEquals("Book was already borrowed!", exception.getMessage());
    }

    @Test
    void givenNotificationFail_whenGetBookByISBN_thenReturnBookSuccessfully() {
        // Arrange
        String userId = "123456789012";
        String bookISBN = "978-3-16-148410-0";

        // Stubbing - Define behavior for mockDatabaseService
        Mockito.when(mockBook.getISBN()).thenReturn(bookISBN);
        Mockito.when(mockUser.getId()).thenReturn(userId);
        Mockito.when(mockDatabaseService.getBookByISBN(mockBook.getISBN())).thenReturn(mockBook);
        Mockito.when(mockBook.isBorrowed()).thenReturn(false);

        Mockito.doThrow(new NotificationException("Notification failed!"))
                .when(mockLibrary).notifyUserWithBookReviews(bookISBN, userId);

        // Act and Assert
        Book returnedBook = mockLibrary.getBookByISBN(mockBook.getISBN(), mockUser.getId());


        // Verify interactions
        Mockito.verify(mockDatabaseService, Mockito.times(1)).getBookByISBN(mockBook.getISBN());

        // Assert the result
        Assertions.assertEquals(mockBook, returnedBook);
    }

}