package com.nokor.efinance.ra.ui.panel.insurance.company.detail.price;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.dao.DataIntegrityViolationException;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.financial.model.InsuranceFinService;
import com.nokor.efinance.core.financial.model.MInsuranceFinService;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.ra.ui.panel.insurance.company.detail.price.InsuracePriceLOSPopupSelectPanel.SelectListener;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Insurance Price Detail Form Panel
 * @author bunlong.taing
 */
public class InsurancePriceDetailFormPanel extends AbstractControlPanel implements AddClickListener, SearchClickListener, DeleteClickListener, SelectListener {
	/** */
	private static final long serialVersionUID = -3164254880272183630L;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimpleTable<Entity> simpleTable;
	private Long selectedItemId = null;
	private Organization insurance;
	private InsuracePriceLOSPopupSelectPanel window;
	
	private EServiceType serviceType;
	private Long insuranceCompanyId;
	
	public InsurancePriceDetailFormPanel(EServiceType serviceType) {
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		addComponent(navigationPanel, 0);
		this.serviceType = serviceType;
		addComponent(createForm());
	}

	/**
	 * Create Form
	 * @return
	 */
	protected Component createForm() {
		window = new InsuracePriceLOSPopupSelectPanel();
		window.setSelectListener(this);
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setStyleName("has-no-padding");
		contentLayout.setSpacing(true);
		
		this.columnDefinitions = createColumnDefinitions();
		simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
		simpleTable.addItemClickListener(new ItemClickListener() {
			/** */
			private static final long serialVersionUID = -281273808131421098L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItemId = (Long) event.getItemId();
			}
		});
		
		contentLayout.addComponent(simpleTable);
		
		return contentLayout;
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(FMEntityField.ASSET_MAKE, I18N.message("asset.make"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(FMEntityField.ASSET_RANGE, I18N.message("asset.range"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(FMEntityField.ASSET_MODEL, I18N.message("asset.model"), String.class, Align.LEFT, 120));
		
		columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.PREMIUM_FIRST_YEAR, I18N.message("premium.1y"), Double.class, Align.RIGHT, 80));
		if (EServiceType.INSLOS.equals(serviceType)) {
			columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.CLAIM_AMOUNT_FIRST_YEAR, I18N.message("sum.insured.1y"), Double.class, Align.RIGHT, 90));
			columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.PREMIUM_SECOND_YEAR, I18N.message("premium.2y"), Double.class, Align.RIGHT, 80));
			columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.CLAIM_AMOUNT_2Y_FIRST_YEAR, I18N.message("sum.insured.2y.first.year"), Double.class, Align.RIGHT, 150));
			columnDefinitions.add(new ColumnDefinition(MInsuranceFinService.CLAIM_AMOUNT_2Y_SECOND_YEAR, I18N.message("sum.insured.2y.second.year"), Double.class, Align.RIGHT, 150));
		}
		return columnDefinitions;
	}
	
	/**
	 * Assign Values
	 * @param insuranceCompanyId
	 */
	public void assignValues(Long insuranceCompanyId) {
		this.insuranceCompanyId = insuranceCompanyId;
		reset();
		if (insuranceCompanyId != null) {
			insurance = ENTITY_SRV.getById(Organization.class, insuranceCompanyId);
			simpleTable.setContainerDataSource(getIndexedContainer(insuranceCompanyId));
		} else {
			simpleTable.removeAllItems();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		selectedItemId = null;
		insurance = null;
	}
	
	/**
	 * Get Indexed Container
	 * @param insuranceCompanyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(Long insuranceCompanyId) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		List<InsuranceFinService> insuranceFinServices = getInsuranceFinServices(insuranceCompanyId);
		if (insuranceFinServices != null) {
			for (InsuranceFinService insuranceFinService : insuranceFinServices) {
				AssetModel assetModel = insuranceFinService.getAssetModel();
				AssetRange assetRange = assetModel != null ? assetModel.getAssetRange() : null;
				AssetMake assetMake = assetRange != null ? assetRange.getAssetMake() : null;
				
				Item item = indexedContainer.addItem(insuranceFinService.getId());
				item.getItemProperty(MInsuranceFinService.ID).setValue(insuranceFinService.getId());
				item.getItemProperty(FMEntityField.ASSET_MAKE).setValue(assetMake != null ? assetMake.getDescEn() : "");
				item.getItemProperty(FMEntityField.ASSET_RANGE).setValue(assetRange != null ? assetRange.getDescEn() : "");
				item.getItemProperty(FMEntityField.ASSET_MODEL).setValue(assetModel != null ? assetModel.getSerie() : "");
				item.getItemProperty(MInsuranceFinService.PREMIUM_FIRST_YEAR).setValue(insuranceFinService.getPremium1Y());
				if (EServiceType.INSLOS.equals(serviceType)) {
					item.getItemProperty(MInsuranceFinService.CLAIM_AMOUNT_FIRST_YEAR).setValue(insuranceFinService.getClaimAmount1Y());
					item.getItemProperty(MInsuranceFinService.PREMIUM_SECOND_YEAR).setValue(insuranceFinService.getPremium2Y());
					item.getItemProperty(MInsuranceFinService.CLAIM_AMOUNT_2Y_FIRST_YEAR).setValue(insuranceFinService.getClaimAmount2YFirstYear());
					item.getItemProperty(MInsuranceFinService.CLAIM_AMOUNT_2Y_SECOND_YEAR).setValue(insuranceFinService.getClaimAmount2YSecondYear());
				}
			}
		}
		return indexedContainer;
	}
	
	/**
	 * Get InsuranceFinServices
	 * @param id
	 * @return
	 */
	private List<InsuranceFinService> getInsuranceFinServices(Long id) {
		BaseRestrictions<InsuranceFinService> restrictions = new BaseRestrictions<InsuranceFinService>(InsuranceFinService.class);
		restrictions.addCriterion(Restrictions.eq(MInsuranceFinService.INSURANCE + "." + MInsuranceFinService.ID, id));
		restrictions.addAssociation(MInsuranceFinService.SERVICE, "ins", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("ins." + MInsuranceFinService.SERVICE_TYPE, serviceType));
		restrictions.addOrder(Order.asc(MInsuranceFinService.ID));
		
		return ENTITY_SRV.list(restrictions);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener#deleteButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void deleteButtonClick(ClickEvent event) {
		if (selectedItemId == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete", String.valueOf(selectedItemId)),
		        new ConfirmDialog.Listener() {
					/** */
					private static final long serialVersionUID = -2879516847956920290L;
					@Override
					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	try {
		                		ENTITY_SRV.delete(InsuranceFinService.class, selectedItemId);
		                		searchButtonClick(null);
		                	} catch (DataIntegrityViolationException e) {
								MessageBox mb = new MessageBox(
										UI.getCurrent(),
										"400px",
										"160px",
										I18N.message("information"),
										MessageBox.Icon.ERROR,
										I18N.message("msg.warning.delete.selected.item.is.used"),
										Alignment.MIDDLE_RIGHT,
										new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
								mb.show();
		                	}
		                }
		            }
		        });
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener#searchButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void searchButtonClick(ClickEvent event) {
		assignValues(insurance.getId());
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		window.reset();
		//window.setInsurance(insurance);
		UI.getCurrent().addWindow(window);
	}

	/**
	 * @return the serviceType
	 */
	public EServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(EServiceType serviceType) {
		this.serviceType = serviceType;
	}
	
	/**
	 * 
	 * @param serviceType
	 * @return
	 */
	private FinService getFinServiceByType(EServiceType serviceType) {
		BaseRestrictions<FinService> restrictions = new BaseRestrictions<>(FinService.class);
		restrictions.addCriterion(Restrictions.eq("serviceType", serviceType));
		restrictions.addOrder(Order.desc(FinService.ID));
		List<FinService> finServices = ENTITY_SRV.list(restrictions);
		if (!finServices.isEmpty()) {
			return finServices.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public void onSelected(Map<Long, Double[]> selectedIds) {
		if (selectedIds != null && !selectedIds.isEmpty()) {
			for (Iterator<Long> iter = selectedIds.keySet().iterator(); iter.hasNext(); ) {
				long assetModelId = iter.next();
				Double[] amounts = selectedIds.get(assetModelId);
				AssetModel assetModel = ENTITY_SRV.getById(AssetModel.class, assetModelId);
				FinService service = getFinServiceByType(serviceType);
				try {
					InsuranceFinService insuranceFinService = new InsuranceFinService();
					insuranceFinService.setInsurance(insurance);
					insuranceFinService.setService(service);
					insuranceFinService.setAssetModel(assetModel);
					insuranceFinService.setPremium1Y(amounts[0]);
					insuranceFinService.setClaimAmount1Y(amounts[1]);
					insuranceFinService.setPremium2Y(amounts[2]);
					insuranceFinService.setClaimAmount2YFirstYear(amounts[3]);
					insuranceFinService.setClaimAmount2YSecondYear(amounts[4]);
					
					ENTITY_SRV.saveOrUpdate(insuranceFinService);
				} catch (Exception e) {
					e.printStackTrace();
					ComponentLayoutFactory.displayErrorMsg(e.getMessage());
				}
			}
			assignValues(insuranceCompanyId);
			window.close();
			ComponentLayoutFactory.displaySuccessfullyMsg();
		}
	}

}
