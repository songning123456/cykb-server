package com.sn.cykb.controller;

import com.sn.cykb.annotation.AControllerAspect;
import com.sn.cykb.dto.ChaptersDTO;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.service.ChaptersService;
import com.sn.cykb.vo.ChaptersVO;
import com.sn.cykb.vo.CommonVO;
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
@RequestMapping("/chapters")
public class ChaptersController {

    @Autowired
    private ChaptersService chaptersService;

    @AControllerAspect(description = "获取目录")
    @PostMapping("/directory")
    public CommonDTO<ChaptersDTO> directorys(@RequestBody CommonVO<ChaptersVO> commonVO) {
        CommonDTO<ChaptersDTO> commonDTO = chaptersService.directory(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "未登录/未加入书架 每次都从第一章开始")
    @PostMapping("/unknownTop")
    public CommonDTO<ChaptersDTO> unknownTops(@RequestBody CommonVO<ChaptersVO> commonVO) {
        CommonDTO<ChaptersDTO> commonDTO = chaptersService.unknownTop(commonVO);
        return commonDTO;
    }
}
