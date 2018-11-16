package com.nokor.efinance.ra.ui.panel.document;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabsheetPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Documents Group panel
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DocumentsUwGroupPanel.NAME)
public class DocumentsUwGroupPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = 6026656529474686364L;

	public static final String NAME = "documents.uw.group";
	
	@Autowired
	private DocumentUwGroupTablePanel documentUwGroupTablePanel;
	@Autowired
	private DocumentUwGroupFormPanel documentUwGroupFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		documentUwGroupTablePanel.setMainPanel(this);
		documentUwGroupFormPanel.setCaption(I18N.message("document.uw.group"));
		getTabSheet().setTablePanel(documentUwGroupTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		documentUwGroupFormPanel.reset();
		getTabSheet().addFormPanel(documentUwGroupFormPanel);
		getTabSheet().setSelectedTab(documentUwGroupFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(documentUwGroupFormPanel);
		initSelectedTab(documentUwGroupFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == documentUwGroupFormPanel) {
			documentUwGroupFormPanel.assignValues(documentUwGroupTablePanel.getItemSelectedId());
		} else if (selectedTab == documentUwGroupTablePanel && getTabSheet().isNeedRefresh()) {
			documentUwGroupTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
