package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Past dues panel in collection right panel
 * @author uhout.cheng
 */
public class CollectionAssetsPanel extends AbstractControlPanel implements ClickListener {

	/** */
	private static final long serialVersionUID = -2964708842236584078L;
	
	private Button btnHide;
	
	private CollectionAssetsTablePanel assetsTablePanel;
	
	/**
	 * 
	 */
	public CollectionAssetsPanel() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		btnHide = ComponentLayoutFactory.getButtonStyle("hide", null, 70, "btn btn-success button-small");
		btnHide.addClickListener(this);
		
		assetsTablePanel = new CollectionAssetsTablePanel();
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		verLayout.addComponent(btnHide);
		verLayout.setComponentAlignment(btnHide, Alignment.TOP_RIGHT);
		verLayout.addComponent(assetsTablePanel);
		
		Panel mainPanel = new Panel();
		mainPanel.setCaption(I18N.message("assets"));
		mainPanel.setContent(verLayout);
		addComponent(mainPanel);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnHide)) {
			assetsTablePanel.setVisible(!assetsTablePanel.isVisible());
			btnHide.setCaption(!assetsTablePanel.isVisible() ? I18N.message("show") : I18N.message("hide"));
		}
	}
	
	/**
	 * 
	 * @param asset
	 */
	public void assignValues(Asset asset) {
		assetsTablePanel.assignValues(asset);
	}
	
}
