package com.sn.cykb.controller;

import com.sn.cykb.annotation.AControllerAspect;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.UsersDTO;
import com.sn.cykb.service.UsersService;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.UsersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songning
 * @date 2020/3/9
 * description
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/login/getUsersInfo")
    @AControllerAspect(description = "登陆时获取用户信息")
    CommonDTO<UsersDTO> getUsersInfos(@RequestBody CommonVO<UsersVO> commonVO) {
        CommonDTO<UsersDTO> commonDTO = usersService.getUsersInfo(commonVO);
        return commonDTO;
    }
}
