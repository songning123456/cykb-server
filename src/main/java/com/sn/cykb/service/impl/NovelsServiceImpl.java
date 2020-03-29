package com.sn.cykb.service.impl;

import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.NovelsDTO;
import com.sn.cykb.entity.Novels;
import com.sn.cykb.repository.NovelsRepository;
import com.sn.cykb.service.NovelsService;
import com.sn.cykb.thread.TheftProcessor;
import com.sn.cykb.util.ClassConvertUtil;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.NovelsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private TheftProcessor theftProcessor;

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
        src.forEach(item -> {
            NovelsDTO dto = new NovelsDTO();
            dto.setSourceName(String.valueOf(item.get("sourceName")));
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
        Integer recordStartNo = commonVO.getRecordStartNo();
        int pageRecordNum = commonVO.getPageRecordNum();
        String sourceName = commonVO.getCondition().getSourceName();
        List<Novels> src;
        List<NovelsDTO> target = new ArrayList<>();
        if (null != recordStartNo) {
            src = novelsRepository.findFirstClassifyNative(sourceName, pageRecordNum);
        } else {
            Long createTime = commonVO.getCondition().getCreateTime();
            src = novelsRepository.findMoreClassifyNative(sourceName, createTime, pageRecordNum);
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

    @Override
    public <T> CommonDTO<T> theftNovels(String sourceName) {
        CommonDTO<T> commonDTO = new CommonDTO<>();
        if (StringUtils.isEmpty(sourceName)) {
            commonDTO.setStatus(202);
            commonDTO.setMessage("sourceName不能为空!");
            return commonDTO;
        }
        // 如果表里存在就说明 已经开始爬虫了
        List<Novels> novelsList = novelsRepository.findFirstClassifyNative(sourceName, 1);
        if (novelsList != null && novelsList.size() > 0) {
            commonDTO.setMessage("此网站正在爬虫!");
        } else {
            commonDTO.setMessage("准备开始爬虫!");
            if ("笔趣阁".equals(sourceName)) {
                theftProcessor.theftBiquge();
            } else if ("147小说".equals(sourceName)) {
                theftProcessor.theft147();
            }else {
                theftProcessor.testTheft();
            }
        }
        return commonDTO;
    }
}
