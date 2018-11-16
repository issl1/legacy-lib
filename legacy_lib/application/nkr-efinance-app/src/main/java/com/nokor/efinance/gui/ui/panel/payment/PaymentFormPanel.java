package com.nokor.efinance.gui.ui.panel.payment;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.document.panel.DocumentViewver;
import com.nokor.efinance.core.payment.model.EPaymentType;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.payment.panel.earlysettlement.PaymentInfo2Panel;
import com.nokor.efinance.core.payment.service.PaymentService;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.workflow.PaymentWkfStatus;
import com.nokor.efinance.gui.report.xls.GLFPurchaseOrder;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.service.ReportService;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaymentFormPanel extends AbstractFormPanel implements CashflowEntityField {

	private static final long serialVersionUID = -9118726466667794288L;
	
	@Autowired
	private ReportService reportService;	
	
	private PaymentService paymentService= SpringUtils.getBean(PaymentService.class);
	private Payment payment;
	private VerticalLayout contentLayout;
	private Panel paymentPanel;
	private ToolbarButtonsPanel toolbarButtons;
	private Button btnPrintPurchaseOrder;
	private PurchaseOrdersPanel secondPaymentsPanel;

	@PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("payment"));
	}
    
	@Override
	protected Payment getEntity() {	
		return payment;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		paymentPanel = new Panel(I18N.message("payment"));
        
        VerticalLayout cashflowsLayout = new VerticalLayout();
        cashflowsLayout.setSpacing(true);
        cashflowsLayout.setMargin(true);
        btnPrintPurchaseOrder = new NativeButton(I18N.message("print.purchase.order"));
        btnPrintPurchaseOrder.setIcon(new ThemeResource("../nkr-default/icons/16/print.png"));
        btnPrintPurchaseOrder.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1044087379420693632L;
			@Override
			public void buttonClick(ClickEvent event) {
				Class<? extends Report> reportClass = GLFPurchaseOrder.class;
				ReportParameter reportParameter = new ReportParameter();
				reportParameter.addParameter("paymnId", payment.getId());	
				String	fileName = "";
				try {
					fileName = reportService.extract(reportClass, reportParameter);
				} catch (Exception e) {
					logger.error("", e);
				}
				String fileDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
				DocumentViewver documentViewver = new DocumentViewver(I18N.message(""), fileDir + "/" + fileName); 
				UI.getCurrent().addWindow(documentViewver);
				Payment updatePayment = paymentService.getById(Payment.class, payment.getId());
				updatePayment.setPrintPurchaseOrderVersion(1);
				SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				updatePayment.setLastUserprintPurchaseOrder(secUser);
				paymentService.saveOrUpdate(updatePayment);
				
				if (getSecondPaymentsPanel() != null) {
					getSecondPaymentsPanel().redirectToSecondPaymentTable();
				}
			}
		});
        
        toolbarButtons = new ToolbarButtonsPanel();
        toolbarButtons.addButton(btnPrintPurchaseOrder);
        
        contentLayout.addComponent(toolbarButtons);
		contentLayout.addComponent(paymentPanel);
		return contentLayout;
	}

	/**
	 * @return the secondPaymentsPanel
	 */
	private PurchaseOrdersPanel getSecondPaymentsPanel() {
		return secondPaymentsPanel;
	}

	/**
	 * @param secondPaymentsPanel the secondPaymentsPanel to set
	 */
	public void setSecondPaymentsPanel(PurchaseOrdersPanel secondPaymentsPanel) {
		this.secondPaymentsPanel = secondPaymentsPanel;
	}
	
	/**
	 * @param id
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			payment = ENTITY_SRV.getById(Payment.class, id);
			if (payment.getPaymentType().equals(EPaymentType.ORC)) {
			
				OfficialPaymentInfo2Panel officialPaymentInfo2Panel = new OfficialPaymentInfo2Panel();
				officialPaymentInfo2Panel.assignValues(payment);
				paymentPanel.setContent(officialPaymentInfo2Panel);
								
			} else if (payment.getPaymentType().equals(EPaymentType.IRC)) {
			
				PaymentInfo2Panel paymentInfo2Panel = new PaymentInfo2Panel();
				paymentInfo2Panel.assignValues(payment);
				paymentPanel.setContent(paymentInfo2Panel);	
			}
			btnPrintPurchaseOrder.setVisible(payment.getPaymentType().equals(EPaymentType.ORC) &&
					(payment.getWkfStatus().equals(PaymentWkfStatus.VAL) || payment.getWkfStatus().equals(PaymentWkfStatus.PAI)));
			toolbarButtons.setVisible(payment.getPaymentType().equals(EPaymentType.ORC) &&
					(payment.getWkfStatus().equals(PaymentWkfStatus.VAL) || payment.getWkfStatus().equals(PaymentWkfStatus.PAI)));
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		payment = new Payment();
	}

	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		return errors.isEmpty();
	}	
}
