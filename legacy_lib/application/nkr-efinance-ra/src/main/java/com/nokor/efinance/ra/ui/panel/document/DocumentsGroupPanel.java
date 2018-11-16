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
@VaadinView(DocumentsGroupPanel.NAME)
public class DocumentsGroupPanel extends AbstractTabsheetPanel implements View {
	
	private static final long serialVersionUID = 1498620652639163991L;

	public static final String NAME = "documents.group";
	
	@Autowired
	private DocumentGroupTablePanel documentGroupTablePanel;
	@Autowired
	private DocumentGroupFormPanel documentGroupFormPanel;
	
	@PostConstruct
	public void PostConstruct() {
		super.init();
		documentGroupTablePanel.setMainPanel(this);
		documentGroupFormPanel.setCaption(I18N.message("document.group"));
		getTabSheet().setTablePanel(documentGroupTablePanel);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	
	@Override
	public void onAddEventClick() {
		documentGroupFormPanel.reset();
		getTabSheet().addFormPanel(documentGroupFormPanel);
		getTabSheet().setSelectedTab(documentGroupFormPanel);
	}

	@Override
	public void onEditEventClick() {
		getTabSheet().addFormPanel(documentGroupFormPanel);
		initSelectedTab(documentGroupFormPanel);
	}
	
	@Override
	public void initSelectedTab(com.vaadin.ui.Component selectedTab) {
		if (selectedTab == documentGroupFormPanel) {
			documentGroupFormPanel.assignValues(documentGroupTablePanel.getItemSelectedId());
		} else if (selectedTab == documentGroupTablePanel && getTabSheet().isNeedRefresh()) {
			documentGroupTablePanel.refresh();
		}
		getTabSheet().setSelectedTab(selectedTab);
	}
}
