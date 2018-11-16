package com.nokor.efinance.gui.ui.panel.collection.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.core.widget.UsersSelectPanel;
import com.nokor.efinance.gui.ui.panel.collection.ColContractTablePanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;

/**
 * @author uhout.cheng
 */
public class LeaderColContractFilterPanel extends AbstractContractFilterPanel implements MCollection, ValueChangeListener {

	private static final long serialVersionUID = -8740744562595680770L;
	
	private ComboBox cbxStaff;
	private NativeButton btnSelect;
	
	private CheckBox cbDueDay0105;
	private CheckBox cbDueDay0610;
	private CheckBox cbDueDay1115;
	private CheckBox cbDueDay1620;
	
	private CheckBox cbGuaranteeYes;
	private CheckBox cbGuaranteeNo;
	
	private CheckBox cbNextActionToday;
	private CheckBox cbNextAction3Days;
	private CheckBox cbNextAction7Days;
	private CheckBox cbNextAction15Days;
	private CheckBox cbNoneNextActionDate;
	
	private CheckBox cbShowCleared;
	
	private List<SecUser> selectUsers = null;
	private Label selectUsersLabel;
	
	/** 
	 * @param caption
	 * @return
	 */
	private CheckBox getCheckBox(String caption) {
		CheckBox checkBox = new CheckBox(I18N.message(caption));
		checkBox.setValue(true);
		checkBox.addValueChangeListener(this);
		return checkBox;
	}
	
	/** 
	 * 
	 * @param collectionContractTablePanel
	 */
	public LeaderColContractFilterPanel(final ColContractTablePanel contractTablePanel) {
		super(contractTablePanel);
		setWidth(600, Unit.PIXELS);
		addComponent(createForm());
	}	
			

	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		refresh();
	}

	private void refresh() {
		super.contractTablePanel.refresh(getRestrictions());
	}
	
	/**
	 * @return
	 */
	protected Component createForm() {
		
		cbxStaff = new ComboBox();
		StaffComboBoxItem allItem = new StaffComboBoxItem("ALL", I18N.message("all"));
		StaffComboBoxItem selectItem = new StaffComboBoxItem("SEL", I18N.message("select"));
		
		cbxStaff.addItem(allItem);
		cbxStaff.addItem(selectItem);
		cbxStaff.setNullSelectionAllowed(false);
		cbxStaff.setValue(allItem);
		cbxStaff.addValueChangeListener(new ValueChangeListener() {			
			/**
			 */
			private static final long serialVersionUID = 7719144447997706712L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				btnSelect.setVisible(false);
				if (cbxStaff.getValue() != null) {
					StaffComboBoxItem staffComboBoxItem = (StaffComboBoxItem) cbxStaff.getValue();
					if ("SEL".equals(staffComboBoxItem.getCode())) {
						btnSelect.setVisible(true);
						showUsersSelectPanel();
					} else {
						selectUsers = null;
						selectUsersLabel.setCaption("");
						refresh();
					}					
				}
			}
		});
		btnSelect = new NativeButton(I18N.message("select"));
		btnSelect.setVisible(false);
		btnSelect.addStyleName("btn btn-success");
		btnSelect.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -911158149367454852L;
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				showUsersSelectPanel();
			}
		});
				
		selectUsersLabel = new Label();
		
		cbDueDay0105 = getCheckBox("01-05");
		cbDueDay0610 = getCheckBox("06-10");
		cbDueDay1115 = getCheckBox("11-15");
		cbDueDay1620 = getCheckBox("16-20");
		
		cbGuaranteeYes = getCheckBox("yes");
		cbGuaranteeNo = getCheckBox("no");
		
		cbNextActionToday = getCheckBox("today");
		cbNextAction3Days = getCheckBox("3.days");
		cbNextAction7Days = getCheckBox("7.days");
		cbNextAction15Days = getCheckBox("15.days+");
		cbNoneNextActionDate = getCheckBox("none");
		
		cbShowCleared = getCheckBox("show.cleared");
		
		FieldSet dueDayFieldSet = new FieldSet();
		
		HorizontalLayout dueDayLayout = new HorizontalLayout();
		dueDayLayout.addComponent(cbDueDay0105);
		dueDayLayout.addComponent(cbDueDay0610);
		dueDayLayout.addComponent(cbDueDay1115);
		dueDayLayout.addComponent(cbDueDay1620);
		dueDayFieldSet.setLegend(I18N.message("due.day"));
		dueDayFieldSet.setContent(dueDayLayout);
		
		FieldSet guaranteeFieldSet = new FieldSet();
		
		HorizontalLayout guaranteeLayout = new HorizontalLayout();
		guaranteeLayout.addComponent(cbGuaranteeYes);
		guaranteeLayout.addComponent(cbGuaranteeNo);
		guaranteeFieldSet.setLegend(I18N.message("guarantee"));
		guaranteeFieldSet.setContent(guaranteeLayout);
		
		FieldSet nextActionFieldSet = new FieldSet();
		
		HorizontalLayout nextActionLayout = new HorizontalLayout();
		nextActionLayout.addComponent(cbNextActionToday);
		nextActionLayout.addComponent(cbNextAction3Days);
		nextActionLayout.addComponent(cbNextAction7Days);
		nextActionLayout.addComponent(cbNextAction15Days);
		nextActionLayout.addComponent(cbNoneNextActionDate);
		nextActionFieldSet.setLegend(I18N.message("next.action.date"));
		nextActionFieldSet.setContent(nextActionLayout);
		
		FieldSet overduesFieldSet = new FieldSet();
		overduesFieldSet.setLegend(I18N.message("overdues"));
		overduesFieldSet.setContent(cbShowCleared);
		
		HorizontalLayout searchLayout = new HorizontalLayout();
		searchLayout.addComponent(dueDayFieldSet);
		searchLayout.addComponent(guaranteeFieldSet);
		searchLayout.addComponent(nextActionFieldSet);
		searchLayout.addComponent(overduesFieldSet);
		
		Label lblStaff = ComponentFactory.getLabel("staff");
		GridLayout staffGridLayout = new GridLayout(3, 1);
		staffGridLayout.setSpacing(true);
		staffGridLayout.addComponent(lblStaff);
		staffGridLayout.addComponent(cbxStaff);
		staffGridLayout.addComponent(btnSelect);
		staffGridLayout.setComponentAlignment(lblStaff, Alignment.MIDDLE_LEFT);
		
		VerticalLayout staffLayout = new VerticalLayout();
		staffLayout.setSpacing(true);
		staffLayout.addComponent(staffGridLayout);
		staffLayout.addComponent(selectUsersLabel);
		
		VerticalLayout contentLayout = new VerticalLayout();		
		contentLayout.addComponent(staffLayout);
		
		contentLayout.addComponent(searchLayout);
		
		return contentLayout;		
	}
	
	/**
	 * Show Users select panel
	 */
	private void showUsersSelectPanel() {
		UsersSelectPanel usersSelectPanel = new UsersSelectPanel();
		usersSelectPanel.show(new String[] {IProfileCode.COL_PHO_STAFF}, new UsersSelectPanel.Listener() {				
			private static final long serialVersionUID = 7757863260440994161L;
			@Override
			public void onClose(UsersSelectPanel dialog) {
				selectUsers = dialog.getSelectedIds();
				String selectUsersDesc = "";
				if (selectUsers != null && !selectUsers.isEmpty()) {
					for (SecUser user : selectUsers) {
						if (!selectUsersDesc.isEmpty()) {
							selectUsersDesc += " - ";
						}
						selectUsersDesc += user.getLogin();
					}									
				}
				selectUsersLabel.setCaption(selectUsersDesc);
				refresh();
			}
		});
	}

	@Override
	public ContractRestriction getRestrictions() {
		
		ContractRestriction restrictions = new ContractRestriction();
		restrictions.addAssociation("collections", "col", JoinType.INNER_JOIN);
		
		if (!cbGuaranteeYes.getValue() && !cbGuaranteeNo.getValue()) {
			restrictions.addCriterion(Restrictions.and(Restrictions.ge("numberGuarantors", 1), Restrictions.eq("numberGuarantors", 0)));
		} else {
			restrictions.setGuaranteed(cbGuaranteeYes.getValue());
			restrictions.setNotGuaranteed(cbGuaranteeNo.getValue());
		}
		
		
		Date toDay = DateUtils.todayH00M00S00();
		Date tomorrow = DateUtils.addDaysDate(toDay, 1);
		Date toNext4Days = DateUtils.addDaysDate(toDay, 4);
		Date toNext8Days = DateUtils.addDaysDate(toDay, 8);
		
		List<Criterion> criDueDays = new ArrayList<>();
		if (cbDueDay0105.getValue()) {
			criDueDays.add(Restrictions.and(Restrictions.ge("col." + Collection.DUEDAY, 1), Restrictions.le("col." + Collection.DUEDAY, 5)));
		}
		if (cbDueDay0610.getValue()) {
			criDueDays.add(Restrictions.and(Restrictions.ge("col." + Collection.DUEDAY, 6), Restrictions.le("col." + Collection.DUEDAY, 10)));
			
		}
		if (cbDueDay1115.getValue()) {
			criDueDays.add(Restrictions.and(Restrictions.ge("col." + Collection.DUEDAY, 11), Restrictions.le("col." + Collection.DUEDAY, 15)));
		}
		if (cbDueDay1620.getValue()) {
			criDueDays.add(Restrictions.and(Restrictions.ge("col." + Collection.DUEDAY, 16), Restrictions.le("col." + Collection.DUEDAY, 20)));
		}
		
		if (!criDueDays.isEmpty()) {
			restrictions.addCriterion(Restrictions.or(criDueDays.toArray(new Criterion[criDueDays.size()])));
		} else {
			restrictions.addCriterion(Restrictions.eq("col." + Collection.DUEDAY, -99999));
		}
				
		if (cbNextActionToday.getValue() || cbNextAction3Days.getValue() || cbNextAction7Days.getValue() || cbNextAction15Days.getValue()) {
			restrictions.addAssociation("col.lastAction", "lasAct", JoinType.LEFT_OUTER_JOIN);
		}
		
		List<Criterion> criNextActions = new ArrayList<>();
		if (cbNextActionToday.getValue()) {
			criNextActions.add(Restrictions.and(Restrictions.ge("lasAct." + CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtBeginningOfDay(toDay)), 
					Restrictions.le("lasAct." + CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtEndOfDay(toDay))));
		}
		if (cbNextAction3Days.getValue()) {
			criNextActions.add(Restrictions.and(Restrictions.ge("lasAct." + CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtBeginningOfDay(tomorrow)), 
					Restrictions.le("lasAct." + CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtEndOfDay(DateUtils.addDaysDate(tomorrow, 2)))));
		}
		if (cbNextAction7Days.getValue()) {
			criNextActions.add(Restrictions.and(Restrictions.ge("lasAct." + CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtBeginningOfDay(toNext4Days)), 
					Restrictions.le("lasAct." + CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtEndOfDay(DateUtils.addDaysDate(toNext4Days, 3)))));
		}
		if (cbNextAction15Days.getValue()) {
			criNextActions.add(Restrictions.ge("lasAct." + CollectionAction.NEXTACTIONDATE, DateUtils.getDateAtBeginningOfDay(toNext8Days)));
		}
		if (cbNoneNextActionDate.getValue()) {
			criNextActions.add(Restrictions.isNull("col.lastAction"));
		}
		
		if (!criNextActions.isEmpty()) {
			restrictions.addCriterion(Restrictions.or(criNextActions.toArray(new Criterion[criNextActions.size()])));
		} else {
			restrictions.addCriterion(Restrictions.eq("col.lastAction.id", -99999l));
		}
		
		if (selectUsers != null && !selectUsers.isEmpty()) {
			DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
			// userContractSubCriteria.createAlias("cousr.secUser", "usr", JoinType.INNER_JOIN);
			userContractSubCriteria.add(Restrictions.in("cousr.secUser", selectUsers));
			userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
				
			restrictions.addCriterion(Property.forName(ContractUserInbox.ID).in(userContractSubCriteria));
		}
		return restrictions;		
	}

	private class StaffComboBoxItem implements Serializable {
		/** 
		 */
		private static final long serialVersionUID = 4487943333257237511L;
		private String code;
		private String label;
		public StaffComboBoxItem(String code, String label) {
			this.code = code;
			this.label = label;
		}		
		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		@Override
		public String toString() {
			return label;
		}
	}
}
