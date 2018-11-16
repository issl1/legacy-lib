package com.nokor.efinance.gui.ui.panel.collection;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author buntha.chea
 */
public class CollectionInboxPanel extends VerticalLayout implements QuotationEntityField, ClickListener{
	
	private static final long serialVersionUID = -2983055467054135680L;
	
	private CollectionInboxSummaryPanel collectionInboxSummaryPanel;
	private CollectionInboxSecondSummaryPanel collectionInboxSecondSummaryPanel;
	private CollectionInboxQuotationsPanel collectionInboxQuotationsPanel;
	private ERefDataComboBox<ETypeContactInfo> cbxPrint;
	private Button btnPrint;
	
	public CollectionInboxPanel() {
		setCaption(I18N.message("inbox"));
		setSizeFull();
		setHeight("100%");
		setMargin(true);
		setSpacing(true);
		
		collectionInboxSummaryPanel = new CollectionInboxSummaryPanel();
		collectionInboxSecondSummaryPanel = new CollectionInboxSecondSummaryPanel();
		collectionInboxQuotationsPanel = new CollectionInboxQuotationsPanel();
		btnPrint = new Button(I18N.message("print"), this);
		btnPrint.setIcon(new ThemeResource("../nkr-default/icons/16/printer.png"));
		cbxPrint = new ERefDataComboBox<>(ETypeContactInfo.class);
		
		addComponent(collectionInboxSummaryPanel);
		addComponent(collectionInboxSecondSummaryPanel);
		addComponent(collectionInboxQuotationsPanel);
		SecUser secUser = UserSessionManager.getCurrentUser();
		if (ProfileUtil.isColBill(secUser)) {
			addComponent(btnPrint);
		}
		addComponent(getResultColors());
	}
	
	/**
	 * 
	 */
	public void refresh() {
		collectionInboxQuotationsPanel.refresh();
	}
	/**
	 * @return
	 */
	public CollectionInboxQuotationsPanel getCollectionInboxQuotationsPanel() {
		return collectionInboxQuotationsPanel;
	}
	
	/**
	 * for note color of each result in GLF
	 */
	public HorizontalLayout getResultColors(){
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setMargin(true);
		Label result1 = new Label(I18N.message("result.1"));
		Label result1Color = new Label("");
		result1Color.setStyleName("v-label-results-green");
		
		Label result2 = new Label(I18N.message("result.2"));
		Label result2Color = new Label("");
		result2Color.setStyleName("v-label-results-yellow");
		
		Label result3 = new Label(I18N.message("result.3"));
		Label result3Color = new Label("");
		result3Color.setStyleName("v-label-results-purple");
		
		Label result4 = new Label(I18N.message("result.4"));
		Label result4Color = new Label("");
		result4Color.setStyleName("v-label-results-bright-blue");
		
		Label result5 = new Label(I18N.message("result.5"));
		Label result5Color = new Label("");
		result5Color.setStyleName("v-label-results-bright-green");
		
		Label result6 = new Label(I18N.message("result.6"));
		Label result6Color = new Label("");
		result6Color.setStyleName("v-label-results-blue");
		
		horizontalLayout.addComponent(result1Color);
		horizontalLayout.addComponent(result1);
		horizontalLayout.addComponent(result2Color);
		horizontalLayout.addComponent(result2);
		horizontalLayout.addComponent(result3Color);
		horizontalLayout.addComponent(result3);
		horizontalLayout.addComponent(result4Color);
		horizontalLayout.addComponent(result4);
		horizontalLayout.addComponent(result5Color);
		horizontalLayout.addComponent(result5);
		horizontalLayout.addComponent(result6Color);
		horizontalLayout.addComponent(result6);
		return horizontalLayout;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		final Window window = new Window();
		window.setModal(true);
		window.setClosable(true);
		window.setResizable(false);		
	    window.setWidth(400, Unit.PIXELS);
	    window.setHeight(200, Unit.PIXELS);
	    window.setCaption(I18N.message("print"));
	    
	    Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -7366871133055251114L;

			public void buttonClick(ClickEvent event) {
				window.close();
            }
        });
	    btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		Button btnPrint = new NativeButton(I18N.message("print"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 4754734115751053990L;

			public void buttonClick(ClickEvent event) {
				window.close();
			}
        });
		btnPrint.setIcon(new ThemeResource("../nkr-default/icons/16/printer.png"));
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnPrint);
		navigationPanel.addButton(btnCancel);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(cbxPrint);
		verticalLayout.setComponentAlignment(cbxPrint, Alignment.MIDDLE_CENTER);
		window.setContent(verticalLayout);
		UI.getCurrent().addWindow(window);
	}
	
}
