package com.nokor.efinance.core.collection.panel;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Comment tab panel in collection
 * @author uhout.cheng
 */
public class CommentPanel extends AbstractTabPanel implements AddClickListener, FMEntityField {

	/** */
	private static final long serialVersionUID = -3107238228142407180L;

	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Comment> pagedTable;
	
	/** */
	public CommentPanel() {
		super();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<Comment>(columnDefinitions);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addAddClickListener(this);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(pagedTable);
		contentLayout.addComponent(pagedTable.createControls());
        return contentLayout;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("updateTime", I18N.message("time"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("user.desc", I18N.message("user.id"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 450));
		
		return columnDefinitions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.AddClickListener#addButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void addButtonClick(ClickEvent event) {
		UI.getCurrent().addWindow(getCommentWindow());
	}
	
	/** */
	private Window getCommentWindow() {
		final Window window = new Window();
		window.setModal(true);
		window.setClosable(true);
		window.setResizable(false);		
	    window.setWidth(500, Unit.PIXELS);
	    window.setHeight(300, Unit.PIXELS);
	    window.setCaption(I18N.message("add.comment"));
	    Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -7366871133055251114L;

			public void buttonClick(ClickEvent event) {
				window.close();
            }
        });
        btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 4754734115751053990L;

			public void buttonClick(ClickEvent event) {
				window.close();
			}
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		
		TextArea txtComment = new TextArea(I18N.message("desc.en"));
		txtComment.setWidth(480, Unit.PIXELS);
		txtComment.setRows(8);
		txtComment.setMaxLength(1000);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		
		VerticalLayout commentLayout = new VerticalLayout();
		commentLayout.setMargin(true);
		commentLayout.setSpacing(true);
		commentLayout.addComponent(txtComment);
		
		verticalLayout.addComponent(commentLayout);
		window.setContent(verticalLayout);
		return window;
	}
}
