package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.weight;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.RefDataId;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import com.nokor.efinance.core.collection.model.EColGroup;
import com.nokor.efinance.core.collection.model.EColTeam;
import com.nokor.efinance.core.collection.model.EColTeamGroup;
import com.nokor.efinance.core.collection.model.EColWeight;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CollectionWeightPanel extends AbstractTabPanel implements ClickListener, CollectionEntityField, FrmkServicesHelper {

	private static final long serialVersionUID = 4081894738568354319L;
	
	private SimplePagedTable<EColWeight> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private Indexed indexedContainer;
	
	private NumberField txtDistribution;
	private Button btnSave;
	private Button btnCancel;
	private Button btnSearch;
	private Button btnReset;
	private EntityRefComboBox<EColTeam> cbxTeam;
	private EntityRefComboBox<EColGroup> cbxGroup;
	
	private VerticalLayout messagePanel;
	private TabSheet tabSheet;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		tabSheet = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		this.messagePanel = new VerticalLayout();
		this.messagePanel.setMargin(true);
		this.messagePanel.setVisible(false);
		this.messagePanel.addStyleName("message");
		
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		btnSave.addClickListener(this);
		
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		btnCancel.addClickListener(this);
		
		btnSearch = new Button(I18N.message("search"), this);
		btnSearch.setIcon(FontAwesome.SEARCH);
		
		btnReset = new Button(I18N.message("reset"), this);
		btnReset.setIcon(FontAwesome.ERASER);
		
		cbxTeam = getEntityRefComboBox(new BaseRestrictions<>(EColTeam.class));
		cbxTeam.setCaption(I18N.message("team"));
		cbxGroup = getEntityRefComboBox(new BaseRestrictions<>(EColGroup.class));
		cbxGroup.setCaption(I18N.message("group"));
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<EColWeight>(this.columnDefinitions);	
		contentLayout.addComponent(searchPanel());
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(this.messagePanel);
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
		tabSheet.addTab(contentLayout, I18N.message("weight"));
		this.setIndexedContainer(ENTITY_SRV.list(getRestrictions()));
		return tabSheet;
	}
	
	/**
	 * 
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
			List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
			columnDefinitions.add(new ColumnDefinition("staff.code", I18N.message("staff.code"), String.class, Align.LEFT, 300));
			columnDefinitions.add(new ColumnDefinition("definitions.percentage", I18N.message("definitions.percentage"), TextField.class, Align.LEFT, 400));
			return columnDefinitions;
		}
	/**
	 * 
	 * @param colWeights
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<EColWeight> colWeights) {
		indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (EColWeight eColWeight : colWeights) {
			final Item item = indexedContainer.addItem(eColWeight.getId());
			txtDistribution = ComponentFactory.getNumberField("", false, 80, 100);
			DecimalFormat df = new DecimalFormat("#0");
			txtDistribution.setValue(df.format(eColWeight.getDistributionRate() != null ? eColWeight.getDistributionRate() : 0d));
			
			item.getItemProperty("staff.code").setValue(eColWeight.getStaff().getDesc());
			item.getItemProperty("definitions.percentage").setValue(txtDistribution);
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<EColWeight> getRestrictions () {
		BaseRestrictions<EColWeight> restrictions = new BaseRestrictions<>(EColWeight.class);
		List<EColTeamGroup> colTeamGroups;
		List<SecUser> secUsers = new ArrayList<>();
		if (cbxTeam.getSelectedEntity() != null) {
			if (cbxGroup.getSelectedEntity() != null) {
				colTeamGroups = this.getSecuserFormTeamGroup(cbxTeam.getSelectedEntity(), cbxGroup.getSelectedEntity());
			} else {
				colTeamGroups = this.getSecuserFormTeamGroup(cbxTeam.getSelectedEntity(), null);
			}
			if (colTeamGroups != null && !colTeamGroups.isEmpty()) {
				for (EColTeamGroup colTeamGroup : colTeamGroups) {
					 secUsers.add(colTeamGroup.getTeamLeader());
				}
				restrictions.addCriterion(Restrictions.in(STAFF, secUsers));
			}
		} else if (cbxGroup.getSelectedEntity() != null) {
			colTeamGroups = this.getSecuserFormTeamGroup(null, cbxGroup.getSelectedEntity());
			if (colTeamGroups != null && !colTeamGroups.isEmpty()) {
				for (EColTeamGroup colTeamGroup : colTeamGroups) {
					 secUsers.add(colTeamGroup.getTeamLeader());
				}
				restrictions.addCriterion(Restrictions.in(STAFF, secUsers));
			}
		}
		restrictions.addOrder(Order.asc(ID));
		return restrictions;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			List<EColWeight> colWeights = ENTITY_SRV.list(getRestrictions());
			for (EColWeight colWeight : colWeights) {
				final Item item = indexedContainer.getItem(colWeight.getId());
				String distributionRate = String.valueOf(item.getItemProperty("definitions.percentage").getValue());
				
				colWeight.setDistributionRate(Double.valueOf(StringUtils.isEmpty(distributionRate) ? "0" : distributionRate));
				ENTITY_SRV.saveOrUpdate(colWeight);	
			}
			setIndexedContainer(ENTITY_SRV.list(getRestrictions()));
			displaySuccess();
		} else if (event.getButton() == btnCancel) {
			
		} else if (event.getButton() == btnSearch) {
			this.setIndexedContainer(ENTITY_SRV.list(getRestrictions()));
		} else if (event.getButton() == btnReset) {
			cbxTeam.setSelectedEntity(null);
			cbxGroup.setSelectedEntity(null);
		}
		
	}
	
	/**
	 * 
	 */
	public void displaySuccess() {
		Label messageLabel = new Label(
				I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		Label iconLabel = new Label();
		iconLabel.setIcon(new ThemeResource(
				"../nkr-default/icons/16/twitter.png"));
		iconLabel.addStyleName("success-icon");
		this.messagePanel.removeAllComponents();
		this.messagePanel.addComponent(new HorizontalLayout(new Component[] {
				iconLabel, messageLabel }));
		this.messagePanel.setVisible(true);
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel searchPanel () {
		HorizontalLayout comboboxContent = new HorizontalLayout();
		comboboxContent.addComponent(new FormLayout(cbxTeam));
		comboboxContent.addComponent(new FormLayout(cbxGroup));
		
		HorizontalLayout buttonContent = new HorizontalLayout();
		buttonContent.addComponent(btnSearch);
		buttonContent.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		buttonContent.addComponent(btnReset);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(comboboxContent);
		verticalLayout.addComponent(buttonContent);
		verticalLayout.setComponentAlignment(buttonContent, Alignment.MIDDLE_CENTER);
		
		Panel panel = new Panel();
		panel.setCaption(I18N.message("search"));
		panel.setContent(verticalLayout);
		
		return panel;
	}
	
	/**
	 * 
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T>  getEntityRefComboBox(BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>();
		comboBox.setWidth(220, Unit.PIXELS);
		comboBox.setRestrictions(restrictions);
		comboBox.renderer();
		return comboBox;
	}
	
	/**
	 * 
	 * @param colTeam
	 * @param colGroup
	 * @return
	 */
	private List<EColTeamGroup> getSecuserFormTeamGroup (EColTeam colTeam, EColGroup colGroup) {
		BaseRestrictions<EColTeamGroup> restrictions = new BaseRestrictions<>(EColTeamGroup.class);
		if (colTeam != null) {
			restrictions.addCriterion(Restrictions.eq(TEAM, colTeam));
		}
		if (colGroup != null) {
			restrictions.addCriterion(Restrictions.eq(GROUP, colGroup));
		}
		List<EColTeamGroup> colTeamGroups = ENTITY_SRV.list(restrictions);
		return colTeamGroups;
	}
}
