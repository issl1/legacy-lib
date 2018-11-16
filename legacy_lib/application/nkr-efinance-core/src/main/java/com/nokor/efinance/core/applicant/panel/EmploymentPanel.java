package com.nokor.efinance.core.applicant.panel;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.CrudAction;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Employment panel
 * @author ly.youhort
 */
public class EmploymentPanel extends AbstractControlPanel {
	
	private static final long serialVersionUID = 8485010240363814186L;

	private CurrentEmploymentPanel currentEmploymentPanel;
	private SecondaryEmploymentPanel secondaryEmploymentPanel1;
	private SecondaryEmploymentPanel secondaryEmploymentPanel2;
	
	public EmploymentPanel() {
		setSizeFull();
		setMargin(true);
		
        currentEmploymentPanel = new CurrentEmploymentPanel();
        secondaryEmploymentPanel1 = new SecondaryEmploymentPanel();
        secondaryEmploymentPanel2 = new SecondaryEmploymentPanel();
        
        final Panel currentEmploymentLayout = new Panel(I18N.message("current.employment"));
		currentEmploymentLayout.setContent(currentEmploymentPanel);        
		
		final HorizontalLayout secondaryEmploymentLayout = new HorizontalLayout();
		secondaryEmploymentLayout.setSpacing(true);
		
		final Panel secondaryEmploymentLayout1 = new Panel(I18N.message("secondary.employment") + " 1");
		secondaryEmploymentLayout1.setContent(secondaryEmploymentPanel1);
        
		final Panel secondaryEmploymentLayout2 = new Panel(I18N.message("secondary.employment") + " 2");
		secondaryEmploymentLayout2.setContent(secondaryEmploymentPanel2);
		
		secondaryEmploymentLayout.addComponent(secondaryEmploymentLayout1);
		secondaryEmploymentLayout.addComponent(secondaryEmploymentLayout2);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(currentEmploymentLayout);
		verticalLayout.addComponent(secondaryEmploymentLayout);
		
		addComponent(verticalLayout);
	}
	
	/**
	 * Set applicant
	 * @param applicant
	 */
	public void assignValues(Applicant applicant) {		
		Employment currentEmployment = applicant.getIndividual().getCurrentEmployment();
		currentEmploymentPanel.setApplicant(applicant, EApplicantType.C);
		if (currentEmployment != null) {
			currentEmploymentPanel.assignValues(currentEmployment);
		} else {
			currentEmploymentPanel.reset();
		}
		
		List<Employment> secondaryEmployments = applicant.getIndividual().getEmployments(EEmploymentType.SECO);
		if (secondaryEmployments != null && !secondaryEmployments.isEmpty()) {
			secondaryEmploymentPanel1.assignValues(secondaryEmployments.get(0));
			if (secondaryEmployments.size() > 1) {
				secondaryEmploymentPanel2.assignValues(secondaryEmployments.get(1));
			} else {
				secondaryEmploymentPanel2.reset();
			}
		} else {
			secondaryEmploymentPanel1.reset();
			secondaryEmploymentPanel2.reset();
		}
		
	}
	
	/**
	 * Get employment
	 * @param applicant
	 * @return
	 */
	public Applicant getEmployments(Applicant applicant) {
		Employment currentEmployment = applicant.getIndividual().getCurrentEmployment();
		if (currentEmployment != null) {
			currentEmploymentPanel.getEmployment(currentEmployment);
		} else {
			currentEmployment = currentEmploymentPanel.getEmployment(new Employment());
			if (currentEmployment.isPersistent()) {
				applicant.getIndividual().addEmployment(currentEmployment);
			}
		}
		List<Employment> secondaryEmployments = applicant.getIndividual().getEmployments(EEmploymentType.SECO);
		if (secondaryEmployments != null && !secondaryEmployments.isEmpty()) {
			Employment secondaryEmployment1 = secondaryEmploymentPanel1.getEmployment(secondaryEmployments.get(0));
			if (secondaryEmployment1 == null) {
				secondaryEmployments.get(0).setCrudAction(CrudAction.DELETE);
			}
		} else {
			Employment prevEmployment1 = secondaryEmploymentPanel1.getEmployment(new Employment());
			if (prevEmployment1 != null) {
				applicant.getIndividual().addEmployment(prevEmployment1);
			}
		}
		
		if (secondaryEmployments != null && secondaryEmployments.size() > 1) {
			Employment secondaryEmployment2 = secondaryEmploymentPanel2.getEmployment(secondaryEmployments.get(1));
			if (secondaryEmployment2 == null) {
				secondaryEmployments.get(1).setCrudAction(CrudAction.DELETE);
			}
		} else {
			Employment secondaryEmployment2 = secondaryEmploymentPanel2.getEmployment(new Employment());
			if (secondaryEmployment2 != null) {
				applicant.getIndividual().addEmployment(secondaryEmployment2);
			}
		}
		
		return applicant;
	}
	
	/**
	 * @return
	 */
	public List<String> fullValidate() {
		super.reset();
		errors.addAll(currentEmploymentPanel.fullValidate());
		return errors;
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		currentEmploymentPanel.reset();
		secondaryEmploymentPanel1.reset();
		secondaryEmploymentPanel2.reset();
	}
}
