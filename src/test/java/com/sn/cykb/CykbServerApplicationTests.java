package com.sn.cykb;

import com.sn.cykb.rpcclient.GenerateIdDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CykbServerApplicationTests {

    @Autowired
    private GenerateIdDao generateIdDao;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Test
    public void testDubbo() {
        for (int i = 0; i < 100; i++) {
            long id = generateIdDao.generateId();
            log.info("id为: {}", id);
        }
    }

    @Test
    public void testRibbon() {
        this.loadBalancerClient.choose("uniId");
        Map result = restTemplate.getForObject("http://uniId/uni-id/generate/id", Map.class);
        assert result != null;
        log.info("当前ribbon result: {}", result.toString());
    }

}
