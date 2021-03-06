package com.nokor.efinance.ra.ui.panel.dealer;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.gl.finwiz.share.domain.AP.AccountHolderDTO;
import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAccountHolder;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.third.finwiz.client.ap.ClientAccountHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


/**
 * 
 * @author uhout.cheng
 */
public class DealerAccountHolderWindowForm extends Window implements FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 8235577315961672679L;
	
	private TextField txtName;

	/**
	 * 
	 * @param dealer
	 * @param accHolderId
	 * @param deleget
	 */
	public DealerAccountHolderWindowForm(Dealer dealer, Long accHolderId, DealerBankAccountTable deleget) {
		setCaption(I18N.message("payee.details"));
		setModal(true);
		setResizable(false);
						
		txtName = ComponentFactory.getTextField35(false, 180, 180);
		
		SecUser secUser = UserSessionManager.getCurrentUser();
		
		if (accHolderId != null) {
			AccountHolderDTO accountHolderDTO = ClientAccountHolder.getAccountHolderById(accHolderId);
			if (accountHolderDTO != null) {
				txtName.setValue(accountHolderDTO.getName());
			}
		}
        
        /**
         * 
         */
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

        	/** */
			private static final long serialVersionUID = -4197845593815500853L;

			/**
        	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
        	 */
        	@Override
			public void buttonClick(ClickEvent event) {
				if (StringUtils.isNotEmpty(txtName.getValue())) {
					AccountHolderDTO accountHolderDTO = null;
					if (accHolderId == null) {
						accountHolderDTO = new AccountHolderDTO();
						accountHolderDTO.setCreatedBy(secUser.getLogin());
						accountHolderDTO = getAccountHolderDTO(accountHolderDTO);
						try {
							AccountHolderDTO newAccHolder = ClientAccountHolder.getAccountHolderCreate(accountHolderDTO);
							try {
								DealerAccountHolder deaAccHolder = DealerAccountHolder.createInstance();
								deaAccHolder.setDealer(dealer);
								deaAccHolder.setAccountHolder(newAccHolder.getId());
								DEA_SRV.create(deaAccHolder);
							} catch (Exception e) {
								ComponentLayoutFactory.displayErrorMsg(e.getMessage());
							}
						} catch (Exception e) {
							ComponentLayoutFactory.displayErrorMsg("Error while create new account holder. [" + txtName.getValue() + "]");
						}
					} else {
						accountHolderDTO = ClientAccountHolder.getAccountHolderById(accHolderId);
						accountHolderDTO.setUpdatedBy(secUser.getLogin());
						accountHolderDTO = getAccountHolderDTO(accountHolderDTO);
						try {
							ClientAccountHolder.setAccountHolderUpdate(accHolderId, accountHolderDTO);
						} catch (Exception e) {
							ComponentLayoutFactory.displayErrorMsg("Error while update account holder. [" + txtName.getValue() + "]");
						}
					}
					close();
					deleget.assignValues(dealer);
				} else {
					txtName.setRequiredError(I18N.message("field.required.1", new String[] { I18N.message("name") }));
					txtName.focus();
				}
            }
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 6381799138923259384L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
            	close();
            }
        });
		btnCancel.setIcon(FontAwesome.BAN);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		GridLayout gridLayout = new GridLayout(2, 1);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		Label lblName = ComponentFactory.getHtmlLabel(I18N.message("name") + StringUtils.SPACE + "<b style=\"color: red\">*</b>");
		gridLayout.addComponent(lblName);
		gridLayout.addComponent(txtName);
		
		gridLayout.setComponentAlignment(lblName, Alignment.MIDDLE_LEFT);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(gridLayout);
        
        setContent(contentLayout);
	}
	
	/**
	 * 
	 * @param accountHolderDTO
	 * @return
	 */
	private AccountHolderDTO getAccountHolderDTO(AccountHolderDTO accountHolderDTO) {
		accountHolderDTO.setName(txtName.getValue());
		accountHolderDTO.setNameEn(txtName.getValue());
		return accountHolderDTO;
	}
	
}
