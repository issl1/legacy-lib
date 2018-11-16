package com.nokor.efinance.gui.ui.panel.payment;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.document.panel.DocumentViewver;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.gui.report.xls.GLFOfficialReceipt;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.service.ReportService;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DownPaymentsPanel.NAME)
public class DownPaymentsPanel extends AbstractTabsheetPanel implements View, CashflowEntityField, PaymentEntityField {
	
	private static final long serialVersionUID = 1921078497711712192L;

	public static final String NAME = "down.payments";
	
	private ReportService reportService = SpringUtils.getBean(ReportService.class);
	private PaymentService paymentService = SpringUtils.getBean(PaymentService.class);
	
	@Autowired
	private DownPaymentTablePanel downPaymentTablePanel;
		
	private DownPaymentDetail2Panel downPaymentDetail2Panel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		downPaymentTablePanel.setMainPanel(this);
		getTabSheet().setTablePanel(downPaymentTablePanel);
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
		Long quotaId = (Long) downPaymentTablePanel.getItemSelectedId();
		final Quotation quotation = paymentService.getById(Quotation.class, quotaId);
		final Window window = new Window();
		window.setImmediate(true);
		window.setModal(true);
		window.setCaption(I18N.message("add.payment"));
		
		downPaymentDetail2Panel = new DownPaymentDetail2Panel();
		downPaymentDetail2Panel.assignValues(quotaId);
		window.setContent(downPaymentDetail2Panel);
		
		window.setWidth(630, Unit.PIXELS);
		window.setHeight(350, Unit.PIXELS);
		window.center();
        UI.getCurrent().addWindow(window);
		
		ClickListener allocateListener = new ClickListener() {
			private static final long serialVersionUID = -7363634930253442559L;
			@Override
			public void buttonClick(ClickEvent event) {
				boolean isValidDownPayment = false;
				Date paymentDate = null;
				EPaymentMethod paymentMethod = null;
				
				isValidDownPayment = downPaymentDetail2Panel.isValid();
				paymentDate = downPaymentDetail2Panel.getPaymentDate();
				paymentMethod = downPaymentDetail2Panel.getPaymentMethod();
				
				if (isValidDownPayment) {
					window.close();
					
					final Payment payment = paymentService.createDownPayment(quotation.getContract(), paymentDate);
					
					ToolbarButtonsPanel toolbarButtons = new ToolbarButtonsPanel();
					Button btnPrintOfficialReceipt = new NativeButton(I18N.message("print.down.payment"));
				    btnPrintOfficialReceipt.setIcon(new ThemeResource("../nkr-default/icons/16/print.png"));
				    btnPrintOfficialReceipt.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 1044087379420693632L;
						@Override
						public void buttonClick(ClickEvent event) {
							Class<? extends Report> reportClass = GLFOfficialReceipt.class;
							ReportParameter reportParameter = new ReportParameter();
							reportParameter.addParameter("paymnId", payment.getId());	
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
				    
					Button btnOk = new NativeButton();
					btnOk = new NativeButton(I18N.message("ok"));
					btnOk.setIcon(new ThemeResource("../nkr-default/icons/16/tick.png"));
					toolbarButtons.addButton(btnOk);
					toolbarButtons.addButton(btnPrintOfficialReceipt);
					final VerticalLayout paymentInfoLayout = new VerticalLayout();
					paymentInfoLayout.setSpacing(true);
					paymentInfoLayout.setMargin(true);
					paymentInfoLayout.addComponent(toolbarButtons);
					
					OfficialPaymentInfo2Panel officialPaymentInfo2Panel = new OfficialPaymentInfo2Panel();
					officialPaymentInfo2Panel.assignValues(payment);
					paymentInfoLayout.addComponent(officialPaymentInfo2Panel);
										
					btnOk.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 3819355610146445846L;
						@Override
						public void buttonClick(ClickEvent event) {
							getTabSheet().removeComponent(paymentInfoLayout);
							Page.getCurrent().setUriFragment("!" + PurchaseOrdersPanel.NAME);
						}
					});
					getTabSheet().addFormPanel(paymentInfoLayout, I18N.message("payment.info"));
					getTabSheet().setSelectedTab(paymentInfoLayout);
					getTabSheet().setNeedRefresh(true);
				}
			}
		};
		downPaymentDetail2Panel.setAllocateListener(allocateListener);		
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == downPaymentTablePanel && getTabSheet().isNeedRefresh()) {
			downPaymentTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
	
}
