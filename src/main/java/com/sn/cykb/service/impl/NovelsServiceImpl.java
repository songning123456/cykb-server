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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
        Integer recordStartNo = commonVO.getRecordStartNo();
        int pageRecordNum = commonVO.getPageRecordNum();
        List<Novels> src;
        List<NovelsDTO> target = new ArrayList<>();
        if (null != recordStartNo) {
            // 第一次查询
            src = novelsRepository.findFirstHomePageNative(pageRecordNum);
        } else {
            // 第二次开始查询
            Long createTime = commonVO.getCondition().getCreateTime();
            src = novelsRepository.findMoreHomePageNative(createTime, pageRecordNum);
        }
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> classifyCount() {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        List<Map<String, Object>> src = novelsRepository.countSourceNative();
        List<NovelsDTO> target = new ArrayList<>();
        Map<String, Object> dataExt = new HashMap<>(2);
        src.forEach(item -> {
            NovelsDTO dto = new NovelsDTO();
            String sourceName = String.valueOf(item.get("sourceName"));
            dto.setSourceName(sourceName);
            List<Map<String, Object>> mapList = novelsRepository.countCategoryBySourceNative(sourceName);
            dataExt.put(sourceName, mapList);
            dto.setTotal(Integer.parseInt(item.get("total").toString()));
            target.add(dto);
        });
        commonDTO.setTotal((long) src.size());
        commonDTO.setData(target);
        commonDTO.setDataExt(dataExt);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> classifyResult(CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        int pageRecordNum = commonVO.getPageRecordNum();
        String sourceName = commonVO.getCondition().getSourceName();
        String category = commonVO.getCondition().getCategory();
        List<Novels> src;
        List<NovelsDTO> target = new ArrayList<>();
        if (null != recordStartNo) {
            src = novelsRepository.findFirstClassifyNative(sourceName, category, pageRecordNum);
        } else {
            Long createTime = commonVO.getCondition().getCreateTime();
            src = novelsRepository.findMoreClassifyNative(sourceName, category, createTime, pageRecordNum);
        }
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> sameAuthor(CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        String author = commonVO.getCondition().getAuthor();
        List<Novels> src = novelsRepository.findByAuthorOrderByCreateTimeDesc(author);
        List<NovelsDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        commonDTO.setTotal((long) target.size());
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> fastSearch(String authorOrTitle) {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        List<Novels> src = novelsRepository.findByAuthorOrTitleNative(authorOrTitle);
        List<NovelsDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<NovelsDTO> searchResult(CommonVO<NovelsVO> commonVO) {
        CommonDTO<NovelsDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        int pageRecordNum = commonVO.getPageRecordNum();
        String authorOrTitle = commonVO.getCondition().getAuthorOrTitle();
        List<Novels> src;
        List<NovelsDTO> target = new ArrayList<>();
        if (recordStartNo != null) {
            src = novelsRepository.findFirstByAuthorOrTitleNative(authorOrTitle, pageRecordNum);
        } else {
            Long createTime = commonVO.getCondition().getCreateTime();
            src = novelsRepository.findMoreByAuthorOrTitleNative(authorOrTitle, createTime, pageRecordNum);
        }
        ClassConvertUtil.populateList(src, target, NovelsDTO.class);
        commonDTO.setData(target);
        return commonDTO;
    }
}
