package com.nokor.efinance.gui.ui.panel.collection.jobdistribution;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.extend.ExtendPanel;
import com.nokor.efinance.gui.ui.panel.collection.transfer.TransferPanel;
import com.nokor.efinance.gui.ui.panel.contract.ContractFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
/**
 * Job distribution panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(JobDistributionsPanel.NAME)
public class JobDistributionsPanel extends AbstractTabsheetPanel implements View {

	private static final long serialVersionUID = -6651024937294153368L;

	public static final String NAME = "job.distribution";
	
	@Autowired
	private JobDistributionTablePanel jobDistributionTablePanel;
	@Autowired
	private ContractFormPanel contractFormPanel;
	@Autowired
	private TransferPanel transferPanel;
	@Autowired
	private ExtendPanel extendPanel;
	
	private TabSheet tabSheet;
	
	/**
	 * @return the tabSheet
	 */
	public TabSheet getTabSheet() {
		return tabSheet;
	}

	/**
	 * @param tabSheet the tabSheet to set
	 */
	public void setTabSheet(TabSheet tabSheet) {
		this.tabSheet = tabSheet;
	}

	/**
	 * @return the quotationFormPanel
	 */
	public ContractFormPanel getQuotationFormPanel() {
		return contractFormPanel;
	}
	
	@PostConstruct
	public void PostConstruct() {
		tabSheet = super.initTabSheet();
		setTabSheet(tabSheet);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();

		verticalLayout.addComponent(this.tabSheet);
		verticalLayout.setExpandRatio(this.tabSheet, 1.0F);
		addComponent(verticalLayout);
		setExpandRatio(verticalLayout, 1.0F);
		
		jobDistributionTablePanel.setMainPanel(this);
		transferPanel.setJobDistributionPanel(this);
		tabSheet.setTablePanel(jobDistributionTablePanel);
		tabSheet.addFormPanel(transferPanel);
		if (ProfileUtil.isColInsideRepoLeader() || ProfileUtil.isColOutsourceRepoLeader()) {
			extendPanel.setJobDistributionPanel(this);
			tabSheet.addFormPanel(extendPanel);
		}
		initSelectedTab(jobDistributionTablePanel);
		
		tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			
			/** */
			private static final long serialVersionUID = 446090711118723113L;

			/**
			 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
			 */
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				com.vaadin.ui.Component selectedTab = (com.vaadin.ui.Component) event.getTabSheet().getSelectedTab(); 
				initSelectedTab(selectedTab);
			}
		});
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onAddEventClick()
	 */
	@Override
	public void onAddEventClick() {
		
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#onEditEventClick()
	 */
	@Override
	public void onEditEventClick() {
		tabSheet.addFormPanel(contractFormPanel);
		initSelectedTab(contractFormPanel);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel#initSelectedTab(com.vaadin.ui.Component)
	 */
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == contractFormPanel) {
			contractFormPanel.assignValues(jobDistributionTablePanel.getItemSelectedId(), true);
		} else if (selectedTab == transferPanel
				   || selectedTab == jobDistributionTablePanel
				   || selectedTab == extendPanel) {
			tabSheet.removeFormPanel(contractFormPanel);
		}	
		tabSheet.setSelectedTab(selectedTab);
	}
}
