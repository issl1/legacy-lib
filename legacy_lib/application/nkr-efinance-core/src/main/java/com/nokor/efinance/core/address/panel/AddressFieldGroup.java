package com.nokor.efinance.core.address.panel;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.vaadin.ui.panel.AbstractFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Address form.
 * @author ly.youhort
 */
public class AddressFieldGroup extends AbstractFieldGroup<FormLayout> {
    
    //private UserAccountParameters userAccountParameters = new UserAccountFieldGroupObject();

	private static final long serialVersionUID = -1996129766207243231L;

	private Address address = null;
	
	public AddressFieldGroup() {
    	this.address = new Address();
    }
    
    /**
     * {@inheritDoc}
     */
    public void init() {
        unbindAll();
        
        BeanItem<Address> addressItem = new BeanItem<Address>(address);
        setItemDataSource(addressItem);

        final FormLayout layout = new FormLayout();

        final TextField txtHouseNo = buildAndBind(I18N.message("house.no"), "houseNo", TextField.class);
        txtHouseNo.setRequired(true);
        txtHouseNo.setMaxLength(10);
        txtHouseNo.setWidth(200, Unit.PIXELS);

        layout.addComponent(txtHouseNo);
        
        setContent(layout);
    }

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
}
