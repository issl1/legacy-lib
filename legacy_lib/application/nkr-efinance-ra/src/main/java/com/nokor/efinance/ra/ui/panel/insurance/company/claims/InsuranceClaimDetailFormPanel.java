package com.nokor.efinance.ra.ui.panel.insurance.company.claims;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.financial.model.InsuranceClaims;
import com.nokor.efinance.core.financial.service.InsuranceClaimsRestriction;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author uhout.cheng
 */
public class InsuranceClaimDetailFormPanel extends AbstractControlPanel implements AddClickListener, EditClickListener, SearchClickListener, DeleteClickListener {
	
	/** */
	private static final long serialVersionUID = -5923982995773828017L;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimpleTable<Entity> simpleTable;
	private Long selectedItemId = null;
	private Organization insurance;
	private InsuranceClaimDetailPopupPanel window;
	
	/**
	 * 
	 */
	public InsuranceClaimDetailFormPanel() {
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addEditClickListener(this);
		navigationPanel.addDeleteClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		addComponent(navigationPanel, 0);
		addComponent(createForm());
	}

	/**
	 * Create Form
	 * @return
	 */
	protected Component createForm() {
		window = new InsuranceClaimDetailPopupPanel(this);
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setStyleName("has-no-padding");
		contentLayout.setSpacing(true);
		
		this.columnDefinitions = createColumnDefinitions();
		simpleTable = new SimpleTable<Entity>(this.columnDefinitions);
		simpleTable.addItemClickListener(new ItemClickListener() {
			
			/** */
			private static final long serialVersionUID = 8586628489979020963L;
			
			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				selectedItemId = (Long) event.getItemId();
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					editButtonClick(null);
				}
			}
		});
		
		contentLayout.addComponent(simpleTable);
		
		return contentLayout;
	}
	
	/**
	 * Create Column Definitions
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(InsuranceClaims.ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(InsuranceClaims.RANGEOFYEAR, I18N.message("range.of.year"), Integer.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(InsuranceClaims.FROM, I18N.message("from"), Integer.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(InsuranceClaims.TO, I18N.message("to"), Integer.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition(InsuranceClaims.PREMIUMNREFUNDEDPERCENTAGE, I18N.message("permiumn.refunded.percentage"), Double.class, Align.LEFT, 190));
		return columnDefinitions;
	}
	
	/**
	 * Assign Values
	 * @param insuranceCompanyId
	 */
	public void assignValues(Long insuranceCompanyId) {
		reset();
		if (insuranceCompanyId != null) {
			insurance = ENTITY_SRV.getById(Organization.class, insuranceCompanyId);
			simpleTable.setContainerDataSource(getIndexedContainer(insuranceCompanyId));
		} else {
			simpleTable.removeAllItems();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		selectedItemId = null;
		insurance = null;
	}
	
	/**
	 * Get Indexed Container
	 * @param isrClaimId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(Long isrClaimId) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		List<InsuranceClaims> insuranceClaims = getInsuranceClaims(isrClaimId);
		if (insuranceClaims != null) {
			for (InsuranceClaims isrClaim : insuranceClaims) {
				Item item = indexedContainer.addItem(isrClaim.getId());
				item.getItemProperty(InsuranceClaims.ID).setValue(isrClaim.getId());
				item.getItemProperty(InsuranceClaims.RANGEOFYEAR).setValue(MyNumberUtils.getInteger(isrClaim.getRangeOfYear()));
				item.getItemProperty(InsuranceClaims.FROM).setValue(MyNumberUtils.getInteger(isrClaim.getFrom()));
				item.getItemProperty(InsuranceClaims.TO).setValue(MyNumberUtils.getInteger(isrClaim.getTo()));
				item.getItemProperty(InsuranceClaims.PREMIUMNREFUNDEDPERCENTAGE).setValue(MyNumberUtils.getDouble(isrClaim.getPremiumnRefundedPercentage()));
			}
		}
		return indexedContainer;
	}
	
	/**
	 * 
	 * @param insuranceId
	 * @return
	 */
	private List<InsuranceClaims> getInsuranceClaims(Long insuranceId) {
		InsuranceClaimsRestriction restrictions = new InsuranceClaimsRestriction();
		restrictions.setInsuranceId(insuranceId);
		return ENTITY_SRV.list(restrictions);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener#deleteButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void deleteButtonClick(ClickEvent event) {
		if (selectedItemId == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete", String.valueOf(selectedItemId)),
		        new ConfirmDialog.Listener() {
					
				/** */
				private static final long serialVersionUID = 7570541701306102646L;

				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						try {
							ENTITY_SRV.delete(InsuranceClaims.class, selectedItemId);
							searchButtonClick(null);
		                } catch (DataIntegrityViolationException e) {
		                	MessageBox mb = new MessageBox(
		                			UI.getCurrent(),
		                			"400px",
		                			"160px",
		                			I18N.message("information"),
									MessageBox.Icon.ERROR,
									I18N.message("msg.warning.delete.selected.item.is.used"),
									Alignment.MIDDLE_RIGHT,
									new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
							mb.show();
		               	}
		           }
				}
			});
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener#searchButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void searchButtonClick(ClickEvent event) {
		assignValues(insurance.getId());
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener#editButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void editButtonClick(ClickEvent event) {
		if (selectedItemId == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			window.assignValues(selectedItemId);
			UI.getCurrent().addWindow(window);
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		window.reset();
		window.setInsurance(insurance);
		UI.getCurrent().addWindow(window);
	}

}
