package com.nokor.efinance.gui.ui.panel.collection.extend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.gui.ui.panel.collection.jobdistribution.JobDistributionsPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Extend tab panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ExtendPanel extends AbstractTabPanel {
	
	/** */
	private static final long serialVersionUID = 5452146981979406782L;
	
	private SimpleTable<Entity> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private Button btnExtend;

	private Label lblYellow;
	private Label lblRed;
	private Label lblBlue;
	
	private JobDistributionsPanel jobDistributionPanel;
	
	/**
	 * @return the jobDistributionPanel
	 */
	public JobDistributionsPanel getJobDistributionPanel() {
		return jobDistributionPanel;
	}

	/**
	 * @param jobDistributionPanel the jobDistributionPanel to set
	 */
	public void setJobDistributionPanel(JobDistributionsPanel jobDistributionPanel) {
		this.jobDistributionPanel = jobDistributionPanel;
	}
	
	/** */
	public ExtendPanel() {
		super();
		setCaption(I18N.message("extend"));
		setSizeFull();
	}
	
	/**
	 * 
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		lblYellow = new Label();
		lblYellow.addStyleName("v-label-results-yellow");
		
		lblRed = new Label();
		lblRed.addStyleName("v-label-results-red");
		
		lblBlue = new Label();
		lblBlue.addStyleName("v-label-results-bright-blue");
		
		btnExtend = new Button(I18N.message("extend"));
		btnExtend.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = -4429132505152823255L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm"), 
						new ConfirmDialog.Listener() {
					
						/** */
						private static final long serialVersionUID = 6067603055744524656L;

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								dialog.close();
				            }
						}
					});
					confirmDialog.setWidth("400px");
					confirmDialog.setHeight("150px");
			}
		});
		
		columnDefinitions = createColumnDefinitions();
	    pagedTable = new SimpleTable<Entity>(columnDefinitions);
	    pagedTable.setPageLength(10);
	    
	    pagedTable.addItemClickListener(new ItemClickListener() {
			
			/** */
			private static final long serialVersionUID = -5050170974788585183L;

			/**
			 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
			 */
			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					/*ContractFormPanel formPanel = jobDistributionPanel.getQuotationFormPanel();
					jobDistributionPanel.getTabSheet().addFormPanel(formPanel);
					jobDistributionPanel.getTabSheet().setSelectedTab(formPanel);*/
				}
				
			}
		});
	    
	    setIndexedContainer();
	    
	    VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
	    contentLayout.addComponent(pagedTable);
	    contentLayout.addComponent(btnExtend);
	    contentLayout.setComponentAlignment(btnExtend, Alignment.BOTTOM_RIGHT);
        return contentLayout;
	}
	
	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("check", I18N.message(""), CheckBox.class, Align.LEFT, 30));
		columnDefinitions.add(new ColumnDefinition("color", I18N.message(""), Label.class, Align.LEFT, 25));
		columnDefinitions.add(new ColumnDefinition("branch", I18N.message("branch"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition("contract.no", I18N.message("contract.no"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("firstname", I18N.message("firstname.en"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("lastname", I18N.message("lastname.en"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("due.date", I18N.message("due.date"), Date.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("installment.vat", I18N.message("ti.installment"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("amount.of.overdues", I18N.message("amount.of.overdue"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("penalty", I18N.message("penalty"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("overdue.amount.a", I18N.message("ti.installment.overdue.a"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("current.penalty.b", I18N.message("current.penalty"), String.class, Align.LEFT, 180));
		
		columnDefinitions.add(new ColumnDefinition("pressing.fee.c", I18N.message("pressing.fee"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("following.fee.d", I18N.message("following.fee"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("total.a+b+c+d", I18N.message("total.a+b+c+d"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("end.month.penalty", I18N.message("end.month.penalty"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("outstanding.balance", I18N.message("outstanding.balance"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("number.of.overdue", I18N.message("number.of.overdue"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("full.block", I18N.message("payment.full.block"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("latest.payment.date", I18N.message("latest.payment.date"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("latest.lock.split.deadline", I18N.message("latest.lock.split.deadline"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("first.due.1.to.3", I18N.message("first.due.1.to.3"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("last.result.date", I18N.message("last.result.date"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("latest.staff", I18N.message("latest.staff"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("current.staff", I18N.message("current.staff"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("debt.level", I18N.message("debt.level"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("terminus", I18N.message("terminus"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("status", I18N.message("status"), String.class, Align.LEFT, 180));
		return columnDefinitions;
	}
	
	/**
	 * Set Value into ColumnDefinitions
	 */
	@SuppressWarnings({ "unchecked" })
	private void setIndexedContainer() {
		Container indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		Label color[] = {lblYellow , lblRed, lblBlue };
		for (int i = 0; i < color.length; i++) {
			Item item = indexedContainer.addItem(i);
			item.getItemProperty("check").setValue(new CheckBox());
			item.getItemProperty("color").setValue(color[i]);
			item.getItemProperty("branch").setValue("Prachachuen");
			item.getItemProperty("contract.no").setValue("123456789");
			item.getItemProperty("firstname").setValue("aaa");
			item.getItemProperty("lastname").setValue("bbb");
			item.getItemProperty("due.date").setValue(DateUtils.today());
			item.getItemProperty("installment.vat").setValue("20.50");
			item.getItemProperty("amount.of.overdues").setValue("12");
			item.getItemProperty("penalty").setValue("2");
			item.getItemProperty("overdue.amount.a").setValue("30");
			item.getItemProperty("current.penalty.b").setValue("30");
			item.getItemProperty("pressing.fee.c").setValue("40");
			item.getItemProperty("following.fee.d").setValue("50");
			item.getItemProperty("total.a+b+c+d").setValue("150");
			item.getItemProperty("end.month.penalty").setValue("50");
			item.getItemProperty("outstanding.balance").setValue("100");
			item.getItemProperty("number.of.overdue").setValue("3");
			item.getItemProperty("full.block").setValue("");
			item.getItemProperty("latest.payment.date").setValue("");
			item.getItemProperty("latest.lock.split.deadline").setValue("");
			item.getItemProperty("first.due.1.to.3").setValue("");
			item.getItemProperty("last.result.date").setValue("");
			item.getItemProperty("latest.staff").setValue("");
			item.getItemProperty("current.staff").setValue("");
			item.getItemProperty("debt.level").setValue("1");
			item.getItemProperty("terminus").setValue("R");
			item.getItemProperty("status").setValue("New");
		}
	}
}

