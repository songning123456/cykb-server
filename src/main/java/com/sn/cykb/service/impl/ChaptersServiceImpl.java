package com.sn.cykb.service.impl;

import com.sn.cykb.dto.ChaptersDTO;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.entity.Chapters;
import com.sn.cykb.repository.ChaptersRepository;
import com.sn.cykb.service.ChaptersService;
import com.sn.cykb.util.ClassConvertUtil;
import com.sn.cykb.vo.ChaptersVO;
import com.sn.cykb.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: songning
 * @date: 2020/3/9 22:58
 */
@Service
public class ChaptersServiceImpl implements ChaptersService {

    @Autowired
    private ChaptersRepository chaptersRepository;

    @Override
    public CommonDTO<ChaptersDTO> directory(CommonVO<ChaptersVO> commonVO) {
        CommonDTO<ChaptersDTO> commonDTO = new CommonDTO<>();
        String novelsId = commonVO.getCondition().getNovelsId();
        List<Map<String, Object>> src = chaptersRepository.findDirectoryNative(novelsId);
        List<ChaptersDTO> target = new ArrayList<>();
        ChaptersDTO dto;
        for (Map<String, Object> item : src) {
            dto = new ChaptersDTO();
            dto.setNovelsId(item.get("id").toString());
            dto.setChapter(item.get("chapter").toString());
            target.add(dto);
        }
        commonDTO.setData(target);
        return commonDTO;
    }

    @Override
    public CommonDTO<ChaptersDTO> unknownTop(CommonVO<ChaptersVO> commonVO) {
        CommonDTO<ChaptersDTO> commonDTO = new CommonDTO<>();
        String novelsId = commonVO.getCondition().getNovelsId();
        Chapters chapters = chaptersRepository.findTopByNovelsIdNative(novelsId);
        List<Map<String, Object>> ext = chaptersRepository.findDirectoryNative(novelsId);
        commonDTO.setListExt(ext);
        ChaptersDTO chaptersDTO = new ChaptersDTO();
        ClassConvertUtil.populate(chapters, chaptersDTO);
        commonDTO.setData(Collections.singletonList(chaptersDTO));
        return commonDTO;
    }
}
