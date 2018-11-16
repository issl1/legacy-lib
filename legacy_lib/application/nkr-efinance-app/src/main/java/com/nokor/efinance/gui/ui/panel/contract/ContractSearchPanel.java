	package com.nokor.efinance.gui.ui.panel.contract;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.filters.DealersSelectPanel;
import com.nokor.efinance.gui.ui.panel.contract.filters.SeriesSelectPanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class ContractSearchPanel extends AbstractSearchPanel<Contract> implements FMEntityField, ClickListener, ValueChangeListener{

	/** */
	private static final long serialVersionUID = -686703278974497895L;
	
	private static final String ALL = I18N.message("all");
	private static final String SELECT = I18N.message("select");
	
	private ERefDataComboBox<EApplicantCategory> cbxApplicantCategroy;
	
	private TextField txtNickName;
	private TextField txtLastName;
	private TextField txtContractID;
	private TextField txtFirstName;
	private TextField txtIDNo;
	private TextField txtCustomerID;
	private AutoDateField dfBirthDate;
	private Button btnMoreDetail;
	
	private TextField txtCompanyName;
	private TextField txtPhoneNumber;
	private TextField txtRegistrationNumber;
	private TextField txtEngineNumber;
	private TextField txtChassisNumber;
	
	private ComboBox cbxDealer;
	private ComboBox cbxSerie;
	private Button btnSelectDealer;
	private Button btnSelectSerie;
	
	private ERefDataComboBox<EWkfStatus> cbxContractStatus;
	private AutoDateField dfContractStartDate;
	private AutoDateField dfContractEndDate;
	
	private HorizontalLayout applicantCategoryLayout;
	private HorizontalLayout searchContractLayoutTop;
	private HorizontalLayout searchContractLayoutBottom;
	
	private GridLayout individualSearchLayout;
	private GridLayout companySearchLayout;
	
	private TextField txtCollectorId;
	
	private TextField txtOdmFrom;
	private TextField txtOdmTo;
	private TextField txtOdiFrom;
	private TextField txtOdiTo;
	private TextField txtDpdFrom;
	private TextField txtDpdTo;
	
	private List<Long> dealerIds;
	private List<Long> serieIds;
	
	/**
	 * 
	 * @param contractTablePanel
	 */
	public ContractSearchPanel(AbstractTablePanel<Contract> contractTablePanel) {
		super(I18N.message("search"), contractTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtNickName.setValue(StringUtils.EMPTY);
		txtLastName.setValue(StringUtils.EMPTY);
		txtContractID.setValue(StringUtils.EMPTY);
		txtFirstName.setValue(StringUtils.EMPTY);
		txtIDNo.setValue(StringUtils.EMPTY);
		txtCustomerID.setValue(StringUtils.EMPTY);
		dfBirthDate.setValue(null);
		
		txtCompanyName.setValue(StringUtils.EMPTY);
		txtPhoneNumber.setValue(StringUtils.EMPTY);
		txtRegistrationNumber.setValue(StringUtils.EMPTY);
		txtEngineNumber.setValue(StringUtils.EMPTY);
		txtChassisNumber.setValue(StringUtils.EMPTY);
		
		cbxDealer.select(SELECT);
		cbxSerie.select(SELECT);
		cbxContractStatus.setSelectedEntity(null);
		dfContractStartDate.setValue(DateUtils.getDateAtBeginningOfMonth());
		dfContractEndDate.setValue(DateUtils.getDateAtEndOfMonth());
		txtCompanyName.setValue(StringUtils.EMPTY);
		txtPhoneNumber.setValue(StringUtils.EMPTY);
		txtRegistrationNumber.setValue(StringUtils.EMPTY);
		txtEngineNumber.setValue(StringUtils.EMPTY);
		txtChassisNumber.setValue(StringUtils.EMPTY);
		
		txtCollectorId.setValue(StringUtils.EMPTY);
		txtOdmFrom.setValue(StringUtils.EMPTY);
		txtOdmTo.setValue(StringUtils.EMPTY);
		txtOdiFrom.setValue(StringUtils.EMPTY);
		txtOdiTo.setValue(StringUtils.EMPTY);
		txtDpdFrom.setValue(StringUtils.EMPTY);
		txtDpdTo.setValue(StringUtils.EMPTY);
		cbxApplicantCategroy.setSelectedEntity(EApplicantCategory.INDIVIDUAL);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		txtNickName = ComponentFactory.getTextField(50, 150);
		txtLastName = ComponentFactory.getTextField(50, 150);
		txtContractID = ComponentFactory.getTextField(50, 150);
		
		txtFirstName = ComponentFactory.getTextField(50, 150);
		txtIDNo = ComponentFactory.getTextField(50, 150);
		txtCustomerID = ComponentFactory.getTextField(50, 150);
		dfBirthDate = ComponentFactory.getAutoDateField();
		
		txtCompanyName = ComponentFactory.getTextField(50, 150);
		
		individualSearchLayout = new GridLayout(6, 2);
		individualSearchLayout.setSpacing(true);
		
		individualSearchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("firstname"));
		individualSearchLayout.addComponent(txtFirstName);
		individualSearchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("lastname"));
		individualSearchLayout.addComponent(txtLastName);
		individualSearchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("nickname"));
		individualSearchLayout.addComponent(txtNickName);
		individualSearchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("customer.id"));
		individualSearchLayout.addComponent(txtCustomerID);
		individualSearchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("id.no"));
		individualSearchLayout.addComponent(txtIDNo);
		individualSearchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("birth.date"));
		individualSearchLayout.addComponent(dfBirthDate);

		individualSearchLayout.getComponent(0, 0).setWidth(95, Unit.PIXELS);
		individualSearchLayout.getComponent(2, 0).setWidth(95, Unit.PIXELS);
		individualSearchLayout.getComponent(4, 0).setWidth(95, Unit.PIXELS);
		
		companySearchLayout = new GridLayout(2, 1);
		companySearchLayout.setSpacing(true);
		
		companySearchLayout.addComponent(ComponentLayoutFactory.getLabelCaption("company"));
		companySearchLayout.addComponent(txtCompanyName);
		
		companySearchLayout.getComponent(0, 0).setWidth(95, Unit.PIXELS);
		
		btnMoreDetail = new Button(I18N.message("more.detail"));
		btnMoreDetail.setStyleName(Reindeer.BUTTON_LINK);
		btnMoreDetail.addClickListener(this);
		
		txtPhoneNumber = ComponentFactory.getTextField("", false, 50, 150);
		txtRegistrationNumber = ComponentFactory.getTextField("", false, 50, 150);
		txtEngineNumber = ComponentFactory.getTextField("", false, 50, 150);
		txtChassisNumber = ComponentFactory.getTextField("", false, 50, 150);
		
		cbxDealer = ComponentFactory.getComboBox();
		cbxDealer.setImmediate(true);
		cbxDealer.setWidth(150, Unit.PIXELS);
		cbxDealer.setNullSelectionAllowed(false);
		cbxDealer.addItem(ALL);
		cbxDealer.addItem(SELECT);
		cbxDealer.select(SELECT);
		cbxDealer.addValueChangeListener(this);
		
		cbxSerie = ComponentFactory.getComboBox();
		cbxSerie.setImmediate(true);
		cbxSerie.setWidth(150, Unit.PIXELS);
		cbxSerie.setNullSelectionAllowed(false);
		cbxSerie.addItem(ALL);
		cbxSerie.addItem(SELECT);
		cbxSerie.select(SELECT);
		cbxSerie.addValueChangeListener(this);
		
		txtOdmFrom = ComponentFactory.getTextField(3, 65);
		txtOdmTo = ComponentFactory.getTextField(3, 70);
		txtOdiFrom = ComponentFactory.getTextField(3, 65);
		txtOdiTo = ComponentFactory.getTextField(3, 70);
		txtDpdFrom = ComponentFactory.getTextField(5, 85);
		txtDpdTo = ComponentFactory.getTextField(5, 90);
		
		btnSelectDealer = ComponentLayoutFactory.getDefaultButton("select", null, 60);
		btnSelectDealer.setVisible(cbxDealer.isSelected(SELECT));
		btnSelectDealer.addClickListener(this);
		
		btnSelectSerie = ComponentLayoutFactory.getDefaultButton("select", null, 60);
		btnSelectSerie.setVisible(cbxSerie.isSelected(SELECT));
		btnSelectSerie.addClickListener(this);
		
		cbxContractStatus = new ERefDataComboBox<>(ContractWkfStatus.listContractStatus());
		cbxContractStatus.setWidth(150, Unit.PIXELS);
		
		dfContractStartDate = ComponentFactory.getAutoDateField();
		dfContractEndDate = ComponentFactory.getAutoDateField();
		dfContractStartDate.setWidth(90, Unit.PIXELS);
		dfContractEndDate.setWidth(90, Unit.PIXELS);
		dfContractStartDate.setValue(DateUtils.getDateAtBeginningOfMonth());
		dfContractEndDate.setValue(DateUtils.getDateAtEndOfMonth());
		
		txtCollectorId = ComponentFactory.getTextField(100, 130);
		
		setVisibleControls(false);
		
		cbxApplicantCategroy = new ERefDataComboBox<>(EApplicantCategory.values());
		cbxApplicantCategroy.setNullSelectionAllowed(false);
		cbxApplicantCategroy.addValueChangeListener(this);
		cbxApplicantCategroy.setWidth(150, Unit.PIXELS);
		cbxApplicantCategroy.setSelectedEntity(EApplicantCategory.INDIVIDUAL);
		
		applicantCategoryLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		applicantCategoryLayout.addComponent(ComponentLayoutFactory.getLabelCaption("applicant.category"));
		applicantCategoryLayout.addComponent(cbxApplicantCategroy);
		
		searchContractLayoutTop = new HorizontalLayout();
		searchContractLayoutTop.addComponent(individualSearchLayout);
		searchContractLayoutTop.addComponent(companySearchLayout);
		
		searchContractLayoutBottom = new HorizontalLayout();
		searchContractLayoutBottom.addComponent(searchContractLayoutBotton());
		
		applicantCategoryLayout.setVisible(false);
		searchContractLayoutTop.setVisible(false);
		searchContractLayoutBottom.setVisible(false);
		
		VerticalLayout searchContractLayout = new VerticalLayout();
		searchContractLayout.setSpacing(true);
		searchContractLayout.addComponent(getContractDetailLayout());
		searchContractLayout.addComponent(btnMoreDetail);
		searchContractLayout.addComponent(applicantCategoryLayout);
		searchContractLayout.addComponent(searchContractLayoutTop);
		searchContractLayout.addComponent(searchContractLayoutBottom);

		return searchContractLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private GridLayout getContractDetailLayout() {
		GridLayout contractInfoLayout = new GridLayout(8, 2);
		contractInfoLayout.setSpacing(true);
		
		contractInfoLayout.addComponent(ComponentLayoutFactory.getLabelCaption("contract.id"));
		contractInfoLayout.addComponent(txtContractID);
		contractInfoLayout.addComponent(ComponentLayoutFactory.getLabelCaption("contract.status"));
		contractInfoLayout.addComponent(cbxContractStatus);
		contractInfoLayout.addComponent(ComponentLayoutFactory.getLabelCaption("contract.start.date"));
		contractInfoLayout.addComponent(getInputRangeLayout(dfContractStartDate, ComponentLayoutFactory.getLabelCaption("to"), dfContractEndDate));
		contractInfoLayout.addComponent(ComponentLayoutFactory.getLabelCaption("collector"));
		contractInfoLayout.addComponent(txtCollectorId);
		contractInfoLayout.addComponent(ComponentLayoutFactory.getLabelCaption("odm"));
		contractInfoLayout.addComponent(getInputRangeLayout(txtOdmFrom, ComponentLayoutFactory.getLabelCaption("-"), txtOdmTo));
		contractInfoLayout.addComponent(ComponentLayoutFactory.getLabelCaption("odi"));
		contractInfoLayout.addComponent(getInputRangeLayout(txtOdiFrom, ComponentLayoutFactory.getLabelCaption("-"), txtOdiTo));
		contractInfoLayout.addComponent(ComponentLayoutFactory.getLabelCaption("dpd"));
		contractInfoLayout.addComponent(getInputRangeLayout(txtDpdFrom, ComponentLayoutFactory.getLabelCaption("-"), txtDpdTo));
		
		contractInfoLayout.getComponent(0, 0).setWidth(95, Unit.PIXELS);
		contractInfoLayout.getComponent(2, 0).setWidth(95, Unit.PIXELS);
		contractInfoLayout.getComponent(4, 0).setWidth(105, Unit.PIXELS);
		contractInfoLayout.getComponent(6, 0).setWidth(70, Unit.PIXELS);
		return contractInfoLayout;
	}
	
	/**
	 * 
	 * @param children
	 * @return
	 */
	private HorizontalLayout getInputRangeLayout(Component... children) {
		HorizontalLayout layout = new HorizontalLayout(children);
		layout.setSpacing(true);
		return layout;
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getCollector() {
		return txtCollectorId.getValue();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Contract> getRestrictions() {		
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("collections", "col", JoinType.LEFT_OUTER_JOIN);
		restrictions.addAssociation("applicant", "app", JoinType.INNER_JOIN);
		restrictions.addAssociation("asset", "ass", JoinType.INNER_JOIN);

		restrictions.addCriterion(Restrictions.eq("app.applicantCategory", cbxApplicantCategroy.getSelectedEntity()));
		
		if (EApplicantCategory.COMPANY.equals(cbxApplicantCategroy.getSelectedEntity())) {
			restrictions.addAssociation("app.company", "com", JoinType.INNER_JOIN);
		} else if (EApplicantCategory.INDIVIDUAL.equals(cbxApplicantCategroy.getSelectedEntity())
				|| EApplicantCategory.GLSTAFF.equals(cbxApplicantCategroy.getSelectedEntity())) {
			restrictions.addAssociation("app.individual", "indi", JoinType.INNER_JOIN);
		}
		
		if (StringUtils.isNotEmpty(txtCollectorId.getValue())) {
			DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
			userContractSubCriteria.createAlias("cousr.secUser", "usr", JoinType.INNER_JOIN);
			userContractSubCriteria.createAlias("usr.defaultProfile", "pro", JoinType.INNER_JOIN);
			userContractSubCriteria.add(Restrictions.in("pro.code", new String[] { IProfileCode.COL_PHO_STAFF, 
					IProfileCode.COL_FIE_STAFF, IProfileCode.COL_INS_STAFF, IProfileCode.COL_OA_STAFF }));
			userContractSubCriteria.add(Restrictions.eq("usr" + "." + SecUser.DESC, txtCollectorId.getValue()));
			userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
			restrictions.addCriterion(Property.forName(Contract.ID).in(userContractSubCriteria));
		}
		
		if (StringUtils.isNumeric(txtOdmFrom.getValue())) {
			restrictions.addCriterion(Restrictions.ge("col" + "." + Collection.DEBTLEVEL, Integer.parseInt(txtOdmFrom.getValue())));
		}
		if (StringUtils.isNumeric(txtOdmTo.getValue())) {
			restrictions.addCriterion(Restrictions.le("col" + "." + Collection.DEBTLEVEL, Integer.parseInt(txtOdmTo.getValue())));
		}
		
		if (StringUtils.isNumeric(txtOdiFrom.getValue())) {
			restrictions.addCriterion(Restrictions.ge("col" + "." + Collection.NBINSTALLMENTSINOVERDUE, Integer.parseInt(txtOdiFrom.getValue())));
		}
		if (StringUtils.isNumeric(txtOdiTo.getValue())) {
			restrictions.addCriterion(Restrictions.le("col" + "." + Collection.NBINSTALLMENTSINOVERDUE, Integer.parseInt(txtOdiTo.getValue())));
		}
		
		if (StringUtils.isNumeric(txtDpdFrom.getValue())) {
			restrictions.addCriterion(Restrictions.ge("col" + "." + Collection.NBOVERDUEINDAYS, Integer.parseInt(txtDpdFrom.getValue())));
		}
		if (StringUtils.isNumeric(txtDpdTo.getValue())) {
			restrictions.addCriterion(Restrictions.le("col" + "." + Collection.NBOVERDUEINDAYS, Integer.parseInt(txtDpdTo.getValue())));
		}
		
		if (!StringUtils.isEmpty(txtNickName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("indi" + ".nickName" , txtNickName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (!StringUtils.isEmpty(txtFirstName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("indi" + "." + FIRST_NAME_EN, txtFirstName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (!StringUtils.isEmpty(txtLastName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("indi" + "." + LAST_NAME_EN, txtLastName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (dfBirthDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("indi" + "." + BIRTH_DATE, DateUtils.getDateAtBeginningOfDay(dfBirthDate.getValue())));
			restrictions.addCriterion(Restrictions.le("indi" + "." + BIRTH_DATE, DateUtils.getDateAtEndOfDay(dfBirthDate.getValue())));
		}
		
		if (!StringUtils.isEmpty(txtContractID.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, txtContractID.getValue(), MatchMode.ANYWHERE));
		}
		
		if (!StringUtils.isEmpty(txtIDNo.getValue())) {
			restrictions.addCriterion(Restrictions.like("indi.idNumber", txtIDNo.getValue(), MatchMode.ANYWHERE));
		}
		
		if (!StringUtils.isEmpty(txtCustomerID.getValue())) {
			restrictions.addCriterion(Restrictions.like("indi" + "." + Individual.REFERENCE, txtCustomerID.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxContractStatus.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq(WKF_STATUS, cbxContractStatus.getSelectedEntity()));
		}
		
		if (!StringUtils.isEmpty(txtCompanyName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("com.nameEn", txtCompanyName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (!StringUtils.isEmpty(txtPhoneNumber.getValue())) {
			if (!EApplicantCategory.COMPANY.equals(cbxApplicantCategroy.getSelectedEntity())) {
				restrictions.addAssociation("indi.individualContactInfos", "incont", JoinType.INNER_JOIN);
				restrictions.addAssociation("incont.contactInfo", "cont", JoinType.INNER_JOIN);
				restrictions.addCriterion(Restrictions.eq("cont.value", txtPhoneNumber.getValue()));
			} else {
				restrictions.addAssociation("com.companyContactInfos", "comcont", JoinType.INNER_JOIN);
				restrictions.addAssociation("comcont.contactInfo", "cont", JoinType.INNER_JOIN);
				restrictions.addCriterion(Restrictions.eq("cont.value", txtPhoneNumber.getValue()));
			}
		}
		
		if (!StringUtils.isEmpty(txtRegistrationNumber.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("ass.plateNumber", txtRegistrationNumber.getValue(), MatchMode.ANYWHERE));
		}
		
		if (!StringUtils.isEmpty(txtEngineNumber.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("ass.engineNumber", txtEngineNumber.getValue(), MatchMode.ANYWHERE));
		}
		
		if (!StringUtils.isEmpty(txtChassisNumber.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("ass.chassisNumber", txtChassisNumber.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxDealer.isSelected(SELECT) && (dealerIds != null && !dealerIds.isEmpty())) {
			restrictions.addCriterion(Restrictions.in("dealer.id", dealerIds));
		}
		
		if (cbxSerie.isSelected(SELECT) && (serieIds != null && !serieIds.isEmpty())) {
			restrictions.addAssociation("ass.model", "mod", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.in("mod.id", serieIds));
		}
		
		if (dfContractStartDate.getValue() != null && dfContractEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(Contract.STARTDATE, DateUtils.getDateAtBeginningOfDay(dfContractStartDate.getValue())));
			restrictions.addCriterion(Restrictions.le(Contract.STARTDATE, DateUtils.getDateAtEndOfDay(dfContractEndDate.getValue())));
		} else if (dfContractStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge(Contract.STARTDATE, DateUtils.getDateAtBeginningOfDay(dfContractStartDate.getValue())));
		} else if (dfContractEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(Contract.STARTDATE, DateUtils.getDateAtEndOfDay(dfContractEndDate.getValue())));
		}
	
		restrictions.addOrder(Order.desc(START_DATE));
		return restrictions;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSelectDealer)) {
			showDealersSelectPanel();
		} else if (event.getButton().equals(btnSelectSerie)) {
			showSeriesSelectPanel();
		} else {
			if (!searchContractLayoutBottom.isVisible()) {
				applicantCategoryLayout.setVisible(true);
				searchContractLayoutTop.setVisible(true);
				searchContractLayoutBottom.setVisible(true);
			} else {
				applicantCategoryLayout.setVisible(false);
				searchContractLayoutTop.setVisible(false);
				searchContractLayoutBottom.setVisible(false);
			}
		}
	}

	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(cbxApplicantCategroy)) {
			if (EApplicantCategory.INDIVIDUAL.equals(cbxApplicantCategroy.getSelectedEntity())) {
				setVisibleControls(true);
				companySearchLayout.setVisible(false);
			} else if (EApplicantCategory.COMPANY.equals(cbxApplicantCategroy.getSelectedEntity())) {
				setVisibleControls(false);
				companySearchLayout.setVisible(true);
			}	
		} else if (event.getProperty().equals(cbxDealer)) {
			btnSelectDealer.setVisible(false);
			if (SELECT.equals(cbxDealer.getValue())) {
				btnSelectDealer.setVisible(true);
			} else {
				btnSelectDealer.setVisible(false);
			}	
		} else if (event.getProperty().equals(cbxSerie)) {
			btnSelectSerie.setVisible(false);
			if (SELECT.equals(cbxSerie.getValue())) {
				btnSelectSerie.setVisible(true);
			} else {
				btnSelectSerie.setVisible(false);
			}	
		}
	}
	
	/**
	 * 
	 * @param isVisible
	 */
	private void setVisibleControls(boolean isVisible) {
		individualSearchLayout.setVisible(isVisible);
	}
	
	/**
	 * Show Dealers select panel
	 */
	private void showDealersSelectPanel() {
		DealersSelectPanel usersSelectPanel = new DealersSelectPanel();
		usersSelectPanel.show(new DealersSelectPanel.Listener() {				
			
			/** */
			private static final long serialVersionUID = 6281794625617994402L;
			
			/**
			 * @see com.nokor.efinance.gui.ui.panel.contract.filters.DealersSelectPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.contract.filters.DealersSelectPanel)
			 */
			@Override
			public void onClose(DealersSelectPanel dialog) {
				dealerIds = dialog.getSelectedIds();
			}
		});
	}
	
	/**
	 * Show Series select panel
	 */
	private void showSeriesSelectPanel() {
		SeriesSelectPanel seriesSelectPanel = new SeriesSelectPanel();
		seriesSelectPanel.show(new SeriesSelectPanel.Listener() {				
			
			/** */
			private static final long serialVersionUID = -3717862642123490438L;

			/**
			 * @see com.nokor.efinance.gui.ui.panel.contract.filters.SeriesSelectPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.contract.filters.SeriesSelectPanel)
			 */
			@Override
			public void onClose(SeriesSelectPanel dialog) {
				serieIds = dialog.getSelectedIds();
			}
		});
	}
	
	/**
	 * 
	 * @return
	 */
	private GridLayout searchContractLayoutBotton() {
		GridLayout gridLayout = new GridLayout(7, 2);
		gridLayout.setSpacing(true);
	
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("phone.number"));
		gridLayout.addComponent(txtPhoneNumber);
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("registration.number"));
		gridLayout.addComponent(txtRegistrationNumber);
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("dealer.shop"));
		gridLayout.addComponent(cbxDealer);
		gridLayout.addComponent(btnSelectDealer);
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("engine.no"));
		gridLayout.addComponent(txtEngineNumber);
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("chassis.no"));
		gridLayout.addComponent(txtChassisNumber);
		gridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("asset.model"));
		gridLayout.addComponent(cbxSerie);
		gridLayout.addComponent(btnSelectSerie);
		
		gridLayout.getComponent(0, 0).setWidth(95, Unit.PIXELS);
		gridLayout.getComponent(2, 0).setWidth(95, Unit.PIXELS);
		gridLayout.getComponent(4, 0).setWidth(95, Unit.PIXELS);
		
		return gridLayout;
	}
}
