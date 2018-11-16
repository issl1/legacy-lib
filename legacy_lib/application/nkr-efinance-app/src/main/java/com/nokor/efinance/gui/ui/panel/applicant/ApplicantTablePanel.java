package com.nokor.efinance.gui.ui.panel.applicant;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.MApplicant;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ApplicantTablePanel extends AbstractTablePanel<Applicant> {
	/** */
	private static final long serialVersionUID = -4258555969899868960L;
	
	/** */
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("applicants"));
		super.init(I18N.message("applicants"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Applicant> createPagedDataProvider() {
		PagedDefinition<Applicant> pagedDefinition = new PagedDefinition<Applicant>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(MApplicant.ID, I18N.message("id"), Long.class, Align.LEFT, 80);
		
		pagedDefinition.addColumnDefinition(MApplicant.ID, I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(MApplicant.NAMEEN, I18N.message("name"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(MApplicant.APPLICANTCATEGORY + "." + EApplicantCategory.DESCEN, I18N.message("applicant.category"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(MApplicant.INDIVIDUAL + "." + Individual.IDNUMBER, I18N.message("id.no"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(MApplicant.INDIVIDUAL + "." + Individual.REFERENCE, I18N.message("customer.id"), String.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<Applicant> pagedDataProvider = new EntityPagedDataProvider<Applicant>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Applicant getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Applicant.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected ApplicantSearchPanel createSearchPanel() {
		return new ApplicantSearchPanel(this);		
	}
}
