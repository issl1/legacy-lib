package com.nokor.efinance.gui.ui.panel.collection.searchcontract;

import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
/**
 * Contract search panel in collection
 * @author uhout.cheng
 */
public class CollectionContractSearchPanel extends AbstractSearchPanel<Quotation> implements QuotationEntityField {

	private static final long serialVersionUID = -635644650780535609L;
	
	private static String LESSEE = I18N.message("lessee");
	private static String GUARANTOR = I18N.message("guarantor");
	
	private OptionGroup optionGroup;

	private TextField txtName;
	private TextField txtContractId;
	private TextField txtApplicantId;
	private TextField txtIdNo;
	private AutoDateField dfDOB;
	
	private TextField txtRegistrationNo;
	private TextField txtEnginNo;
	private TextField txtChassisNo;
	private TextField txtInstallment;
	private TextField txtResponsibilitor;
	private ERefDataComboBox<EWkfStatus> cbxContractStatus;
	
	private TextField txtNameOrSurName;
	private TextField txtNameSurnameOrJutistic;
	private TextField txtContractNo;
	private ComboBox cbxShopName;
	private EntityRefComboBox<AssetMake> cbxBrand;
	private ComboBox cbxProhibitToSell;
	private CheckBox cbProhibitToSell;
	
	/**
	 * 
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T>  getEntityRefComboBox(BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>();
		comboBox.setWidth(190, Unit.PIXELS);
		comboBox.setRestrictions(restrictions);
		comboBox.renderer();
		return comboBox;
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	private <T extends RefDataId> ERefDataComboBox<T>  getERefDataComboBox(List<T> values) {
		ERefDataComboBox<T> comboBox = new ERefDataComboBox<>(values);
		comboBox.setWidth(190, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * 
	 * @return
	 */
	private ComboBox getComboBox() {
		ComboBox comboBox = ComponentFactory.getComboBox();
		comboBox.setWidth(190, Unit.PIXELS);
		return comboBox;
	}
	
	/**
	 * 
	 * @param searchContractTablePanel
	 */
	public CollectionContractSearchPanel(CollectionSearchContractTablePanel searchContractTablePanel) {
		super(I18N.message("search"), searchContractTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtName.setValue("");
		txtContractId.setValue("");
		txtApplicantId.setValue("");
		txtIdNo.setValue("");
		dfDOB.setValue(null);
		
		txtRegistrationNo.setValue("");
		txtEnginNo.setValue("");
		txtChassisNo.setValue("");
		txtInstallment.setValue("");
		txtRegistrationNo.setValue("");
		
		txtNameOrSurName.setValue("");
		txtNameSurnameOrJutistic.setValue("");
		txtContractNo.setValue("");
		txtResponsibilitor.setValue("");
		cbxShopName.setValue(null);
		cbxBrand.setValue(null);
		cbxProhibitToSell.setValue("");
		cbxProhibitToSell.setValue(false);
		cbxContractStatus.setValue("");
		optionGroup.clear();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		final List<String> options = Arrays.asList(new String[] {LESSEE, GUARANTOR});
        optionGroup = new OptionGroup(null, options);
		optionGroup.addStyleName("horizontal");
		optionGroup.setImmediate(true);
		optionGroup.setNullSelectionAllowed(false);
		optionGroup.setMultiSelect(false);
		
		txtName = ComponentFactory.getTextField(false, 60, 190);
		txtContractId = ComponentFactory.getTextField(false, 60, 190);
		txtApplicantId = ComponentFactory.getTextField( false, 60, 190);
		txtIdNo = ComponentFactory.getTextField(false, 60, 190);
		dfDOB = ComponentFactory.getAutoDateField();
		
		txtRegistrationNo = ComponentFactory.getTextField(false, 60, 190);
		txtEnginNo = ComponentFactory.getTextField(false, 60, 190);
		txtChassisNo = ComponentFactory.getTextField( false, 60, 190);
		txtInstallment = ComponentFactory.getTextField(false, 60, 190);
		txtResponsibilitor = ComponentFactory.getTextField(false, 60, 190);
		
		txtNameOrSurName = ComponentFactory.getTextField(false, 60, 190);
		txtNameSurnameOrJutistic = ComponentFactory.getTextField(false, 60, 190);
		txtContractNo = ComponentFactory.getTextField(false, 60, 190);
		cbxShopName = getComboBox();
		cbxBrand = getEntityRefComboBox(new BaseRestrictions<>(AssetMake.class));
		cbxProhibitToSell = getComboBox();
		cbxContractStatus = getERefDataComboBox(EWkfStatus.values());
		cbProhibitToSell = new CheckBox();
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(getSearchLayout());
		
		return mainLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private GridLayout getSearchLayout() {
		HorizontalLayout horizontalLayoutShopNameAndBrand = ComponentFactory.getHorizontalLayout();
		horizontalLayoutShopNameAndBrand.addComponent(cbxShopName);
		horizontalLayoutShopNameAndBrand.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		horizontalLayoutShopNameAndBrand.addComponent(new Label(I18N.message("brand")));
		horizontalLayoutShopNameAndBrand.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		horizontalLayoutShopNameAndBrand.addComponent(cbxBrand);
		
		HorizontalLayout horizontalLayoutProhibitToSell = ComponentFactory.getHorizontalLayout();
		horizontalLayoutProhibitToSell.addComponent(cbProhibitToSell);
		horizontalLayoutProhibitToSell.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		horizontalLayoutProhibitToSell.addComponent(new Label(I18N.message("prohibit.to.sell")));
		horizontalLayoutProhibitToSell.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		horizontalLayoutProhibitToSell.addComponent(cbxProhibitToSell);
		
		GridLayout gridLayout = new GridLayout(10, 10);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		int iCol = 2;
		gridLayout.addComponent(optionGroup, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("contract.id")), iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(txtContractId, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(new Label(I18N.message("chassis.no")), iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(txtChassisNo, iCol++, 1);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("applicant.id")), iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(txtApplicantId, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(new Label(I18N.message("id.no")), iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(txtIdNo, iCol++, 2);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("name.en")), iCol++, 3);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 3);
		gridLayout.addComponent(txtName, iCol++, 3);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 3);
		gridLayout.addComponent(new Label(I18N.message("installment")), iCol++, 3);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 3);
		gridLayout.addComponent(txtInstallment, iCol++, 3);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("name.surname")), iCol++, 4);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 4);
		gridLayout.addComponent(txtNameOrSurName, iCol++, 4);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 4);
		gridLayout.addComponent(new Label(I18N.message("shop.name")), iCol++, 4);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 4);
		gridLayout.addComponent(horizontalLayoutShopNameAndBrand, iCol++, 4);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("dob")), iCol++, 5);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 5);
		gridLayout.addComponent(dfDOB, iCol++, 5);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 5);
		gridLayout.addComponent(new Label(I18N.message("responsibilitor")), iCol++, 5);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 5);
		gridLayout.addComponent(txtResponsibilitor, iCol++, 5);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("name.surname.or.jutistic")), iCol++, 6);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 6);
		gridLayout.addComponent(txtNameSurnameOrJutistic, iCol++, 6);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 6);
		gridLayout.addComponent(new Label(I18N.message("contract.status")), iCol++, 6);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 6);
		gridLayout.addComponent(cbxContractStatus, iCol++, 6);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("contract.no")), iCol++, 7);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 7);
		gridLayout.addComponent(txtContractNo, iCol++, 7);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 7);
		gridLayout.addComponent(horizontalLayoutProhibitToSell, 6, 7);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("registration.no")), iCol++, 8);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 8);
		gridLayout.addComponent(txtRegistrationNo, iCol++, 8);
		
		iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("engine.no")), iCol++, 9);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 9);
		gridLayout.addComponent(txtEnginNo, iCol++, 9);
		return gridLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Quotation> getRestrictions() {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);		
		restrictions.addCriterion(Restrictions.between(ID, 1l, 7l));
		restrictions.addOrder(Order.desc(ID));
		return restrictions;
	}
	
}
