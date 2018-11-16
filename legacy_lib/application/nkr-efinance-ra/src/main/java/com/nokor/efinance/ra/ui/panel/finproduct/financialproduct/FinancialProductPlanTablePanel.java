package com.nokor.efinance.ra.ui.panel.finproduct.financialproduct;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.financial.model.FinProduct;
import com.nokor.efinance.core.financial.model.FinProductService;
import com.nokor.efinance.core.shared.financialproduct.FinancialProductEntityField;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FinancialProductPlanTablePanel extends VerticalLayout implements FinancialProductEntityField {
	
	private static final long serialVersionUID = -881834042339718886L;

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	
	private FinProduct financialProduct;
	private List<ColumnDefinition> columnDefinitions;	
	private SimplePagedTable<FinProductService> pagedTable;
	private Item selectedItem = null;
	
	@PostConstruct
	public void PostConstruct() {
		
		setSizeFull();
		setMargin(true);
		setSpacing(true);	
		
		NavigationPanel navigationPanel = new NavigationPanel();
		NativeButton btnAddService = new NativeButton(I18N.message("add.plan"));
		btnAddService.setIcon(new ThemeResource("../nkr-default/icons/16/add.png"));
		btnAddService.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 4131075353203566534L;

			@Override
			public void buttonClick(ClickEvent event) {
				final Window winAddPlan = new Window(I18N.message("add"));
				winAddPlan.setModal(true);
				
				VerticalLayout contentLayout = new VerticalLayout(); 
				contentLayout.setSpacing(true);
										        
		        final TextField txtCode = ComponentFactory.getTextField("code", true, 60, 100);		
		        final TextField txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);		
		        final TextField txtDesc = ComponentFactory.getTextField("desc", false, 60, 200);
		        
		        final TextField txtDefaultInterestRate = ComponentFactory.getTextField("default", false, 60, 100);		        		        
		        final TextField txtMinInterestRate = ComponentFactory.getTextField("min", false, 60, 100); 		        		        
		        final TextField txtMaxInterestRate = ComponentFactory.getTextField("max", false, 60, 100); 		        		        
		        final TextField txtDefaultTerm = ComponentFactory.getTextField("default", false, 60, 100); 		        		        
		        final TextField txtMinTerm = ComponentFactory.getTextField("min", false, 60, 100); 		        		        
		        final TextField txtMaxTerm = ComponentFactory.getTextField("max", false, 60, 100); 
		        		        
		        final CheckBox cbDefault = new CheckBox(I18N.message("default"));
		        
		        final GridLayout gridLayout = new GridLayout(4, 2);
		        gridLayout.setMargin(true);
		        
		        int row = 0;		        
		        gridLayout.addComponent(new Label(I18N.message("interest.rate")), 0, row);
		        gridLayout.addComponent(new FormLayout(txtMinInterestRate), 1, row);
		        gridLayout.addComponent(new FormLayout(txtMaxInterestRate), 2, row);
		        gridLayout.addComponent(new FormLayout(txtDefaultInterestRate), 3, row);
		        
		        row++;
		        gridLayout.addComponent(new Label(I18N.message("term")), 0, row);
		        gridLayout.addComponent(new FormLayout(txtMinTerm), 1, row);
		        gridLayout.addComponent(new FormLayout(txtMaxTerm), 2, row);
		        gridLayout.addComponent(new FormLayout(txtDefaultTerm), 3, row);
		        
		        Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
					private static final long serialVersionUID = -8845816306197506596L;
					public void buttonClick(ClickEvent event) {
						winAddPlan.close();						
		            }
		        });
				btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
				
				Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {
					private static final long serialVersionUID = 3975121141565713259L;
					public void buttonClick(ClickEvent event) {
		            	winAddPlan.close();
		            }
		        });
				btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
				
				NavigationPanel navigationPanel = new NavigationPanel();
				navigationPanel.addButton(btnSave);
				navigationPanel.addButton(btnCancel);
				
				contentLayout.addComponent(navigationPanel);
		        contentLayout.addComponent(gridLayout);
		        
		        winAddPlan.setContent(contentLayout);
		        UI.getCurrent().addWindow(winAddPlan);
			}
		});
		
		NativeButton btnDeleteService = new NativeButton(I18N.message("delete"));
		btnDeleteService.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		btnDeleteService.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 2910252608135870153L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (selectedItem == null) {
					MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
							MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"),
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.setWidth("300px");
					mb.show();
				} else {
					final Long id = (Long) selectedItem.getItemProperty("id").getValue();
					ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single", String.valueOf(id)),
				        new ConfirmDialog.Listener() {

							private static final long serialVersionUID = -5719696946393183890L;

							public void onClose(ConfirmDialog dialog) {
				                if (dialog.isConfirmed()) {
				            		entityService.delete(FinProductService.class, id);
				            		assignValues(financialProduct.getId());
				                }
				            }
				        });
				}
			}
		});
		
		
		navigationPanel.addButton(btnAddService);
		navigationPanel.addButton(btnDeleteService);
		
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<FinProductService>(this.columnDefinitions);
		
		pagedTable.addGeneratedColumn(MANDATORY, new Table.ColumnGenerator() {

			private static final long serialVersionUID = -3386177070767094422L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
                Item item = pagedTable.getItem(itemId);
                Boolean mandatory = (Boolean) item.getItemProperty(MANDATORY).getValue();
                CheckBox cbMandatory = new CheckBox();
                cbMandatory.setValue(mandatory);
                cbMandatory.setReadOnly(true);
                return cbMandatory;
			}
        });
				
		pagedTable.addGeneratedColumn(HIDDEN, new Table.ColumnGenerator() {
			
			private static final long serialVersionUID = -9030501582469785700L;

			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
                Item item = pagedTable.getItem(itemId);
                Boolean hidden = (Boolean) item.getItemProperty(HIDDEN).getValue();
                CheckBox cbMandatory = new CheckBox();
                cbMandatory.setValue(hidden);
                cbMandatory.setReadOnly(true);
                return cbMandatory;
			}
        });

		pagedTable.setColumnWidth(MANDATORY, 100);
		pagedTable.setColumnWidth(HIDDEN, 100);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItem = event.getItem();
			}
		});
		
		addComponent(navigationPanel);
		addComponent(pagedTable);
		addComponent(pagedTable.createControls());
	}
	
	/**
	 * @param fiprdId
	 */
	public void assignValues(Long fiprdId) {
		if (fiprdId != null) {
			pagedTable.setContainerDataSource(getIndexedContainer(fiprdId));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(Long fiprdId) {
		IndexedContainer indexedContainer = new IndexedContainer();
		try {
			financialProduct = entityService.getById(FinProduct.class, new Long(fiprdId));
					
			for (ColumnDefinition column : this.columnDefinitions) {
				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			}
			indexedContainer.addContainerProperty(MANDATORY, Boolean.class, null);
			indexedContainer.addContainerProperty(HIDDEN, Boolean.class, null);
			
			for (FinProductService financialProductService : financialProduct.getFinancialProductServices()) {
				Item item = indexedContainer.addItem(financialProductService.getId());
				item.getItemProperty(ID).setValue(financialProductService.getId());
				item.getItemProperty(SERVICE).setValue(financialProductService.getService().getDescEn());
				item.getItemProperty(MANDATORY).setValue(financialProductService.isMandatory());
				item.getItemProperty(HIDDEN).setValue(financialProductService.isHidden());
			}
						
		} catch (DaoException e) {
			logger.error("DaoException", e);
		}
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(SERVICE, I18N.message("code"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
}
