package com.nokor.efinance.gui.ui.panel.collection.staffphone.locksplit;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;


/**
 * 
 * @author uhout.cheng
 */
public class ColLockSplitTopPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 8216213234445383226L;

	private TextField txtLockSplitRef;
	private AutoDateField dfDueDateFrom;
	private AutoDateField dfDueDateTo;
	private CheckBox cbToday;
	private CheckBox cbAuto;
	private TextArea txtRemark;
	
	private ColLockSplitChannelPanel channelPanel;
	
	private LockSplit lockSplit;
	
	/**
	 * 
	 */
	public ColLockSplitTopPanel() {
		txtLockSplitRef = ComponentFactory.getTextField(30, 120);
		txtLockSplitRef.setEnabled(false);
		dfDueDateFrom = ComponentFactory.getAutoDateField();
		dfDueDateTo = ComponentFactory.getAutoDateField();
		cbToday = new CheckBox(I18N.message("today"));
		cbAuto = new CheckBox(I18N.message("auto"));
		txtRemark = ComponentFactory.getTextArea(false, 200, 50);
		
		channelPanel = new ColLockSplitChannelPanel();
		
		dfDueDateFrom.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 6949307687281165162L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (dfDueDateFrom.getValue() != null) {
					if (cbAuto.getValue()) {
						dfDueDateTo.setValue(DateUtils.addDaysDate(dfDueDateFrom.getValue(), 30));
					}
				}
			}
		});
		
		dfDueDateTo.addValueChangeListener(new ValueChangeListener() {
		
			/** */
			private static final long serialVersionUID = -3854542954898812472L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (dfDueDateTo.getValue() != null) {
					if (cbAuto.getValue()) {
						dfDueDateTo.setValue(DateUtils.addDaysDate(dfDueDateFrom.getValue(), 30));
					}
				}
			}
		});
		
		cbToday.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 6949307687281165162L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				dfDueDateFrom.setEnabled(!cbToday.getValue());
				if (cbToday.getValue()) {
					dfDueDateFrom.setValue(DateUtils.today());
					if (cbAuto.getValue()) {
						dfDueDateTo.setValue(DateUtils.addDaysDate(dfDueDateFrom.getValue(), 30));
					}
				} else {
					dfDueDateFrom.setValue(null);
					cbAuto.setValue(false);
				}
			}
		});
		
		cbAuto.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -3854542954898812472L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				dfDueDateTo.setEnabled(!cbAuto.getValue());
				if (cbAuto.getValue()) {
					if (dfDueDateFrom.getValue() != null) {
						Date dueDateTo = DateUtils.addDaysDate(dfDueDateFrom.getValue(), 30);
						dfDueDateTo.setValue(dueDateTo);
					}
				} else {
					dfDueDateTo.setValue(null);
				}
			}
		});
		
		Label lblLockSplitRefTitle = ComponentLayoutFactory.getLabelCaption("id");
		Label lblDueDateFromTitle = ComponentLayoutFactory.getLabelCaption("due.date.from");
		Label lblDueDateToTitle = ComponentLayoutFactory.getLabelCaption("to");
		Label lblRemarkTitle = ComponentLayoutFactory.getLabelCaption("remark");
		
		GridLayout gridLayout = new GridLayout(20, 1);
		int iCol = 0;
		gridLayout.addComponent(lblLockSplitRefTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(txtLockSplitRef, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblDueDateFromTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(dfDueDateFrom, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(cbToday, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblDueDateToTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(dfDueDateTo, iCol++, 0);
		gridLayout.addComponent(cbAuto, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblRemarkTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(txtRemark, iCol++, 0);
		
		setSpacing(true);
		addComponent(gridLayout);
		addComponent(channelPanel);
	}
	
	/**
	 * 
	 * @param lockSplit
	 */
	public void assignValues(LockSplit lockSplit) {
		reset();
		this.lockSplit = lockSplit;
		txtLockSplitRef.setValue(getDefaultString(lockSplit.getReference()));
		dfDueDateFrom.setValue(lockSplit.getFrom());
		dfDueDateTo.setValue(lockSplit.getTo());
		txtRemark.setValue(getDefaultString(lockSplit.getComment()));
		channelPanel.assignValues(lockSplit);
	}
	
	/**
	 * 
	 * @return
	 */
	public LockSplit getLockSplit() {
		this.lockSplit.setFrom(dfDueDateFrom.getValue());
		this.lockSplit.setTo(dfDueDateTo.getValue());
		this.lockSplit.setPaymentChannel(channelPanel.getLockSplit().getPaymentChannel());
		this.lockSplit.setDealer(channelPanel.getLockSplit().getDealer());
		this.lockSplit.setComment(txtRemark.getValue());
		return this.lockSplit;
	}
	
	/**
	 * Error if date from bigger than date to
	 * @return
	 */
	public String validated() {
		Date from = DateUtils.getDateAtBeginningOfDay(dfDueDateFrom.getValue());
		Date to = DateUtils.getDateAtBeginningOfDay(dfDueDateTo.getValue());
		if (from != null && to != null) {
			if (from.before(DateUtils.getDateAtBeginningOfDay(DateUtils.today()))) {
				if (this.lockSplit.getId() == null) {
					return I18N.message("due.date.from.less.than.today", 
							new String[] {DateUtils.getDateLabel(DateUtils.today(), DateUtils.FORMAT_DDMMYYYY_SLASH)});
				}
			} else if (to.before(DateUtils.getDateAtBeginningOfDay(DateUtils.today()))) {
				if (this.lockSplit.getId() == null) {
					return I18N.message("due.date.to.less.than.today", 
							new String[] {DateUtils.getDateLabel(DateUtils.today(), DateUtils.FORMAT_DDMMYYYY_SLASH)});
				}
			} else if (from.after(to)) {
				return I18N.message("due.date.from.less.than.due.date.to");
			}
		} else {
			if (from == null) {
				return I18N.message("field.required.1", new String[] {I18N.message("due.date.from")});
			} else if (to == null) {
				return I18N.message("field.required.1", new String[] {I18N.message("due.date.to")});
			}			
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 */
	protected void reset() {
		txtLockSplitRef.setValue(StringUtils.EMPTY);
		dfDueDateFrom.setValue(null);
		dfDueDateTo.setValue(null);
		cbToday.setValue(false);
		cbAuto.setValue(false);
		txtRemark.setValue(StringUtils.EMPTY);
	}
}
