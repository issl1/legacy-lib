package com.nokor.efinance.core.quotation.panel.comment;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
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
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Comments panel
 * @author ly.youhort
 */
public class CommentsPanel extends AbstractTabPanel implements AddClickListener, FMEntityField {
	
	private static final long serialVersionUID = 2202264472024719484L;
	
	private EntityService entityService = (EntityService) SecApplicationContextHolder.getContext().getBean("entityService");
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Comment> pagedTable;
	private Quotation quotation;
	
	/** */
	public CommentsPanel() {
		super();
		setSizeFull();				
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		addComponent(navigationPanel, 0);
		
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Comment>(this.columnDefinitions);
		pagedTable.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = -6676228064499031341L;

			@Override
			public void itemClick(ItemClickEvent event) {
				boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
				if (isDoubleClick) {
					Long itemId = (Long) event.getItemId();
					CommentFormPanel commentFormPanel = new CommentFormPanel(quotation,QuotationWkfStatus.QUO, false, null);
					commentFormPanel.setCaption(I18N.message("comment.detail"));
					Comment comment = entityService.getById(Comment.class, itemId.intValue());
					commentFormPanel.assignValueToDescription(comment);
					commentFormPanel.setWidth(650, Unit.PIXELS);
					commentFormPanel.setHeight(300, Unit.PIXELS);
			        UI.getCurrent().addWindow(commentFormPanel);
				}
			}
		});
		
		pagedTable.setItemDescriptionGenerator(new ItemDescriptionGenerator() {
			private static final long serialVersionUID = 2323324339977289357L;
			@Override
			public String generateDescription(Component source, Object itemId, Object propertyId) {
				if (propertyId == DESC_EN) {
					String description = "<table cellpadding='5' cellspacing='5'><tr><td>";
					description += (String) pagedTable.getContainerProperty(itemId, propertyId).getValue();
					description += "</td></tr></table>";
		            return description;
		        }                                                                      
		        return null;
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
	private IndexedContainer getIndexedContainer(Quotation quotation) {
		IndexedContainer indexedContainer = new IndexedContainer();
		try {
					
			for (ColumnDefinition column : this.columnDefinitions) {
				indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
			}
			indexedContainer.addContainerProperty(DESC_EN, String.class, null);
			indexedContainer.addContainerProperty("user.desc", String.class, null);
			if (ProfileUtil.isCollectionController() || ProfileUtil.isCollectionSupervisor()) {
				indexedContainer.addContainerProperty("profile", String.class, null);
			}
			
			BaseRestrictions<Comment> restrictions = new BaseRestrictions<Comment>(Comment.class);			
			List<Criterion> criterions = new ArrayList<Criterion>();
			criterions.add(Restrictions.eq("quotation.id", quotation.getId()));
			
			SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (ProfileUtil.isProfileExist(FMProfile.CO, secUser.getProfiles())) {
				criterions.add(Restrictions.eq("onlyForUW", false));
			}
			restrictions.setCriterions(criterions);
			restrictions.addOrder(Order.desc("updateDate"));
			
			List<Comment> comments = entityService.list(restrictions);
			
			for (Comment comment : comments) {
				Item item = indexedContainer.addItem(comment.getId());
				item.getItemProperty(ID).setValue(comment.getId());
				item.getItemProperty(DESC_EN).setValue(comment.getDesc());
				item.getItemProperty("user.desc").setValue(comment.getUser().getDesc());
				if (ProfileUtil.isCollectionController() || ProfileUtil.isCollectionSupervisor()) {
					item.getItemProperty("profile").setValue(comment.getUser().getDefaultProfile().getDescEn());
				}
				item.getItemProperty("updateDate").setValue(DateUtils.formatDate(comment.getUpdateDate(), DateUtils.FORMAT_YYYYMMDD_HHMMSS_SLASH));
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
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 600));
		columnDefinitions.add(new ColumnDefinition("user.desc", I18N.message("user"), String.class, Align.LEFT, 150));
		if (ProfileUtil.isCollectionController() || ProfileUtil.isCollectionSupervisor()) {
			columnDefinitions.add(new ColumnDefinition("profile", I18N.message("profile"), String.class, Align.LEFT, 150));
		}
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * Set quotation
	 * @param quotation
	 */
	public void assignValues(Quotation quotation) {
		this.quotation = quotation;
		if(quotation!=null){
			pagedTable.setContainerDataSource(getIndexedContainer(quotation));
		}
		else{
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
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		CommentFormPanel commentFormPanel = new CommentFormPanel(quotation,
				QuotationWkfStatus.QUO, false,
				new ClickListener() {
					private static final long serialVersionUID = -8159169476150724593L;
					@Override
					public void buttonClick(ClickEvent event) {
						assignValues(quotation);
					}
				});
		commentFormPanel.setCaption(I18N.message("add.comment"));
		commentFormPanel.setWidth(450, Unit.PIXELS);
		commentFormPanel.setHeight(300, Unit.PIXELS);
        UI.getCurrent().addWindow(commentFormPanel);
	}

}
