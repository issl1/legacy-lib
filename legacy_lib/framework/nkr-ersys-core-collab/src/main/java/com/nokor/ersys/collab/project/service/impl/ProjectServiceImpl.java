package com.nokor.ersys.collab.project.service.impl;

import java.util.List;

import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.model.entity.MainEntity;
import org.seuksa.frmk.service.impl.MainEntityServiceImpl;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.ersys.collab.project.dao.ProjectDao;
import com.nokor.ersys.collab.project.model.Project;
import com.nokor.ersys.collab.project.service.ProjectService;

/**
 * Project Service Impl
 * @author bunlong.taing
 *
 */
@Service("projectService")
public class ProjectServiceImpl extends MainEntityServiceImpl implements ProjectService {

	/**  */
	private static final long serialVersionUID = 4316632697494040962L;
	
	@Autowired
	private ProjectDao dao;
	
	public ProjectServiceImpl() {
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.BaseEntityServiceImpl#getDao()
	 */
	@Override
	public ProjectDao getDao() {
		return dao;
	}
	
	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#createProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void createProcess(MainEntity mainEntity) throws DaoException {
		final Project project = (Project) mainEntity;

        saveOnAction(project.getSubEntitiesToCascadeAction());
        if (!project.getSubListEntitiesToCascade().isEmpty()) {
        	for (final List<EntityA> child : project.getSubListEntitiesToCascade()) {
                saveOnAction(child);
            }
        }

        getDao().create(project);
	}

	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#updateProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void updateProcess(MainEntity mainEntity) throws DaoException {
		Project project = (Project) mainEntity;

        saveOnAction(project.getSubEntitiesToCascadeAction());
        if (!project.getSubListEntitiesToCascade().isEmpty()) {
        	for (final List<EntityA> child : project.getSubListEntitiesToCascade()) {
                saveOnAction(child);
            }
        }
        
        project = getDao().merge(project);
	}

	/**
	 * @see org.seuksa.frmk.service.impl.MainEntityServiceImpl#deleteProcess(org.seuksa.frmk.model.entity.MainEntity)
	 */
	@Override
	public void deleteProcess(MainEntity mainEntity) throws DaoException {
        // TODO: delete other entities first
        throwIntoRecycledBin(mainEntity);
	}

}
