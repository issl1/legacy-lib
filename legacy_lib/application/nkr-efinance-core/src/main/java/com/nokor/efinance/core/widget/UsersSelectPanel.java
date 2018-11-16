package com.nokor.efinance.core.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
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
 * @author youhort.ly
 *
 */
public class UsersSelectPanel extends Window implements FinServicesHelper {
	
	/**
	 */
	private static final long serialVersionUID = 9115931175918084143L;

	private static final String SELECT = "select";

	// private TextField txtCode;
	private TextField txtName;
	
	protected Button btnSearch;
	protected Button btnReset;
	
	private Button btnSelect;	
	private SimpleTable<SecUser> simpleTable;	
	private Listener selectListener = null;
	
	private String[] profiles;
	
	public interface Listener extends Serializable {
        void onClose(UsersSelectPanel dialog);
    }

    /**
     * Show a modal ConfirmDialog in a window.
     * @param selectListener
     */
    public void show(final String[] profiles, final Listener selectListener) {
    	this.profiles = profiles;
    	this.selectListener = selectListener;
    	UI.getCurrent().addWindow(this);
    }
    
	/**
	 */
	public UsersSelectPanel() {
		super.center();
		setModal(true);
		
		VerticalLayout containLayout = new VerticalLayout();
		containLayout.setMargin(true);
		containLayout.setSpacing(true);
		
		btnSelect = new Button(I18N.message("select"));
		btnSelect.setIcon(FontAwesome.CHECK_SQUARE_O);
		
		 // Create a listener for buttons
        Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
                if (selectListener != null) {
                    selectListener.onClose(UsersSelectPanel.this);
                }
                UI.getCurrent().removeWindow(UsersSelectPanel.this);
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
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	private void reset() {
		// txtCode.setValue("");
		txtName.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
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
			/**	 */
			private static final long serialVersionUID = -5644792188986529962L;

			@Override
			public void buttonClick(ClickEvent event) {
				assignValues();
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
			/**	 */
			private static final long serialVersionUID = 3257468831521150461L;

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
		
		// txtCode = ComponentFactory.getTextField("dealershop.id", false, 60, 150);         
		txtName = ComponentFactory.getTextField("name", false, 60, 150);
		
        final FormLayout formLayout1 = new FormLayout();
        final FormLayout formLayout2 = new FormLayout();
        
        
        // formLayout1.addComponent(txtCode);
        formLayout2.addComponent(txtName);
        
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
	 * @param entityId
	 * @return
	 */
	private BaseRestrictions<SecUser> getRestrictions() {					
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addAssociation("defaultProfile", "pro", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.in("pro.code", profiles));
		if (StringUtils.isNotEmpty(txtName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("desc", txtName.getValue(), MatchMode.ANYWHERE));
		}
		return restrictions;
	}
	
	/**
	 * @param users
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<SecUser> users) {
		Container indexedContainer = simpleTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (users != null && !users.isEmpty()) {
			for (SecUser user : users) {
				Item item = indexedContainer.addItem(user.getId());
				CheckBox cbSelect = new CheckBox();
				cbSelect.setData(user);
				item.getItemProperty(SELECT).setValue(cbSelect);
				item.getItemProperty("id").setValue(user.getId());
				item.getItemProperty("code").setValue("");
				item.getItemProperty("name").setValue(user.getDesc());
			}
		}
	}
	
	/**
	 */
	private void assignValues() {
		setIndexedContainer(ENTITY_SRV.list(getRestrictions()));
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(SELECT, I18N.message("check"), CheckBox.class, Align.LEFT, 30));
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("code", I18N.message("code"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("name", I18N.message("name"), String.class, Align.LEFT, 200));
		return columnDefinitions;
	}

	/**
	 * @param pagedTable
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<SecUser> getSelectedIds() {
		List<SecUser> secUsers = new ArrayList<>();
		for (Iterator i = simpleTable.getItemIds().iterator(); i.hasNext();) {
		    Long iid = (Long) i.next();
		    Item item = simpleTable.getItem(iid);
		    CheckBox cbSelect = (CheckBox) item.getItemProperty("select").getValue();
		    if (cbSelect.getValue()) {
		    	secUsers.add((SecUser) cbSelect.getData());
		    }
		}
		return secUsers;
	}
}
