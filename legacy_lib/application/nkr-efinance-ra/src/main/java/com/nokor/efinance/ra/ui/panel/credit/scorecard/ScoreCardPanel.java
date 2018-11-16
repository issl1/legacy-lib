package com.nokor.efinance.ra.ui.panel.credit.scorecard;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import ru.xpoft.vaadin.VaadinView;
import com.nokor.efinance.core.scoring.ScoreCard;
import com.nokor.efinance.core.scoring.ScoreGroup;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(ScoreCardPanel.NAME)
public class ScoreCardPanel extends AbstractControlPanel implements View, FMEntityField, ClickListener, SeuksaServicesHelper {
	
	/** */
	private static final long serialVersionUID = -4353086637402978052L;
	
	public static final String NAME = "score.cards";
	
	private VerticalLayout mainVerLayout;
	private List<CheckBox> cbScoreGroups;
	private List<TextField> txtScoreCards;
	private Map<Long, List<ScoreCard>> scoreGroupMaps;
	private Button btnSave;
	private Button btnRefresh;
	private List<String> errors;
	private VerticalLayout messagePanel;
	private TabSheet mainTabSheet;
	
	/**
	 * 
	 */
	public ScoreCardPanel() {
		setSpacing(true);
		setMargin(true);
		addComponent(createForm());
	}
	
	/**
	 * 
	 * @return
	 */
	private Component createForm() {
		init();
		mainTabSheet = new TabSheet();
		mainTabSheet.setHeight(780, Unit.PIXELS);
		mainTabSheet.addTab(mainVerLayout, I18N.message("score.card"));
		return mainTabSheet;
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	private FormLayout getFormLayout(Component component) {
		FormLayout frmLayout = new FormLayout();
		frmLayout.setStyleName("myform-align-left");
		frmLayout.setMargin(true);
		frmLayout.setSpacing(false);
		frmLayout.addComponent(component);
		return frmLayout;
	}
	
	/**
	 * 
	 */
	private void init() {
		mainVerLayout = new VerticalLayout();
		mainVerLayout.setMargin(true);
		mainVerLayout.setSpacing(true);
		
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		btnRefresh = new NativeButton(I18N.message("refresh"));
		btnRefresh.setIcon(FontAwesome.REFRESH);
		btnRefresh.addClickListener(this);
		
		errors = new ArrayList<String>();
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeFull();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnRefresh);
		
		mainVerLayout.addComponent(navigationPanel);
		mainVerLayout.addComponent(messagePanel);
		
		txtScoreCards = new ArrayList<TextField>();
		cbScoreGroups = new ArrayList<CheckBox>();
		scoreGroupMaps = new LinkedHashMap<Long, List<ScoreCard>>();
		
		List<ScoreCard> scoreCards = ENTITY_SRV.list(getScoreCardRestriction());
		if (scoreCards != null && !scoreCards.isEmpty()) {
			for (ScoreCard scoreCard : scoreCards) {
				List<ScoreCard> scoreCardsByGroup = scoreGroupMaps.get(scoreCard.getScoreGroup().getId());
				if (scoreCardsByGroup == null) {
					scoreCardsByGroup = new ArrayList<ScoreCard>();
					scoreGroupMaps.put(scoreCard.getScoreGroup().getId(), scoreCardsByGroup);
				}
				scoreCardsByGroup.add(scoreCard);
			}
		}		
		List<ScoreGroup> scoreGroups = ENTITY_SRV.list(getScoreGroupRestriction());
		for (ScoreGroup  scoreGroup : scoreGroups) {
        	CheckBox cbScoreGroup = new CheckBox();
        	cbScoreGroup.setCaption(scoreGroup.getDescEn());
        	cbScoreGroup.setData(scoreGroup);
        	if (EStatusRecord.ACTIV.getId().equals(scoreGroup.getStatusRecord().getId())) {
				cbScoreGroup.setValue(true);
			} else {
				cbScoreGroup.setValue(false);
			}
        	cbScoreGroups.add(cbScoreGroup);
        	
        	HorizontalLayout cbLayout = new HorizontalLayout();
    		final Button btnGroup = new Button();
    		btnGroup.setStyleName(Reindeer.BUTTON_LINK);
    		btnGroup.setIcon(FontAwesome.CARET_RIGHT);
    		cbLayout.addComponent(btnGroup);
    		cbLayout.addComponent(cbScoreGroup);
    		
        	VerticalLayout btnHideShowLayout = new VerticalLayout();
	        btnHideShowLayout.setSpacing(false);
	        btnHideShowLayout.setMargin(true);
	        btnHideShowLayout.addComponent(cbLayout);
			
	        List<ScoreCard> scoreCardsByGroup = scoreGroupMaps.get(scoreGroup.getId());
	        Panel mainPanel = new Panel();
	        btnGroup.setVisible(scoreCardsByGroup != null);
	        if (scoreCardsByGroup != null && !scoreCardsByGroup.isEmpty()) {    
	        	
	        	final GridLayout gridLayout = new GridLayout(2, scoreCardsByGroup.size() + 1);
	        	gridLayout.setSpacing(false);
	        	gridLayout.setMargin(true);
	        	gridLayout.setVisible(false);
	        	gridLayout.setSizeFull();
	        	
	        	int i = 1;
	        	for (ScoreCard scoreCard : scoreCardsByGroup) {
	        		TextField txtScoreCard = ComponentFactory.getTextField(scoreCard.getDescEn(), false, 20, 70);
	        		txtScoreCard.setValue(getDefaultString(scoreCard.getScoreValue()));
	        		txtScoreCard.setData(scoreCard);
	        		txtScoreCard.addStyleName("mytextfield-caption");
	        		txtScoreCards.add(txtScoreCard);
	        		
	        		gridLayout.addComponent(getFormLayout(txtScoreCard), 0, i);
	        		i++;
	        	}
	        	btnGroup.addClickListener(new ClickListener() {
	 				
	        		/** */
	 				private static final long serialVersionUID = -4397865683057016309L;

	 				/**
	 				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 				 */
	 				@Override
	 				public void buttonClick(ClickEvent event) {
	 					gridLayout.setVisible(!gridLayout.isVisible());	
	 					if (gridLayout.isVisible()) {
	 						btnGroup.setIcon(FontAwesome.CARET_DOWN);
	 					} else {
	 						btnGroup.setIcon(FontAwesome.CARET_RIGHT);
	 					}
	 				}
	 			});
	        	btnHideShowLayout.addComponent(gridLayout);
	        } 
	        mainPanel.setContent(btnHideShowLayout);
        	mainVerLayout.addComponent(mainPanel);
        }
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSave)) {
			if (validate()) {
				List<ScoreGroup> scoreGroups = new ArrayList<ScoreGroup>();
				List<ScoreCard> scoreCards = new ArrayList<ScoreCard>();
				for (int i = 0; i < cbScoreGroups.size(); i++) {
					CheckBox cbScoreGroup = cbScoreGroups.get(i);
					ScoreGroup scoreGroup = (ScoreGroup) cbScoreGroup.getData();
					scoreGroup.setStatusRecord(cbScoreGroup.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
					scoreGroups.add(scoreGroup);
				}
				// TODO saveOrUpdate Score groups
				logger.debug(">> saveOrUpdate score groups");
				
				ENTITY_SRV.saveOrUpdateList(scoreGroups);
				
				logger.debug("<< saveOrUpdate score groups");
				
				for (int i = 0; i < txtScoreCards.size(); i++) {
					TextField txtScoreCard = txtScoreCards.get(i);
					ScoreCard scoreCard = (ScoreCard) txtScoreCard.getData();
					if (StringUtils.isEmpty(txtScoreCard.getValue())) {
						scoreCard.setScoreValue(0d);
					} else {
						scoreCard.setScoreValue(getDouble(txtScoreCard));
					}
					scoreCards.add(scoreCard);
				}
				// TODO saveOrUpdate Score cards
				logger.debug(">> saveOrUpdate score cards");
				
				ENTITY_SRV.saveOrUpdateList(scoreCards);
				
				logger.debug("<< saveOrUpdate score cards");
				assignValues();
				displaySuccessMsg();
			} else {
				displayErrors();
			}
		} else if (event.getButton().equals(btnRefresh)) {
			removeErrors();
			assignValues();
		}
	}
	
	/**
	 * 
	 */
	private void assignValues() {
		if (!cbScoreGroups.isEmpty()) {
			for (int i = 0; i < cbScoreGroups.size(); i++) {
				CheckBox cbScoreGroup = cbScoreGroups.get(i);
				ScoreGroup scoreGroup = (ScoreGroup) cbScoreGroup.getData();
				cbScoreGroup.setValue(scoreGroup.getStatusRecord().equals(EStatusRecord.ACTIV));
			}
		}
		if (!txtScoreCards.isEmpty()) {
			for (int i = 0; i < txtScoreCards.size(); i++) {
				TextField txtScoreCard = txtScoreCards.get(i);
				ScoreCard scoreCard = (ScoreCard) txtScoreCard.getData();
				txtScoreCard.setValue(getDefaultString(scoreCard.getScoreValue()));
			}
		}
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<ScoreCard> getScoreCardRestriction() {
		BaseRestrictions<ScoreCard> restrictions = new BaseRestrictions<>(ScoreCard.class);
		restrictions.addOrder(Order.asc(SORT_INDEX));
		return restrictions;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<ScoreGroup> getScoreGroupRestriction() {
		BaseRestrictions<ScoreGroup> restrictions = new BaseRestrictions<>(ScoreGroup.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		restrictions.getStatusRecordList().add(EStatusRecord.INACT);
		restrictions.addOrder(Order.asc(SORT_INDEX));
		return restrictions;
	}
	
	/**
	 * Validate the score card form
	 * @return
	 */
	private boolean validate() {
		messagePanel.setVisible(false);
		messagePanel.removeAllComponents();
		errors.clear();
		for (int i = 0; i < txtScoreCards.size(); i++) {
			TextField txtScoreCard = txtScoreCards.get(i);
			checkDoubleFields(txtScoreCard, txtScoreCard.getCaption());
		}
		return errors.isEmpty();
	}
	
	/**
	 * @param component
	 * @param message
	 * @return
	 */
	private List<String> checkDoubleFields(AbstractTextField field, String messageKey) {
		if (StringUtils.isNotEmpty(field.getValue())) {
			try {
				Double.parseDouble(field.getValue());
			} catch (Exception e) {
				errors.add(I18N.message("field.value.incorrect.1", I18N.message(messageKey)));
			}
		}
		return errors;
	}
	
	/**
	 * display success 
	 */
	private void displaySuccessMsg() {
		Label messageLabel = new Label(I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		Label iconLabel = new Label();
		iconLabel.setIcon(new ThemeResource("../nkr-default/icons/16/twitter.png"));
		iconLabel.addStyleName("success-icon");
		messagePanel.removeAllComponents();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.addComponent(iconLabel);
		layout.addComponent(messageLabel);
		messagePanel.addComponent(layout);
		messagePanel.setVisible(true);
	}
	
	/**
	 * Display Errors
	 */
	public void displayErrors() {
		messagePanel.removeAllComponents();
		if (!(errors.isEmpty())) {
			for (String error : errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
	}
	
	/**
	 * Reset panel
	 */
	private void removeErrors() {
		messagePanel.setVisible(false);
		messagePanel.removeAllComponents();
		errors.clear();
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		removeErrors();
	}
}
