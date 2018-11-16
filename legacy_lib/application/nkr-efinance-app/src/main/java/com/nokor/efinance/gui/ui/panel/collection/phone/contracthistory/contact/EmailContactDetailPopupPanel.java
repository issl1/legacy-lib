package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.CollectionHistory;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class EmailContactDetailPopupPanel extends Window{

	/** */
	private static final long serialVersionUID = -7360544734568909938L;

	private Label lblType;
	private Label lblDate;
	private Label lblHour;
	private Label lblUser;
	private Label lblDepartment;
	private Label lblPersonContact;
	private Label lblEmailContact;
	private Label lblSubject;
	private TextArea txtText;
	
	/**
	 * 
	 */
	public EmailContactDetailPopupPanel() {
		setModal(true);
		setCaption(I18N.message("contact.details"));
		
		lblType = getLabelValue();
		lblDate = getLabelValue();
		lblHour = getLabelValue();
		lblUser = getLabelValue();
		lblDepartment = getLabelValue();
		lblPersonContact = getLabelValue();
		lblSubject = getLabelValue();
		lblEmailContact = getLabelValue();
		
		Label lblTypeTitle = getLabel("type");
		Label lblDateTitle = getLabel("date");
		Label lblHourTitle = getLabel("hour");
		Label lblUserTitle = getLabel("user");
		Label lblDepartmentTitle = getLabel("department");
		Label lblPersonContactTitle = getLabel("person.contact");
		Label lblSubjectTitle = getLabel("subject");
		Label lblEmailContactTitle = getLabel("email.contact");
		
		txtText = ComponentFactory.getTextArea(false, 450, 60);
		
		GridLayout gridLayout = new GridLayout(8, 5);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		int iCol = 0;
		gridLayout.addComponent(lblTypeTitle, iCol++, 0);
		gridLayout.addComponent(lblType, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblDateTitle, iCol++, 0);
		gridLayout.addComponent(lblDate, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblHourTitle, iCol++, 0);
		gridLayout.addComponent(lblHour, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblUserTitle, iCol++, 1);
		gridLayout.addComponent(lblUser, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblDepartmentTitle, iCol++, 1);
		gridLayout.addComponent(lblDepartment, iCol++, 1);
		
		iCol = 0;
		gridLayout.addComponent(lblPersonContactTitle, iCol++, 2);
		gridLayout.addComponent(lblPersonContact, iCol++, 2);
		
		iCol = 0;
		gridLayout.addComponent(lblEmailContactTitle, iCol++, 3);
		gridLayout.addComponent(lblEmailContact, iCol++, 3);
		
		iCol = 0;
		gridLayout.addComponent(lblSubjectTitle, iCol++, 4);
		gridLayout.addComponent(lblSubject, iCol++, 4);
		
		HorizontalLayout horizontalPanel = new HorizontalLayout();
		Label lblText = ComponentFactory.getLabel("text");
		horizontalPanel.setMargin(true);
		horizontalPanel.addComponent(lblText);
		horizontalPanel.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		horizontalPanel.addComponent(txtText);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(gridLayout);
		verticalLayout.addComponent(horizontalPanel);
		
		setContent(verticalLayout);
		
	}
	
	/**
	 * 
	 * @return label
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value != null ? value : "");
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	/**
	 * reset
	 */
	private void reset() {
		lblType.setValue(StringUtils.EMPTY);
		lblDate.setValue(StringUtils.EMPTY);
		lblDepartment.setValue(StringUtils.EMPTY);
		lblHour.setValue(StringUtils.EMPTY);
		lblPersonContact.setValue(StringUtils.EMPTY);
		lblSubject.setValue(StringUtils.EMPTY);
		lblUser.setValue(StringUtils.EMPTY);
		lblEmailContact.setValue(StringUtils.EMPTY);
		txtText.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * assign values
	 */
	public void assignValues(CollectionHistory collectionHistory) {
		reset();
		lblType.setValue(getDescription(collectionHistory.getCallType() != null ? 
				collectionHistory.getCallType().getDescLocale() : StringUtils.EMPTY));
		lblDate.setValue(getDescription(DateUtil.formatDate(collectionHistory.getCreateDate(), "dd/MM/yyyy")));
		
		LocalTime time = new LocalTime(DateUtils.getTimeLabel(collectionHistory.getCreateDate()));    
	    DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm a");
		
		lblHour.setValue(getDescription(fmt.print(time))); 
		lblUser.setValue(getDescription(collectionHistory.getCreateUser()));
		lblDepartment.setValue(getDescription());
		lblPersonContact.setValue(getDescription(collectionHistory.getReachedPerson() != null ? 
				collectionHistory.getReachedPerson().getDescLocale() : StringUtils.EMPTY));
		String detailInfo = StringUtils.EMPTY;
		String cntType = StringUtils.EMPTY;
		if (ETypeContactInfo.MOBILE.equals(collectionHistory.getContactedTypeInfo())) {
			cntType = "(M)";
		} else if (ETypeContactInfo.LANDLINE.equals(collectionHistory.getContactedTypeInfo())) {
			cntType = "(L)";
		} else if (ETypeContactInfo.EMAIL.equals(collectionHistory.getContactedTypeInfo())) {
			cntType = "(E)";
		}
		detailInfo = StringUtils.isNotEmpty(cntType) ? cntType + StringUtils.SPACE + collectionHistory.getContactedInfoValue() :
			collectionHistory.getOtherContact();
		lblEmailContact.setValue(getDescription(detailInfo));
		lblSubject.setValue(getDescription(collectionHistory.getSubject() != null ? 
				collectionHistory.getSubject().getDescLocale() : StringUtils.EMPTY));
		
		
	}
}
