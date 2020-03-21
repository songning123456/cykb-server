package com.sn.cykb.service.impl;

import com.sn.cykb.constant.HttpStatus;
import com.sn.cykb.dto.ChaptersDTO;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.UsersNovelsRelationDTO;
import com.sn.cykb.entity.Chapters;
import com.sn.cykb.entity.Novels;
import com.sn.cykb.entity.UsersNovelsRelation;
import com.sn.cykb.repository.ChaptersRepository;
import com.sn.cykb.repository.NovelsRepository;
import com.sn.cykb.repository.UsersNovelsRelationRepository;
import com.sn.cykb.service.UsersNovelsRelationService;
import com.sn.cykb.util.ClassConvertUtil;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.UsersNovelsRelationVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: songning
 * @date: 2020/3/9 22:59
 */
@Service
public class UsersNovelsRelationServiceImpl implements UsersNovelsRelationService {

    @Autowired
    private UsersNovelsRelationRepository usersNovelsRelationRepository;
    @Autowired
    private NovelsRepository novelsRepository;
    @Autowired
    private ChaptersRepository chaptersRepository;

    @Override
    public CommonDTO<UsersNovelsRelationDTO> bookcase(CommonVO<UsersNovelsRelationVO> commonVO) {
        CommonDTO<UsersNovelsRelationDTO> commonDTO = new CommonDTO<>();
        String uniqueId = commonVO.getCondition().getUniqueId();
        List<String> novelsIds = usersNovelsRelationRepository.findByUniqueIdNative(uniqueId);
        if (novelsIds.isEmpty()) {
            commonDTO.setTotal(0L);
            commonDTO.setStatus(HttpStatus.HTTP_ACCEPTED);
            commonDTO.setMessage("书架暂无您的书籍");
        } else {
            String sortType = commonVO.getCondition().getSortType();
            List<UsersNovelsRelationDTO> target = new ArrayList<>();
            if ("最近阅读".equals(sortType)) {
                // 根据 最近阅读 排序 users_novels_relation => update_time
                UsersNovelsRelationDTO relationDTO;
                for (String novelsId : novelsIds) {
                    relationDTO = new UsersNovelsRelationDTO();
                    Novels novels = novelsRepository.findById(novelsId).get();
                    ClassConvertUtil.populate(novels, relationDTO);
                    target.add(relationDTO);
                }
            } else {
                // 根据最近更新排序 novels => update_time
                List<Novels> src = novelsRepository.findAllByIdInOrderByUpdateTimeDesc(novelsIds);
                ClassConvertUtil.populateList(src, target, UsersNovelsRelationDTO.class);
            }
            commonDTO.setData(target);
            commonDTO.setTotal((long) target.size());
        }
        return commonDTO;
    }

    @Override
    public CommonDTO<UsersNovelsRelationDTO> insertBookcase(CommonVO<UsersNovelsRelationVO> commonVO) {
        CommonDTO<UsersNovelsRelationDTO> commonDTO = new CommonDTO<>();
        String novelsId = commonVO.getCondition().getNovelsId();
        String uniqueId = commonVO.getCondition().getUniqueId();
        UsersNovelsRelation relation = UsersNovelsRelation.builder().novelsId(novelsId).uniqueId(uniqueId).updateTime(new Date()).build();
        usersNovelsRelationRepository.save(relation);
        return commonDTO;
    }

    @Override
    public CommonDTO<UsersNovelsRelationDTO> topBookcase(CommonVO<UsersNovelsRelationVO> commonVO) {
        CommonDTO<UsersNovelsRelationDTO> commonDTO = new CommonDTO<>();
        String uniqueId = commonVO.getCondition().getUniqueId();
        String novelsId = commonVO.getCondition().getNovelsId();
        usersNovelsRelationRepository.updateByRecentReadNative(uniqueId, novelsId, new Date());
        return commonDTO;
    }

    @Override
    public CommonDTO<UsersNovelsRelationDTO> deleteBookcase(CommonVO<UsersNovelsRelationVO> commonVO) {
        CommonDTO<UsersNovelsRelationDTO> commonDTO = new CommonDTO<>();
        String uniqueId = commonVO.getCondition().getUniqueId();
        String novelsId = commonVO.getCondition().getNovelsId();
        usersNovelsRelationRepository.deleteByUniqueIdAndNovelsId(uniqueId, novelsId);
        return commonDTO;
    }

    @Override
    public CommonDTO<UsersNovelsRelationDTO> beginReading(CommonVO<UsersNovelsRelationVO> commonVO) {
        CommonDTO<UsersNovelsRelationDTO> commonDTO = new CommonDTO<>();
        String uniqueId = commonVO.getCondition().getUniqueId();
        String novelsId = commonVO.getCondition().getNovelsId();
        UsersNovelsRelation relation = usersNovelsRelationRepository.findByUniqueIdAndAndNovelsId(uniqueId, novelsId);
        Chapters chapters;
        // 已加入书架， 但未开始阅读，将第一章加入书架
        if (StringUtils.isEmpty(relation.getCurrentChapterId())) {
            chapters = chaptersRepository.findTopByNovelsIdNative(novelsId);
            usersNovelsRelationRepository.updateChapterIdNative(chapters.getId(), new Date(), uniqueId, novelsId);
        } else {
            String currentChapterId = relation.getCurrentChapterId();
            chapters = chaptersRepository.findById(currentChapterId).get();
            usersNovelsRelationRepository.updateUpdateTimeNative(new Date(), uniqueId, novelsId);
        }
        UsersNovelsRelationDTO dto = new UsersNovelsRelationDTO();
        dto.setContent(chapters.getContent());
        commonDTO.setData(Collections.singletonList(dto));
        return commonDTO;
    }

    @Override
    public CommonDTO<UsersNovelsRelationDTO> isExist(CommonVO<UsersNovelsRelationVO> commonVO) {
        CommonDTO<UsersNovelsRelationDTO> commonDTO = new CommonDTO<>();
        String uniqueId = commonVO.getCondition().getUniqueId();
        String novelsId = commonVO.getCondition().getNovelsId();
        UsersNovelsRelation relation = usersNovelsRelationRepository.findByUniqueIdAndAndNovelsId(uniqueId, novelsId);
        if (relation == null) {
            commonDTO.setStatus(202);
        }
        return commonDTO;
    }
}
