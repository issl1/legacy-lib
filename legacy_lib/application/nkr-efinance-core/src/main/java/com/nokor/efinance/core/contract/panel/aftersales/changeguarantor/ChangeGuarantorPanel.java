package com.nokor.efinance.core.contract.panel.aftersales.changeguarantor;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.panel.guarantor.GuarantorPanel;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.quotation.model.QuotationDocument;
import com.nokor.efinance.core.quotation.panel.QuotationsPanel;
import com.nokor.efinance.tools.ui.TooltipUtil;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ChangeGuarantorPanel.NAME)
public class ChangeGuarantorPanel extends VerticalLayout implements View {

	private static final long serialVersionUID = -187744090078204360L;

	public static final String NAME = "change.guarantor";
	
	private static final ThemeResource errorIcon = new ThemeResource("../nkr-default/icons/16/close.png");
	
	@Autowired
	private QuotationService quotationService;
	
	private Quotation quotation;
	private Applicant guarantor;
	
	private TabSheet accordionPanel;
	private GuarantorPanel guarantorPanel;
	private DocumentsGuarantorPanel documentsGuarantorPanel;
	private NavigationPanel navigationPanel;
	
	private Button btnBackQuotation;
	private Button btnSave;
	
	public ChangeGuarantorPanel() {
		setSpacing(true);
	}
	
	@PostConstruct
	public void PostConstruct() {
		
		accordionPanel = new TabSheet();
		navigationPanel = new NavigationPanel();
		guarantorPanel = new GuarantorPanel();
		documentsGuarantorPanel = new DocumentsGuarantorPanel();
		accordionPanel.addTab(guarantorPanel, I18N.message("guarantor"));
		accordionPanel.addTab(documentsGuarantorPanel, I18N.message("documents"));
		
		btnBackQuotation = new NativeButton(I18N.message("back.quotation"));
		btnBackQuotation.setIcon(new ThemeResource("../nkr-default/icons/16/previous.png"));
		navigationPanel.addButton(btnBackQuotation);
		
		btnBackQuotation.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6661162238224070578L;

			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().setUriFragment("!" + QuotationsPanel.NAME + "/" + quotation.getId());
			}
		});
				
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		navigationPanel.addButton(btnSave);
		
		btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6661162238224070578L;
			@Override
			public void buttonClick(ClickEvent event) {
				saveGuarantor();
			}
		});
		
		addComponent(navigationPanel);
		addComponent(accordionPanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String quotaId = event.getParameters();
		assignValues(new Long(quotaId));
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long quotaId) {
		quotation = quotationService.getById(Quotation.class, quotaId);
		Applicant mainApplicant = quotation.getApplicant();
		// DO NOT delete these line => LAZY initialization
		mainApplicant.getIndividual().getMainAddress().getCountry();
		guarantorPanel.setMainApplicant(mainApplicant);
		
		QuotationApplicant oldQuotationGuarantor = quotation.getQuotationApplicant(EApplicantType.O);
		if (oldQuotationGuarantor != null) {
			QuotationApplicant quotationGuarantor = quotation.getQuotationApplicant(EApplicantType.G);
			guarantor = quotationGuarantor.getApplicant();
			guarantorPanel.assignValues(quotationGuarantor);
			documentsGuarantorPanel.assignValues(quotation);
		} else {
			guarantor = new Applicant();
			List<QuotationDocument> documents = quotation.getQuotationDocuments();
			if (documents != null) {
				for (QuotationDocument quotationDocument : documents) {
					if (quotationDocument.getDocument().getApplicantType().equals(EApplicantType.G)) {
						quotationDocument.setCrudAction(CrudAction.DELETE);
					}
				}
			}
		}
		
		guarantorPanel.setEnabledGuarantor(true);
		displayErrorsInformation();
	}
	
	/**
	 * Display icon error on each tabs
	 */
	private void displayErrorsInformation() {
		Tab guarantorTab = accordionPanel.getTab(guarantorPanel);
		List<String> errors = guarantorPanel.fullValidate();
		guarantorTab.setIcon(!errors.isEmpty() ? errorIcon : null);
		guarantorTab.setDescription(!errors.isEmpty() ? TooltipUtil.getToolTip(errors) : null);
		Tab documentsTab = accordionPanel.getTab(documentsGuarantorPanel);
		errors = documentsGuarantorPanel.fullValidate();
		documentsTab.setIcon(!errors.isEmpty() ? errorIcon : null);
		documentsTab.setDescription(!errors.isEmpty() ? TooltipUtil.getToolTip(errors) : null);
	}
	
	private void saveGuarantor() {
		guarantor = guarantorPanel.getApplicant(guarantor);
		QuotationApplicant oldQuotationGuarantor = quotation.getQuotationApplicant(EApplicantType.O);
		if (oldQuotationGuarantor == null) {
			QuotationApplicant quotationGuarantor = quotation.getQuotationApplicant(EApplicantType.G);
			quotationGuarantor.setApplicantType(EApplicantType.O);
			quotation.setGuarantor(guarantor);
			
		}
		guarantorPanel.getQuotationApplicant(quotation.getQuotationApplicant(EApplicantType.G));
		documentsGuarantorPanel.getDocuments(quotation);
		quotationService.saveChangeGuarantor(quotation);
		quotationService.refresh(quotation);
		assignValues(quotation.getId());
	}
}
