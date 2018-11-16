package com.nokor.ersys.vaadin.ui.security;

import com.nokor.common.app.tools.helper.AppConfigFileHelper;
import com.nokor.ersys.core.hr.model.organization.BaseOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.security.SecurityHelper;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.service.AuthenticationServiceAware;
import com.nokor.frmk.vaadin.ui.panel.main.BaseMainUI;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.util.VaadinConfigHelper;
import com.nokor.frmk.vaadin.util.VaadinServicesHelper;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author prasnar
 */
public abstract class BaseLoginPanel extends VerticalLayout implements View, ClickListener, VaadinServicesHelper {
    /** */
    private static final long serialVersionUID = 4003544603794696831L;

    public static final String NAME = "login";

    public static final String MAIN_WIDTH = VaadinConfigHelper.getLoginPanelWidth();
    public static final String MAIN_HEIGHT = VaadinConfigHelper.getLoginPanelHeight();

    protected Authentication auth;
    protected Item selectedItem;

    private TextField txtUserName;
    private PasswordField txtPassword;
    private VerticalLayout profileListLayout;
    private HorizontalLayout headerLayout;
    private Table profileTable;
    private Button btnLogin;
    private SecUser secUser;

    @PostConstruct
    public void PostConstruct() {
        if (!VAADIN_SESSION_MNG.isAuthenticated()) {
            getMainPanel().setVisible(true);
            profileListLayout.setVisible(false);
        }

        txtUserName.focus();
        txtUserName.setValue("");
        txtPassword.setValue("");

        this.auth = null;
        this.selectedItem = null;
    }

    /**
     *
     */
    public BaseLoginPanel() {

    }

    protected abstract AbstractLayout getMainPanel();

    protected abstract void initView();

    /**
     * @return the multiProfiles
     */
    protected boolean isMultiProfiles() {
        return false;
    }

    /**
     * @return the secUser
     */
    protected SecUser getSecUser() {
        return secUser;
    }

    /**
     * @return the txtUserName
     */
    public TextField getTxtUserName() {
        return txtUserName;
    }

    /**
     * @param txtUserName the txtUserName to set
     */
    public void setTxtUserName(TextField txtUserName) {
        this.txtUserName = txtUserName;
    }

    /**
     * @return the txtPassword
     */
    public PasswordField getTxtPassword() {
        return txtPassword;
    }

    /**
     * @param txtPassword the txtPassword to set
     */
    public void setTxtPassword(PasswordField txtPassword) {
        this.txtPassword = txtPassword;
    }

    /**
     * @return the profileTable
     */
    protected Table getProfileTable() {
        return profileTable;
    }


    /**
     * @return the profileListLayout
     */
    protected VerticalLayout getProfileListLayout() {
        if (profileListLayout == null) {
            createProfileLayoutListView();
        }
        return profileListLayout;
    }

    /**
     * @return the headerLayout
     */
    protected HorizontalLayout getHeaderLayout() {
        if (headerLayout == null) {
            createHeaderLayout();
        }
        return headerLayout;
    }

    /**
     * @param headerLayout the headerLayout to set
     */
    protected void setHeaderLayout(HorizontalLayout headerLayout) {
        this.headerLayout = headerLayout;
    }

    /**
     * @return
     */
    protected void createProfileLayoutListView() {
        profileTable = new Table(I18N.message("select.user.profile"));
        profileTable.setSizeFull();
        profileTable.addContainerProperty("profileObj", SecProfile.class, null);
        profileTable.addContainerProperty("code", String.class, null);
        profileTable.addContainerProperty("name", String.class, null);
        profileTable.setColumnHeader("code", I18N.message("code"));
        profileTable.setColumnHeader("name", I18N.message("name.en"));
        profileTable.setColumnWidth("code", 175);
        profileTable.setColumnWidth("name", 300);
        profileTable.setVisibleColumns("code", "name");
        profileTable.setSelectable(true);
        profileTable.setMultiSelect(false);
        profileTable.addItemClickListener(new ItemClickListener() {
            private static final long serialVersionUID = -6676228064499031341L;

            @Override
            public void itemClick(ItemClickEvent event) {
                selectedItem = event.getItem();
                boolean isDoubleClick = event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet();
                if (isDoubleClick) {
                    onSelectedProfile(selectedItem);
                }
            }
        });

        final Button btnBack = new Button(I18N.message("back"), new ThemeResource("../nkr-default/icons/16/back.png"));
        final Button btnOk = new Button(I18N.message("ok"), new ThemeResource("../nkr-default/icons/16/tick.png"));

        ClickListener actionClickListener = new ClickListener() {

            /** */
            private static final long serialVersionUID = 1340275865735111768L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (event.getSource().equals(btnBack)) {
                    auth = null;
                    getMainPanel().setVisible(true);
                    profileListLayout.setVisible(false);
                } else if (event.getSource().equals(btnOk)) {
                    onSelectedProfile(selectedItem);
                }
            }
        };
        btnBack.addClickListener(actionClickListener);
        btnOk.addClickListener(actionClickListener);

        HorizontalLayout formFooterLayout = new HorizontalLayout();
        formFooterLayout.setHeight("40px");
        formFooterLayout.setWidth("170px");
        formFooterLayout.setStyleName("fields");
        formFooterLayout.setSpacing(true);
        formFooterLayout.addComponent(btnBack);
        formFooterLayout.addComponent(btnOk);
        formFooterLayout.setComponentAlignment(btnOk, Alignment.MIDDLE_RIGHT);

        profileListLayout = new VerticalLayout();
        profileListLayout.setVisible(false);
        profileListLayout.setStyleName("login-panel");
        profileListLayout.setSpacing(true);
        profileListLayout.setMargin(true);
        profileListLayout.setWidth(MAIN_WIDTH);
        profileListLayout.setHeight(MAIN_HEIGHT);
        profileListLayout.addComponent(profileTable);
        profileListLayout.addComponent(formFooterLayout);

        profileListLayout.setComponentAlignment(formFooterLayout, Alignment.BOTTOM_RIGHT);

        profileListLayout.setExpandRatio(profileTable, 1f);

    }

    /**
     * @return
     */
    protected void createHeaderLayout() {
        Image logo = new Image();
        logo.setStyleName("v-link-logo");
        BaseOrganization company = Organization.getMainOrganization();
        String logoPath = company.getLogoPath() == null ? "" : company.getLogoPath();
        if (logoPath != null) {
            logo.setSource(new ThemeResource(logoPath));
        }

        VerticalLayout space = new VerticalLayout();
        space.setWidth(1, Unit.PIXELS);

        // App name in multi-lines
        String lblCode = "lbl.welcome";
        String appName = I18N.message(new String[]{lblCode, lblCode + "." + AppConfigFileHelper.getApplicationCode().toLowerCase()});
        Label lblAppName = new Label(appName, ContentMode.HTML);

        headerLayout = new HorizontalLayout();
        headerLayout.setStyleName("labels");
        headerLayout.setMargin(true);
        headerLayout.setSpacing(true);
        headerLayout.addComponent(logo);
        headerLayout.addComponent(space);
        headerLayout.addComponent(lblAppName);
        headerLayout.setComponentAlignment(lblAppName, Alignment.MIDDLE_LEFT);

    }

    /**
     * @param profiles
     */
    protected void assignProfileValues(List<SecProfile> profiles) {
        if (profileTable != null) {
            profileTable.removeAllItems();
            SecApplication secApp = SecurityHelper.getSecApplication();

            for (SecProfile secProfile : profiles) {
                if (checkAppInProfile(secApp, secProfile)) {
                    Object newItemId = profileTable.addItem();
                    Item row = profileTable.getItem(newItemId);
                    row.getItemProperty("profileObj").setValue(secProfile);
                    row.getItemProperty("code").setValue(secProfile.getCode());
                    row.getItemProperty("name").setValue(secProfile.getDescEn());
                }
            }
            profileTable.setPageLength(profileTable.size());
        }
    }

    /**
     * @param secApp
     * @param secProfile
     * @return
     */
    private boolean checkAppInProfile(SecApplication secApp, SecProfile secProfile) {
        for (SecApplication app : secProfile.getApplications()) {
            if (app != null && app.getId().equals(secApp.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        String msg = "";
        if (getTxtUserName().getValue() == null || getTxtUserName().getValue().trim().equals("")) {
            msg += "login";
        }
        if (getTxtPassword().getValue() == null || getTxtPassword().getValue().trim().equals("")) {
            if (!msg.equals("")) {
                msg += " and ";
            }
            msg += "password";
        }


        if (StringUtils.isNotEmpty(msg)) {
            getTxtUserName().setValue("");
            getTxtPassword().focus();
            Notification.show("Please enter " + msg + "!");
            return;
        }

        try {
            secUser = getAuthenticationService().authenticate(getTxtUserName().getValue(), getTxtPassword().getValue());
            if (secUser == null) {
                throw new IllegalStateException("The user [" + getTxtUserName().getValue() + "] can not be authenticated.");
            }
        } catch (Exception e) {
            String errMsg = "Error while authenticating.";
            LoggerFactory.getLogger(this.getClass()).error(errMsg, e);

            if (e.getCause() != null && (e.getCause() instanceof InsufficientAuthenticationException)) {
                Notification.show("Access denied, this user can't access to this application!");
            } else {
                Notification.show("Access denied, Please check the user/password and try again!");
            }
            return;
        }

        try {
            if (isMultiProfiles() && secUser.hasMultiProfiles()) {
                manageMultiProfiles();
            } else {
                afterAuthentication();
            }
        } catch (Exception e) {
            String errMsg = "Error while initializing the UI.";
            LoggerFactory.getLogger(this.getClass()).error(errMsg, e);
            Notification.show(errMsg);
        }

    }

    /**
     * @return
     */
    protected AuthenticationServiceAware getAuthenticationService() {
        return AUTHENTICAT_SRV;
    }

    /**
     *
     */
    protected void afterAuthentication() {
        VAADIN_SESSION_MNG.getCurrent().start();
        gotoNextPanel((BaseMainUI) UI.getCurrent());
    }

    /**
     * Default behavior if the user has several profiles
     */
    protected void manageMultiProfiles() {
        getMainPanel().setVisible(false);
        profileListLayout.setVisible(true);

        assignProfileValues(secUser.getProfiles());

        auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(null);

        // If has only profile in the list, then auto-select the first one
        if (getProfileTable().size() == 1) {
            Object itemId = getProfileTable().getItemIds().iterator().next();
            selectedItem = getProfileTable().getItem(itemId);
            onSelectedProfile(selectedItem);
        }
        // Else Let select manually
    }

    /**
     * @param item
     */
    protected void onSelectedProfile(Item item) {
        if (item != null) {
            SecProfile currentProfile = (SecProfile) item.getItemProperty("profileObj").getValue();
            if (auth != null && currentProfile != null) {
                ((SecUser) auth.getPrincipal()).setDefaultProfile(currentProfile);
                SecurityContextHolder.getContext().setAuthentication(auth);
                afterAuthentication();
            }
        } else {
            MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
                    MessageBox.Icon.INFO, I18N.message("please.select.profile"),
                    new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
            mb.setWidth("250px");
            mb.setHeight("145px");
            mb.show();
        }
    }


    /**
     *
     */
    protected void gotoNextPanel(BaseMainUI mainUI) {
        if (VAADIN_SESSION_MNG.isAllowedActionMenu(mainUI.getAfterLoginPanelName())) {
            Page.getCurrent().setUriFragment("!" + mainUI.getAfterLoginPanelName());
        } else {
            Page.getCurrent().setUriFragment("!" + DefaultPanel.NAME);
        }
    }


    /**
     * @return the btnLogin
     */
    public Button getBtnLogin() {
        return btnLogin;
    }

    /**
     * @param btnLogin the btnLogin to set
     */
    public void setBtnLogin(Button btnLogin) {
        this.btnLogin = btnLogin;
    }

    /**
     * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
     */
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
