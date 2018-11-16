package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.letters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.address.panel.AddressComboBox;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.collection.model.MCollectionLetter;
import com.nokor.efinance.core.common.reference.model.ELetterTemplate;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractApplicant;
import com.nokor.efinance.core.contract.model.Letter;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.applicant.AddressUtils;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColPhoneLettersFormPanel extends AbstractControlPanel implements MCollectionLetter, ClickListener, FinServicesHelper, ItemClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1685150775035677250L;
	
	private ERefDataComboBox<EApplicantType> cbxSendTo;
	private AddressComboBox cbxAddress;
	private EntityComboBox<ELetterTemplate> cbxLetters;
	private AutoDateField dfDate;
	private CheckBox cbNow;
	private Button btnSave;
	
	private SimplePagedTable<Entity> pageTable;
	private List<ColumnDefinition> columnDefinitions;
	private Contract contract;
	
	private Letter letter;
	
	private Item selectedItem;
	
	/**
	 * 
	 */
	public ColPhoneLettersFormPanel() {
		setSpacing(true);
		setMargin(true);
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		
		cbxSendTo = new ERefDataComboBox<>(EApplicantType.values());
		cbxSendTo.setImmediate(true);
		cbxSendTo.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -8889532066809159058L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxSendTo.getSelectedEntity() != null) {
					List<Address> addresses = getAddresses(cbxSendTo.getSelectedEntity());
					cbxAddress.setEntities(addresses);
				} else {
					cbxAddress.clear();
				}
			}
		});
		cbxSendTo.setWidth(100, Unit.PIXELS);
		
		cbxAddress = new AddressComboBox();
		cbxAddress.setImmediate(true);
		cbxAddress.setWidth(230, Unit.PIXELS);
		
		cbxLetters = new EntityComboBox<>(ELetterTemplate.class, "descEn");
		cbxLetters.setImmediate(true);
		cbxLetters.renderer();
		
		dfDate = ComponentFactory.getAutoDateField();
		cbNow = new CheckBox(I18N.message("now"));
		cbNow.addValueChangeListener(new ValueChangeListener() {
		
			private static final long serialVersionUID = -8889532066809159058L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbNow.getValue()) {
					dfDate.setEnabled(false);
					dfDate.setValue(DateUtils.today());
				} else {
					dfDate.setEnabled(true);
				}
				
			}
		});
		
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
		
		pageTable = new SimplePagedTable<>(getColumnDefinitions());
		pageTable.addItemClickListener(this);
		
		Label lblSendTo = ComponentLayoutFactory.getLabelCaptionRequired("send.to");
		Label lblDate = ComponentLayoutFactory.getLabelCaption("date");
		
		GridLayout gridLayout = new GridLayout(7, 1);
		gridLayout.setSpacing(true);
		
		gridLayout.addComponent(lblSendTo);
		gridLayout.addComponent(cbxSendTo);
		gridLayout.addComponent(cbxAddress);
		gridLayout.addComponent(cbxLetters);
		gridLayout.addComponent(lblDate);
		gridLayout.addComponent(dfDate);
		gridLayout.addComponent(cbNow);
		
		gridLayout.setComponentAlignment(cbNow, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.addComponent(btnSave);

		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		verLayout.addComponent(gridLayout);
		verLayout.addComponent(horLayout);
		verLayout.setComponentAlignment(horLayout, Alignment.MIDDLE_RIGHT);
		
		Panel letterPanel = new Panel(verLayout);
		
		addComponent(letterPanel);
		addComponent(pageTable);
		addComponent(pageTable.createControls());
		
		pageTable.setPageLength(5);
	}
	
	private List<ColumnDefinition> getColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("letter.id"), Long.class, Align.LEFT, 50, true));
		columnDefinitions.add(new ColumnDefinition(NAME, I18N.message("name"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(SEND_TO, I18N.message("send.to"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(ADDRESS, I18N.message("address"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(SEND_ON, I18N.message("send.on"), String.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(DETAILS, I18N.message("details"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition(SEND_DATE, I18N.message("send.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(STATUS_DATE, I18N.message("status.date"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(FMEntityField.WKF_STATUS, I18N.message("status"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(COMMENT, I18N.message("comment"), String.class, Align.LEFT, 130));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param letters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<Letter> letters) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		
		for (Letter letter : letters) {
			Item item = indexedContainer.addItem(letter.getId());
			
			/*WkfHistoryItemRestriction<WkfHistoryItem> restrictions = new WkfHistoryItemRestriction<>(WkfHistoryItem.class);
			restrictions.setEntity(letter.getWkfFlow().getEntity());
			restrictions.setEntityId(letter.getId());
			restrictions.addOrder(Order.desc(ID));
			List<WkfHistoryItem> histories = ENTITY_SRV.list(restrictions);
			String statusDate = StringUtils.EMPTY;
			if (histories != null && !histories.isEmpty()) {
				statusDate = DateUtils.getDateLabel(histories.get(0).getChangeDate(), DateUtils.FORMAT_YYYYMMDD_HHMMSS_SLASH);
			}*/
			
			item.getItemProperty(ID).setValue(letter.getId());
			item.getItemProperty(SEND_DATE).setValue(letter.getSendDate());
//			item.getItemProperty(STATUS_DATE).setValue(statusDate);
//			item.getItemProperty(FMEntityField.WKF_STATUS).setValue(letter.getWkfStatus().getDescLocale());
			item.getItemProperty(STATUS_DATE).setValue(StringUtils.EMPTY);
			item.getItemProperty(FMEntityField.WKF_STATUS).setValue(StringUtils.EMPTY);
			item.getItemProperty(COMMENT).setValue(letter.getMessage());
			if (letter.getLetterTemplate() != null) {
				item.getItemProperty(NAME).setValue(letter.getLetterTemplate().getCode());
				item.getItemProperty(DESC).setValue(letter.getLetterTemplate().getDescLocale());
			}
			item.getItemProperty(SEND_TO).setValue(letter.getSendTo() != null ? letter.getSendTo().getDescLocale() : "");
			if (letter.getSendAddress() != null) {
				Address address = letter.getSendAddress();
				String result = ADDRESS_SRV.getDetailAddress(address);
				item.getItemProperty(ADDRESS).setValue(address.getType() != null ? address.getType().getDescLocale() : "");
				item.getItemProperty(SEND_ON).setValue(address.getHouseNo());
				item.getItemProperty(DETAILS).setValue(result);
			}
		}
		return indexedContainer;
	}
	
	/**
	 * AssignValue
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		reset();
		if (contract != null) {
			this.contract = contract;
			List<Letter> letters = ENTITY_SRV.list(getRestrictions());
			pageTable.setContainerDataSource(getIndexedContainer(letters));
		}
	}
	
	/**
	 * get list of letter sent
	 * @return
	 */
	private BaseRestrictions<Letter> getRestrictions() {
		BaseRestrictions<Letter> restrictions = new BaseRestrictions<>(Letter.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addOrder(Order.desc(Letter.ID));
		return restrictions;
	}
	
	/**
	 * get list address by cbxSendTo (ApplicantType)
	 * @param applicantType
	 * @return
	 */
	private List<Address> getAddresses(EApplicantType applicantType) {
		List<Address> addresses = new ArrayList<>();
		if (applicantType.equals(EApplicantType.C)) {
			addresses.addAll(getAddresses(contract.getApplicant().getIndividual()));
		} else {
			if (applicantType.equals(EApplicantType.G)) {
				List<ContractApplicant> contractApplicants = contract.getContractApplicants();
				if (contractApplicants != null && !contractApplicants.isEmpty()) {
					for (ContractApplicant contractApplicant : contractApplicants) {
						if (contractApplicant.getApplicantType().equals(EApplicantType.G)) {
							addresses.addAll(getAddresses(contractApplicant.getApplicant().getIndividual()));
						}
					}
				}
			}
		}
		return addresses;
	}
	
	private List<Address> getAddresses(Individual individual) {
		List<Address> addresses = new ArrayList<>();
		List<IndividualAddress> individualAddresses = individual.getIndividualAddresses();
		if (individualAddresses!= null && !individualAddresses.isEmpty()) {
			for (IndividualAddress individualAddress : individualAddresses) {
				addresses.add(individualAddress.getAddress());
			}
		}
		return addresses;
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		super.reset();
		this.letter = Letter.createInstance();
		cbxSendTo.setSelectedEntity(null);
		cbxAddress.setSelectedEntity(null);
		cbxLetters.setSelectedEntity(null);
		dfDate.setValue(null);
		cbNow.setValue(false);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validate() {
		errors = new ArrayList<>();
		
		if (cbxSendTo.getSelectedEntity() == null) {
			ComponentLayoutFactory.getNotification(Type.ERROR_MESSAGE).setDescription(I18N.message("field.required.1", new String[]{ I18N.message("send.to") }));
			return false;
		}
		
		if (cbxAddress.getSelectedEntity() == null) {
			ComponentLayoutFactory.getNotification(Type.ERROR_MESSAGE).setDescription(I18N.message("field.required.1", new String[]{ I18N.message("address") }));
			return false;
		}
		
		return errors.isEmpty();
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			this.letter = ENTITY_SRV.getById(Letter.class, getItemSelectedId());
			cbxSendTo.setSelectedEntity(this.letter.getSendTo());
			
			List<Address> addresses = new ArrayList<Address>();
			Address addr = null;
			if (this.letter.getSendAddress() != null) {
				addr = ADDRESS_SRV.getById(Address.class, this.letter.getSendAddress().getId());
			}
			if (addr != null) {
				addresses.add(addr);
				cbxAddress.setEntities(addresses);
				cbxAddress.setSelectedEntity(addresses.get(0));
			}
			cbxLetters.setSelectedEntity(this.letter.getLetterTemplate());
			dfDate.setValue(this.letter.getSendDate());
		}
	}
		
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ID).getValue());
		}
		return null;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {	
			if (validate()) {
				try {
					SecUser user = UserSessionManager.getCurrentUser();
					if (this.letter == null) {
						this.letter = Letter.createInstance();
					}
					letter.setContract(contract);
					letter.setUserLogin(user.getLogin());
					
					Address address = new Address();
					address = AddressUtils.copy(cbxAddress.getSelectedEntity(), address);
					ENTITY_SRV.saveOrUpdate(address);
					
					letter.setSendTo(cbxSendTo.getSelectedEntity());
					letter.setSendAddress(address);
					letter.setLetterTemplate(cbxLetters.getSelectedEntity());
					letter.setSendDate(dfDate.getValue());
					ENTITY_SRV.saveOrUpdate(letter);
					ComponentLayoutFactory.displaySuccessfullyMsg();
					assignValue(contract);
				} catch (Exception e) {
					logger.error(e.getMessage());
					Notification.show("", e.getMessage(), Type.ERROR_MESSAGE);
				}
			}
		}
	}

}
