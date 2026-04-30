package com.da.dg2526.api.services;

import com.da.dg2526.api.exceptions.ServiceValidationException;
import com.da.dg2526.api.models.dao.IBookEntityDAO;
import com.da.dg2526.api.models.dao.ICategoryEntityDAO;
import com.da.dg2526.api.models.dao.IUserEntityDAO;
import com.da.dg2526.api.models.dto.bookEntity.BookNewInputResultDTO;
import com.da.dg2526.api.models.dto.bookEntity.BookNewInputDTO;
import com.da.dg2526.api.models.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {


    private final IBookEntityDAO bookEntityDAO;
    private final ICategoryEntityDAO categoryEntityDAO;
    private final IUserEntityDAO userEntityDAO;

    @Autowired
    public BookService(IBookEntityDAO bookEntityDAO, ICategoryEntityDAO categoryEntityDAO, IUserEntityDAO userEntityDAO) {
        this.bookEntityDAO = bookEntityDAO;
        this.categoryEntityDAO = categoryEntityDAO;
        this.userEntityDAO = userEntityDAO;
    }

    @Transactional
    public BookNewInputResultDTO addNewBook(BookNewInputDTO book) {
        // Duplicants test
        if (bookEntityDAO.existsById(book.getIsbn())) {
            throw new ServiceValidationException("Book already exists");
        }

        // Category test
        var categoryEntity = categoryEntityDAO
                .findById(book.getCategory())
                .orElseThrow(() -> new ServiceValidationException("Category does not exist"));
        var newBookEntity = toBookEntity(book, categoryEntity);

        bookEntityDAO.save(newBookEntity);

        return toBookReturnDTO(newBookEntity);

    }


    @Transactional
    public BookNewInputResultDTO lendBook(String isbn, String userId) {
        var bookEntity = bookEntityDAO.findById(isbn).orElseThrow(() -> new ServiceValidationException("isbn does not exist"));

        var userEntity = userEntityDAO.findById(userId).orElseThrow(() -> new ServiceValidationException("User does not exist"));

        var result = 

    }

    private BookEntity toBookEntity(BookNewInputDTO book, CategoryEntity category) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setIsbn(book.getIsbn());
        bookEntity.setTitle(book.getTitle());
        bookEntity.setCopies(book.getCopies());
        bookEntity.setOutline(book.getOutline());
        bookEntity.setPublisher(book.getPublisher());
        bookEntity.setCategory(category);
        return bookEntity;
    }

    private LendingEntity toLendingEntity(BookEntity book, UserEntity user) {

        LendingEntity lendingEntity = new LendingEntity();
        lendingEntity.setBook(book);
        lendingEntity.setBorrower(user);
        return lendingEntity;
    }

    private BookNewInputResultDTO toBookReturnDTO(BookEntity bookEntity) {
        return new BookNewInputResultDTO(bookEntity.getIsbn(),bookEntity.getTitle(), bookEntity.getCopies(), bookEntity.getOutline(),bookEntity.getPublisher(), bookEntity.getCategory().getName());
    }

}
