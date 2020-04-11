package com.sn.cykb.service.impl;

import com.alibaba.fastjson.JSON;
import com.sn.cykb.constant.HttpStatus;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.UsersDTO;
import com.sn.cykb.entity.Users;
import com.sn.cykb.repository.UsersRepository;
import com.sn.cykb.service.UsersService;
import com.sn.cykb.util.ClassConvertUtil;
import com.sn.cykb.util.HttpUtil;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.UsersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;

/**
 * @author songning
 * @date 2020/3/9
 * description
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public CommonDTO<UsersDTO> getWxUsersInfo(CommonVO<UsersVO> commonVO) {
        CommonDTO<UsersDTO> commonDTO = new CommonDTO<>();
        String code = commonVO.getCondition().getCode();
        String avatar = commonVO.getCondition().getAvatar();
        String nickName = commonVO.getCondition().getNickName();
        int gender = commonVO.getCondition().getGender();
        if (StringUtils.isEmpty(code)) {
            commonDTO.setStatus(HttpStatus.HTTP_ACCEPTED);
            commonDTO.setMessage("weixin code不能为空");
            return commonDTO;
        }
        String uniqueId = this.getWeixinUniqueId(code);
        if (StringUtils.isEmpty(uniqueId)) {
            commonDTO.setStatus(HttpStatus.HTTP_ACCEPTED);
            commonDTO.setMessage("未获取到微信openId");
            return commonDTO;
        }
        Users users = usersRepository.findByUniqueId(uniqueId);
        // 判断是否存在
        if (users != null) {
            // 判断是否修改过
            if (!avatar.equals(users.getAvatar()) || !nickName.equals(users.getNickName()) || gender != users.getGender()) {
                users = Users.builder().uniqueId(uniqueId).avatar(avatar).nickName(nickName).gender(gender).updateTime(new Date()).build();
                usersRepository.updateNative(users);
            }
        } else {
            users = Users.builder().uniqueId(uniqueId).avatar(avatar).nickName(nickName).gender(gender).updateTime(new Date()).build();
            users = usersRepository.save(users);
        }
        UsersDTO usersDTO = new UsersDTO();
        ClassConvertUtil.populate(users, usersDTO);
        commonDTO.setData(Collections.singletonList(usersDTO));
        return commonDTO;
    }

    @Override
    public CommonDTO<UsersDTO> getUniUsersInfo(CommonVO<UsersVO> commonVO) {
        CommonDTO<UsersDTO> commonDTO = new CommonDTO<>();
        String code = commonVO.getCondition().getCode();
        Users users = usersRepository.findByUniqueId(code);
        if (users == null) {
            users = Users.builder().uniqueId(code).updateTime(new Date()).build();
            users = usersRepository.save(users);
        }
        UsersDTO usersDTO = new UsersDTO();
        assert users != null;
        usersDTO.setUniqueId(users.getId());
        commonDTO.setData(Collections.singletonList(usersDTO));
        return commonDTO;
    }

    private String getWeixinUniqueId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + "wxa0c6ab139ce8d933" + "&secret=" + "8154f064fd75361e41ee71f1e1de6fd2" + "&js_code=" + code + "&grant_type=" + code;
        String respond = HttpUtil.doGet(url);
        String uniqueId = JSON.parseObject(respond).getString("openid");
        if (!StringUtils.isEmpty(uniqueId)) {
            return uniqueId;
        } else {
            return "";
        }
    }
}
