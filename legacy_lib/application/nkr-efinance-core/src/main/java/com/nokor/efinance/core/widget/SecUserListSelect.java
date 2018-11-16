package com.nokor.efinance.core.widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;

import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.vaadin.ui.ListSelect;

/**
 * SecUser List Select
 * @author bunlong.taing
 *
 * @param <T>
 */
public class SecUserListSelect<T extends SecUser> extends ListSelect {

	/** */
	private static final long serialVersionUID = -4687780878568153465L;
	private EntityService entityService;
	private BaseRestrictions<T> restrictions;
	private boolean onlyActive;
	protected LinkedHashMap<String, T> valueMap;
	
	/** */
	public SecUserListSelect() {
		this(null);
	}
	
	/**
	 * @param caption
	 */
	public SecUserListSelect(String caption) {
		this(caption, true);
	}
	
	/**
	 * @param caption
	 * @param onlyActive
	 */
	public SecUserListSelect(String caption, boolean onlyActive) {
		super(I18N.message(caption));

		this.valueMap = new LinkedHashMap<String, T>();
		this.entityService = ((EntityService) SecApplicationContextHolder.getContext().getBean("entityService"));
		setNullSelectionAllowed(true);
		setMultiSelect(false);
	}
	
	/** */
	public void renderer() {
		clear();
		if ((this.restrictions.getOrders() == null)
				|| (this.restrictions.getOrders().isEmpty())) {
			this.restrictions.addOrder(Order.asc("login"));
		}
		List<T> entities = this.entityService.list(this.restrictions);
		addEntities(entities);
	}
	
	/** */
	public void clear() {
		this.valueMap.clear();
		removeAllItems();
		setValue(null);
	}
	
	/**
	 * Get all the selected Entities
	 * @return
	 */
	public List<T> getSelectedEntities () {
		List<T> selectedValue = new ArrayList<T>();
		if (isMultiSelect()) {
			Object[] keys = ((Set) getValue()).toArray();
			for (Object string : keys) {
				selectedValue.add(valueMap.get((String) string));
			}
		} else {
			if (getValue() != null) {
				selectedValue.add(valueMap.get((String) getValue()));
			}
		}
		return selectedValue;
	}
	
	/**
	 * @param values
	 */
	public void setSelectedEntities (List<T> values) {
		if (values != null) {
			List<String> selectedEntities = new ArrayList<String>();
			if (isMultiSelect()) {
				for(T refEntity : values) {
					selectedEntities.add(refEntity.getId().toString());
					setValue(selectedEntities);
				}
			} else {
				if (!values.isEmpty()) {
					setValue(values.get(0));
				}
			}
		} else {
			setValue(null);
		}
	}
	
	/**
	 * Add = entity
	 * @param entity
	 */
	public void addEntity(T entity) {
		if (!valueMap.containsKey(entity.getId().toString())) {
			addItem(entity.getId().toString());
			setItemCaption(entity.getId().toString(), entity.getLogin());
			valueMap.put(entity.getId().toString(), entity);
		}
	}
	
	/**
	 * Add a list of entities
	 * @param entities
	 */
	public void addEntities(List<T> entities) {
		if (entities != null) {
			for (T t : entities) {
				addEntity(t);
			}
		}
	}
	
	/**
	 * Remove the entity.
	 * @param entity
	 */
	public void removeEntity(T entity) {
		removeItem(entity.getId().toString());
		valueMap.remove(entity.getId().toString());
	}
	
	/**
	 * Remove a list of entities
	 * @param entities
	 */
	public void removeEntities(List<T> entities) {
		if (entities != null) {
			for (T t : entities) {
				removeEntity(t);
			}
		}
	}
	
	/**
	 * @return
	 */
	public BaseRestrictions<T> getRestrictions() {
		return this.restrictions;
	}
	
	/**
	 * @param restrictions
	 */
	public void setRestrictions(BaseRestrictions<T> restrictions) {
		this.restrictions = restrictions;
		if (this.onlyActive) {
			this.restrictions.addCriterion("statusRecord", FilterMode.EQUALS, new Object[] { EStatusRecord.ACTIV });
		}
	}
	
	/**
	 * get all entities in ListSelect
	 * @return
	 */
	public List<T> getAllEntities() {
		return new ArrayList<T>(valueMap.values());
	}
	
}
