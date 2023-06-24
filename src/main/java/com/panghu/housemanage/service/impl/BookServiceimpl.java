package com.panghu.housemanage.service.impl;

import com.panghu.housemanage.dao.BookMapper;
import com.panghu.housemanage.pojo.po.*;
import com.panghu.housemanage.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceimpl implements BookService {
    @Autowired(required = false)
    BookMapper bookMapper;

    // user
    @Override
    public List<BookList> getlist() {
        return bookMapper.getList();
    }

    @Override
    public List<BookList> getlistByQuery(String query) {
        return bookMapper.getListByQuery(query);
    }

    @Override
    public List<Book> getlistByuId(int userId) {
        return null;
    }

    @Override
    public void processRes(String ISBN, UserBak user) {
        int count = bookMapper.processRes(ISBN, user);
//        if (count == 0) throw new PHServiceException(ExceptionEnum.RESERVATION_FAILURE);
    }

    @Override
    public void addBookList(BookList bookList,int state) {
        bookMapper.addBookList(bookList,"图书流通室",state);
    }

    @Override
    public void insertBorrow(int reservationId,int operator) {
        bookMapper.insertBorrow(reservationId,operator);
    }

    @Override
    public List<ReservationDetail> getResById(UserBak user) {
        return bookMapper.getResById(user);
    }

    @Override
    public int deleteBookList(BookList bookList) {
        return bookMapper.deleteBookListById(bookList.getIsbn());
    }

    @Override
    public List<Reservation> getResInfo() {
        return null;
    }

    @Override
    public List<BorrowDetail> getBorInfo(UserBak user) {
        return bookMapper.getBorById(user);
    }

    @Override
    public List<ReservationDetail> getResList() {
        return bookMapper.getResList();
    }

    @Override
    public void returnBookById(int borrowId) {
        bookMapper.returnBookById(borrowId);
    }

    @Override
    public List<BorrowDetail> getBorList() {
        return bookMapper.getBorList();
    }
}
