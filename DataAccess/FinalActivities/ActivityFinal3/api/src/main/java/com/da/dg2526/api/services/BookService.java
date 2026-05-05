package com.da.dg2526.api.services;

import com.da.dg2526.api.exceptions.ServiceValidationException;
import com.da.dg2526.api.models.dao.*;
import com.da.dg2526.api.models.dto.bookEntity.*;
import com.da.dg2526.api.models.dto.lendingEntity.LendingReturnDTO;
import com.da.dg2526.api.models.dto.reserveEntity.ReserveResultDTO;
import com.da.dg2526.api.models.entities.*;
import com.da.dg2526.api.models.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class BookService {


    private final IBookEntityDAO bookEntityDAO;
    private final ICategoryEntityDAO categoryEntityDAO;
    private final IUserEntityDAO userEntityDAO;
    private final ILendingEntityDAO lendingEntityDAO;
    private final IReservationEntityDAO reservationEntityDAO;


    private static final Logger log = LoggerFactory.getLogger(BookService.class);


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

        var validatedBook = validateBook(book);
        if (validatedBook.book() == null) {
            var error = ServiceErrorMessages.bookAddErrorGeneralMessage + validatedBook.error();
            log.warn(error);
            throw new ServiceValidationException(error);
        }

        // Saving and returning the saved entity.
        var result = bookEntityDAO.save(validatedBook.book());
        return toBookReturnDTO(result);

    }

    @Transactional
    public List<BookNewInputResultDTO> importBooks(List<BookNewInputDTO> dto) {
        List<BookEntity> validatedBooks = new ArrayList<>();
        var errorStringBuilder = new StringBuilder();

        // Test checks for validity, and returns errors. Only service level checks.
        for (BookNewInputDTO bookNewInputDTO : dto) {
            var result = validateBook(bookNewInputDTO);
            if (result.book() == null) {
                // Book is null so there is an error.
                errorStringBuilder.append(result.error());
            } else {
                // There is no book entity if error happens
                validatedBooks.add(result.book());
            }
        }

        // If all books failed.
        if (validatedBooks.isEmpty()) {
            var error = ServiceErrorMessages.bookImportFullyFailed(dto.size(), errorStringBuilder);
            log.warn(error);
            throw new ServiceValidationException(error);
        }

        if (!errorStringBuilder.isEmpty()) {
            var error = ServiceErrorMessages.bookImportGeneralMessageError + errorStringBuilder;
            log.warn(error);
            throw new ServiceValidationException(error);
        }

        // Save all and return list containing all added books.
        var iterableResult = bookEntityDAO.saveAll(validatedBooks);
        var results = new ArrayList<BookNewInputResultDTO>();
        for (BookEntity bookEntity : iterableResult) {
            results.add(toBookReturnDTO(bookEntity));
        }

        return results;
    }

    @Transactional
    public BookReturnResponseDTO returnBook(String isbn, String userId) {
        var results = checkBookAndUserExists(isbn, userId);
        var user = results.user();
        var book = results.book();

        // Test if lending even exists
        log.debug("Searching active lending for ISBN {} and user {}", isbn, userId);
        var lendingOpt = lendingEntityDAO.findFirstByBookEntityAndBorrowerAndReturningdateIsNullOrderByIdDesc(book, user);

        if (lendingOpt.isEmpty()) {
            log.debug("No active lending found for ISBN {} and user {}", isbn, userId);
            throw new ServiceValidationException(ServiceErrorMessages.lendingNotFound(isbn, userId));
        }
        var lending = lendingOpt.get();

        // Late return, return will be processed but all books returned late will cause fine to reset.
        var dateNow = LocalDate.now();
        var fined = lending.getLendingdate().plusDays(7).isBefore(dateNow);
        log.debug("Processing return of book: {}, was user fined: {}", isbn, fined);

        // Setting fined endFineDate.
        if (fined) {
            user.setFined(dateNow);
            log.warn("User was fined {}, dated {}", userId, dateNow);
        }

        // Marks lending as finished.
        lending.setReturningdate(dateNow);
        log.debug("Lending of book {}, by user {} was success ", isbn, user);
        return new BookReturnResponseDTO(Status.SUCCESS, ServiceErrorMessages.bookReturnedSuccess(book,fined), fined);
    }


    //Book reservation
    @Transactional
    public BookReserveResponseDTO reserveBook(String isbn, String userId) {

        // I could cut down on fined check, but if someone calls api directly fine can be avoided.
        var results = checkBookAndUserExists(isbn, userId);
        var user = results.user();
        var book = results.book();

        log.debug("User {} attempting to reserve a book with isbn {}", userId, isbn);
        // Checking fined status first to not give "Reserve option" while they can't even lend it.
        var userEntityFinedDate = user.getFined();
        if (userEntityFinedDate != null) {
            var userFinedResult = isUserFined(userEntityFinedDate);
            if (userFinedResult.isFined()) {
                log.warn("User {} is fined, fine started on {}, end-date: {}", userId, userEntityFinedDate, userFinedResult.endFineDate());
                return new BookReserveResponseDTO(Status.FAILURE, ServiceErrorMessages.userCurrentlyFined(userId, userFinedResult.endFineDate()), null);
            }
        }


        var email = user.getEmail();
        var phone = user.getPhone();

        if (email == null && phone == null) {
            var error = ServiceErrorMessages.userPersonalDetailsMissing(userId);
            log.debug("{}", error);
            throw new ServiceValidationException(error);
        }

        //If book has copies and is unreserved then reservation is not allowed
        var lent = lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(book);
        var reserved = reservationEntityDAO.countAllByBookEntityAndLendingEntityIsNull(book);

        // Without reserve check you get stuck.
        var freeCopies = book.getCopies() - lent;
        if (freeCopies > 0 && reserved == 0) {
            var error = ServiceErrorMessages.bookHasFreeLendings(isbn, freeCopies);
            log.debug("{}", error);
            throw new ServiceValidationException(error);
        }

        // From forum comment it seems user can reserve as many books as they want...
        var newReservation = toReservationEntity(book, user);
        reservationEntityDAO.save(newReservation);

        return new BookReserveResponseDTO(Status.SUCCESS, "", toReservationResultDTO(userId, reserved + 1));

    }

    // **Book lending**
    @Transactional
    public BookLendingResponseDTO lendBook(String isbn, String userId) {

        var results = checkBookAndUserExists(isbn, userId);
        var user = results.user();
        var book = results.book();

        // Checking fined status first to not give "Reserve option" while they can't even lend it.
        log.debug("User {} attempting to lend a book with isbn {}", userId, isbn);
        var userEntityFinedDate = user.getFined();
        if (userEntityFinedDate != null) {
            var isUserFinedValidationResult = isUserFined(userEntityFinedDate);
            if (isUserFinedValidationResult.isFined()) {
                var error = ServiceErrorMessages.userIsFined(userId, isUserFinedValidationResult.endFineDate());
                log.warn(error);
                return new BookLendingResponseDTO(Status.FAILURE, error, null, false);
            }
        }

        // Check if user is borrowing more than 3 books.
        // This checks if user borrowed books. Not amount of copies of a book are borrowed.
        var lendings = lendingEntityDAO.countAllByBorrowerAndReturningdateIsNull(user);
        log.debug("Total lendings found on a book {}, {} ", isbn, lendings);
        if (lendings >= 3) {
            var error = ServiceErrorMessages.userLendingLimitExceeded(userId, lendings);
            log.debug("{}", error);
            return new BookLendingResponseDTO(Status.FAILURE, error, null, false);
        }

        //Check if there are any left books to borrow.
        var currenLendings = lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(book);
        var bookCopies = book.getCopies();
        if (currenLendings >= bookCopies) {
            var error = ServiceErrorMessages.bookHasNoFreeLendings(isbn, bookCopies, currenLendings);
            log.debug("{}", error);
            return new BookLendingResponseDTO(Status.FAILURE, error, null, true);
        }
        log.debug("Book with ISBN: {} has {} free copies to lend", isbn, bookCopies - currenLendings);

        // Check if book is currently reserved and if the request user is the oldest reserver
        var oldestReserve = reservationEntityDAO.findFirstByBookEntityAndLendingEntityIsNullOrderByDateAsc(book);
        if (oldestReserve.isPresent()) {
            var oldest = oldestReserve.get().getBorrower().getCode();
            if (!Objects.equals(oldestReserve.get().getBorrower().getCode(), user.getCode())) {
                var error = ServiceErrorMessages.reserveRequestUserNotOwner(isbn);
                log.debug("{} The oldest lending belongs to {}", error, oldest);
                return new BookLendingResponseDTO(Status.FAILURE, error, null, true);
            }
        }

        //Save new lending
        var newLending = toLendingEntity(book, user);
        lendingEntityDAO.save(newLending);

        // var reserves = reservationEntityDAO.countAllByBookEntityAndLendingEntityIsNull(book);
        log.debug("Lending saved for book {} and user {}", isbn, userId);
        return new BookLendingResponseDTO(Status.SUCCESS, "", toLendingResultDTO(newLending, userId), false);
    }


    //** Validations **//

    private record BookValidatedResult(BookEntity book, String error) {
    }

    private BookValidatedResult validateBook(BookNewInputDTO book) {

        StringBuilder errors = new StringBuilder();
        log.debug("Validating book with isbn: {}", book.isbn());
        if (bookEntityDAO.existsById(book.isbn())) {
            errors.append(ServiceErrorMessages.bookAlreadyExists(book));
        }

        // Category test
        var categoryOpt = categoryEntityDAO.findById(book.category());
        CategoryEntity category = null;
        if (categoryOpt.isEmpty()) {
            errors.append(ServiceErrorMessages.bookCategoryDoesNotExist(book));
        } else {
            category = categoryOpt.get();
        }


        BookEntity bookEntity = null;
        if (errors.isEmpty()) {
            bookEntity = toBookEntity(book, category);
        }
        return new BookValidatedResult(bookEntity, errors.toString());

    }


    // Returns Book and User, throws service level error if not found.
    private record BookAndUser(BookEntity book, UserEntity user) {
    }

    private BookAndUser checkBookAndUserExists(String isbn, String userId) {
        var errorStringBuilder = new StringBuilder();

        // Book existance check
        log.debug("Checking book with ISBN: {} and user {} for existance", isbn, userId);
        var bookEntityOpt = bookEntityDAO.findById(isbn);
        BookEntity bookEntity = null;
        if (bookEntityOpt.isEmpty()) {
            var error = ServiceErrorMessages.bookDoesNotExist(isbn);
            errorStringBuilder.append(error);
        } else {
            bookEntity = bookEntityOpt.get();
        }

        // User existence check
        var userEntityOpt = userEntityDAO.findById(userId);
        UserEntity userEntity = null;
        if (userEntityOpt.isEmpty()) {
            var error = ServiceErrorMessages.userDoesNotExist(userId);
            errorStringBuilder.append(error);
        } else {
            userEntity = userEntityOpt.get();
        }


        if (!errorStringBuilder.isEmpty()) {
            var error = errorStringBuilder.toString();
            log.debug(error);
            throw new ServiceValidationException(error);
        }
        return new BookAndUser(bookEntity, userEntity);
    }


    // Checks if user was fined and returns possible fine end endFineDate.
    private record UserFinedStatus(boolean isFined, LocalDate endFineDate) {
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

    private LendingReturnDTO toLendingResultDTO(LendingEntity lendingEntity, String borrowerId) {
        return new LendingReturnDTO(lendingEntity.getLendingdate(), lendingEntity.getReturningdate(), borrowerId, lendingEntity.getBook().getTitle());
    }


    private BookEntity toBookEntity(BookNewInputDTO book, CategoryEntity category) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setIsbn(book.isbn());
        bookEntity.setTitle(book.title());
        bookEntity.setCopies(book.copies());
        bookEntity.setOutline(book.outline());
        bookEntity.setPublisher(book.publisher());
        bookEntity.setCategory(category);
        return bookEntity;
    }

    private BookNewInputResultDTO toBookReturnDTO(BookEntity bookEntity) {
        return new BookNewInputResultDTO(bookEntity.getIsbn(), bookEntity.getTitle(), bookEntity.getCopies(), bookEntity.getOutline(), bookEntity.getPublisher(), bookEntity.getCategory().getName());
    }


}
