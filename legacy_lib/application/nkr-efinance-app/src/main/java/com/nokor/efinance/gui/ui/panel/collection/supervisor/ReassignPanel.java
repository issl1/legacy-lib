package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.service.UserContractDetail;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Phone Unassigned Manual Panel
 * @author bunlong.taing
 */
public class ReassignPanel extends Window implements ClickListener, FinServicesHelper {
	/** */
	private static final long serialVersionUID = -6570859719391526461L;
	
	private static final String CURRENT_CONTRACT = "current.contract";
	private static final String NEW_CONTRACT = "new.contract";
	private static final String TOTAL_CONTRACT = "total.contract";
	private static final String ADJUSTED_CONTRACT = "adjusted.contract";
	private static final String REMOVE = "remove";
	
	private EntityComboBox<SecUser> cbxStaff;
	private TextField txtCurrentContracts;
	private TextField txtNewContracts;
	private TextField txtTotalContracts;
	private TextField txtNbContracts;
	private NativeButton btnAdd;
	private NativeButton btnConfirm;
	private NativeButton btnCancel;
	
	private SimpleTable<SecUser> staffTable;
	private VerticalLayout messagePanel;
	
	private int totalNbContract;
	private Map<SecUser, Integer> reassignStaffs;
	private List<String> errors;
	
	private SecUser selectedStaff;
	private UserContractDetail userContractDetail; 
	private  PhoneAssignedPanel mainPanel;
	
	public ReassignPanel(PhoneAssignedPanel mainPanel) {
		this.mainPanel = mainPanel;
		errors = new ArrayList<>();
		reassignStaffs = new HashMap<>();
		setCaption(I18N.message("specific.move"));
		setModal(true);
		setResizable(false);
		setWidth(600, Unit.PIXELS);
		setHeight(450, Unit.PIXELS);
		setContent(createForm());
	}
	
	/**
	 * Create Form
	 * @return
	 */
	private VerticalLayout createForm() {
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		staffTable = new SimpleTable<>(createStaffTableColumnDefinition());
		staffTable.setHeight(250, Unit.PIXELS);
		btnConfirm = new NativeButton(I18N.message("confirm"));
		btnConfirm.addStyleName("btn btn-success");
		btnConfirm.addClickListener(this);
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.addStyleName("btn btn-success");
		btnCancel.addClickListener(this);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(btnConfirm);
		horizontalLayout.addComponent(btnCancel);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(createTopLayout());
		verticalLayout.addComponent(staffTable);
		verticalLayout.addComponent(horizontalLayout);
		verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(messagePanel);
		content.addComponent(verticalLayout);
		
		return content;
	}
	
	/**
	 * Create top layout
	 * @return
	 */
	private GridLayout createTopLayout() {
		Label lblStaff = ComponentFactory.getLabel("staff");
		Label lblCurrentContracts = ComponentFactory.getLabel("current.contracts");
		Label lblNewContracts = ComponentFactory.getLabel("new.contracts");
		Label lblNbContract = ComponentFactory.getLabel("number.contract");
		Label lblTotal = ComponentFactory.getLabel("total");
		
		cbxStaff = new EntityComboBox<>(SecUser.class, SecUser.LOGIN);
		cbxStaff.setWidth(150, Unit.PIXELS);
		cbxStaff.setImmediate(true);
		cbxStaff.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -5798454307855142228L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxStaff.getSelectedEntity() != null) {
					userContractDetail = new UserContractDetail(cbxStaff.getSelectedEntity());
					userContractDetail.setNbCurrentContracts(INBOX_SRV.countCurrentContractByUser(cbxStaff.getSelectedEntity()));					
					txtCurrentContracts.setValue(String.valueOf(userContractDetail.getNbCurrentContracts()));
					txtNewContracts.setValue(String.valueOf(userContractDetail.getNbNewContract()));
					txtTotalContracts.setValue(String.valueOf(userContractDetail.getNbCurrentContracts() + userContractDetail.getNbNewContract()));
				}
			}
		});
		
		txtCurrentContracts = ComponentFactory.getTextField(5, 80);
		txtCurrentContracts.setEnabled(false);
		txtNewContracts = ComponentFactory.getTextField(5, 80);
		txtNewContracts.setEnabled(false);
		txtTotalContracts = ComponentFactory.getTextField(5, 80);
		txtTotalContracts.setEnabled(false);
		
		txtNbContracts = ComponentFactory.getTextField(100, 150);
		txtNbContracts.setImmediate(true);
		txtNbContracts.addValueChangeListener(new ValueChangeListener() {
			/**
			 */
			private static final long serialVersionUID = -8750175540460018533L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxStaff.getSelectedEntity() != null && StringUtils.isNotEmpty(txtNbContracts.getValue())) {
					if (userContractDetail != null) {
						txtTotalContracts.setValue(String.valueOf(Long.parseLong(txtNbContracts.getValue()) + userContractDetail.getNbCurrentContracts() + userContractDetail.getNbNewContract()));
					}
				}
			}
		});
		
		btnAdd = new NativeButton(I18N.message("add"));
		btnAdd.addStyleName("btn btn-success");
		btnAdd.addClickListener(this);
		
		GridLayout content = new GridLayout(6, 2);
		content.setSpacing(true);
		
		content.addComponent(lblStaff);
		content.addComponent(cbxStaff);
		content.addComponent(lblCurrentContracts);
		content.addComponent(txtCurrentContracts);
		content.addComponent(lblNewContracts);
		content.addComponent(txtNewContracts);
		
		content.addComponent(lblNbContract);
		content.addComponent(txtNbContracts);
		content.addComponent(lblTotal);
		content.addComponent(txtTotalContracts);
		content.addComponent(btnAdd);
		
		content.setComponentAlignment(lblStaff, Alignment.MIDDLE_LEFT);
		content.setComponentAlignment(lblCurrentContracts, Alignment.MIDDLE_LEFT);
		content.setComponentAlignment(lblNewContracts, Alignment.MIDDLE_LEFT);
		content.setComponentAlignment(lblNbContract, Alignment.MIDDLE_LEFT);
		content.setComponentAlignment(lblTotal, Alignment.MIDDLE_LEFT);
		
		return content;
	}
	
	/**
	 * Create Staff Table Column Definition
	 * @return
	 */
	private List<ColumnDefinition> createStaffTableColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(SecUser.LOGIN, I18N.message("staff"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(CURRENT_CONTRACT, I18N.message("current.contracts"), Long.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(NEW_CONTRACT, I18N.message("new.contracts"), Long.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(ADJUSTED_CONTRACT, I18N.message("adjusted.contracts"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(TOTAL_CONTRACT, I18N.message("total"), Long.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(REMOVE, I18N.message("remove"), Button.class, Align.CENTER, 50));
		return columnDefinitions;
	}
	
	/**
	 * Set Staff Table Container Data Source
	 */
	@SuppressWarnings("unchecked")
	public void setStaffTableContainerDataSource() {
		staffTable.removeAllItems();
		for (SecUser user : reassignStaffs.keySet()) {
			
			long currentContracts = 0l;
			long newContracts = 0l;
			long adjustedContracts = reassignStaffs.get(user);
			
			if (userContractDetail != null) {
				currentContracts = userContractDetail.getNbCurrentContracts();
				newContracts = userContractDetail.getNbNewContract();
			}
			
			Item item = staffTable.addItem(user);
			item.getItemProperty(SecUser.LOGIN).setValue(user != null ? user.getLogin() : "");
			item.getItemProperty(CURRENT_CONTRACT).setValue(currentContracts);
			item.getItemProperty(NEW_CONTRACT).setValue(newContracts);
			item.getItemProperty(ADJUSTED_CONTRACT).setValue(adjustedContracts);
			item.getItemProperty(TOTAL_CONTRACT).setValue(currentContracts + newContracts + adjustedContracts);
			
			NativeButton btnRemove = new NativeButton();
			btnRemove.addStyleName("btn btn-success");
			btnRemove.setIcon(FontAwesome.TIMES);
			btnRemove.addClickListener(new ClickListener() {
				/** */
				private static final long serialVersionUID = -8895029487274535623L;
				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					reassignStaffs.remove(user);
					setStaffTableContainerDataSource();
				}
			});
			item.getItemProperty(REMOVE).setValue(btnRemove);
		}
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnAdd) {
			if (validateReassignStaff()) {
				addStaff();
			} else {
				displayErrors();
			}
		} else if (event.getButton() == btnConfirm) {
			if (validateReassign()) {
				reassign();
			} else {
				displayErrors();
			}
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}
	
	/**
	 * Validate Reassign Staff
	 * @return
	 */
	private boolean validateReassignStaff() {
		errors.clear();
		boolean valid = true;
		if (cbxStaff.getValue() == null) {
			errors.add(I18N.message("field.required.1", I18N.message("staff")));
			valid = false;
		}
		if (StringUtils.isEmpty(txtNbContracts.getValue())) {
			errors.add(I18N.message("field.required.1", I18N.message("number.contract")));
			valid = false;
		} else if (!ValidateUtil.checkIntegerField(txtNbContracts, "")) {
			errors.add(I18N.message("field.value.incorrect.1", I18N.message("number.contract")));
			valid = false;
		} else {
			int nbContract = MyNumberUtils.getInteger(txtNbContracts.getValue(), 0);
			int nbAssignedContract = 0;
			for (Integer nb : reassignStaffs.values()) {
				nbAssignedContract += nb;
			}
			if (totalNbContract < nbContract + nbAssignedContract) {
				errors.add(I18N.message("msg.error.nb.contract.over.total.nb.reassigned.contract"));
				valid = false;
			}
		}
		return valid;
	}
	
	/**
	 * Add staff
	 */
	private void addStaff() {
		reassignStaffs.put(cbxStaff.getSelectedEntity(), MyNumberUtils.getInteger(txtNbContracts.getValue(), 0));
		setStaffTableContainerDataSource();
		resetControls();
	}
	
	/**
	 * Validate
	 * @return
	 */
	private boolean validateReassign() {
		boolean valid = true;
		errors.clear();
		if (reassignStaffs.isEmpty()) {
			errors.add(I18N.message("msg.error.no.staff.reassign"));
			valid = false;
		}
		return valid;
	}
	
	/**
	 * Reassign
	 */
	private void reassign() {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.reassign"), new ConfirmDialog.Listener() {
			/** */
			private static final long serialVersionUID = 364182479126545046L;
			/**
			 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
			 */
			@Override
			public void onClose(ConfirmDialog dialog) {
				if (dialog.isConfirmed()) {
					INBOX_SRV.transferUserInbox(selectedStaff, reassignStaffs);
					mainPanel.refresh();
					close();
				}
			}
		});
		confirmDialog.setWidth(400, Unit.PIXELS);
		confirmDialog.setHeight(150, Unit.PIXELS);
	}
	
	/**
	 * Assign Values
	 * @param nbContract
	 * @param selectedStaff
	 */
	public void assignValues(int totalNbContract, SecUser selectedStaff) {
		resetAll();
		if (totalNbContract > 0 && selectedStaff != null) {
			this.totalNbContract = totalNbContract;
			this.selectedStaff = selectedStaff;
			String profileCode = selectedStaff.getDefaultProfile() != null ? selectedStaff.getDefaultProfile().getCode() : "";
			List<SecUser> staffs = COL_SRV.getCollectionUsers(new String[] { profileCode });
			staffs.remove(selectedStaff);
			cbxStaff.renderer(staffs);
		}
	}
	
	/**
	 * Reset the window popup
	 */
	public void resetAll() {
		errors.clear();
		reassignStaffs.clear();
		
//		selectedStaff = null;
		totalNbContract = 0;
		staffTable.removeAllItems();
		resetControls();
	}
	
	/**
	 * Reset controls
	 */
	public void resetControls() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		
		cbxStaff.setSelectedEntity(null);
		txtCurrentContracts.setValue("");
		txtNewContracts.setValue("");
		txtTotalContracts.setValue("");
		txtNbContracts.setValue("");
	}
	
	/**
	 * Show the window popup
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
	}
	
	/**
	 * Display Errors Panel
	 */
	public void displayErrors() {
		messagePanel.removeAllComponents();
		if (!errors.isEmpty()) {
			for (String error : errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
	}

}
