/**
 *
 */
package com.oseasy.pro.modules.project.entity;

import com.oseasy.act.modules.act.entity.Act;

/**
 * 工作流Entity
 */
public class ProAct extends Act {
	private static final long serialVersionUID = 1L;
	private ProjectDeclare projectDeclare;

	public ProAct() {
		super();
	}

	public ProAct(ProjectDeclare projectDeclare) {
        super();
        this.projectDeclare = projectDeclare;
    }


    public ProjectDeclare getProjectDeclare() {
		return projectDeclare;
	}

	public void setProjectDeclare(ProjectDeclare projectDeclare) {
		this.projectDeclare = projectDeclare;
	}
}


