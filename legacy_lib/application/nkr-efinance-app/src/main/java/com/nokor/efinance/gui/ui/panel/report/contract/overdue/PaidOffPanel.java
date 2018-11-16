package com.nokor.efinance.gui.ui.panel.report.contract.overdue;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.payment.panel.earlysettlement.EarlySettlementPopupPanel;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * EarlySettlement panel
 * @author uhout.cheng
 * @author Chakriya.POV (modified)
 */
public class PaidOffPanel extends AbstractTabPanel {

	/** */
	private static final long serialVersionUID = -8911289076695224618L;
	
	private Panel contentPanel;
	private Button btnPaidOff;
	private AutoDateField dfEarlySettlement;
	private CheckBox cbIncludePenalty;
	private EarlySettlementPopupPanel earlySettlementPopupPanel;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentPanel = new Panel();
        contentLayout.addComponent(contentPanel);
		return contentLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(final Contract contract) {
		
		FormLayout formLayout = new FormLayout();
		
		dfEarlySettlement = new AutoDateField();
		dfEarlySettlement.setValue(DateUtils.today());
		dfEarlySettlement.setEnabled(true);
		dfEarlySettlement.setVisible(ProfileUtil.isCollectionController() || ProfileUtil.isCollectionSupervisor());
		
		cbIncludePenalty = new CheckBox(I18N.message("include.penalty"));
		cbIncludePenalty.setValue(true);
		cbIncludePenalty.setVisible(ProfileUtil.isCollectionController() || ProfileUtil.isCollectionSupervisor());
		
		btnPaidOff = new Button(I18N.message("paid.off"));
		
		final Window window = new Window();
		btnPaidOff.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2015429185329076412L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (dfEarlySettlement.getValue() != null) {
				earlySettlementPopupPanel = new EarlySettlementPopupPanel();
				earlySettlementPopupPanel.assignValues(contract.getId(), dfEarlySettlement.getValue(), cbIncludePenalty.getValue());
//				earlySettlementPopupPanel.assignValues(contract.getId(), DateUtils.today(), true);
				window.setModal(true);
				window.setContent(earlySettlementPopupPanel);
				window.setCaption(I18N.message("paid.off.info"));
				window.setWidth(650, Unit.PIXELS);
				window.setHeight(320, Unit.PIXELS);
				window.center();
				UI.getCurrent().addWindow(window);
				}
			}
		});
        
		formLayout.addComponent(dfEarlySettlement);
		formLayout.addComponent(cbIncludePenalty);
		formLayout.addComponent(btnPaidOff);
		
		VerticalLayout earlySettmentLayout = new VerticalLayout();
		earlySettmentLayout.setMargin(true);
		earlySettmentLayout.addComponent(formLayout);
        contentPanel.setContent(earlySettmentLayout);
}}
