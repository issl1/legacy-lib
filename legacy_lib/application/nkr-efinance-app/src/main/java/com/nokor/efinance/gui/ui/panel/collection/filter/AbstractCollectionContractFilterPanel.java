package com.nokor.efinance.gui.ui.panel.collection.filter;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.document.panel.DocumentViewver;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.ColFilterPopUpPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.ColFilterPopUpPanel.StoreControl;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.nokor.efinance.gui.ui.panel.collection.supervisor.report.ReportContractAreaInsideRepoSupervisor;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.tools.report.service.ReportService;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public abstract class AbstractCollectionContractFilterPanel extends VerticalLayout implements ClickListener, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7079783938188684181L;
	
	private ReportService reportService = (ReportService) SecApplicationContextHolder.getContext().getBean("reportService");
	
	private NativeButton btnUpdateFilter;
	private Label lblContractId;
	private Label lblDueDateFrom;
	private Label lblDueDateTo;
	private Label lblAssignDateFrom;
	private Label lblAssignDateTo;
	private Label lblNextActionDate;
	private Label lblNbGuarantor;
	private Label lblArea;
	private Label lblBranch;
	protected Button btnExcel;
	protected List<Contract> contracts;
	
	protected StoreControl storeControl;
	
	private CollectionContractTablePanel tablePanel;
	
	private Label lblUserSelected;
	protected SecUser secUser;
	
	public AbstractCollectionContractFilterPanel(CollectionContractTablePanel tablePanel) {
		this.tablePanel = tablePanel;
		init();
	}
	
	/**
	 * Init
	 */
	private void init() {
		secUser = UserSessionManager.getCurrentUser();
	
		btnUpdateFilter = new NativeButton(I18N.message("update"));
		btnUpdateFilter.setStyleName("btn btn-success button-small");
		btnUpdateFilter.setWidth("70px");
		btnUpdateFilter.addClickListener(this);
		
		lblContractId = getLabelValue();
		lblDueDateFrom = getLabelValue();
		lblDueDateTo = getLabelValue();
		lblAssignDateFrom = getLabelValue();
		lblAssignDateTo = getLabelValue();
		lblNextActionDate = getLabelValue();
		lblNbGuarantor = getLabelValue();
		lblBranch = getLabelValue();
		
		lblUserSelected = getLabelValue();
		lblArea = getLabelValue();
		btnExcel = ComponentLayoutFactory.getDefaultButton("export", FontAwesome.FILE_EXCEL_O, 70);
		btnExcel.removeClickShortcut();
		btnExcel.addClickListener(this);
		
		HorizontalLayout filterLayout = new HorizontalLayout();
		filterLayout.setSpacing(true);
		filterLayout.setMargin(true);
		filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("current.filters") + " : "));
		filterLayout.addComponent(btnUpdateFilter);
		filterLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		
		filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("contract.id") + " : "));
		filterLayout.addComponent(lblContractId);
		filterLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		
		filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("due.date") + " : "));
		filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption("from"));
		filterLayout.addComponent(lblDueDateFrom);
		filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption("to"));
		filterLayout.addComponent(lblDueDateTo);
		filterLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		
		filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("assign.date") + " : "));
		filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption("from"));
		filterLayout.addComponent(lblAssignDateFrom);
		filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption("to"));
		filterLayout.addComponent(lblAssignDateTo);
		filterLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		
		filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("next.action.date") + " : "));
		filterLayout.addComponent(lblNextActionDate);
		filterLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		
		filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("guarantor") + " : "));
		filterLayout.addComponent(lblNbGuarantor);
		
		filterLayout.setComponentAlignment(lblContractId, Alignment.MIDDLE_LEFT);
		filterLayout.setComponentAlignment(lblDueDateFrom, Alignment.MIDDLE_LEFT);
		filterLayout.setComponentAlignment(lblDueDateTo, Alignment.MIDDLE_LEFT);
		filterLayout.setComponentAlignment(lblAssignDateFrom, Alignment.MIDDLE_LEFT);
		filterLayout.setComponentAlignment(lblAssignDateTo, Alignment.MIDDLE_LEFT);
		filterLayout.setComponentAlignment(lblNextActionDate, Alignment.MIDDLE_LEFT);
		filterLayout.setComponentAlignment(lblNbGuarantor, Alignment.MIDDLE_LEFT);
		
		if (ProfileUtil.isColField() || ProfileUtil.isColInsideRepo()) {
			filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("area") + " : "));
			filterLayout.addComponent(lblArea);
			filterLayout.setComponentAlignment(lblArea, Alignment.MIDDLE_LEFT);
		}
		if (ProfileUtil.isColPhoneLeader(secUser) 
				|| ProfileUtil.isColPhoneSupervisor(secUser) 
				|| ProfileUtil.isColFieldLeader(secUser)
				|| ProfileUtil.isColFieldSupervisor(secUser)
				|| ProfileUtil.isColInsideRepoLeader(secUser)
				|| ProfileUtil.isColInsideRepoSupervisor(secUser)
				|| ProfileUtil.isCallCenterLeader()) {
			
			filterLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
			filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("selected") + " : "));
			filterLayout.addComponent(lblUserSelected);
			filterLayout.setComponentAlignment(lblUserSelected, Alignment.MIDDLE_LEFT);
		}
		
		if (ProfileUtil.isColInsideRepoSupervisor()) {
			filterLayout.addComponent(btnExcel);
			filterLayout.setComponentAlignment(btnExcel, Alignment.TOP_RIGHT);
		}
		
		if (ProfileUtil.isColPhone()) {
			filterLayout.addComponent(ComponentLayoutFactory.getLabelCaption(I18N.message("branch") + " : "));
			filterLayout.addComponent(lblBranch);
			filterLayout.setComponentAlignment(lblBranch, Alignment.MIDDLE_LEFT);
		}

		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(filterLayout);
		mainLayout.setComponentAlignment(filterLayout, Alignment.MIDDLE_CENTER);
		
		Panel filterPanel = new Panel();
		filterPanel.setContent(mainLayout);
		
		storeControl = null;
		
		addComponent(filterPanel);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value == null ? "" : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : StringUtils.EMPTY;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		return label;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnUpdateFilter) {
			ColFilterPopUpPanel window = ColFilterPopUpPanel.show(storeControl, new ColFilterPopUpPanel.Listener() {
				
				/** */
				private static final long serialVersionUID = -8651335693496190700L;

				/**
				 * @see com.nokor.efinance.gui.ui.panel.collection.phone.filter.ColFilterPopUpPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.filter.ColFilterPopUpPanel, com.nokor.efinance.gui.ui.panel.collection.phone.filter.ColFilterPopUpPanel.StoreControl)
				 */
				@Override
				public void onClose(ColFilterPopUpPanel dialog, StoreControl storectl) {
					storeControl = storectl;
					tablePanel.refresh(getRestrictions());
					lblContractId.setValue(getDescription(storectl.getContractId()));
					lblDueDateFrom.setValue(getDescription(storectl.getDueDateFrom()));
					lblDueDateTo.setValue(getDescription(storectl.getDueDateTo()));
					lblAssignDateFrom.setValue(getDescription(getDateFormat(storectl.getAssignDateFrom())));
					lblAssignDateTo.setValue(getDescription(getDateFormat(storectl.getAssignDateTo())));
					lblNbGuarantor.setValue(getDescription(storectl.getGuarantor()));
					lblUserSelected.setValue(getDescription(storectl.getLblStaffSelected() !=null ? storectl.getLblStaffSelected().getCaption() : I18N.message("all")));
					lblArea.setValue(getDescription(storectl.getArea() != null ? storectl.getArea().getLine2() : ""));
					lblBranch.setValue(getDescription(storectl.getBrand() != null ? storectl.getBrand().getNameLocale() : ""));
				}
			});
			UI.getCurrent().addWindow(window);
		} else if (event.getButton() == btnExcel) {
			Class<? extends Report> reportClass = ReportContractAreaInsideRepoSupervisor.class;
			ReportParameter reportParameter = new ReportParameter();
			reportParameter.addParameter("contracts", contracts);	
			String	fileName = "";
			try {
				fileName = reportService.extract(reportClass, reportParameter);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String fileDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
			DocumentViewver documentViewver = new DocumentViewver(I18N.message(""), fileDir + "/" + fileName); 
			UI.getCurrent().addWindow(documentViewver);
		}
	}
	
	public abstract BaseRestrictions<ContractUserInbox> getRestrictions();

}
