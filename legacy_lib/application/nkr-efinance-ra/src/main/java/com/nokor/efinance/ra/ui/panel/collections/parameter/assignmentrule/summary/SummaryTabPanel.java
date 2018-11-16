package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.summary;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.EColArea;
import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.collection.model.EColTeamGroup;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * Summary tab in assignment rule
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SummaryTabPanel extends AbstractTabPanel implements CollectionEntityField, FrmkServicesHelper {

	/** */
	private static final long serialVersionUID = 1608587733840293996L;

	private static String OPEN_TABLE = "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; "
			+ "border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"230px\" bgcolor=\"#e1e1e1\" "
			+ "style=\"border:1px solid black;\" >";
	private static String OPEN_TD = "<td class=\"align-left\" style=\"border:1px solid black;\" >";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	/**
	 * 
	 * @return
	 */
	private Label getLabel() {
		Label label = ComponentFactory.getLabel();
		return label;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		Panel numGroupPanel = new Panel(I18N.message("no.group"));
		numGroupPanel.setStyleName(Reindeer.PANEL_LIGHT);
		numGroupPanel.setContent(createNumGroupTable());
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		mainLayout.addComponent(createSummaryTable());
		mainLayout.addComponent(numGroupPanel);
		
		TabSheet mainTab = new TabSheet();
		mainTab.setHeight(750, Unit.PIXELS);
        mainTab.addTab(mainLayout, I18N.message("summaries"));
		
		return mainTab;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout createSummaryTable(){
		CustomLayout summaryCustomLayout = new CustomLayout("xxx");
		String summaryTemplate = OPEN_TABLE;
		summaryTemplate += OPEN_TR;
		summaryTemplate += "<th class=\"align-center\" width=\"50px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		summaryTemplate += "<div location =\"lblNo\" />";
		summaryTemplate += CLOSE_TH;
		summaryTemplate += OPEN_TH;
		summaryTemplate += "<div location =\"lblTeam\" />";
		summaryTemplate += CLOSE_TH;
		summaryTemplate += OPEN_TH;
		summaryTemplate += "<div location =\"lblGroup\" />";
		summaryTemplate += CLOSE_TH;
		summaryTemplate += OPEN_TH;
		summaryTemplate += "<div location =\"lblTeamLeader\" />";
		summaryTemplate += CLOSE_TH;
		summaryTemplate += OPEN_TH;
		summaryTemplate += "<div location =\"lblStaff\" />";
		summaryTemplate += CLOSE_TH;
		summaryTemplate += OPEN_TH;
		summaryTemplate += "<div location =\"lblAreaCode\" />";
		summaryTemplate += CLOSE_TH;
		summaryTemplate += CLOSE_TR;
		summaryCustomLayout.addComponent(new Label(I18N.message("no")), "lblNo");
		summaryCustomLayout.addComponent(new Label(I18N.message("team")), "lblTeam");
		summaryCustomLayout.addComponent(new Label(I18N.message("group")), "lblGroup");
		summaryCustomLayout.addComponent(new Label(I18N.message("team.leader")), "lblTeamLeader");
		summaryCustomLayout.addComponent(new Label(I18N.message("staff")), "lblStaff");
		summaryCustomLayout.addComponent(new Label(I18N.message("area.code")), "lblAreaCode");
		
		List<EColTeamGroup> colTeamGroups = getColTeamGroups();
		if (colTeamGroups != null && !colTeamGroups.isEmpty()) {
			int i = 0;
			for (EColTeamGroup colTeamGroup : colTeamGroups) {
				i++;
				summaryTemplate += OPEN_TR;
				summaryTemplate += OPEN_TD;
				summaryTemplate += "<div location =\"lblNoValue" + i + "\" />";
				summaryTemplate += CLOSE_TD;
				summaryTemplate += OPEN_TD;
				summaryTemplate += "<div location =\"lblTeamValue" + i + "\" />";
				summaryTemplate += CLOSE_TD;
				summaryTemplate += OPEN_TD;
				summaryTemplate += "<div location =\"lblGroupValue" + i + "\" />";
				summaryTemplate += CLOSE_TD;
				summaryTemplate += OPEN_TD;
				summaryTemplate += "<div location =\"lblTeamLeaderValue" + i + "\" />";
				summaryTemplate += CLOSE_TD;
				summaryTemplate += OPEN_TD;
				summaryTemplate += "<div location =\"lblStaffValue" + i + "\" />";
				summaryTemplate += CLOSE_TD;
				summaryTemplate += OPEN_TD;
				summaryTemplate += "<div location =\"lblAreaCodeValue" + i + "\" />";
				summaryTemplate += CLOSE_TD;
				summaryTemplate += CLOSE_TR;
				
				Label lblNoValue = getLabel();
				Label lblTeamValue = getLabel();
				Label lblGroupValue = getLabel();
				Label lblTeamLeaderValue = getLabel();
				Label lblStaffValue = getLabel();
				Label lblAreaCodeValue = getLabel();
				lblStaffValue.setContentMode(ContentMode.HTML);
				lblAreaCodeValue.setContentMode(ContentMode.HTML);
				
				EColGroup colGroup = colTeamGroup.getGroup();
				List<String> staffNames = new ArrayList<String>();
				List<String> areaCodes = new ArrayList<String>();
				if (colGroup != null && !colGroup.getStaffs().isEmpty()) {
					for (SecUser user : colGroup.getStaffs()) {
						List<EColArea> colAreas = getEColAreas(user.getId());
						staffNames.add(user.getDesc());
						if (!colAreas.isEmpty()) {
							List<String> anyAreaCodes = new ArrayList<String>();
							for (EColArea eColArea : colAreas) {
								anyAreaCodes.add(eColArea.getDescEn());
							}
							areaCodes.add(getListAreaCodes(anyAreaCodes)) ;
						}
					}
				}
				
				lblNoValue.setValue(getDefaultString(i));
				lblTeamValue.setValue(colTeamGroup.getTeam() != null ? colTeamGroup.getTeam().getDescEn() : "");
				lblGroupValue.setValue(colTeamGroup.getGroup() != null ? colTeamGroup.getGroup().getDescEn() : "");
				lblTeamLeaderValue.setValue(colTeamGroup.getTeamLeader() != null ? colTeamGroup.getTeamLeader().getDesc() : "");
				lblStaffValue.setValue(getListStaffs(staffNames));
				lblAreaCodeValue.setValue(getListStaffs(areaCodes));
				
				summaryCustomLayout.addComponent(lblNoValue, "lblNoValue" + i);
				summaryCustomLayout.addComponent(lblTeamValue, "lblTeamValue" + i);
				summaryCustomLayout.addComponent(lblGroupValue, "lblGroupValue" + i);
				summaryCustomLayout.addComponent(lblTeamLeaderValue, "lblTeamLeaderValue" + i);
				summaryCustomLayout.addComponent(lblStaffValue, "lblStaffValue" + i);
				summaryCustomLayout.addComponent(lblAreaCodeValue, "lblAreaCodeValue" + i);
			}
		}
		
		summaryTemplate += CLOSE_TABLE;
		summaryCustomLayout.setTemplateContents(summaryTemplate);
		
		return summaryCustomLayout;
	}

	/**
	 * 
	 * @param strings
	 * @return
	 */
	private String getListStaffs(List<String> strings) {
		StringBuffer staffName = new StringBuffer(); 
		if (!strings.isEmpty()) {
			for (String string : strings) {
				staffName.append(string);
				staffName.append("<br>");
			}
		}
		return staffName.toString();
	}
	
	/**
	 * 
	 * @param areaCodes
	 * @return
	 */
	private String getListAreaCodes(List<String> areaCodes) {
		StringBuffer areaCode = new StringBuffer(); 
		if (!areaCodes.isEmpty()) {
			int size = areaCodes.size();
			int i = 0;
			for (String string : areaCodes) {
				i++;
				areaCode.append(string);
				if (i < size) {
					areaCode.append(",&nbsp;");
				}
			}
		}
		return areaCode.toString();
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	private List<EColArea> getEColAreas(Long id) {
		BaseRestrictions<EColArea> restrictions = new BaseRestrictions<>(EColArea.class);
		restrictions.addAssociation(STAFFS, "sta", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("sta." + ID, id));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	private List<SecUser> getSecUserNoGroups(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		BaseRestrictions<SecUser> restrictions = new BaseRestrictions<>(SecUser.class);
		restrictions.addCriterion(Restrictions.not(Restrictions.in(ID, ids)));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<EColTeamGroup> getColTeamGroups() {
		BaseRestrictions<EColTeamGroup> restrictions = new BaseRestrictions<>(EColTeamGroup.class);
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout createNumGroupTable(){
		CustomLayout numGroupSummaryCustomLayout = new CustomLayout("xxx");
		String numGroupSummaryTemplate = OPEN_TABLE;
		numGroupSummaryTemplate += OPEN_TR;
		numGroupSummaryTemplate += "<th class=\"align-center\" width=\"50px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		numGroupSummaryTemplate += "<div location =\"lblNo\" />";
		numGroupSummaryTemplate += CLOSE_TH;
		numGroupSummaryTemplate += "<th class=\"align-center\" width=\"300px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		numGroupSummaryTemplate += "<div location =\"lblStaffCode\" />";
		numGroupSummaryTemplate += CLOSE_TH;
		numGroupSummaryTemplate += "<th class=\"align-center\" width=\"300px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		numGroupSummaryTemplate += "<div location =\"lblZipCode\" />";
		numGroupSummaryTemplate += CLOSE_TH;
		numGroupSummaryTemplate += "<th class=\"align-center\" width=\"300px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		numGroupSummaryTemplate += "<div location =\"lblAreaCode\" />";
		numGroupSummaryTemplate += CLOSE_TH;
		numGroupSummaryTemplate += CLOSE_TR;
		numGroupSummaryCustomLayout.addComponent(new Label(I18N.message("no")), "lblNo");
		numGroupSummaryCustomLayout.addComponent(new Label(I18N.message("staff.code")), "lblStaffCode");
		numGroupSummaryCustomLayout.addComponent(new Label(I18N.message("zip.code")), "lblZipCode");
		numGroupSummaryCustomLayout.addComponent(new Label(I18N.message("area.code")), "lblAreaCode");
		
		List<Long> groupStaffIDs = new ArrayList<Long>();
		List<EColTeamGroup> colTeamGroups = getColTeamGroups();
		if (colTeamGroups != null && !colTeamGroups.isEmpty()) {
			for (EColTeamGroup colTeamGroup : colTeamGroups) {
				if (colTeamGroup != null) {
					EColGroup colGroup = colTeamGroup.getGroup();
					if (colGroup != null && !colGroup.getStaffs().isEmpty()) {
						for (SecUser user : colGroup.getStaffs()) {
							groupStaffIDs.add(user.getId());
						}
					}
				}
			}
		}
		
		List<SecUser> secUsers = getSecUserNoGroups(groupStaffIDs);
		int i = 0;
		if (secUsers != null && !secUsers.isEmpty()) {
			for (SecUser secUser : secUsers) {
				i++;
				List<EColArea> colAreas = getEColAreas(secUser.getId());
				List<String> areaCodes = new ArrayList<String>();
				List<String> postalCodes = new ArrayList<String>();
				if (!colAreas.isEmpty()) {
					for (EColArea eColArea : colAreas) {
						areaCodes.add(eColArea.getDescEn());
						postalCodes.add(eColArea.getPostalCode());
					}
				}
				numGroupSummaryTemplate += OPEN_TR;
				numGroupSummaryTemplate += OPEN_TD;
				numGroupSummaryTemplate += "<div location =\"lblNoValue" + i + "\" />";
				numGroupSummaryTemplate += CLOSE_TD;
				numGroupSummaryTemplate += OPEN_TD;
				numGroupSummaryTemplate += "<div location =\"lblStaffCodeValue" + i + "\" />";
				numGroupSummaryTemplate += CLOSE_TD;
				numGroupSummaryTemplate += OPEN_TD;
				numGroupSummaryTemplate += "<div location =\"lblZipCodeValue" + i + "\" />";
				numGroupSummaryTemplate += CLOSE_TD;
				numGroupSummaryTemplate += OPEN_TD;
				numGroupSummaryTemplate += "<div location =\"lblAreaCodeValue" + i + "\" />";
				numGroupSummaryTemplate += CLOSE_TD;
				numGroupSummaryTemplate += CLOSE_TR;
				
				Label lblNoValue = getLabel();
				Label lblStaffCodeValue = getLabel();
				Label lblZipCodeValue = getLabel();
				Label lblAreaCodeValue = getLabel();
				
				lblNoValue.setValue(getDefaultString(i));
				lblStaffCodeValue.setValue(getDefaultString(secUser.getDesc()));
				lblZipCodeValue.setValue(getListAreaCodes(postalCodes));
				lblAreaCodeValue.setValue(getListAreaCodes(areaCodes));
				lblZipCodeValue.setContentMode(ContentMode.HTML);
				lblAreaCodeValue.setContentMode(ContentMode.HTML);
				
				numGroupSummaryCustomLayout.addComponent(lblNoValue, "lblNoValue" + i);
				numGroupSummaryCustomLayout.addComponent(lblStaffCodeValue, "lblStaffCodeValue" + i);
				numGroupSummaryCustomLayout.addComponent(lblZipCodeValue, "lblZipCodeValue" + i);
				numGroupSummaryCustomLayout.addComponent(lblAreaCodeValue, "lblAreaCodeValue" + i);
			}
		} 
		
		numGroupSummaryTemplate += CLOSE_TABLE;
		numGroupSummaryCustomLayout.setTemplateContents(numGroupSummaryTemplate);
		
		return numGroupSummaryCustomLayout;
	}
}
