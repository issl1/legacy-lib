package com.nokor.efinance.gui.ui.panel.collection;

import java.io.IOException;
import java.io.InputStream;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.security.model.SecUser;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public class CollectionInboxSecondSummaryPanel extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	public CollectionInboxSecondSummaryPanel() {
		setSizeFull();
		setHeight("100%");
		setMargin(true);
		setSpacing(true);
		addComponent(createSecondSummaryPanel());
	}
	
	/**
	 * 
	 * @return
	 */
	public CustomLayout createSecondSummaryPanel() {
		String template = "";
		SecUser secUser = UserSessionManager.getCurrentUser();
		if (ProfileUtil.isColPhone(secUser)) {
			template = "collectionInboxSecondSummaryInProfilePhoneTable";
		} else {
			template = "collectionInboxSecondSummaryInProfileBillTable";
		}
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}	
		customLayout.addComponent(new Label(I18N.message("payment.at.shop")),"lblPaymentAtShop");
		customLayout.addComponent(new Label(I18N.message("transfered")),"lblTransfered");
		customLayout.addComponent(new Label(I18N.message("no.answer")),"lblNoAnswer");
		customLayout.addComponent(new Label(I18N.message("basy.call.back.later")),"lblBusyCallBackLater");
		customLayout.addComponent(new Label(I18N.message("wrong.or.dead.number")),"lblWrongNumber");
		customLayout.addComponent(new Label(I18N.message("does.not.want.to.pay")),"lblDoesNotWantToPay");
		if (ProfileUtil.isColBill(secUser)) {
			customLayout.addComponent(new Label(I18N.message("lessee.has.disappeared")),"lblLesseeHasDisappeared");
			customLayout.addComponent(new Label(I18N.message("lost.or.damaged.motorbike")),"lblLostOrDamagedMotorbike");
			customLayout.addComponent(new Label(I18N.message("motorbike.at.police.station")), "lblMotorbikeAtPoliceStation");
			customLayout.addComponent(new Label(I18N.message("other.key.in")), "lblOtherKeyIn");
		}
		customLayout.addComponent(new Label(I18N.message("total")),"lblTotal");
		return customLayout;
	}

}
