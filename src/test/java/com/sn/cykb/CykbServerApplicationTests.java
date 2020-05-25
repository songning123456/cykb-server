package com.sn.cykb;

import com.sn.cykb.rpcclient.GenerateIdDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CykbServerApplicationTests {

    @Autowired
    private GenerateIdDao generateIdDao;

    @Test
    public void testDemo() {
        for (int i = 0; i < 100; i++) {
            long id = generateIdDao.generateId();
            log.info("id为: {}", id);
        }
    }

}
