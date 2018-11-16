package com.nokor.efinance.gui.ui.panel.dashboard;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.quotation.panel.QuotationsPanel;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
//@VaadinView(DashboardPanel.NAME)
public class DashboardOldPanel extends VerticalLayout implements
		QuotationEntityField, SelectedItem {

	private static final long serialVersionUID = 6227740006388204118L;

	@Autowired
	private EntityService entityService;

	private SecUserDetail secUserDetail;
	private DashboardReportsPanel dashboardReportsPanel;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("dashboard"));
		secUserDetail = getSecUserDetail();
		assignValues();
	}

	/**
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		BaseRestrictions<SecUserDetail> restrictions = new BaseRestrictions<SecUserDetail>(SecUserDetail.class);
		restrictions.addCriterion(Restrictions.eq("secUser.id", secUser.getId()));
		List<SecUserDetail> usrDetails = entityService.list(restrictions);
		if (!usrDetails.isEmpty()) {
			return usrDetails.get(0);
		}
		SecUserDetail usrDetail = new SecUserDetail();
		usrDetail.setSecUser(secUser);
		return usrDetail;
	}

	/**
	 */
	private void assignValues() {
		if (secUserDetail.getDealer() == null) {
			final Window winDealer = new Window();
			winDealer.setModal(true);
			winDealer.setClosable(false);
			winDealer.setResizable(false);
			winDealer.setWidth(340, Unit.PIXELS);
			winDealer.setHeight(160, Unit.PIXELS);
			winDealer.setCaption(I18N.message("please.select.dealer"));
			final DealerComboBox cbxDealer = new DealerComboBox(DataReference.getInstance().getDealers());
			Button btnCancel = new NativeButton(I18N.message("cancel"),
					new Button.ClickListener() {
						private static final long serialVersionUID = 3975121141565713259L;

						public void buttonClick(ClickEvent event) {
							Page.getCurrent().setUriFragment("!" + QuotationsPanel.NAME);
							winDealer.close();
						}
					});
			btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));

			Button btnSave = new NativeButton(I18N.message("save"),
					new Button.ClickListener() {
						private static final long serialVersionUID = 8088485001713740490L;

						public void buttonClick(ClickEvent event) {
							secUserDetail.setDealer(cbxDealer.getSelectedEntity());
							entityService.saveOrUpdate(secUserDetail);
							assignValues();
							winDealer.close();
						}
					});
			btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));

			NavigationPanel navigationPanel = new NavigationPanel();
			navigationPanel.addButton(btnSave);
			navigationPanel.addButton(btnCancel);

			final GridLayout gridLayout = new GridLayout(2, 1);
			gridLayout.setMargin(true);
			gridLayout.setSpacing(true);
			gridLayout.addComponent(new Label(I18N.message("dealer")), 0, 0);
			gridLayout.addComponent(cbxDealer, 1, 0);

			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.addComponent(navigationPanel);
			verticalLayout.addComponent(gridLayout);
			winDealer.setContent(verticalLayout);
			UI.getCurrent().addWindow(winDealer);
		} 
		
		else {
			dashboardReportsPanel = new DashboardReportsPanel();
			dashboardReportsPanel.search(secUserDetail.getDealer());
			DashboardFormPanel dashboardFormPanel = new DashboardFormPanel(secUserDetail);
			dashboardFormPanel.setDashboardReportsPanel(dashboardReportsPanel);
			
			DashboardFormWithoutButtonsPanel dashboardFormWithoutButtonsPanel = new DashboardFormWithoutButtonsPanel(secUserDetail);
			dashboardFormWithoutButtonsPanel.setDashboardReportsPanel(dashboardReportsPanel);

			VerticalLayout verticalLayout = new VerticalLayout();
			verticalLayout.setSizeFull();
			
			if (ProfileUtil.isSeniorCO()) {
				verticalLayout.addComponent(dashboardFormWithoutButtonsPanel);
				verticalLayout.addComponent(dashboardReportsPanel);
				} 
			
			else {
				verticalLayout.addComponent(dashboardFormPanel);
				verticalLayout.addComponent(dashboardReportsPanel);
			}
			addComponent(verticalLayout);
		}
	}

	/**
	 * Set main panel
	 * 
	 * @param mainPanel
	 */
	public void setMainPanel(AbstractTabsheetPanel mainPanel) {
		dashboardReportsPanel.setMainPanel(mainPanel);
	}

	/**
	 * Get Item Selected Id
	 * 
	 * @return
	 */
	public Long getItemSelectedId() {
		return this.dashboardReportsPanel.getItemSelectedId();
	}

	@Override
	public Item getSelectedItem() {
		return dashboardReportsPanel.getSelectedItem();
	}
}
