package com.nokor.efinance.ra.ui.panel.credit.risksegment.detail;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.scoring.RiskSegment;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * Risk Segment Form Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RiskSegmentFormPanel extends AbstractFormPanel {
	/** */
	private static final long serialVersionUID = -4841949831890891361L;
	
	private TextField txtName;
	private TextField txtMinScore;
	private TextField txtMaxScore;
	private TextField txtProbabilityDefault;
	private TextField txtExpectedDistr;
	private TextField txtOdds;
	private TextField txtDecision;
	private TextArea txtRecommandations;
	
	private RiskSegment riskSegment;

	/**
	 */
	@PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtName = ComponentFactory.getTextField("name", false, 100, 200);
		txtMinScore = ComponentFactory.getTextField("min.score", false, 100, 200);
		txtMaxScore = ComponentFactory.getTextField("max.score", false, 100, 200);
		txtProbabilityDefault = ComponentFactory.getTextField("probability.of.default", false, 100, 200);
		txtExpectedDistr = ComponentFactory.getTextField("expected.distribution", false, 100, 200);
		txtOdds = ComponentFactory.getTextField("odds", false, 100, 200);
//		txtRecommandations = ComponentFactory.getTextField("recommendations", false, 100, 200);
		txtDecision = ComponentFactory.getTextField("decision", false, 100, 200);
		txtRecommandations = ComponentFactory.getTextArea("recommendations", false, 300, 100);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtName);
		formLayout.addComponent(txtMinScore);
		formLayout.addComponent(txtMaxScore);
		formLayout.addComponent(txtProbabilityDefault);
		formLayout.addComponent(txtExpectedDistr);
		formLayout.addComponent(txtOdds);
		formLayout.addComponent(txtDecision);
		formLayout.addComponent(txtRecommandations);

		return formLayout;
	}
	
	/**
	 * Assign Values to controls
	 * @param riskSegmentId
	 */
	public void assignValues(Long riskSegmentId) {
		super.reset();
		if (riskSegmentId != null) {
			riskSegment = ENTITY_SRV.getById(RiskSegment.class, riskSegmentId);
			txtName.setValue(getDefaultString(riskSegment.getName()));
			txtMinScore.setValue(getDefaultString(riskSegment.getMinScore()));
			txtMaxScore.setValue(getDefaultString(riskSegment.getMaxScore()));
			txtProbabilityDefault.setValue(getDefaultString(riskSegment.getProbabilityDefault()));
			txtExpectedDistr.setValue(getDefaultString(riskSegment.getExpectedDistr()));
			txtOdds.setValue(getDefaultString(riskSegment.getOdds()));
			txtDecision.setValue(riskSegment.getDecision());
			txtRecommandations.setValue(riskSegment.getRecommendations());
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		riskSegment = new RiskSegment();
		txtName.setValue("");
		txtMinScore.setValue("");
		txtMaxScore.setValue("");
		txtProbabilityDefault.setValue("");
		txtExpectedDistr.setValue("");
		txtOdds.setValue("");
		txtDecision.setValue("");
		txtRecommandations.setValue("");
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		riskSegment.setName(txtName.getValue());
		riskSegment.setMinScore(getDouble(txtMinScore));
		riskSegment.setMaxScore(getDouble(txtMaxScore));
		riskSegment.setProbabilityDefault(getDouble(txtProbabilityDefault));
		riskSegment.setExpectedDistr(getDouble(txtProbabilityDefault));
		riskSegment.setOdds(getDouble(txtOdds));
		riskSegment.setDecision(txtDecision.getValue());
		riskSegment.setRecommendations(txtRecommandations.getValue());
		return riskSegment;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkDoubleField(txtMinScore, "min.score");
		checkDoubleField(txtMaxScore, "max.score");
		checkDoubleField(txtProbabilityDefault, "probability.of.default");
		checkDoubleField(txtExpectedDistr, "expected.distribution");
		checkDoubleField(txtOdds, "odds");
		return errors.isEmpty();
	}
	
}
