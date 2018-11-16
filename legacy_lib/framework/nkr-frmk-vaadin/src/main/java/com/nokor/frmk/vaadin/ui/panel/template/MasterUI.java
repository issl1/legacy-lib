package com.nokor.frmk.vaadin.ui.panel.template;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author prasnar
 *
 */
public abstract class MasterUI {
	
	private String comLogoPath;
	private String comURL;
	
	private VerticalLayout mainLayout;

	public abstract MenuBar getMainMenuBar();
	
	/**
	 * 
	 */
	public MasterUI() {
		super();
	}
	
	/**
	 * 
	 */
	public MasterUI(String comLogoPath, String comURL) {
		this.comLogoPath = comLogoPath;
		this.comURL = comURL;
	}
	
	public String getComLogoPath() {
		return comLogoPath;
	}
	
	public String getComURL() {
		return comURL;
	}
	
	/**
	 * 
	 */
	public void buildLayout() {
		mainLayout = initLayout(); 
	}
	
	public abstract VerticalLayout initLayout();
	public abstract void render(boolean display);
	
	public abstract VerticalLayout getContentPanel();
	public abstract VerticalLayout getTopPanel();
	public abstract VerticalLayout getLeftPanel();
	public abstract VerticalLayout getRightPanel();
	public abstract VerticalLayout getBottomPanel();

	/**
	 * @return the mainLayout
	 */
	public VerticalLayout getMainLayout() {
		return mainLayout;
	}

}
