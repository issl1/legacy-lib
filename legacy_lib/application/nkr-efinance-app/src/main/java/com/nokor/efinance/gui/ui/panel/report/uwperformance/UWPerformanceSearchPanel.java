package com.nokor.efinance.gui.ui.panel.report.uwperformance;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

public class UWPerformanceSearchPanel extends AbstractSearchPanel<Quotation> implements QuotationEntityField {

	private static final long serialVersionUID = 6831105544366692945L;
	
	private DateField dfApplyDate;
	private DateField dfEndDate;
	private SecUserComboBox cbxUnderWriter;
	private SecUserComboBox cbxUnderWriterSupervisor;
	private SecUserComboBox cbxManagement;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	
	private ValueChangeListener valueChangeListener;
	
	private EntityService entityService;
	
	public UWPerformanceSearchPanel(AbstractTablePanel<Quotation> tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	@Override
	protected void reset() {
		dfApplyDate.setValue(null);
		dfEndDate.setValue(null);
		cbxUnderWriter.setSelectedEntity(null);
		cbxUnderWriterSupervisor.setSelectedEntity(null);
		cbxManagement.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
	}

	@Override
	protected Component createForm() {
		entityService = ((EntityService) SecApplicationContextHolder.getContext().getBean("entityService"));
		
		dfApplyDate = ComponentFactory.getAutoDateField(I18N.message("apply.date"), false);
		dfEndDate = ComponentFactory.getAutoDateField(I18N.message("end.date"), false);
		
		cbxUnderWriter = new SecUserComboBox(I18N.message("underwriter"), getUnderWriters());
		cbxUnderWriter.setWidth(150, Unit.PIXELS);
		
		cbxUnderWriterSupervisor = new SecUserComboBox(I18N.message("uw.supervisor"), getUnderWriterSupervisors());
		cbxUnderWriterSupervisor.setWidth(150, Unit.PIXELS);
		
		cbxManagement = new SecUserComboBox(I18N.message("management"), getManagements());
		cbxManagement.setWidth(150, Unit.PIXELS);
		
		cbxDealerType = new ERefDataComboBox<EDealerType>(I18N.message("dealer.type"), EDealerType.values());
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		valueChangeListener = new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = 5719856916200703515L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				EntityService ent = SpringUtils.getBean(EntityService.class);
				cbxDealer.setDealers(ent.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		};
		cbxDealerType.addValueChangeListener(valueChangeListener);
		
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
        cbxDealer = new DealerComboBox(I18N.message("dealer"), entityService.list(restrictions));
        if (ProfileUtil.isCreditOfficer() || ProfileUtil.isProductionOfficer()) {
        	cbxDealer.setEnabled(false);
        	cbxDealerType.setEnabled(false);
        }
        if(ProfileUtil.isCreditOfficerMovable()){
        	cbxDealer.setEnabled(true);
        	cbxDealerType.setEnabled(true);
        }
        
        cbxDealer.setImmediate(true);
		cbxDealer.setWidth("220px");
				
		HorizontalLayout searchLayout = new HorizontalLayout();
		searchLayout.setSpacing(true);
		searchLayout.addComponent(new FormLayout(dfApplyDate, cbxUnderWriter));
		searchLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		searchLayout.addComponent(new FormLayout(dfEndDate, cbxUnderWriterSupervisor));
		searchLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		searchLayout.addComponent(new FormLayout(cbxDealerType, cbxManagement));
		searchLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		searchLayout.addComponent(new FormLayout(cbxDealer,ComponentFactory.getVerticalLayout(25)));
		//searchLayout.addComponent(new FormLayout(ComponentFactory.getVerticalLayout(25), cbxManagement));
				
		return searchLayout;
	}
	
	@Override
	public BaseRestrictions<Quotation> getRestrictions() {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.isNotNull("firstSubmissionDate"));
		restrictions.addCriterion(Restrictions.not(Restrictions.eq("quotationStatus", QuotationWkfStatus.CAN)));
		restrictions.setOrder(Order.desc("firstSubmissionDate"));
		
		if (dfApplyDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateUtils.getDateAtBeginningOfDay(dfApplyDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		if (cbxUnderWriter.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("underwriter.id", cbxUnderWriter.getSelectedEntity().getId()));
		}
		if (cbxUnderWriterSupervisor.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("underwriterSupervisor.id", cbxUnderWriterSupervisor.getSelectedEntity().getId()));
		}
		if (cbxManagement.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("manager.id", cbxManagement.getSelectedEntity().getId()));
		}
		if (cbxDealer.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}
		
		return restrictions;
	}
	
	/**
	 * Get all the underwriters 
	 * @return
	 */
	private List<SecUser> getUnderWriters() {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addAssociation("defaultProfile", "DEFAULT_PROFILE", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("DEFAULT_PROFILE.code", "UW"));
		restrictions.addOrder(Order.asc("desc"));
		return entityService.list(restrictions);
	}
	
	/**
	 * Get all the underwriter supervisors
	 * @return
	 */
	private List<SecUser> getUnderWriterSupervisors() {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addAssociation("defaultProfile", "DEFAULT_PROFILE", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("DEFAULT_PROFILE.code", "US"));
		restrictions.addOrder(Order.asc("desc"));
		return entityService.list(restrictions);
	}
	
	/**
	 * Get all the managements
	 * @return
	 */
	private List<SecUser> getManagements() {
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addAssociation("defaultProfile", "DEFAULT_PROFILE", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("DEFAULT_PROFILE.code", "MA"));
		restrictions.addOrder(Order.asc("desc"));
		return entityService.list(restrictions);
	}
}
