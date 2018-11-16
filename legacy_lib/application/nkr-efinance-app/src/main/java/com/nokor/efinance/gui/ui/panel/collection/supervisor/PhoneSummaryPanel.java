package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.service.PhoneSummary;
import com.nokor.efinance.core.collection.service.PhoneSummary.BranchSummary;
import com.nokor.efinance.core.collection.service.PhoneSummary.DueDateSummary;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * 
 * @author buntha.chea
 *
 */
public class PhoneSummaryPanel extends VerticalLayout implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -709269345567816337L;
	
	private static String OPEN_TABLE = "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; "
			+ "border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"300px\" bgcolor=\"#fff400\" "
			+ "style=\"border:1px solid black;\" >";
	private static String OPEN_TD = "<td class=\"align-center\" style=\"border:1px solid black;\" bgcolor=\"#e1e1e1\" >";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	private int numberOfStaff;
	private PhoneSummary phoneSummary;

	private int subTotalGuaranteed;
	private int subTotalNonGuaranteed;
	
	private List<Integer> dueDayDates;
	private List<BranchSummary> branchSummaries;
	private Map<Long, Integer> totalContractBranch;
	private Map<Integer, Integer> totalContractDueDate;
	private Integer totalAllContracts;
	private Integer contractGuaranteed;
	
	public PhoneSummaryPanel() {
		setCaption(I18N.message("summary"));
		setSizeFull();
		setMargin(true);
		
		totalContractBranch = new HashMap<>();
		totalContractDueDate = new HashMap<>();
		
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		panel.setContent(createDashboardTableLayout());
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(panel);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		horizontalLayout.addComponent(getSummaryDetail());
		
		VerticalLayout dashboardLayout = new VerticalLayout();
		dashboardLayout.setSpacing(true);
		dashboardLayout.addComponent(horizontalLayout);
		
		Panel mainPanel = new Panel();
		mainPanel.setContent(dashboardLayout);
		
		addComponent(mainPanel);
	}
	
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private CustomLayout createDashboardTableLayout() {
		CustomLayout dashboardCustomLayout = new CustomLayout("xxx");
		
		phoneSummary = COL_SRV.getPhoneSummaries();
		dueDayDates = phoneSummary.getDueDayDates();
		branchSummaries = phoneSummary.getSummaries();
		
		String dashborardTemplate = OPEN_TABLE;
		
		List<Integer> extendIndays = new ArrayList<>();
		int index = 0;
		//Header of table
		dashborardTemplate += OPEN_TR;
		
		dashborardTemplate += "<th rowspan=\"2\" class=\"align-center\" width=\"50px\" bgcolor=\"#fff400\" "
				+ "style=\"border:1px solid black;\" >";
		dashborardTemplate += "<div location =\"lblBrand\" />";
		dashborardTemplate += CLOSE_TH;
		
		dashboardCustomLayout.addComponent(new Label(I18N.message("brand")), "lblBrand");
		for (Integer dueDayDate : dueDayDates) {
			if (dueDayDate > 0 && !extendIndays.contains(dueDayDate)) {
				index++;
				
				dashborardTemplate += "<th colspan=\"3\" class=\"align-center\" width=\"50px\" bgcolor=\"#fff400\" "
						+ "style=\"border:1px solid black;\" >";
				dashborardTemplate += "<div location =\"lblDueDayDate"+ index +"\" />";
				dashborardTemplate += CLOSE_TH;
					
				dashboardCustomLayout.addComponent(new Label(String.valueOf(dueDayDate)), "lblDueDayDate" + index);
				extendIndays.add(dueDayDate);
			} 
		}
		
		dashborardTemplate += "<th rowspan=\"2\" class=\"align-center\" width=\"50px\" bgcolor=\"#fff400\" "
				+ "style=\"border:1px solid black;\" >";
		dashborardTemplate += "<div location =\"lblTotal\" />";
		dashborardTemplate += CLOSE_TH;
		
		dashboardCustomLayout.addComponent(new Label(I18N.message("total")), "lblTotal");
		
		dashborardTemplate += CLOSE_TR;
		dashborardTemplate += OPEN_TR;
		
		extendIndays.clear();
		for (Integer dueDayDate : dueDayDates) {
			if (dueDayDate > 0 && !extendIndays.contains(dueDayDate)) {
				index++;
				dashborardTemplate += OPEN_TH;
				dashborardTemplate += "<div location =\"lblGuaranteed"+ index +"\" />";
				dashborardTemplate += CLOSE_TH;
				
				dashborardTemplate += OPEN_TH;
				dashborardTemplate += "<div location =\"lblNonGuaranteed"+ index +"\" />";
				dashborardTemplate += CLOSE_TH;
				
				dashborardTemplate += OPEN_TH;
				dashborardTemplate += "<div location =\"lblTotalNbContracts"+ index +"\" />";
				dashborardTemplate += CLOSE_TH;
				
				dashboardCustomLayout.addComponent(new Label(I18N.message("guaranteed")), "lblGuaranteed"+ index);
				dashboardCustomLayout.addComponent(new Label(I18N.message("non.guaranteed")), "lblNonGuaranteed"+ index);
				dashboardCustomLayout.addComponent(new Label(I18N.message("total")), "lblTotalNbContracts"+ index);
				extendIndays.add(dueDayDate);
			}
		}
		dashborardTemplate += CLOSE_TR;
		//list of brand name 
		int dueDateIndex = 0;
		int indexValue = 0;
		for (BranchSummary branch : branchSummaries) {
			index++;
			dashborardTemplate += OPEN_TR;
			
			dashborardTemplate += "<td class=\"align-center\" style=\"border:1px solid black;\" bgcolor=\"#fff400\" >";
			dashborardTemplate += "<div location =\"lblBrandName" + index +"\" />";
			dashborardTemplate += CLOSE_TD;
			
			int total = 0;
		   //set value
			for (Integer dueDayDate : dueDayDates) {
				dueDateIndex++;
				dashborardTemplate += OPEN_TD;
				dashborardTemplate += "<div location =\"lblGuaranteedValue"+ dueDateIndex +"\" />";
				dashborardTemplate += CLOSE_TD;
				
				dashborardTemplate += OPEN_TD;
				dashborardTemplate += "<div location =\"lblNonGuaranteedValue"+ dueDateIndex +"\" />";
				dashborardTemplate += CLOSE_TD;
				
				dashborardTemplate += OPEN_TD;
				dashborardTemplate += "<div location =\"lblTotalNbContractsValue"+ dueDateIndex +"\" />";
				dashborardTemplate += CLOSE_TD;
				
				dashboardCustomLayout.addComponent(new Label("0"), "lblGuaranteedValue"+ dueDateIndex);
				dashboardCustomLayout.addComponent(new Label("0"), "lblNonGuaranteedValue"+ dueDateIndex);
				dashboardCustomLayout.addComponent(new Label("0"), "lblTotalNbContractsValue"+ dueDateIndex);
				
				for (DueDateSummary dueDate : branch.getSummaries()) {
					if (dueDate.getDueDayDate() == dueDayDate) {
						dashboardCustomLayout.addComponent(new Label(String.valueOf(dueDate.getNbContractGuaranteed())), "lblGuaranteedValue"+ dueDateIndex);
						dashboardCustomLayout.addComponent(new Label(String.valueOf(dueDate.getNbContractNonGuaranteed())), "lblNonGuaranteedValue"+ dueDateIndex);
						dashboardCustomLayout.addComponent(new Label(String.valueOf(dueDate.getTotalNbContracts())), "lblTotalNbContractsValue"+ dueDateIndex);
						total += dueDate.getTotalNbContracts();
					} 
				}
				
				
			}
			
			//set total of branch to hasmap
			totalContractBranch.put(branch.getBranchId(), total);
		
			dashborardTemplate += OPEN_TD;
			dashborardTemplate += "<div id=\"mylable-color\" location =\"lblTotal" + index +"\" />";
			dashborardTemplate += CLOSE_TD;
		
			dashborardTemplate += CLOSE_TR;
			
			dashboardCustomLayout.addComponent(new Label(String.valueOf(branch.getBranchName())), "lblBrandName"+ index);
			dashboardCustomLayout.addComponent(new Label(String.valueOf(total)), "lblTotal"+ index);
		}
		//Sub-Total
		dashborardTemplate += OPEN_TR;
		dashborardTemplate += "<td class=\"align-center\" style=\"border:1px solid black;\" bgcolor=\"#fff400\" >";
		dashborardTemplate += "<div location =\"lblSubTotal\" />";
		dashborardTemplate += CLOSE_TD;
		
		subTotalGuaranteed = 0;
		subTotalNonGuaranteed = 0;
		contractGuaranteed = 0;
		int subTotalDueDateIndex = 0;
		for (Integer dueDayDate : dueDayDates) {
			subTotalDueDateIndex++;
			dashborardTemplate += OPEN_TD;
			dashborardTemplate += "<div location =\"lblSubTotalGuaranteedValue"+ subTotalDueDateIndex +"\" />";
			dashborardTemplate += CLOSE_TD;
			
			dashborardTemplate += OPEN_TD;
			dashborardTemplate += "<div location =\"lblSubTotalNonGuaranteedValue"+ subTotalDueDateIndex +"\" />";
			dashborardTemplate += CLOSE_TD;
			
			dashborardTemplate += "<td class=\"align-center\" style=\"border:1px solid black; font-weight: bold;\" bgcolor=\"#e1e1e1\" >";
			dashborardTemplate += "<div location =\"lblSubTotalNbContractsValue"+ subTotalDueDateIndex +"\" />";
			dashborardTemplate += CLOSE_TD;
		}
		//set value to sub total
		subTotalDueDateIndex = 0;
		totalAllContracts = 0;
		for (Integer dueDateDay : dueDayDates) {
			subTotalDueDateIndex++;
			subTotalGuaranteed = 0;
			subTotalNonGuaranteed = 0;
			for (BranchSummary branch : branchSummaries) {
				for (DueDateSummary dueDate : branch.getSummaries()) {
					if (dueDateDay == dueDate.getDueDayDate()) {
						subTotalGuaranteed += dueDate.getNbContractGuaranteed();
						subTotalNonGuaranteed += dueDate.getNbContractNonGuaranteed();
						totalAllContracts += dueDate.getTotalNbContracts();		
					}
				}
			}
			contractGuaranteed += subTotalGuaranteed; 
			totalContractDueDate.put(dueDateDay, subTotalGuaranteed + subTotalNonGuaranteed);
			
			dashboardCustomLayout.addComponent(new Label(String.valueOf(subTotalGuaranteed)), "lblSubTotalGuaranteedValue" + subTotalDueDateIndex);
			dashboardCustomLayout.addComponent(new Label(String.valueOf(subTotalNonGuaranteed)), "lblSubTotalNonGuaranteedValue" + subTotalDueDateIndex);
			dashboardCustomLayout.addComponent(new Label(String.valueOf(subTotalGuaranteed + subTotalNonGuaranteed)), "lblSubTotalNbContractsValue" + subTotalDueDateIndex);
		}
		
		//total of sub-total
		dashborardTemplate += OPEN_TD;
		dashborardTemplate += "<div id=\"mylable-color\" location =\"lblTotalValue\" />";
		dashborardTemplate += CLOSE_TD;
		
		dashborardTemplate += CLOSE_TR;
		
		dashboardCustomLayout.addComponent(new Label(String.valueOf(totalAllContracts)), "lblTotalValue");
		dashboardCustomLayout.addComponent(new Label(I18N.message("sub.total")), "lblSubTotal");
		
		dashborardTemplate += CLOSE_TABLE;
		dashboardCustomLayout.setTemplateContents(dashborardTemplate);
		return dashboardCustomLayout;
	}
	
	/**
	 * Summery Panel 
	 * @return
	 */
	private Panel getSummaryDetail() {
		numberOfStaff = 0;
		//Staff
		HorizontalLayout nbStaffLayout = new HorizontalLayout();
		nbStaffLayout.addComponent(new Label(I18N.message("number.of.staff")));
		nbStaffLayout.addComponent(new Label(String.valueOf(numberOfStaff)));
		
		//branch
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(nbStaffLayout);
		for (BranchSummary branchSummary : phoneSummary.getSummaries()) {
			HorizontalLayout eachBrandLayout = new HorizontalLayout();
			eachBrandLayout.addComponent(new Label(I18N.message("contract.of.brand : ") + branchSummary.getBranchName()));
			eachBrandLayout.addComponent(new Label(" : (" + totalContractBranch.get(branchSummary.getBranchId()) + "/40 of adjusted)"));
		
			verticalLayout.addComponent(eachBrandLayout);
		}
		
		//Due Date
		VerticalLayout eachDueDateLayoutContent = new VerticalLayout();
		eachDueDateLayoutContent.setSpacing(true);
		eachDueDateLayoutContent.setMargin(true);
		eachDueDateLayoutContent.addComponent(new Label());
		for (Integer dueDayDate : phoneSummary.getDueDayDates()) {
			HorizontalLayout eachDueDateLayout = new HorizontalLayout();
			eachDueDateLayout.addComponent(new Label(I18N.message("contract.of.due.date") + dueDayDate + ":"));
			eachDueDateLayout.addComponent(new Label(": ("+ totalContractDueDate.get(dueDayDate) +"/40 of adjusted)"));
		
			eachDueDateLayoutContent.addComponent(eachDueDateLayout);
		}
		
		//Total
		HorizontalLayout contractTotalLayout = new HorizontalLayout();
		contractTotalLayout.addComponent(new Label(I18N.message("contract.in.total")));
		contractTotalLayout.addComponent(new Label("("+ totalAllContracts + "/40 of adjusted)"));
		
		eachDueDateLayoutContent.addComponent(contractTotalLayout);
		
		//Guaranteed
		HorizontalLayout contractGuaranteedLayout = new HorizontalLayout();
		contractGuaranteedLayout.addComponent(new Label(I18N.message("contract.of.guaranteed")));
		contractGuaranteedLayout.addComponent(new Label("(" + contractGuaranteed + "/40 of adjusted)"));
		
		eachDueDateLayoutContent.addComponent(contractGuaranteedLayout);
		
		Panel brandPanel = new Panel();
		brandPanel.setStyleName(Reindeer.PANEL_LIGHT);
		brandPanel.setContent(verticalLayout);
		
		Panel dueDatePanel = new Panel();
		dueDatePanel.setStyleName(Reindeer.PANEL_LIGHT);
		dueDatePanel.setContent(eachDueDateLayoutContent);
		
		HorizontalLayout content = new HorizontalLayout();
		content.addComponent(brandPanel);
		content.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		content.addComponent(dueDatePanel);
		
		Panel panel = new Panel();
		panel.setCaption(I18N.message("detail"));
		panel.setContent(content);
		
		return panel;
	}

}
