package com.da.dg2526.api.serviceTests.book;

import com.da.dg2526.api.models.dao.*;
import com.da.dg2526.api.models.dto.bookEntity.BookNewInputDTO;
import com.da.dg2526.api.models.entities.BookEntity;
import com.da.dg2526.api.models.entities.UserEntity;
import com.da.dg2526.api.services.BookService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
abstract class AbstractBookServiceTestBase {

    @Mock
    protected IBookEntityDAO bookEntityDAO;
    @Mock
    protected ICategoryEntityDAO categoryEntityDAO;
    @Mock
    protected IUserEntityDAO userEntityDAO;
    @Mock
    protected ILendingEntityDAO lendingEntityDAO;
    @Mock
    protected IReservationEntityDAO reservationEntityDAO;

    @InjectMocks
    protected BookService bookService;

    protected final BookNewInputDTO testBookDTO = new BookNewInputDTO("testBookId", "test", 2, "test book if seen on production something went wrong", "test publisher", "OTHER");
    protected final String testUserId = "testUserId";
    protected final String testBookId = "testBookId";

    protected UserEntity createUser() {
        var user = new UserEntity();
        user.setName("Test User");
        user.setCode(testUserId);
        return user;
    }

    protected BookEntity createBook() {
        var book = new BookEntity();
        book.setCopies(2);
        book.setIsbn(testBookId);
        book.setTitle("Test Book");
        return book;
    }

    protected void mockBookAndUser(BookEntity book, UserEntity user) {
        when(bookEntityDAO.findById(testBookId)).thenReturn(Optional.of(book));
        when(userEntityDAO.findById(testUserId)).thenReturn(Optional.of(user));
    }

    // Repeated book tests
    protected void mockBookNotFound() {
        when(bookEntityDAO.findById(testBookId)).thenReturn(Optional.empty());
    }
    // Repeated user tests
    protected void mockUserNotFound() {
        when(bookEntityDAO.findById(testBookId)).thenReturn(Optional.of(new BookEntity()));
        when(userEntityDAO.findById(testUserId)).thenReturn(Optional.empty());
    }

}
