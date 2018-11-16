package com.nokor.ersys.collab.tools.helper;

import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.ersys.collab.project.service.ProjectService;
import com.nokor.ersys.collab.project.service.TaskService;

/**
 * 
 * @author prasnar
 *
 */
public interface ErsysCollabAppServicesHelper extends AppServicesHelper {

	ProjectService PROJECT_SRV = SpringUtils.getBean(ProjectService.class);
	TaskService TASK_SRV = SpringUtils.getBean(TaskService.class);

}
