package com.nokor.ersys.core.hr.holiday.list;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.ersys.core.hr.model.PublicHoliday;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * Holiday Search Panel
 * @author phirun.kong
 *
 */
public class HolidaySearchPanel extends AbstractSearchPanel<PublicHoliday> {

	private static final long serialVersionUID = -4853168192116682004L;
	private ComboBox cbYear;
	private TextField txtDescEn;

	/**
	 * @param tablePanel
	 */
	public HolidaySearchPanel(HolidayTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	@Override
	protected Component createForm() {
		cbYear = getYearComboBox();
		txtDescEn = ComponentFactory.getTextField("desc.en",false,80,180);
		GridLayout grid = new GridLayout(2,1);
		grid.addComponent(new FormLayout(cbYear)); 
		grid.addComponent(new FormLayout(txtDescEn));

		return grid; 
	}
	
	private ComboBox getYearComboBox(){
		ComboBox combobox = ComponentFactory.getComboBox("holiday.year", null);
		int startYear = 2010;
		int duration = 5;
		int currentYear = DateUtils.getYear(DateUtils.today());
		for(int i = startYear ; i <currentYear ; i++){
			combobox.addItem(String.valueOf(i));
		}
		for(int i = currentYear ; i < currentYear + duration+1 ; i++){
			combobox.addItem(String.valueOf(i));
		}
		combobox.setWidth(110, Unit.PIXELS);
		combobox.setValue(String.valueOf(currentYear)); 
		return combobox;
	}

	@Override
	public BaseRestrictions<PublicHoliday> getRestrictions() {
		BaseRestrictions<PublicHoliday> restrictions = new BaseRestrictions<PublicHoliday>(PublicHoliday.class);
		if(cbYear.getValue() != null){
			Date start =DateUtils.getDate("31/12/" + (Integer.parseInt(cbYear.getValue().toString())-1), DateUtils.FORMAT_DDMMYYYY_SLASH);
			Date end =DateUtils.getDate("31/12/" + cbYear.getValue(), DateUtils.FORMAT_DDMMYYYY_SLASH);
			restrictions.addCriterion(Restrictions.ge("day",DateUtils.getStart(start)));
			restrictions.addCriterion(Restrictions.le("day",DateUtils.getEnd(end)));
		}
		if(StringUtils.isNotEmpty(txtDescEn.getValue())){
			restrictions.addCriterion(Restrictions.ilike("descEn", txtDescEn.getValue(),MatchMode.ANYWHERE));
		}
		return restrictions;
	}

	@Override
	protected void reset() {
		cbYear.setValue(DateUtils.getYear(DateUtils.today()));  
		txtDescEn.setValue("");
	}
	
	

}
