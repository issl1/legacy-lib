package com.nokor.efinance.core.applicant.panel.guarantor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.service.ApplicantService;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
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
import com.vaadin.ui.VerticalLayout;

/**
 * Guarantor identity panel
 * @author ly.youhort
 */
public class IdentificationPanel extends AbstractTabPanel implements FMEntityField {

	private static final long serialVersionUID = -3020972492621000892L;
	
	protected ApplicantService applicantService = (ApplicantService) SecApplicationContextHolder.getContext().getBean("applicantService");
	
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private AutoDateField dfDateOfBirth;
	private NavigationPanel navigationPanel;
	private NativeButton btnNewApplicant;
	private NativeButton btnIdentify;
	private VerticalLayout verticalLayout;
	private FormLayout formLayout;
	private SimplePagedTable<Applicant> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private Item selectedItem = null;
		
	/**
	 * @param quotationFormPanel
	 */
	public IdentificationPanel(/*QuotationFormPanel quotationFormPanel*/) {
		super();
		setMargin(true);
		setSizeFull();
	}
	
	@Override
	protected com.vaadin.ui.Component createForm() {
		Panel contentPanel = new Panel(I18N.message("identification.process"));
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
		
		formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.addComponent(txtLastNameEn);
		formLayout.addComponent(txtFirstNameEn);
		formLayout.addComponent(dfDateOfBirth);
		
		verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(formLayout);
		
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Applicant>(this.columnDefinitions);
		btnIdentify.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -9047707680639618553L;
			@Override
			public void buttonClick(ClickEvent event) {
				if (isValid()) {
					List<Applicant> applicants = applicantService.identify(txtLastNameEn.getValue(), txtFirstNameEn.getValue(), dfDateOfBirth.getValue());
					
					if (applicants == null || applicants.isEmpty()) {
						// quotationFormPanel.setGuarantor(getApplicant());
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
				}
			}
		});		

		btnNewApplicant.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5334205712092090703L;
			@Override
			public void buttonClick(ClickEvent event) {
				// quotationFormPanel.setGuarantor(getApplicant());
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
					// quotationFormPanel.setGuarantor(getApplicant(appliId));
				}
			}
		});
		
		contentPanel.setContent(verticalLayout);
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
	private boolean isValid() {
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
	
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIRST_NAME, I18N.message("firstname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(LAST_NAME, I18N.message("lastname.en"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(BIRTH_DATE, I18N.message("dateofbirth"), Date.class, Align.LEFT, 100));
		return columnDefinitions;
	}
}
