package com.nokor.efinance.ra.ui.panel.dealer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAssetModel;
import com.nokor.efinance.core.dealer.model.DealerEmployee;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EJobPosition;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DealerEmployeeTablePanel extends VerticalLayout implements FMEntityField {

	/** */
	private static final long serialVersionUID = 7237030075370921376L;
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<DealerAssetModel> pagedTable;
	private Item selectedItem = null;
	private long dealerId;
	private Dealer dealer;
	private Long dealerEmployeeId;
	private VerticalLayout messagePanel;
	
	private DealerEmployee dealerEmployee;
	
	private TextField txtReference;
	private TextField txtFistNameEn;
	private TextField txtLastNameEn;
	private TextField txtNickName;
	private ERefDataComboBox<ETypeIdNumber> cbxTypeIdNumber;
	private TextField txtIdNumber;
	private AutoDateField dfissuingIdNumberDate;
	private AutoDateField dfExpiringIdNumberDate;
	private ERefDataComboBox<ECivility> cbxCivility;
	private AutoDateField dfBirthdate;
	private EntityRefComboBox<Province> cbxProvince;
	private ERefDataComboBox<EGender> cbxGender;
	private ERefDataComboBox<EMaritalStatus> cbxMaritalStatus;
	private ERefDataComboBox<ENationality> cbxNationality;
	
	private AutoDateField dfEnrollmentDate;
	private AutoDateField dfQuitDate;
	private ERefDataComboBox<EJobPosition> cbxJobPosition;
	private ERefDataComboBox<ETypeContact> cbxTypeContact;
	private TextField txtEmail;
	private TextField txtTel;
	private TextArea txtComment;
	
	@PostConstruct
	public void PostConstruct() {
		setSizeFull();
		setMargin(true);
		setSpacing(true);	
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAddDealerEmployee = new NativeButton(I18N.message("add"));
		btnAddDealerEmployee.setIcon(FontAwesome.PLUS_CIRCLE);
		btnAddDealerEmployee.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4058398610792221873L;

			@Override
			public void buttonClick(ClickEvent event) {
				messagePanel.removeAllComponents();
				messagePanel.setVisible(false);
				dealerEmployeeId = null;
				getAssetModelForm(dealerEmployeeId);
			}
		});		
		
		NativeButton btnDeleteDealerEmployee = new NativeButton(I18N.message("delete"));
		btnDeleteDealerEmployee.setIcon(FontAwesome.TRASH_O);
		btnDeleteDealerEmployee.addClickListener(new ClickListener() {
			
			/***/
			private static final long serialVersionUID = -2425213680429632766L;

			@Override
			public void buttonClick(ClickEvent event) {
				delete();
			}
		});
		
		navigationPanel.addButton(btnAddDealerEmployee);
		navigationPanel.addButton(btnDeleteDealerEmployee);
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<DealerAssetModel>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
			}
		});
		
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	public void getAssetModelForm(final Long dealerEmployeeId) {
		this.dealerEmployeeId = dealerEmployeeId;
		final Window winDealerEmployee = new Window(I18N.message("employee"));
		winDealerEmployee.setModal(true);
		winDealerEmployee.setResizable(false);
		winDealerEmployee.setWidth(760, Unit.PIXELS);
		winDealerEmployee.setHeight(530, Unit.PIXELS);
	    
		VerticalLayout contentLayout = new VerticalLayout(); 
		
		txtReference = ComponentFactory.getTextField("reference", false, 60, 200);
		txtFistNameEn = ComponentFactory.getTextField("firstname.en", false, 60, 200);
		txtLastNameEn = ComponentFactory.getTextField("lastname.en", false, 60, 200);
		txtNickName = ComponentFactory.getTextField("nickname", false, 60, 200);
		cbxTypeIdNumber = new ERefDataComboBox<>(I18N.message("id.type"), ETypeIdNumber.class);
		txtIdNumber = ComponentFactory.getTextField("id.number", false, 60, 200);
		dfissuingIdNumberDate = ComponentFactory.getAutoDateField("issuing.id.date", false);
		dfExpiringIdNumberDate = ComponentFactory.getAutoDateField("expiring.id.date", false);
		cbxCivility = new ERefDataComboBox<>(I18N.message("civility"), ECivility.class);
		dfBirthdate = ComponentFactory.getAutoDateField("dateofbirth", false);
		
		cbxProvince = new EntityRefComboBox<>(I18N.message("placeofbirth"));
		cbxProvince.setRestrictions(new BaseRestrictions<>(Province.class));
		cbxProvince.setImmediate(true);
		cbxProvince.setWidth(150, Unit.PIXELS);
		
		cbxGender = new ERefDataComboBox<>(I18N.message("gender"), EGender.class);
		cbxMaritalStatus = new ERefDataComboBox<>(I18N.message("marital.status"), EMaritalStatus.class);
		cbxNationality = new ERefDataComboBox<>(I18N.message("nationality"), ENationality.class);
		
		dfEnrollmentDate = ComponentFactory.getAutoDateField("enrollment.date", false);
		dfQuitDate = ComponentFactory.getAutoDateField(I18N.message("quit.date"), false);
		cbxJobPosition = new ERefDataComboBox<>(I18N.message("position"), EJobPosition.class);
		cbxTypeContact = new ERefDataComboBox<>(I18N.message("type.contact"), ETypeContact.class);
		txtEmail = ComponentFactory.getTextField("email", false, 60, 200);
		txtTel = ComponentFactory.getTextField("tel", false, 60, 200);
		txtComment = ComponentFactory.getTextArea("comment", false, 200, 100);
					       
        if (dealerEmployeeId != null) {
			dealerEmployee = entityService.getById(DealerEmployee.class, dealerEmployeeId);
			txtReference.setValue(dealerEmployee.getReference() != null ? dealerEmployee.getReference() : "");
			txtFistNameEn.setValue(dealerEmployee.getFirstNameEn() != null ? dealerEmployee.getFirstNameEn() : "");
			txtLastNameEn.setValue(dealerEmployee.getLastNameEn() != null ? dealerEmployee.getLastNameEn() : "");
			txtNickName.setValue(dealerEmployee.getNickName() != null ? dealerEmployee.getNickName() : "");
			cbxTypeIdNumber.setSelectedEntity(dealerEmployee.getTypeIdNumber());
			txtIdNumber.setValue(dealerEmployee.getIdNumber() != null ? dealerEmployee.getIdNumber() : "");
			dfissuingIdNumberDate.setValue(dealerEmployee.getIssuingIdNumberDate());
			dfExpiringIdNumberDate.setValue(dealerEmployee.getExpiringIdNumberDate());
			cbxCivility.setSelectedEntity(dealerEmployee.getCivility());
			dfBirthdate.setValue(dealerEmployee.getBirthDate());
			txtComment.setValue(dealerEmployee.getComment() != null ? dealerEmployee.getComment() : "");
			cbxProvince.setSelectedEntity(dealerEmployee.getPlaceOfBirth());
			cbxGender.setSelectedEntity(dealerEmployee.getGender());
			cbxMaritalStatus.setSelectedEntity(dealerEmployee.getMaritalStatus());
			cbxNationality.setSelectedEntity(dealerEmployee.getNationality());
			dfEnrollmentDate.setValue(dealerEmployee.getEnrollmentDate());
			dfQuitDate.setValue(dealerEmployee.getQuitDate());
			cbxJobPosition.setSelectedEntity(dealerEmployee.getJobPosition());
			cbxTypeContact.setSelectedEntity(dealerEmployee.getTypeContact());
			txtEmail.setValue(dealerEmployee.getEmailPro() != null ? dealerEmployee.getEmailPro() : "");
			txtTel.setValue(dealerEmployee.getTelPro() != null ? dealerEmployee.getTelPro() : "");
		}
        FormLayout formLayoutLeft = new FormLayout();
		formLayoutLeft.setStyleName("myform-align-left");
        formLayoutLeft.setMargin(true);
        formLayoutLeft.setSpacing(true);
   
        formLayoutLeft.addComponent(txtReference);
        formLayoutLeft.addComponent(txtFistNameEn);
        formLayoutLeft.addComponent(txtLastNameEn);
        formLayoutLeft.addComponent(txtNickName);
        formLayoutLeft.addComponent(cbxTypeIdNumber);
        formLayoutLeft.addComponent(txtIdNumber);
        formLayoutLeft.addComponent(dfissuingIdNumberDate);
        formLayoutLeft.addComponent(dfExpiringIdNumberDate);
        formLayoutLeft.addComponent(cbxCivility);
        formLayoutLeft.addComponent(dfBirthdate);
        formLayoutLeft.addComponent(txtComment);
    
        FormLayout formLayoutRight = new FormLayout();
        formLayoutRight.setStyleName("myform-align-left");
        formLayoutRight.setMargin(true);
        formLayoutRight.setSpacing(true);
        
        formLayoutRight.addComponent(cbxProvince);
        formLayoutRight.addComponent(cbxGender);
        formLayoutRight.addComponent(cbxMaritalStatus);
        formLayoutRight.addComponent(cbxNationality);
        formLayoutRight.addComponent(dfEnrollmentDate);
        formLayoutRight.addComponent(dfQuitDate);
        formLayoutRight.addComponent(cbxJobPosition);
        formLayoutRight.addComponent(cbxTypeContact);
        formLayoutRight.addComponent(txtEmail);
        formLayoutRight.addComponent(txtTel);
        
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(formLayoutLeft);
        horizontalLayout.addComponent(formLayoutRight);
        
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			private static final long serialVersionUID = -4024064977917270885L;

			public void buttonClick(ClickEvent event) {
				if (validate()) {
					if (dealerEmployeeId == null) {
						dealerEmployee = new DealerEmployee();
						dealerEmployee.setCompany(dealer);
						dealerEmployee.setReference(txtReference.getValue());
						dealerEmployee.setFirstNameEn(txtFistNameEn.getValue());
						dealerEmployee.setLastNameEn(txtLastNameEn.getValue());
						dealerEmployee.setNickName(txtNickName.getValue());
						dealerEmployee.setTypeIdNumber(cbxTypeIdNumber.getSelectedEntity());
						dealerEmployee.setIdNumber(txtIdNumber.getValue());
						dealerEmployee.setIssuingIdNumberDate(dfissuingIdNumberDate.getValue());
						dealerEmployee.setExpiringIdNumberDate(dfExpiringIdNumberDate.getValue());
						dealerEmployee.setCivility(cbxCivility.getSelectedEntity());
						dealerEmployee.setBirthDate(dfBirthdate.getValue());
						dealerEmployee.setComment(txtComment.getValue());
						dealerEmployee.setPlaceOfBirth(cbxProvince.getSelectedEntity());
						dealerEmployee.setGender(cbxGender.getSelectedEntity());
						dealerEmployee.setMaritalStatus(cbxMaritalStatus.getSelectedEntity());
						dealerEmployee.setNationality(cbxNationality.getSelectedEntity());
						dealerEmployee.setEnrollmentDate(dfEnrollmentDate.getValue());
						dealerEmployee.setQuitDate(dfQuitDate.getValue());
						dealerEmployee.setJobPosition(cbxJobPosition.getSelectedEntity());
						dealerEmployee.setTypeContact(cbxTypeContact.getSelectedEntity());
						dealerEmployee.setEmailPro(txtEmail.getValue());
						dealerEmployee.setTelPro(txtTel.getValue());
						entityService.saveOrUpdate(dealerEmployee);
						winDealerEmployee.close();
						assignValues(dealerId);
					}	
				}
            }
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			private static final long serialVersionUID = 3975121141565713259L;

			public void buttonClick(ClickEvent event) {
            	winDealerEmployee.close();
            }
        });
		btnCancel.setIcon(FontAwesome.TIMES);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
        contentLayout.addComponent(horizontalLayout);
        
        winDealerEmployee.setContent(contentLayout);
        UI.getCurrent().addWindow(winDealerEmployee);
	
	}
	
	/**
	 * 
	 * @param dealerId
	 */
	public void assignValues(Long dealerId) {
		if (dealerId != null) {
			this.dealerId = dealerId;
			this.dealer =  entityService.getById(Dealer.class, new Long(dealerId));
			if (dealer.getDealerEmployees() != null && !dealer.getDealerEmployees().isEmpty()) {
				pagedTable.setContainerDataSource(getIndexedContainer(dealer.getDealerEmployees()));
			}
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME_EN, I18N.message("firstname.en"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<DealerEmployee> dealerEmployees) {
		IndexedContainer indexedContainer = new IndexedContainer();		
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (DealerEmployee dealerEmployee : dealerEmployees) {
			Item item = indexedContainer.addItem(dealerEmployee.getId());
			item.getItemProperty(ID).setValue(dealerEmployee.getId());
			item.getItemProperty(REFERENCE).setValue(dealerEmployee.getReference());
			item.getItemProperty(FIRST_NAME_EN).setValue(dealerEmployee.getFirstNameEn());
			item.getItemProperty(LAST_NAME_EN).setValue(dealerEmployee.getLastNameEn());
		}
		
		return indexedContainer;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean validate() {
		messagePanel.removeAllComponents();
		List<String> errors = new ArrayList<>();
		errors.clear();
		/*if (cbxAssetModel.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("asset.model") }));
		}*/
		if (!errors.isEmpty()) {
			for (String error : errors) {
				Label messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		return errors.isEmpty();
	}
	
	/**
	 * delete dealerEmployee
	 */
	private void delete() {
		if (selectedItem == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			final Long id = (Long) selectedItem.getItemProperty("id").getValue();
			ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
					new String[] {id.toString()}),
					new ConfirmDialog.Listener() {

				/** */
				private static final long serialVersionUID = -1278300263633872114L;

				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						entityService.delete(DealerEmployee.class, id);
					    dialog.close();
					    assignValues(dealer.getId());
					}
				}
			});
			
		}
	}
	
}
