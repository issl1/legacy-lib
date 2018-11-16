package com.nokor.frmk.vaadin.ui;

import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

import com.nokor.frmk.vaadin.ui.panel.main.ViewType;
import com.nokor.frmk.vaadin.ui.panel.main.event.BrowserResizeEvent;
import com.nokor.frmk.vaadin.ui.panel.main.event.CloseOpenWindowsEvent;
import com.nokor.frmk.vaadin.ui.panel.main.event.MainUIEventBus;
import com.nokor.frmk.vaadin.ui.panel.main.event.PostViewChangeEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author prasnar
 *
 */
public class PageNavigator extends Navigator {
    /** */
	private static final long serialVersionUID = -4914598044287671772L;
	// Provide a Google Analytics tracker id here
    private static final String TRACKER_ID = null;// "UA-658457-6";
    private GoogleAnalyticsTracker tracker;

    private static final ViewType ERROR_VIEW = ViewType.DASHBOARD;
    private ViewProvider errorViewProvider;

    /**
     * 
     * @param container
     */
    public PageNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);

        if (TRACKER_ID != null) {
            initGATracker(TRACKER_ID);
        }
        initViewChangeListener();
        initViewProviders();

    }

    /**
     * 
     * @param trackerId
     */
    private void initGATracker(final String trackerId) {
        tracker = new GoogleAnalyticsTracker(trackerId, "none");

        // GoogleAnalyticsTracker is an extension add-on for UI so it is
        // initialized by calling .extend(UI)
        tracker.extend(UI.getCurrent());
    }

    /**
     * 
     */
    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            /**
			 * 
			 */
			private static final long serialVersionUID = 7581088038806561822L;

			@Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                // Since there's no conditions in switching between the views
                // we can always return true.
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                ViewType view = ViewType.getByViewName(event
                        .getViewName());
                // Appropriate events get fired after the view is changed.
                MainUIEventBus.post(new PostViewChangeEvent(view));
                MainUIEventBus.post(new BrowserResizeEvent());
                MainUIEventBus.post(new CloseOpenWindowsEvent());

                if (tracker != null) {
                    // The view change is submitted as a pageview for GA tracker
                    tracker.trackPageview("/dashboard/" + event.getViewName());
                }
            }
        });
    }

    /**
     * 
     */
    private void initViewProviders() {
        // A dedicated view provider is added for each separate view type
        for (final ViewType viewType : ViewType.values()) {
            ViewProvider viewProvider = new ClassBasedViewProvider(
                    viewType.getViewName(), viewType.getViewClass()) {

                /** */
				private static final long serialVersionUID = 6733897672217199364L;
				
				// This field caches an already initialized view instance if the
                // view should be cached (stateful views).
                private View cachedInstance;

                @Override
                public View getView(final String viewName) {
                    View result = null;
                    if (viewType.getViewName().equals(viewName)) {
                        if (viewType.isStateful()) {
                            // Stateful views get lazily instantiated
                            if (cachedInstance == null) {
                                cachedInstance = super.getView(viewType
                                        .getViewName());
                            }
                            result = cachedInstance;
                        } else {
                            // Non-stateful views get instantiated every time
                            // they're navigated to
                            result = super.getView(viewType.getViewName());
                        }
                    }
                    return result;
                }
            };

            if (viewType == ERROR_VIEW) {
                errorViewProvider = viewProvider;
            }

            addProvider(viewProvider);
        }

        setErrorProvider(new ViewProvider() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 8124823041096578134L;

			@Override
            public String getViewName(final String viewAndParameters) {
                return ERROR_VIEW.getViewName();
            }

            @Override
            public View getView(final String viewName) {
                return errorViewProvider.getView(ERROR_VIEW.getViewName());
            }
        });
    }
}
