package com.nokor.efinance.tools.report.panel;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.document.panel.DocumentViewver;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.service.ReportService;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.navigator.View;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ly.youhort
 *
 */
public abstract class AbstractReportPanel extends VerticalLayout implements View {

	private static final long serialVersionUID = -357254938899220554L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected void init() {
		final AbstractSearchReportPanel searchPanel = createSearchPanel();
		NavigationPanel navigationPanel = new NavigationPanel();
		Button btnExecute = new NativeButton(I18N.message("execute"));
		btnExecute.setIcon(new ThemeResource("../nkr-default/icons/16/tick.png"));
		navigationPanel.addButton(btnExecute);
		
		Button btnReset = new NativeButton(I18N.message("reset"));
		btnReset.setIcon(new ThemeResource("../nkr-default/icons/16/translate.png"));
		navigationPanel.addButton(btnReset);
		
		addComponent(navigationPanel);
		addComponent(searchPanel);
		
		btnExecute.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7653593119193887552L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					execute(searchPanel.getReportParameter());
				} catch (Exception e) {
					logger.error("AbstractReportPanel", e);
				}
			}
		});
		
		btnReset.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8343682252132351381L;
			@Override
			public void buttonClick(ClickEvent event) {
				searchPanel.reset();
			}
		});
	}
	
	protected abstract AbstractSearchReportPanel createSearchPanel();
	protected abstract Class<? extends Report> getReportClass();
	
	/**
	 * @throws Exception
	 */
	protected void execute(ReportParameter parameter) throws Exception {
		ReportService reportService = SpringUtils.getBean(ReportService.class);
		String fileDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
		String fileName = reportService.extract(getReportClass(), parameter);
		DocumentViewver documentViewver = new DocumentViewver(I18N.message(""), fileDir + "/" + fileName); 
		UI.getCurrent().addWindow(documentViewver);
	}
}
