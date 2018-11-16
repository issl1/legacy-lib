package com.nokor.efinance.gui.ui.panel.dashboard;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.DateFilterUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author sok.vina
 * @author Va.David (added)
 */
public class DashboardFormWithoutButtonsPanel extends VerticalLayout implements View, QuotationEntityField {
	
	private static final long serialVersionUID = 6227741117498204118L;

	public static final String NAME = "dashboard";
	
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
		
	private VerticalLayout sessionLayout;
	private VerticalLayout stastiticLayout;
	private DashboardReportsPanel dashboardReportsPanel;
	private SecUserDetail secUserDetail;
	private Label lblSession;
	private POSession pos;
	private Panel sessionPanel;
	
	
	/**
	 * @param secUserDetail
	 */
	public DashboardFormWithoutButtonsPanel(SecUserDetail secUserDetail) {
		this.secUserDetail = secUserDetail;
		addComponent(createForm());
	}
	
	/**
	 * @return
	 */
	private HorizontalLayout createForm() {
		pos = new POSession();
		POSession poSession = pos.assignValue(getQuotations());
		
		lblSession = new Label("<b><font size='3'>" + I18N.message("po.session") + ": " + secUserDetail.getDealer().getNameEn() + "</font></b>", ContentMode.HTML);
		Button btnDealer = new Button();
		btnDealer.setIcon(new ThemeResource("../nkr-default/icons/16/table.png"));
		btnDealer.addClickListener(new ClickListener() {			
			private static final long serialVersionUID = -2247703063857004304L;
			@Override
			public void buttonClick(ClickEvent event) {
				final Window winDealer = new Window();
				winDealer.setModal(true);
				winDealer.setClosable(false);
				winDealer.setResizable(false);
			    winDealer.setWidth(340, Unit.PIXELS);
			    winDealer.setHeight(160, Unit.PIXELS);
		        winDealer.setCaption(I18N.message("please.select.dealer"));
		        final DealerComboBox cbxDealer = new DealerComboBox(DataReference.getInstance().getDealers());
		        cbxDealer.setSelectedEntity(secUserDetail.getDealer());
		        
		        Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {
					private static final long serialVersionUID = 3975121141565713259L;
					public void buttonClick(ClickEvent event) {
						winDealer.close();
		            }
		        });
		        btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
				
				Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
					private static final long serialVersionUID = 8088485001713740490L;
					public void buttonClick(ClickEvent event) {
						secUserDetail.setDealer(cbxDealer.getSelectedEntity());
						entityService.saveOrUpdate(secUserDetail);
						getRefreshDashboard(secUserDetail);
						winDealer.close();
		            }
		        });
				btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
				
				NavigationPanel navigationPanel = new NavigationPanel();
				navigationPanel.addButton(btnSave);
				navigationPanel.addButton(btnCancel);
				
				final GridLayout gridLayout = new GridLayout(2, 1);
				gridLayout.setMargin(true);
				gridLayout.setSpacing(true);
		        gridLayout.addComponent(new Label(I18N.message("dealer")), 0, 0);
				gridLayout.addComponent(cbxDealer, 1, 0);
				
				VerticalLayout verticalLayout = new VerticalLayout();
				verticalLayout.addComponent(navigationPanel);
				verticalLayout.addComponent(gridLayout);
				winDealer.setContent(verticalLayout);
		        UI.getCurrent().addWindow(winDealer);
			}
		});
		
		sessionLayout = new VerticalLayout();
		sessionLayout.setWidth("90%");
		sessionLayout.setSpacing(true);
		sessionLayout.addComponent(new HorizontalLayout(lblSession, btnDealer));
		
		sessionPanel = new Panel();
		sessionPanel.setContent(getTablePOSession(poSession));
		sessionLayout.addComponent(sessionPanel);
		
		
		stastiticLayout = new VerticalLayout();
		stastiticLayout.setSizeFull();
		HorizontalLayout dateTimeHorizontalLayout = new HorizontalLayout();
		dateTimeHorizontalLayout.setStyleName("margin-left60");
		dateTimeHorizontalLayout.addComponent(new Label("<font size='4'>" + DateUtils.formatDate(DateUtils.today(), "EEEE dd/MM/yyyy HH:mm a") + "</font>", ContentMode.HTML));
		
		
		
		stastiticLayout.addComponent(dateTimeHorizontalLayout);
		VerticalLayout space = new VerticalLayout();
		space.setHeight("15");
		stastiticLayout.addComponent(space);
		
		VerticalLayout spaceHeight2 = new VerticalLayout();
        spaceHeight2.setHeight("20");
		stastiticLayout.addComponent(spaceHeight2);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		horizontalLayout.setMargin(true);
		
		horizontalLayout.addComponent(sessionLayout);
		horizontalLayout.addComponent(stastiticLayout);
		
		return horizontalLayout;
	}
	
	/**
	 * @return
	 */
	private List<Quotation> getQuotations() {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		if (secUserDetail != null && secUserDetail.getDealer() != null) {
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, secUserDetail.getDealer().getId()));
		}
		restrictions.addCriterion(Restrictions.gt(QUOTATION_DATE, 
				DateFilterUtil.getStartDate(DateUtils.addMonthsDate(DateUtils.today(), -3))));
		restrictions.addCriterion(Restrictions.le(QUOTATION_DATE, DateFilterUtil.getEndDate(DateUtils.today())));
		return entityService.list(restrictions);

	}

	/**
	 * @param poSession
	 * @return
	 */
	public CustomLayout getTablePOSession(POSession poSession) {
		String table = "<table>";
			   table += "<tr><td class='under_line_and_width80'>"+I18N.message("new.applications")+"</td><td >"+getNbNewApplications()+"</td></tr>";
			   table += "<tr><td class='under_line_and_width80'>"+I18N.message("field.check")+"</td><td >"+getNbFieldChecks()+"</td></tr>";
			   table += "<tr><td class='under_line_and_width80'>"+I18N.message("in.process.at.pos")+"</td><td >"+poSession.getNumInProcessAtPoS()+"</td></tr>";
			   table += "<tr><td class='under_line_and_width80'>"+I18N.message("in.process.at.uw")+"</td><td >"+poSession.getNumInProcessAtUW()+"</td></tr>";
			   table += "<tr><td class='under_line_and_width80'>"+I18N.message("new.contracts")+"</td><td >"+poSession.getNumNewContracts()+"</td></tr>";
			   table += "<tr><td class='under_line_and_width80'>"+I18N.message("pending.for.new.contract")+"</td><td >"+poSession.getNumPendingNewContract()+"</td></tr>";
			   table +=	"</table>";
		CustomLayout CustomLayoutTablePOSession = new CustomLayout("table");
		CustomLayoutTablePOSession.setTemplateContents(table);
		CustomLayoutTablePOSession.setSizeFull();
		return CustomLayoutTablePOSession;
	}
	
	private void getRefreshDashboard(SecUserDetail secUserDetail) {
		List<Quotation> quotation = getQuotations();
		POSession poSession = pos.assignValue(quotation);
		sessionLayout.removeComponent(sessionPanel);
		sessionPanel = new Panel();
		sessionPanel.setContent(getTablePOSession(poSession));
		sessionLayout.addComponent(sessionPanel);
		dashboardReportsPanel.search(secUserDetail.getDealer());
		lblSession.setValue("<b><font size='3'>" + I18N.message("po.session") + ": " + secUserDetail.getDealer().getNameEn() + "</font></b>");
	}
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	/**
	 * @return number of total applications
	 */
	private long getNbNewApplications() {
		Date dateBeginning = DateUtils.getDateAtBeginningOfMonth(DateUtils.today());
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		if (secUserDetail != null && secUserDetail.getDealer() != null) {
			restrictions.addCriterion(Restrictions.eq("dealer.id", secUserDetail.getDealer().getId()));
		}
		restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateFilterUtil.getStartDate(dateBeginning)));
		restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateFilterUtil.getEndDate(DateUtils.today())));
		return entityService.count(restrictions);
	}
	
	/**
	 * @return number of total field checks 
	 */
	private long getNbFieldChecks() {
		Date dateBeginning = DateUtils.getDateAtBeginningOfMonth(DateUtils.today());
		// TODO PYI
//		BaseRestrictions<QuotationStatusHistory> restrictions = new BaseRestrictions<>(QuotationStatusHistory.class);
//		restrictions.addAssociation("quotation", "quo", JoinType.INNER_JOIN);
//		restrictions.addCriterion(Restrictions.eq("quotationStatus", WkfQuotationStatus.RFC));
//		restrictions.addCriterion(Restrictions.ge("createDate", DateFilterUtil.getStartDate(dateBeginning)));
//		restrictions.addCriterion(Restrictions.le("createDate", DateFilterUtil.getEndDate(DateUtils.today())));
//		if (secUserDetail != null && secUserDetail.getDealer() != null) {
//			restrictions.addCriterion(Restrictions.eq("quo.dealer.id", secUserDetail.getDealer().getId()));
//		}			
//		List<QuotationStatusHistory> quotationStatusHistories = entityService.list(restrictions);
		Map<Long, Long> results = new HashMap<>();
//		for (QuotationStatusHistory quotationStatusHistory : quotationStatusHistories) {
//			if (!results.containsKey(quotationStatusHistory.getQuotation().getId())) {
//				results.put(quotationStatusHistory.getQuotation().getId(), quotationStatusHistory.getId());
//			}
//		}		
		return results.size();
	}
	
	private class POSession {
	
		private long numInProcessAtPoS;
		private long numInProcessAtUW;
		private long numNewContracts;
		private long numPendingNewContract;
		
		/**
		 * @return the numInProcessAtPoS
		 */
		public long getNumInProcessAtPoS() {
			return numInProcessAtPoS;
		}
		/**
		 * @param numInProcessAtPoS the numInProcessAtPoS to set
		 */
		public void setNumInProcessAtPoS(long numInProcessAtPoS) {
			this.numInProcessAtPoS = numInProcessAtPoS;
		}
		
		/**
		 * @return the numInProcessAtUW
		 */
		public long getNumInProcessAtUW() {
			return numInProcessAtUW;
		}
		
		/**
		 * @param numInProcessAtUW the numInProcessAtUW to set
		 */
		public void setNumInProcessAtUW(long numInProcessAtUW) {
			this.numInProcessAtUW = numInProcessAtUW;
		}
		
		/**
		 * @return the numNewContracts
		 */
		public long getNumNewContracts() {
			return numNewContracts;
		}
		
		/**
		 * @param numNewContracts the numNewContracts to set
		 */
		public void setNumNewContracts(long numNewContracts) {
			this.numNewContracts = numNewContracts;
		}
		
		/**
		 * @return the numPendingNewContract
		 */
		public long getNumPendingNewContract() {
			return numPendingNewContract;
		}
		
		/**
		 * @param numPendingNewContract the numPendingNewContract to set
		 */
		public void setNumPendingNewContract(long numPendingNewContract) {
			this.numPendingNewContract = numPendingNewContract;
		}
		
		public POSession assignValue(List<Quotation> quotations) {
			long numInProcessAtPoS = 0;
			long numInProcessAtUW = 0;
			long numNewContracts = 0;
			long numPendingNewContract = 0;
			if (quotations != null && !quotations.isEmpty()) {
				for (Quotation quotation : quotations) {
					if (quotation.getWkfStatus().equals(QuotationWkfStatus.QUO) 
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.RFC)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWT)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.ACG)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.LCG)) {
						numInProcessAtPoS++;
					}  else if (quotation.getWkfStatus().equals(QuotationWkfStatus.PRO) 
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.REU)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.APS)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.PRA)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWU)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWS)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.RCG)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.RVG)) {
						numInProcessAtUW++;
					} else if (quotation.getWkfStatus().equals(QuotationWkfStatus.ACT)) {
						if (DateUtils.isSameDay(quotation.getActivationDate(), DateUtils.today())) {
							numNewContracts++;
						}
					} else if (quotation.getWkfStatus().equals(QuotationWkfStatus.APV)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.AWT)
							|| quotation.getWkfStatus().equals(QuotationWkfStatus.ACG)) {
						numPendingNewContract++;
					}
				}
				
			}
			POSession poSession = new POSession();
			poSession.setNumInProcessAtPoS(numInProcessAtPoS);
			poSession.setNumInProcessAtUW(numInProcessAtUW);
			poSession.setNumNewContracts(numNewContracts);
			poSession.setNumPendingNewContract(numPendingNewContract);
			return poSession;
		}
	}
	
	/**
	 * Set DashboardReportsPanel
	 * @param dashboardReportsPanel
	 */
	public void setDashboardReportsPanel (DashboardReportsPanel dashboardReportsPanel) {
		this.dashboardReportsPanel = dashboardReportsPanel;
	}
	
}
