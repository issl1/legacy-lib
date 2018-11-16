package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.DateUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EFlag;
import com.nokor.efinance.core.collection.service.ContractFlagRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class LegalFormPanel extends AbstractControlPanel implements ClickListener, FMEntityField {
	
	/** */
	private static final long serialVersionUID = 5618474636868696557L;

	private Button btnSubmit;
	private Button btnCancel;
	
	private EntityComboBox<Province> cbxProvince;
	private EntityComboBox<District> cbxDistrict;
	private EntityComboBox<Commune> cbxSubDistrict;
	
	private AutoDateField dfSeizedDate;
	private ComboBox cbxTime;
	
	private TextField txtRemark;
	private TextField txtCurrentLocation;
	
	private VerticalLayout topDetailLayout;
	
	private LegalSubmitFormPanel legalSubmitFormPanel;
	
	private ColPhoneLegalFormPanel colPhoneLegalFormPanel;
	
	private LegalMotoTakenDetailPanel legalMotoTakenDetailPanel;
	
	private ContractFlag contractFlagSeized;
	private Contract contract;
	
	/**
	 * 
	 * @param colPhoneLegalFormPanel
	 */
	public LegalFormPanel(ColPhoneLegalFormPanel colPhoneLegalFormPanel) {
		this.colPhoneLegalFormPanel = colPhoneLegalFormPanel;
		setSizeFull();
		createForm();
	}

	/**
	 * create form
	 */
	@SuppressWarnings("unchecked")
	public void createForm() {
		Label lblRemark = ComponentFactory.getLabel(I18N.message("remark"));
		Label lblDate = ComponentFactory.getLabel(I18N.message("date"));
		Label lblTime = ComponentFactory.getLabel(I18N.message("time"));
		Label lblCurrentLocation = ComponentFactory.getLabel(I18N.message("current.location.of.motobike"));
		Label lblProvince = ComponentFactory.getLabel(I18N.message("province"));
		Label lblDistrict = ComponentFactory.getLabel(I18N.message("district"));
		Label lblSubDistrict = ComponentFactory.getLabel(I18N.message("sub.district"));
		
		dfSeizedDate = ComponentFactory.getAutoDateField();
		cbxTime = ComponentLayoutFactory.getTimeComboBox();
		cbxTime.setWidth(80, Unit.PIXELS);
		txtRemark = ComponentFactory.getTextField(false, 100, 100);
		txtCurrentLocation = ComponentFactory.getTextField(false, 100, 100);
		
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
		
		btnSubmit = new NativeButton(I18N.message("submit"), this);
		btnSubmit.setStyleName("btn btn-success button-small");
		btnSubmit.setWidth("70px");
		btnSubmit.setIcon(FontAwesome.CHECK);
		
		btnCancel = ComponentLayoutFactory.getButtonCancel();
		btnCancel.addClickListener(this);
		btnCancel.setWidth(70, Unit.PIXELS);
		
		GridLayout gridLayout = new GridLayout(15, 1);
		
		int iCol = 0;
		gridLayout.addComponent(lblDate, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(dfSeizedDate, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblTime, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxTime, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblCurrentLocation, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(txtCurrentLocation, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblProvince, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbxProvince, iCol++, 0);
		
		gridLayout.setComponentAlignment(lblDate, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(dfSeizedDate, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblTime, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbxTime, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(txtCurrentLocation, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblProvince, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbxProvince, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblCurrentLocation, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponent(lblDistrict);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		hLayout.addComponent(cbxDistrict);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		hLayout.addComponent(lblSubDistrict);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		hLayout.addComponent(cbxSubDistrict);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		hLayout.addComponent(lblRemark);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		hLayout.addComponent(txtRemark);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		hLayout.addComponent(btnSubmit);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS));
		hLayout.addComponent(btnCancel);
		
		hLayout.setComponentAlignment(lblRemark, Alignment.MIDDLE_LEFT);
		hLayout.setComponentAlignment(lblDistrict, Alignment.MIDDLE_LEFT);
		hLayout.setComponentAlignment(lblSubDistrict, Alignment.MIDDLE_LEFT);
		
		topDetailLayout = new VerticalLayout();
		topDetailLayout.setSpacing(true);
		
		topDetailLayout.addComponent(gridLayout);
		topDetailLayout.addComponent(hLayout);
		
		legalSubmitFormPanel = new LegalSubmitFormPanel(this);
		legalMotoTakenDetailPanel = new LegalMotoTakenDetailPanel();
		
		addComponent(topDetailLayout);
		addComponent(legalSubmitFormPanel);
		addComponent(legalMotoTakenDetailPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		
		ContractFlagRestriction restrictions = new ContractFlagRestriction();
		restrictions.setConId(contract.getId());
		restrictions.setFlags(new EFlag[] { EFlag.SEIZED });
		List<ContractFlag> contractFlags = ENTITY_SRV.list(restrictions);
		if (contractFlags != null && !contractFlags.isEmpty()) {
			this.contractFlagSeized = contractFlags.get(0);
		} else {
			this.contractFlagSeized = null;
		}
		legalSubmitFormPanel.reset();
		if (this.contractFlagSeized != null && this.contractFlagSeized.getId() != null) {
			if (this.contractFlagSeized.isCompleted()) {
				legalSubmitFormPanel.setVisible(false);
				topDetailLayout.setVisible(false);
				legalMotoTakenDetailPanel.setVisible(true);
				colPhoneLegalFormPanel.setVisibleHeaderLayout(false);
				assignMotoTakenValues(contractFlagSeized);
			} else {
				legalSubmitFormPanel.setVisible(true);
				topDetailLayout.setVisible(false);
				legalMotoTakenDetailPanel.setVisible(false);
				legalSubmitFormPanel.assignValues(contractFlagSeized);
				colPhoneLegalFormPanel.setVisibleHeaderLayout(false);
			}
		} else {
			topDetailLayout.setVisible(false);
			legalMotoTakenDetailPanel.setVisible(false);
			legalSubmitFormPanel.setVisible(false);
			colPhoneLegalFormPanel.setVisibleHeaderLayout(true);
		}
		legalSubmitFormPanel.showControl(true);
	}
	
	/**
	 * reset
	 */
	public void reset() {
		dfSeizedDate.setValue(null);
		cbxTime.setValue(null);
		cbxSubDistrict.setValue(null);
		cbxProvince.setValue(null);
		cbxDistrict.setValue(null);
		txtCurrentLocation.setValue("");
		txtRemark.setValue("");
	}
	
	/**
	 * 
	 * @param contractFlag
	 */
	public void assignMotoTakenValues(ContractFlag contractFlag) {
		if (contractFlag != null && contractFlag.getId() != null) {
			if (contractFlag.isCompleted()) {
				legalSubmitFormPanel.setVisible(false);
				topDetailLayout.setVisible(false);
				legalMotoTakenDetailPanel.setVisible(true);
				colPhoneLegalFormPanel.setVisibleHeaderLayout(false);
				legalMotoTakenDetailPanel.assignMotoTakenValue(contractFlag);
			} else {
				topDetailLayout.setVisible(false);
				legalMotoTakenDetailPanel.setVisible(false);
				legalSubmitFormPanel.setVisible(true);
			}
	
		} else {
			topDetailLayout.setVisible(false);
			legalMotoTakenDetailPanel.setVisible(false);
			legalSubmitFormPanel.setVisible(true);
			colPhoneLegalFormPanel.setVisibleHeaderLayout(true);
		}
	}
	
	/**
	 * 
	 * @param contractFlagSeized
	 */
	public void assignContractFlagValues(ContractFlag contractFlagSeized) {
		this.contractFlagSeized = contractFlagSeized;
		reset();
		if (this.contractFlagSeized != null && this.contractFlagSeized.getId() != null) {
			legalSubmitFormPanel.setVisible(false);
			topDetailLayout.setVisible(true);
			legalMotoTakenDetailPanel.setVisible(false);
			
			if (contractFlagSeized.getDate() != null) {
				dfSeizedDate.setValue(contractFlagSeized.getDate());
				cbxTime.setValue(contractFlagSeized.getDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractFlagSeized.getDate()).getTime());
			}
			txtRemark.setValue(getDefaultString(contractFlagSeized.getComment()));
			cbxProvince.setSelectedEntity(contractFlagSeized.getProvince());
			cbxDistrict.setSelectedEntity(contractFlagSeized.getDistrict());
			cbxSubDistrict.setSelectedEntity(contractFlagSeized.getCommune());
			txtCurrentLocation.setValue(contractFlagSeized.getLocantion());
		} else {
			topDetailLayout.setVisible(false);
			legalMotoTakenDetailPanel.setVisible(false);
			legalSubmitFormPanel.setVisible(false);
			colPhoneLegalFormPanel.setVisibleHeaderLayout(true);
		}
	}
	
	/**
	 * 
	 * @param contractFlagSeized
	 */
	public void deleteContractFlag(ContractFlag contractFlagSeized) {
		confirmWithdraw(contractFlagSeized);
		this.contractFlagSeized = null;
	}
	
	/**
	 * 
	 * @param contractFlag
	 * @param flag
	 */
	private void confirmWithdraw(ContractFlag contractFlag) {
		ConfirmDialog.show(UI.getCurrent(), I18N.message("withdraw"),
				new ConfirmDialog.Listener() {

			/** */
			private static final long serialVersionUID = -1952300225253296951L;

			public void onClose(ConfirmDialog dialog) {
				if (dialog.isConfirmed()) {
					ENTITY_SRV.delete(contractFlag);
					ComponentLayoutFactory.getNotificationDesc(contractFlag.getId().toString(), "withdraw.successfully");
					
					colPhoneLegalFormPanel.setVisibleHeaderLayout(true);
					topDetailLayout.setVisible(false);
					legalMotoTakenDetailPanel.setVisible(false);
					legalSubmitFormPanel.setVisible(false);
				}
			}
		});
	}
	
	/**
	 * hide layout
	 */
	public void hideResultLayout() {
		topDetailLayout.setVisible(true);
		legalMotoTakenDetailPanel.setVisible(false);
		legalSubmitFormPanel.setVisible(false);
	}
	
	/**
	 * Save ContractFlag Seized
	 */
	private void saveSeized() {
		if (contractFlagSeized == null) {
			contractFlagSeized = new ContractFlag();
			contractFlagSeized.setContract(contract);
			contractFlagSeized.setFlag(EFlag.SEIZED);
		}
		Date date = dfSeizedDate.getValue();
		if (date != null) {
			date = DateUtils.getDateAtBeginningOfDay(date);
			if (cbxTime.getValue() != null) {
				long time = (long) cbxTime.getValue();
				date.setTime(date.getTime() + time);
			}
		}
		contractFlagSeized.setDate(date);
		contractFlagSeized.setProvince(cbxProvince.getSelectedEntity());
		contractFlagSeized.setDistrict(cbxDistrict.getSelectedEntity());
		contractFlagSeized.setCommune(cbxSubDistrict.getSelectedEntity());
		contractFlagSeized.setComment(txtRemark.getValue());
		contractFlagSeized.setLocantion(txtCurrentLocation.getValue());
		try {
			ENTITY_SRV.saveOrUpdate(contractFlagSeized);
			ComponentLayoutFactory.displaySuccessfullyMsg();
			setVisibleLayout();
			legalSubmitFormPanel.assignValues(contractFlagSeized);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * visible layout
	 */
	public void setVisibleLayout() {
		legalSubmitFormPanel.setVisible(true);
		topDetailLayout.setVisible(false);
		legalMotoTakenDetailPanel.setVisible(false);
		colPhoneLegalFormPanel.setVisibleHeaderLayout(false);
	}
	
	/**
	 * 
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T> getEntityRefComboBox(BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = getEntityRefComboBox();
		comboBox.setRestrictions(restrictions);
		comboBox.setImmediate(true);
		comboBox.renderer();
		return comboBox;
	}
	
	/**
	 * 
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T> getEntityRefComboBox() {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>();
		comboBox.setWidth(190, Unit.PIXELS);
		comboBox.setImmediate(true);
		return comboBox;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSubmit) {
			saveSeized();
		} else if (event.getButton() == btnCancel) {
			topDetailLayout.setVisible(false);
			legalMotoTakenDetailPanel.setVisible(false);
			legalSubmitFormPanel.setVisible(false);
			colPhoneLegalFormPanel.setVisibleHeaderLayout(true);
		}
		
	}
	
}
