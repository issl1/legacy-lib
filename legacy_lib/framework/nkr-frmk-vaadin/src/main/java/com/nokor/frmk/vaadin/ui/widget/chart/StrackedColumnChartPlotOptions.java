package com.nokor.frmk.vaadin.ui.widget.chart;

import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.ColumnChartPlotOptions;

/**
 * 
 * @author phirun.kong
 *
 */
public class StrackedColumnChartPlotOptions extends ColumnChartPlotOptions {

	@Override
    public String getHighChartValue() {
        StringBuilder builder = new StringBuilder();
        builder.append(", plotOptions: { ");
        builder.append(getChartType().getHighChartValue() + ": { ");
        builder.append("stacking: 'normal'");
        builder.append(", allowPointSelect: " + isAllowPointSelect());
        builder.append(", dashStyle: '" + getDashStyle().name() +"'");
        builder.append(", showCheckbox: " + isShowCheckBox());
        builder.append(", step: '" + getSteps().name() + "'");
        builder.append(", connectNulls: " + isConnectNulls());
        builder.append(", animation: " + isAnimated());
        builder.append(", shadow: " + isShadow());
        builder.append(", dataLabels: { ");
        builder.append("enabled: " + isDataLabelsEnabled());
        builder.append(", color: '" + getDataLabelsFontColor().getCSS() + "'");
        builder.append(", style: { ");
        builder.append("fontFamily: '" + getDataLabelsFont() + "'");
        builder.append(", fontWeight: '" + getDataLabelFontWeight() + "'");
        builder.append(", fontSize: '" + getDataLabelsFontSize() + "px'");
        builder.append(", textShadow: " + false);
        builder.append(" }");
        builder.append(" }");
        builder.append(" }");
        builder.append(" }");
        return builder.toString();
    }
	
}
