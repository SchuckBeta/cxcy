package com.oseasy.pro.modules.promodel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.dao.ProTeamDao;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.sys.modules.team.entity.Team;

/**
 * 团队管理Service
 *
 * @author 刘波
 * @version 2017-03-30
 */
@Service
@Transactional(readOnly = true)
public class ProTeamService extends CrudService<ProTeamDao, Team> {
    public List<ProjectExpVo> findProjectByTeamId(String teamId) {
        return dao.findProjectByTeamId(teamId);
    }

    public List<GContestUndergo> findGContestByTeamId(String teamId) {
        return dao.findGContestByTeamId(teamId);
    }
}