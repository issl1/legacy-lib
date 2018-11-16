package com.nokor.efinance.core.applicant.panel.applicant.individual;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * Reference panel
 * @author ly.youhort
 */
public class ContactReferencePanel extends Panel {
	
	private static final long serialVersionUID = 966783199811064295L;
		
	// Reference layout
	private TextField txtReferenceType;
	private TextField txtRelationship;
	private TextField txtReferenceName;
	private TextField txtReferencePhoneType;
	private TextField txtReferencePhoneNumber;
	private Button btnFullDetail;
	
	/**
	 * 
	 */
	public ContactReferencePanel() {
		createForm();
	}
	
	/**
	 * 
	 * @return
	 */
	private TextField getTextField(String caption) {
		TextField txtField = ComponentFactory.getTextField(caption, false, 60, 150);
		txtField.setEnabled(false);
		return txtField;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Button getButton(String caption) {
		Button btnButton = ComponentFactory.getButton(caption);
		btnButton.setStyleName(Reindeer.BUTTON_LINK);
		return btnButton;
	} 
	
	/** */
	private void createForm() {
		txtReferenceType = getTextField(null);
		txtRelationship = getTextField(null);
		txtReferenceName = getTextField(null);
		txtReferencePhoneType = getTextField(null);
		txtReferencePhoneNumber = getTextField(null);
		
		btnFullDetail = getButton("full.detail");
		
		String OPEN_TABLE = "<table cellspacing=\"1\" cellpadding=\"1\" style=\"border:0\" >";
		String OPEN_TR = "<tr>";
		String OPEN_TD = "<td align=\"left\" >";
		String CLOSE_TR = "</tr>";
		String CLOSE_TD = "</td>";
		String CLOSE_TABLE = "</table>";
		
		CustomLayout cusLayout = new CustomLayout("xxx");
		cusLayout.addStyleName("overflow-layout-style");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		template += "<td align=\"left\" width=\"140\" >";
		template += "<div location =\"lblReferenceType\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtReferenceType\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblRelationship\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtRelationship\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblReferenceName\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtReferenceName\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblReferencePhone\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtReferencePhoneType\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtReferencePhoneNumber\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		
		cusLayout.addComponent(new Label(I18N.message("reference.type")), "lblReferenceType");
		cusLayout.addComponent(new Label(I18N.message("reference.relationship")), "lblRelationship");
		cusLayout.addComponent(new Label(I18N.message("reference.name")), "lblReferenceName");
		cusLayout.addComponent(new Label(I18N.message("reference.phone")), "lblReferencePhone");
		cusLayout.addComponent(txtReferenceType, "txtReferenceType");
		cusLayout.addComponent(txtRelationship, "txtRelationship");
		cusLayout.addComponent(txtReferenceName, "txtReferenceName");
		cusLayout.addComponent(txtReferencePhoneType, "txtReferencePhoneType");
		cusLayout.addComponent(txtReferencePhoneNumber, "txtReferencePhoneNumber");
		
		template += CLOSE_TABLE;
		cusLayout.setTemplateContents(template);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponent(cusLayout);
		layout.addComponent(btnFullDetail);
		layout.setComponentAlignment(btnFullDetail, Alignment.BOTTOM_RIGHT);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.addComponent(layout);
		
		setCaption("<h2 style=\"border:1px solid #E3E3E3;padding:9px;border-radius:3px;margin:0;"
				+ "background-color:#F5F5F5;\" align=\"center\" >" + I18N.message("reference") + "</h2>");
		setCaptionAsHtml(true);
		setContent(horizontalLayout);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Applicant applicant) {	
		if (applicant != null) {
			List<IndividualReferenceInfo> individualReferenceInfos = applicant.getIndividual().getIndividualReferenceInfos();
			if (individualReferenceInfos != null && !individualReferenceInfos.isEmpty()) {
				for (IndividualReferenceInfo individualReferenceInfo : individualReferenceInfos) {
					if (individualReferenceInfo != null) {
						txtReferenceType.setValue(individualReferenceInfo.getReferenceType() != null ? individualReferenceInfo.getReferenceType().getDescEn() : "");
						txtRelationship.setValue(individualReferenceInfo.getRelationship() != null ? 
								individualReferenceInfo.getRelationship().getDescEn() : "");
						txtReferenceName.setValue(individualReferenceInfo.getLastNameEn() + " " + individualReferenceInfo.getFirstNameEn());
						List<IndividualReferenceContactInfo> list = individualReferenceInfo.getIndividualReferenceContactInfos();
						if (list != null && !list.isEmpty()) {
							IndividualReferenceContactInfo contactInfo = list.get(0);
							ContactInfo info = contactInfo.getContactInfo();
							if (info != null) {
								txtReferencePhoneType.setValue(info.getTypeInfo() != null ? info.getTypeInfo().getDescEn() : "");
								txtReferencePhoneNumber.setValue(info.getValue());
							}
						}
					}
				}
			}
		}
	}

	
	/**
	 * Reset panel
	 */
	public void reset() {
		txtReferenceType.setValue("");
		txtRelationship.setValue("");
		txtReferenceName.setValue("");
		txtReferencePhoneType.setValue("");
		txtReferencePhoneNumber.setValue("");
	}

	/**
	 * @return the btnFullDetail
	 */
	public Button getBtnFullDetail() {
		return btnFullDetail;
	}

	/**
	 * @param btnFullDetail the btnFullDetail to set
	 */
	public void setBtnFullDetail(Button btnFullDetail) {
		this.btnFullDetail = btnFullDetail;
	}
}
