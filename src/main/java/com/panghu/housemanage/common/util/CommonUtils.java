package com.panghu.housemanage.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;

public class CommonUtils {

    /**
     * 获取姓名首字母缩写
     *
     * @param name 名字
     * @return {@link String}
     */
    public static String getInitials(String name) {
        StringBuilder initials = new StringBuilder();
        char[] chars = name.toCharArray();
        for (char c : chars) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
            if (pinyinArray != null && pinyinArray.length > 0) {
                initials.append(pinyinArray[0].charAt(0));
            }
        }
        return initials.toString().toUpperCase();
    }
}
