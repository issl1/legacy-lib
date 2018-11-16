package com.nokor.efinance.core.widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.criteria.FilterMode;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.vaadin.ui.ListSelect;

/**
 * Multi-selection list
 * @author bunlong.taing
 * @param <T>
 */
public class AreaListSelect extends ListSelect implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -8142490059427163234L;
	private BaseRestrictions<Area> restrictions;
	private boolean onlyActive;
	protected LinkedHashMap<String, Area> valueMap;
	
	/** */
	public AreaListSelect() {
		this(null);
	}
	
	/**
	 * @param caption
	 */
	public AreaListSelect(String caption) {
		this(caption, false);
	}
	
	/**
	 * @param caption
	 * @param onlyActive
	 */
	public AreaListSelect(String caption, boolean onlyActive) {
		super(I18N.message(caption));

		this.valueMap = new LinkedHashMap<String, Area>();
		setNullSelectionAllowed(true);
		setMultiSelect(true);
	}
	
	/**
	 * @return
	 */
	public BaseRestrictions<Area> getRestrictions() {
		return this.restrictions;
	}
	
	/**
	 * @param restrictions
	 */
	public void setRestrictions(BaseRestrictions<Area> restrictions) {
		this.restrictions = restrictions;
		if (this.onlyActive) {
			this.restrictions.addCriterion("statusRecord", FilterMode.EQUALS, new Object[] { EStatusRecord.ACTIV });
		}
	}
	
	/** */
	public void renderer() {
		this.valueMap.clear();
		removeAllItems();
		if ((this.restrictions.getOrders() == null)
				|| (this.restrictions.getOrders().isEmpty())) {
			this.restrictions.addOrder(Order.asc("descEn"));
		}
		List<Area> entities = ENTITY_SRV.list(this.restrictions);
		for (Area area : entities) {
			addItem(area.getId().toString());
			setItemCaption(area.getId().toString(), getDetailArea(area));
			this.valueMap.put(area.getId().toString(), area);
		}
		setValue(null);
	}
	
	private String getDetailArea(Area area) {
		List<String> descriptions = new ArrayList<>();
		if (StringUtils.isNotBlank(area.getLine1())) {
			descriptions.add(area.getLine1());
		}
		if (StringUtils.isNotBlank(area.getLine2())) {
			descriptions.add(area.getLine2());
		}
		
		if (StringUtils.isNotBlank(area.getStreet())) {
			descriptions.add(area.getStreet());
		}
		
		if (area.getCommune() != null && StringUtils.isNotBlank(area.getCommune().getDescEn())) {
			descriptions.add(area.getCommune().getDescEn());
		}
		if (area.getDistrict() != null && StringUtils.isNotBlank(area.getDistrict().getDescEn())) {
			descriptions.add(area.getDistrict().getDescEn());
		}
		if (area.getProvince() != null && StringUtils.isNotBlank(area.getProvince().getDescEn())) {
			descriptions.add(area.getProvince().getDescEn());
		}
		
		return StringUtils.join(descriptions, ",");
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
	public List<Area> getSelectedEntities () {
		List<Area> selectedValue = new ArrayList<>();
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
	public void setSelectedEntities (List<Area> values) {
		if (values != null) {
			List<String> selectedEntities = new ArrayList<>();
			for(Area refEntity : values) {
				selectedEntities.add(refEntity.getId().toString());
			}
			setValue(selectedEntities);
		} else {
			setValue(null);
		}
	}
	

	/**
	 * Add = entity
	 * @param area
	 */
	public void addEntity(Area area) {
		if (!valueMap.containsKey(area.getId().toString())) {
			addItem(area.getId().toString());
			setItemCaption(area.getId().toString(), getDetailArea(area));
			valueMap.put(area.getId().toString(), area);
		}
	}
	
	/**
	 * Add a list of entities
	 * @param areas
	 */
	public void addEntities(List<Area> areas) {
		if (areas != null) {
			for (Area area : areas) {
				addEntity(area);
			}
		}
	}
	
	/**
	 * Remove the entity.
	 * @param area
	 */
	public void removeEntity(Area area) {
		removeItem(area.getId().toString());
		valueMap.remove(area.getId().toString());
	}
	
	/**
	 * Remove a list of entities
	 * @param areas
	 */
	public void removeEntities(List<Area> areas) {
		if (areas != null) {
			for (Area area : areas) {
				removeEntity(area);
			}
		}
	}
	
	/**
	 * get all entities in ListSelect
	 * @return
	 */
	public List<Area> getAllEntities() {
		return new ArrayList<Area>(valueMap.values());
	}
}
