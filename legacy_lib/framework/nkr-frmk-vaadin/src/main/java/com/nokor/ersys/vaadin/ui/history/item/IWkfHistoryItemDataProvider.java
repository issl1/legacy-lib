package com.nokor.ersys.vaadin.ui.history.item;

import java.io.Serializable;
import java.util.List;

import org.seuksa.frmk.model.entity.Entity;

import com.nokor.common.app.workflow.model.WkfBaseHistory;

public interface IWkfHistoryItemDataProvider extends Serializable {

	List<? extends Entity> fetchCustomEntities(WkfBaseHistory baseWkfHistory, String customEntity, Long entityId);
}
