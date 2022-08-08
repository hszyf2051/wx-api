package com.yif.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yif.entity.Question;
import com.yif.mapper.QuestionMapper;
import com.yif.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author admin
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
}
