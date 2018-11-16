package com.nokor.efinance.gui.ui.panel.system;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.glf.statistic.model.Statistic3HoursVisitor;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
/**
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(StatisticVisitorsPanel.NAME)
public class StatisticVisitorsPanel extends Panel implements View {
	
	private static final long serialVersionUID = 6227740006388204118L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "statistic.visitors";
	
	@Autowired
	private EntityService entityService;
	private TextField txtNumberVisitorCompany;
	private TextField txtNumberVisitorDealer;
	private TextField txtNumberVisitorApply;
	private DealerComboBox cbxDealer;
	private SecUserDetail secUserDetail;
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("statistic.visitors"));
		
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		cbxDealer.setImmediate(true);
		
		secUserDetail = getSecUserDetail(); 
		cbxDealer.setSelectedEntity(secUserDetail.getDealer());
		cbxDealer.setRequired(true);
		
		cbxDealer.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1812853593330316633L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				assignValues();
			}
		});
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setMargin(true);
		VerticalLayout verticalLayout1 = new VerticalLayout();
		verticalLayout1.setSpacing(true);
		verticalLayout1.setMargin(true);
		verticalLayout1.setCaption(I18N.message("visitor.company"));
		VerticalLayout verticalLayout2 = new VerticalLayout();
		verticalLayout2.setCaption(I18N.message("visitor.dealer"));
		verticalLayout2.setMargin(true);
		verticalLayout2.setSpacing(true);
		VerticalLayout verticalLayout3 = new VerticalLayout();
		verticalLayout3.setCaption(I18N.message("visitor.apply"));
		verticalLayout3.setMargin(true);
		verticalLayout3.setSpacing(true);
		
		txtNumberVisitorCompany = ComponentFactory.getTextField(60, 150);
		txtNumberVisitorCompany.setEnabled(false);
		txtNumberVisitorCompany.setHeight("150");
		txtNumberVisitorCompany.setStyleName("text_align_centre");
		
		txtNumberVisitorDealer = ComponentFactory.getTextField(60, 150);
		txtNumberVisitorDealer.setEnabled(false);
		txtNumberVisitorDealer.setHeight("150");
		txtNumberVisitorDealer.setStyleName("text_align_centre");
		
		txtNumberVisitorApply = ComponentFactory.getTextField(60, 150);
		txtNumberVisitorApply.setEnabled(false);
		txtNumberVisitorApply.setHeight("150");
		txtNumberVisitorApply.setStyleName("text_align_centre");
		
		horizontalLayout.addComponent(txtNumberVisitorDealer);
		horizontalLayout.addComponent(verticalLayout2);
		
		horizontalLayout.addComponent(txtNumberVisitorCompany);
		horizontalLayout.addComponent(verticalLayout1);
		
		horizontalLayout.addComponent(txtNumberVisitorApply);
		horizontalLayout.addComponent(verticalLayout3);
		
		VerticalLayout vertical = new VerticalLayout();
		vertical.setSizeFull();
		vertical.setMargin(true);
		vertical.addComponent(cbxDealer);
		vertical.addComponent(horizontalLayout);
		setContent(vertical);
		assignValues();
	}
	
	/**
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EntityService entityService = SpringUtils.getBean(EntityService.class);
		return entityService.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	private void assignValues() {
		BaseRestrictions<Statistic3HoursVisitor> restrictions = new BaseRestrictions<Statistic3HoursVisitor>(Statistic3HoursVisitor.class);
		restrictions.addCriterion(Restrictions.ge("createDate", DateUtils.getDateAtBeginningOfDay(DateUtils.today())));
		restrictions.addCriterion(Restrictions.le("createDate", DateUtils.getDateAtEndOfDay(DateUtils.today())));
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealer.id", cbxDealer.getSelectedEntity().getId()));
		}
		
	}
}
