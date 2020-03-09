package com.sn.cykb.util.classconvert.service;

import com.sn.cykb.util.DateUtil;

import java.util.Date;

/**
 * @author: songning
 * @date: 2020/2/15 18:29
 */
public class DateToStringImpl implements IClassConvert {

    @Override
    public Object classConvert(Object param) {
        return DateUtil.dateToStr((Date) param, "yyyy-MM-dd HH:mm:ss");
    }
}
