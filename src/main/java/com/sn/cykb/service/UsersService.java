package com.sn.cykb.service;

import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.UsersDTO;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.UsersVO;

/**
 * @author songning
 * @date 2020/3/9
 * description
 */
public interface UsersService {

    CommonDTO<UsersDTO> getUsersInfo(CommonVO<UsersVO> commonVO);
}
