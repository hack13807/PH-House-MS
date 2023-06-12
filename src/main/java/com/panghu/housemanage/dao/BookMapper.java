package com.panghu.housemanage.dao;

import com.panghu.housemanage.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {
    List<BookList> getList();

    List<BookList> getListByQuery(@Param("query") String query);

    // 得到能被预约的书
    List<Book> getRes(@Param("isbn") String ISBN);

    // 获取预约号
    Reservation getResId(@Param("book") Book book, @Param("user") UserBak user);

    // 根据userid获取预约记录 注意返回值需要的是包装起来的书名和预约号
    List<ReservationDetail> getResById(@Param("user") UserBak user);

    // 用户还书
    void returnBookById(@Param("borrowId") Integer borrowId);

    // 根据userid获取已借阅记录
    List<BorrowDetail> getBorById(@Param("user") UserBak user);

    List<Book> getListById(@Param("user") UserBak user);

    int processRes(@Param("isbn") String ISBN, @Param("user") UserBak user);

    // admin
    void addBookList(@Param("booklist") BookList booklist, @Param("location") String location, @Param("state") Integer state);

    //管理员获取所有预约记录 注意返回值需要的是包装起来的书名和预约号还有用户名
    List<ReservationDetail> getResList();

    List<BorrowDetail> getBorList();

    void insertBorrow(@Param("rid")int reservationId, @Param("op") int operator);


    int deleteBookListById(@Param("isbn")String isbn);


}
