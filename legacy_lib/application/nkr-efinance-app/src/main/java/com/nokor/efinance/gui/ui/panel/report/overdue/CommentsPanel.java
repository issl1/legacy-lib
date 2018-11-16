package com.nokor.efinance.gui.ui.panel.report.overdue;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Comments panel
 * @author sok.vina
 */
public class CommentsPanel extends AbstractTabPanel implements AddClickListener, FMEntityField {
	
	private static final long serialVersionUID = 2202264472024719484L;
	
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Comment> pagedTable;
	private List<ContractNote> contractNotes;
	private ContractNote contractNote;
	private NavigationPanel navigationPanel;
	private TextArea txtComment;
	private Window winComment;
	private Contract contract;
	
	public CommentsPanel() {
		super();
		setSizeFull();				
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		addComponent(navigationPanel, 0);
	}
	
	@Override
	protected Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		
		navigationPanel = new NavigationPanel();
        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
			private static final long serialVersionUID = -4024064977917270885L;
			public void buttonClick(ClickEvent event) {
				if (contractNote == null) {
					contractNote = new ContractNote();
				}
				contractNote.setContract(contract);
				contractNote.setComment(getDefaultString(txtComment.getValue()));
				SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				contractNote.setUserLogin(secUser.getLogin());
				entityService.saveOrUpdate(contractNote);
				assignValues(contract);
				winComment.close();
			}
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {
			private static final long serialVersionUID = 3975121141565713259L;
			public void buttonClick(ClickEvent event) {
				winComment.close();
            }
        });
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);		
		
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Comment>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					Long itemId = (Long) event.getItemId();
					contractNote = entityService.getById(ContractNote.class, itemId.intValue());
					getCommentWindow(contractNote);
				}
			}
		});
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
        return contentLayout;
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<ContractNote> contractNotes) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		indexedContainer.addContainerProperty(DESC_EN, String.class, null);
		indexedContainer.addContainerProperty("updateDate", String.class, null);
		
		for (ContractNote contractNote : contractNotes) {
			Item item = indexedContainer.addItem(contractNote.getId());
			item.getItemProperty(ID).setValue(contractNote.getId());
			item.getItemProperty("comment").setValue(contractNote.getComment());
			item.getItemProperty("user.created").setValue(contractNote.getUserLogin());
			item.getItemProperty("updateDate").setValue(DateUtils.formatDate(contractNote.getUpdateDate(), DateUtils.FORMAT_YYYYMMDD_HHMMSS_SLASH));
		}
		return indexedContainer;
	}
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition("comment", I18N.message("comment"), String.class, Align.LEFT, 600));
		columnDefinitions.add(new ColumnDefinition("user.created", I18N.message("user"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date"), String.class, Align.LEFT, 140));
		return columnDefinitions;
	}
	/**
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		BaseRestrictions<ContractNote> restrictions = new BaseRestrictions<ContractNote>(ContractNote.class);
		restrictions.addCriterion(Restrictions.eq("contract.id", contract.getId()));
		this.contractNotes = entityService.list(restrictions);
		if (contractNotes != null && !contractNotes.isEmpty()) {
			pagedTable.setContainerDataSource(getIndexedContainer(contractNotes));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
	}

	/**
	 * @return
	 */
	public boolean isValid() {
		super.removeErrorsPanel();
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	@Override
	public void addButtonClick(ClickEvent event) {
		getCommentWindow(null);
	}
	
	private void getCommentWindow(ContractNote contractNote){
		this.contractNote = contractNote;
		winComment = new Window();
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setWidth("100%");
		contentLayout.addComponent(navigationPanel);
		txtComment = new TextArea(I18N.message("comment"));
		txtComment.setSizeFull();
		txtComment.setWidth("100%");
		txtComment.setRows(5);
		txtComment.setMaxLength(1000);
		VerticalLayout verticalLayoutComment = new VerticalLayout();
		verticalLayoutComment.setSizeFull();
		verticalLayoutComment.setMargin(true);
		verticalLayoutComment.setSpacing(true);
		verticalLayoutComment.addComponent(txtComment);
		contentLayout.addComponent(verticalLayoutComment);
		if (contractNote != null) {
			txtComment.setValue(contractNote.getComment());
		}
		winComment.setModal(true);
	    winComment.setWidth(430, Unit.PIXELS);
	    winComment.setHeight(250, Unit.PIXELS);
        winComment.setCaption(I18N.message("add.comment"));
        winComment.setContent(contentLayout);
        UI.getCurrent().addWindow(winComment);
	}
}
