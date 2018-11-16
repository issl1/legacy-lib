package com.nokor.efinance.gui.ui.panel.payment.locksplit;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class LockSplitSearchPanel extends AbstractSearchPanel<LockSplit>  implements FinServicesHelper {

	/**
	 */
	private static final long serialVersionUID = -658996800702181915L;
	
	private AutoDateField dfDueDateFrom;
	private AutoDateField dfDueDateTo;
	private ERefDataComboBox<EPaymentChannel> cbxChannel;
	private EntityComboBox<Dealer> cbxDealerShipName;
	private TextField txtContractId;
	
	private CheckBox cbExpired;
	private TextField txtCode;
	private AutoDateField dfCreateDate;
	private SecUserComboBox cbxUser;
	
	public LockSplitSearchPanel(LockSplitTablePanel lockSplitTablePanel) {
		super(I18N.message("search"), lockSplitTablePanel);
	}
	
	@Override
	protected Component createForm() {
		final GridLayout gridLayout = new GridLayout(10, 3);		
		gridLayout.setSpacing(true);
		gridLayout.setMargin(new MarginInfo(true, true, false, true));
		
		dfDueDateFrom = ComponentFactory.getAutoDateField();
		dfDueDateFrom.setValue(DateUtils.addMonthsDate(DateUtils.todayH00M00S00(), -1));
		
		dfDueDateTo = ComponentFactory.getAutoDateField();
		dfDueDateTo.setValue(DateUtils.todayH00M00S00());
	
		cbxChannel = new ERefDataComboBox<EPaymentChannel>(EPaymentChannel.values());
		cbxChannel.setWidth(150, Unit.PIXELS);
		
		cbxDealerShipName = new EntityComboBox<Dealer>(Dealer.class, Dealer.NAMELOCALE);
		cbxDealerShipName.setWidth(150, Unit.PIXELS);
		cbxDealerShipName.renderer();
		
		txtContractId = ComponentFactory.getTextField();
		cbExpired = new CheckBox(I18N.message("expired"));
		txtCode = ComponentFactory.getTextField();
		dfCreateDate = ComponentFactory.getAutoDateField();
		cbxUser = new SecUserComboBox(ENTITY_SRV.list(SecUser.class));
		cbxUser.setWidth(150, Unit.PIXELS);
		
		cbExpired.setValue(false);
		
		Label lblDueDateFrom = ComponentFactory.getLabel(I18N.message("payment.date.from"));
		Label lblDueDateTo = ComponentFactory.getLabel(I18N.message("payment.date.to"));
		Label lblChannel = ComponentFactory.getLabel(I18N.message("channel"));
		Label lblDealerShipName = ComponentFactory.getLabel(I18N.message("dealer.ship.name"));
		Label lblContractId = ComponentFactory.getLabel(I18N.message("contract.id"));
		Label lblCode = ComponentFactory.getLabel(I18N.message("code"));
		Label lblCreateDate = ComponentFactory.getLabel(I18N.message("create.date"));
		Label lblUserId = ComponentFactory.getLabel(I18N.message("user.id"));
		
		int iCol = 0;
		gridLayout.addComponent(lblDueDateFrom, iCol++, 0);
		gridLayout.addComponent(dfDueDateFrom, iCol++, 0);
		gridLayout.addComponent(lblDueDateTo, iCol++, 0);
		gridLayout.addComponent(dfDueDateTo, iCol++, 0);
		gridLayout.addComponent(lblChannel, iCol++, 0);
		gridLayout.addComponent(cbxChannel, iCol++, 0);
		gridLayout.addComponent(lblDealerShipName, iCol++, 0);
		gridLayout.addComponent(cbxDealerShipName, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblContractId, iCol++, 1);
		gridLayout.addComponent(txtContractId, iCol++, 1);
		gridLayout.addComponent(lblCode, iCol++, 1);
		gridLayout.addComponent(txtCode, iCol++, 1);
		gridLayout.addComponent(lblCreateDate, iCol++, 1);
		gridLayout.addComponent(dfCreateDate, iCol++, 1);
		gridLayout.addComponent(lblUserId, iCol++, 1);
		gridLayout.addComponent(cbxUser, iCol++, 1);
		
		iCol = 3;
		gridLayout.addComponent(cbExpired, iCol++, 2);
		
		gridLayout.setComponentAlignment(lblDueDateFrom, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblDueDateTo, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblChannel, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblDealerShipName, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblCode, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblCreateDate, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblContractId, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblUserId, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbExpired, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		buttonLayout.addComponent(btnSearch);
		buttonLayout.addComponent(btnReset);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, false, false, false), false);
		mainLayout.addComponent(gridLayout);
		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		
		Panel mainPanel = new Panel(mainLayout);
		mainPanel.setCaption(I18N.message("filters"));
		return mainPanel;
	}
	
	/**
	 * Reset
	 */
	@Override
	protected void reset() {
		dfDueDateFrom.setValue(null);
		dfDueDateTo.setValue(null);
		cbxChannel.setSelectedEntity(null);
		cbxDealerShipName.setSelectedEntity(null);
		txtContractId.setValue("");
		txtCode.setValue("");
		dfCreateDate.setValue(null);
		cbxUser.setSelectedEntity(null);
	}
	
	@Override
	public BaseRestrictions<LockSplit> getRestrictions() {
		
		BaseRestrictions<LockSplit> restrictions = new BaseRestrictions<>(LockSplit.class);
		restrictions.addCriterion(Restrictions.ne("wkfStatus", LockSplitWkfStatus.LCAN));
		
		if (dfDueDateFrom.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("from", dfDueDateFrom.getValue())); 
		}
		if (dfDueDateTo.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("to", dfDueDateTo.getValue()));
		}
		if (cbxChannel.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("paymentChannel", cbxChannel.getSelectedEntity()));
		}
		if (cbxDealerShipName.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealer", cbxDealerShipName.getSelectedEntity()));
		}
		if (!StringUtils.isEmpty(txtContractId.getValue())) {
			restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.ilike("con.reference", txtContractId.getValue(), MatchMode.ANYWHERE));
		}
		if (!cbExpired.getValue()) {
			restrictions.addCriterion(Restrictions.ne("wkfStatus", LockSplitWkfStatus.LEXP));
		}
		if (!StringUtils.isEmpty(txtCode.getValue())) {
			restrictions.addAssociation("items", "ite", JoinType.INNER_JOIN);
			restrictions.addAssociation("ite.lockSplitType", "lckType", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.ilike("lckType.code", txtCode.getValue(), MatchMode.ANYWHERE));
		}
		if (dfCreateDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("createDate", DateUtils.getDateAtBeginningOfDay(dfCreateDate.getValue())));
			restrictions.addCriterion(Restrictions.le("createDate", DateUtils.getDateAtEndOfDay(dfCreateDate.getValue())));
		}
		if (cbxUser.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("createUser", cbxUser.getSelectedEntity().getLogin()));
		}
		restrictions.addOrder(Order.desc("createDate"));
		return restrictions;
	}
}
