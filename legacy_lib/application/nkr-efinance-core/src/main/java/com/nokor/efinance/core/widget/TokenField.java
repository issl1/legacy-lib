package com.nokor.efinance.core.widget;

import java.util.LinkedList;
import java.util.List;

import org.vaadin.suggestfield.StringSuggestionConverter;
import org.vaadin.suggestfield.SuggestField;
import org.vaadin.suggestfield.SuggestField.NewItemsHandler;
import org.vaadin.suggestfield.SuggestField.TokenHandler;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.themes.Reindeer;

/**
 * TokenField
 * @author bunlong.taing
 */
public class TokenField  extends CssLayout implements LayoutClickListener {
	/** */
	private static final long serialVersionUID = 7193100285423371294L;

	private SuggestField suggestField;

	/**
	 */
	public TokenField() {
		super();
		setWidth("100%");
		addStyleName("blackborder");

		suggestField = new SuggestField();
		suggestField.addStyleName("noborder");
		suggestField.setNewItemsAllowed(true);
		suggestField.setImmediate(true);
		suggestField.setTokenMode(true);
		suggestField.setSuggestionConverter(new StringSuggestionConverter());
		suggestField.setWidth(200, Unit.PIXELS);
		suggestField.setPopupWidth(400);
		suggestField.setNewItemsHandler(new NewItemsHandler() {
			/** */
			private static final long serialVersionUID = 7743916241341605542L;
			@Override
			public Object addNewItem(String newItemText) {
				return newItemText;
			}
		});
		
		addComponent(suggestField);
		addLayoutClickListener(this);
	}
	
	/**
	 * @param tokenHandler
	 */
	public void setTokenHandler(TokenHandler tokenHandler) {
		suggestField.setTokenHandler(tokenHandler);
	}

	/**
	 * @see com.vaadin.event.LayoutEvents.LayoutClickListener#layoutClick(com.vaadin.event.LayoutEvents.LayoutClickEvent)
	 */
	@Override
	public void layoutClick(LayoutClickEvent event) {
		if (event.getClickedComponent() == null) {
			suggestField.focus();
		}

	}

	private ClickListener tokenRemoveClick = new ClickListener() {
		/** */
		private static final long serialVersionUID = 7523198851639776067L;
		@Override
		public void buttonClick(ClickEvent event) {
			TokenField.this.removeComponent(event.getButton());
			event.getButton().removeClickListener(tokenRemoveClick);
		}
	};

	/**
	 */
	private void clearTokens() {
		while (getComponentCount() > 1) {
			if (getComponent(0) instanceof Button) {
				final Button btn = (Button) getComponent(0);
				btn.removeClickListener(tokenRemoveClick);
				removeComponent(btn);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getValue() {
		List<String> values = new LinkedList<String>();
		for (int i = 0; i < getComponentCount(); i++) {
			if (getComponent(i) instanceof Button) {
				final Button btn = (Button) getComponent(i);
				values.add((String) btn.getData());
			}
		}
		return values;
	}

	/**
	 * 
	 * @param value
	 */
	public void setTokens(List<String> value) {
		clearTokens();
		if (value != null) {
			for (String token : value) {
				addToken(token);
			}
		}
	}
	
	/**
	 * 
	 * @param token
	 */
	public void addToken(String token) {
		if (token != null) {
			int index = getComponentIndex(suggestField);
			addComponent(generateToken(token), index);
		}
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	private Button generateToken(String token) {
		final Button btn = new NativeButton(token);
		btn.setIcon(FontAwesome.TIMES);
		btn.setData(token);
		btn.addStyleName(Reindeer.BUTTON_SMALL);
		btn.addStyleName("btn btn-success");
		btn.addClickListener(tokenRemoveClick);
		return btn;
	}

	/**
	 * @see com.vaadin.ui.AbstractComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		suggestField.setEnabled(enabled);
	}

}
