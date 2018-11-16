package com.nokor.efinance.core.applicant.panel.contact.dealer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAddress;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * Contact panel
 * @author ly.youhort
 */
public class ContactDealerPanel extends Panel {
	
	private static final long serialVersionUID = 966783199811064295L;
		
	// Dealer shop layout
	private TextField txtDealerName;
	private TextField txtBranch;
	private TextArea txtDealerAddress;
	private TextField txtDealerPhone;
	private TextField txtManagerName;
	private Button btnFullDetail;
		
	/**
	 */
	public ContactDealerPanel() {
		setSizeFull();
		createForm();
	}
	
	/**
	 * 
	 * @return
	 */
	private TextField getTextField(String caption) {
		TextField txtField = ComponentFactory.getTextField(caption, false, 60, 150);
		txtField.setEnabled(false);
		return txtField;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Button getButton(String caption) {
		Button btnButton = ComponentFactory.getButton(caption);
		btnButton.setStyleName(Reindeer.BUTTON_LINK);
		return btnButton;
	} 
	
	/** */
	private void createForm() {
		txtDealerName = getTextField("dealer.name");
		txtBranch = getTextField("branch");
		txtDealerAddress = ComponentFactory.getTextArea("address", false, 150, 50);
		txtDealerAddress.setEnabled(false);
		txtDealerPhone = getTextField("phone");
		txtManagerName = getTextField("manager.name");
		
		btnFullDetail = getButton("full.detail");
				
		FormLayout formLayout = new FormLayout();
		formLayout.setStyleName("myform-align-left");
		formLayout.addComponent(txtDealerName);
		formLayout.addComponent(txtBranch);
		formLayout.addComponent(txtDealerAddress);
		formLayout.addComponent(txtDealerPhone);
		formLayout.addComponent(txtManagerName);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponent(formLayout);
		layout.addComponent(btnFullDetail);
		layout.setComponentAlignment(btnFullDetail, Alignment.BOTTOM_RIGHT);
		layout.addComponent(ComponentFactory.getSpaceHeight(10, Unit.PIXELS));
		
		setCaption("<h2 style=\"border:1px solid #E3E3E3;padding:9px;border-radius:3px;margin:0;"
				+ "background-color:#F5F5F5;\" align=\"center\" >" + I18N.message("dealer.shop") + "</h2>");
		setCaptionAsHtml(true);
		setContent(layout);
	}
	
	/**
	 * 
	 * @param dealer
	 */
	public void assignValues(Dealer dealer) {
		if (dealer != null) {
			txtDealerName.setValue(dealer.getNameEn());
			txtBranch.setValue("");
			txtDealerPhone.setValue(dealer.getMobile());
			List<DealerAddress> dealerAddress = dealer.getDealerAddresses();
			if (dealerAddress != null && !dealerAddress.isEmpty()) {
				Address address = dealerAddress.get(0).getAddress();
				if (address != null) {
					setAddress(address, txtDealerAddress);
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @param address
	 * @param field
	 */
	private void setAddress(Address address, AbstractTextField field) {
		StringBuffer referenceName = new StringBuffer(); 
		List<String> descriptions = new ArrayList<>();
		descriptions.add(address.getHouseNo());
		descriptions.add(address.getLine1());
		descriptions.add(address.getLine2());
		descriptions.add(address.getStreet());
		descriptions.add(address.getCommune() != null ? address.getCommune().getDescEn() : "");
		descriptions.add(address.getDistrict() != null ? address.getDistrict().getDescEn() : "");
		descriptions.add(address.getProvince() != null ? address.getProvince().getDescEn() : "");
		descriptions.add(address.getPostalCode());
		for (String string : descriptions) {
			referenceName.append(string);
			if (StringUtils.isNotEmpty(string)) {
				referenceName.append(",");
			}
		}
		int lastIndex = referenceName.lastIndexOf(",");
		referenceName.replace(lastIndex, lastIndex + 1, "");
		field.setValue(referenceName.toString());
	}
	
	
	/**
	 * Reset panel
	 */
	public void reset() {
		txtDealerName.setValue("");
		txtBranch.setValue("");
		txtDealerAddress.setValue("");
		txtDealerPhone.setValue("");
		txtManagerName.setValue("");
	}

	/**
	 * @return the btnFullDetail
	 */
	public Button getBtnFullDetail() {
		return btnFullDetail;
	}

	/**
	 * @param btnFullDetail the btnFullDetail to set
	 */
	public void setBtnFullDetail(Button btnFullDetail) {
		this.btnFullDetail = btnFullDetail;
	}	
}
