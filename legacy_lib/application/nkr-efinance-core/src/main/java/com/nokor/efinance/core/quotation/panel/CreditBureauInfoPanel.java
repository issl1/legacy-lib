package com.nokor.efinance.core.quotation.panel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jibx.runtime.JiBXException;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.service.AddressService;
import com.nokor.efinance.core.quotation.model.QuotationExtModule;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.third.creditbureau.cbc.XmlBinder;
import com.nokor.efinance.third.creditbureau.cbc.model.ProductStatus;
import com.nokor.efinance.third.creditbureau.cbc.model.ProductType;
import com.nokor.efinance.third.creditbureau.cbc.model.response.AccDetail;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Address;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Consumer;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Eadr;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Employer;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Response;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.common.app.eref.ECountry;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Credit bureau panel
 * @author ly.youhort
 */
public class CreditBureauInfoPanel extends VerticalLayout implements QuotationEntityField {

	private static final long serialVersionUID = 754341755217081254L;
	
	private AddressService addressService = (AddressService) SecApplicationContextHolder.getContext().getBean("addressService");
	
	private VerticalLayout creditBureauLayout;
	
	public CreditBureauInfoPanel() {
		super();
		setSizeFull();	
		creditBureauLayout = new VerticalLayout();
		creditBureauLayout.setSizeFull();
		creditBureauLayout.setMargin(true);
		creditBureauLayout.setSpacing(true);
		addComponent(creditBureauLayout);		
	}
	
	/**
	 * Set quotation
	 * @param quotation
	 */
	public void assignValues(QuotationExtModule quotationExtModule) {		
		creditBureauLayout.removeAllComponents();		
		if (quotationExtModule != null) {			
			try {
				if (StringUtils.isNotEmpty(quotationExtModule.getResult())) {
					Response response = XmlBinder.unmarshal(quotationExtModule.getResult());
					Consumer consumer = response.getMessage().getItems().get(0).getRspReport().getConsumer();
					List<AccDetail> accDetails = consumer.getAccDetails();
					if (accDetails != null) {
						
						String cbLiabilitiesTable = "<table cellspacing=\"0\" cellpadding=\"5\" style=\"border-collapse:collapse;\">";
						cbLiabilitiesTable += "<tr>";
						cbLiabilitiesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("product.type") + "</b></th>";
						cbLiabilitiesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("limit") + "</b></th>";
						cbLiabilitiesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("averaged.monthly.installment") + "</b></th>";
						cbLiabilitiesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("closing.date") + "</b></th>";
						cbLiabilitiesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("status") + "</b></th>";
						cbLiabilitiesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("No of Previous Default last 24 cycle") + "</b></th>";
						cbLiabilitiesTable += "</tr>";
						
						Map<String, Double> totalInstallmentMap = new LinkedHashMap<String, Double>();
												
						for (AccDetail accDetail : accDetails) {
							
							Double totalInstallment = 0.0;
							if (totalInstallmentMap.containsKey(accDetail.getAcccurr())) {
								totalInstallment = accDetail.getAccinstl() + totalInstallmentMap.get(accDetail.getAcccurr());
							} else {
								totalInstallment = accDetail.getAccinstl();
							}
							totalInstallmentMap.put(accDetail.getAcccurr(), totalInstallment) ;
														
							cbLiabilitiesTable += "<tr>";
							cbLiabilitiesTable += "<td style=\"border:1px solid black;\">" + ProductType.valueOf(accDetail.getAccprd()).getDesc() + "</td>";
							cbLiabilitiesTable += "<td style=\"border:1px solid black; text-align:right;\">" + accDetail.getAcclimit() + accDetail.getAcccurr() + "</td>";
							cbLiabilitiesTable += "<td style=\"border:1px solid black; text-align:right;\">" + accDetail.getAccinstl() + accDetail.getAcccurr() + "</td>";
							cbLiabilitiesTable += "<td style=\"border:1px solid black;\">" + accDetail.getAccclsddt() + "</td>";
							cbLiabilitiesTable += "<td style=\"border:1px solid black;\">" + ProductStatus.valueOf(accDetail.getAccstatus()).getDesc() + "</td>";
							cbLiabilitiesTable += "<td style=\"border:1px solid black;\">" + accDetail.getAccsummry() + "</td>";
							cbLiabilitiesTable += "</tr>";
						}
						
						for (Iterator<String> iter = totalInstallmentMap.keySet().iterator(); iter.hasNext();) {
							String currency = iter.next();
							Double totalInstallment = totalInstallmentMap.get(currency);
							cbLiabilitiesTable += "<tr>";
							cbLiabilitiesTable += "<td>" + "&nbsp;" + "</td>";
							cbLiabilitiesTable += "<td style=\"border:1px solid black; text-align:right;\">" + "<b>Total (" + currency + ")</b></td>";
							cbLiabilitiesTable += "<td style=\"border:1px solid black; text-align:right;\"><b>" + totalInstallment + currency + "</b></td>";
							cbLiabilitiesTable += "<td>" + "&nbsp;" + "</td>";
							cbLiabilitiesTable += "<td>" + "&nbsp;" + "</td>";
							cbLiabilitiesTable += "<td>" + "&nbsp;" + "</td>";
							cbLiabilitiesTable += "</tr>";
						}
						
						cbLiabilitiesTable += "</table>";
						
						try {
							CustomLayout customLayout = new CustomLayout(new ByteArrayInputStream(cbLiabilitiesTable.getBytes()));
							customLayout.setSizeFull();
							Panel liabilitiesPanel = new Panel(I18N.message("liabilities.information"));
							VerticalLayout liabilitiesLayout = new VerticalLayout();
							liabilitiesLayout.setSizeFull();
							liabilitiesLayout.setMargin(true);
							liabilitiesLayout.addComponent(customLayout);
							liabilitiesPanel.setContent(liabilitiesLayout);
							creditBureauLayout.addComponent(liabilitiesPanel);
						} catch (IOException e) {
							Notification.show(e.toString());
						}
					}
					
					List<Address> addresses = consumer.getAddresses();
					if (addresses != null) {
						String cbAddressesTable = "<table cellspacing=\"0\" cellpadding=\"5\" style=\"border-collapse:collapse;\">";
						cbAddressesTable += "<tr>";
						cbAddressesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("type") + "</b></th>";
						cbAddressesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("address") + "</b></th>";
						cbAddressesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("village") + "</b></th>";
						cbAddressesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("commune") + "</b></th>";
						cbAddressesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("district") + "</b></th>";
						cbAddressesTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("province") + "</b></th>";
						cbAddressesTable += "</tr>";
						
						for (int i = 0; i < addresses.size(); i++) {
							
							Address address = addresses.get(i);
							
							if (StringUtils.isNotEmpty(address.getCaprov())) {
								Province province = addressService.getProvineByCode(ECountry.KHM, address.getCaprov());
								District district = null;
								Commune commune = null;
								Village village = null;
								if (province != null && StringUtils.isNotEmpty(address.getCadist())) {
									district = addressService.getDistrictByCode(province, address.getCadist());
								}							
								if (district != null && StringUtils.isNotEmpty(address.getCacomm())) {
									commune = addressService.getCommuneByCode(district, address.getCacomm());
								}							
								if (commune != null && StringUtils.isNotEmpty(address.getCavill())) {
									village = addressService.getVillageByCode(commune, address.getCavill());
								}
								
								cbAddressesTable += "<tr>";
								cbAddressesTable += "<td style=\"border:1px solid black;\">" + address.getCacadt() + "</td>";
								cbAddressesTable += "<td style=\"border:1px solid black;\">" + address.getCacad1e() + "</td>";
								cbAddressesTable += "<td style=\"border:1px solid black;\">" + (village != null ? village.getDescEn() : "") + "</td>";
								cbAddressesTable += "<td style=\"border:1px solid black;\">" + (commune != null ? commune.getDescEn() : "") + "</td>";
								cbAddressesTable += "<td style=\"border:1px solid black;\">" + (district != null ? district.getDescEn() : "") + "</td>";
								cbAddressesTable += "<td style=\"border:1px solid black;\">" + (province != null ? province.getDescEn() : "") + "</td>";
								cbAddressesTable += "</tr>";
							}
						}
						
						try {
							CustomLayout customLayout = new CustomLayout(new ByteArrayInputStream(cbAddressesTable.getBytes()));
							customLayout.setSizeFull();
							Panel addressesPanel = new Panel(I18N.message("address.information"));
							VerticalLayout addressesLayout = new VerticalLayout();
							addressesLayout.setSizeFull();
							addressesLayout.setMargin(true);
							addressesLayout.addComponent(customLayout);
							addressesPanel.setContent(addressesLayout);
							creditBureauLayout.addComponent(addressesPanel);
							
						} catch (IOException e) {
							Notification.show(e.toString());
						}
					}
					
					List<Employer> employers = consumer.getEmployers();
					if (employers != null) {
						String cbEmployersTable = "<table cellspacing=\"0\" cellpadding=\"5\" style=\"border-collapse:collapse;\">";
						cbEmployersTable += "<tr>";
						cbEmployersTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("type") + "</b></th>";
						cbEmployersTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("employer") + "</b></th>";
						cbEmployersTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("length.of.service") + "</b></th>";
						cbEmployersTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("salary") + "</b></th>";
						cbEmployersTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("address") + "</b></th>";
						cbEmployersTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("village") + "</b></th>";
						cbEmployersTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("commune") + "</b></th>";
						cbEmployersTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("district") + "</b></th>";
						cbEmployersTable += "<th style=\"border:1px solid black;\"><b>" + I18N.message("province") + "</b></th>";
						cbEmployersTable += "</tr>";
						
						for (int i = 0; i < employers.size(); i++) {
							
							Employer employer = employers.get(i);
							
							List<Eadr> eadrs = employer.getEadrs();
							Eadr eadr = null;
							Province province = null;
							District district = null;
							Commune commune = null;
							Village village = null;
							if (eadrs != null && !eadrs.isEmpty()) {
								eadr = eadrs.get(0);
								
								province = addressService.getProvineByCode(ECountry.KHM, eadr.getEaprov());
								if (province != null && StringUtils.isNotEmpty(eadr.getEadist())) {
									district = addressService.getDistrictByCode(province, eadr.getEadist());
								}							
								if (district != null && StringUtils.isNotEmpty(eadr.getEacomm())) {
									commune = addressService.getCommuneByCode(district, eadr.getEacomm());
								}							
								if (commune != null && StringUtils.isNotEmpty(eadr.getEavill())) {
									village = addressService.getVillageByCode(commune, eadr.getEavill());
								}
							}
							
							cbEmployersTable += "<tr>";
							cbEmployersTable += "<td style=\"border:1px solid black;\">" + employer.getEtyp() + "</td>";
							cbEmployersTable += "<td style=\"border:1px solid black;\">" + employer.getEnme() + "</td>";
							cbEmployersTable += "<td style=\"border:1px solid black;\">" + employer.getElen() + "</td>";
							cbEmployersTable += "<td style=\"border:1px solid black; text-align:right;\">" + employer.getEtms() + employer.getEcurr() + "</td>";
							cbEmployersTable += "<td style=\"border:1px solid black;\">" + (eadr != null ? eadr.getEaad1e() : "") + "</td>";
							cbEmployersTable += "<td style=\"border:1px solid black;\">" + (village != null ? village.getDescEn() : "") + "</td>";
							cbEmployersTable += "<td style=\"border:1px solid black;\">" + (commune != null ? commune.getDescEn() : "") + "</td>";
							cbEmployersTable += "<td style=\"border:1px solid black;\">" + (district != null ? district.getDescEn() : "") + "</td>";
							cbEmployersTable += "<td style=\"border:1px solid black;\">" + (province != null ? province.getDescEn() : "") + "</td>";
							cbEmployersTable += "</tr>";
						}
						
						cbEmployersTable += "</table>";
						try {
							CustomLayout customLayout = new CustomLayout(new ByteArrayInputStream(cbEmployersTable.getBytes()));
							customLayout.setSizeFull();
							Panel employersPanel = new Panel(I18N.message("employer.information"));
							VerticalLayout employersLayout = new VerticalLayout();
							employersLayout.setSizeFull();
							employersLayout.setMargin(true);
							employersLayout.addComponent(customLayout);
							employersPanel.setContent(employersLayout);
							creditBureauLayout.addComponent(employersPanel);
						} catch (IOException e) {
							Notification.show(e.toString());
						}
					}
				
				}
			} catch (JiBXException e) {
				Notification.show(e.toString());
			}
		}
	}
}
