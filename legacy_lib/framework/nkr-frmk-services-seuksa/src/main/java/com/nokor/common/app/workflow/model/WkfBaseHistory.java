package com.nokor.common.app.workflow.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.frmk.security.model.SecUser;

/**
 * History of statuses amended (fromStatus/toStatus) of a given WkfFlow.
 * Possibility to historize the fields before switching fromStatus->toStatus in WkfFlowItem.
 * 
 * @author prasnar
 * 
 */
@MappedSuperclass
public abstract class WkfBaseHistory extends EntityA implements MWkfBaseHistory {
	/** */
	private static final long serialVersionUID = 2129280821192094637L;
	
	protected EWkfFlow flow;
	protected EMainEntity entity;
	protected Long entityId;
	protected List<WkfBaseHistoryItem> histItems;
	protected List<WkfBaseTempItem> tempItems;

	private WkfBaseHistoryItem mainHistItem; // history for fromStatus/toStatus
	private WkfBaseTempItem mainTempItem; // history for fromStatus/toStatus
	

	/**
     * 
     * @return
     */
    public static <T extends WkfBaseHistory> T createInstance(Class<T> hisClazz, EWkfFlow flow) {
    	T his = EntityFactory.createInstance(hisClazz);
    	his.flow = flow; 
        return his;
    }
    
	/**
	 * @return the flow
	 */
    @Column(name = "wkf_flo_id", nullable = true)
    @Convert(converter = EWkfFlow.class)
	public EWkfFlow getFlow() {
		return flow;
	}

	/**
	 * @param flow the flow to set
	 */
	public void setFlow(EWkfFlow flow) {
		this.flow = flow;
	}

	/**
     * @return the entityId
     */
    @Column(name = "his_entity_id", nullable = false)
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(final Long entityId) {
        this.entityId = entityId;
    }

    /**
	 * @return the entity
	 */
    @Column(name = "mai_ent_id", nullable = false)
    @Convert(converter = EMainEntity.class)
	public EMainEntity getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(EMainEntity entity) {
		this.entity = entity;
	}
	
	/**
	 * @return the fromStatus
	 */
	@Transient
	public EWkfStatus getFromStatus() {
		return EWkfStatus.getById(Long.valueOf(getMainHistItem().getOldValue()));
	}

	
	/**
	 * @return the toStatus
	 */
	@Transient
	public EWkfStatus getToStatus() {
		return EWkfStatus.getById(Long.valueOf(getMainHistItem().getNewValue()));
	}

	
	/**
	 * @return the changeDate
	 */
	@Transient
	public Date getChangeDate() {
		return getMainHistItem().getChangeDate();
	}


	/**
	 * @return the comment
	 */
	@Transient
	public String getComment() {
		return getMainHistItem().getComment();
	}

	

	/**
	 * @return the secUser
	 */
	@Transient
 	public SecUser getSecUser() {
		return getMainHistItem().getModifiedBy();
	}


	/**
	 * @return the mainHistory
	 */
	@Transient
	public WkfBaseHistoryItem getMainHistItem() {
		if (mainHistItem == null) {
			List<? extends WkfBaseHistoryItem> histItems = getHistItems();
			for (WkfBaseHistoryItem entry : histItems) {
				if (entry.getWkfHistory().getId().equals(getId())) {
					mainHistItem = entry;
					break;
				}
			}
			 
		}
		return mainHistItem;
	}

	/**
	 * @return the mainTemp
	 */
	@Transient
	public WkfBaseTempItem getmainTempItem() {
		if (mainTempItem == null) {
			List<? extends WkfBaseTempItem> tempItems = getTempItems();
			for (WkfBaseTempItem entry : tempItems) {
				if (entry.getWkfHistory().getId().equals(getId())) {
					mainTempItem = entry;
					break;
				}
			}
			 
		}
		return mainTempItem;
	}
	/**
	 * @return the items
	 */
	@Transient
	public List<? extends WkfBaseHistoryItem> getHistItems() {
		if (histItems == null) {
			histItems = new ArrayList<>();
		}
		return histItems;
	}

	/**
	 * 
	 * @param item
	 */
	@Transient
	public void addHistItem(WkfBaseHistoryItem item) {
		if (histItems == null) {
			histItems = new ArrayList<>();
		}
		histItems.add(item);
	}
	/**
	 * @param items the items to set
	 */
	public void setHistItems(List<? extends WkfBaseHistoryItem> items) {
		this.histItems = (List<WkfBaseHistoryItem>) items;
	}

	/**
	 * @return the items
	 */
	@Transient
	public List<? extends WkfBaseTempItem> getTempItems() {
		if (tempItems == null) {
			tempItems = new ArrayList<>();
		}
		return tempItems;
	}

	/**
	 * 
	 * @param item
	 */
	@Transient
	public void addTempItem(WkfBaseTempItem item) {
		if (tempItems == null) {
			tempItems = new ArrayList<>();
		}
		tempItems.add(item);
	}
	/**
	 * @param items the items to set
	 */
	public void setTempItems(List<? extends WkfBaseTempItem> items) {
		this.tempItems = (List<WkfBaseTempItem>) items;
	}
	
}
