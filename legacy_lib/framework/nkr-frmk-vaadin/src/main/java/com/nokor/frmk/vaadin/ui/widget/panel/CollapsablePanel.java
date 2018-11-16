package com.nokor.frmk.vaadin.ui.widget.panel;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

/**
 * 
 * @author vanrin.chhea
 *
 */
public class CollapsablePanel extends CustomComponent {

	/** */
	private static final long serialVersionUID = 4416239653440462534L;
	private CssLayout root;
	private CssLayout bodyContainer;
	
	/**
	 * Constructor
	 * @param title
	 * @param body
	 */
	public CollapsablePanel(String title, Component body) {
		super();
		root = new CssLayout();
		root.addStyleName("collapseble-panel-container");
		setCompositionRoot(root);
		draw(title, body);
	}
	
	/**
	 * 
	 */
	private void toggleBodyVisible() {
		bodyContainer.setVisible(!bodyContainer.isVisible());
	}
	
	/**
	 * 
	 * @param visible
	 */
	public void setBodyVisible(boolean visible) {
		bodyContainer.setVisible(visible);
	}
	
	/**
	 * 
	 * @param title
	 * @param body
	 */
	private void draw(String title, Component body) {
		CssLayout titleLayout = new CssLayout();
		titleLayout.addStyleName("collapseble-panel-title");
		titleLayout.addComponent(new Label(title));
		titleLayout.addLayoutClickListener(new LayoutClickListener() {

			/**	 */
			private static final long serialVersionUID = 4273809993261985340L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				toggleBodyVisible();
			}
		});

		bodyContainer = new CssLayout();
		bodyContainer.addStyleName("collapseble-panel-body");
		bodyContainer.addComponent(body);

		root.addComponent(titleLayout);
		root.addComponent(bodyContainer);
	}
}
