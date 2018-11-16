package com.nokor.efinance.gui.ui.panel.inbox.collection;

import java.util.function.Function;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColResult;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public abstract class AbstractColContractFilterPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8012330835316045697L;
	
	protected Button btnSearch;
	protected Button btnReset;
	
	protected TextField txtFullName;
	protected TextField txtContractId;
	protected TextField txtPhone;
	
	protected TextField txtDueDayFrom;
	protected TextField txtDueDayTo;
	
	protected AutoDateField dfLPDFrom;
	protected AutoDateField dfLPDTo;
	
	protected TextField txtCollectedFrom;
	protected TextField txtCollectedTo;
	
	protected EntityRefComboBox<EColResult> cbxColResutl;
	protected AutoDateField dfPromisedDateFrom;
	protected AutoDateField dfPromisedDateTo;
	
	protected TextField txtTotalDueFrom;
	protected TextField txtTotalDueTo;
	
	public AbstractColContractFilterPanel() {
		setMargin(true);
		
		txtFullName = ComponentFactory.getTextField(60, 125);
		txtContractId = ComponentFactory.getTextField(60, 125);
		txtPhone = ComponentFactory.getTextField(60, 125);
		
		txtDueDayFrom = ComponentFactory.getTextField(60, 125);
		txtDueDayTo = ComponentFactory.getTextField(60, 125);
		
		dfLPDFrom = ComponentFactory.getAutoDateField();
		dfLPDTo = ComponentFactory.getAutoDateField();
		txtCollectedFrom = ComponentFactory.getTextField(60, 125);
		txtCollectedTo = ComponentFactory.getTextField(60, 125);
		
		cbxColResutl = new EntityRefComboBox<EColResult>();
		cbxColResutl.setRestrictions(getResults(EColType.PHONE));
		cbxColResutl.getRestrictions().addOrder(Order.asc("descEn"));
		cbxColResutl.setCaptionRenderer(new Function<EColResult, String>() {
			/**
			 * @see java.util.function.Function#apply(java.lang.Object)
			 */
			@Override
			public String apply(EColResult t) {
				return t.getCode() + " - " + (I18N.isEnglishLocale() ? t.getDescEn() : t.getDesc());
			}
		});
		cbxColResutl.renderer();
		cbxColResutl.setWidth(130, Unit.PIXELS);
		
		dfPromisedDateFrom = ComponentFactory.getAutoDateField();
		dfPromisedDateTo = ComponentFactory.getAutoDateField();
		txtTotalDueFrom = ComponentFactory.getTextField(60, 125);
		txtTotalDueTo = ComponentFactory.getTextField(60, 110);
		
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnReset = ComponentLayoutFactory.getButtonReset();
		btnReset.addClickListener(new ClickListener() {
			
			private static final long serialVersionUID = 5792183403621943108L;

			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		
		HorizontalLayout buttonaLayout = new HorizontalLayout();
		buttonaLayout.setSpacing(true);
		buttonaLayout.addComponent(btnSearch);
		buttonaLayout.addComponent(btnReset);
		
		GridLayout filterLayout = createFilterLayout();
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.addComponent(filterLayout);
		mainLayout.addComponent(buttonaLayout);
		mainLayout.setComponentAlignment(buttonaLayout, Alignment.BOTTOM_CENTER);
		mainLayout.setComponentAlignment(filterLayout, Alignment.TOP_CENTER);
		
		addComponent(mainLayout);
	}
	
	/**
	 * Reset
	 */
	private void reset() {
		txtFullName.setValue("");
		txtContractId.setValue("");
		txtPhone.setValue("");
		txtDueDayFrom.setValue("");
		txtDueDayTo.setValue("");
		dfLPDFrom.setValue(null);
		dfLPDTo.setValue(null);
		txtCollectedFrom.setValue("");
		txtCollectedTo.setValue("");
		cbxColResutl.setSelectedEntity(null);
		dfPromisedDateFrom.setValue(null);
		dfPromisedDateTo.setValue(null);
		txtTotalDueFrom.setValue("");
		txtTotalDueTo.setValue("");
	}
	
	/**
	 * 
	 * @return
	 */
	private GridLayout createFilterLayout() {
		GridLayout gridLayout = new GridLayout(8, 5);
		gridLayout.setSpacing(true);
		
		Label lblFullName = ComponentLayoutFactory.getLabelCaption("fullname");
		Label lblDueDayFrom = ComponentLayoutFactory.getLabelCaption("due.day.from");
		Label lblDueDayTo = ComponentLayoutFactory.getLabelCaption("to");
		Label lblLastResult = ComponentLayoutFactory.getLabelCaption("last.result");
		Label lblContractId = ComponentLayoutFactory.getLabelCaption("contract.id");
		Label lblLPDFrom = ComponentLayoutFactory.getLabelCaption("lpd.from");
		Label lblLPDTo = ComponentLayoutFactory.getLabelCaption("to");
		Label lblPromisedDateFrom = ComponentLayoutFactory.getLabelCaption("promised.date.from");
		Label lblPromisedDateTo = ComponentLayoutFactory.getLabelCaption("to");
		Label lblPhone = ComponentLayoutFactory.getLabelCaption("phone");
		Label lblCollectedFrom = ComponentLayoutFactory.getLabelCaption("collected.from");
		Label lblCollectedTo = ComponentLayoutFactory.getLabelCaption("to");
		Label lblTotalDueFrom = ComponentLayoutFactory.getLabelCaption("total.due.from");
		Label lblTotalDueTo = ComponentLayoutFactory.getLabelCaption("to");
		
		int iCol = 0;
		gridLayout.addComponent(lblFullName, iCol++, 0);
		gridLayout.addComponent(txtFullName, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblDueDayFrom, iCol++, 0);
		gridLayout.addComponent(txtDueDayFrom, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblDueDayTo, iCol++, 0);
		gridLayout.addComponent(txtDueDayTo, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblContractId, iCol++, 1);
		gridLayout.addComponent(txtContractId, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblLPDFrom, iCol++, 1);
		gridLayout.addComponent(dfLPDFrom, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblLPDTo, iCol++, 1);
		gridLayout.addComponent(dfLPDTo, iCol++, 1);
		
		iCol = 0;
		gridLayout.addComponent(lblPhone, iCol++, 2);
		gridLayout.addComponent(txtPhone, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(lblCollectedFrom, iCol++, 2);
		gridLayout.addComponent(txtCollectedFrom, iCol++, 2);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 2);
		gridLayout.addComponent(lblCollectedTo, iCol++, 2);
		gridLayout.addComponent(txtCollectedTo, iCol++, 2);
		
		iCol = 0;
		gridLayout.addComponent(lblLastResult, iCol++, 3);
		gridLayout.addComponent(cbxColResutl, iCol++, 3);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(30, Unit.PIXELS), iCol++, 3);
		gridLayout.addComponent(lblPromisedDateFrom, iCol++, 3);
		gridLayout.addComponent(dfPromisedDateFrom, iCol++, 3);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 3);
		gridLayout.addComponent(lblPromisedDateTo, iCol++, 3);
		gridLayout.addComponent(dfPromisedDateTo, iCol++, 3);
		
		iCol = 0;
		gridLayout.addComponent(lblTotalDueFrom, iCol++, 4);
		gridLayout.addComponent(txtTotalDueFrom, iCol++, 4);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS), iCol++, 4);
		gridLayout.addComponent(lblTotalDueTo, iCol++, 4);
		gridLayout.addComponent(txtTotalDueTo, iCol++, 4);
	
		return gridLayout;
	}
	
	/**
	 * Get ColResut to combo box by colType
	 * @param colType
	 * @return
	 */
	private BaseRestrictions<EColResult> getResults(EColType colType) {
		BaseRestrictions<EColResult> restrictions = new BaseRestrictions<>(EColResult.class);
		restrictions.addCriterion(Restrictions.eq("colTypes", colType));
		return restrictions;
	}
	
	public abstract BaseRestrictions<ContractUserInbox> getRestrictions();

}
