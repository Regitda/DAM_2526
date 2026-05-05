package com.da.dg2526.api.serviceTests.book;

import com.da.dg2526.api.exceptions.ServiceValidationException;
import com.da.dg2526.api.models.dto.bookEntity.BookNewInputDTO;
import com.da.dg2526.api.models.entities.BookEntity;
import com.da.dg2526.api.models.entities.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.da.dg2526.api.services.ServiceErrorMessages.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookAdditionTests extends AbstractBookServiceTestBase {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void setupBookMocks(BookNewInputDTO book, boolean shouldBookAlreadyExist, Optional<CategoryEntity> categoryThatWillBeFound) {
        when(bookEntityDAO.existsById(book.isbn())).thenReturn(shouldBookAlreadyExist);
        when(categoryEntityDAO.findById(book.category())).thenReturn(categoryThatWillBeFound);
    }

    @Test
    void addNewBook_success() {
        var categoryExists = new CategoryEntity();
        categoryExists.setCode("OTHER");
        categoryExists.setName("Other");

        setupBookMocks(testBookDTO, false, Optional.of(categoryExists));

        when(bookEntityDAO.save(any(BookEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = bookService.addNewBook(testBookDTO);

        assertEquals(testBookDTO.isbn(), result.isbn());
        assertEquals("test", result.title());
        assertEquals(2, result.copies());
        assertEquals("Other", result.category());

        verify(bookEntityDAO).save(any(BookEntity.class));
    }

    @Test
    void addNewBook_bookCategoryInvalid() {
        setupBookMocks(testBookDTO, false, Optional.empty());

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> bookService.addNewBook(testBookDTO));

        assertEquals(bookAddErrorGeneralMessage + bookCategoryDoesNotExist(testBookDTO), exception.getMessage());

        verify(bookEntityDAO, never()).save(any(BookEntity.class));
    }


    @Test
    void addNewBook_bookAlreadyExists() {
        var category = new CategoryEntity();
        category.setCode("OTHER");
        category.setName("Other");

        setupBookMocks(testBookDTO, true, Optional.of(category));

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> bookService.addNewBook(testBookDTO));

        assertEquals(bookAddErrorGeneralMessage + bookAlreadyExists(testBookDTO), exception.getMessage());

        verify(bookEntityDAO, never()).save(any(BookEntity.class));
    }

    @Test
    void addNewBook_bookAlreadyExistsAndCategoryDoesNotExist() {
        setupBookMocks(testBookDTO, true, Optional.empty());

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> bookService.addNewBook(testBookDTO));

        assertEquals(bookAddErrorGeneralMessage + bookAlreadyExists(testBookDTO) + bookCategoryDoesNotExist(testBookDTO), exception.getMessage());

        verify(bookEntityDAO, never()).save(any(BookEntity.class));
    }

    // Tests over the mass import function
    @Test
    void importNewBook_success() {
        var category = new CategoryEntity();
        category.setCode("OTHER");
        category.setName("Other");

        for (var book : testBookDTOList) {
            setupBookMocks(book, false, Optional.of(category));

        }
        when(bookEntityDAO.saveAll(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = bookService.importBooks(testBookDTOList);

        assertEquals(testBookDTOList.size(), result.size());

        verify(bookEntityDAO).saveAll(Mockito.any());

    }

    @Test
    void importNewBooks_oneBookInvalid_failure() {
        var category = new CategoryEntity();
        category.setCode("OTHER");
        category.setName("Other");

        // Setting up mocks
        var invalidBook = testBookDTOList.get(0);
        setupBookMocks(invalidBook, true, Optional.of(category));
        for (int i = 1; i < testBookDTOList.size(); i++) {
            setupBookMocks(testBookDTOList.get(i), false, Optional.of(category));
        }

        // Catching the exception
        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> bookService.importBooks(testBookDTOList));

        // Making sure the error is correct.
        assertEquals(bookImportGeneralMessageError + bookAlreadyExists(invalidBook), exception.getMessage());

        verify(bookEntityDAO, never()).saveAll(Mockito.any());
    }


    @Test
    void importNewBooks_allBooksInvalidAlreadyExists_failure() {
        var category = new CategoryEntity();
        category.setCode("OTHER");
        category.setName("Other");

        // Setting up mocks
        for (var book : testBookDTOList) {
            setupBookMocks(book, true, Optional.of(category));
        }

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> bookService.importBooks(testBookDTOList));

        StringBuilder expectedErrors = new StringBuilder();

        for (var book : testBookDTOList) {
            expectedErrors.append(bookAlreadyExists(book));
        }

        assertEquals(bookImportFullyFailed(testBookDTOList.size(), expectedErrors), exception.getMessage());

        verify(bookEntityDAO, never()).saveAll(Mockito.any());
        verify(bookEntityDAO, never()).save(any(BookEntity.class));
    }

    @Test
    void importNewBooks_allBooksInvalidAlreadyExistsAndCategoryError_failure() {
        // Setting up mocks
        for (var book : testBookDTOList) {
            setupBookMocks(book, true, Optional.empty());
        }

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> bookService.importBooks(testBookDTOList));
        StringBuilder expectedErrors = new StringBuilder();

        for (var book : testBookDTOList) {
            expectedErrors.append(bookAlreadyExists(book)).append(bookCategoryDoesNotExist(book));
        }

        assertEquals(bookImportFullyFailed(testBookDTOList.size(), expectedErrors), exception.getMessage());

        verify(bookEntityDAO, never()).saveAll(Mockito.any());
        verify(bookEntityDAO, never()).save(any(BookEntity.class));
    }
}
