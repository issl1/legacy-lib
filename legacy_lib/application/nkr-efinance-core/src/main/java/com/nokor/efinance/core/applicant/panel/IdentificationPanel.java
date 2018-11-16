package com.nokor.efinance.core.applicant.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.applicant.service.IndividualService;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.panel.QuotationsPanel;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Customer identity panel
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IdentificationPanel extends AbstractTabPanel implements FMEntityField {
	
	private static final long serialVersionUID = -3020972492621000892L;
	
	@Autowired
	protected ApplicantService applicantService;
	
	@Autowired
	protected IndividualService individualService;
		
	private QuotationsPanel quotationsPanel;
	
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private AutoDateField dfDateOfBirth;
	private Panel contentPanel;
	private NavigationPanel navigationPanel;
	private NativeButton btnNewApplicant;
	private NativeButton btnIdentify;
	private VerticalLayout verticalLayout;
	private FormLayout formLayout;
	private SimplePagedTable<Applicant> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private Item selectedItem = null;
	
	private Date startCreationDate;
		
	public IdentificationPanel() {
		super();
		setMargin(true);
		setSizeFull();
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		contentPanel = new Panel(I18N.message("identification.process"));
		contentPanel.setSizeFull();
		navigationPanel = new NavigationPanel();
		btnIdentify = new NativeButton(I18N.message("identify"));
		btnIdentify.setIcon(new ThemeResource("../nkr-default/icons/16/user.png"));
		btnNewApplicant = new NativeButton(I18N.message("new.applicant"));
		btnNewApplicant.setIcon(new ThemeResource("../nkr-default/icons/16/user.png"));
		btnNewApplicant.setVisible(false);
		
		navigationPanel.addButton(btnIdentify);
		navigationPanel.addButton(btnNewApplicant);
		
		txtFirstNameEn = ComponentFactory.getTextField("firstname.en", true, 50, 200);
		txtLastNameEn = ComponentFactory.getTextField("lastname.en", true, 50, 200);		
		dfDateOfBirth = ComponentFactory.getAutoDateField("dateofbirth", true);
		dfDateOfBirth.setWidth(95,Unit.PIXELS);
		
		formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.addComponent(txtLastNameEn);
		formLayout.addComponent(txtFirstNameEn);
		formLayout.addComponent(dfDateOfBirth);
		
		verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
		verticalLayout.addComponent(navigationPanel);
		contentPanel.setContent(verticalLayout);
		addComponent(contentPanel);
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Applicant>(this.columnDefinitions);
		btnIdentify.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -9047707680639618553L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (isValid()) {
					if (individualService.isIndividualOver18Years(dfDateOfBirth.getValue())) {
						List<Applicant> applicants = applicantService.identify(txtLastNameEn.getValue(), txtFirstNameEn.getValue(), dfDateOfBirth.getValue());
						Quotation quotation = new Quotation();
						quotation.setWkfStatus(QuotationWkfStatus.QUO);
						quotation.setStartCreationDate(startCreationDate);
						
						setSecUserDetailDealer(quotation);
						
						if (applicants == null || applicants.isEmpty()) {
							quotation.setMainApplicant(getApplicant());
							quotationsPanel.createQuotation(quotation);
						} else {
							verticalLayout.setMargin(true);
							verticalLayout.setSpacing(true);
							verticalLayout.removeAllComponents();
							btnIdentify.setVisible(false);
							btnNewApplicant.setVisible(true);
							verticalLayout.addComponent(navigationPanel);
							VerticalLayout informationLayout = new VerticalLayout();
							informationLayout.setMargin(true);
							informationLayout.addComponent(new Label(I18N.message("identification.match", txtLastNameEn.getValue(), txtFirstNameEn.getValue()), ContentMode.HTML));
							verticalLayout.addComponent(informationLayout);
							pagedTable.setContainerDataSource(getIndexedContainer(applicants));
							verticalLayout.addComponent(pagedTable);
						}
					} else {
						MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
								MessageBox.Icon.INFO, I18N.message("applicant.not.over.18.years", "" + 18),
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.setWidth("300px");
						mb.setHeight("150px");
						mb.show();
					}
				}
			}
		});		

		btnNewApplicant.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5334205712092090703L;
			@Override
			public void buttonClick(ClickEvent event) {
				Quotation quotation = new Quotation();
				quotation.setWkfStatus(QuotationWkfStatus.QUO);
				quotation.setStartCreationDate(startCreationDate);
				quotation.setMainApplicant(getApplicant());
				setSecUserDetailDealer(quotation);
				quotationsPanel.createQuotation(quotation);
			}			
		});
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -4677521976229171029L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					Long appliId = (Long) selectedItem.getItemProperty(ID).getValue();
					if (!applicantService.isMaxQuotationAuthorisedReach(appliId)) {
						Quotation quotation = new Quotation();
						quotation.setWkfStatus(QuotationWkfStatus.QUO);
						quotation.setStartCreationDate(startCreationDate);
						quotation.setMainApplicant(getApplicant(appliId));
						setSecUserDetailDealer(quotation);
						quotationsPanel.createQuotation(quotation); 
					} else {
						MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
								MessageBox.Icon.INFO, I18N.message("maximum.applicant.reached"),
								new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.setWidth("300px");
						mb.setHeight("150px");
						mb.show();
					}
				}
			}
		});
		return contentPanel;
	}
	
	/**
	 * @return
	 */
	private Applicant getApplicant() {
		Applicant mainApplicant = new Applicant();
		mainApplicant.getIndividual().setFirstNameEn(txtFirstNameEn.getValue());
		mainApplicant.getIndividual().setLastNameEn(txtLastNameEn.getValue());
		mainApplicant.getIndividual().setBirthDate(dfDateOfBirth.getValue());
		return mainApplicant;
	}	
	
	/**
	 * @return
	 */
	private Applicant getApplicant(Long appliId) {
		return applicantService.getById(Applicant.class, appliId);
	}
	
	public boolean isValid() {
		boolean valid = true;
		try {
			txtFirstNameEn.validate();
			txtLastNameEn.validate();
			dfDateOfBirth.validate();
		} catch (EmptyValueException e) {
			valid = false;
		}
		return valid;
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		verticalLayout.removeAllComponents();
		verticalLayout.addComponent(navigationPanel);
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
		dfDateOfBirth.setValue(null);
		verticalLayout.addComponent(formLayout);
		btnIdentify.setVisible(true);
		btnNewApplicant.setVisible(false);
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<Applicant> applicants) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		
		for (Applicant applicant : applicants) {
			Item item = indexedContainer.addItem(applicant.getId());
			item.getItemProperty(ID).setValue(applicant.getId());
			item.getItemProperty(FIRST_NAME).setValue(applicant.getIndividual().getFirstNameEn());
			item.getItemProperty(LAST_NAME).setValue(applicant.getIndividual().getLastNameEn());
			item.getItemProperty(BIRTH_DATE).setValue(applicant.getIndividual().getBirthDate());
		}
		return indexedContainer;
	}
	
	
	/**
	 * @param startCreationDate the startCreationDate to set
	 */
	public void setStartCreationDate(Date startCreationDate) {
		this.startCreationDate = startCreationDate;
	}

	/**
	 * @param mainPanel
	 */
	public void setQuotationsPanel(QuotationsPanel quotationsPanel) {
		this.quotationsPanel = quotationsPanel;
	}
	
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME, I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(BIRTH_DATE, I18N.message("dateofbirth"), Date.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param quotation
	 */
	private void setSecUserDetailDealer(Quotation quotation) {
		SecUserDetail secUserDetail = getSecUserDetail(); 
		if (ProfileUtil.isPOS() && secUserDetail != null && secUserDetail.getDealer() != null) {
			quotation.setDealer(secUserDetail.getDealer());
		}
	}
	
	/**
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EntityService entityService = SpringUtils.getBean(EntityService.class);
		return entityService.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}

	
}
