package com.sn.cykb.service.impl;

import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.entity.Novels;
import com.sn.cykb.repository.NovelsRepository;
import com.sn.cykb.service.NovelsService;
import com.sn.cykb.util.ClassConvertUtil;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.NovelsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2020/3/9 22:59
 */
@Service
public class NovelsServiceImpl implements NovelsService {

    @Autowired
    private NovelsRepository novelsRepository;

    @Override
    public CommonDTO<NovelsDTO> homePage(CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        int recordStartNo = commonVO.getRecordStartNo();
        int pageRecordNum = commonVO.getPageRecordNum();
        Sort sort = Sort.by(Sort.Direction.ASC, "update_time");
        Pageable pageable = PageRequest.of(recordStartNo, pageRecordNum, sort);
        Page<Novels> page = novelsRepository.findNovelsNative(pageable);
        List<Novels> src = page.getContent();
        List<NovelsDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        commonDTO.setTotal(page.getTotalElements());
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> classify(CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        String sex = commonVO.getCondition().getSex();
        List<Map<String, Object>> src = novelsRepository.countBySexNative(sex);
        List<NovelsDTO> target = new ArrayList<>();
        src.forEach(item -> {
            NovelsDTO dto = new NovelsDTO();
            dto.setCategory(String.valueOf(item.get("category")));
            dto.setTotal(Integer.parseInt(item.get("total").toString()));
            target.add(dto);
        });
        commonDTO.setTotal((long) src.size());
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> classifyResult(CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        int recordStartNo = commonVO.getRecordStartNo();
        int pageRecordNum = commonVO.getPageRecordNum();
        String sex = commonVO.getCondition().getSex();
        String category = commonVO.getCondition().getCategory();
        Sort sort = Sort.by(Sort.Direction.ASC, "update_time");
        Pageable pageable = PageRequest.of(recordStartNo, pageRecordNum, sort);
        Page<Novels> page = novelsRepository.findBySexAndCategoryNative(sex, category, pageable);
        List<Novels> src = page.getContent();
        List<NovelsDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        commonDTO.setTotal(page.getTotalElements());
        return commonDTO;
    }
}
