package com.panghu.housemanage.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.ReceiptTypeEnum;
import com.panghu.housemanage.pojo.po.*;
import com.panghu.housemanage.pojo.vo.*;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Controller处理请求工具类
 */
public class RequestHandleUtils {

    private RequestHandleUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> Page<T> getPage(HttpServletRequest request) {
        int offset = Integer.parseInt(request.getParameter("offset") == null ? "0" : request.getParameter("offset"));
        int limit = Integer.parseInt(request.getParameter("limit") == null ? "10" : request.getParameter("limit"));
        return new Page<>(offset / limit + 1L, limit);
    }

    public static PHResp<Map<String, Object>> successPageResult(IPage<? extends PHBaseVo> pageResult) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", pageResult.getTotal());
        resultMap.put("rows", pageResult.getRecords());
        return PHResp.success(resultMap);
    }

    public static PHResp<String> successResult() {
        return PHResp.success();
    }

    public static Map<String, Object> errorResult(Exception e) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 300);
        resultMap.put("msg", e.toString());
        return resultMap;
    }

    /**
     * 建立订单实体
     *
     * @param request 请求
     * @param clazz   clazz
     * @return {@link T}
     * @throws Exception 异常
     */
    public static <T> T buildPoEntity(HttpServletRequest request, Class<? extends PHBaseVo> clazz) throws Exception {
// 实例化类对象
        T instance = (T) clazz.newInstance();

        // 获取所有属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 获取属性名
            String fieldName = field.getName();

            // 从请求参数中获取对应的值
            String value = request.getParameter(fieldName);
            if (value == null) {
                continue;
            }

            // 将值转换为与属性类型相符的类型，并设置属性值
            Class<?> fieldType = field.getType();
            Object fieldValue = convertStringToType(value, fieldType);
            field.setAccessible(true);
            field.set(instance, fieldValue);
        }

        return instance;
    }

    private static Object convertStringToType(String str, Class<?> fieldType) {
        if (fieldType.equals(String.class)) {
            return str;
        } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
            return Integer.valueOf(str);
        } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
            return Long.valueOf(str);
        } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
            return Boolean.valueOf(str);
        } else {
            throw new IllegalArgumentException("Unsupported field type: " + fieldType.getName());
        }
    }

    public static List<MemberPo> memberDTOTrans(List<MemberVo> volist) {
        List<MemberPo> poList = new ArrayList<>(volist.size());
        volist.forEach(memberVo -> {
            MemberPo memberPo = new MemberPo();
            Optional.ofNullable(memberVo.getRowId()).ifPresent(memberPo::setId);
            Optional.ofNullable(memberVo.getMemberName()).ifPresent(memberPo::setName);
            Optional.ofNullable(memberVo.getSex()).ifPresent(sex -> memberPo.setSex(sex.getCode()));
            Optional.ofNullable(memberVo.getTel()).ifPresent(memberPo::setTel);
            Optional.ofNullable(memberVo.getIdCard()).ifPresent(memberPo::setIdCard);
            Optional.ofNullable(memberVo.getMemberStatus()).ifPresent(status -> memberPo.setStatus(status.getCode()));
            poList.add(memberPo);
        });
        return poList;
    }

    public static List<RoomPo> roomDTOTrans(List<RoomVo> volist) {
        List<RoomPo> poList = new ArrayList<>(volist.size());
        volist.forEach(roomVo -> {
            RoomPo roomPo = new RoomPo();
            Optional.ofNullable(roomVo.getRowId()).ifPresent(roomPo::setId);
            Optional.ofNullable(roomVo.getRoomNo()).ifPresent(roomPo::setNumber);
            Optional.ofNullable(roomVo.getRoomDesc()).ifPresent(roomPo::setDescription);
            Optional.ofNullable(roomVo.getRoomStatus()).ifPresent(status -> roomPo.setStatus(status.getCode()));
            Optional.ofNullable(roomVo.getEnableStatus()).ifPresent(enable -> roomPo.setEnable(Integer.parseInt(enable)));
            poList.add(roomPo);
        });
        return poList;
    }

    public static List<LeasePo> leaseDTOTrans(List<LeaseVo> leaseVos) {
        List<LeasePo> poList = new ArrayList<>(leaseVos.size());
        leaseVos.forEach(leaseVo -> {
            LeasePo leasePo = new LeasePo();
            Optional.ofNullable(leaseVo.getRowId()).ifPresent(leasePo::setId);
            Optional.ofNullable(leaseVo.getRoomId()).ifPresent(leasePo::setRoomId);
            Optional.ofNullable(leaseVo.getMembers()).ifPresent(leasePo::setMembers);
            Optional.ofNullable(leaseVo.getStartDate()).ifPresent(leasePo::setStartDate);
            Optional.ofNullable(leaseVo.getEndDate()).ifPresent(leasePo::setEndDate);
            Optional.ofNullable(leaseVo.getUnit()).ifPresent(leasePo::setUnit);
            Optional.ofNullable(leaseVo.getRentAmount()).ifPresent(leasePo::setRentAmount);
            Optional.ofNullable(leaseVo.getLeaseType()).ifPresent(typeEnum -> leasePo.setLeaseType(typeEnum.getCode()));
            Optional.ofNullable(leaseVo.getVoEffective()).ifPresent(eff -> leasePo.setEffective(Integer.parseInt(eff)));
            poList.add(leasePo);
        });
        return poList;
    }

    private static Object receiptDTOTrans(List<ReceiptVo> voList) {
        List<ReceiptPo> poList = new ArrayList<>(voList.size());
        voList.forEach(receiptVo -> {
            ReceiptPo receiptPo = new ReceiptPo();
            Optional.ofNullable(receiptVo.getRowId()).ifPresent(receiptPo::setId);
            Optional.ofNullable(receiptVo.getAmount()).ifPresent(receiptPo::setAmount);
            Optional.ofNullable(receiptVo.getReceiptType()).ifPresent(typeEnum -> receiptPo.setReceiptType(typeEnum.getCode()));
            Optional.ofNullable(receiptVo.getPayType()).ifPresent(payTypeEnum -> receiptPo.setPayType(payTypeEnum.getCode()));
            Optional.ofNullable(receiptVo.getBizDate()).ifPresent(receiptPo::setBizDate);
            Optional.ofNullable(receiptVo.getRoomNo()).ifPresent(receiptPo::setRoomNo);
            Optional.ofNullable(receiptVo.getLeaseNumber()).ifPresent(receiptPo::setLeaseNumber);
            Optional.ofNullable(receiptVo.getMemberName()).ifPresent(receiptPo::setMemberName);
            Optional.ofNullable(receiptVo.getMemberIdcard()).ifPresent(receiptPo::setMemberIdcard);
            Optional.ofNullable(receiptVo.getMemberTel()).ifPresent(receiptPo::setMemberTel);
            poList.add(receiptPo);
        });
        return poList;
    }


    public static <T extends PHBaseVo, R extends PHBasePo> List<R> modelDTOTrans(List<T> voList) {
        T firstVo = voList.get(0);
        switch (firstVo) {
            case MemberVo m -> {
                return (List<R>) memberDTOTrans((List<MemberVo>) voList);
            }
            case RoomVo r -> {
                return (List<R>) roomDTOTrans((List<RoomVo>) voList);
            }
            case LeaseVo l -> {
                return (List<R>) leaseDTOTrans((List<LeaseVo>) voList);
            }
            case ReceiptVo l -> {
                return (List<R>) receiptDTOTrans((List<ReceiptVo>) voList);
            }
            default -> throw new IllegalStateException("Unexpected value: " + firstVo);
        }
    }

}
