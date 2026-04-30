package com.da.dg2526.api.models.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservations", schema = "_da_library2526")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book", nullable = false)
    private BookEntity bookEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borrower", nullable = false)
    private UserEntity borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lending")
    private LendingEntity lendingEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public LendingEntity getLending() {
        return lendingEntity;
    }

    public void setLending(LendingEntity lendingEntity) {
        this.lendingEntity = lendingEntity;
    }

}