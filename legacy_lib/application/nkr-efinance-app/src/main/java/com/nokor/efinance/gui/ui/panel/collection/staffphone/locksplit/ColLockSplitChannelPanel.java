package com.nokor.efinance.gui.ui.panel.collection.staffphone.locksplit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class ColLockSplitChannelPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = -7080983655082611779L;
	
	private ERefDataComboBox<EPaymentChannel> cbxChannel;
	private EntityComboBox<Dealer> cbxDealerShipName;
	private TextField txtDealerShipID;
	private TextArea txtDealerShipAddress;
	private LockSplit lockSplit;
	
	/**
	 * 
	 */
	public ColLockSplitChannelPanel() {
		cbxChannel = new ERefDataComboBox<EPaymentChannel>(EPaymentChannel.values());
		cbxChannel.setWidth(150, Unit.PIXELS);
		cbxDealerShipName = new EntityComboBox<Dealer>(Dealer.class, Dealer.NAMELOCALE);
		cbxDealerShipName.setWidth(180, Unit.PIXELS);
		cbxDealerShipName.renderer();
		txtDealerShipID = ComponentFactory.getTextField(30, 180);
		txtDealerShipAddress = ComponentFactory.getTextArea(false, 300, 50);
		cbxDealerShipName.setEnabled(false);
		txtDealerShipID.setEnabled(false);
		txtDealerShipAddress.setEnabled(false);
		
		cbxChannel.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 4353319200795790557L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxChannel.getSelectedEntity() != null) {
					if (EPaymentChannel.DEALERCOUNTER.equals(cbxChannel.getSelectedEntity())) {
						showDealerShipData(false);
					} else {
						showDealerShipData(true);
					}
				} else {
					resetChannelDetail(false);
				}
			}
		});
		addComponent(getChannelLayoutPanel());
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getChannelLayoutPanel() {
		Label lblChannel = ComponentLayoutFactory.getLabelCaption("channel");
		Label lblDealerShipID = ComponentLayoutFactory.getLabelCaption("dealer.ship.id");
		Label lblDealerShipName = ComponentLayoutFactory.getLabelCaption("dealer.ship.name");
		Label lblDealerShipAddress = ComponentLayoutFactory.getLabelCaption("dealer.ship.address");
		
		GridLayout gridLayout = new GridLayout(15, 5);
		gridLayout.setSpacing(true);
		int iCol = 0;
		gridLayout.addComponent(lblChannel, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxChannel, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblDealerShipName, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxDealerShipName, iCol++, 0);
		iCol = 4;
		gridLayout.addComponent(lblDealerShipID, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(txtDealerShipID, iCol++, 1);
		iCol = 4;
		gridLayout.addComponent(lblDealerShipAddress, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(txtDealerShipAddress, iCol++, 2);
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(new MarginInfo(true, true, false, true), false);
		horLayout.addComponent(gridLayout);
		Panel mainPanel = new Panel(new VerticalLayout(horLayout));
		return mainPanel;
	}
	
	/**
	 * 
	 * @param isEnabled
	 */
	private void showDealerShipData(boolean isEnabled) {
		resetChannelDetail(isEnabled);
		if (this.lockSplit != null) {
			Contract contract = this.lockSplit.getContract();
			if (contract != null) {
				Dealer dealer = null;
				if (EPaymentChannel.DEALERCOUNTER.equals(cbxChannel.getSelectedEntity())) {
					dealer = contract.getDealer();
				} else {
					dealer = this.lockSplit.getDealer();
				}
				cbxDealerShipName.setSelectedEntity(dealer);
				setValuesToDealerControls();
				cbxDealerShipName.addValueChangeListener(new ValueChangeListener() {
					
					/** */
					private static final long serialVersionUID = 7246559111919941863L;

					/**
					 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
					 */
					@Override
					public void valueChange(ValueChangeEvent event) {
						if (cbxDealerShipName.getSelectedEntity() != null) {
							setValuesToDealerControls();
						}
					}
				});
			}
		}
	}
	
	/**
	 * 
	 * @param isEnabled
	 */
	private void resetChannelDetail(boolean isEnabled) {
		cbxDealerShipName.setEnabled(isEnabled);
		cbxDealerShipName.setSelectedEntity(null);
		txtDealerShipID.setValue("");
		txtDealerShipAddress.setValue("");
	}
	
	/**
	 * 
	 * @param dealer
	 */
	private void setValuesToDealerControls() {
		Dealer dealer = cbxDealerShipName.getSelectedEntity();
		if (dealer != null) {
			txtDealerShipID.setValue(dealer.getCode());
			Address dealerAddress = dealer.getMainAddress();
			if (dealerAddress != null) {
				txtDealerShipAddress.setValue(getAddress(dealerAddress));
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public LockSplit getLockSplit() {
		this.lockSplit.setPaymentChannel(cbxChannel.getSelectedEntity());
		this.lockSplit.setDealer(cbxDealerShipName.getSelectedEntity());
		return this.lockSplit;
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	public void assignValues(LockSplit lockSplit) {
		reset();
		this.lockSplit = lockSplit;
		cbxChannel.setSelectedEntity(lockSplit.getPaymentChannel());
	}
	
	/**
	 * 
	 */
	protected void reset() {
		cbxChannel.setSelectedEntity(null);
		cbxDealerShipName.setSelectedEntity(null);
		txtDealerShipID.setValue(StringUtils.EMPTY);
		txtDealerShipAddress.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	private String getAddress(Address address) {
		StringBuffer stringBuffer = new StringBuffer(); 
		List<String> descriptions = new ArrayList<>();
		descriptions.add(getDefaultString(address.getHouseNo()));
		descriptions.add(getDefaultString(address.getLine1()));
		descriptions.add(getDefaultString(address.getLine2()));
		descriptions.add(getDefaultString(address.getStreet()));
		descriptions.add(address.getCommune() != null ? address.getCommune().getDescLocale() : "");
		descriptions.add(address.getDistrict() != null ? address.getDistrict().getDescLocale() : "");
		descriptions.add(address.getProvince() != null ? address.getProvince().getDescLocale() : "");
		descriptions.add(getDefaultString(address.getPostalCode()));
		if (!descriptions.isEmpty()) {
			for (String string : descriptions) {
				stringBuffer.append(string);
				if (StringUtils.isNotEmpty(string)) {
					stringBuffer.append(",");
				}
			}
			int lastIndex = stringBuffer.lastIndexOf(",");
			stringBuffer.replace(lastIndex, lastIndex + 1, "");
		}
		return stringBuffer.toString();
	}
	
}
