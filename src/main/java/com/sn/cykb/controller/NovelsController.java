package com.sn.cykb.controller;

import com.sn.cykb.annotation.AControllerAspect;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.service.NovelsService;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.NovelsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: songning
 * @date: 2020/3/9 22:55
 */
@RestController
@RequestMapping("/novels")
public class NovelsController {

    @Autowired
    private NovelsService novelsService;

    @AControllerAspect(description = "首页查询小说")
    @PostMapping("/homePage")
    public CommonDTO<NovelsDTO> homePages(@RequestBody CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = novelsService.homePage(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "分类统计小说总数")
    @PostMapping("/classify")
    public CommonDTO<NovelsDTO> classify(@RequestBody CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = novelsService.classify(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "分类查询小说")
    @PostMapping("/classifyResult")
    public CommonDTO<NovelsDTO> classifyResults(@RequestBody CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = novelsService.classifyResult(commonVO);
        return commonDTO;
    }
}
