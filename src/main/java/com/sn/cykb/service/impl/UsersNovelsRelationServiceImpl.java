package com.sn.cykb.service.impl;

import com.sn.cykb.constant.HttpStatus;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.dto.UsersNovelsRelationDTO;
import com.sn.cykb.entity.Novels;
import com.sn.cykb.entity.UsersNovelsRelation;
import com.sn.cykb.repository.NovelsRepository;
import com.sn.cykb.repository.UsersNovelsRelationRepository;
import com.sn.cykb.service.UsersNovelsRelationService;
import com.sn.cykb.util.ClassConvertUtil;
import com.sn.cykb.vo.CommonVO;
import com.sn.cykb.vo.UsersNovelsRelationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        List<String> novelsIds = usersNovelsRelationRepository.findByUniqueIdNative(uniqueId);
        UsersNovelsRelationDTO relationDTO;
        List<UsersNovelsRelationDTO> target = new ArrayList<>();
        // 置顶后根据relation updateTime重新排序(即 最近阅读排序)
        for (String novels_id : novelsIds) {
            relationDTO = new UsersNovelsRelationDTO();
            Novels novels = novelsRepository.findById(novels_id).get();
            ClassConvertUtil.populate(novels, relationDTO);
            target.add(relationDTO);
        }
        commonDTO.setData(target);
        return commonDTO;
    }
}
