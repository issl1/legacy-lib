package com.nokor.efinance.gui.ui.panel.collection;

import java.io.IOException;
import java.io.InputStream;

import org.seuksa.frmk.i18n.I18N;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * @author buntha.chea
 */
public class CollectionInboxSummaryPanel extends VerticalLayout {

	private static final long serialVersionUID = 8341162309347917428L;
	
	public CollectionInboxSummaryPanel() {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		setHeight("100%");
		addComponent(createSummaryPanel());
	}
	
	public Panel createSummaryPanel(){
		String template = "collectionInboxSummaryTable";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout inputFieldLayout = null;
		try {
			inputFieldLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template,
					e.getMessage(), Type.ERROR_MESSAGE);
		}
		//Row 1
		inputFieldLayout.addComponent(new Label(I18N.message("staff.code")),"lblStaffCode");
		inputFieldLayout.addComponent(new Label(I18N.message("number.of.contracts")), "lblNumberOfContracts");
		inputFieldLayout.addComponent(new Label(I18N.message("called")),"lblCalled");
		inputFieldLayout.addComponent(new Label(I18N.message("called.percentag")), "lblCalledPercentag");
		inputFieldLayout.addComponent(new Label(I18N.message("returned.motorcycle")),"lblReturnedMotorcycle");
		inputFieldLayout.addComponent(new Label(I18N.message("number.of.1st.promise")),"lblNumberOf1stPromise");
		inputFieldLayout.addComponent(new Label(I18N.message("number.of.2nd.promise")),"lblNumberOf2ndPromise");
		inputFieldLayout.addComponent(new Label(I18N.message("number.of.3rd.promise")),"lblNumberOf3rdPromise");
		inputFieldLayout.addComponent(new Label(I18N.message("number.of.customer.that.paid")),"lblNumberOfCustomerThatPaid");
		inputFieldLayout.addComponent(new Label(I18N.message("number.of.white.clearance")),"lblNumberOfWhiteClearance");
		inputFieldLayout.addComponent(new Label(I18N.message("number.of.partial.payment")),"lblNumberOfPartialPayment");
		inputFieldLayout.addComponent(new Label(I18N.message("white.clearance.percentag")),"lblWhiteClearancePercentag");
		inputFieldLayout.addComponent(new Label(I18N.message("partial.payment.percentag")),"lblPartialPaymentPercentag");
		inputFieldLayout.addComponent(new Label(I18N.message("total.amount.in.overdues")),"lblTotalAmountInOverdue");
		inputFieldLayout.addComponent(new Label(I18N.message("total.amount.collected")),"lblTotalAmountCollected");
		inputFieldLayout.addComponent(new Label(I18N.message("collected.percentag")),"lblCollectedPercentag");
		inputFieldLayout.addComponent(new Label(I18N.message("adjusted.lock.split")),"lblAdjustedLockSplit");
		//Row 2
		inputFieldLayout.addComponent(new Label(I18N.message("accumulated")),"lblReturnedMotorcycleAccumulated");
		inputFieldLayout.addComponent(new Label(I18N.message("Today")), "lblReturnedMotorcycleToday");
		inputFieldLayout.addComponent(new Label(I18N.message("accumulated")),"lblWhiteClearancePercentagAccumulated");
		inputFieldLayout.addComponent(new Label(I18N.message("Today")), "lblWhiteClearancePercentagToday");
		inputFieldLayout.addComponent(new Label(I18N.message("accumulated")),"lblPartialPaymentPercentagAccumulated");
		inputFieldLayout.addComponent(new Label(I18N.message("Today")), "lblPartialPaymentPercentagToday");
		inputFieldLayout.addComponent(new Label(I18N.message("accumulated")),"lblCollectedPercentagAccumulated");
		inputFieldLayout.addComponent(new Label(I18N.message("Today")), "lblCollectedPercentagToday");
		inputFieldLayout.addComponent(new Label(I18N.message("accumulated")),"lblAdjustedLockSplitAccumulated");
		inputFieldLayout.addComponent(new Label(I18N.message("Today")), "lblAdjustedLockSplitToday");
		//Row 3
		inputFieldLayout.addComponent(new Label(I18N.message("test1")),"lblTest1");
		//Row 4
		inputFieldLayout.addComponent(new Label(I18N.message("test2")),"lblTest2");
		//Row 5
		inputFieldLayout.addComponent(new Label(I18N.message("total")),"lblTotal");
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(inputFieldLayout);
		
		Panel panel = new Panel();
		panel.setContent(horizontalLayout);
		return panel;
	}
}
