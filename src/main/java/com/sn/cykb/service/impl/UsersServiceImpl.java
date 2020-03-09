package com.sn.cykb.service.impl;

import com.alibaba.fastjson.JSON;
import com.sn.cykb.constant.HttpStatus;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.UsersDTO;
import com.sn.cykb.entity.UsersEntity;
import com.sn.cykb.repository.UsersRepository;
import com.sn.cykb.service.UsersService;
import com.sn.cykb.util.HttpUtil;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.UsersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public CommonDTO<UsersDTO> getUsersInfo(CommonVO<UsersVO> commonVO) {
        CommonDTO<UsersDTO> commonDTO = new CommonDTO<>();
        String code = commonVO.getCondition().getCode();
        String avatar = commonVO.getCondition().getAvatar();
        String nickName = commonVO.getCondition().getNickName();

        if (StringUtils.isEmpty(code)) {
            commonDTO.setStatus(HttpStatus.HTTP_ACCEPTED);
            commonDTO.setMessage("weixin code不能为空");
            return commonDTO;
        }
        String uniqueId = "";
        try {
            uniqueId = this.getWeixinUniqueId(code);
        } catch (Exception e) {
            commonDTO.setStatus(HttpStatus.HTTP_ACCEPTED);
            commonDTO.setMessage(e.getMessage());
            return commonDTO;
        }
        UsersEntity usersEntity = usersRepository.findByUniqueId(uniqueId);
        if (usersEntity != null) {
            if (!avatar.equals(usersEntity.getAvatar()) || !nickName.equals(usersEntity.getNickName())) {
                usersEntity = UsersEntity.builder().uniqueId(uniqueId).avatar(avatar).nickName(nickName).updateTime(new Date()).build();
                usersRepository.save(usersEntity);
            } else {

            }
        } else {
            usersEntity = UsersEntity.builder().uniqueId(uniqueId).avatar(avatar).nickName(nickName).updateTime(new Date()).build();
            usersRepository.save(usersEntity);
        }
        return commonDTO;
    }

    private String getWeixinUniqueId(String code) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + "wxffd0f17a532bf00b" + "&secret=" + "42690dadb85962bcf8382356099b9225" + "&js_code=" + code + "&grant_type=" + code;
        String respond = HttpUtil.doGet(url);
        String uniqueId = JSON.parseObject(respond).getString("openid");
        if (!StringUtils.isEmpty(uniqueId)) {
            return uniqueId;
        } else {
            throw new Exception("未获取到微信openId");
        }
    }
}
