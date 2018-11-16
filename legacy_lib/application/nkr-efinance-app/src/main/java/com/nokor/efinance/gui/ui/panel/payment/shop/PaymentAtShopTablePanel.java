package com.nokor.efinance.gui.ui.panel.payment.shop;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentFile;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
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
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaymentAtShopTablePanel extends AbstractTablePanel<PaymentFileItem> implements FinServicesHelper, PrintClickListener {

	/** */
	private static final long serialVersionUID = 1530839759685898023L;

	/**
	 * Post Construct
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("files.upload"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("files.upload"));
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addAddClickListener(this);
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
	protected PagedDataProvider<PaymentFileItem> createPagedDataProvider() {
		PagedDefinition<PaymentFileItem> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(PaymentFile.ID, I18N.message("id"), Long.class, Align.LEFT, 70);
		
		pagedDefinition.addColumnDefinition(PaymentFileItem.DEALERNO, I18N.message("dealer.id"), String.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(PaymentFileItem.CUSTOMERREF1, I18N.message("contract.id"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(PaymentFileItem.PAYMENTDATE, I18N.message("payment.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(PaymentFileItem.AMOUNT, I18N.message("amount"), Double.class, Align.RIGHT, 100);
		pagedDefinition.addColumnDefinition(PaymentFileItem.WKFSTATUS + "." + EWkfStatus.DESCEN, I18N.message("status"), String.class, Align.LEFT, 80);
		
		EntityPagedDataProvider<PaymentFileItem> pagedDataProvider = new EntityPagedDataProvider<PaymentFileItem>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected PaymentFileItem getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(PaymentFileItem.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected PaymentAtShopSearchPanel createSearchPanel() {
		return new PaymentAtShopSearchPanel(this);
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
		UI.getCurrent().addWindow(new PaymentAtShopFileUploadWindow(this));
	}

}
