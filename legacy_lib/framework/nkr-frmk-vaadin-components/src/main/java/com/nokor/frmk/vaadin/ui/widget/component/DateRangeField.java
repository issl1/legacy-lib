package com.nokor.frmk.vaadin.ui.widget.component;

import java.util.Date;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;

/**
 * @author sok.vina
 */
public class DateRangeField extends HorizontalLayout {
	
	private static final long serialVersionUID = 8996076137942271578L;
	
	private Button btnDay;
	private Button btnWeek;
	private Button btnMonth;
	private Button btnYear;
	
	/**
	 * @param dfStartDate
	 * @param dfEndDate
	 */
	public DateRangeField(final PopupDateField dfStartDate, final PopupDateField dfEndDate){
		super();
		setSizeFull();
		setSpacing(true);
		
		btnDay = new Button(I18N.message("day"));
		btnDay.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1873448117879919207L;
			@Override
			public void buttonClick(ClickEvent event) {
				dfStartDate.setValue(DateUtils.today());
				dfEndDate.setValue(DateUtils.today());
			}
		});

		btnWeek = new Button(I18N.message("week"));
		btnWeek.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -3165619868469607995L;
			@Override
			public void buttonClick(ClickEvent event) {
				Date[] dateRangeCurrentWeek = DateUtils.getDateRangeCurrentWeek();
				dfStartDate.setValue(dateRangeCurrentWeek[0]);
				dfEndDate.setValue(dateRangeCurrentWeek[1]);
			}
		});

		btnMonth = new Button(I18N.message("month"));
		btnMonth.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8156462936152544000L;
			@Override
			public void buttonClick(ClickEvent event) {
				Date[] dateRangeCurrentMonth = DateUtils.getDateRangeCurrentMonth();
				dfStartDate.setValue(dateRangeCurrentMonth[0]);
				dfEndDate.setValue(dateRangeCurrentMonth[1]);
			}
		});

		btnYear = new Button(I18N.message("year"));
		btnYear.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 5548527550153028282L;
			@Override
			public void buttonClick(ClickEvent event) {
				Date[] dateRangeCurrentYear = DateUtils.getDateRangeCurrentYear();
				dfStartDate.setValue(dateRangeCurrentYear[0]);
				dfEndDate.setValue(dateRangeCurrentYear[1]);
			}
		});
		
		addComponent(btnDay);
		addComponent(btnWeek);
		addComponent(btnMonth);
		addComponent(btnYear);
	}
}
