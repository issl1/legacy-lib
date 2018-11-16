package com.nokor.efinance.core.applicant.panel;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustry;
import com.nokor.ersys.core.hr.model.eref.EEmploymentStatus;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

/**
 * Secondary employment
 * @author ly.youhort
 */
public class SecondaryEmploymentPanel extends AbstractControlPanel {

	private static final long serialVersionUID = 2542460101873663919L;
	
	private TextField txtPosition;
	private TextField txtNoMonthInYear;
	private TextField txtRevenue;
	private TextField txtBusinessExpense;
	private ERefDataComboBox<EEmploymentStatus> cbxEmploymentStatus;
	private ERefDataComboBox<EEmploymentIndustry> cbxEmploymentIndustry;
	
	public SecondaryEmploymentPanel() {
		this("secondaryEmployment");
	}
	public SecondaryEmploymentPanel(String template) {
		
		txtPosition = ComponentFactory.getTextField(false, 150, 250);
		txtRevenue = ComponentFactory.getTextField(false, 50, 150);
		txtNoMonthInYear = ComponentFactory.getTextField(false, 50, 150);
		txtBusinessExpense = ComponentFactory.getTextField(false, 50, 150);
		cbxEmploymentStatus = new ERefDataComboBox<EEmploymentStatus>(EEmploymentStatus.class);
		cbxEmploymentIndustry = new ERefDataComboBox<EEmploymentIndustry>(EEmploymentIndustry.class);
		
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(new Label(I18N.message("per.month")), "lblPerMonth");
		customLayout.addComponent(new Label(I18N.message("per.12")), "lblPer12");
		customLayout.addComponent(new Label(I18N.message("position")), "lblPosition");
		customLayout.addComponent(txtPosition, "txtPosition");
		customLayout.addComponent(new Label(I18N.message("no.month.in.year")), "lblNoMonthInYear");
		customLayout.addComponent(txtNoMonthInYear, "txtNoMonthInYear");
		customLayout.addComponent(new Label(I18N.message("revenue")), "lblRevenue");
		customLayout.addComponent(txtRevenue, "txtRevenue");
		customLayout.addComponent(new Label(I18N.message("business.expense")), "lblBusinessExpense");
		customLayout.addComponent(txtBusinessExpense, "txtBusinessExpense");
		customLayout.addComponent(new Label(I18N.message("employment.status")), "lblEmploymentStatus");
		customLayout.addComponent(cbxEmploymentStatus, "cbxEmploymentStatus");
		customLayout.addComponent(new Label(I18N.message("employment.industry")), "lblEmploymentIndustry");
		customLayout.addComponent(cbxEmploymentIndustry, "cbxEmploymentIndustry");
        addComponent(customLayout);
	}
	
	/**
	 * Set applicant
	 * @param applicant
	 */
	public void assignValues(Employment employment) {
		txtPosition.setValue(getDefaultString(employment.getPosition()));
		txtRevenue.setValue(AmountUtils.format(employment.getRevenue()));
		txtBusinessExpense.setValue(AmountUtils.format(employment.getBusinessExpense()));
		cbxEmploymentStatus.setSelectedEntity(employment.getEmploymentStatus());
		cbxEmploymentIndustry.setSelectedEntity(employment.getEmploymentIndustry());
		txtNoMonthInYear.setValue(getDefaultString(employment.getNoMonthInYear()));
	}
	
	/**
	 * Get employment
	 * @param employment
	 * @return
	 */
	public Employment getEmployment(Employment employment) {		
		if (StringUtils.isNotEmpty(txtPosition.getValue())
				|| StringUtils.isNotEmpty(txtRevenue.getValue())
				|| StringUtils.isNotEmpty(txtBusinessExpense.getValue()) 
				|| cbxEmploymentStatus.getSelectedEntity() != null) {				
			employment.setPosition(txtPosition.getValue());
			employment.setRevenue(getDouble(txtRevenue));
			employment.setBusinessExpense(getDouble(txtBusinessExpense));
			employment.setEmploymentStatus(cbxEmploymentStatus.getSelectedEntity());
			employment.setEmploymentIndustry(cbxEmploymentIndustry.getSelectedEntity());
			employment.setEmploymentType(EEmploymentType.SECO);
			employment.setNoMonthInYear(getInteger(txtNoMonthInYear));
			return employment;
		}
		return null;
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		/*txtPosition.setValue("");
		txtRevenue.setValue("");
		txtBusinessExpense.setValue("");
		txtEmployerName.setValue("");
		cbxEmploymentStatus.setSelectedEntity(null);*/
		assignValues(new Employment());
	}
}
