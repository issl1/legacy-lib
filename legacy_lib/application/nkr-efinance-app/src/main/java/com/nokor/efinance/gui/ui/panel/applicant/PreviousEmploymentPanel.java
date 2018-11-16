package com.nokor.efinance.gui.ui.panel.applicant;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.ersys.core.hr.model.eref.EEmploymentStatus;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Previous employment
 * @author ly.youhort
 */
public class PreviousEmploymentPanel extends AbstractControlPanel {

	private static final long serialVersionUID = 2542460101873663919L;
	
	private TextField txtPosition;
	private TextField txtEmployerName;
	private TextField txtRevenue;
	private TextField txtBusinessExpense;
	private ERefDataComboBox<EEmploymentStatus> cbxEmploymentStatus;
	
		
	public PreviousEmploymentPanel() {
		txtPosition = ComponentFactory.getTextField("position", false, 150, 300);
		txtRevenue = ComponentFactory.getTextField("revenue", false, 50, 150);
		txtBusinessExpense = ComponentFactory.getTextField("business.expense", false, 50, 150);
		txtEmployerName = ComponentFactory.getTextField("employer", false, 150, 300);
		cbxEmploymentStatus = new ERefDataComboBox<EEmploymentStatus>(I18N.message("employment.status"), EEmploymentStatus.class);
		
		final FormLayout formDetailPanel = new FormLayout();
        formDetailPanel.setMargin(true);
        formDetailPanel.addComponent(txtPosition);
        formDetailPanel.addComponent(cbxEmploymentStatus);
        formDetailPanel.addComponent(txtRevenue);
        formDetailPanel.addComponent(txtBusinessExpense);
        formDetailPanel.addComponent(txtEmployerName);
		        
        addComponent(formDetailPanel);
	}
	
	/**
	 * Set applicant
	 * @param applicant
	 */
	public void assignValues(Employment employment) {
		txtPosition.setValue(getDefaultString(employment.getPosition()));
		txtRevenue.setValue(getDefaultString(employment.getRevenue()));
		txtBusinessExpense.setValue(getDefaultString(employment.getBusinessExpense()));
		txtEmployerName.setValue(getDefaultString(employment.getEmployerName()));
		cbxEmploymentStatus.setSelectedEntity(employment.getEmploymentStatus());
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
				|| StringUtils.isNotEmpty(txtEmployerName.getValue())
				|| cbxEmploymentStatus.getSelectedEntity() != null) {				
			employment.setPosition(txtPosition.getValue());
			employment.setRevenue(getDouble(txtRevenue));
			employment.setBusinessExpense(getDouble(txtBusinessExpense));
			employment.setEmployerName(txtEmployerName.getValue());
			employment.setEmploymentStatus(cbxEmploymentStatus.getSelectedEntity());
			employment.setEmploymentType(EEmploymentType.PREV);
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
