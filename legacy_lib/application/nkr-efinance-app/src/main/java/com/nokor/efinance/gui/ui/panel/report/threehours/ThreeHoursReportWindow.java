package com.nokor.efinance.gui.ui.panel.report.threehours;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.glf.statistic.model.Statistic3HoursVisitor;
import com.nokor.efinance.gui.ui.panel.dashboard.DashboardPopupTablePanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ThreeHoursReportWindow extends Window {

	/** */
	private static final long serialVersionUID = 1L;
	
	private DashboardPopupTablePanel dashboardPopupTablePanel;
	private Dealer dealer;
	private Button btnSave;
	private Button btnCancel;
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	public ThreeHoursReportWindow(Dealer dealer) {
		this.dealer = dealer;
		
		setCaption(I18N.message("3.hours.report"));
		setModal(true);
		
		btnSave = new NativeButton(I18N.message("save"), getButtonClickListener());
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		btnCancel = new NativeButton(I18N.message("cancel"), getButtonClickListener());
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		NavigationPanel navigationPanelStatistic = new NavigationPanel();
		navigationPanelStatistic.addButton(btnSave);
		navigationPanelStatistic.addButton(btnCancel);
		
		dashboardPopupTablePanel = new DashboardPopupTablePanel(this.dealer);
		dashboardPopupTablePanel.setStyleName("remove_vertical_space");
		dashboardPopupTablePanel.search();
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.addComponent(navigationPanelStatistic);
		contentLayout.addComponent(dashboardPopupTablePanel);
		
		setContent(contentLayout);
		setWidth(1200, Unit.PIXELS);
		setHeight(680, Unit.PIXELS);
	}
	
	private ClickListener getButtonClickListener() {
		return new ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (btnSave == event.getButton()) {
					onSave();
				} else {
					close();
				}
			}
		};
	}
	
	private void onSave() {
		List<Statistic3HoursVisitor> statistic3HoursVisitors = dashboardPopupTablePanel.getStatistic3HoursVisitors();
		if (statistic3HoursVisitors != null && !statistic3HoursVisitors.isEmpty()) {
			if (this.dealer != null) {
				for (Statistic3HoursVisitor statistic3HoursVisitor : statistic3HoursVisitors) {
						entityService.saveOrUpdate(statistic3HoursVisitor);
				}
			} else {
				close();
			}
		}
		close();
	}

}
