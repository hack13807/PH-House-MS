package com.panghu.housemanage.service;

import com.panghu.housemanage.pojo.po.*;

import java.util.List;

public interface BookService {
    // user
    List<BookList> getlist();

    List<BookList> getlistByQuery(String query);

    List<Book> getlistByuId(int userId);

    void processRes(String ISBN, UserBak user);

    List<ReservationDetail> getResById(UserBak user);

    void returnBookById(int borrowId);

    // admin
    void addBookList(BookList booklist,int state);

    void insertBorrow(int reservationId,int operator);

    List<Reservation> getResInfo();

    List<BorrowDetail> getBorInfo(UserBak user);

    List<ReservationDetail> getResList();

    List<BorrowDetail> getBorList();

    int deleteBookList(BookList bookList);
}
