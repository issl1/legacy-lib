package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


/**
 * 
 * @author buntha.chea
 *
 */
public class FlagPanel extends VerticalLayout implements FMEntityField, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -6866477870468922772L;
	
	private SimplePagedTable<Contract> pagedTable;
	private List<ColumnDefinition> columnDefinitions; 
	private Item selectedItem = null;
	
	private Button btnSave;
	private Button btnCancel;
	private SecUserComboBox cbxStaff;
	private EColType flagTeam;
	//private String profielCode;
	
	private VerticalLayout messagePanel;
	
	public FlagPanel() {
		setCaption(I18N.message("flag"));
		setMargin(true);
		setSpacing(true);
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		columnDefinitions = createColumnDefinitions();
	    pagedTable = new SimplePagedTable<Contract>(columnDefinitions);
	    pagedTable.addItemClickListener(new ItemClickListener() {
	    	
			/** */
			private static final long serialVersionUID = 4543032249393031819L;

			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				
				if (event.isDoubleClick()) {
					messagePanel.removeAllComponents();
					messagePanel.setVisible(false);
					
					Long contractId = (Long) selectedItem.getItemProperty(ID).getValue();
					flagPopupWindow(ENTITY_SRV.getById(Contract.class, contractId));
				}
				
			}
		});
	    
	    setFlagTeamAndProfileCode();
	     
	    pagedTable.setContainerDataSource(getIndexedContainer());
	    
	    addComponent(pagedTable);
	    addComponent(pagedTable.createControls());
	}
	
	/**
	 * 
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("applicant." + NAME_EN, I18N.message("name.en"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("asset." + DESC_EN, I18N.message("asset"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("financialProduct." + DESC_EN, I18N.message("financial.product"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("dealer." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(WKF_STATUS + "." + DESC_EN, I18N.message("contract.status"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer() {
		IndexedContainer indexedContainer = new IndexedContainer();
					
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		int index = 0;
		List<Contract> contracts = COL_SRV.getFlagContractStatusApprove(flagTeam);
		for (Contract contract : contracts) {
			Item item = indexedContainer.addItem(index);
			item.getItemProperty(ID).setValue(contract.getId());
			item.getItemProperty(REFERENCE).setValue(contract.getReference());
			item.getItemProperty("applicant." + NAME_EN).setValue(contract.getApplicant().getNameEn());
			
			if (contract.getAsset() != null) {
				item.getItemProperty("asset." + DESC_EN).setValue(contract.getAsset().getDescEn());
			}
			
			if (contract.getFinancialProduct() != null) {
				item.getItemProperty("financialProduct." + DESC_EN).setValue(contract.getFinancialProduct().getDescEn());
			}
			
			if (contract.getDealer() != null) {
				item.getItemProperty("dealer." + NAME_EN).setValue(contract.getDealer().getDescEn());
			}
			
			item.getItemProperty(START_DATE).setValue(contract.getStartDate());
			
			if (contract.getWkfStatus() != null) {
				item.getItemProperty(WKF_STATUS + "." + DESC_EN).setValue(contract.getWkfStatus().getDescEn());
			}
				
			index++;
		}
						
		return indexedContainer;
	}
	
	/**
	 * refresh pageTable
	 */
	public void refresh() {
		pagedTable.setContainerDataSource(getIndexedContainer());
	}
	
	/**
	 * popup window for select staff for contract
	 * @param contract
	 */
	private void flagPopupWindow(Contract contract) {
		final Window window = new Window(I18N.message("assist"));
		window.setModal(true);
		window.setResizable(false);
		window.setWidth(480, Unit.PIXELS);
		window.setHeight(155, Unit.PIXELS);
		
		cbxStaff = new SecUserComboBox(I18N.message("staff"), getCollectionPhoneStaffs());
		cbxStaff.setImmediate(true);
		cbxStaff.setRequired(true);
			
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (checkRequire()) {
					
					ContractUserInbox contractUserInbox = new ContractUserInbox();
					contractUserInbox.setContract(contract);
					contractUserInbox.setSecUser(cbxStaff.getSelectedEntity());
					ENTITY_SRV.saveOrUpdate(contractUserInbox);
					COL_SRV.assignFlagRequest(contract.getId());
					window.close();
					
					refresh();
					
					Notification notification = new Notification("",I18N.message("msg.info.save.successfully"), 
												Notification.Type.HUMANIZED_MESSAGE, true);
					notification.show(Page.getCurrent());	  	
				}
			}
		});
		
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 8292212285919430248L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				window.close();
			}
		});
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxStaff);
		
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(formLayout);
		
		window.setContent(verticalLayout);
		UI.getCurrent().addWindow(window);	

	}
	
	/**
	 * Check Mandatory Field
	 * @return
	 */
	public boolean checkRequire() {
		messagePanel.removeAllComponents();
		List<String> errors = new ArrayList<>();
		errors.clear();
		Label messageLabel;
		if (cbxStaff.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("staff") }));
		}
		
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		return errors.isEmpty();
	}
	
	/**
	 * Assist team by login pofile
	 */
	private void setFlagTeamAndProfileCode() {
		//profielCode = "";
		if (ProfileUtil.isColPhoneSupervisor()) {
			flagTeam = EColType.PHONE;
			//profielCode = "COL_PHO_SUPER";
		} else if (ProfileUtil.isColFieldSupervisor()) {
			flagTeam = EColType.FIELD;
			//profielCode = "COL_FIE_SUPER";
		} else if (ProfileUtil.isColInsideRepoSupervisor()) {
			flagTeam = EColType.INSIDE_REPO;
			//profielCode = "COL_INS_SUPER";
		} else if (ProfileUtil.isColOASupervisor()) {
			flagTeam = EColType.OA;
			//profielCode = "COL_OA_SUPER";
		}
	}
	
	/**
	 * list of user by profile login into combo box
	 * @return
	 */
	private List<SecUser> getCollectionPhoneStaffs() {
		SecProfile profile = ENTITY_SRV.getByCode(SecProfile.class, IProfileCode.COL_PHO_STAFF);	
		BaseRestrictions<SecUser> restrictions	 = new BaseRestrictions<>(SecUser.class);
		restrictions.addCriterion(Restrictions.eq("defaultProfile", profile));
		return ENTITY_SRV.list(restrictions);
	}
}
