package com.nokor.efinance.gui.ui.panel.collection.supervisor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserSimulInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.Or;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
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
 * 
 * @author buntha.chea
 *
 */
public class PhoneReDistributionPanel extends VerticalLayout implements ClickListener, FMEntityField, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -3891895329843686084L;
	
	public static final String GROUPBY = "group.by";
	
	private Button btnSave;
	private TreeTable tableStaffTree;
	private VerticalLayout messagePanel;
	
	/** */
	public PhoneReDistributionPanel() {
		
		setCaption(I18N.message("redistribution"));
		setSizeFull();
		setMargin(true);

		messagePanel = new VerticalLayout();
		messagePanel.setSizeUndefined();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		tableStaffTree = createTableStaffTree();
		renderRow(-1, 0);
		
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
		treeLayout.setMargin(true);
		
		treeLayout.addComponent(navigationPanel);
		treeLayout.addComponent(messagePanel);
		treeLayout.addComponent(treepanel);

		addComponent(treeLayout);
	}
	
	
	private TreeTable createTableStaffTree() {
		final TreeTable table = new TreeTable();
		table.setStyleName("v-workplace-tree");
		table.setWidth("100%");
		table.setImmediate(true);
		table.setDragMode(TableDragMode.ROW);
		table.setItemCaptionPropertyId("caption");
		
		table.setPageLength(10);
		table.setSelectable(true);
		table.setSizeFull();
		table.setColumnCollapsingAllowed(true);
		setUpColumnDefinitions(table);
		
		table.setDropHandler(new DropHandler() {
			private static final long serialVersionUID = 7492520500678755519L;

			public AcceptCriterion getAcceptCriterion() {
				// Accept drops in the middle of items that can have
				// children, and below and above all items.
				return new Or(Tree.TargetItemAllowsChildren.get(), new Not(VerticalLocationIs.MIDDLE));
			}

			@SuppressWarnings("unchecked")
			public void drop(DragAndDropEvent event) {
				// Wrapper for the object that is dragged
				DataBoundTransferable t = (DataBoundTransferable) event.getTransferable();

				AbstractSelectTargetDetails target = (AbstractSelectTargetDetails) event.getTargetDetails();

				// Get ids of the dragged item and the target item
				Object sourceItemId = t.getData("itemId");
				Object targetItemId = target.getItemIdOver();

				// Check that the target is not in the subtree of
				// the dragged item itself
				for (Object itemId = targetItemId; itemId != null; itemId = table.getParent(itemId)) {
					if (itemId == sourceItemId) {
						return;
					}
				}

				// On which side of the target was the item dropped
				VerticalDropLocation location = target.getDropLocation();

				HierarchicalContainer container = (HierarchicalContainer) table.getContainerDataSource();

				Long contractId = (Long) container.getItem(sourceItemId).getItemProperty(ID).getValue();
				Contract contrat = ENTITY_SRV.getById(Contract.class, contractId);
				table.setChildrenAllowed(sourceItemId, false);

				Item item = null;
				Object parentId = container.getParent(targetItemId);
				if (parentId == null) {
					parentId = targetItemId;
				}

				// Drop at the top of a subtree -> make it previous
				if (location == VerticalDropLocation.TOP) {

					table.getContainerDataSource().removeItem(sourceItemId);
					item = container.addItemAt(((Number) targetItemId).intValue(), sourceItemId);
					table.setParent(sourceItemId, parentId);
				} else if (location == VerticalDropLocation.BOTTOM) {
					// Drop below another item -> make it next
					table.getContainerDataSource().removeItem(sourceItemId);
					item = container.addItemAfter(targetItemId, sourceItemId);
					table.setParent(sourceItemId, parentId);
				}
				if (item != null) {
					item.getItemProperty(ID).setValue(contrat.getId());
					item.getItemProperty(REFERENCE).setValue(contrat.getReference());
					item.getItemProperty("applicant." + NAME_EN).setValue(contrat.getApplicant().getNameEn());
					item.getItemProperty("asset." + DESC_EN).setValue(contrat.getAsset().getDescEn());
					item.getItemProperty("financialProduct." + DESC_EN).setValue(contrat.getFinancialProduct().getDescEn());
					item.getItemProperty("dealer." + NAME_EN).setValue(contrat.getDealer().getDescEn());
					item.getItemProperty(START_DATE).setValue(contrat.getStartDate());
					item.getItemProperty(WKF_STATUS + "." + DESC_EN).setValue(contrat.getWkfStatus().getDescEn());
				}
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
			if (GROUPBY.equals(columnDefinition.getPropertyId())) {
				table.addContainerProperty(
						columnDefinition.getPropertyId(),
						columnDefinition.getPropertyType(),
						null,
						columnDefinition.getPropertyCaption(),
						null,
						columnDefinition.getPropertyAlignment());
				table.setColumnWidth(columnDefinition.getPropertyId(), columnDefinition.getPropertyWidth());
			} else {
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
	@SuppressWarnings({ "unchecked"})
	private int renderRow(int parent, int index) {
		List<SecUser> staffs = COL_SRV.getCollectionUsers(new String[] {IProfileCode.COL_PHO_STAFF});
		
		if (staffs != null && !staffs.isEmpty()) {
			int subParent = parent;
			for (SecUser staff : staffs) {
				String key = staff.getDesc();
				subParent = renderParentRow(key, staff.getId(), parent, index);
				index++;
				
				List<ContractUserSimulInbox> contractUserInboxs = INBOX_SRV.getContractUserSimulInboxByUser(staff.getId());
				for (ContractUserSimulInbox contractUserInbox : contractUserInboxs) {
					Item item = tableStaffTree.addItem(index);
					if (subParent != -1) {
						tableStaffTree.setParent(index, subParent);
						tableStaffTree.setCollapsed(subParent, true);
					}
					tableStaffTree.setChildrenAllowed(index, false);
					Contract contract = contractUserInbox.getContract();
					if (contract != null) {
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
					
					index++;
				}
			}
		}
		return index;
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
	 * save
	 */
	private void save() {
		java.util.Collection<?> collection = tableStaffTree.getItemIds();
		Integer[] itemIds = (Integer[])(collection.toArray(new Integer[collection.size()]));
		for (int i = 0; i < itemIds.length; i++) {
			if (tableStaffTree.getChildren(i) != null) {
				Item item = tableStaffTree.getItem(i);
				Long staffId = (Long) item.getItemProperty(ID).getValue();
				
				java.util.Collection<?> c = tableStaffTree.getChildren(i);
				Integer[] childIds = (Integer[])(c.toArray(new Integer[c.size()]));
				
				List<Long> contractIds = new ArrayList<>();
				for (Integer childId : childIds) {
					Item childItem = tableStaffTree.getItem(childId);
					Long contractId = (Long) childItem.getItemProperty(ID).getValue();
					contractIds.add(contractId);				
				}
				
				INBOX_SRV.addContractsToInbox(staffId, contractIds, EColType.PHONE);
				displaySuccessMsg();
				
			}
		}
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
	 * refresh
	 */
	public void refresh() {
		tableStaffTree.removeAllItems();
		renderRow(-1, 0);
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			this.save();
		}
	}

}
