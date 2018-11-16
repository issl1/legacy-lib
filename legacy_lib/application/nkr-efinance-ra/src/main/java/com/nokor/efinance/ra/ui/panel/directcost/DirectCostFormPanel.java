package com.nokor.efinance.ra.ui.panel.directcost;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DirectCostFormPanel extends AbstractFormPanel {

	private static final long serialVersionUID = -8371274781712659783L;
	
	private CheckBox cbActive;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
	private ERefDataComboBox<EServiceType> cbxServiceType;
	private ERefDataComboBox<EFrequency> cbxFrequency;
	private ERefDataComboBox<ETreasuryType> cbxTreasuryType;
	private ERefDataComboBox<ECalculMethod> cbxCalculMethod;
	
	private TextField txtTermInMonths;
	private CheckBox cbContractDuration;
	private CheckBox cbPaidOneShot;
	private AutoDateField dfDueDate;
	
	private TextField txtFixedAmount;
	
	private TextField txtPercentageOfPremiumFirstYear;
	private TextField txtPercentageOfPremiumSecondYear;
	private TextField txtPercentageOfPremiumThirdYear;
	private TextField txtPercentageOfPremiumForthYear;
	private TextField txtPercentageOfPremiumFifthYear;
	
	private TextField txtPercentageOfAssetFirstYear;
	private TextField txtPercentageOfAssetSecondYear;
	private TextField txtPercentageOfAssetThirdYear;
	private TextField txtPercentageOfAssetForthYear;
	private TextField txtPercentageOfAssetFifthYear;
	
	private TextField txtFormula;
	
	private GridLayout basedOnAssetGrid;
	private HorizontalLayout formulaGrid;
	
	private FinService service;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("direct.cost"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		service.setCode(txtCode.getValue());
		service.setDesc(txtDesc.getValue());
		service.setDescEn(txtDescEn.getValue());
		service.setServiceType(cbxServiceType.getSelectedEntity());
		service.setCalculMethod(cbxCalculMethod.getSelectedEntity());
		service.setPercentage(0d);
		service.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		service.setTreasuryType(cbxTreasuryType.getSelectedEntity());
		service.setFrequency(cbxFrequency.getSelectedEntity());
		service.setDueDate(dfDueDate.getValue());
		service.setContractDuration(cbContractDuration.getValue());
		service.setPaidOneShot(cbPaidOneShot.getValue());
		service.setTermInMonths(getInteger(txtTermInMonths));
		service.setTePrice(getDouble(txtFixedAmount, 0d));
		service.setVatPrice(0d);
		service.setTiPrice(service.getTePrice());
		service.setPercentageOfAssetFirstYear(getDouble(txtPercentageOfAssetFirstYear));
		service.setPercentageOfAssetSecondYear(getDouble(txtPercentageOfAssetSecondYear));
		service.setPercentageOfAssetThirdYear(getDouble(txtPercentageOfAssetThirdYear));
		service.setPercentageOfAssetForthYear(getDouble(txtPercentageOfAssetForthYear));
		service.setPercentageOfAssetFifthYear(getDouble(txtPercentageOfAssetFifthYear));
		service.setPercentageOfPremiumFirstYear(getDouble(txtPercentageOfPremiumFirstYear));
		service.setPercentageOfPremiumSecondYear(getDouble(txtPercentageOfPremiumSecondYear));
		service.setPercentageOfPremiumThirdYear(getDouble(txtPercentageOfPremiumThirdYear));
		service.setPercentageOfPremiumForthYear(getDouble(txtPercentageOfPremiumForthYear));
		service.setPercentageOfPremiumFifthYear(getDouble(txtPercentageOfPremiumFifthYear));
		service.setFormula(txtFormula.getValue());
				
		return service;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {		
		final FormLayout formPanel = new FormLayout();
		
		txtCode = ComponentFactory.getTextField("code", true, 10, 100);      
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);
		
		txtTermInMonths = ComponentFactory.getTextField(I18N.message("term.month"), false, 3, 100);
		
		cbxServiceType = new ERefDataComboBox<>(EServiceType.listDirectCosts());
		cbxServiceType.setCaption(I18N.message("type"));
		cbxServiceType.setImmediate(true);
		cbxServiceType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4756412359161227195L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxServiceType.getValue() != null) {
					if (cbxServiceType.getSelectedEntity().equals(EServiceType.INEX)) {
					} else if(cbxServiceType.getSelectedEntity().equals(EServiceType.COMM)) {
					}
				}	
			}
		});
		
		cbxFrequency = new ERefDataComboBox<EFrequency>(I18N.message("frequency"), EFrequency.class);
		
        cbxTreasuryType = new ERefDataComboBox<ETreasuryType>(I18N.message("payer"), ETreasuryType.class);
        cbxTreasuryType.setRequired(true);
        
        cbxCalculMethod = new ERefDataComboBox<ECalculMethod>(I18N.message("calculate.method"), ECalculMethod.class);
    	cbxCalculMethod.setRequired(true);
    	cbxCalculMethod.setSelectedEntity(ECalculMethod.FIX);
		
    	dfDueDate = ComponentFactory.getAutoDateField("due.date", true);
    	dfDueDate.setDateFormat("dd");
    	
    	cbPaidOneShot = new CheckBox(I18N.message("paid.one.short"));
		
		cbContractDuration = new CheckBox(I18N.message("contract.duration"));
		cbContractDuration.setValue(false);
		cbContractDuration.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbContractDuration.getValue()) {
					txtTermInMonths.setEnabled(false);
					txtTermInMonths.setValue("");
				} else {
					txtTermInMonths.setEnabled(true);
				}				
			}
		});
    	
		cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
    
        txtFixedAmount = ComponentFactory.getTextField("fixed.amount", false, 60, 120);
        
        txtPercentageOfAssetFirstYear = ComponentFactory.getTextField(false, 60,200);
		txtPercentageOfAssetSecondYear = ComponentFactory.getTextField(false, 60,200);
		txtPercentageOfAssetThirdYear = ComponentFactory.getTextField(false, 60,200);
		txtPercentageOfAssetForthYear = ComponentFactory.getTextField(false, 60,200);
		txtPercentageOfAssetFifthYear = ComponentFactory.getTextField(false, 60,200);
		
		txtPercentageOfPremiumFirstYear = ComponentFactory.getTextField(false, 60,200);
		txtPercentageOfPremiumSecondYear = ComponentFactory.getTextField(false, 60,200);
		txtPercentageOfPremiumThirdYear = ComponentFactory.getTextField(false, 60,200);
		txtPercentageOfPremiumForthYear = ComponentFactory.getTextField(false, 60,200);
		txtPercentageOfPremiumFifthYear = ComponentFactory.getTextField(false, 60,200);
		
		formulaGrid = new HorizontalLayout();
		formulaGrid.setCaption(I18N.message("formula"));
		txtFormula = ComponentFactory.getTextField(false, 60, 300);
		Label lblFormula = new Label("ap : Asset Price, la : Loan amount");
		formulaGrid.addComponent(txtFormula);
		formulaGrid.addComponent(lblFormula);
		formulaGrid.setVisible(false);
		
		int iCol = 0;
		basedOnAssetGrid = new GridLayout(3, 12);
        basedOnAssetGrid.setSpacing(true);
        basedOnAssetGrid.setVisible(false);
        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("percentage.of.asset.price")), 2, 0);
        
        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("fisrt.year")), iCol++, 1);
        basedOnAssetGrid.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 1);
        basedOnAssetGrid.addComponent(txtPercentageOfAssetFirstYear, iCol++, 1);

        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("second.year")), iCol++, 2);
        basedOnAssetGrid.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 2);
        basedOnAssetGrid.addComponent(txtPercentageOfAssetSecondYear, iCol++, 2);
        
        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("third.year")), iCol++, 3);
        basedOnAssetGrid.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 3);
        basedOnAssetGrid.addComponent(txtPercentageOfAssetThirdYear, iCol++, 3);
        
        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("forth.year")), iCol++, 4);
        basedOnAssetGrid.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 4);
        basedOnAssetGrid.addComponent(txtPercentageOfAssetForthYear, iCol++, 4);
        
        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("fifth.year")), iCol++, 5);
        basedOnAssetGrid.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 5);
        basedOnAssetGrid.addComponent(txtPercentageOfAssetFifthYear, iCol++, 5);
        
        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("premium.in.percentag")), 2, 6);
        
        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("fisrt.year")), iCol++, 7);
        basedOnAssetGrid.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 7);
        basedOnAssetGrid.addComponent(txtPercentageOfPremiumFirstYear, iCol++, 7);

        iCol = 0;
       
        basedOnAssetGrid.addComponent(new Label(I18N.message("second.year")), iCol++, 8);
        basedOnAssetGrid.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 8);
        basedOnAssetGrid.addComponent(txtPercentageOfPremiumSecondYear, iCol++, 8);
        
        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("third.year")), iCol++, 9);
        basedOnAssetGrid.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 9);
        basedOnAssetGrid.addComponent(txtPercentageOfPremiumThirdYear, iCol++, 9);
        
        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("forth.year")), iCol++, 10);
        basedOnAssetGrid.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 10);
        basedOnAssetGrid.addComponent(txtPercentageOfPremiumForthYear, iCol++, 10);
        
        iCol = 0;
        basedOnAssetGrid.addComponent(new Label(I18N.message("fifth.year")), iCol++, 11);
        basedOnAssetGrid.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 11);
        basedOnAssetGrid.addComponent(txtPercentageOfPremiumFifthYear, iCol++, 11);
        
        cbxCalculMethod.addValueChangeListener(new ValueChangeListener() {    	
			private static final long serialVersionUID = 2888159615146594988L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxCalculMethod.getSelectedEntity().equals(ECalculMethod.FIX)) {
					txtFixedAmount.setVisible(true);
					basedOnAssetGrid.setVisible(false);
					formulaGrid.setVisible(false);
				} else if (cbxCalculMethod.getSelectedEntity().equals(ECalculMethod.PAP)) {
					txtFixedAmount.setVisible(false);
					basedOnAssetGrid.setVisible(true);
					formulaGrid.setVisible(false);
				} else if (cbxCalculMethod.getSelectedEntity().equals(ECalculMethod.FOR)) {
					txtFixedAmount.setVisible(false);
					basedOnAssetGrid.setVisible(false);
					formulaGrid.setVisible(true);
				}
			}
		});
        
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(cbxTreasuryType);
        formPanel.addComponent(cbPaidOneShot);
        formPanel.addComponent(cbxFrequency);
        formPanel.addComponent(dfDueDate);
        formPanel.addComponent(cbContractDuration);
        formPanel.addComponent(txtTermInMonths);
        formPanel.addComponent(cbxServiceType);
        formPanel.addComponent(cbxCalculMethod);
        formPanel.addComponent(txtFixedAmount);
        formPanel.addComponent(basedOnAssetGrid);
        formPanel.addComponent(formulaGrid);
        formPanel.addComponent(cbActive);
        
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(formPanel);
		return verticalLayout;
	}
	
	/**
	 * @param serviId
	 */
	public void assignValues(Long serviId) {
		super.reset();
		if (serviId != null) {
			service = ENTITY_SRV.getById(FinService.class, serviId);
			txtCode.setValue(service.getCode());
			txtDescEn.setValue(service.getDescEn());
			txtDesc.setValue(service.getDesc());
			cbxServiceType.setSelectedEntity(service.getServiceType());;
			cbxCalculMethod.setSelectedEntity(service.getCalculMethod());
			cbxFrequency.setSelectedEntity(service.getFrequency());
			cbxTreasuryType.setSelectedEntity(service.getTreasuryType());
			
			cbContractDuration.setValue(service.isContractDuration());
			cbPaidOneShot.setValue(service.isPaidOneShot());
			txtTermInMonths.setValue(getDefaultString(service.getTermInMonths()));
			txtTermInMonths.setEnabled(!service.isContractDuration());
			dfDueDate.setValue(service.getDueDate());
			
			txtFixedAmount.setValue(getDefaultString(service.getTiPrice()));
			txtPercentageOfPremiumFirstYear.setValue(getDefaultString(service.getPercentageOfPremiumFirstYear()));
			txtPercentageOfPremiumSecondYear.setValue(getDefaultString(service.getPercentageOfPremiumSecondYear()));
			txtPercentageOfPremiumThirdYear.setValue(getDefaultString(service.getPercentageOfPremiumThirdYear()));
			txtPercentageOfPremiumForthYear.setValue(getDefaultString(service.getPercentageOfPremiumForthYear()));
			txtPercentageOfPremiumFifthYear.setValue(getDefaultString(service.getPercentageOfPremiumFifthYear()));
			txtPercentageOfAssetFirstYear.setValue(getDefaultString(service.getPercentageOfAssetFirstYear()));
			txtPercentageOfAssetSecondYear.setValue(getDefaultString(service.getPercentageOfAssetSecondYear()));
			txtPercentageOfAssetThirdYear.setValue(getDefaultString(service.getPercentageOfAssetThirdYear()));
			txtPercentageOfAssetForthYear.setValue(getDefaultString(service.getPercentageOfAssetForthYear()));
			txtPercentageOfAssetFifthYear.setValue(getDefaultString(service.getPercentageOfAssetFifthYear()));
			txtFormula.setValue(service.getFormula());
			
			cbActive.setValue(service.getStatusRecord().equals(EStatusRecord.ACTIV));
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		service = new FinService();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		cbActive.setValue(true);
		cbxServiceType.setSelectedEntity(null);;
		cbxCalculMethod.setSelectedEntity(ECalculMethod.FIX);
		cbxFrequency.setSelectedEntity(null);
		cbxTreasuryType.setSelectedEntity(null);
		
		txtTermInMonths.setValue("");
		txtTermInMonths.setEnabled(true);
		cbContractDuration.setValue(false);
		cbPaidOneShot.setValue(false);
		dfDueDate.setValue(null);;
		
		txtFixedAmount.setValue("");
		txtPercentageOfPremiumFirstYear.setValue("");
		txtPercentageOfPremiumSecondYear.setValue("");
		txtPercentageOfPremiumThirdYear.setValue("");
		txtPercentageOfPremiumForthYear.setValue("");
		txtPercentageOfPremiumFifthYear.setValue("");
		txtPercentageOfAssetFirstYear.setValue("");
		txtPercentageOfAssetSecondYear.setValue("");
		txtPercentageOfAssetThirdYear.setValue("");
		txtPercentageOfAssetForthYear.setValue("");
		txtPercentageOfAssetFifthYear.setValue("");
		txtFormula.setValue("");
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");		
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatorySelectField(cbxServiceType, "service.type");
		checkMandatorySelectField(cbxCalculMethod, "calculate.method");
		checkMandatorySelectField(cbxTreasuryType, "payer");
		return errors.isEmpty();
	}
}
