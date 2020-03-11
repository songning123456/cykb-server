package com.sn.cykb.service.impl;

import com.sn.cykb.dto.CommentsDTO;
import com.sn.cykb.dto.CommonDTO;
import com.sn.cykb.entity.Comments;
import com.sn.cykb.repository.CommentsRepository;
import com.sn.cykb.service.CommentsService;
import com.sn.cykb.vo.CommentsVO;
import com.sn.cykb.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: songning
 * @date: 2020/3/9 22:59
 */
@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Override
    public CommonDTO<CommentsDTO> publishComment(CommonVO<CommentsVO> commonVO) {
        CommonDTO<CommentsDTO> commonDTO = new CommonDTO<>();
        String content = commonVO.getCondition().getContent();
        String contact = commonVO.getCondition().getContact();
        Comments comments = Comments.builder().contact(contact).content(content).updateTime(new Date()).build();
        commentsRepository.save(comments);
        return commonDTO;
    }
}
