package com.nokor.efinance.gui.ui.panel.collection.jobdistribution;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

/**
 * Job distribution table layout in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JobDistributionTablePanel extends AbstractTablePanel<Quotation> implements ClickListener, ValueChangeListener {
	
	/** */
	private static final long serialVersionUID = 8339819065916215144L;
	
	public static final String NAME = "job.distribution";

	private static String ZERO = I18N.message("zero");
	private static String ONE = I18N.message("one");
	private static String TWO = I18N.message("two");
	private static String THREE = I18N.message("three");
	private static String FOUR_FIVE = I18N.message("four.five");
	private static String SIX = I18N.message("six");
	private static String SEVEN = I18N.message("seven");
	private static String TRANSFERED = I18N.message("transfered");
	private static String EXPIRED = I18N.message("expired");
	private static String EXTENDED = I18N.message("extended");
	private static String LOCK_SPLIT = I18N.message("lock.split");
	
	private OptionGroup optionGroup;
	
	private Button btnOpen;
	private Button btnModify;
	private Button btnApply;
	private VerticalLayout mainLayout;
	private Panel responsibilitorPanel;
	private boolean selectAll = false;
	private CheckBox cbLockSplit;
		
	/**
	 * 
	 * @param caption
	 * @param themeResource
	 * @return
	 */
	private Button getButton(String caption) {
		Button button = ComponentFactory.getButton(caption);
		button.setStyleName(Runo.BUTTON_SMALL);
		return button;
	}
	
	/** */
	private HorizontalLayout getHorizontalLayout() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		return horizontalLayout;
	}
	
	@PostConstruct
	public void PostConstruct() {
		super.init(I18N.message("job.distribution"));
		setCaption(I18N.message("job.distribution"));
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		
		List<String> options = null;
		optionGroup = new OptionGroup(null);
        if (ProfileUtil.isColPhoneLeader()) {
        	options = Arrays.asList(new String[] {ZERO, ONE, TWO, THREE});
        	cbLockSplit = new CheckBox(LOCK_SPLIT);
        	cbLockSplit.addValueChangeListener(this);
        	optionGroup.setMultiSelect(true);
        	optionGroup.addValueChangeListener(this);
        } else if (ProfileUtil.isColBillLeader()) {
        	options = Arrays.asList(new String[] {FOUR_FIVE ,LOCK_SPLIT, TRANSFERED});
        	optionGroup.setMultiSelect(false);
        } else if (ProfileUtil.isColInsideRepoLeader()) {
        	options = Arrays.asList(new String[] {SIX, LOCK_SPLIT, TRANSFERED, EXTENDED});
        	optionGroup.setMultiSelect(false);
        } else if (ProfileUtil.isColOutsourceRepoLeader()) {
        	options = Arrays.asList(new String[] {SEVEN, TRANSFERED, EXPIRED, EXTENDED});
        	optionGroup.setMultiSelect(false);
        } else {
        	options = Arrays.asList(new String[] {ZERO, ONE, TWO, THREE});
        	cbLockSplit = new CheckBox(LOCK_SPLIT);
        	cbLockSplit.addValueChangeListener(this);
        	optionGroup.setMultiSelect(true);
        	optionGroup.addValueChangeListener(this);
        }
		optionGroup.addItems(options);
		optionGroup.addStyleName("horizontal");
		optionGroup.setImmediate(true);
		optionGroup.setNullSelectionAllowed(false);
		btnOpen = getButton("open");
		btnOpen.setStyleName(Reindeer.BUTTON_SMALL);
		btnOpen.addClickListener(this);
		btnModify = ComponentFactory.getButton("modify");
		btnApply = ComponentFactory.getButton("apply");
		btnModify.addClickListener(this);
		btnApply.addClickListener(this);
		
		HorizontalLayout optionGroupLayout = getHorizontalLayout();
		optionGroupLayout.addComponent(new Label(I18N.message("debt.level")));
		optionGroupLayout.addComponent(optionGroup);
		if (ProfileUtil.isColPhoneLeader()) {
			optionGroupLayout.addComponent(cbLockSplit);
		}
		
		HorizontalLayout buttonOpenLayout = getHorizontalLayout();
		buttonOpenLayout.addComponent(new Label(I18N.message("new.contracts.assign")));
		buttonOpenLayout.addComponent(new Label(I18N.message("500")));
		buttonOpenLayout.addComponent(btnOpen);
		
		HorizontalLayout buttonModifyLayout = getHorizontalLayout();
		buttonModifyLayout.addComponent(btnModify);
		buttonModifyLayout.addComponent(btnApply);
		
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.addComponent(optionGroupLayout);
		mainLayout.addComponent(buttonOpenLayout);
		mainLayout.addComponent(getTreeTable());
		mainLayout.addComponent(buttonModifyLayout);
		addComponent(mainLayout, 0);
		setComponentVisible(false);
    	addComponent(getNewResponsibilitorPanel());
    	
    	pagedTable.setColumnIcon("selectAll", new ThemeResource("../nkr-default/icons/16/tick.png"));
    	pagedTable.addHeaderClickListener(new HeaderClickListener() {
			
			/** */
			private static final long serialVersionUID = 3295715508611706855L;

			/**
			 * @see com.vaadin.ui.Table.HeaderClickListener#headerClick(com.vaadin.ui.Table.HeaderClickEvent)
			 */
			@SuppressWarnings("unchecked")
			@Override
			public void headerClick(HeaderClickEvent event) {
				if (event.getPropertyId() == "selectAll") {
					selectAll = !selectAll;
					Collection<Long> ids = (Collection<Long>) pagedTable.getItemIds();
					for (Long id : ids) {
						Item item = pagedTable.getItem(id);
						CheckBox cbSelect = (CheckBox) item.getItemProperty("selectAll").getValue();
						cbSelect.setImmediate(true);
						cbSelect.setValue(selectAll);
					}
				}
			}
		});
	}	
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (ProfileUtil.isColPhoneLeader()) {
			if (event.getProperty().equals(cbLockSplit)) {
				if (cbLockSplit.getValue()) {	
					optionGroup.clear();
					cbLockSplit.setValue(true);
				}  
			} else if (event.getProperty().equals(optionGroup)) {
				cbLockSplit.setValue(false);
			}
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnOpen)) {
		
		} else if (event.getButton().equals(btnModify)) {
			setComponentVisible(true);
			responsibilitorPanel.setVisible(true);
		} else if (event.getButton().equals(btnApply)) {
			ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm"), new ConfirmDialog.Listener() {
							
				/** */
				private static final long serialVersionUID = -494043234946003203L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							dialog.close();
			            }
					}
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");
		}
	}
	
	/**
	 * For hide or show component (Search,Table,and pagetable.control)
	 * @param visible
	 */
	private void setComponentVisible(boolean visible) {
		super.pagedTable.setVisible(visible);
    	searchPanel.setVisible(visible);
    	getComponent(3).setVisible(visible);
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Quotation> createPagedDataProvider() {
		PagedDefinition<Quotation> pagedDefinition = new PagedDefinition<Quotation>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new CollectionRowRenderer());
		pagedDefinition.addColumnDefinition("selectAll", "", CheckBox.class, Align.CENTER, 30);
		pagedDefinition.addColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 60, false);
		pagedDefinition.addColumnDefinition("branch", I18N.message("branch"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("contract.no", I18N.message("contract.no"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("firstname", I18N.message("firstname.en"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("lastname", I18N.message("lastname.en"), String.class, Align.LEFT, 145);
		pagedDefinition.addColumnDefinition("due.date", I18N.message("due.date"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("installment.vat", I18N.message("installment.vat"), Amount.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("installment.overdue", I18N.message("installment.overdue"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("penalty", I18N.message("penalty"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("overdue.amount.a", I18N.message("overdue.amount.a"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("current.penalty.b", I18N.message("current.penalty.b"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("pressing.fee.c", I18N.message("pressing.fee.c"), String.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition("following.fee.d", I18N.message("following.fee.d"), String.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition("total.amountabcd", I18N.message("total.amountabcd"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("end.month.penalty", I18N.message("end.month.penalty"), Date.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("outstanding.balance", I18N.message("outstanding.balance"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("number.of.overdue", I18N.message("number.of.overdue"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("full.block", I18N.message("full.block"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("latest.payment.date", I18N.message("latest.payment.date"), Date.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("latest.lock.split.deadline", I18N.message("latest.lock.split.deadline"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("first.due.1.to.3", I18N.message("first.due.1.to.3"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("last.result.date", I18N.message("last.result.date"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("last.result", I18N.message("last.result"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("latest.staff", I18N.message("latest.staff"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("current.staff", I18N.message("current.staff"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("debt.level", I18N.message("debt.level"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("status", I18N.message("status"), String.class, Align.LEFT, 110);
		EntityPagedDataProvider<Quotation> pagedDataProvider = new EntityPagedDataProvider<Quotation>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}	
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	private class CollectionRowRenderer implements RowRenderer {

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.RowRenderer#renderer(com.vaadin.data.Item, org.seuksa.frmk.model.entity.Entity)
		 * Set Value into pageDefinitiohn
		 */
		@SuppressWarnings({ "unchecked", "unused" })
		@Override
		public void renderer(Item item, Entity entity) {
			Quotation quotation = (Quotation) entity;
			Contract contract = quotation.getContract();
			Applicant applicant = quotation.getApplicant();
			Asset asset = quotation.getAsset();
			AssetModel assetModel = null;
			AssetRange assetRange = null;
			
			item.getItemProperty("selectAll").setValue(getRenderSelected(quotation.getId()));
			item.getItemProperty("id").setValue(quotation.getId());
			if (asset != null) {
				if (assetModel != null) {
					assetRange = assetModel.getAssetRange();
					item.getItemProperty("branch").setValue(assetRange.getAssetMake() == null ? 
							null : assetRange.getAssetMake().getDescEn());
				}
			}
			if (contract != null) {
				item.getItemProperty("contract.no").setValue(contract.getReference());
				item.getItemProperty("due.date").setValue(contract.getFirstDueDate());
			}
			if (applicant != null) {
				item.getItemProperty("firstname").setValue(applicant.getIndividual().getFirstNameEn());
				item.getItemProperty("lastname").setValue(applicant.getIndividual().getLastNameEn());
			}
			item.getItemProperty("installment.vat").setValue(AmountUtils.convertToAmount(MyNumberUtils.getDouble(quotation.getVatInstallmentAmount())));
			item.getItemProperty("installment.overdue").setValue("");
			item.getItemProperty("penalty").setValue("");
			item.getItemProperty("overdue.amount.a").setValue("");
			item.getItemProperty("current.penalty.b").setValue("");
			item.getItemProperty("pressing.fee.c").setValue("");
			item.getItemProperty("following.fee.d").setValue("");
			item.getItemProperty("total.amountabcd").setValue("");
			item.getItemProperty("end.month.penalty").setValue(DateUtils.today());
			item.getItemProperty("outstanding.balance").setValue("");
			item.getItemProperty("number.of.overdue").setValue("");
			item.getItemProperty("full.block").setValue("");
			item.getItemProperty("latest.payment.date").setValue(quotation.getLastDueDate());
			item.getItemProperty("latest.lock.split.deadline").setValue("");
			item.getItemProperty("first.due.1.to.3").setValue("");
			item.getItemProperty("last.result.date").setValue("");
			item.getItemProperty("last.result").setValue("");
			item.getItemProperty("latest.staff").setValue("");
			item.getItemProperty("current.staff").setValue("");
			item.getItemProperty("debt.level").setValue("");
			item.getItemProperty("status").setValue("");
		}
	}
	
	/**
	 * Tree table properties
	 * @param treeTable
	 * @param propertyId
	 * @param type
	 * @param columnHeader
	 * @param width
	 * @param alignment
	 */
	private void setTreeTableProperties(TreeTable treeTable, Object propertyId, Class<?> type, String columnHeader, 
			int width, Align alignment) {
		treeTable.addContainerProperty(propertyId, type, null, I18N.message(columnHeader), null, alignment);
		treeTable.setColumnWidth(propertyId, width);
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TreeTable getTreeTable() {
		TreeTable treeTable = new TreeTable();
		setTreeTableProperties(treeTable, "group.staff", String.class, "group.staff", 170, Align.LEFT);
		setTreeTableProperties(treeTable, "previous", Integer.class, "previous", 70, Align.LEFT);
		setTreeTableProperties(treeTable, "current", Integer.class, "current", 70, Align.LEFT);
		setTreeTableProperties(treeTable, "default", Integer.class, "default", 70, Align.LEFT);
		setTreeTableProperties(treeTable, "accrued", Integer.class, "accrued", 70, Align.LEFT);
	
		Stack<Integer> parents = new Stack<Integer>();
		for (int i = 0; i < 6; i++) {
			Item item = treeTable.addItem(i); 
			if (i == 0) {
				item.getItemProperty("group.staff").setValue("Group");
			} else if (i == 1) {
				item.getItemProperty("group.staff").setValue("Phone A");
			} else if (i == 2) {
				item.getItemProperty("group.staff").setValue("Staff");
			} else {
				item.getItemProperty("group.staff").setValue("Staff " + i);
			}
		    item.getItemProperty("previous").setValue(i);
		    item.getItemProperty("current").setValue(i);
		    item.getItemProperty("default").setValue(i);
		    item.getItemProperty("accrued").setValue(i);
		    
		    if (parents.size() > 0) {
		    	treeTable.setParent(i, parents.lastElement());
			    treeTable.setCollapsed(i, false);
		    }
		    if (parents.size() < 3) {
		        parents.push(i);
		    } else {
		        treeTable.setChildrenAllowed(i, false);
		        if (parents.size() > 0) {
		        	//parents.pop();
		        }
		    }
		}
		
		return treeTable;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	private CheckBox getRenderSelected(Long id) {
		final CheckBox checkBox = new CheckBox();
		checkBox.setImmediate(true);
		checkBox.setData(id);
		return checkBox;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */	
	@Override
	protected Quotation getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Quotation.class, id);
		}
		return null;
	}
	
	/**	
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected JobDistributionSearchPanel createSearchPanel() {
		return new JobDistributionSearchPanel(this);		
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getNewResponsibilitorPanel() {
		final List<String> staffs = Arrays.asList(new String[] {
	            "Staff1", "Staff2", "Staff3", "Staff4", "Staff5", "Staff6", "Staff7" });
		responsibilitorPanel = new Panel(I18N.message("new.responsibilitor"));
		responsibilitorPanel.setVisible(false);
		EntityRefComboBox<EColGroup> cbxGroup = new EntityRefComboBox<>();
		cbxGroup.setRestrictions(new BaseRestrictions<>(EColGroup.class));
		cbxGroup.setWidth(120, Unit.PIXELS);
		cbxGroup.renderer();
		ListSelect lstedStaff = new ListSelect();
		lstedStaff.addItems(staffs);
		lstedStaff.setWidth(100, Unit.PIXELS);
		lstedStaff.setRows(7);
		lstedStaff.setNullSelectionAllowed(false);
		lstedStaff.setImmediate(true);
		
		Button btnSaveAsDraft = ComponentFactory.getButton("save.as.draft");
		Button btnCancel = ComponentFactory.getButton("cancel");
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(btnSaveAsDraft);
		buttonLayout.addComponent(btnCancel);
		
		GridLayout gridLayout = new GridLayout(3, 3);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		int iCol = 0;
		gridLayout.addComponent(new Label(I18N.message("select.group")), iCol++, 0);
		gridLayout.addComponent(cbxGroup, iCol++, 0);
		gridLayout.addComponent(lstedStaff, 1, 1);
		gridLayout.addComponent(buttonLayout, 1, 2);
		responsibilitorPanel.setContent(gridLayout);
		return responsibilitorPanel;
	}
}
