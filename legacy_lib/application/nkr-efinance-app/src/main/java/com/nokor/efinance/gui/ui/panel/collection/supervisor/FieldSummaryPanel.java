package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.collection.service.FieldSummary;
import com.nokor.efinance.core.collection.service.FieldSummary.AreaSummary;
import com.nokor.efinance.core.collection.service.FieldSummary.DetailSummary;
import com.nokor.efinance.core.collection.service.FieldSummary.StaffSummary;
import com.nokor.efinance.core.collection.service.FieldSummary.TeamSummary;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.security.model.SecUser;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Dashboard Profile Collection Field Supervisor
 * @author buntha.chea
 *
 */
public class FieldSummaryPanel extends VerticalLayout implements FMEntityField, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -788660794010976278L;
	
	private static String OPEN_TABLE = "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid #bdbdbd; "
			+ "border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"50px\" bgcolor=\"#8dc63f\""+"style=\"border:1px solid #bdbdbd;\" >";
	private static String OPEN_TD = "<td class=\"align-center\" style=\"border:1px solid #bdbdbd;\">";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	private int contractTotal = 0;
	
	private Integer nbFlaggedContractGuaranteed;
	private Integer nbFlaggedContractNonGuaranteed;
	
	private Integer nbODM4ContractGuaranteed;
	private Integer nbODM4ContractNonGuaranteed;
	
	private Integer nbODM5ContractGuaranteed;
	private Integer nbODM5ContractNonGuaranteed;
	
	private Integer subTotalNbContractGuaranteed;
	private Integer subTotalNbContractNonGuaranteed;
	
	private Panel summaryPanel;
	
	public FieldSummaryPanel() {
		setCaption(I18N.message("summary"));
		setSizeFull();
		setMargin(true);
		
		summaryPanel = new Panel();
		summaryPanel.setStyleName(Reindeer.PANEL_LIGHT);
		summaryPanel.setContent(createFieldSuperDashboard());
		
		VerticalLayout dashboardLayout = new VerticalLayout();
		dashboardLayout.setSpacing(true);
		dashboardLayout.addComponent(summaryPanel);
		
		Panel mainPanel = new Panel();
		mainPanel.setContent(dashboardLayout);
		
		addComponent(mainPanel);
	}
	
	/**
	 * Field Super Dashboard
	 */
	private CustomLayout createFieldSuperDashboard() {
		CustomLayout fieldSuperDashboardCustomLayout = new CustomLayout("xxx");
			
		FieldSummary fieldSummary = COL_SRV.getFieldSummaries();
		
		//Create Table Header
		String dashboardTemplate = OPEN_TABLE;
		
			dashboardTemplate += OPEN_TR;
				dashboardTemplate += "<th rowspan=\"3\" class=\"align-center\" width=\"60px\" bgcolor=\"#8dc63f\""+
			"style=\"border:1px solid #bdbdbd;\" >";
				
				dashboardTemplate += "<div location =\"lblTeam\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += "<th rowspan=\"3\" class=\"align-center\" width=\"90px\" bgcolor=\"#8dc63f\""+
				"style=\"border:1px solid #bdbdbd;\" >";
				
				dashboardTemplate += "<div location =\"lblStaff\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += "<th rowspan=\"3\" class=\"align-center\" width=\"50px\" bgcolor=\"#8dc63f\""+""
						+ "style=\"border:1px solid #bdbdbd;\" >";
				
				dashboardTemplate += "<div location =\"lblArea\" />";
				dashboardTemplate += CLOSE_TH;
			dashboardTemplate += CLOSE_TR;
			
			dashboardTemplate += OPEN_TR;
				dashboardTemplate += "<th colspan=\"2\" class=\"align-center\" width=\"50px\" bgcolor=\"#8dc63f\""+
			"style=\"border:1px solid #bdbdbd;\" >";
				
				dashboardTemplate += "<div location =\"labFlag\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += "<th colspan=\"2\" class=\"align-center\" width=\"50px\" bgcolor=\"#8dc63f\""+
				"style=\"border:1px solid #bdbdbd;\" >";
				
				dashboardTemplate += "<div location =\"lblOdm4\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += "<th colspan=\"2\" class=\"align-center\" width=\"50px\" bgcolor=\"#8dc63f\""+
				"style=\"border:1px solid #bdbdbd;\" >";
				
				dashboardTemplate += "<div location =\"lblOdm5\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += "<th colspan=\"2\" class=\"align-center\" width=\"50px\" bgcolor=\"#8dc63f\""+
				"style=\"border:1px solid #bdbdbd;\" >";
				
				dashboardTemplate += "<div location =\"lblSubTotal\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += "<th class=\"align-center\" width=\"50px\" bgcolor=\"#8dc63f\""+
						"style=\"border:1px solid #bdbdbd;\" >";
						
						dashboardTemplate += "<div location =\"lblTotal\" />";
						dashboardTemplate += CLOSE_TH;
						
			dashboardTemplate += CLOSE_TR;
			
			dashboardTemplate += OPEN_TR;
				dashboardTemplate += OPEN_TH;
				dashboardTemplate += "<div location =\"lblGuarantor1\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += OPEN_TH;
				dashboardTemplate += "<div location =\"lblNonGuarantor1\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += OPEN_TH;
				dashboardTemplate += "<div location =\"lblGuarantor2\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += OPEN_TH;
				dashboardTemplate += "<div location =\"lblNonGuarantor2\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += OPEN_TH;
				dashboardTemplate += "<div location =\"lblGuarantor3\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += OPEN_TH;
				dashboardTemplate += "<div location =\"lblNonGuarantor3\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += OPEN_TH;
				dashboardTemplate += "<div location =\"lblGuarantor4\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += OPEN_TH;
				dashboardTemplate += "<div location =\"lblNonGuarantor4\" />";
				dashboardTemplate += CLOSE_TH;
				
				dashboardTemplate += OPEN_TH;
				dashboardTemplate += "<div location =\"lblPreAssigned\" />";
				dashboardTemplate += CLOSE_TH;
			dashboardTemplate += CLOSE_TR;
			
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("teams")), "lblTeam");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("staffs")), "lblStaff");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("areas")), "lblArea");
			
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("flagged.by.passed")), "labFlag");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("odm.four")), "lblOdm4");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("odm.five")), "lblOdm5");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("sub.total")), "lblSubTotal");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("total")), "lblTotal");
			
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("guaranteed")), "lblGuarantor1");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("non.guaranteed")), "lblNonGuarantor1");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("guaranteed")), "lblGuarantor2");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("non.guaranteed")), "lblNonGuarantor2");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("guaranteed")), "lblGuarantor3");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("non.guaranteed")), "lblNonGuarantor3");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("guaranteed")), "lblGuarantor4");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("non.guaranteed")), "lblNonGuarantor4");
			fieldSuperDashboardCustomLayout.addComponent(new Label(I18N.message("pre.assigned")), "lblPreAssigned");
			//Create Table Content	
			int colAreaIndex = 1;
			int colTeamIndex = 1;
			int colStaffIndex = 1;
			for (TeamSummary teamSummary : fieldSummary.getSummaries()) {
				List<StaffSummary> staffSummaries = teamSummary.getSummaries();
				
				dashboardTemplate += OPEN_TR;
				dashboardTemplate += "<td rowspan=\"" + countAreas(staffSummaries) + "\"class=\"align-center\" style=\"border:1px solid #bdbdbd;\">";
				dashboardTemplate += "<div location =\"lblTeam" + colTeamIndex + "\" />";
				dashboardTemplate += CLOSE_TD;
				dashboardTemplate += CLOSE_TR;
				
				for (StaffSummary staffSummary : staffSummaries) {
					List<AreaSummary> areaSummaries = staffSummary.getSummaries();
				
					int rowSpanSize = areaSummaries.size() + 1;
					dashboardTemplate += OPEN_TR;
					dashboardTemplate += "<td rowspan=\"" + rowSpanSize + "\"class=\"align-center\" style=\"border:1px solid #bdbdbd;\">";
					dashboardTemplate += "<div location =\"lblStaff" + colStaffIndex + "\" />";
					dashboardTemplate += CLOSE_TD;
					//dashboardTemplate += CLOSE_TR;
					
					for (AreaSummary areaSummary : areaSummaries) {
						dashboardTemplate += OPEN_TR;
						dashboardTemplate += OPEN_TD;
						dashboardTemplate += "<div location =\"lblArea" + colAreaIndex + "\" />";
						dashboardTemplate += CLOSE_TD;
						
						dashboardTemplate += OPEN_TD;
						dashboardTemplate += "<div location =\"lblFlggedGuarateed" + colAreaIndex + "\" />";
						dashboardTemplate += CLOSE_TD;
						
						dashboardTemplate += OPEN_TD;
						dashboardTemplate += "<div location =\"lblFlggedNonGuarateed" + colAreaIndex + "\" />";
						dashboardTemplate += CLOSE_TD;
						
						dashboardTemplate += OPEN_TD;
						dashboardTemplate += "<div location =\"lblODM4Guarateed" + colAreaIndex + "\" />";
						dashboardTemplate += CLOSE_TD;
						
						dashboardTemplate += OPEN_TD;
						dashboardTemplate += "<div location =\"lblODM4NonGuarateed" + colAreaIndex + "\" />";
						dashboardTemplate += CLOSE_TD;
						
						dashboardTemplate += OPEN_TD;
						dashboardTemplate += "<div location =\"lblODM5Guarateed" + colAreaIndex + "\" />";
						dashboardTemplate += CLOSE_TD;
						
						dashboardTemplate += OPEN_TD;
						dashboardTemplate += "<div location =\"lblODM5NonGuarateed" + colAreaIndex + "\" />";
						dashboardTemplate += CLOSE_TD;
						
						dashboardTemplate += OPEN_TD;
						dashboardTemplate += "<div location =\"lblSubTotalGuarateed" + colAreaIndex + "\" />";
						dashboardTemplate += CLOSE_TD;
						
						dashboardTemplate += OPEN_TD;
						dashboardTemplate += "<div location =\"lblSubTotalNonGuarateed" + colAreaIndex + "\" />";
						dashboardTemplate += CLOSE_TD;
						
						dashboardTemplate += OPEN_TD;
						dashboardTemplate += "<div location =\"lblTotal" + colAreaIndex + "\" />";
						dashboardTemplate += CLOSE_TD;
						
						dashboardTemplate += CLOSE_TR;
						
						
						//Set Value to table 
						nbFlaggedContractGuaranteed = 0;
						nbFlaggedContractNonGuaranteed = 0;
						nbODM4ContractGuaranteed = 0;
						nbODM4ContractNonGuaranteed = 0;
						nbODM5ContractGuaranteed = 0;
						nbODM5ContractNonGuaranteed =0;
						subTotalNbContractGuaranteed = 0;
						subTotalNbContractNonGuaranteed = 0;
						contractTotal = 0;
						for (DetailSummary detailSummary : areaSummary.getSummaries()) {
							if (FieldSummary.FLAG == detailSummary.getDebLevel()) {
								nbFlaggedContractGuaranteed = detailSummary.getNbContractGuaranteed();
								nbFlaggedContractNonGuaranteed = detailSummary.getNbContractNonGuaranteed();
								
								subTotalNbContractGuaranteed += detailSummary.getNbContractGuaranteed();
								subTotalNbContractNonGuaranteed += detailSummary.getNbContractNonGuaranteed();
								
							} else if (FieldSummary.ODM4 == detailSummary.getDebLevel()) {
								nbODM4ContractGuaranteed = detailSummary.getNbContractGuaranteed();
								nbODM4ContractNonGuaranteed = detailSummary.getNbContractNonGuaranteed();
								
								subTotalNbContractGuaranteed += detailSummary.getNbContractGuaranteed();
								subTotalNbContractNonGuaranteed += detailSummary.getNbContractNonGuaranteed();
								
							} else if (FieldSummary.ODM5 == detailSummary.getDebLevel()) {
								nbODM5ContractGuaranteed = detailSummary.getNbContractGuaranteed();
								nbODM5ContractNonGuaranteed = detailSummary.getNbContractNonGuaranteed();
								
								subTotalNbContractGuaranteed += detailSummary.getNbContractGuaranteed();
								subTotalNbContractNonGuaranteed += detailSummary.getNbContractNonGuaranteed();
							}
							contractTotal = subTotalNbContractGuaranteed + subTotalNbContractNonGuaranteed;
							
							fieldSuperDashboardCustomLayout.addComponent(new Label(String.valueOf(nbFlaggedContractGuaranteed)), "lblFlggedGuarateed"+ colAreaIndex);
							fieldSuperDashboardCustomLayout.addComponent(new Label(String.valueOf(nbFlaggedContractNonGuaranteed)), "lblFlggedNonGuarateed"+ colAreaIndex);
							fieldSuperDashboardCustomLayout.addComponent(new Label(String.valueOf(nbODM4ContractGuaranteed)), "lblODM4Guarateed"+ colAreaIndex);
							fieldSuperDashboardCustomLayout.addComponent(new Label(String.valueOf(nbODM4ContractNonGuaranteed)), "lblODM4NonGuarateed"+ colAreaIndex);
							fieldSuperDashboardCustomLayout.addComponent(new Label(String.valueOf(nbODM5ContractGuaranteed)), "lblODM5Guarateed"+ colAreaIndex);
							fieldSuperDashboardCustomLayout.addComponent(new Label(String.valueOf(nbODM5ContractNonGuaranteed)), "lblODM5NonGuarateed"+ colAreaIndex);
							fieldSuperDashboardCustomLayout.addComponent(new Label(String.valueOf(subTotalNbContractGuaranteed)), "lblSubTotalGuarateed"+ colAreaIndex);
							fieldSuperDashboardCustomLayout.addComponent(new Label(String.valueOf(subTotalNbContractNonGuaranteed)), "lblSubTotalNonGuarateed"+ colAreaIndex);
							fieldSuperDashboardCustomLayout.addComponent(new Label(String.valueOf(contractTotal)), "lblTotal"+ colAreaIndex);
							
						}
						if (areaSummary.getAreaId() != null) {
							Area area = ENTITY_SRV.getById(Area.class, areaSummary.getAreaId());
							fieldSuperDashboardCustomLayout.addComponent(new Label(area.getDescEn()), "lblArea"+ colAreaIndex);
						}
						colAreaIndex++;
					}
					SecUser staff = ENTITY_SRV.getById(SecUser.class, staffSummary.getStaffId());
					
					fieldSuperDashboardCustomLayout.addComponent(new Label(staff.getDesc()), "lblStaff"+ colStaffIndex);
					colStaffIndex++;
					dashboardTemplate += CLOSE_TR;
				}
				EColTeam colTeam = ENTITY_SRV.getById(EColTeam.class, teamSummary.getTeamId());
				
				fieldSuperDashboardCustomLayout.addComponent(new Label(colTeam.getDescEn()), "lblTeam"+ colTeamIndex);
				colTeamIndex++;
			}
			
		dashboardTemplate += CLOSE_TABLE;
					   
		fieldSuperDashboardCustomLayout.setTemplateContents(dashboardTemplate);
		return fieldSuperDashboardCustomLayout;	   
					  			  
	}
	
	/**
	 * count area for rowspan
	 * @param staffs
	 * @return
	 */
	private int countAreas(List<StaffSummary> staffSummaries) {
		int colAreaIndex = 1;
		for (StaffSummary staffSummary : staffSummaries) {
			for (int i = 0; i < staffSummary.getSummaries().size() + 1; i++) {
				colAreaIndex++;
			}
		}
		return colAreaIndex;
	}	
	/**
	 * Refresh
	 */
	public void refresh() {
		summaryPanel.setContent(createFieldSuperDashboard());
	}
	

}
