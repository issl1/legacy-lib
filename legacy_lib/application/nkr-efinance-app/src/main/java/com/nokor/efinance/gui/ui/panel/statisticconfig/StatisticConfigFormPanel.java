package com.nokor.efinance.gui.ui.panel.statisticconfig;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.glf.statistic.model.StatisticConfig;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * StatisticConfigFormPanel
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StatisticConfigFormPanel extends AbstractFormPanel {

	private static final long serialVersionUID = 5797514520736099076L;

	private StatisticConfig statisticConfig;
	private DealerComboBox cbxDealer;
	private AutoDateField dfStartDate;
	private TextField txttargetLow;
    private TextField txttargetHigh;
	private SecUserDetail secUserDetail;
	private boolean isUpdateStatisticConfig = false;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Entity getEntity() {
		statisticConfig.setDealer(cbxDealer.getSelectedEntity());
		statisticConfig.setStartDate(DateUtils.getDateAtBeginningOfMonth(dfStartDate.getValue()));
		statisticConfig.setTargetLow(getInteger(txttargetLow));
		statisticConfig.setTargetHigh(getInteger(txttargetHigh));
		return statisticConfig;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();	
		cbxDealer = new DealerComboBox(I18N.message("dealer"), DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		secUserDetail = getSecUserDetail(); 
		if (secUserDetail != null) {
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());	
		} else {
			cbxDealer.setSelectedEntity(null);
		}
		cbxDealer.setRequired(true);
		dfStartDate = ComponentFactory.getAutoDateField("", true);
		dfStartDate.setCaption(I18N.message("start.date"));
		dfStartDate.setValue(DateUtils.getDateAtBeginningOfMonth());
		txttargetHigh = ComponentFactory.getTextField("target.high", false, 60, 200);	
		txttargetHigh.setRequired(true);
		txttargetLow = ComponentFactory.getTextField("target.low", true, 60, 200);      
		txttargetLow.setRequired(true);
        
        formPanel.addComponent(cbxDealer);
        formPanel.addComponent(dfStartDate);
        formPanel.addComponent(txttargetHigh);
        formPanel.addComponent(txttargetLow);
		return formPanel;
	}
	
	/**
	 * @param statisticConfigId
	 */
	public void assignValues(Long statisticConfigId) {
		super.reset();
		if (statisticConfigId != null) {
			isUpdateStatisticConfig = true;
			statisticConfig = ENTITY_SRV.getById(StatisticConfig.class, statisticConfigId);
			cbxDealer.setSelectedEntity(statisticConfig.getDealer());
			dfStartDate.setValue(statisticConfig.getStartDate());
			txttargetHigh.setValue(getDefaultString(statisticConfig.getTargetHigh()));
			txttargetLow.setValue(getDefaultString(statisticConfig.getTargetLow()));
		}
	}
	
	/**
	 * Reset
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		isUpdateStatisticConfig = false;
		statisticConfig = new StatisticConfig();
		txttargetHigh.setValue("");
		txttargetLow.setValue("");
		dfStartDate.setValue(DateUtils.getDateAtBeginningOfMonth());
		if (secUserDetail != null) {
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());	
		} else {
			cbxDealer.setSelectedEntity(null);
		}
		markAsDirty();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txttargetHigh, "target.high");
		checkMandatoryField(txttargetLow, "target.low");
		checkMandatoryDateField(dfStartDate, "start.date");
		checkMandatorySelectField(cbxDealer, "dealer");
		
		if (errors.isEmpty()) {
			BaseRestrictions<StatisticConfig> restrictions = new BaseRestrictions<StatisticConfig>(StatisticConfig.class);		
			restrictions.addCriterion(Restrictions.eq("dealer.id", cbxDealer.getSelectedEntity().getId()));
			restrictions.addCriterion(Restrictions.eq("startDate", DateUtils.getDateAtBeginningOfMonth(dfStartDate.getValue())));
			if (!isUpdateStatisticConfig && ENTITY_SRV.list(restrictions) != null && !ENTITY_SRV.list(restrictions).isEmpty()) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("information"),
						MessageBox.Icon.ERROR, I18N.message("this.statistic.config.already.created",""+ DateUtils.getDateLabel(DateUtils.getDateAtBeginningOfMonth(dfStartDate.getValue()), "dd/MM/yyyy")), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
				return false;
			}
		}
		return errors.isEmpty();
	}
	
	/**
	 * @return usrDetail
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EntityService entityService = SpringUtils.getBean(EntityService.class);
		return entityService.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}
}
