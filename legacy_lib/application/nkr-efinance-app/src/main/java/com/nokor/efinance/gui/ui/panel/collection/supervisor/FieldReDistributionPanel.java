package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserSimulInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.Or;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.AbstractSelect.VerticalLocationIs;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
/**
 * Dashboard Profile Collection Field Supervisor
 * @author buntha.chea
 *
 */
public class FieldReDistributionPanel extends VerticalLayout implements ClickListener, FMEntityField, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -3891895329843686084L;
	
	public static final String GROUPBY = "group.by";
	
	private Button btnSave;
	private TreeTable tableStaffTree;
	private VerticalLayout messagePanel;
	
	private List<Integer> staffIds;
	
	/** */
	public FieldReDistributionPanel() {
		staffIds = new ArrayList<Integer>();
		setCaption(I18N.message("redistribution"));
		setSizeFull();
		setMargin(true);

		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		tableStaffTree = createTableStaffTree();
		renderRow();
		
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		
		Panel treepanel = new Panel();
		treepanel.setStyleName(Reindeer.PANEL_LIGHT);
		treepanel.setContent(tableStaffTree);
		
		VerticalLayout treeLayout = new VerticalLayout();
		treeLayout.setSpacing(true);
		
		treeLayout.addComponent(navigationPanel);
		treeLayout.addComponent(messagePanel);
		treeLayout.addComponent(treepanel);

		addComponent(treeLayout);
	}
	
	/**
	 * @return
	 */
	private TreeTable createTableStaffTree() {
		final TreeTable table = new TreeTable();
		table.setStyleName("v-workplace-tree");
		table.setWidth("100%");
		table.setImmediate(true);
		table.setDragMode(TableDragMode.ROW);
		
		table.setPageLength(10);
		table.setSelectable(true);
		table.setSizeFull();
		table.setColumnCollapsingAllowed(true);
		setUpColumnDefinitions(table);
		
		table.setDropHandler(new DropHandler() {
			/** */
			private static final long serialVersionUID = -2215355122695986076L;
			
			/**
			 * @see com.vaadin.event.dd.DropHandler#getAcceptCriterion()
			 */
			@Override
			public AcceptCriterion getAcceptCriterion() {
				// Accept drops in the middle of items that can have
				// children, and below and above all items.
				return new Or(Tree.TargetItemAllowsChildren.get(), new Not(VerticalLocationIs.MIDDLE));
			}
			
			/**
			 * @see com.vaadin.event.dd.DropHandler#drop(com.vaadin.event.dd.DragAndDropEvent)
			 */
			@Override
			public void drop(DragAndDropEvent event) {
				// Wrapper for the object that is dragged
				DataBoundTransferable t = (DataBoundTransferable) event.getTransferable();

				AbstractSelectTargetDetails target = (AbstractSelectTargetDetails) event.getTargetDetails();

				// Get ids of the dragged item and the target item
				Object sourceItemId = t.getData("itemId");
				Object targetItemId = target.getItemIdOver();

				if (targetItemId == null) {
					return;
				}
				// Only area is dragable
				Object sourceParentId = table.getParent(sourceItemId);
				Object sourceChildId = table.getChildren(sourceItemId);
				if (sourceChildId == null || sourceParentId == null) {
					return;
				}
				// Only diff Parent is dragable
				Object targetChildId = table.getChildren(targetItemId);
				Object targetParentId = table.getParent(targetItemId);
				if (targetParentId == null) {
					targetParentId = targetItemId;
				} else if (targetChildId == null) {
					targetItemId = table.getParent(targetItemId);
					targetChildId = table.getChildren(targetItemId);
					targetParentId = table.getParent(targetItemId);
				}
				if (sourceParentId == targetParentId) {
					return;
				}
				
				table.setParent(sourceItemId, targetParentId);
			}
		});	
		return table;
	}
	
	/**
	 * Set Up Column Definitions
	 * @param table
	 */
	private void setUpColumnDefinitions(TreeTable table) {
		for (ColumnDefinition columnDefinition : createColumnDefinitions()) {
			table.removeContainerProperty(columnDefinition.getPropertyId());
		}
		for (ColumnDefinition columnDefinition : createColumnDefinitions()) {
			table.addContainerProperty(
					columnDefinition.getPropertyId(),
					columnDefinition.getPropertyType(),
					null,
					columnDefinition.getPropertyCaption(),
					null,
					columnDefinition.getPropertyAlignment());
			table.setColumnWidth(columnDefinition.getPropertyId(), columnDefinition.getPropertyWidth());
		}
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(GROUPBY, I18N.message("group.by"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("applicant." + NAME_EN, I18N.message("name.en"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("asset." + DESC_EN , I18N.message("asset"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("financialProduct." + DESC_EN, I18N.message("financial.product"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("dealer." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(WKF_STATUS + "." + DESC_EN, I18N.message("contract.status"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contractUser
	 * @param parent
	 * @param index
	 * @return
	 */
	private void renderRow() {
		List<SecUser> staffs = getStaffs();
		int parent = -1;
		int index = 0;
		int subParent = -1;
		staffIds.clear();
		
		if (staffs != null && !staffs.isEmpty()) {
			for (SecUser staff : staffs) {
				String key = staff.getDesc();
				staffIds.add(index);
				parent = renderParentRow(key, staff.getId(), -1, index++);
				
				List<ContractUserSimulInbox> contractUserInboxs = INBOX_SRV.getContractUserSimulInboxByUser(staff.getId());
				
				// Group by area
				Map<Area, List<ContractUserSimulInbox>> contractUserInboxGroup = groupByArea(contractUserInboxs);
				for (Area area : contractUserInboxGroup.keySet()) {
					List<ContractUserSimulInbox> areaContracts = contractUserInboxGroup.get(area);
					String areaDesc = area != null ? area.getDescLocale() : I18N.message("no.area");
					subParent = renderParentRow(areaDesc, area != null ? area.getId() : null, parent, index++);
					
					// Contract
					for (ContractUserSimulInbox contractUserInbox : areaContracts) {
						Item item = tableStaffTree.addItem(index);
						tableStaffTree.setParent(index, subParent);
						tableStaffTree.setCollapsed(subParent, true);
						tableStaffTree.setChildrenAllowed(index, false);
						Contract contract = contractUserInbox.getContract();
						if (contract != null) {
							renderContractRow(contract, item);
						}
						index++;
					}
				}
			}
		}
	}
	
	/**
	 * Group ContractUserSimulInbox By Area
	 * @param contractUserInboxs
	 * @return
	 */
	private Map<Area, List<ContractUserSimulInbox>> groupByArea(List<ContractUserSimulInbox> contractUserInboxs) {
		Map<Area, List<ContractUserSimulInbox>> contractUserInboxGroup = new HashMap<Area, List<ContractUserSimulInbox>>();
		
		if (contractUserInboxs != null) {
			for (ContractUserSimulInbox contractInbox : contractUserInboxs) {
				if (!contractUserInboxGroup.containsKey(contractInbox.getArea())) {
					contractUserInboxGroup.put(contractInbox.getArea(), new ArrayList<ContractUserSimulInbox>());
				}
				contractUserInboxGroup.get(contractInbox.getArea()).add(contractInbox);
			}
		}
		
		return contractUserInboxGroup;
	}
	
	/**
	 * @param key
	 * @param parent
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int renderParentRow(String key, Long id, int parent, int index) {
		Item item = tableStaffTree.addItem(index);
		item.getItemProperty(GROUPBY).setValue(key);
		item.getItemProperty(ID).setValue(id);
		tableStaffTree.setCollapsed(item, true);
		if (parent != -1) {
			tableStaffTree.setParent(index, parent);
			tableStaffTree.setCollapsed(parent, true);
		}
		return index;
	}
	
	/**
	 * Render Contract Row
	 * @param contract
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	private void renderContractRow(Contract contract, Item item) {
		item.getItemProperty(ID).setValue(contract.getId());
		item.getItemProperty(REFERENCE).setValue(contract.getReference());
		item.getItemProperty("applicant." + NAME_EN).setValue(contract.getApplicant().getNameEn());
		
		if (contract.getAsset() != null) {
			item.getItemProperty("asset." + DESC_EN).setValue(contract.getAsset().getDescEn());
		}
		if (contract.getFinancialProduct() != null) {
			item.getItemProperty("financialProduct." + DESC_EN).setValue(contract.getFinancialProduct().getDescEn());
		}
		if (contract.getDealer() != null) {
			item.getItemProperty("dealer." + NAME_EN).setValue(contract.getDealer().getDescEn());
		}
		item.getItemProperty(START_DATE).setValue(contract.getStartDate());
		item.getItemProperty(WKF_STATUS + "." + DESC_EN).setValue(contract.getWkfStatus().getDescEn());
	}
			
	/**
	 * save
	 */
	private void save() {
		Map<Long, List<Long>> staffContracts = new HashMap<>();
		for (Integer parentId : staffIds) {
			Item item = tableStaffTree.getItem(parentId);
			Long staffId = (Long) item.getItemProperty(ID).getValue();
			
			Collection<?> areaColIds = tableStaffTree.getChildren(parentId);
			if (areaColIds != null) {
				Integer[] areaIds = (areaColIds.toArray(new Integer[areaColIds.size()]));
				for (Integer areaId : areaIds) {
					Collection<?> contractColIds = tableStaffTree.getChildren(areaId);
					if (contractColIds != null) {
						Integer[] childIds = (contractColIds.toArray(new Integer[contractColIds.size()]));
						
						for (Integer childId : childIds) {
							Item childItem = tableStaffTree.getItem(childId);
							Long contractId = (Long) childItem.getItemProperty(ID).getValue();
							
							if (!staffContracts.containsKey(staffId)) {
								staffContracts.put(staffId, new ArrayList<Long>());
							}
							staffContracts.get(staffId).add(contractId);
						}
					}
				}
			}
		}
		
		INBOX_SRV.addContractsToInbox(staffContracts, EColType.FIELD);
		refresh();
		displaySuccessMsg();
	}
	
	/**
	 * display success 
	 */
	private void displaySuccessMsg() {
		Label messageLabel = new Label(I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		Label iconLabel = new Label();
		iconLabel.setIcon(new ThemeResource("../nkr-default/icons/16/twitter.png"));
		iconLabel.addStyleName("success-icon");
		messagePanel.removeAllComponents();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.addComponent(iconLabel);
		layout.addComponent(messageLabel);
		messagePanel.addComponent(layout);
		messagePanel.setVisible(true);
	}

	/**
	 * get secuse that profile col phone
	 * @return
	 */
	private List<SecUser> getStaffs() {
		List<SecUser> staffs = new ArrayList<>();
		BaseRestrictions<ContractUserSimulInbox> restrictions = new BaseRestrictions<>(ContractUserSimulInbox.class);
		restrictions.addCriterion(Restrictions.eq("colType", EColType.FIELD));
		List<ContractUserSimulInbox> contractUserSimulInboxs = ENTITY_SRV.list(restrictions);
		
		for (ContractUserSimulInbox contractUserSimulInbox : contractUserSimulInboxs) {
			if (!staffs.contains(contractUserSimulInbox.getSecUser())) {
				staffs.add(contractUserSimulInbox.getSecUser());
			}
		}
		
		return staffs;
	}
	
	/**
	 * refresh
	 */
	public void refresh() {
		tableStaffTree.removeAllItems();
		renderRow();
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			this.save();
		}
	}

}
