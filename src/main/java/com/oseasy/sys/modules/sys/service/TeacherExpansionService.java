package com.oseasy.sys.modules.sys.service;

import com.oseasy.sys.modules.sys.dao.TeacherExpansionDao;
import com.oseasy.sys.modules.sys.entity.TeacherExpansion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/2/23.
 */
@Service
@Transactional(readOnly = true)
public class TeacherExpansionService {
    @Autowired
    private TeacherExpansionDao teacherExpansionDao;

    @Transactional(readOnly = false)
    public void save(TeacherExpansion teacherExpansion) {
        teacherExpansionDao.insert(teacherExpansion);
    }
}
