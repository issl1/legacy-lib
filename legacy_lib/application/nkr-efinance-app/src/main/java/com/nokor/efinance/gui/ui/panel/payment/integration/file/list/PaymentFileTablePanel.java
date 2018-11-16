package com.nokor.efinance.gui.ui.panel.payment.integration.file.list;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentFile;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.PrintClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;

/**
 * File Integration Table Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaymentFileTablePanel extends AbstractTablePanel<PaymentFile> implements FinServicesHelper, PrintClickListener {
	/** */
	private static final long serialVersionUID = -4701862449550116038L;
	
	/**
	 * Post Construct
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("payment.files"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("payment.files"));
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		navigationPanel.addPrintClickListener(this);
		navigationPanel.getEditClickButton().setCaption(I18N.message("view"));
		navigationPanel.getEditClickButton().setIcon(FontAwesome.EYE);
		navigationPanel.getPrintClickButton().setCaption(I18N.message("upload"));
		navigationPanel.getPrintClickButton().setIcon(FontAwesome.UPLOAD);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<PaymentFile> createPagedDataProvider() {
		PagedDefinition<PaymentFile> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(PaymentFile.ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		
		pagedDefinition.addColumnDefinition(PaymentFile.SEQUENCE, I18N.message("sequence"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PaymentFile.BANKCODE, I18N.message("bank.code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PaymentFile.COMPANYACCOUNT, I18N.message("company.account"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PaymentFile.COMPANYNAME, I18N.message("company.name"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(PaymentFile.EFFECTIVEDATE, I18N.message("effective.date"), Date.class, Align.LEFT, 100);		
		pagedDefinition.addColumnDefinition(PaymentFile.LASTSEQUENCE, I18N.message("last.sequence"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PaymentFile.TOTALDEBITAMOUNT, I18N.message("total.debit.amount"), Double.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PaymentFile.TOTALDEBITTRANSACTION, I18N.message("total.debit.transaction"), Integer.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition(PaymentFile.TOTALCREDITAMOUNT, I18N.message("total.credit.amount"), Double.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PaymentFile.TOTALCREDITTRANSACTION, I18N.message("total.credit.transaction"), Integer.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition(PaymentFile.TOTALAMOUNT, I18N.message("total.amount"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PaymentFile.TOTALTRANSACTION, I18N.message("total.transaction"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(PaymentFile.WKFSTATUS + "." + EWkfStatus.DESCEN, I18N.message("status"), String.class, Align.LEFT, 80);
		
		EntityPagedDataProvider<PaymentFile> pagedDataProvider = new EntityPagedDataProvider<PaymentFile>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected PaymentFile getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return FILE_INTEGRATION_SRV.getById(PaymentFile.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected PaymentFileSearchPanel createSearchPanel() {
		return new PaymentFileSearchPanel(this);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#editButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void editButtonClick(ClickEvent event) {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.view.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			mainPanel.getTabSheet().setAdd(false);
			mainPanel.onEditEventClick();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.PrintClickListener#printButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void printButtonClick(ClickEvent event) {
		UI.getCurrent().addWindow(new FileIntegrationUploadWindow(this));
	}

}
