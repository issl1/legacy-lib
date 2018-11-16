package com.nokor.efinance.gui.ui.panel.contract.reverse;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.aftersales.ReverseContractStatusService;
import com.nokor.efinance.core.contract.service.aftersales.ReverseContractStatusValidateRequest;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author meng.kim
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReverseContractStatusFormPanel extends VerticalLayout {

	private static final long serialVersionUID = -2602408442583719738L;
	
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	private ReverseContractStatusService reverseContractStatusService = SpringUtils.getBean(ReverseContractStatusService.class);
	
	private Panel contentPanel;
	private Button btnReverse;
	private ReverseContractStatusValidateRequest reverseContractStatusValidateRequest;
	
	@PostConstruct
	public void PostConstruct() {
		addComponent(createForm());
	}
	
	/**
	 * initiate form components 
	 * @return
	 */
	protected com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentPanel = new Panel();
        contentLayout.addComponent(contentPanel);
		return contentLayout;
	}
	
	/**
	 * @param contract id
	 */
	public void assignValues(Long cotraId) {
		final Contract contract = entityService.getById(Contract.class, cotraId);
		
		FormLayout formLayout = new FormLayout();
			
		reverseContractStatusValidateRequest = new ReverseContractStatusValidateRequest();
		reverseContractStatusValidateRequest.setCotraId(contract.getId());
		reverseContractStatusValidateRequest.setWkfStatus(contract.getWkfStatus());
		
		btnReverse = new Button(I18N.message("reverse.to.activate"));
		
		btnReverse.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2006429185329076412L;
			@Override
			public void buttonClick(ClickEvent event) {
				reverseContractStatusService.validate(reverseContractStatusValidateRequest);
				Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
				notification.setDescription(I18N.message("success.change.status.contract"));
				notification.setDelayMsec(3000);
				notification.show(Page.getCurrent());
			}
		});
        
		TextField txtReference = new TextField(I18N.message("reference"));
		txtReference.setValue(contract.getReference());
		txtReference.setEnabled(false);
		
		TextField txtCurrentContractStatus = new TextField(I18N.message("current.contract.status"));
		txtCurrentContractStatus.setValue(contract.getWkfStatus().getDesc());
		txtCurrentContractStatus.setEnabled(false);
		
		
		formLayout.addComponent(txtReference);
		formLayout.addComponent(txtCurrentContractStatus);
		formLayout.addComponent(btnReverse);
		VerticalLayout earlySettmentLayout = new VerticalLayout();
		earlySettmentLayout.setMargin(true);
		earlySettmentLayout.addComponent(formLayout);
        contentPanel.setContent(earlySettmentLayout);
	}
}
