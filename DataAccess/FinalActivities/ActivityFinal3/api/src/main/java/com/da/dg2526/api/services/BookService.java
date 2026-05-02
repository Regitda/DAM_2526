package com.da.dg2526.api.services;

import com.da.dg2526.api.exceptions.ServiceValidationException;
import com.da.dg2526.api.models.dao.*;
import com.da.dg2526.api.models.dto.bookEntity.*;
import com.da.dg2526.api.models.dto.lendingEntity.LendingReturnDTO;
import com.da.dg2526.api.models.dto.reserveEntity.ReserveResultDTO;
import com.da.dg2526.api.models.entities.*;
import com.da.dg2526.api.utils.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BookService {


    private final IBookEntityDAO bookEntityDAO;
    private final ICategoryEntityDAO categoryEntityDAO;
    private final IUserEntityDAO userEntityDAO;
    private final ILendingEntityDAO lendingEntityDAO;
    private final IReservationEntityDAO reservationEntityDAO;

    @Autowired
    public BookService(IBookEntityDAO bookEntityDAO, ICategoryEntityDAO categoryEntityDAO, IUserEntityDAO userEntityDAO, ILendingEntityDAO lendingEntityDAO, IReservationEntityDAO reservationEntityDAO) {
        this.bookEntityDAO = bookEntityDAO;
        this.categoryEntityDAO = categoryEntityDAO;
        this.userEntityDAO = userEntityDAO;
        this.lendingEntityDAO = lendingEntityDAO;
        this.reservationEntityDAO = reservationEntityDAO;
    }

    //**Book addition**
    @Transactional
    public BookNewInputResultDTO addNewBook(BookNewInputDTO book) {
        // Duplicants test
        if (bookEntityDAO.existsById(book.getIsbn())) {
            throw new ServiceValidationException("Book already exists");
        }

        // Category test
        var categoryEntity = categoryEntityDAO.findById(book.getCategory()).orElseThrow(() -> new ServiceValidationException("Category does not exist: " + book.getCategory()));
        var newBookEntity = toBookEntity(book, categoryEntity);


        bookEntityDAO.save(newBookEntity);
        return toBookReturnDTO(newBookEntity);

    }

    @Transactional
    public BookReturnResponseDTO returnBook(String isbn, String userId) {
        var results = checkBookAndUserExists(isbn, userId);
        var user = results.user;
        var book = results.book;

        // Test if lending even exists
        var lending = lendingEntityDAO.findFirstByBookEntityAndBorrowerAndReturningdateIsNullOrderByIdDesc(book, user).orElseThrow(() -> new ServiceValidationException("Lending not found"));

        // Late return, return will be processed but all books returned late will cause fine to reset.
        var fined = lending.getLendingdate().plusDays(7).isBefore(LocalDate.now());
        LoggerUtil.logInfo("Processing return of book: " + isbn + ", was user fined: " + fined);

        // Setting fined date.
        if (fined) {
            user.setFined(LocalDate.now());
        }

        // Marks lending as finished.
        lending.setReturningdate(LocalDate.now());
        return new BookReturnResponseDTO("Success", "Book " + book.getTitle() + " was returned.", fined);
    }


    //Book reservation
    @Transactional
    public BookReserveResponseDTO reserveBook(String isbn, String userId) {

        // I could cut down on fined check, but if someone calls api directly fine can be avoided.
        var results = checkBookAndUserExists(isbn, userId);
        var user = results.user;
        var book = results.book;


        // Checking fined status first to not give "Reserve option" while they can't even lend it.
        var userEntityFinedDate = user.getFined();
        if (userEntityFinedDate != null) {
            var userFinedResult = isUserFined(userEntityFinedDate);
            if (userFinedResult.fined) {
                LoggerUtil.logWarning("User is fined, canceling reservation.");
                return new BookReserveResponseDTO("Fail", "User is currently fined, end of fine: " + userFinedResult.date, null);
            }
        }


        var email = user.getEmail();
        var phone = user.getPhone();

        if (email == null && phone == null) {
            throw new ServiceValidationException("Email or phone is obligatory for reservation");
        }

        //If book has copies reservation is not allowed
        var lent = lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(book);
        var reserved = reservationEntityDAO.countAllByBookEntityAndLendingEntityIsNull(book);

        // Without reserve check you get stuck.
        if (lent >= book.getCopies() && reserved == 0) {
            throw new ServiceValidationException("Book has free copies to lend");
        }


        // From forum comment it seems user can reserve as many books as they want...
        var newReservation = toReservationEntity(book, user);
        reservationEntityDAO.save(newReservation);

        var reservations = reservationEntityDAO.countAllByBookEntityAndLendingEntityIsNull(book);
        return new BookReserveResponseDTO("Succcess", "", toReservationResultDTO(userId, reservations));

    }

    // **Book lending**
    @Transactional
    public BookLendingResult lendBook(String isbn, String userId) {

        var results = checkBookAndUserExists(isbn, userId);
        var user = results.user;
        var book = results.book;

        // Checking fined status first to not give "Reserve option" while they can't even lend it.
        var userEntityFinedDate = user.getFined();
        if (userEntityFinedDate != null) {
            var userFinedResult = isUserFined(userEntityFinedDate);
            if (userFinedResult.fined) {
                LoggerUtil.logWarning("User is fined, canceling lending.");
                return new BookLendingResult("Fail", "User is currently fined, end of fine: " + userFinedResult.date, null, false);
            }
        }

        // Check if user is borrowing more than 3 books.
        var lendings = lendingEntityDAO.countAllByBorrowerAndReturningdateIsNull(user);
        if (lendings > 3) {
            return new BookLendingResult("Fail", "User currently is borrowing 3 books. Limit is 3", null, false);
        }

        //Check if there are any left books.
        var currenLendings = lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(book);
        var bookCopies = book.getCopies();
        if (currenLendings >= bookCopies)
            return new BookLendingResult("Fail", "All books have been lent: Book has " + bookCopies + "copies, out of which are lent: " + currenLendings, null, true);

        // Check if book is currently reserved and if the request user is the oldest reserver
        var oldestReserve = reservationEntityDAO.findFirstByBookEntityAndLendingEntityIsNullOrderByDateAsc(book);
        if (oldestReserve.isPresent()) {
            if (oldestReserve.get().getBorrower() != user)
                return new BookLendingResult("Fail", "The book is currently reserved by a different user", null, true);
        }

        //Save new lending
        var newLending = toLendingEntity(book, user);
        lendingEntityDAO.save(newLending);

        var reserves = reservationEntityDAO.countAllByBookEntityAndLendingEntityIsNull(book);
        return new BookLendingResult("Success", "", toLendingResultDTO(newLending, userId), false);
    }


    //** Validations **//

    // Returns Book and User, throws service level error if not found.
    private record BookAndUser(BookEntity book, UserEntity user) {
    }

    private BookAndUser checkBookAndUserExists(String isbn, String userId) {
        var bookEntity = bookEntityDAO.findById(isbn).orElseThrow(() -> new ServiceValidationException("Book with provided ISBN does not exist: " + isbn));

        var userEntity = userEntityDAO.findById(userId).orElseThrow(() -> new ServiceValidationException("No user with provided id found: " + userId));
        return new BookAndUser(bookEntity, userEntity);
    }


    // Checks if user was fined and returns possible fine end date.
    private record UserFinedStatus(boolean fined, LocalDate date) {
    }

    private UserFinedStatus isUserFined(LocalDate finedDate) {
        var currentDate = LocalDate.now();
        var fineEndDate = finedDate.plusDays(15);
        return new UserFinedStatus(fineEndDate.isAfter(currentDate), fineEndDate);
    }


    //** Mappers ** //
    private ReservationEntity toReservationEntity(BookEntity book, UserEntity user) {
        var reservationEntity = new ReservationEntity();
        reservationEntity.setDate(LocalDate.now());
        reservationEntity.setBorrower(user);
        reservationEntity.setBook(book);
        reservationEntity.setLending(null);
        return reservationEntity;
    }

    private ReserveResultDTO toReservationResultDTO(String borrower, Integer reservationsOnThatBook) {
        return new ReserveResultDTO(LocalDate.now(), borrower, reservationsOnThatBook);
    }

    private LendingEntity toLendingEntity(BookEntity book, UserEntity user) {
        LendingEntity lendingEntity = new LendingEntity();
        lendingEntity.setBook(book);
        lendingEntity.setBorrower(user);
        lendingEntity.setLendingdate(LocalDate.now());
        return lendingEntity;
    }

    private LendingReturnDTO toLendingResultDTO(LendingEntity lendingEntity, String borrower) {
        return new LendingReturnDTO(lendingEntity.getLendingdate(), lendingEntity.getReturningdate(), borrower);
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

    private BookNewInputResultDTO toBookReturnDTO(BookEntity bookEntity) {
        return new BookNewInputResultDTO(bookEntity.getIsbn(), bookEntity.getTitle(), bookEntity.getCopies(), bookEntity.getOutline(), bookEntity.getPublisher(), bookEntity.getCategory().getName());
    }

}
