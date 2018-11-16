package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.service.AreaRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class AreaSelectPopupPanel extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -3540570662558160006L;

	private static final String SELECT = "select";

	private TextField txtCode;
	
	protected Button btnSearch;
	protected Button btnReset;
	
	private Button btnSelect;	
	private SimpleTable<Area> simpleTable;	
	private Listener selectListener = null;
	private List<Long> areas;
	
	/**
	 * @return the areas
	 */
	public List<Long> getAreas() {
		return areas;
	}
	
	/**
	 * 
	 * @author seanglay.chhoeurn
	 *
	 */
	public interface Listener extends Serializable {
        void onClose(AreaSelectPopupPanel dialog);
    }
	
    /**
     * Show a modal ConfirmDialog in a window.
     * @param selectListener
     */
    public void show() {
    	UI.getCurrent().addWindow(this);
    }
    
    public AreaSelectPopupPanel() {
		super.center();
		setModal(true);
		
		VerticalLayout containLayout = new VerticalLayout();
		containLayout.setMargin(true);
		containLayout.setSpacing(true);
		
		btnSelect = new Button(I18N.message("select"));
		btnSelect.setIcon(FontAwesome.CHECK_SQUARE_O);

        Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
                if (selectListener != null) {
                    selectListener.onClose(AreaSelectPopupPanel.this);
                }
                UI.getCurrent().removeWindow(AreaSelectPopupPanel.this);
            }
        };
		
        btnSelect.addClickListener(cb);
        
		VerticalLayout buttonSelectLayout = new VerticalLayout();
		buttonSelectLayout.addComponent(btnSelect);
		
		simpleTable = new SimpleTable<>(createColumnDefinitions());
		simpleTable.setCaption(null);		
		containLayout.addComponent(createSearchForm());
		containLayout.addComponent(buttonSelectLayout);
		containLayout.addComponent(simpleTable);
		setContent(containLayout);
	}
    
    /**
     * 
     * @return
     */
    private Component createSearchForm() {
		
		Panel searchPanel = new Panel(I18N.message("search"));
		
		VerticalLayout containLayout = new VerticalLayout();
		containLayout.setStyleName("panel-search");
		containLayout.setMargin(true);

		btnSearch = new Button(I18N.message("search"));
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
			btnSearch.setIcon(FontAwesome.SEARCH);
        } else {
        	btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
        }
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setImmediate(true);
		btnSearch.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = 5946005010475128171L;

			@Override
			public void buttonClick(ClickEvent event) {
				search();
				
			}
		});
				
		btnReset = new Button(I18N.message("reset"));
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
			btnReset.setIcon(FontAwesome.ERASER);
        } else {
        	btnReset.setIcon(new ThemeResource("../nkr-default/icons/16/reset.png"));
        }
		btnReset.setClickShortcut(KeyCode.ESCAPE, null);
		btnReset.setImmediate(true);
		btnReset.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -3615554511676495240L;

			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setStyleName("panel-search-center");
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		final HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setSpacing(true);
		        
		txtCode = ComponentFactory.getTextField("code", false, 60, 150);
		
        final FormLayout formLayout1 = new FormLayout();
        final FormLayout formLayout2 = new FormLayout();
        
        formLayout2.addComponent(txtCode);
        
        formLayout.addComponent(formLayout1);
        formLayout.addComponent(formLayout2);
        
        containLayout.addComponent(formLayout);
		containLayout.addComponent(ComponentFactory.getVerticalLayout(10));
		containLayout.addComponent(buttonsLayout);
        
		searchPanel.setContent(containLayout);
		
		return searchPanel;
	}
    
    /**
     * 
     * @param selectListener
     */
    public void show(final Listener selectListener) {
    	this.selectListener = selectListener;
    	UI.getCurrent().addWindow(this);
    }
    
    /**
     * search
     */
    private void search() {
		simpleTable.removeAllItems();
		setTableDataSource(ENTITY_SRV.list(getRestrictions()));
	}
    
    /**
     * 
     * @return restrictions
     */
    private BaseRestrictions<Area> getRestrictions() {			
    	AreaRestriction restrictions = new AreaRestriction();
    	if (ProfileUtil.isColField()) {
    		restrictions.setColType(EColType.FIELD);
    	} else if (ProfileUtil.isColInsideRepo()) {
    		restrictions.setColType(EColType.INSIDE_REPO);
    	} else if (ProfileUtil.isColOA()) {
    		restrictions.setColType(EColType.OA);
    	} 
		if (StringUtils.isNotEmpty(txtCode.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("code", txtCode.getValue(), MatchMode.ANYWHERE));
		}
		return restrictions;
	}
    
    /**
     * reset
     */
    public void reset() {
		txtCode.setValue("");
		simpleTable.removeAllItems();
	}
    
    /**
     * 
     * @param areas
     */
    @SuppressWarnings("unchecked")
	private void setTableDataSource(List<Area> areas) {
		if (areas != null) {
			for (Area area : areas) {
				Item item = simpleTable.addItem(area.getId());
				CheckBox cbSelect = new CheckBox();
				cbSelect.setValue(false);
				item.getItemProperty(SELECT).setValue(cbSelect);
				item.getItemProperty(Area.ID).setValue(area.getId());
				item.getItemProperty(Area.CODE).setValue(area.getCode());
				item.getItemProperty("line2").setValue(area.getLine2());
			}
		}
	}
    
    /**
     * 
     * @return columnDefinitions
     */
    private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(SELECT, I18N.message("check"), CheckBox.class, Align.LEFT, 30));
		columnDefinitions.add(new ColumnDefinition(Area.ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Area.CODE, I18N.message("code"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("line2", I18N.message("line2"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}
    
    /**
     * 
     * @return areas
     */
    @SuppressWarnings("rawtypes")
	public List<Long> getSelectedIds() {
		areas = new ArrayList<>();
		for (Iterator i = simpleTable.getItemIds().iterator(); i.hasNext();) {
		    Long id = (Long) i.next();
		    Item item = simpleTable.getItem(id);
		    CheckBox cbSelect = (CheckBox) item.getItemProperty("select").getValue();
		    if (cbSelect.getValue()) {
		    	areas.add(id);
		    }
		}
		return areas;
	}

}
