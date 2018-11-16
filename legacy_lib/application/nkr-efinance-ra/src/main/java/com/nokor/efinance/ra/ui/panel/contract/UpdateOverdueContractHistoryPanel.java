package com.nokor.efinance.ra.ui.panel.contract;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.collection.model.ContractCollectionHistory;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(UpdateOverdueContractHistoryPanel.NAME)
public class UpdateOverdueContractHistoryPanel extends Panel implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "update.contract.overdue.history";
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	@Autowired
	private QuotationService quotationService; 
	
	
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption("Update Contract Overdue History");
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);

		Button btnOk = new Button(I18N.message("ok"));
		btnOk.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7761470482429822091L;
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Update Contract Overdues History ?",
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 2380193173874927880L;
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {	
				                	updateContractOverdueHistory();
				                }
							}
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");	
			}
		});
		
        final GridLayout gridLayout = new GridLayout(5, 1);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
        int iCol = 0;
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(btnOk, iCol++, 0);
        verticalLayout.addComponent(gridLayout);
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	private void updateContractOverdueHistory() {
		BaseRestrictions<ContractCollectionHistory> restrictions = new BaseRestrictions<>(ContractCollectionHistory.class);
		List<ContractCollectionHistory> contractCollectionHistorys = quotationService.list(restrictions);
		for (ContractCollectionHistory contractCollectionHistory : contractCollectionHistorys) {
			if(contractCollectionHistory.getAssignee() == null){
				contractCollectionHistory.setAssignee(contractCollectionHistory.getCollectionOfficer2());
				entityService.saveOrUpdate(contractCollectionHistory);
			}
		}
	}
}
