package com.nokor.ersys.vaadin.ui.history;

import java.io.Serializable;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.common.app.workflow.model.WkfHistory;

/**
 * 
 * @author pengleng.huot
 *
 */
public interface IWkfHistoryDataProvider extends Serializable {

	List<? extends Entity> fetchCustomEntities(BaseRestrictions<WkfHistory> histoRestrictions, String customEntity, Integer firstResult, Integer maxResults);
	
	long getTotalRecords(BaseRestrictions<WkfHistory> histoRestrictions, String customEntity);
}
