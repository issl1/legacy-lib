package com.nokor.efinance.gui.ui.panel;


import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * This class is responsible for UI components collaborations on header part of the screen.
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HeaderPanel extends HorizontalLayout {

	private static final long serialVersionUID = 1126960707157423364L;

	/**
     * Initializes view when system enters 'initView' action state.
     *
     * @param event -  state event.
     */
    public HeaderPanel() {
        HorizontalLayout content = new HorizontalLayout();
        content.setWidth("100%");
        content.setMargin(true);
        content.addComponent(buildTitleArea());
        final CssLayout cssLayout = buildLoginArea();
        content.addComponent(cssLayout);
        content.setComponentAlignment(cssLayout, Alignment.MIDDLE_RIGHT);
        
    }


    /**
     * @return
     */
    public CssLayout buildLoginArea() {
        final CssLayout cssLayout = new CssLayout();
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        Button help = new Button(I18N.message("button.help"));
        help.setIcon(new ThemeResource("../runo/icons/16/help.png"));
        help.setStyleName(Reindeer.BUTTON_SMALL);
        buttons.addComponent(help);
        buttons.setComponentAlignment(help, Alignment.MIDDLE_CENTER);
        Button button = buildLogoutButton();
        buttons.addComponent(button);
        cssLayout.addComponent(buttons);
        return cssLayout;
    }

    /**
     * @return
     */
    public Button buildLogoutButton() {
        final Button logout = new Button(I18N.message("buton.logout"));
        logout.addClickListener(new Button.ClickListener() {
			@Override
            public void buttonClick(Button.ClickEvent event) {
                
            }
        });
        logout.setStyleName(Reindeer.BUTTON_SMALL);
        return logout;
    }

    public VerticalLayout buildTitleArea() {
        VerticalLayout titleLayout = new VerticalLayout();
        titleLayout.setStyleName("logo");
        Embedded embedded = new Embedded();
        embedded.setSource(new ThemeResource("icons/logo.png"));
        titleLayout.addComponent(embedded);
        Label description = new Label(I18N.message("application.name"));
        description.setSizeUndefined();
        titleLayout.addComponent(description);

        return titleLayout;
    }

}
