package com.nokor.efinance.gui.ui.panel.collection.filter.callcenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.core.widget.UsersSelectPanel;
import com.nokor.efinance.gui.ui.panel.collection.callcenter.leader.assign.CallCenterLeaderContractTablePanel;
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
 * Call Center Assign Contract Filter Panel
 * @author uhout.cheng
 */
public class CallCenterContractFilterPanel extends AbstractCallCenterContractFilterPanel implements MCollection, ValueChangeListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -4522849465779827549L;
	
	private final static String COL = "col";
	private final static String DOT = ".";

	private ComboBox cbxStaff;
	private NativeButton btnSelect;
	
	private CheckBox cbDueDay0105;
	private CheckBox cbDueDay0610;
	private CheckBox cbDueDay1115;
	private CheckBox cbDueDay1620;
	
	private CheckBox cbGuaranteeYes;
	private CheckBox cbGuaranteeNo;

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
	 * @param contractTablePanel
	 */
	public CallCenterContractFilterPanel(final CallCenterLeaderContractTablePanel contractTablePanel) {
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
		super.callCenterLeaderContractTablePanel.refresh(getRestrictions());
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
			
			/** */
			private static final long serialVersionUID = -4117187442489285546L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
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
						selectUsersLabel.setVisible(StringUtils.isNotEmpty(selectUsersLabel.getCaption()));
						refresh();
					}					
				}
			}
		});
		btnSelect = new NativeButton(I18N.message("select"));
		btnSelect.setVisible(false);
		btnSelect.addStyleName("btn btn-success button-small");
		btnSelect.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -2220663994794163276L;

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
		
		HorizontalLayout searchLayout = new HorizontalLayout();
		searchLayout.addComponent(dueDayFieldSet);
		searchLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		searchLayout.addComponent(guaranteeFieldSet);
		
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
	 * Show Users Select Panel
	 */
	private void showUsersSelectPanel() {
		UsersSelectPanel usersSelectPanel = new UsersSelectPanel();
		usersSelectPanel.show(new String[] { IProfileCode.CAL_CEN_STAFF }, new UsersSelectPanel.Listener() {				
			
			/** */
			private static final long serialVersionUID = 5572755908663822570L;

			/**
			 * @see com.nokor.efinance.core.widget.UsersSelectPanel.Listener#onClose(com.nokor.efinance.core.widget.UsersSelectPanel)
			 */
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
				selectUsersLabel.setVisible(StringUtils.isNotEmpty(selectUsersLabel.getCaption()));
				refresh();
			}
		});
	}
	
	/**
	 * @see com.nokor.efinance.gui.ui.panel.collection.filter.callcenter.AbstractCallCenterContractFilterPanel#getRestrictions()
	 */
	@Override
	public ContractRestriction getRestrictions() {
		ContractRestriction restrictions = new ContractRestriction();
		restrictions.addAssociation(Contract.COLLECTIONS, COL, JoinType.INNER_JOIN);
		
		if (!cbGuaranteeYes.getValue() && !cbGuaranteeNo.getValue()) {
			restrictions.addCriterion(Restrictions.and(Restrictions.ge("numberGuarantors", 1), Restrictions.eq("numberGuarantors", 0)));
		} else {
			restrictions.setGuaranteed(cbGuaranteeYes.getValue());
			restrictions.setNotGuaranteed(cbGuaranteeNo.getValue());
		}
		
		List<Criterion> criDueDays = new ArrayList<>();
		if (cbDueDay0105.getValue()) {
			criDueDays.add(Restrictions.and(Restrictions.ge(COL + DOT + Collection.DUEDAY, 1), Restrictions.le(COL + DOT + Collection.DUEDAY, 5)));
		}
		if (cbDueDay0610.getValue()) {
			criDueDays.add(Restrictions.and(Restrictions.ge(COL + DOT + Collection.DUEDAY, 6), Restrictions.le(COL + DOT + Collection.DUEDAY, 10)));
			
		}
		if (cbDueDay1115.getValue()) {
			criDueDays.add(Restrictions.and(Restrictions.ge(COL + DOT + Collection.DUEDAY, 11), Restrictions.le(COL + DOT + Collection.DUEDAY, 15)));
		}
		if (cbDueDay1620.getValue()) {
			criDueDays.add(Restrictions.and(Restrictions.ge(COL + DOT + Collection.DUEDAY, 16), Restrictions.le(COL + DOT + Collection.DUEDAY, 20)));
		}
		
		if (!criDueDays.isEmpty()) {
			restrictions.addCriterion(Restrictions.or(criDueDays.toArray(new Criterion[criDueDays.size()])));
		} else {
			restrictions.addCriterion(Restrictions.eq(COL + DOT + Collection.DUEDAY, -99999));
		}
		
		if (selectUsers == null || selectUsers.isEmpty()) {			
			selectUsers = COL_SRV.getCollectionUsers(new String[] { IProfileCode.CAL_CEN_STAFF }); 
		}
		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria.add(Restrictions.in("cousr.secUser", selectUsers));
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
		
		restrictions.addCriterion(Property.forName(ContractUserInbox.ID).in(userContractSubCriteria));
		return restrictions;		
	}

	/**
	 * @return
	 */
	public List<SecUser> getSelectUsers() {
		return selectUsers;
	}
	
	private class StaffComboBoxItem implements Serializable {
		
		/** */
		private static final long serialVersionUID = 9177721870757941344L;
		
		private String code;
		private String label;
		
		/**
		 * 
		 * @param code
		 * @param label
		 */
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

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return label;
		}
	}
}
