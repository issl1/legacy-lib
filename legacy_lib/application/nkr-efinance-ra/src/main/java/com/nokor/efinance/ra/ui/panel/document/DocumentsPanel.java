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
 * Documents panel
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(DocumentsPanel.NAME)
public class DocumentsPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = -5741217846590982315L;

	public static final String NAME = "documents";
	
	@Autowired
	private DocumentTablePanel documentTablePanel;
	@Autowired
	private DocumentFormPanel documentFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		documentTablePanel.setMainPanel(this);
		documentFormPanel.setCaption(I18N.message("document"));
		getTabSheet().setTablePanel(documentTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		documentFormPanel.reset();
		getTabSheet().addFormPanel(documentFormPanel);
		getTabSheet().setSelectedTab(documentFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(documentFormPanel);
		initSelectedTab(documentFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == documentFormPanel) {
			documentFormPanel.assignValues(documentTablePanel.getItemSelectedId());
		} else if (selectedTab == documentTablePanel && getTabSheet().isNeedRefresh()) {
			documentTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
