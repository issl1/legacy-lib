package com.nokor.efinance.gui.ui;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.collection.service.ReminderRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.reminder.CurrentReminderWindowPanel;
import com.nokor.efinance.gui.ui.panel.dashboard.DashboardPanel;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.main.BaseMainUI;
import com.nokor.frmk.vaadin.ui.panel.template.MasterUI;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;

/**
 * Main User Interface
 * @author prasnar
 */
@PreserveOnRefresh
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Theme("efinance")
public class MainUI extends BaseMainUI implements FinServicesHelper, ClickListener {
	/** */
	private static final long serialVersionUID = -35069089364783277L;
	
	private GridLayout infoGridLayout;
	public static MainUI mainUI;

	/**
	 * 
	 */
	public MainUI() {
//		setAfterLoginPanelName(DashboardMainPanel.NAME);
		
		setAfterLoginPanelName(DashboardPanel.NAME);
		mainUI = this;
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.main.BaseMainUI#init(com.vaadin.server.VaadinRequest)
	 */
	@Override
	protected void init(VaadinRequest request) {
		super.init(request);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.main.BaseMainUI#initMasterUI()
	 */
	@Override
	public MasterUI initMasterUI() {
		final Organization mainCom = Organization.getMainOrganization();
		
		return new EfinanceMasterUI(mainCom.getLogoPath(), mainCom.getWebsite()) {
			
			@Override
			public void render(boolean display) {
				super.render(display);
				
				if (display) {
					infoGridLayout = new GridLayout(11, 1);
					infoGridLayout.setStyleName("info-layout");
					infoGridLayout.setSpacing(true);
					infoGridLayout.setMargin(true);
					
					HorizontalLayout infoLayout = getTopInfoLayout();
					if (infoLayout != null) {
						infoLayout.removeAllComponents();
						infoLayout.addComponent(infoGridLayout);
					}
					
					// User Label
					Label userLabel = new Label(I18N.message("user.id"));
					userLabel.setWidth(50, Unit.PIXELS);
					infoGridLayout.addComponent(userLabel, 0, 0);
					
					Label userValueLabel = new Label(SecurityContextHolder.getContext().getAuthentication().getName());
					userValueLabel.setStyleName("label-value");
					userValueLabel.setWidth(120, Unit.PIXELS);
					infoGridLayout.addComponent(userValueLabel, 1, 0);

					// Company Label
					Label companyLabel = new Label(I18N.message("company"));
					companyLabel.setWidth(60, Unit.PIXELS);
					infoGridLayout.addComponent(companyLabel, 2, 0);

					Label companyValueLabel = new Label(mainCom.getName());
					companyValueLabel.setStyleName("label-value");
					companyValueLabel.setWidth(180, Unit.PIXELS);
					infoGridLayout.addComponent(companyValueLabel, 3, 0);

					// Branch Label
					Label branchLabel = new Label(I18N.message("branch"));
					branchLabel.setWidth(50, Unit.PIXELS);
					infoGridLayout.addComponent(branchLabel, 4, 0);

					Label branchValueLabel = new Label("");
					branchValueLabel.setStyleName("label-value");
					branchValueLabel.setWidth(180, Unit.PIXELS);
					infoGridLayout.addComponent(branchValueLabel, 5, 0);

					// Date Label
					Label dateLabel = new Label(I18N.message("date"));
					dateLabel.setWidth(50, Unit.PIXELS);
					infoGridLayout.addComponent(dateLabel, 6, 0);
					
					Label dateValueLabel = new Label(DateUtils.date2StringDDMMYYYY_SLASH(new Date()));
					dateValueLabel.setStyleName("label-value");
					infoGridLayout.addComponent(dateValueLabel, 7, 0);
					
					// Time Label
					Label timeLabel = new Label(I18N.message("time"));
					timeLabel.setWidth(50, Unit.PIXELS);
					infoGridLayout.addComponent(timeLabel, 8, 0);
					
					Label timeValueLabel = new Label(DateUtils.date2String(new Date(), "hh:mm"));
					timeValueLabel.setStyleName("label-value");
					infoGridLayout.addComponent(timeValueLabel, 9, 0);
					
					createReminederButton();
				}
			}
		};
	}
	
	/**
	 * 
	 */
	public void createReminederButton() {
		long countReminders = (long) REMINDER_SRV.count(getReminderRestrictions());
		Button btnReminder = new NativeButton("<b style=\"background-color:red; border-radius: 5px; position: absolute; padding: 2px; right: -2px;"
					+ "top: -10px;\">" + String.valueOf(countReminders) + "</b>", this);
				btnReminder.addStyleName("btn btn-success btn-icon-padding button-small");
				btnReminder.setIcon(FontAwesome.BELL_O);
				btnReminder.setCaptionAsHtml(true);
				btnReminder.setWidth("35px");
				btnReminder.setHeight("22px");
				btnReminder.addClickListener(this);
				infoGridLayout.removeComponent(10, 0);
				infoGridLayout.addComponent(btnReminder, 10, 0);
	}
	
	/**
	 * get all remineder
	 * @return
	 */
	public ReminderRestriction getReminderRestrictions() {
		SecUser currentUser = UserSessionManager.getCurrentUser();		
		ReminderRestriction restrictions = new ReminderRestriction();
		restrictions.addCriterion(Restrictions.eq("secUser", currentUser));
		return restrictions;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		List<Reminder> reminders = REMINDER_SRV.list(getReminderRestrictions());
		CurrentReminderWindowPanel windowPanel = CurrentReminderWindowPanel.show(reminders, this);
		UI.getCurrent().addWindow(windowPanel);
	}
	
}