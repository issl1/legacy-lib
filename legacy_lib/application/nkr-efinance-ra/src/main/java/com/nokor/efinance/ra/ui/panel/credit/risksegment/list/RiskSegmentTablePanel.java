package com.nokor.efinance.ra.ui.panel.credit.risksegment.list;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.scoring.MRiskSegment;
import com.nokor.efinance.core.scoring.RiskSegment;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Risk Segment Table Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RiskSegmentTablePanel extends AbstractTablePanel<RiskSegment> implements MRiskSegment {
	/** */
	private static final long serialVersionUID = 6843806660891597491L;
	
	/**
	 * Risk Segment Table Panel post constructor
	 */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("risk.segments"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		super.init(I18N.message("risk.segments"));
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<RiskSegment> createPagedDataProvider() {
		PagedDefinition<RiskSegment> pagedDefinition = new PagedDefinition<RiskSegment>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(NAME, I18N.message("name"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(MIN_SCORE, I18N.message("min.score"), Double.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(MAX_SCORE, I18N.message("max.score"), Double.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(PROBABILITY_DEFAULT, I18N.message("probability.of.default"), Double.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(EXPECTED_DISTR, I18N.message("expected.distribution"), Double.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(ODDS, I18N.message("odds"), Double.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(RECOMMENDATIONS, I18N.message("recommendations"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DECISION, I18N.message("decision"), String.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<RiskSegment> pagedDataProvider = new EntityPagedDataProvider<RiskSegment>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected RiskSegment getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(RiskSegment.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<RiskSegment> createSearchPanel() {
		return new RiskSegmentSearchPanel(this);
	}
	
}
