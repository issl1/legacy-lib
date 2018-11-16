package com.nokor.efinance.ra.ui.panel.dealer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.service.DealerRestriction;
import com.nokor.efinance.core.financial.model.CampaignDealer;
import com.nokor.efinance.core.financial.model.InsuranceCampaignDealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;
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
public class DealersSelectPanel extends Window implements DealerEntityField, FinServicesHelper {

	private static final long serialVersionUID = 5489374367808132695L;
	
	private static final String SELECT = "select";

	private TextField txtIntCode;
	private TextField txtNameEn;
	
	protected Button btnSearch;
	protected Button btnReset;
	
	private Button btnSelect;
	
	private SimpleTable<Dealer> simpleTable;
	
	private Listener selectListener = null;
	
	private Long campaignId;
	private Long insuranceCampaignId;
	
	/**
	 * @param campaignId the campaignId to set
	 */
	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	/**
	 * @param insuranceCampaignId the insuranceCampaignId to set
	 */
	public void setInsuranceCampaignId(Long insuranceCampaignId) {
		this.insuranceCampaignId = insuranceCampaignId;
	}

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
		containLayout.addComponent(createSearchForm());
		containLayout.addComponent(buttonSelectLayout);
		containLayout.addComponent(simpleTable);
		setContent(containLayout);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	private void reset() {
		txtIntCode.setValue("");
		txtNameEn.setValue("");
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
		
		txtIntCode = ComponentFactory.getTextField("dealershop.id", false, 60, 150);         
		txtNameEn = ComponentFactory.getTextField("name", false, 60, 150);
		
        final FormLayout formLayout1 = new FormLayout();
        final FormLayout formLayout2 = new FormLayout();
        
        
        formLayout1.addComponent(txtIntCode);
        formLayout2.addComponent(txtNameEn);
        
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
	private BaseRestrictions<Dealer> getRestrictions() {
		String CAM_DEA = "camdea";
		String DOT = ".";
			
		DealerRestriction restrictions = new DealerRestriction();
		restrictions.setInternalCode(txtIntCode.getValue());
		restrictions.setName(txtNameEn.getValue());
		if (campaignId != null) {
			DetachedCriteria camDeaSubCriteria = DetachedCriteria.forClass(CampaignDealer.class, CAM_DEA);
			camDeaSubCriteria.add(Restrictions.eq(CAM_DEA + DOT + CampaignDealer.CAMPAIGN + DOT + ID, campaignId));
			camDeaSubCriteria.setProjection(Projections.projectionList().add(Projections.property(CAM_DEA + DOT + CampaignDealer.DEALER + DOT + ID)));
			restrictions.addCriterion(Property.forName(Dealer.ID).notIn(camDeaSubCriteria));
		} else if (insuranceCampaignId != null) {
			DetachedCriteria insCamDeaSubCriteria = DetachedCriteria.forClass(InsuranceCampaignDealer.class, "inscamdea");
			insCamDeaSubCriteria.add(Restrictions.eq("inscamdea.insuranceCampaign.id", insuranceCampaignId));
			insCamDeaSubCriteria.setProjection(Projections.projectionList().add(Projections.property("inscamdea.dealer.id")));
			restrictions.addCriterion(Property.forName(Dealer.ID).notIn(insCamDeaSubCriteria));
		}
		restrictions.addCriterion(Restrictions.ne(STATUS_RECORD, EStatusRecord.RECYC));
		restrictions.addOrder(Order.asc(NAME_EN));
		
		return restrictions;
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
				item.getItemProperty(SELECT).setValue(new CheckBox());
				item.getItemProperty(ID).setValue(dealer.getId());
				item.getItemProperty(INTERNAL_CODE).setValue(dealer.getCode());
				item.getItemProperty(NAME_EN).setValue(dealer.getNameEn());
				item.getItemProperty(LICENCE_NO).setValue(dealer.getLicenceNo());
				item.getItemProperty(DEALER_TYPE).setValue(dealer.getDealerType() != null ? dealer.getDealerType().getDescEn() : "");
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
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(INTERNAL_CODE, I18N.message("dealershop.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(NAME_EN, I18N.message("name"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(LICENCE_NO, I18N.message("commercial.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DEALER_TYPE, I18N.message("dealer.type"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}

	/**
	 * @param pagedTable
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Long> getSelectedIds() {
		List<Long> ids = new ArrayList<>();
		for (Iterator i = simpleTable.getItemIds().iterator(); i.hasNext();) {
		    Long iid = (Long) i.next();
		    Item item = simpleTable.getItem(iid);
		    CheckBox cbSelect = (CheckBox) item.getItemProperty("select").getValue();
		    if (cbSelect.getValue()) {
		    	ids.add(iid);
		    }
		}
		return ids;
	}
}
