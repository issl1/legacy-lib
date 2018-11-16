package com.nokor.efinance.ra.ui.panel.contract;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
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
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(UpdateRejectDatesPanel.NAME)
public class UpdateRejectDatesPanel extends Panel implements View {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "update.reject.dates";
	
	@Autowired
	private QuotationService quotationService; 
	
	
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption("Update Contracts Dates");
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);

		Button btnOk = new Button(I18N.message("ok"));
		btnOk.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7761470482429822091L;
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Update reject dates",
				        new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 2380193173874927880L;
							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {	
				                	updateRejectQuotation();
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
	
	private void updateRejectQuotation() {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq("quotationStatus", QuotationWkfStatus.REJ));
		restrictions.addCriterion(Restrictions.isNull("rejectDate"));
		List<Quotation> quotations = quotationService.list(restrictions);
		for (Quotation quotation : quotations) {
			// TODO PYI
//			List<QuotationStatusHistory> his = quotationService.getWkfStatusHistories(quotation.getId(), Order.desc("createDate"));
//			for (QuotationStatusHistory qh : his) {
//				if (qh.getWkfStatus().equals(WkfQuotationStatus.REJ)) {
//					quotation.setRejectDate(qh.getCreateDate());
//					quotationService.saveOrUpdate(quotation);
//				}
//			}
		}
	}
}
