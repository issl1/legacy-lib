package com.nokor.efinance.ra.ui.panel.leasingmodule;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.applicant.AddressUtils;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ButtonFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * Registration Expense for POS
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(RegistrationExpensesPanel.NAME)
public class RegistrationExpensesPanel extends AbstractTabPanel implements View, FMEntityField {
	
	/** */
	private static final long serialVersionUID = 1L;
	public static final String NAME = "registration.expenses";
	
	private static String REGISTRATION_EXPENSES = "registrationExpenses";
	
	private Button btnSave;
	private SimpleTable<Dealer> tableExpenses;
	private List<Dealer> listDealers;
	
	@Autowired
	private EntityService entityService;
	
	/**
	 * Post construct
	 */
	@PostConstruct
	public void PostConstruct() {
		assignValue();
	}
	
	/**
	 * Assign value to the form
	 */
	public void assignValue() {
		this.listDealers = getDealers();
		setContainerDataSource(this.listDealers);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		btnSave = ButtonFactory.getSaveButton();
		btnSave.addClickListener(getClickListener());
		tableExpenses = new SimpleTable<Dealer>(getColumnDefinition());
		tableExpenses.setPageLength(15);
		
		TabSheet tabSheet = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);

		ToolbarButtonsPanel tblButtonsPanel = new ToolbarButtonsPanel();
		tblButtonsPanel.addButton(btnSave);

		btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1717161084451001316L;
			@Override
			public void buttonClick(ClickEvent event) {
				//to do save
			}
		});

		contentLayout.addComponent(tblButtonsPanel, 0);
		contentLayout.addComponent(tableExpenses);

		tabSheet.addTab(contentLayout, I18N.message("registration.expenses"));
		return tabSheet;
	}
	
	/**
	 * Get columnDefinition
	 * @return List of columnDefinition
	 */
	private List<ColumnDefinition> getColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		
		columnDefinitions.add(new ColumnDefinition(PROVINCE, I18N.message("province"), String.class, Align.LEFT, 250));
		columnDefinitions.add(new ColumnDefinition(NAME_EN, I18N.message("pos"), String.class, Align.LEFT, 500));
		columnDefinitions.add(new ColumnDefinition(REGISTRATION_EXPENSES, I18N.message("registration.expenses"), TextField.class, Align.LEFT, 250));
		
		return columnDefinitions;
	}
	
	/**
	 * Set a container for the table
	 */
	@SuppressWarnings("unchecked")
	public void setContainerDataSource(List<Dealer> dealers) {
		Container indexedContainer = tableExpenses.getContainerDataSource();
		indexedContainer.removeAllItems();
		
		for (Dealer dealer : dealers) {
			Item item = indexedContainer.addItem(dealer);
			NumberField txtRegExpense = ComponentFactory.getNumberField();
			String address = "";
			
			address = AddressUtils.getDealerAddress(dealer);
			txtRegExpense.setValue("0");
			
			item.getItemProperty(PROVINCE).setValue(address);
			item.getItemProperty(NAME_EN).setValue(dealer.getNameEn());
			item.getItemProperty(REGISTRATION_EXPENSES).setValue(txtRegExpense);
		}
	}
	
	/**
	 * Get Dealers
	 * @return List of Dealers
	 */
	private List<Dealer> getDealers() {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.addOrder(Order.asc(NAME_EN));
		List<Dealer> dealers = entityService.list(restrictions);
		
		return dealers;
	}
	
	/**
	 * Get the button clicklistener
	 * @return Button.ClickListener
	 */
	private ClickListener getClickListener() {
		return new ClickListener() {
			/** */
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				for (Dealer dealer : listDealers) {
					Item item = tableExpenses.getItem(dealer);
					entityService.saveOrUpdate(dealer);
				}
				MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("save.successfully"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			}
		};
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
