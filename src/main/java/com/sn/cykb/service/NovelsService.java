package com.sn.cykb.service;

import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.NovelsVO;

/**
 * @author: songning
 * @date: 2020/3/9 22:58
 */
public interface NovelsService {

    CommonDTO<NovelsDTO> homePage(CommonVO<NovelsVO> commonVO);

    CommonDTO<NovelsDTO> classify(CommonVO<NovelsVO> commonVO);

    CommonDTO<NovelsDTO> classifyResult(CommonVO<NovelsVO> commonVO);
}
