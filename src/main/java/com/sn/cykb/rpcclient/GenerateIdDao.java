package com.sn.cykb.rpcclient;

import com.alibaba.dubbo.config.annotation.Reference;
import com.uni.dubbo.service.GenerateIdService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sonin
 * @date 2020/5/25 20:04
 */
@Component
public class GenerateIdDao {

    @Reference(check = false)
    private GenerateIdService generateIdService;

    public long generateId() {
        return generateIdService.getSnowflakeId();
    }
}
