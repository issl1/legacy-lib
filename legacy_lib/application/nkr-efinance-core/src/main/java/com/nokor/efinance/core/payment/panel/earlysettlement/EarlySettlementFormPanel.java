package com.nokor.efinance.core.payment.panel.earlysettlement;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Early Settlement form panel
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EarlySettlementFormPanel extends VerticalLayout {

	private static final long serialVersionUID = -2602408442583719738L;
	
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	private Panel contentPanel;
	private AutoDateField dfEarlySettlement;
	private CheckBox cbIncludePenalty;
	private Button btnPaidOff;
	private EarlySettlementPopupPanel earlySettlementPopupPanel;
	
	@PostConstruct
	public void PostConstruct() {
		addComponent(createForm());
	}
	
	
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentPanel = new Panel();
        contentLayout.addComponent(contentPanel);
		return contentLayout;
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(final Long cotraId) {
		
		FormLayout formLayout = new FormLayout();
			
		dfEarlySettlement = new AutoDateField(I18N.message("early.settlement.date"));
		dfEarlySettlement.setValue(DateUtils.today());
		dfEarlySettlement.setEnabled(true);
		//dfEarlySettlement.setEnabled(!ProfileUtil.isPOS());
		
		cbIncludePenalty = new CheckBox(I18N.message("include.penalty"));
		cbIncludePenalty.setValue(true);
		cbIncludePenalty.setVisible(ProfileUtil.isAccountingController());
		
		btnPaidOff = new Button(I18N.message("paid.off"));
		
		final Window window = new Window();
		btnPaidOff.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2006429185329076412L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (dfEarlySettlement.getValue() != null) {
					Contract contract = entityService.getById(Contract.class, cotraId);
					if (contract.getWkfStatus().equals(ContractWkfStatus.FIN)) {
						earlySettlementPopupPanel = new EarlySettlementPopupPanel();
						earlySettlementPopupPanel.assignValues(contract.getId(), dfEarlySettlement.getValue(), cbIncludePenalty.getValue());
						window.setModal(true);
						window.setContent(earlySettlementPopupPanel);
						window.setCaption(I18N.message("paid.off.info"));
						window.setWidth(650, Unit.PIXELS);
						window.setHeight(320, Unit.PIXELS);
						window.center();
				        UI.getCurrent().addWindow(window);
				        
						ClickListener allocateListener = new ClickListener() {
							private static final long serialVersionUID = -7363634930253442559L;
							@Override
							public void buttonClick(ClickEvent event) {
								window.close();
								PaymentInfo2Panel paymentInfo2Panel = new PaymentInfo2Panel();
								paymentInfo2Panel.assignValues(null);
								contentPanel.setContent(paymentInfo2Panel);
							}
						};
				        earlySettlementPopupPanel.setAllocateListener(allocateListener);
					} else {
						MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("information"),
								MessageBox.Icon.ERROR, I18N.message("paid.off.is.already.applied."), Alignment.MIDDLE_RIGHT,
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					}
				} else {
					MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("early.settlement.date.required."), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
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
	}
}
