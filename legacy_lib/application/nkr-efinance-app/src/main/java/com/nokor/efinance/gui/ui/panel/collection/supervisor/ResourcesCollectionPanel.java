package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.common.security.model.SecUserDeptLevel;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Resource Pop up Panel
 * @author bunlong.taing
 */
public class ResourcesCollectionPanel extends AbstractTabPanel implements FinServicesHelper {
	/** */
	private static final long serialVersionUID = -2436438887791152332L;
	
	private SimpleTable<SecUser> staffTable;
	private EColType colType;

	/**
	 * @param colType
	 */
	public ResourcesCollectionPanel(final EColType colType) {
		this.colType = colType;
		staffTable = new SimpleTable<>(createStaffTableColumnDefinition(colType));
		staffTable.addItemClickListener(new ItemClickListener() {
			
			/** */
			private static final long serialVersionUID = -1557574503745187258L;
			
			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					SecUser selectedUser = (SecUser) event.getItemId();
					String profile = IProfileCode.COL_PHO_STAFF;
					if (EColType.FIELD.equals(colType)) {
						profile = IProfileCode.COL_FIE_STAFF;
					} else if (EColType.INSIDE_REPO.equals(colType)) {
						profile = IProfileCode.COL_INS_STAFF;
					} else if (EColType.OA.equals(colType)) {
						profile = IProfileCode.COL_OA_STAFF;
					}
					
					ResourcePanel resourcePanel = ResourcePanel.show(profile, new ResourcePanel.Listener() {
						/**
						 */
						private static final long serialVersionUID = -840176672952065698L;

						@Override
						public void onClose(ResourcePanel dialog) {
							setStaffTableContainerDataSource(colType);
						}
					});
					resourcePanel.assignValues(selectedUser);
					UI.getCurrent().addWindow(resourcePanel);
				}
			}
		});
			
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(staffTable);
		addComponent(verticalLayout);
		setStaffTableContainerDataSource(colType);
	}
	
	public void assignValues() {
		setStaffTableContainerDataSource(colType);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		return new VerticalLayout();
	}
	
	/**
	 * Create Staff Table Column Definition
	 * @return
	 */
	private List<ColumnDefinition> createStaffTableColumnDefinition(EColType colType) {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(SecUser.LOGIN, I18N.message("code"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(SecUser.DESC, I18N.message("name"), String.class, Align.LEFT, 150));
		if (EColType.PHONE.equals(colType)) {
			columnDefinitions.add(new ColumnDefinition("odm0", I18N.message("odm0"), String.class, Align.CENTER, 50));
		}
		if (EColType.PHONE.equals(colType) || EColType.FIELD.equals(colType) || EColType.INSIDE_REPO.equals(colType) || EColType.OA.equals(colType)) {
			columnDefinitions.add(new ColumnDefinition("odm1", I18N.message("odm1"), String.class, Align.CENTER, 50));
			columnDefinitions.add(new ColumnDefinition("odm2", I18N.message("odm2"), String.class, Align.CENTER, 50));
			columnDefinitions.add(new ColumnDefinition("odm3", I18N.message("odm3"), String.class, Align.CENTER, 50));
		} 
		if (EColType.FIELD.equals(colType) || EColType.INSIDE_REPO.equals(colType)|| EColType.OA.equals(colType)) {
			columnDefinitions.add(new ColumnDefinition("odm4", I18N.message("odm4"), String.class, Align.CENTER, 50));
			columnDefinitions.add(new ColumnDefinition("odm5", I18N.message("odm5"), String.class, Align.CENTER, 50));
		}
		if (EColType.INSIDE_REPO.equals(colType) || EColType.OA.equals(colType)) {
			columnDefinitions.add(new ColumnDefinition("odm6", I18N.message("odm6"), String.class, Align.CENTER, 50));
			columnDefinitions.add(new ColumnDefinition("odm7", I18N.message("odm7"), String.class, Align.CENTER, 50));
		}
		
		columnDefinitions.add(new ColumnDefinition("enable.assigned.contract", I18N.message("enable.assigned.contract"), String.class, Align.CENTER, 150));
		columnDefinitions.add(new ColumnDefinition("phone", I18N.message("phone"), String.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("branch", I18N.message("branch"), String.class, Align.LEFT, 110));
		return columnDefinitions;
	}
	
	/**
	 * Set Staff Table Container Data Source
	 */
	@SuppressWarnings("unchecked")
	private void setStaffTableContainerDataSource(EColType colType) {
		staffTable.removeAllItems();
		String[] profiles = new String[] {IProfileCode.COL_PHO_STAFF};
		if (EColType.FIELD.equals(colType)) {
			profiles = new String[] {IProfileCode.COL_FIE_STAFF};
		} else if (EColType.INSIDE_REPO.equals(colType)) {
			profiles = new String[] {IProfileCode.COL_INS_STAFF};
		} else if (EColType.OA.equals(colType)) {
			profiles = new String[] {IProfileCode.COL_OA_STAFF};
		}
		
		for (SecUser user : COL_SRV.getCollectionUsers(profiles)) {
			Item item = staffTable.addItem(user);
			item.getItemProperty(SecUser.LOGIN).setValue(user != null ? user.getLogin() : "");
			item.getItemProperty(SecUser.DESC).setValue(user != null ? user.getDesc() : "");

			List<SecUserDeptLevel> secUserDeptLevels = COL_SRV.getSecUserDeptLevel(user.getId());
			String isODM0 = "";
			String isODM1 = "";
			String isODM2 = "";
			String isODM3 = "";
			String isODM4 = "";
			String isODM5 = "";
			String isODM6 = "";
			String isODM7 = "";
			
			if (secUserDeptLevels != null && !secUserDeptLevels.isEmpty()) {
				for (SecUserDeptLevel secUserDeptLevel : secUserDeptLevels) {
					if (secUserDeptLevel.getDebtLevel() == 0) {
						isODM0 = "x";
					} else if (secUserDeptLevel.getDebtLevel() == 1) {
						isODM1 = "x";
					} else if (secUserDeptLevel.getDebtLevel() == 2) {	
						isODM2 = "x";
					} else if (secUserDeptLevel.getDebtLevel() == 3) {
						isODM3 = "x";
					} else if (secUserDeptLevel.getDebtLevel() == 4) {
						isODM4 = "x";
					} else if (secUserDeptLevel.getDebtLevel() == 5) {
						isODM5 = "x";
					} else if (secUserDeptLevel.getDebtLevel() == 6) {
						isODM6 = "x";
					} else if (secUserDeptLevel.getDebtLevel() == 7) {
						isODM7 = "x";
					}
				}
			}
			if (EColType.PHONE.equals(colType)) {
				item.getItemProperty("odm0").setValue(isODM0);
			}
			if (EColType.PHONE.equals(colType) || EColType.FIELD.equals(colType) || EColType.INSIDE_REPO.equals(colType) || EColType.OA.equals(colType)) {
				item.getItemProperty("odm1").setValue(isODM1);
				item.getItemProperty("odm2").setValue(isODM2);
				item.getItemProperty("odm3").setValue(isODM3);
			} 
			
			if (EColType.FIELD.equals(colType) || EColType.INSIDE_REPO.equals(colType) || EColType.OA.equals(colType)) {
				item.getItemProperty("odm4").setValue(isODM4);
				item.getItemProperty("odm5").setValue(isODM5);
			} 
			if (EColType.INSIDE_REPO.equals(colType) || EColType.OA.equals(colType)) {
				item.getItemProperty("odm6").setValue(isODM6);
				item.getItemProperty("odm7").setValue(isODM7);
			}
			
			SecUserDetail secUserDetail = getUserDetail(user);
			if (secUserDetail != null) {
				item.getItemProperty("enable.assigned.contract").setValue(secUserDetail.isEnableAssignContracts()  ? "x" : "");
			}
		}
	}
	/**
	 * 
	 * @param secUser
	 * @return
	 */
	private SecUserDetail getUserDetail(SecUser secUser) {
		BaseRestrictions<SecUserDetail> restrictions = new BaseRestrictions<>(SecUserDetail.class);
		restrictions.addCriterion(Restrictions.eq(SecUserDetail.SECUSER + "." + SecUserDetail.ID, secUser.getId()));
		List<SecUserDetail> userDetails = ENTITY_SRV.list(restrictions);
		if (!userDetails.isEmpty()) {
			return userDetails.get(0);
		}
		return null;
	}
}
