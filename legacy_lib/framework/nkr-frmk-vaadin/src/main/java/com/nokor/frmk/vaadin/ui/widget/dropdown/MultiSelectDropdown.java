package com.nokor.frmk.vaadin.ui.widget.dropdown;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.RefDataId;
import org.vaadin.hene.popupbutton.PopupButton;

import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefOptionGroup;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class MultiSelectDropdown<T extends RefDataId> extends PopupButton {

	/** */
	private static final long serialVersionUID = -1563515933182034184L;
	
	private EntityRefOptionGroup<T> optionGroup;

	public MultiSelectDropdown() {
		this(null);
	}
	
	public MultiSelectDropdown(List<T> values) {
		this(null, values);
	}
	/**
	 * 
	 */
	public MultiSelectDropdown(String caption, List<T> values) {
		super(caption);
		optionGroup = new EntityRefOptionGroup<>(values);
		optionGroup.setMultiSelect(true);
		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setSpacing(true);
		
		Button btnSelectAll = new Button(I18N.message("select.all"));
		btnSelectAll.setStyleName(Reindeer.BUTTON_LINK);
		btnSelectAll.addClickListener(new Button.ClickListener() {

			/**	 */
			private static final long serialVersionUID = -7595667870620242624L;

			@Override
			public void buttonClick(ClickEvent event) {
				LinkedHashMap<String, T> valueMap = optionGroup.getValueMap();
				List<T> values = new ArrayList<>(valueMap.values());
				setSelectedEntity(values);
			}
		});
		
		Button btnDeselectAll = new Button(I18N.message("deselect.all"));
		btnDeselectAll.setStyleName(Reindeer.BUTTON_LINK);
		btnDeselectAll.addClickListener(new ClickListener() {

			/**	 */
			private static final long serialVersionUID = -5321642763832391411L;

			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedEntity(null);
			}
		});
		
		topLayout.addComponent(btnSelectAll);
		topLayout.addComponent(btnDeselectAll);
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.setMargin(true);
		vLayout.addComponent(topLayout);
		vLayout.addComponent(optionGroup);

		Panel contentPanel = new Panel();
		contentPanel.setWidth(180, Unit.PIXELS);
		contentPanel.setHeight(200, Unit.PIXELS);
		contentPanel.setContent(vLayout);
		
		this.setContent(contentPanel);
	}

	/**
	 * Add Array of object to MultiselectDropdown
	 * @param itemId
	 */
	public void addItems(Object... items) {
		optionGroup.addItems(items);
	}

	/**
	 * Add collection to MultiselectDropdown
	 * @param itemIds
	 */
	public void addItems(Collection<T> items) {
		optionGroup.addItems(items);
	}

	/**
	 * 
	 * @param datasource
	 */
	public void setContainerDataSource(Container datasource) {
		optionGroup.setContainerDataSource(datasource);
	}

	/**
	 * 
	 * @return
	 */
	public Container getContainerDataSource() {
		return optionGroup.getContainerDataSource();
	}

	/**
	 * 
	 */
	public void renderer() {
		optionGroup.renderer();
	}

	/**
	 * 
	 */
	public void clear() {
		optionGroup.clear();
	}

	/**
	 * 
	 * @return
	 */
	public List<T> getSelectedEntity() {
		return optionGroup.getSelectedEntity();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Long> getSelectedIds() {
		List<T> selectedEntities = optionGroup.getSelectedEntity();
		if (selectedEntities == null) {
			return null;
		}
		List<Long> ids = new ArrayList<>();
		for (T t : selectedEntities) {
			ids.add(t.getId());
		}
		return ids;
	}

	/**
	 * 
	 * @param values
	 */
	public void setSelectedEntity(List<T> values) {
		optionGroup.setSelectedEntity(values);
	}

	/**
	 * 
	 * @param restrictions
	 */
	public void setRestrictions(BaseRestrictions<T> restrictions) {
		optionGroup.setRestrictions(restrictions);
	}

	/**
	 * 
	 * @return
	 */
	public BaseRestrictions<T> getRestrictions() {
		return optionGroup.getRestrictions();
	}
}
