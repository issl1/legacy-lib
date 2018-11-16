package com.nokor.ersys.vaadin.ui.security.secuser.detail.profilegroup;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

public class SecProfilePanel extends VerticalLayout implements VaadinServicesHelper {

	/** */
	private static final long serialVersionUID = 4416839108984402529L;
	
	private SecProfile secProfile;
	private CheckBox cbProfile;
	
	public SecProfilePanel() {
		
	}
	
	public boolean isChecked() {
		return cbProfile.getValue();
	}
	
	public void setChecked(boolean checked) {
		cbProfile.setValue(checked);
	}
	
	public SecProfile getSecProfile() {
		return secProfile;
	}
	
	public void renderView(final SecProfile secProfile) {
		this.secProfile = secProfile;
		List<SecApplication> applications = secProfile.getApplications();

		final GridLayout applicationGridLayout = new GridLayout(2, applications.size() + 1);
		applicationGridLayout.setWidth(100,Unit.PIXELS);
		applicationGridLayout.setSpacing(true);
		applicationGridLayout.setMargin(true);
		
		Label lblComponent = ComponentFactory.getHtmlLabel("<b>" + I18N.message("application") + "</b>");
		Label lblView = ComponentFactory.getHtmlLabel("");
		applicationGridLayout.addComponent(lblComponent, 0, 0);
		applicationGridLayout.addComponent(lblView, 1, 0);

		int index = 0;    			

		for (final SecApplication application : applications) {
			Label lblControl = ComponentFactory.getLabel(application.getDesc());
			lblControl.setWidth(300, Unit.PIXELS);
			Button viewLink = null;
			
			if (!secProfile.getId().equals(SecProfile.ADMIN.getId())) {
				viewLink = new Button(I18N.message("view"));
				viewLink.addStyleName(BaseTheme.BUTTON_LINK);
				viewLink.addClickListener(new ClickListener() {
					
					/** */
					private static final long serialVersionUID = 843333953121871283L;

					@Override
					public void buttonClick(ClickEvent event) {
						SecProfileAppFormPopupPanel dialog = new SecProfileAppFormPopupPanel();
						dialog.renderView(application);
						dialog.setSelectByProfile(secProfile);
						UI.getCurrent().addWindow(dialog);
					}
				});
			}
			index++;
			applicationGridLayout.addComponent(lblControl, 0, index);
			
			if (viewLink != null) {
				applicationGridLayout.addComponent(viewLink, 1, index);
			}
		}
		
		cbProfile = new CheckBox();
		cbProfile.addStyleName("margin-left66");
		cbProfile.setValue(false);
		cbProfile.setCaption(secProfile.getDescEn());
    	
    	final VerticalLayout applicationLayout = new VerticalLayout();
    	applicationLayout.setMargin(true);
    	applicationLayout.addComponent(cbProfile);
    	applicationLayout.addComponent(new FormLayout(applicationGridLayout));
    	addComponent(applicationLayout);
	}
}
