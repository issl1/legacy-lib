package com.nokor.efinance.gui.ui.panel.contract.filters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.service.DealerRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class DealersSelectPanel extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -7599427018749080392L;

	private static final String SELECT = "select";

	private TextField txtCode;
	private TextField txtName;
	
	private Button btnSearch;
	private Button btnReset;
	
	private Button btnSelect;	
	private SimpleTable<Dealer> simpleTable;	
	private Listener selectListener = null;
	
	private boolean selectAll = false;
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	public interface Listener extends Serializable {
        void onClose(DealersSelectPanel dialog);
    }

    /**
     * Show a modal ConfirmDialog in a window.
     * @param selectListener
     */
    public void show(final Listener selectListener) {
    	this.selectListener = selectListener;
    	UI.getCurrent().addWindow(this);
    }
    
	/**
	 * 
	 */
	public DealersSelectPanel() {
		super.center();
		setModal(true);
		setCaption(I18N.message("dealers"));
		
		VerticalLayout containLayout = new VerticalLayout();
		containLayout.setMargin(true);
		containLayout.setSpacing(true);
		
		btnSelect = new Button(I18N.message("select"));
		btnSelect.setIcon(FontAwesome.CHECK_SQUARE_O);
		
		// Create a listener for buttons
        Button.ClickListener cb = new Button.ClickListener() {
            
        	/** */
			private static final long serialVersionUID = -1008689704689014164L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
                if (selectListener != null) {
                    selectListener.onClose(DealersSelectPanel.this);
                }
                UI.getCurrent().removeWindow(DealersSelectPanel.this);
            }
        };
		
        btnSelect.addClickListener(cb);
        
		VerticalLayout buttonSelectLayout = new VerticalLayout();
		buttonSelectLayout.addComponent(btnSelect);
		
		simpleTable = new SimpleTable<>(createColumnDefinitions());
		simpleTable.setCaption(null);		
		
		simpleTable.setColumnIcon(SELECT, FontAwesome.CHECK);
		simpleTable.addHeaderClickListener(new HeaderClickListener() {
		
			/** */
			private static final long serialVersionUID = 387310399477934562L;

			/**
			 * @see com.vaadin.ui.Table.HeaderClickListener#headerClick(com.vaadin.ui.Table.HeaderClickEvent)
			 */
			@Override
			public void headerClick(HeaderClickEvent event) {
				if (event.getPropertyId() == SELECT) {
					selectAll = !selectAll;
					@SuppressWarnings("unchecked")
					java.util.Collection<Long> ids = (java.util.Collection<Long>) simpleTable.getItemIds();
					for (Long id : ids) {
						Item item = simpleTable.getItem(id);
						CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
						cbSelect.setImmediate(true);
						cbSelect.setValue(selectAll);
					}
				}
			}
		});
		
		containLayout.addComponent(createSearchForm());
		containLayout.addComponent(buttonSelectLayout);
		containLayout.addComponent(simpleTable);
		setContent(containLayout);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	private void reset() {
		txtCode.setValue("");
		txtName.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	
	private Component createSearchForm() {
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnSearch.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 387310399477934562L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				assignValues();
			}
		});
				
		btnReset = ComponentLayoutFactory.getButtonReset();
		btnReset.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -9223228663102141768L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		
		txtCode = ComponentFactory.getTextField(100, 130);         
		txtName = ComponentFactory.getTextField(130, 130);
		
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
        filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption("dealershop.id"));
        filterLayout.addComponent(txtCode);
        filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption("name"));
        filterLayout.addComponent(txtName);
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		Panel searchPanel = new Panel(I18N.message("filters"));
		
		VerticalLayout containLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		containLayout.addComponent(filterLayout);
		containLayout.addComponent(buttonsLayout);
		containLayout.setComponentAlignment(buttonsLayout, Alignment.TOP_CENTER);
        
		searchPanel.setContent(containLayout);
		
		return searchPanel;
	}
	
	/**
	 * 
	 * @param entityId
	 * @return
	 */
	private BaseRestrictions<Dealer> getRestrictions() {		
		DealerRestriction dealerRestrictions = new DealerRestriction();
		dealerRestrictions.setInternalCode(txtCode.getValue());
		dealerRestrictions.setName(txtName.getValue());
		return dealerRestrictions;
	}
	
	/**
	 * @param dealers
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Dealer> dealers) {
		Container indexedContainer = simpleTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (dealers != null && !dealers.isEmpty()) {
			for (Dealer dealer : dealers) {
				Item item = indexedContainer.addItem(dealer.getId());
				CheckBox cbSelect = new CheckBox();
				cbSelect.setData(dealer);
				item.getItemProperty(SELECT).setValue(cbSelect);
				item.getItemProperty(Dealer.ID).setValue(dealer.getId());
				item.getItemProperty(Dealer.CODE).setValue(dealer.getCode());
				item.getItemProperty(Dealer.NAMELOCALE).setValue(dealer.getNameLocale());
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
		columnDefinitions.add(new ColumnDefinition(SELECT, StringUtils.EMPTY, CheckBox.class, Align.CENTER, 30));
		columnDefinitions.add(new ColumnDefinition(Dealer.ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(Dealer.CODE, I18N.message("dealershop.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(Dealer.NAMELOCALE, I18N.message("name"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}

	/**
	 * @param pagedTable
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Long> getSelectedIds() {
		List<Long> dealerIds = new ArrayList<>();
		for (Iterator i = simpleTable.getItemIds().iterator(); i.hasNext();) {
		    Long iid = (Long) i.next();
		    Item item = simpleTable.getItem(iid);
		    CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
		    if (cbSelect.getValue()) {
		    	dealerIds.add(iid);
		    }
		}
		return dealerIds;
	}
}
