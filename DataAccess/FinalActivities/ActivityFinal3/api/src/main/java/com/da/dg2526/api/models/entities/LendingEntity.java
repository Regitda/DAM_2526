package com.da.dg2526.api.models.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "lendings", schema = "_da_library2526")
public class LendingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "lendingdate", nullable = false)
    private LocalDate lendingdate;

    @Column(name = "returningdate")
    private LocalDate returningdate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book", nullable = false)
    private BookEntity bookEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borrower", nullable = false)
    private UserEntity borrower;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getLendingdate() {
        return lendingdate;
    }

    public void setLendingdate(LocalDate lendingdate) {
        this.lendingdate = lendingdate;
    }

    public LocalDate getReturningdate() {
        return returningdate;
    }

    public void setReturningdate(LocalDate returningdate) {
        this.returningdate = returningdate;
    }

    public BookEntity getBook() {
        return bookEntity;
    }

    public void setBook(BookEntity bookEntity) {
        this.bookEntity = bookEntity;
    }

    public UserEntity getBorrower() {
        return borrower;
    }

    public void setBorrower(UserEntity borrower) {
        this.borrower = borrower;
    }

}