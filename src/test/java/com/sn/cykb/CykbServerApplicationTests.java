package com.sn.cykb;

import com.sn.cykb.rpcclient.GenerateIdDao;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            long id = generateIdDao.generateId();
            longList.add(id);
            log.info("id为: {}", id);
        }
        this.appendFile(longList);
    }

    @Test
    public void testRibbon() {
        this.loadBalancerClient.choose("uniId");
        Map result = restTemplate.getForObject("http://uniId/uni-id/generate/id", Map.class);
        assert result != null;
        log.info("当前ribbon result: {}", result.toString());
    }

    public void appendFile(List<Long> longList) {
        try {
            // 准备文件666.txt其中的内容是空的
            File file = new File("D:/project/snowflake.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            // 把数据写入到输出流
            for (long item : longList) {
                fileWriter.write(Long.toString(item));
                fileWriter.write("\r\n");
            }
            // 关闭输出流
            fileWriter.close();
            System.out.println("输入完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
