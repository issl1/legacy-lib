package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal.court;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EFlag;
import com.nokor.efinance.core.collection.service.ContractFlagRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * 
 * @author uhout.cheng
 */
public class LegalCourtDetailPanel extends AbstractControlPanel implements ClickListener, FMEntityField, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -4842463011771649916L;
	
	private AutoDateField dfDate;
	private ComboBox cbxTime;
	private TextField txtCourtInCharge;
	
	private EntityComboBox<Province> cbxProvince;
	private EntityComboBox<District> cbxDistrict;
	private EntityComboBox<Commune> cbxSubDistrict;
	
	private ERefDataComboBox<EWkfStatus> cbxStatus;
	private TextArea txtRemark;
	
	private Button btnSubmit;
	private Button btnCancel;
	
	private GridLayout caseDetailLayout;
	
	private LegalCourtResultPanel caseResultPanel;
	
	private LegalCourtPanel legalCourtPanel;
	
	private ContractFlag contractFlag;
	private Contract contract;

	/**
	 * 
	 * @param legalCourtPanel
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LegalCourtDetailPanel(LegalCourtPanel legalCourtPanel) {
		this.legalCourtPanel = legalCourtPanel;
		dfDate = ComponentFactory.getAutoDateField();
		cbxTime = ComponentLayoutFactory.getTimeComboBox();
		cbxTime.setWidth(70, Unit.PIXELS);
		
		cbxProvince = new EntityComboBox<>(Province.class, "desc");
		cbxProvince.setEntities(ENTITY_SRV.list(new BaseRestrictions(Province.class)));
		cbxProvince.setWidth("150px");
		
		cbxDistrict = new EntityComboBox<>(District.class, "desc");
		cbxDistrict.setEntities(ENTITY_SRV.list(new BaseRestrictions(District.class)));
		cbxDistrict.setWidth("150px");
		
		cbxSubDistrict = new EntityComboBox<>(Commune.class, "desc");
		cbxSubDistrict.setEntities(ENTITY_SRV.list(new BaseRestrictions(Commune.class)));
		cbxSubDistrict.setWidth("150px");
		
		cbxProvince.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 4990219457127237004L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					BaseRestrictions<District> restrictions = new BaseRestrictions<>(District.class);
					restrictions.addCriterion(Restrictions.eq(PROVINCE + "." + ID, cbxProvince.getSelectedEntity().getId()));
					cbxDistrict.setEntities(ENTITY_SRV.list(restrictions));
					cbxDistrict.renderer();
					
				} else {
					cbxDistrict.clear();
				}
				cbxSubDistrict.clear();
			}
		});
		
		cbxDistrict.addValueChangeListener(new ValueChangeListener() {

			/** */
			private static final long serialVersionUID = -7843134426891484688L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					BaseRestrictions<Commune> restrictions = new BaseRestrictions<>(Commune.class);
					restrictions.addCriterion(Restrictions.eq(DISTRICT + "." + ID, cbxDistrict.getSelectedEntity().getId()));
					cbxSubDistrict.setEntities(ENTITY_SRV.list(restrictions));
					cbxSubDistrict.renderer();
					
					
				} else {
					cbxSubDistrict.clear();
				}
			}
		});
		
		
		txtCourtInCharge = ComponentFactory.getTextField(150, 150);
		txtRemark = ComponentFactory.getTextArea(false, 200, 60);
		
		cbxStatus = new ERefDataComboBox<>(ContractWkfStatus.listLegalStatus());
		cbxStatus.setWidth(150, Unit.PIXELS);
		
		btnSubmit = ComponentLayoutFactory.getDefaultButton("submit", FontAwesome.CHECK, 70);
		btnSubmit.addClickListener(this);
		btnCancel = ComponentLayoutFactory.getDefaultButton("cancel", FontAwesome.TIMES, 70);
		btnCancel.addClickListener(this);
		
		caseResultPanel = new LegalCourtResultPanel(this);
		
		caseDetailLayout = new GridLayout(6, 3);
		caseDetailLayout.setSpacing(true);
		caseDetailLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("date"));
		caseDetailLayout.addComponent(dfDate);
		caseDetailLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("time"));
		caseDetailLayout.addComponent(cbxTime);
		caseDetailLayout.addComponent(ComponentLayoutFactory.getLabelCaption("court.in.charge"));
		caseDetailLayout.addComponent(txtCourtInCharge);
		
		caseDetailLayout.addComponent(ComponentLayoutFactory.getLabelCaption("province"));
		caseDetailLayout.addComponent(cbxProvince);
		caseDetailLayout.addComponent(ComponentLayoutFactory.getLabelCaption("district"));
		caseDetailLayout.addComponent(cbxDistrict);
		caseDetailLayout.addComponent(ComponentLayoutFactory.getLabelCaption("subdistrict"));
		caseDetailLayout.addComponent(cbxSubDistrict);
		
		caseDetailLayout.addComponent(ComponentLayoutFactory.getLabelCaption("status"));
		caseDetailLayout.addComponent(cbxStatus);
		caseDetailLayout.addComponent(ComponentLayoutFactory.getLabelCaption("remark"));
		caseDetailLayout.addComponent(txtRemark);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnSubmit);
		buttonLayout.addComponent(btnCancel);
		
		caseDetailLayout.addComponent(buttonLayout, 5, 2);
		caseDetailLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		
		addComponent(caseDetailLayout);
		addComponent(caseResultPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	protected void assignValues(Contract contract) {
		this.reset();
		this.contract = contract;
		ContractFlagRestriction restrictions = new ContractFlagRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setFlags(new EFlag[] { EFlag.DEAD });
		List<ContractFlag> contractFlags = ENTITY_SRV.list(restrictions);
		if (contractFlags != null && !contractFlags.isEmpty()) {
			this.contractFlag = contractFlags.get(0);
			caseResultPanel.assignValues(this.contractFlag);
		} else {
			cancelAction();	
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSubmit)) {
			if (isValid()) {
				saveOrUpdateLegalCase();
			} else {
				ComponentLayoutFactory.displayErrorMsg(errors.get(0).toString());
			}
		} else if (event.getButton().equals(btnCancel)) {
			cancelAction();
		}
	}
	
	/**
	 * Show Open button after clicked on cancel button
	 */
	private void cancelAction() {
		caseDetailLayout.setVisible(false);
		caseResultPanel.setVisible(false);
		legalCourtPanel.getBtnOpen().setVisible(true);
	}
	
	/**
	 * 
	 */
	private void saveOrUpdateLegalCase() {
		if (this.contractFlag == null) {
			this.contractFlag = ContractFlag.createInstance();
		}
		this.contract.setWkfSubStatus(cbxStatus.getSelectedEntity());
		this.contractFlag.setContract(contract);
		
		this.contractFlag.setFlag(EFlag.DEAD);
		this.contractFlag.setProvince(cbxProvince.getSelectedEntity());
		this.contractFlag.setDistrict(cbxDistrict.getSelectedEntity());
		this.contractFlag.setCommune(cbxSubDistrict.getSelectedEntity());
		this.contractFlag.setComment(txtRemark.getValue());
		this.contractFlag.setCourtInCharge(txtCourtInCharge.getValue());
		
		Date date = dfDate.getValue();
		if (date != null) {
			date = DateUtils.getDateAtBeginningOfDay(date);
			long time = (long) cbxTime.getValue();
			date.setTime(date.getTime() + time);
		}
		this.contractFlag.setDate(date);
		try {
			CON_FLAG_SRV.saveOrUpdateLegalCase(this.contractFlag);
			ComponentLayoutFactory.displaySuccessfullyMsg();
			assignValues(this.contractFlag.getContract());
		} catch (Exception e) {
			ComponentLayoutFactory.displayErrorMsg(e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	protected void assignValueToControls() {
		this.reset();
		Contract contract = this.contractFlag.getContract();
		if (contract != null) {
			cbxStatus.setSelectedEntity(contract.getWkfSubStatus());
		}
		cbxProvince.setSelectedEntity(this.contractFlag.getProvince());
		cbxDistrict.setSelectedEntity(this.contractFlag.getDistrict());
		cbxSubDistrict.setSelectedEntity(this.contractFlag.getCommune());
		txtRemark.setValue(this.contractFlag.getComment());
		txtCourtInCharge.setValue(this.contractFlag.getCourtInCharge());
		if (this.contractFlag.getDate() != null) {
			dfDate.setValue(this.contractFlag.getDate());
			cbxTime.setValue(this.contractFlag.getDate().getTime() - 
					DateUtils.getDateAtBeginningOfDay(this.contractFlag.getDate()).getTime());
		}
	}
	
	/**
	 * 
	 */
	protected void hideDetailLayout() {
		caseDetailLayout.setVisible(false);
		caseResultPanel.setVisible(true);
		legalCourtPanel.getBtnOpen().setVisible(!caseDetailLayout.isVisible() && !caseResultPanel.isVisible());
	}
	
	/**
	 * Display layout after clicked edit
	 * @param contractFlag
	 */
	protected void displayDetailLayout(ContractFlag contractFlag) {
		if (contractFlag != null && contractFlag.getId() != null) {
			btnSubmit.setCaption(I18N.message("save"));
			btnSubmit.setIcon(FontAwesome.SAVE);
			btnCancel.setVisible(false);
		} else {
			btnSubmit.setCaption(I18N.message("submit"));
			btnSubmit.setIcon(FontAwesome.CHECK);
			btnCancel.setVisible(true);
		}
		caseDetailLayout.setVisible(true);
		caseResultPanel.setVisible(false);
		legalCourtPanel.getBtnOpen().setVisible(!caseDetailLayout.isVisible() && !caseResultPanel.isVisible());
	}
	
	/**
	 * 
	 */
	private boolean isValid() {
		errors.clear();
		checkMandatoryDateField(dfDate, "date");
		if (cbxTime.getValue() == null) {
			errors.add(I18N.message("field.required.1", I18N.message("time")));
		}
		return errors.isEmpty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		btnSubmit.setCaption(I18N.message("submit"));
		btnSubmit.setIcon(FontAwesome.CHECK);
		dfDate.setValue(null);
		cbxTime.setValue(null);
		txtCourtInCharge.setValue(StringUtils.EMPTY);
		cbxProvince.setSelectedEntity(null);
		cbxDistrict.setSelectedEntity(null);
		cbxSubDistrict.setSelectedEntity(null);
		cbxStatus.setSelectedEntity(null);
		txtRemark.setValue(StringUtils.EMPTY);
	}
}
