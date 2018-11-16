package com.nokor.efinance.gui.ui.panel.payment.blocked.identify.dealership;

import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class IdentifyPaymentDealerShipPanel extends VerticalLayout {

	/** */
	private static final long serialVersionUID = -4861611069484817139L;

	private TextField txtDealerShipId;
	private EntityComboBox<Dealer> cbxDealerShipName;
	private ComboBox cbxDealerShipPayment;
	
	/**
	 * 
	 */
	public IdentifyPaymentDealerShipPanel() {
		txtDealerShipId = ComponentFactory.getTextField(50, 120);
	
		cbxDealerShipName = new EntityComboBox<>(Dealer.class, Dealer.NAMELOCALE);
		cbxDealerShipName.renderer(new BaseRestrictions<Dealer>(Dealer.class));
		cbxDealerShipName.setWidth(170, Unit.PIXELS);
		
		cbxDealerShipPayment = ComponentFactory.getComboBox();
		cbxDealerShipPayment.setWidth(170, Unit.PIXELS);
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.addComponent(ComponentLayoutFactory.getLabelCaption("dealer.ship.id"));
		horLayout.addComponent(txtDealerShipId);
		horLayout.addComponent(ComponentLayoutFactory.getLabelCaption("dealer.ship.name"));
		horLayout.addComponent(cbxDealerShipName);
		horLayout.addComponent(ComponentLayoutFactory.getLabelCaption("dealer.ship.payment"));
		horLayout.addComponent(cbxDealerShipPayment);
		
		setMargin(true);
		addComponent(horLayout);
	}
	
}
