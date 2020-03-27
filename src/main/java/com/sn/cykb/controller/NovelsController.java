package com.sn.cykb.controller;

import com.sn.cykb.annotation.AControllerAspect;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.service.NovelsService;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.NovelsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @AControllerAspect(description = "作者也写过")
    @PostMapping("/sameAuthor")
    public CommonDTO<NovelsDTO> sameAuthors(@RequestBody CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = novelsService.sameAuthor(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "快速搜索")
    @GetMapping("/fastSearch")
    public CommonDTO<NovelsDTO> fastSearches(@RequestParam(value = "authorOrTitle") String authorOrTitle) {
        CommonDTO<NovelsDTO> commonDTO = novelsService.fastSearch(authorOrTitle);
        return commonDTO;
    }

    @AControllerAspect(description = "本地搜索")
    @PostMapping("/nativeSearch")
    public CommonDTO<NovelsDTO> nativeSearches(@RequestBody CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = novelsService.nativeSearch(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "外网搜索")
    @PostMapping("/ecdemicSearch")
    public CommonDTO<NovelsDTO> ecdemicSearches(@RequestBody CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = novelsService.ecdemicSearch(commonVO);
        return commonDTO;
    }
}
