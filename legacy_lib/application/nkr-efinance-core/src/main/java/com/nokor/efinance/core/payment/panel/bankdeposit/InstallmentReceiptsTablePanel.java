package com.nokor.efinance.core.payment.panel.bankdeposit;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.document.panel.DocumentViewver;
import com.nokor.efinance.core.payment.model.InstallmentReceipt;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Runo;

/**
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InstallmentReceiptsTablePanel extends AbstractTablePanel<InstallmentReceipt> {
	
	/** */
	private static final long serialVersionUID = 5204101495689053450L;
	private static final String INSTALLMENT_RECEIPT = "installment.receipt";

	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message(INSTALLMENT_RECEIPT));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		super.init("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<InstallmentReceipt> createPagedDataProvider() {
		PagedDefinition<InstallmentReceipt> pagedDefinition = new PagedDefinition<InstallmentReceipt>(searchPanel.getRestrictions());
		
		pagedDefinition.setRowRenderer(new InstallmentRowRenderer());
		pagedDefinition.addColumnDefinition("dealer", I18N.message("dealer"), String.class, Align.LEFT, 230);
		pagedDefinition.addColumnDefinition("user.upload", I18N.message("user.upload"), String.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition("download", I18N.message("download"), Button.class, Align.CENTER, 65);
		pagedDefinition.addColumnDefinition("upload.date", I18N.message("upload.date"), Date.class, Align.LEFT, 75);
		pagedDefinition.addColumnDefinition("file.name", I18N.message("file.name"), String.class, Align.LEFT, 300);
		
		EntityPagedDataProvider<InstallmentReceipt> pagedDataProvider = new EntityPagedDataProvider<InstallmentReceipt>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @author bunlong.taing
	 */
	private class InstallmentRowRenderer implements RowRenderer {

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.RowRenderer#renderer(com.vaadin.data.Item, org.seuksa.frmk.Entity.model.entity.Entity)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			InstallmentReceipt installmentReceipt = (InstallmentReceipt) entity;
			
			Button btnPath = new Button();
			btnPath.setIcon(new ThemeResource("../nkr-default/icons/16/pdf.png"));
			btnPath.setData(installmentReceipt.getPath());
			btnPath.setStyleName(Runo.BUTTON_LINK);
			btnPath.addClickListener(new ClickListener() {
				/** */
				private static final long serialVersionUID = 8230089820671415953L;
				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					String filePathName = (String) ((Button) event.getSource()).getData();
					String tmpDir = "";
	            	if (filePathName.indexOf("tmp_") != -1) {
	            		tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir"); 
	            	} else {
	            		tmpDir = AppConfig.getInstance().getConfiguration().getString("document.path");
	            	}
	            	UI.getCurrent().addWindow(new DocumentViewver(I18N.message(""), tmpDir + "/" + filePathName));
				}
			});
			
			item.getItemProperty("dealer").setValue(installmentReceipt.getDealer().getNameEn());
			item.getItemProperty("user.upload").setValue(installmentReceipt.getUserUpload().getDesc());
			item.getItemProperty("upload.date").setValue(installmentReceipt.getUploadDate());
			item.getItemProperty("file.name").setValue(installmentReceipt.getPath());
			item.getItemProperty("download").setValue(btnPath);
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected InstallmentReceipt getEntity() {
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<InstallmentReceipt> createSearchPanel() {
		return new InstallmentReceiptsSearchPanel(this);
	}
}
