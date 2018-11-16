package com.nokor.efinance.gui.ui.panel.collection.jobdistribution;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;
/**
 * Job distribution search layout in collection
 * @author uhout.cheng
 */
public class JobDistributionSearchPanel extends AbstractSearchPanel<Quotation> {

	/** */
	private static final long serialVersionUID = -235115502039767116L;

	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private EntityRefComboBox<EColGroup> cbxGroup;
	private TextField txtStaffCode ;
	
	/**
	 * 
	 * @param jobDistributionTablePanel
	 */
	public JobDistributionSearchPanel(JobDistributionTablePanel jobDistributionTablePanel) {
		super(I18N.message("adjust.job.distribution"), jobDistributionTablePanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		cbxGroup.setSelectedEntity(null);
		txtStaffCode.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		dfStartDate = ComponentFactory.getAutoDateField();
		dfEndDate = ComponentFactory.getAutoDateField();
		cbxGroup = new EntityRefComboBox<>();
		cbxGroup.setRestrictions(new BaseRestrictions<>(EColGroup.class));
		cbxGroup.renderer();
		cbxGroup.setWidth(120, Unit.PIXELS);
		txtStaffCode = ComponentFactory.getTextField(60, 120);
		
		HorizontalLayout layout = ComponentFactory.getHorizontalLayout();
		layout.addComponent(getSearchPanel());
		
		return layout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getSearchPanel() {
		Panel searchPanel = new Panel();
		searchPanel.setStyleName(Reindeer.PANEL_LIGHT);
		Button btnSearch = ComponentFactory.getButton("search");
		GridLayout gridLayout = new GridLayout(10, 1);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("search.assigment.period")), iCol++, 0);
		gridLayout.addComponent(dfStartDate, iCol++, 0);
		gridLayout.addComponent(new Label(I18N.message("to")), iCol++, 0);
		gridLayout.addComponent(dfEndDate, iCol++, 0);
		gridLayout.addComponent(new Label(I18N.message("group")), iCol++, 0);
		gridLayout.addComponent(cbxGroup, iCol++, 0);
		gridLayout.addComponent(new Label(I18N.message("staff.code")), iCol++, 0);
		gridLayout.addComponent(txtStaffCode, iCol++, 0);
		gridLayout.addComponent(btnSearch, iCol++, 0);
		searchPanel.setContent(gridLayout);
		return searchPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<Quotation> getRestrictions() {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<Quotation>(Quotation.class);	
		return restrictions;
	}
	
}
