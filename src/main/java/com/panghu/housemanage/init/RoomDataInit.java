//package com.panghu.housemanage.init;
//
//import com.panghu.housemanage.dao.RoomMapper;
//import com.panghu.housemanage.pojo.po.Room;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.List;
//

//@Component
//public class RoomDataInit {
//
//    @Autowired
//    public RoomMapper roomMapper;
//
//    @PostConstruct
//    private void init(){
////        List<Room> list = new ArrayList<>();
////        int high = 2;
////        int no = 1;
////        for (int i = 0; i < 100; i++) {
////            if (high == 10 && no == 7) {
////               break;
////            }
////            if (no == 7) {
////                no = 1;
////                high++;
////            }
////            Room roomDto = new Room();
////            roomDto.setNumber(high+ "0" + no++);
////            roomDto.setDescription("12345");
////            roomDto.setMemberId(Long.valueOf(i));
////            list.add(roomDto);
////        }
////        int rows = roomMapper.insertBatch(list);
////        System.out.printf(rows+"");
//    }
//}
