package com.nokor.efinance.gui.ui.panel.payment;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.document.panel.DocumentViewver;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.gui.report.xls.GLFOfficialReceipt;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.service.ReportService;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DownPaymentReportsPanel.NAME)
public class DownPaymentReportsPanel extends AbstractTabsheetPanel implements View, CashflowEntityField, PaymentEntityField {
	
	private static final long serialVersionUID = 1921078497711712192L;

	public static final String NAME = "report.down.payments";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private ReportService reportService = (ReportService) SecApplicationContextHolder.getContext().getBean("reportService");
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	private VerticalLayout verticalLayout;
	private ToolbarButtonsPanel toolbarButtons;
	@Autowired
	private DownPaymentReportTablePanel downPaymentReportTablePanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		downPaymentReportTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(downPaymentReportTablePanel);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		String paymnId = event.getParameters();
		if (StringUtils.isNotEmpty(paymnId)) {;
			getTabSheet().setForceSelected(true);
		}
	}

	@Override
	public void onAddEventClick() {
	}

	@Override
	public void onEditEventClick() {

		verticalLayout = new VerticalLayout();
		verticalLayout.setCaption(I18N.message("payment.info"));
		toolbarButtons = new ToolbarButtonsPanel();
		Payment payment = entityService.getById(Payment.class, downPaymentReportTablePanel.getItemSelectedId());
		Button btnPrintOfficialReceipt = new NativeButton(I18N.message("print.down.payment"));
	    btnPrintOfficialReceipt.setIcon(new ThemeResource("../nkr-default/icons/16/print.png"));
	    btnPrintOfficialReceipt.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1044087379420693632L;
				@Override
			public void buttonClick(ClickEvent event) {
					Class<? extends Report> reportClass = GLFOfficialReceipt.class;
					ReportParameter reportParameter = new ReportParameter();
					reportParameter.addParameter("paymnId", downPaymentReportTablePanel.getItemSelectedId());	
					String	fileName = "";
					try {
						fileName = reportService.extract(reportClass, reportParameter);
					} catch (Exception e) {
					}
					 String fileDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
					DocumentViewver documentViewver = new DocumentViewver(I18N.message(""), fileDir + "/" + fileName); 
					UI.getCurrent().addWindow(documentViewver);
				}
			});

	    toolbarButtons.addButton(btnPrintOfficialReceipt);
	    verticalLayout.addComponent(toolbarButtons);
		
	    OfficialPaymentInfo2Panel OfficialPaymentInfo2Panel = new OfficialPaymentInfo2Panel();
	    OfficialPaymentInfo2Panel.assignValues(payment);
	    verticalLayout.addComponent(OfficialPaymentInfo2Panel);
		getTabSheet().addTab(verticalLayout);
			    
	    getTabSheet().setSelectedTab(verticalLayout);
		
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == downPaymentReportTablePanel && getTabSheet().isNeedRefresh()) {
			downPaymentReportTablePanel.refresh();
		}
		if (verticalLayout != null) {
			getTabSheet().removeComponent(verticalLayout);
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
}
