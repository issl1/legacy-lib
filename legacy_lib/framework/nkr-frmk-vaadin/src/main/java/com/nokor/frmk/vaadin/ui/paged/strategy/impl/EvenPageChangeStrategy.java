package com.nokor.frmk.vaadin.ui.paged.strategy.impl;

import com.nokor.frmk.vaadin.ui.paged.ComponentsManager;
import com.nokor.frmk.vaadin.ui.paged.strategy.PageChangeStrategy;

/**
 * Strategy for an even number of buttons page.
 */
public final class EvenPageChangeStrategy extends PageChangeStrategy {
	
	public EvenPageChangeStrategy(ComponentsManager manager) {
		super(manager);
	}

	@Override
	protected int calculatePageNumberWhereStartTheIteration(int currentPage, int buttonPageMargin) {
		return currentPage > manager.getPreviousPage() ? currentPage - buttonPageMargin + 1: currentPage - buttonPageMargin;
	}

}
