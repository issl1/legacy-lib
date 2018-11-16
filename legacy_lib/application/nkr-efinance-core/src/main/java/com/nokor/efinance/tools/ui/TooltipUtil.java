package com.nokor.efinance.tools.ui;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.service.AddressService;
import com.nokor.efinance.third.creditbureau.cbc.model.response.Employer;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.common.app.eref.ECountry;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

public final class TooltipUtil {
	
	/**
	 * Generate tooltip
	 * @param values
	 * @return
	 */
	public static String getToolTip(List<String> values) {		
		String tooltip = "";		
		if (values != null && !values.isEmpty()) {
			tooltip = "<h3><img src=\"VAADIN/themes/nkr-default/icons/16/twitter.png\"/>" + I18N.message("error.validation") + "</h3>";
			tooltip += "<ul>";
			for (String value : values) {
				tooltip += "<li>" + value + "</li>";
			}
			tooltip += "</ul>";
		}
		return tooltip;
	}
	
	/**
	 * @param address
	 * @param cbAddress
	 * @return
	 */
	public static String getToolTip(Address address, List<com.nokor.efinance.third.creditbureau.cbc.model.response.Address> cbAddresses) {
		AddressService addressService = (AddressService) SecApplicationContextHolder.getContext().getBean("addressService");
		String tooltip = "";
		tooltip += "<b>From Applicant</b></br>";
		tooltip += "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"3\" style=\"border:1px solid black;\">";
		tooltip += "<tr>";
		tooltip += "<th>" + I18N.message("house.no") + "</th>";
		tooltip += "<th>" + I18N.message("street") + "</th>";
		tooltip += "<th>" + I18N.message("province") + "</th>";
		tooltip += "<th>" + I18N.message("district") + "</th>";
		tooltip += "<th>" + I18N.message("commune") + "</th>";
		tooltip += "<th>" + I18N.message("village") + "</th>";
		tooltip += "</tr>";
		tooltip += "<tr>";
		tooltip += "<td>" + address.getHouseNo() + "</td>";
		tooltip += "<td>" + address.getStreet() + "</td>";
		tooltip += "<td>" + address.getProvince().getDescEn() + "</td>";
		tooltip += "<td>" + address.getDistrict().getDescEn() + "</td>";
		tooltip += "<td>" + address.getCommune().getDescEn() + "</td>";
		tooltip += "<td>" + address.getVillage().getDescEn() + "</td>";
		tooltip += "</tr>";
		tooltip += "</table>";
		tooltip += "<br/>";
		if (cbAddresses != null && !cbAddresses.isEmpty()) {
			tooltip += "<b>From Credit Bureau</b></br>";
			tooltip += "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"3\" style=\"border:1px solid black;\">";
			tooltip += "<tr>";
			tooltip += "<th>" + I18N.message("house.no") + "/" + I18N.message("street") + "</th>";
			tooltip += "<th>" + I18N.message("province") + "</th>";
			tooltip += "<th>" + I18N.message("district") + "</th>";
			tooltip += "<th>" + I18N.message("commune") + "</th>";
			tooltip += "<th>" + I18N.message("village") + "</th>";
			tooltip += "</tr>";
			
			for (int i = 1; i < cbAddresses.size(); i++) {
				com.nokor.efinance.third.creditbureau.cbc.model.response.Address cbAddress = cbAddresses.get(i); 
				
				Province province = addressService.getProvineByCode(ECountry.KHM, cbAddress.getCaprov());
				District district = null;
				Commune commune = null;
				Village village = null;
				if (province != null && StringUtils.isNotEmpty(cbAddress.getCadist())) {
					district = addressService.getDistrictByCode(province, cbAddress.getCadist());
				}							
				if (district != null && StringUtils.isNotEmpty(cbAddress.getCacomm())) {
					commune = addressService.getCommuneByCode(district, cbAddress.getCacomm());
				}							
				if (commune != null && StringUtils.isNotEmpty(cbAddress.getCavill())) {
					village = addressService.getVillageByCode(commune, cbAddress.getCavill());
				}
				
				tooltip += "<tr>";
				tooltip += "<td>" + cbAddress.getCacad1e() + "</td>";
				tooltip += "<td>" + (province != null ? province.getDescEn() : "") + "</td>";
				tooltip += "<td>" + (district != null ? district.getDescEn() : "") + "</td>";
				tooltip += "<td>" + (commune != null ? commune.getDescEn() : "") + "</td>";
				tooltip += "<td>" + (village != null ? village.getDescEn() : "") + "</td>";
				tooltip += "</tr>";
			}
			tooltip += "</table>";
		}
		
		return tooltip;
	}
	
	/**
	 * @param address
	 * @param cbAddress
	 * @return
	 */
	public static String getToolTip(Employment employment, List<com.nokor.efinance.third.creditbureau.cbc.model.response.Employer> cbEmployers) {		
		AddressService addressService = (AddressService) SecApplicationContextHolder.getContext().getBean("addressService");		
		Address address = employment.getAddress();		
		String tooltip = "";
		tooltip += "<b>From Applicant</b></br>";
		tooltip += "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"3\" style=\"border:1px solid black;\">";
		tooltip += "<tr>";
		tooltip += "<th>" + I18N.message("employer") + "</th>";
		tooltip += "<th>" + I18N.message("house.no") + "</th>";
		tooltip += "<th>" + I18N.message("street") + "</th>";
		tooltip += "<th>" + I18N.message("province") + "</th>";
		tooltip += "<th>" + I18N.message("district") + "</th>";
		tooltip += "<th>" + I18N.message("commune") + "</th>";
		tooltip += "<th>" + I18N.message("village") + "</th>";
		tooltip += "</tr>";
		tooltip += "<tr>";
		tooltip += "<td>" + employment.getEmployerName() + "</td>";
		tooltip += "<td>" + address.getHouseNo() + "</td>";
		tooltip += "<td>" + address.getStreet() + "</td>";
		tooltip += "<td>" + address.getProvince().getDescEn() + "</td>";
		tooltip += "<td>" + address.getDistrict().getDescEn() + "</td>";
		tooltip += "<td>" + address.getCommune().getDescEn() + "</td>";
		tooltip += "<td>" + address.getVillage().getDescEn() + "</td>";
		tooltip += "</tr>";
		tooltip += "</table>";
		tooltip += "<br/>";
		if (cbEmployers != null && !cbEmployers.isEmpty()) {
			tooltip += "<b>From Credit Bureau</b></br>";
			tooltip += "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"3\" style=\"border:1px solid black;\">";
			tooltip += "<tr>";
			tooltip += "<th>" + I18N.message("employer") + "</th>";
			tooltip += "<th>" + I18N.message("house.no") + "/" + I18N.message("street") + "</th>";
			tooltip += "<th>" + I18N.message("province") + "</th>";
			tooltip += "<th>" + I18N.message("district") + "</th>";
			tooltip += "<th>" + I18N.message("commune") + "</th>";
			tooltip += "<th>" + I18N.message("village") + "</th>";
			tooltip += "</tr>";
			for (int i = 1; i < cbEmployers.size(); i++) {
				Employer cbEmployer = cbEmployers.get(i);
				if (cbEmployer.getEadrs() != null && !cbEmployer.getEadrs().isEmpty()) {
					com.nokor.efinance.third.creditbureau.cbc.model.response.Eadr cbEmployerAddress = cbEmployer.getEadrs().get(0);
					
					Province province = addressService.getProvineByCode(ECountry.KHM, cbEmployerAddress.getEaprov());
					District district = null;
					Commune commune = null;
					Village village = null;
					if (province != null && StringUtils.isNotEmpty(cbEmployerAddress.getEadist())) {
						district = addressService.getDistrictByCode(province, cbEmployerAddress.getEadist());
					}							
					if (district != null && StringUtils.isNotEmpty(cbEmployerAddress.getEacomm())) {
						commune = addressService.getCommuneByCode(district, cbEmployerAddress.getEacomm());
					}							
					if (commune != null && StringUtils.isNotEmpty(cbEmployerAddress.getEavill())) {
						village = addressService.getVillageByCode(commune, cbEmployerAddress.getEavill());
					}
					
					tooltip += "<tr>";
					tooltip += "<td>" + cbEmployer.getEnme() + "</td>";
					tooltip += "<td>" + cbEmployerAddress.getEaad1e() + "</td>";
					tooltip += "<td>" + (province != null ? province.getDescEn() : "") + "</td>";
					tooltip += "<td>" + (district != null ? district.getDescEn() : "") + "</td>";
					tooltip += "<td>" + (commune != null ? commune.getDescEn() : "") + "</td>";
					tooltip += "<td>" + (village != null ? village.getDescEn() : "") + "</td>";
					tooltip += "</tr>";
				}
			}
			tooltip += "</table>";
		}		
		
		return tooltip;
	}
}
