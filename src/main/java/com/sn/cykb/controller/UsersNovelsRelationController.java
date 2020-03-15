package com.sn.cykb.controller;

import com.sn.cykb.annotation.AControllerAspect;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.UsersNovelsRelationDTO;
import com.sn.cykb.service.UsersNovelsRelationService;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.UsersNovelsRelationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: songning
 * @date: 2020/3/9 22:56
 */
@RestController
@RequestMapping("/relation")
public class UsersNovelsRelationController {

    @Autowired
    private UsersNovelsRelationService usersNovelsRelationService;

    @AControllerAspect(description = "我的书架")
    @PostMapping("/bookcase")
    public CommonDTO<UsersNovelsRelationDTO> bookcases(@RequestBody CommonVO<UsersNovelsRelationVO> commonVO) {
        CommonDTO<UsersNovelsRelationDTO> commonDTO = usersNovelsRelationService.bookcase(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "加入书架")
    @PostMapping("/insertBookcase")
    public CommonDTO<UsersNovelsRelationDTO> insertBookcases(@RequestBody CommonVO<UsersNovelsRelationVO> commonVO) {
        CommonDTO<UsersNovelsRelationDTO> commonDTO = usersNovelsRelationService.insertBookcase(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "将本书置顶")
    @PostMapping("/topBookcase")
    public CommonDTO<UsersNovelsRelationDTO> topBookcases(@RequestBody CommonVO<UsersNovelsRelationVO> commonVO) {
        CommonDTO<UsersNovelsRelationDTO> commonDTO = usersNovelsRelationService.topBookcase(commonVO);
        return commonDTO;
    }

    @AControllerAspect(description = "从书架删除本书")
    @PostMapping("/deleteBookcase")
    public CommonDTO<UsersNovelsRelationDTO> deleteBookcases(@RequestBody CommonVO<UsersNovelsRelationVO> commonVO) {
        CommonDTO<UsersNovelsRelationDTO> commonDTO = usersNovelsRelationService.deleteBookcase(commonVO);
        return commonDTO;
    }
}
