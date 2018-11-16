package com.nokor.ersys.vaadin.ui.referential.reftabletopic.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.frmk.config.model.RefTable;
import com.nokor.frmk.config.model.RefTopic;
import com.nokor.frmk.config.service.RefTableTopicVO;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.EditClickListener;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author phirun.kong
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RefTableTopicTreeTablePanel extends VerticalLayout implements SelectedItem, 
	EditClickListener, VaadinServicesHelper {

	/** */
	private static final long serialVersionUID = -6235006998832407301L;
	
	private VerticalLayout messagePanel;
	protected AbstractTabsheetPanel mainPanel;
	
	private TreeTable refTableTreeTable;
	
	protected Item selectedItem;
	protected Long refTableId;
	
	private List<RefTableTopicVO> listRefTableTopicVOs;
	private Map<Long, List<RefTableTopicVO>> mapChildRefTableTopicVOs;
	private Map<Long, Long> refTableIds;
	
	/**
	 * 
	 * Work Place TreeTable Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("ref.table.data"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		init();
		addDefaultNavigation();
	}
	
	/**
	 * Init
	 */
	public void init() {
		this.messagePanel = new VerticalLayout();
		this.messagePanel.setMargin(true);
		this.messagePanel.setVisible(false);
		this.messagePanel.addStyleName("message");

		addComponent(this.messagePanel);
		addComponent(createForm());
	}
	
	/**
	 * 
	 * @return
	 */
	private com.vaadin.ui.Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		generateRefTableTopicVOs();
		
		refTableTreeTable = createLocationTreeTable();
		refreshTreeTable();
		
		contentLayout.addComponent(refTableTreeTable);
		 
		return contentLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	public TreeTable createLocationTreeTable() {		
		final TreeTable treeTable = new TreeTable();
		treeTable.setStyleName("v-workplace-tree");
		treeTable.setSizeFull();
		treeTable.setImmediate(true);	
		treeTable.setSelectable(true);
		
		treeTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            /** */
			private static final long serialVersionUID = 2068314108919135281L;

            public void itemClick(ItemClickEvent event) {
            	
            	selectedItem = event.getItem();
            	refTableId = null;
            	Object key = (Long) event.getItemId();
            	if (key != null) {
            		refTableId = refTableIds.get(key);
            	}
            	
				boolean isDoubleClick = (event.isDoubleClick())
						|| (SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet());
				if (isDoubleClick){
					mainPanel.getTabSheet().setAdd(false);
					((BaseRefTableTopicHolderPanel) mainPanel).onViewSubTab();
				}
            }
        });
		
		treeTable.setColumnHeader("desc", I18N.message("desc"));
		treeTable.setColumnHeader("descEn", I18N.message("desc.en"));
			
		return treeTable;
	}
	
	/**
	 * 
	 * @return
	 */
	private void generateRefTableTopicVOs() {
		listRefTableTopicVOs = new ArrayList<>();
		mapChildRefTableTopicVOs = new TreeMap<>();
		refTableIds = new TreeMap<>();
		
		Map<Long, Long> mapRefTopicId = new TreeMap<>(); 
		
		List<RefTopic> refTopics = ENTITY_SRV.list(RefTopic.class);
		long id = 1;
		
		for (RefTopic refTopic : refTopics) {
			boolean isAllowedRefTopic = VAADIN_SESSION_MNG.isAllowedRefTableCode(refTopic.getCode());
			if (isAllowedRefTopic) {
				RefTableTopicVO refTopicVO = new RefTableTopicVO();
				refTopicVO.setId(id++);
				refTopicVO.setDesc(refTopic.getDesc());
				refTopicVO.setDescEn(refTopic.getDescEn());
				
				if (refTopic.getParent() == null) {
					listRefTableTopicVOs.add(refTopicVO);
					mapChildRefTableTopicVOs.put(refTopicVO.getId(), new ArrayList<>());
					mapRefTopicId.put(refTopic.getId(), refTopicVO.getId());
				} else {
					List<RefTableTopicVO> refTableTopic = mapChildRefTableTopicVOs.get(mapRefTopicId.get(refTopic.getParent().getId()));
					refTableTopic.add(refTopicVO);
				}
				
				if (refTopic.getTables() != null || !refTopic.getTables().isEmpty()) {
					List<RefTableTopicVO> refTableTopicVOs = new ArrayList<RefTableTopicVO>();
					for (RefTable refTable : refTopic.getTables()) {
						boolean isAllowedRefTable = VAADIN_SESSION_MNG.isAllowedRefTableCode(refTable.getCode());
						if (isAllowedRefTable) {
							RefTableTopicVO refTableTopicVO = new RefTableTopicVO();
							refTableTopicVO.setId(id++);
							refTableTopicVO.setParentId(refTopicVO.getId());
							refTableTopicVO.setRefTableId(refTable.getId());
							refTableTopicVO.setDesc(refTable.getDesc());
							refTableTopicVO.setDescEn(refTable.getDescEn());
							refTableTopicVOs.add(refTableTopicVO);
							
							refTableIds.put(refTableTopicVO.getId(), refTableTopicVO.getRefTableId());
						}
					}
					mapChildRefTableTopicVOs.put(refTopicVO.getId(), refTableTopicVOs);
				}
			}
		}
	}
	
	
	/**
	 * Refresh TreeTable
	 */
	public void refreshTreeTable() {
		
		HierarchicalContainer refTableContainer = new HierarchicalContainer();
        
        refTableContainer.addContainerProperty("desc", Label.class, "");
        refTableContainer.addContainerProperty("descEn", Label.class, "");        

        new Object() {
        	@SuppressWarnings("unchecked")
 			public void put(List<RefTableTopicVO> orgEntityVOs, Long parentId, HierarchicalContainer container) {
         		for (int i=0; i<orgEntityVOs.size(); i++) {
         			RefTableTopicVO orgEntityVO = orgEntityVOs.get(i);

                     Item item = container.addItem(orgEntityVO.getId());
                     String desc = orgEntityVO.getDesc() != null ? orgEntityVO.getDesc(): "";
                     String descEn = orgEntityVO.getDescEn() != null ? orgEntityVO.getDescEn() : "";
                     
                     Label lblDesc = new Label(desc);
                     lblDesc.setParent(null);
                     Label lblDescEn = new Label(descEn);
                     lblDescEn.setParent(null);
                     
                     item.getItemProperty("desc").setValue(lblDesc);
                     item.getItemProperty("descEn").setValue(lblDescEn);
                     container.setParent(orgEntityVO.getId(), parentId);
                     
                     List<RefTableTopicVO> childOrgEntityVOs = mapChildRefTableTopicVOs.get(orgEntityVO.getId());
                     if (childOrgEntityVOs == null) {
                         container.setChildrenAllowed(orgEntityVO.getId(), false);
                     } else {
                    	 put(childOrgEntityVOs, orgEntityVO.getId(), container);
                     }
                 }
         	}
        }.put(listRefTableTopicVOs, null, refTableContainer);
        
		if (refTableContainer != null) {
			for (Object itemId : refTableContainer.getItemIds()) {
				Item item = refTableContainer.getItem(itemId);
				Label lblDesc = (Label) item.getItemProperty("desc").getValue();
				if (lblDesc != null) {
					lblDesc.setParent(null);
				}
				Label lblDescEn = (Label) item.getItemProperty("descEn").getValue();
				if (lblDescEn != null) {
					lblDescEn.setParent(null);
				}
			}
			
			refTableTreeTable.setContainerDataSource(refTableContainer);
			
			for (Object itemId : refTableTreeTable.getItemIds()) {
				refTableTreeTable.setCollapsed(itemId, false);
			}
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	
	/**
	 * Get Item Selected Id
	 */
	public Long getItemSelectedId() {
		if (refTableId != null) {
			return (Long) refTableId;
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public NavigationPanel addNavigationPanel() {
		NavigationPanel navigationPanel = new NavigationPanel();
		addComponentAsFirst(navigationPanel);
		return navigationPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	protected NavigationPanel addDefaultNavigation() {
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addEditClickListener(this);
		
		Button btnViewData = new NativeButton(I18N.message(I18N.message("view.data")),
			new Button.ClickListener() {

				/**	 */
				private static final long serialVersionUID = 8037669867834969655L;

				public void buttonClick(ClickEvent event) {					
					mainPanel.getTabSheet().setAdd(false);
					((BaseRefTableTopicHolderPanel) mainPanel).onViewSubTab();
				}
			}
		);
		if (AppConfigFileHelper.isFontAwesomeIcon()) {
			btnViewData.setIcon(FontAwesome.TABLE);
        }
        else {
        	btnViewData.setIcon(new ThemeResource("../nkr-default/icons/16/view.png"));
        }
		navigationPanel.addButton(btnViewData);
		return navigationPanel;
	}

	public Class<?> getERefDataClass() throws ClassNotFoundException {
		RefTable refTable = getEntity();
		if (refTable != null) {
			return Class.forName(refTable.getCode());
		}
		return null;
	}

	protected RefTable getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(RefTable.class, id);
		}
		return null;
	}
	
	/**
	 * 
	 * setMainPanel
	 */
	public void setMainPanel(AbstractTabsheetPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	@Override
	public void editButtonClick(ClickEvent event) {
		if (getItemSelectedId() == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig[] {
							new MessageBox.ButtonConfig(MessageBox.ButtonType.OK, I18N.message("ok")) });

			mb.show();
		} else {
			this.mainPanel.getTabSheet().setAdd(false);
			this.mainPanel.onEditEventClick();
		}
	}
}
