package com.nokor.frmk.vaadin.ui.paged.strategy.impl;

import com.nokor.frmk.vaadin.ui.paged.ComponentsManager;
import com.nokor.frmk.vaadin.ui.paged.strategy.PageChangeStrategy;

/**
 * Strategy for an odd number of buttons page.
 */
public final class OddPageChangeStrategy extends PageChangeStrategy {
	
	public OddPageChangeStrategy(ComponentsManager manager) {
		super(manager);
	}

	@Override
	protected int calculatePageNumberWhereStartTheIteration(int currentPage, int buttonPageMargin) {
		return currentPage - buttonPageMargin;
	}

}
