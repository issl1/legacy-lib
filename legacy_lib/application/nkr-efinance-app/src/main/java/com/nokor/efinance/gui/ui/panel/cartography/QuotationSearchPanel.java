package com.nokor.efinance.gui.ui.panel.cartography;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Village search
 * @author youhort.ly
 *
 */
public class QuotationSearchPanel extends VerticalLayout implements FMEntityField {

	private static final long serialVersionUID = 2337729438174372754L;
	private DealerComboBox cbxDealer;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
    
	public QuotationSearchPanel() {
		setMargin(true);
		addComponent(createForm());
	}


	/**
	 * @return
	 */
	protected Component createForm() {		
		cbxDealer = new DealerComboBox(DataReference.getInstance().getDealers());
		cbxDealer.setWidth("220px");
		 
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.today());
		
		dfEndDate = ComponentFactory.getAutoDateField("", false);    
		dfEndDate.setValue(DateUtils.today());
		
		int col = 0;
		final GridLayout gridLayout = new GridLayout(12, 1);
		gridLayout.setSpacing(true);
        gridLayout.addComponent(new Label(I18N.message("dealer")), col++, 0);
        gridLayout.addComponent(cbxDealer, col++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), col++, 0);
        gridLayout.addComponent(new Label(I18N.message("start.date")), col++, 0);
        gridLayout.addComponent(dfStartDate, col++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), col++, 0);
        gridLayout.addComponent(new Label(I18N.message("end.date")), col++, 0);
        gridLayout.addComponent(dfEndDate, col++, 0);
        
        VerticalLayout contenLayout = new VerticalLayout();
        contenLayout.setMargin(true);
        contenLayout.addComponent(gridLayout);
		return contenLayout;
	}
	
	/**
	 * @return district.province.id
	 */
	public BaseRestrictions<Quotation> getRestrictions() {		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);
		if (cbxDealer.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge("startCreationDate", DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("startCreationDate", DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		restrictions.addOrder(Order.desc("dealer"));
		return restrictions;
	}

	public void reset() {
		cbxDealer.setSelectedEntity(null);
		dfEndDate.setValue(null);
		dfStartDate.setValue(null);
	}
}
