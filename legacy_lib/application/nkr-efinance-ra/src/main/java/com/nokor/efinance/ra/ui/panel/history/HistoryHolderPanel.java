package com.nokor.efinance.ra.ui.panel.history;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.ersys.vaadin.ui.history.WkfHistoryHolderPanel;
import com.nokor.ersys.vaadin.ui.history.WkfHistorySearchPanel;

import ru.xpoft.vaadin.VaadinView;

/**
 * History Holder Panel.
 * 
 * @author pengleng.huot
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(WkfHistoryHolderPanel.NAME)
public class HistoryHolderPanel extends WkfHistoryHolderPanel {

	/** */
	private static final long serialVersionUID = 2979791729028690088L;

	public final static String CUSTOM_ENTITY_AUCTION = "history.entity.custom.auction";
	public final static String CUSTOM_ENTITY_COLLECTION = "history.entity.custom.collection";
	public final static String CUSTOM_ENTITY_CONTRACT = "history.entity.custom.contract";
	public final static String CUSTOM_ENTITY_PAYMENT = "history.entity.custom.payment";
	
	@PostConstruct
	public void PostConstruct() {
		super.PostConstruct();
		
		List<String> customEntityValues = new ArrayList<String>();
		customEntityValues.add(CUSTOM_ENTITY_AUCTION);
		customEntityValues.add(CUSTOM_ENTITY_COLLECTION);
		customEntityValues.add(CUSTOM_ENTITY_CONTRACT);
		customEntityValues.add(CUSTOM_ENTITY_PAYMENT);
		
		WkfHistorySearchPanel searchPanel = tablePanel.getSearchPanel();
		searchPanel.assignCustomEntityValues(customEntityValues);
		
		tablePanel.setHistoryDataProvider(new HistoryDataProvider());
		itemTablePanel.setHistoryItemDataProvider(new HistoryItemDataProvider());
	}
}
