package com.nokor.efinance.gui.ui.panel.dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.stock.model.EStockReason;
import com.nokor.efinance.core.stock.model.ProductStock;
import com.nokor.efinance.core.stock.model.ProductStockInventory;
import com.nokor.efinance.core.stock.service.StockService;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ButtonFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;
/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(StockPosFormPanel.NAME)
public class StockPosFormPanel extends AbstractTabPanel implements View, ValueChangeListener, FrmkServicesHelper {
	
	private static final long serialVersionUID = 6227740006388204118L;

	public static final String NAME = "stock.pos";
	
	@Autowired
	private StockService stockService;
	
	private Button btnSave;
	private ValueChangeListener valueChangeListener;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private NumberField txtLeafletDelivery;
	private NumberField txtLeafletDistribution;
	private NumberField txtLeafletDistInsideDealer;
	private NumberField txtLeafletDistInTheStreet;
	private NumberField txtLeafletDistInFieldCheck;
	private NumberField txtLeafletDistInSpecialEvent;
	private NumberField txtLeafletStock;
	
	private NumberField txtTShirtDelivery;
	private NumberField txtTShirtDistribution;
	private NumberField txtTShirtStock;
	
	private int initialLeafletStock;
	private int initialTShirtStock;
	
	private Label lblLastUpdateValue;
	private Label lblAccountName;
	
	/**
	 * @param secUserDetail
	 */
	@PostConstruct
	public void PostConstruct() {
		initialLeafletStock = 0;
		initialTShirtStock = 0;
		
		if (cbxDealer.getSelectedEntity() != null) {
			assignValues(cbxDealer.getSelectedEntity());
		}
	}
	
	/*
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		
		TabSheet tabSheet = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		
		cbxDealer = new DealerComboBox(DataReference.getInstance().getDealers());
		cbxDealer.setWidth("220px");
		cbxDealer.setImmediate(true);
		cbxDealer.setSelectedEntity(null);
		
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -6124925564786267649L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		};
		cbxDealerType.addValueChangeListener(valueChangeListener);
		
		SecUserDetail secUserDetail = getSecUserDetail(); 
		if (ProfileUtil.isPOS() && secUserDetail != null && secUserDetail.getDealer() != null) {
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());
			cbxDealerType.removeValueChangeListener(valueChangeListener);
			cbxDealerType.setSelectedEntity(secUserDetail.getDealer().getDealerType() != null ? 
					secUserDetail.getDealer().getDealerType() : null);
			cbxDealerType.addValueChangeListener(valueChangeListener);
			cbxDealer.setEnabled(false);
			cbxDealerType.setEnabled(false);
		} else {
			cbxDealer.setEnabled(true);
			cbxDealerType.setEnabled(true);
		}
		if(ProfileUtil.isCreditOfficerMovable()){
			cbxDealer.setEnabled(true);
			cbxDealerType.setEnabled(true);
		}
		
		txtLeafletDelivery = ComponentFactory.getNumberField();
		txtLeafletDelivery.setImmediate(true);
		txtLeafletDelivery.addValueChangeListener(this);
		
		txtLeafletDistribution = ComponentFactory.getNumberField();
		txtLeafletDistribution.setEnabled(false);
		
		txtLeafletDistInsideDealer = ComponentFactory.getNumberField();
		txtLeafletDistInsideDealer.setImmediate(true);
		txtLeafletDistInsideDealer.addValueChangeListener(this);
	
		txtLeafletDistInTheStreet = ComponentFactory.getNumberField();
		txtLeafletDistInTheStreet.setImmediate(true);
		txtLeafletDistInTheStreet.addValueChangeListener(this);
		
		txtLeafletDistInFieldCheck = ComponentFactory.getNumberField();
		txtLeafletDistInFieldCheck.setImmediate(true);
		txtLeafletDistInFieldCheck.addValueChangeListener(this);
		
		txtLeafletDistInSpecialEvent = ComponentFactory.getNumberField();
		txtLeafletDistInSpecialEvent.setImmediate(true);
		txtLeafletDistInSpecialEvent.addValueChangeListener(this);
		
		txtLeafletStock = ComponentFactory.getNumberField();
		txtLeafletStock.setEnabled(false);
		
		txtTShirtDelivery = ComponentFactory.getNumberField();
		txtTShirtDelivery.setImmediate(true);
		txtTShirtDelivery.addValueChangeListener(this);
		
		txtTShirtDistribution = ComponentFactory.getNumberField();
		txtTShirtDistribution.setImmediate(true);
		txtTShirtDistribution.addValueChangeListener(this);
		
		txtTShirtStock = ComponentFactory.getNumberField();
		txtTShirtStock.setEnabled(false);
		
		lblLastUpdateValue = new Label();
		lblAccountName = new Label();
		
		btnSave = ButtonFactory.getSaveButton();
		
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/stock.html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template stock", e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		customLayout.addComponent(new Label(I18N.message("dealer.type")), "lblDealerType");
		customLayout.addComponent(cbxDealerType, "cbxDealerType");
		customLayout.addComponent(new Label(I18N.message("dealer")), "lblDealer");
		customLayout.addComponent(cbxDealer, "cbxDealer");
		customLayout.addComponent(new Label(I18N.message("last.update") + " : "), "lblLastUpdate");
		customLayout.addComponent(lblLastUpdateValue, "lblLastUpdateValue");
		customLayout.addComponent(new Label(I18N.message("account") + " : "), "lblAccount");
		customLayout.addComponent(lblAccountName, "lblAccountName");
		
		customLayout.addComponent(new Label(I18N.message("leaflet")), "lblLeaflet");
		customLayout.addComponent(new Label(I18N.message("delivery")), "lblLeafletDelivery");
		customLayout.addComponent(txtLeafletDelivery, "txtLeafletDelivery");
		customLayout.addComponent(new Label(I18N.message("distribution")), "lblLeafletDistibution");
		customLayout.addComponent(txtLeafletDistribution, "txtLeafletDistribution");
		customLayout.addComponent(new Label(I18N.message("inside.dealer")), "lblLeafletDistInsideDealer");
		customLayout.addComponent(txtLeafletDistInsideDealer, "txtLeafletDistInsideDealer");
		customLayout.addComponent(new Label(I18N.message("in.the.street")), "lblLeafletDistInTheStreet");
		customLayout.addComponent(txtLeafletDistInTheStreet, "txtLeafletDistInTheStreet");
		customLayout.addComponent(new Label(I18N.message("in.field.check")), "lblLeafletDistInFieldCheck");
		customLayout.addComponent(txtLeafletDistInFieldCheck, "txtLeafletDistInFieldCheck");
		customLayout.addComponent(new Label(I18N.message("in.special.event")), "lblLeafletDistInSpecialEvent");
		customLayout.addComponent(txtLeafletDistInSpecialEvent, "txtLeafletDistInSpecialEvent");
		customLayout.addComponent(new Label(I18N.message("stock")), "lblLeafletStock");
		customLayout.addComponent(txtLeafletStock, "txtLeafletStock");
		
		customLayout.addComponent(new Label(I18N.message("tshirt")), "lblTShirt");
		customLayout.addComponent(new Label(I18N.message("delivery")), "lblTShirtDelivery");
		customLayout.addComponent(txtTShirtDelivery, "txtTShirtDelivery");
		customLayout.addComponent(new Label(I18N.message("distribution")), "lblTShirtDistibution");
		customLayout.addComponent(txtTShirtDistribution, "txtTShirtDistribution");
		customLayout.addComponent(new Label(I18N.message("stock")), "lblTShirtStock");
		customLayout.addComponent(txtTShirtStock, "txtTShirtStock");
		
		contentLayout.addComponent(customLayout);
		
		ToolbarButtonsPanel tblButtonsPanel = new ToolbarButtonsPanel();
		tblButtonsPanel.addButton(btnSave);
		
		btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1717161084451001316L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (isValid()) {
					saveStock(cbxDealer.getSelectedEntity());
				}
			}
		});
		
		contentLayout.addComponent(tblButtonsPanel, 0);
		tabSheet.addTab(contentLayout, I18N.message("stock"));
		
		return tabSheet;
	}
	
	/**
	 * 
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ENTITY_SRV.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}
	
	/**
	 * @return
	 */
	private boolean isValid() {
		checkMandatorySelectField(cbxDealer, "dealer");
		if (!errors.isEmpty()) {
			displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * @param dealer
	 */
	private void saveStock(Dealer dealer) {
		stockService.saveStock(dealer, "LEAFL", getInteger(txtLeafletDelivery), EStockReason.DELIVERY);
		stockService.saveStock(dealer, "LEAFL", getInteger(txtLeafletDistInsideDealer), EStockReason.INSIDE_DEALER);
		stockService.saveStock(dealer, "LEAFL", getInteger(txtLeafletDistInTheStreet), EStockReason.IN_THE_STREET);
		stockService.saveStock(dealer, "LEAFL", getInteger(txtLeafletDistInFieldCheck), EStockReason.IN_FIELD_CHECK);
		stockService.saveStock(dealer, "LEAFL", getInteger(txtLeafletDistInSpecialEvent), EStockReason.IN_SPECIAL_EVENT);
		
		stockService.saveStock(dealer, "TSHIRT", getInteger(txtTShirtDelivery), EStockReason.DELIVERY);
		stockService.saveStock(dealer, "TSHIRT", getInteger(txtTShirtDistribution), EStockReason.DISTRIBUTION);
		
		txtLeafletDelivery.setValue("");
		txtLeafletDistribution.setValue("");
		txtLeafletDistInsideDealer.setValue("");
		txtLeafletDistInTheStreet.setValue("");
		txtLeafletDistInFieldCheck.setValue("");
		txtLeafletDistInSpecialEvent.setValue("");
		
		txtTShirtDelivery.setValue("");;
		txtTShirtDistribution.setValue("");;
				
		displaySuccess();
		
		assignValues(dealer);
	}
	
	/**
	 * @param dealer
	 */
	private void assignValues(Dealer dealer) {
		ProductStock productStockLeaflet = stockService.getProductStock(dealer, "LEAFL");
		if (productStockLeaflet != null) {
			initialLeafletStock = productStockLeaflet.getQty();
			txtLeafletStock.setValue(String.valueOf(initialLeafletStock));
		}		
		ProductStock productStockTShirt = stockService.getProductStock(dealer, "TSHIRT");
		if (productStockTShirt != null) {
			initialTShirtStock = productStockTShirt.getQty();
			txtTShirtStock.setValue(String.valueOf(initialTShirtStock));
		}
		//add the date of last update and the account which made the update
		ProductStockInventory productStockInventory = getLastUpdateForDealder(dealer);
		if (productStockInventory != null) {
			lblLastUpdateValue.setValue(DateUtils.date2String(productStockInventory.getUpdateDate(), "yyyy/MM/dd HH:mm:ss"));
			lblAccountName.setValue(productStockInventory.getUpdateUser());
		}
	}
	
	/**
	 * get last update for dealer
	 * @param dealer
	 * @return 
	 */
	private ProductStockInventory getLastUpdateForDealder(Dealer dealer) {
		BaseRestrictions<ProductStockInventory> restrictions = new BaseRestrictions<>(ProductStockInventory.class);
		restrictions.addCriterion(Restrictions.eq("dealer" + "." + "id", cbxDealer.getSelectedEntity().getId()));
		restrictions.addOrder(Order.desc("date"));
		List<ProductStockInventory> list = ENTITY_SRV.list(restrictions);
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}
	
	/*
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {		
	}

	/*
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		txtLeafletDistribution.setValue(String.valueOf(
				MyNumberUtils.getInteger(getInteger(txtLeafletDistInsideDealer)) + MyNumberUtils.getInteger(getInteger(txtLeafletDistInTheStreet)) 
				+ MyNumberUtils.getInteger(getInteger(txtLeafletDistInFieldCheck)) + MyNumberUtils.getInteger(getInteger(txtLeafletDistInSpecialEvent))
		));
		txtLeafletStock.setValue(String.valueOf(
				initialLeafletStock + MyNumberUtils.getInteger(getInteger(txtLeafletDelivery)) - MyNumberUtils.getInteger(getInteger(txtLeafletDistribution)) 
		));
		
		txtTShirtStock.setValue(String.valueOf(
				initialTShirtStock + MyNumberUtils.getInteger(getInteger(txtTShirtDelivery)) - MyNumberUtils.getInteger(getInteger(txtTShirtDistribution)) 
		));
	}
}
