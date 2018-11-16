package com.nokor.common.app.workflow.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.seuksa.frmk.model.entity.EMainEntity;
import org.seuksa.frmk.model.eref.BaseERefEntity;

import com.nokor.common.app.workflow.WorkflowException;
import com.nokor.frmk.security.model.SecProfile;

/**
 * Workflow
 * 
 * @author prasnar
 * 
 */
@Entity
@Table(name = "tu_wkf_flow")
public class EWkfFlow extends BaseERefEntity implements AttributeConverter<EWkfFlow, Long>, MEWkfFlow {
	/** */
	private static final long serialVersionUID = 1639843386777488895L;

	private static final long DEFAULT_ID = 1000;

	private EMainEntity entity;

	private EWkfStatus defaultWkfStatusAtCreation;
	
	private List<WkfFlowItem> items;
	private List<WkfFlowStatus> flowStatuses;
	private List<WkfFlowProfile> flowProfiles;
	
	private Class<? extends EntityWkf> entityWkfClazz;
	/**
	 * 
	 */
	public EWkfFlow() {
	}
	
	/**
	 * 
	 * @param code
	 * @param id
	 */
	public EWkfFlow(String code, long id) {
		super(code, id);
	}
	
	@Override
	public EWkfFlow convertToEntityAttribute(Long id) {
		return super.convertToEntityAttribute(id);
	}
	
	@Override
	public Long convertToDatabaseColumn(EWkfFlow arg0) {
		return super.convertToDatabaseColumn(arg0);
	}
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_flo_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the entity
	 */
    @Column(name = "mai_ent_id", nullable = true) // to false
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
	 * @return the defaultWkfStatusAtCreation
	 */
    @Column(name = "default_at_creation_wkf_sta_id", nullable = true)
    @Convert(converter = EWkfStatus.class)
	public EWkfStatus getDefaultWkfStatusAtCreation() {
		return defaultWkfStatusAtCreation != null ? defaultWkfStatusAtCreation : EWkfStatus.NEW;
	}

	/**
	 * @param defaultWkfStatusAtCreation the defaultWkfStatusAtCreation to set
	 */
	public void setDefaultWkfStatusAtCreation(EWkfStatus defaultWkfStatusAtCreation) {
		this.defaultWkfStatusAtCreation = defaultWkfStatusAtCreation;
	}

	/**
	 * @return the items
	 */
	@OneToMany(mappedBy = "flow", fetch = FetchType.LAZY)
	public List<WkfFlowItem> getItems() {
		if (items == null) {
			items = new ArrayList<>();
		}
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<WkfFlowItem> items) {
		this.items = items;
	}

	/**
	 * @return the flowStatuses
	 */
	@OneToMany(mappedBy = "flow", fetch = FetchType.LAZY)
	public List<WkfFlowStatus> getFlowStatuses() {
		if (flowStatuses == null) {
			flowStatuses = new ArrayList<>();
		}
		return flowStatuses;
	}

	/**
	 * @param flowStatuses the flowStatuses to set
	 */
	public void setFlowStatuses(List<WkfFlowStatus> flowStatuses) {
		this.flowStatuses = flowStatuses;
	}

	/**
	 * @return the flowProfiles
	 */
	@OneToMany(mappedBy = "flow", fetch = FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	public List<WkfFlowProfile> getFlowProfiles() {
		return flowProfiles;
	}

	/**
	 * @param flowProfiles the flowProfiles to set
	 */
	public void setFlowProfiles(List<WkfFlowProfile> flowProfiles) {
		this.flowProfiles = flowProfiles;
	}

	/**
	 * 
	 * @param pro
	 * @return
	 */
	@Transient
	public List<WkfFlowItem> getItems(SecProfile pro) {
		List<WkfFlowItem> allItems = getItems();
		List<WkfFlowItem> itemsPro = new ArrayList<>();
		for (WkfFlowItem item : allItems) {
			if (item.hasProfile(pro)) {
				itemsPro.add(item);
			}
		}
		return itemsPro;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public List<EWkfStatus> getStatuses() {
		List<WkfFlowStatus> flowStatuses = getFlowStatuses();
		List<EWkfStatus> statusList = new ArrayList<>();
		for (WkfFlowStatus floSta : flowStatuses) {
			statusList.add(floSta.getStatus());
		}
		return statusList;
	}

	/**
	 * 
	 * @param eStatus
	 * @return
	 */
	@Transient
	public List<EWkfStatus> getNextWkfStatuses(EWkfStatus eStatus) {
		List<WkfFlowItem> allItems = getItems();
		List<EWkfStatus> statusList = new ArrayList<EWkfStatus>();
		for (WkfFlowItem item : allItems) {
			if ((item.getFromStatus() == null && eStatus == null)
				|| (item.getFromStatus() != null && item.getFromStatus().equals(eStatus))
				) {
				statusList.add(item.getToStatus());
			}
		}
		
		return statusList;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<EWkfFlow> values() {
		return getValues(EWkfFlow.class);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static EWkfFlow getById(long id) {
		EWkfFlow flow = getById(EWkfFlow.class, id);
		if (flow == null) {
			throw new WorkflowException("Error - EWkfFlow [" + id + "] can not be found");
		}
		return flow;
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EWkfFlow getByCode(String code) {
		EWkfFlow flow = getByCode(EWkfFlow.class, code);
		if (flow == null) {
			logger.debug("EWkfFlow [" + code + "] can not be found");
		}
		return flow;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static EWkfFlow getByClass(Class clazz) {
		EMainEntity entity = EMainEntity.getByCode(clazz.getCanonicalName());
		EWkfFlow flow = EWkfFlow.getByCode(clazz.getSimpleName());
		flow.setEntity(entity);
		// Error loop in WorkflowInterceptor
		//ENTITY_SRV.getByField(EWkfFlow.class, EWkfFlow.ENTITY, entity);
		
//		if (!((EWkfFlow)flow.getEntityRef()).getEntity().equals(entity)) {
//			throw new IllegalStateException("Error - Workflow [" + flow.getCode() + "] is not the same as Entity [" + entity.getCode() + "] can not be found");
//		}
		return flow;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public <T extends EntityWkf> Class<? extends WkfBaseHistory> getWkfHistoryClass() {
		try {
			T entityWkf = (T) getEntityWkfClass().newInstance();
			return entityWkf.getWkfHistoryClass();
		} catch (Exception e) {
			String errMsg = "Can not get WkfHistoryClass because instiantiaciation of EntityWkf [" + getCode() + "]";
			logger.error(errMsg, e);
			throw new WorkflowException(errMsg, e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public <T extends EntityWkf> Class<? extends WkfBaseHistoryItem> getWkfHistoryItemClass() {
		try {
			T entityWkf = (T) getEntityWkfClass().newInstance();
			return entityWkf.getWkfHistoryItemClass();
		} catch (Exception e) {
			String errMsg = "Can not get WkfHistoryClass because instiantiaciation of EntityWkf [" + getCode() + "]";
			logger.error(errMsg, e);
			throw new WorkflowException(errMsg, e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public <T extends EntityWkf> Class<? extends WkfBaseTempItem> getWkfTempItemClass() {
		try {
			T entityWkf = (T) getEntityWkfClass().newInstance();
			return entityWkf.getWkfTempItemClass();
		} catch (Exception e) {
			String errMsg = "Can not get WkfHistoryClass because instiantiaciation of EntityWkf [" + getCode() + "]";
			logger.error(errMsg, e);
			throw new WorkflowException(errMsg, e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public <T extends EntityWkf> Class<T> getEntityWkfClass() {
		if (entityWkfClazz == null) {
			try {
				entityWkfClazz = (Class<T>) Class.forName(getEntity().getCode());
			} catch (Exception e) {
				String errMsg = "Can not get EntityWkf class from [" + getEntity().getCode() + "]";
				logger.error(errMsg, e);
				throw new WorkflowException(errMsg, e);
			}
		} 
		return (Class<T>) entityWkfClazz;
	}
	
	/**
	 * 
	 * @return
	 */
	public static EWkfFlow getDefault() {
		EWkfFlow flow = getById(EWkfFlow.class, DEFAULT_ID);
		if (flow == null) {
			logger.debug("EWkfFlow [" + DEFAULT_ID + "] can not be found");
		}
		return flow; 
	}
}
