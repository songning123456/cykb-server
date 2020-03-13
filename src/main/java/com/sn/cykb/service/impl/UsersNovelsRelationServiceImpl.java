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
            List<Novels> src = novelsRepository.findAllByIdInOrderByUpdateTimeDesc(novelsIds);
            List<UsersNovelsRelationDTO> target = new ArrayList<>();
            ClassConvertUtil.populateList(src, target, UsersNovelsRelationDTO.class);
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
}
