package com.nokor.efinance.core.asset.panel;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.widget.component.TextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

/**
 * Asset right table layout more detail
 * @author uhout.cheng
 */
public class AssetRightTableLayout extends CustomLayout {

	/** */
	private static final long serialVersionUID = 6707802783181696832L;

	private static String OPEN_TABLE_HAS_BORDER = "<table cellspacing=\"3\" cellpadding=\"3\" style=\"border:1px solid black; border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"120px\" bgcolor=\"#e1e1e1\" "
			+ "style=\"border:1px solid black;\" >";
	private static String OPEN_TD = "<td align=\"left\" style=\"border:1px solid black;\" >";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	/** */
	public AssetRightTableLayout() {
		String template = OPEN_TABLE_HAS_BORDER;
		template += OPEN_TR;
		template += "<th rowspan=\"3\" class=\"align-center\" bgcolor=\"#e1e1e1\" style=\"border:1px solid black;\" >";
		template += "<div location =\"lblCondition\" />";
		template += CLOSE_TH;
		template += "<th colspan=\"6\" class=\"align-center\" bgcolor=\"#e1e1e1\" style=\"border:1px solid black;\" >";
		template += "<div location =\"lblStateGrade\" />";
		template += CLOSE_TH;
		template += CLOSE_TR;
		List<String> locations = new ArrayList<String>();
		locations.add("<div location =\"lblTotallyBroken\" />");
		locations.add("<div location =\"lblBadCondition\" />");
		locations.add("<div location =\"lblAverageCondition\" />");
		locations.add("<div location =\"lblGoodCondition\" />");
		locations.add("<div location =\"lblVeryGoodCondition\" />");
		locations.add("<div location =\"lblBrandNew\" />");
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TH;
			template += string;
			template += CLOSE_TH;
		}
		template += CLOSE_TR;
		
		locations = new ArrayList<String>();
		locations.add("<div location =\"lblZero\" />");
		locations.add("<div location =\"lblOne\" />");
		locations.add("<div location =\"lblTwo\" />");
		locations.add("<div location =\"lblThree\" />");
		locations.add("<div location =\"lblFour\" />");
		locations.add("<div location =\"lblFive\" />");
		template += OPEN_TR;
		for (String string : locations) {
			template += OPEN_TH;
			template += string;
			template += CLOSE_TH;
		}
		template += CLOSE_TR;
		
		locations = new ArrayList<String>();
		locations.add("<div location =\"lblFrontBrakePump\" />");
		locations.add("<div location =\"lblBattery\" />");
		locations.add("<div location =\"lblFrontDisc\" />");
		locations.add("<div location =\"lblCarburatorInjetor\" />");
		locations.add("<div location =\"lblTopBrakePump\" />");
		locations.add("<div location =\"lblExhaustPipe\" />");
		locations.add("<div location =\"lblDistart\" />");
		locations.add("<div location =\"lblEngineNoScratch\" />");
		locations.add("<div location =\"lblChassisNoScratch\" />");
		for (String string : locations) {
			template += OPEN_TR;
			template += OPEN_TD;
			template += string;
			template += CLOSE_TD;
			template += OPEN_TD + CLOSE_TD;
			template += OPEN_TD + CLOSE_TD;
			template += OPEN_TD + CLOSE_TD;
			template += OPEN_TD + CLOSE_TD;
			template += OPEN_TD + CLOSE_TD;
			template += OPEN_TD + CLOSE_TD;
			template += CLOSE_TR;
		}
		template += OPEN_TR;
		template += OPEN_TD;
		template += "<div location =\"lblKey\" />";
		template += CLOSE_TD;
		template += "<td colspan=\"6\" align=\"left\" style=\"border:1px solid black;\" >";
			template += "<table border=\"0\" cellspacing=\"2\" cellpadding=\"0\" >";
			template += "<tr>";
			template += "<td>";
			template += "<div location =\"lblNumber\" />";
			template += CLOSE_TD;
			template += "<td>";
			template += "<div location =\"txtNumber\" />";
			template += CLOSE_TD;
			template += CLOSE_TR;
			template += CLOSE_TABLE;
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		
		template += "<table border=\"0\" cellspacing=\"2\" cellpadding=\"2\" >";
		template += "<tr>";
		template += "<td></td>";
		template += "<td></td>";
		template += "<td align=\"right\" >";
		template += "<div location =\"lblGradeTitle\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += "<td align=\"right\" >";
		template += "<div location =\"lblPercentageTitle\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += "<tr>";
		template += "<td width=\"120px\" align=\"right\" >";
		template += "<div location =\"btnPercentage\" />";
		template += CLOSE_TD;
		template += "<td align=\"right\" >";
		template += "<div location =\"lblAutomaticCalculate\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += "<td align=\"left\" >";
		template += "<div location =\"txtAutoGrade\" />";
		template += CLOSE_TD;
		template += "<td align=\"left\" >";
		template += "<div location =\"txtAutoPercentage\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += "<tr>";
		template += "<td></td>";
		template += "<td align=\"right\" >";
		template += "<div location =\"lblManualCondition\" class =\"requiredfield\" />";
		template += CLOSE_TD;
		template += "<td align=\"left\" >";
		template += "<div location =\"txtManualGrade\" />";
		template += CLOSE_TD;
		template += "<td align=\"left\" >";
		template += "<div location =\"txtManualPercentage\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		
		addComponent(new Label(I18N.message("no.or.total.broken")), "lblTotallyBroken");
		addComponent(new Label(I18N.message("condition")), "lblCondition");
		addComponent(new Label(I18N.message("state.grade")), "lblStateGrade");
		addComponent(new Label(I18N.message("bad.condition")), "lblBadCondition");
		addComponent(new Label(I18N.message("brand")), "lblAverageCondition");
		addComponent(new Label(I18N.message("average.condition")), "lblGoodCondition");
		addComponent(new Label(I18N.message("very.good.condition")), "lblVeryGoodCondition");
		addComponent(new Label(I18N.message("brand.new")), "lblBrandNew");
		addComponent(new Label(I18N.message("0")), "lblZero");
		addComponent(new Label(I18N.message("1")), "lblOne");
		addComponent(new Label(I18N.message("2")), "lblTwo");
		addComponent(new Label(I18N.message("3")), "lblThree");
		addComponent(new Label(I18N.message("4")), "lblFour");
		addComponent(new Label(I18N.message("5")), "lblFive");
		addComponent(new Label(I18N.message("front.brake.pump")), "lblFrontBrakePump");
		addComponent(new Label(I18N.message("battery")), "lblBattery");
		addComponent(new Label(I18N.message("front.disc")), "lblFrontDisc");
		addComponent(new Label(I18N.message("carburator.injetor")), "lblCarburatorInjetor");
		addComponent(new Label(I18N.message("top.brake.pump")), "lblTopBrakePump");
		addComponent(new Label(I18N.message("exhaust.pipe")), "lblExhaustPipe");
		addComponent(new Label(I18N.message("distart")), "lblDistart");
		addComponent(new Label(I18N.message("engine.no.scratch")), "lblEngineNoScratch");
		addComponent(new Label(I18N.message("chassis.no.scratch")), "lblChassisNoScratch");
		addComponent(new Label(I18N.message("key")), "lblKey");
		addComponent(new Label(I18N.message("number")), "lblNumber");
		addComponent(new TextField(), "txtNumber");
		addComponent(new Label(I18N.message("grade")), "lblGradeTitle");
		addComponent(new Label(I18N.message("percentage")), "lblPercentageTitle");
		addComponent(new Label(I18N.message("automatic.calculation")), "lblAutomaticCalculate");
		addComponent(new Label(I18N.message("manual.condition")), "lblManualCondition");
		addComponent(new Button(I18N.message("percentage")), "btnPercentage");
		addComponent(new TextField(), "txtAutoGrade");
		addComponent(new TextField(), "txtAutoPercentage");
		addComponent(new TextField(), "txtManualGrade");
		addComponent(new TextField(), "txtManualPercentage");
		
		setTemplateContents(template);
	}
}
