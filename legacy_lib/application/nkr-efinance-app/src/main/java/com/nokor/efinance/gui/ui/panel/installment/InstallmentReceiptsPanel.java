package com.nokor.efinance.gui.ui.panel.installment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.document.panel.DisplayDocumentPanel;
import com.nokor.efinance.core.payment.model.InstallmentReceipt;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(InstallmentReceiptsPanel.NAME)
public class InstallmentReceiptsPanel extends AbstractTabPanel implements View, ClickListener, FrmkServicesHelper {

	/** */
	private static final long serialVersionUID = 3868237242905511913L;
	public static final String NAME = "installments.receipt";
	
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private Upload upload;
	private Button btnDelete;
	private Map<InstallmentReceipt, CheckBox> mapReceipt;
	private SimplePagedTable<InstallmentReceipt> pagedTable;
	private InstallmentReceiptUploader uploader;
	private ValueChangeListener valueChangeListener;
	private Button btnSearch;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		cbxDealer = new DealerComboBox(I18N.message("dealer"), ENTITY_SRV.list(getDealerRestriction()));
		cbxDealer.setWidth("220px");
		
		/*List<DealerType> dealerTypes = DealerType.list();
		dealerTypes.remove(DealerType.OTH);*/
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setWidth("220px");
		
		btnSearch = ComponentFactory.getButton("search");
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(getButtonClickListener());	// add button click listener
		
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 2870696494290782085L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = getDealerRestriction();
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		};
		cbxDealerType.addValueChangeListener(valueChangeListener);
		
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SecUserDetail usrDetail = ENTITY_SRV.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
		if (ProfileUtil.isPOS() && usrDetail != null && usrDetail.getDealer() != null) {
			cbxDealerType.setSelectedEntity(usrDetail.getDealer() != null ? usrDetail.getDealer().getDealerType() : null);
			cbxDealer.setSelectedEntity(usrDetail.getDealer());
		}
		
		//disable dealer selection if in co/po profile
		if (ProfileUtil.isCreditOfficer() || ProfileUtil.isProductionOfficer()) {
        	cbxDealer.setEnabled(false);
        	cbxDealerType.setEnabled(false);
		}
		 if(ProfileUtil.isCreditOfficerMovable()){
	        	cbxDealer.setEnabled(false);
	        	cbxDealerType.setEnabled(false);
	        }
		
		//dfDate = ComponentFactory.getAutoDateField("date", false);
		dfStartDate = ComponentFactory.getAutoDateField("startdate", false);
		//dfStartDate.setValue(DateUtils.today());
		dfStartDate.setValue(DateUtils.minusMonth(DateUtils.today(), 12));
		dfEndDate = ComponentFactory.getAutoDateField("enddate", false);
		dfEndDate.setValue(DateUtils.today());
		
		final InstallmentReceiptUploader installmentReceiptUploader = new InstallmentReceiptUploader(this);
		uploader = installmentReceiptUploader;
		upload = new Upload();
		upload.setReceiver(uploader);
		upload.addSucceededListener(uploader);
		btnDelete = ComponentFactory.getButton("delete");
		btnDelete.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		btnDelete.addClickListener(this);
		pagedTable = new SimplePagedTable<InstallmentReceipt>(createColumnDefinitions());
		
		TabSheet tabSheet = new TabSheet();
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		
		HorizontalLayout horizontalLayoutTop = new HorizontalLayout();
		horizontalLayoutTop.setSpacing(true);
		horizontalLayoutTop.addComponent(new FormLayout(cbxDealerType));
		horizontalLayoutTop.addComponent(new FormLayout(cbxDealer));
		//horizontalLayoutTop.addComponent(new FormLayout(dfStartDate));
		//horizontalLayoutTop.addComponent(new FormLayout(dfEndDate));
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(new FormLayout(dfStartDate));
		horizontalLayout.addComponent(new FormLayout(dfEndDate));
		horizontalLayout.addComponent(new FormLayout(btnSearch));
		
		horizontalLayout.addComponent(new FormLayout(upload));
		horizontalLayout.addComponent(new FormLayout(btnDelete));
		
		verticalLayout.addComponent(horizontalLayoutTop);
		verticalLayout.addComponent(horizontalLayout);
		
		Panel panelSearch = new Panel(I18N.message("upload"));
		panelSearch.setContent(verticalLayout);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.setMargin(true);
		content.addComponent(panelSearch);
		content.addComponent(pagedTable);
		content.addComponent(pagedTable.createControls());
		refresh();
		
		tabSheet.addTab(content, I18N.message("installment.receipt"));
		
		return tabSheet;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		// restrictions.addCriterion(Restrictions.ne("dealerType", DealerType.OTH));
		
		return restrictions;
	}
	
	/**
	 * Create a list of ColumnDefinition
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions () {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		
		columnDefinitions.add(new ColumnDefinition("check", I18N.message("check"), CheckBox.class, Align.CENTER, 40));
		columnDefinitions.add(new ColumnDefinition("download", I18N.message("download"), Button.class, Align.CENTER, 65));
		columnDefinitions.add(new ColumnDefinition("upload.date", I18N.message("upload.date"), Date.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("file.name", I18N.message("file.name"), String.class, Align.LEFT, 300));
		
		return columnDefinitions;
	}
	
	/**
	 * Set the ContainerDataSource of table
	 * @param lstInstallmentReceipts
	 */
	private void setContainerDataSource (List<InstallmentReceipt> lstInstallmentReceipts) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		
		if (lstInstallmentReceipts == null) {
			return;
		}
		mapReceipt = new HashMap<InstallmentReceipt, CheckBox>();
		for (InstallmentReceipt installmentReceipt : lstInstallmentReceipts) {
			Button btnPath = new Button();
			btnPath.setIcon(new ThemeResource("../nkr-default/icons/16/pdf.png"));
			btnPath.setData(installmentReceipt.getPath());
			btnPath.setStyleName(Runo.BUTTON_LINK);
			btnPath.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1674938731111758211L;
				@Override
				public void buttonClick(ClickEvent event) {
					new DisplayDocumentPanel((String) ((Button) event.getSource()).getData()).display();
				}
			});
			CheckBox checkBox = new CheckBox();
			mapReceipt.put(installmentReceipt, checkBox);
			
			Item item = indexedContainer.addItem(installmentReceipt.getId());
			item.getItemProperty("check").setValue(checkBox);
			item.getItemProperty("upload.date").setValue(installmentReceipt.getUploadDate());
			item.getItemProperty("file.name").setValue(installmentReceipt.getPath());
			item.getItemProperty("download").setValue(btnPath);
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * Get Restrictions
	 * @return
	 */
	private BaseRestrictions<InstallmentReceipt> getRestrictions () {
		BaseRestrictions<InstallmentReceipt> restrictions = new BaseRestrictions<InstallmentReceipt>(InstallmentReceipt.class);
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
			
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("uploadDate", DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("uploadDate", DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//restrictions.addCriterion(Restrictions.eq("userUpload", secUser));
		restrictions.addCriterion(Restrictions.eq("dealer", getDealer()));
		restrictions.addOrder(Order.desc("uploadDate"));
		
		return restrictions;
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnDelete) {
			delete();
		}
	}
	
	/**
	 * Delete the selected InstallmentReceipt from the database
	 */
	private void delete() {
		if (mapReceipt == null) {
			return;
		}
		for (InstallmentReceipt installmentReceipt : mapReceipt.keySet()) {
			if (mapReceipt.get(installmentReceipt).getValue()) {
				ENTITY_SRV.delete(installmentReceipt);
			}
		}
		MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
				MessageBox.Icon.INFO, I18N.message("delete.successfully"), Alignment.MIDDLE_RIGHT,
				new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
		mb.show();
		refresh();
	}
	
	/**
	 * Refresh or reload the table
	 */
	public void refresh () {
		setContainerDataSource(ENTITY_SRV.list(getRestrictions()));
	}
	
	/**
	 * Get the selected dealer
	 * @return Dealer
	 */
	public Dealer getDealer () {
		return cbxDealer.getSelectedEntity();
	}
	
	/**
	 * Get the selected date
	 * @return Date
	 */
	public Date getDate () {
		return dfEndDate.getValue();
	}
//***on search**
	/**
	 * Get the button click listener for the search and resbutton
	 * @return Button Click Listener
	 */
	private ClickListener getButtonClickListener() {
		return new ClickListener() {
			/** */
			private static final long serialVersionUID = -774154222891913537L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (event.getButton() == btnSearch) { // Button Search is clicked
					onSearch();
				} 
			}
		};
	}
	/**
	 * Search
	 */
	private void onSearch() {
		List<InstallmentReceipt> contracts = ENTITY_SRV.list(getRestrictions());
		setContainerDataSource(contracts);
	}

}
