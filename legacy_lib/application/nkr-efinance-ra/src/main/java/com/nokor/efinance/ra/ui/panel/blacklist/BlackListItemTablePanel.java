package com.nokor.efinance.ra.ui.panel.blacklist;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.common.reference.model.BlackListItem;
import com.nokor.efinance.core.common.reference.model.MBlackListItem;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BlackListItemTablePanel extends AbstractTablePanel<BlackListItem> implements MBlackListItem, ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 6572123304489893074L;
	
	private Button btnUpload;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("blacklist.items"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		super.init(I18N.message("blacklist.items"));
		
		addDefaultNavigation();
	}
	
	/**
	 * Get item selected id
	 * @return
	 */
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty(ID).getValue();
		}
		return null;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createBeforeTablePanel()
	 */
	@Override
	protected Panel createBeforeTablePanel() {
		btnUpload = ComponentFactory.getButton("upload");
		btnUpload.setIcon(FontAwesome.UPLOAD);
		btnUpload.addClickListener(this);
		
		VerticalLayout mainVerLayout = new VerticalLayout();
		mainVerLayout.addComponent(btnUpload);
		mainVerLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
		
		Panel mainPanel = new Panel();
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		mainPanel.setContent(mainVerLayout);
		return mainPanel;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnUpload)) {
			BlackListItemPopupWindow blackListItemPopupWindow = new BlackListItemPopupWindow(this);
			UI.getCurrent().addWindow(blackListItemPopupWindow);
		} 
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<BlackListItem> createPagedDataProvider() {
		PagedDefinition<BlackListItem> pagedDefinition = new PagedDefinition<BlackListItem>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70, false);
		pagedDefinition.addColumnDefinition(IDNUMBER, I18N.message("id.no"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(FIRSTNAMEEN, I18N.message("firstname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(LASTNAMEEN, I18N.message("lastname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(MOBILEPERSO, I18N.message("phone.number"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(REASON + "." + FMEntityField.DESC_EN, I18N.message("reason"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(SOURCE + "." + FMEntityField.DESC_EN, I18N.message("source"), String.class, Align.LEFT, 150);
		
		pagedDefinition.addColumnDefinition(DETAILS, I18N.message("details"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(REMARKS, I18N.message("remarks"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(FMEntityField.STATUS_RECORD + "." + FMEntityField.DESC_EN, I18N.message("action"), String.class, Align.LEFT, 120);
		
		EntityPagedDataProvider<BlackListItem> pagedDataProvider = new EntityPagedDataProvider<BlackListItem>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#deleteButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void deleteButtonClick(ClickEvent event) {
		BlackListItem selectedEntity = getEntity();
		if (selectedEntity == null) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
					MessageBox.Icon.WARN, I18N.message("msg.info.delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete", String.valueOf(selectedEntity.getId())),
		        new ConfirmDialog.Listener() {
		            
					/** */
					private static final long serialVersionUID = 7878594235006115632L;

					public void onClose(ConfirmDialog dialog) {		            	
						// after the deletion, refresh with same criteria
		                if (dialog.isConfirmed()) {
		                	try {
		                		ORG_SRV.deleteProcess(selectedEntity);
		                		selectedItem = null;
		                		refresh();
		                	} catch (Exception ex) {
			    				logger.error(ex.getMessage(), ex);
			    				if (ex instanceof DataIntegrityViolationException) {
			    					MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("warning"),
			            					MessageBox.Icon.WARN, I18N.message("msg.warning.delete.selected.item.is.used"),
			            					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			            			mb.setWidth("300px");
			            			mb.setHeight("160px");
			            			mb.show();
			    				} else {
			    					MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("error"),
			            					MessageBox.Icon.ERROR, I18N.message("msg.error.technical"),
			            					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			            			mb.setWidth("300px");
			            			mb.setHeight("160px");
			            			mb.show();
			    				}
		                	}
		                }
					}
			});
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected BlackListItem getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		    return ENTITY_SRV.getById(BlackListItem.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected BlackListItemSearchPanel createSearchPanel() {
		return new BlackListItemSearchPanel(this);		
	}
}
