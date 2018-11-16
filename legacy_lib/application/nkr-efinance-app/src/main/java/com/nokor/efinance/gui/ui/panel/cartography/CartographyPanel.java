package com.nokor.efinance.gui.ui.panel.cartography;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinServletService;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.InfoWindowClosedListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author ly.youhort
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(CartographyPanel.NAME)
public class CartographyPanel extends VerticalLayout implements View {

	private static final long serialVersionUID = -2567643163312893276L;

	public static final String NAME = "cartography";

	@Autowired
	private QuotationService quotationService;
	
	private TabSheet tabSheet;
	private QuotationSearchPanel quotationSearchPanel;
	private GoogleMap googleMap;
	private List<MakerInfo> makerInfos;
	private GoogleMapInfoWindow infoWindow;
	private List<QuotationGroupByDealer> quotationGroupByDealers;
	private CartographyReportsPanel cartographyReportsPanel;
	
	
	@PostConstruct
	public void PostConstruct() {
		tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
		verticalLayout.setSpacing(true);		
		Panel searchPanel = new Panel(I18N.message("search"));
		quotationSearchPanel = new QuotationSearchPanel();
		searchPanel.setContent(quotationSearchPanel);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		
		NativeButton refreshButton = new NativeButton(I18N.message("refresh"));
		refreshButton.setIcon(new ThemeResource("../nkr-default/icons/16/refresh.png"));
		navigationPanel.addButton(refreshButton);
		
		NativeButton resethButton = new NativeButton(I18N.message("reset"));
		navigationPanel.addButton(resethButton);
		
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(searchPanel);
		verticalLayout.addComponent(tabSheet);
		verticalLayout.setExpandRatio(tabSheet, 1.0f);
		
		addComponent(verticalLayout);
		setExpandRatio(verticalLayout, 1f);

		VerticalLayout googleMapTab = new VerticalLayout();
		googleMapTab.setSizeFull();
		googleMapTab.setMargin(true);
		googleMapTab.setSpacing(true);
		googleMapTab.setCaption(I18N.message(NAME));
		
		String apiKey = "";
//		googleMap = new GoogleMap(new LatLon(11.5477700759027, 104.916729945117), 12.0, apiKey);
		googleMap = new GoogleMap(apiKey, null, "English");
		googleMap.setSizeFull();
//		googleMap.setMinZoom(12.0);
		googleMap.setHeight("600px");
		googleMap.setImmediate(true);
		googleMapTab.addComponent(googleMap);
		googleMapTab.setExpandRatio(googleMap, 1.0f);
				
		refreshButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 30483242026642875L;

			@Override
             public void buttonClick(ClickEvent event) {
				refreshMap();
             }
         });
		
		resethButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 30483242026642875L;
			@Override
             public void buttonClick(ClickEvent event) {
				quotationSearchPanel.reset();
             }
         });
		
		cartographyReportsPanel = new CartographyReportsPanel();
		
		tabSheet.addTab(googleMapTab, I18N.message("view.by.map"));
		tabSheet.addTab(cartographyReportsPanel, I18N.message("view.by.list"));
	}

	/**
	 * @param request
	 * @return
	 */
    public static String getBaseUrl(HttpServletRequest request) {
        if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
            return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
        else
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

	/**
	 * 
	 */
	private void refreshMap() {
		
        String markerIcon = getBaseUrl(VaadinServletService.getCurrentServletRequest()) + "/VAADIN/themes/efinance/icons/32/";
		BaseRestrictions<Quotation> restrictions = quotationSearchPanel.getRestrictions();
		List<Quotation> quotations = quotationService.list(restrictions);
		
		googleMap.clearMarkers();
		makerInfos = new ArrayList<MakerInfo>();
		quotationGroupByDealers = new ArrayList<QuotationGroupByDealer>();
		
		if (quotations != null && !quotations.isEmpty()) {
			int nbActivate = 0;
			int nbReject = 0;
			int nbDecline = 0; 	
			int nbProposal = 0;
			for (int i = 0; i < quotations.size(); i++) {
				Quotation quotation = quotations.get(i);
				Dealer dealer = quotation.getDealer();
				if (dealer != null && dealer.getDealerAddresses() != null) {
					Address dealerAddress = dealer.getDealerAddresses().get(0).getAddress();
					if (dealerAddress.getLatitude() != null
							&& dealerAddress.getLongitude() != null) {			
						if (quotation.getWkfStatus().equals(QuotationWkfStatus.ACT)) {
							nbActivate++;
						} else if (quotation.getWkfStatus().equals(QuotationWkfStatus.REJ)) {
							nbReject++;
						} else if (quotation.getWkfStatus().equals(QuotationWkfStatus.DEC)) {
							nbDecline++;
						} else {
							nbProposal++;
						}
						
						
						if (i == quotations.size() - 1 || quotations.get(i).getDealer() != quotations.get(i + 1).getDealer()) {
		                    int nbTotal = nbActivate + nbReject + nbDecline + nbProposal;
		                    
		                    GoogleMapMarker googleMapMarker = new GoogleMapMarker();
		                    
		                    googleMapMarker.setCaption(dealer.getNameEn());
							googleMapMarker.setPosition(new LatLon(dealerAddress.getLatitude(), dealerAddress.getLongitude()));

		                    if (nbReject <= (nbTotal * 0.1)) {
		                    	googleMapMarker.setIconUrl(markerIcon + "marker_green.png");
		                    } else if (nbReject <= (nbTotal * 0.6)) {
		                    	googleMapMarker.setIconUrl(markerIcon + "marker_orange.png");
		                    } else {
		                    	googleMapMarker.setIconUrl(markerIcon + "marker_red.png");
		                    }
		                	googleMap.addMarker(googleMapMarker);
		                	
		                	MakerInfo makerInfo = new MakerInfo();
		                    String strDescription = "<b>" + dealer.getNameEn() + "</b>" +
		                    		"<ul><li>" + I18N.message("approved") + ":" + String.valueOf(nbActivate)+"</li>"+
		                    		"<li>" + I18N.message("rejected") + ":" + String.valueOf(nbReject)+"</li>" +
		                    		"<li>" + I18N.message("declined") + ":" + String.valueOf(nbDecline)+"</li>" +
		                    		"<li>" + I18N.message("proposal") + ":" + String.valueOf(nbProposal)+"</li>" +
		                    		"<li>" + I18N.message("total") + ":" + String.valueOf(nbTotal)+"</li></ul>";
		                	makerInfo.setDesc(strDescription);
		                	makerInfo.setLatLon(googleMapMarker.getPosition());
		                	makerInfos.add(makerInfo);
		                	
		                	QuotationGroupByDealer quotationGroupByDealer = new QuotationGroupByDealer();
		                	quotationGroupByDealer.setDealer(dealer);
		                	quotationGroupByDealer.setNumApproved(nbActivate);
		                	quotationGroupByDealer.setNumRejected(nbReject);
		                	quotationGroupByDealer.setNumDeclined(nbDecline);
		                	quotationGroupByDealer.setNumProposal(nbProposal);
		                	quotationGroupByDealer.setNumTotal(nbTotal);
		                	quotationGroupByDealers.add(quotationGroupByDealer);
		    				nbActivate = 0;
							nbReject = 0;
							nbDecline = 0; 	
							nbProposal = 0;
						}
					}
				}
				
			}
			
		 	Panel console = new Panel();
            console.setHeight("100px");
            final CssLayout consoleLayout = new CssLayout();
            consoleLayout.setCaption("");
            console.setContent(consoleLayout);

			 googleMap.addMarkerClickListener(new MarkerClickListener() {
				private static final long serialVersionUID = -4854865518188426630L;
				@Override
                 public void markerClicked(GoogleMapMarker clickedMarker) {
					if (infoWindow != null) {
						googleMap.closeInfoWindow(infoWindow);	
					}
               	  infoWindow = new GoogleMapInfoWindow();
               	  infoWindow.setWidth("200px");
               	  infoWindow.setHeight("200px");
               	  infoWindow.setContent(MakerInfo.getDesc(makerInfos, clickedMarker.getPosition()));
               	  infoWindow.setPosition(clickedMarker.getPosition());
               	  googleMap.openInfoWindow(infoWindow);
                 }
             });
             
             googleMap.addInfoWindowClosedListener(new InfoWindowClosedListener() {
				private static final long serialVersionUID = -3640450177590278746L;
				@Override
                 public void infoWindowClosed(GoogleMapInfoWindow window) {
                     Label consoleEntry = new Label("InfoWindow \""
                             + window.getContent() + "\" closed");
                     consoleLayout.addComponent(consoleEntry, 0);
                 }
             });
		}
		
		cartographyReportsPanel.setIndexedContainer(quotationGroupByDealers);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		refreshMap();
	}
}
