package com.nokor.efinance.ra.ui.panel.referential.address;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.applicant.service.AddressService;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.ersys.core.hr.model.address.Village;
import com.nokor.common.app.eref.ECountry;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(UploadPanel.NAME)
public class UploadPanel extends Panel implements View {
	
	private static final long serialVersionUID = -2149707107396177780L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String NAME = "address.upload";
	
	@Autowired
	private AddressService addressService;

	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("upload.province.district"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		Upload uploadAddress = new Upload();
		uploadAddress.setButtonCaption(I18N.message("upload"));
		final FileUploader uploader = new FileUploader(); 
		uploadAddress.setReceiver(uploader);
		uploadAddress.addSucceededListener(uploader);
		verticalLayout.addComponent(uploadAddress);
		
		Button btnAddUnknownAddress = new Button("Add Unkown Address Reference");
		btnAddUnknownAddress.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4334791215639244956L;

			@Override
			public void buttonClick(ClickEvent event) {
				addressService.addUnknownAddressesReference();
			}
		});
		
		verticalLayout.addComponent(btnAddUnknownAddress);
		
		setContent(verticalLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	/**
	 * @author ly.youhort
	 */
	private class FileUploader implements Receiver, SucceededListener {
		private static final long serialVersionUID = -4738310396410864219L;
		
		private File file;
		
	    public OutputStream receiveUpload(String filename, String mimeType) {
	        FileOutputStream fos = null;
	        try {
	        	String tmpDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	            file = new File(tmpDir + "/" + filename);
	            fos = new FileOutputStream(file);
	        } catch (FileNotFoundException e) {
	            Notification.show("Could not open file<br/>", e.getMessage(), Type.ERROR_MESSAGE);
	            return null;
	        }
	        return fos;
	    }
	    
	    public void uploadSucceeded(SucceededEvent event) {
	        if (file != null) {
	        	InputStream stream;
				try {
					stream = new FileInputStream(file);
					HSSFWorkbook wb = new HSSFWorkbook(stream);
					HSSFSheet sheet = wb.getSheetAt(0);
					Iterator<Row> rowIterator = sheet.iterator();
					int i = 0;
				    while (rowIterator.hasNext()) {
				    	Row row = rowIterator.next();
				    	if (i > 1) {
					        String provinceCode = "" + (int) row.getCell(0).getNumericCellValue();
					        String provinceDes = row.getCell(1).getStringCellValue();
					        String districtCode = "" + (int) row.getCell(2).getNumericCellValue();
					        String districtDes = row.getCell(3).getStringCellValue();
					        String communeCode = "" + (int) row.getCell(4).getNumericCellValue();
					        String communeDes = row.getCell(5).getStringCellValue();
					        String villageCode = "" + (int) row.getCell(6).getNumericCellValue();
					        String villageDes = row.getCell(7).getStringCellValue();
					        Province province = addressService.getByCode(Province.class, provinceCode);
					        if (province == null) {
					        	province = new Province();
					        	province.setCode(provinceCode);
					        	province.setDesc(provinceDes);
					        	province.setDescEn(provinceDes);
					        	province.setCountry(ECountry.KHM);
					        	addressService.saveOrUpdate(province);
					        }
					        
					        BaseRestrictions<District> restrictionsDisctrict = new BaseRestrictions<District>(District.class);							
							List<Criterion> criterionsDisctrict = new ArrayList<Criterion>();
							criterionsDisctrict.add(Restrictions.eq("code", districtCode));
							criterionsDisctrict.add(Restrictions.eq("province.id", province.getId()));							
							restrictionsDisctrict.setCriterions(criterionsDisctrict);
					        District district = null;
					        List<District> listDisctricts = addressService.list(restrictionsDisctrict);
					        if (listDisctricts != null && !listDisctricts.isEmpty()) {
					        	if (listDisctricts.size() != 1) {
					        		throw new IllegalArgumentException("Province "+ provinceDes + " could not have more than 1 district [" + districtDes + "]");
					        	} else {
					        		district = listDisctricts.get(0);
					        	}
					        } else {
					        	district = new District();
					        	district.setCode(districtCode);
					        	district.setDesc(districtDes);
					        	district.setDescEn(districtDes);
					        	district.setProvince(province);
					        	addressService.saveOrUpdate(district);
					        }
					        
					        BaseRestrictions<Commune> restrictionsCommune = new BaseRestrictions<Commune>(Commune.class);							
							List<Criterion> criterionsCommune = new ArrayList<Criterion>();
							criterionsCommune.add(Restrictions.eq("code", communeCode));
							criterionsCommune.add(Restrictions.eq("district.id", district.getId()));							
							restrictionsCommune.setCriterions(criterionsCommune);
							Commune commune = null;
					        List<Commune> listCommunes = addressService.list(restrictionsCommune);
					        if (listCommunes != null && !listCommunes.isEmpty()) {
					        	if (listCommunes.size() != 1) {
					        		throw new IllegalArgumentException("District "+ districtDes + " could not have more than 1 commune [" + communeDes + "]");
					        	} else {
					        		commune = listCommunes.get(0);
					        	}
					        } else {
					        	commune = new Commune();
					        	commune.setCode(communeCode);
					        	commune.setDesc(communeDes);
					        	commune.setDescEn(communeDes);
					        	commune.setDistrict(district);
					        	addressService.saveOrUpdate(commune);
					        }
					        
					        BaseRestrictions<Village> restrictionsVillage = new BaseRestrictions<Village>(Village.class);							
							List<Criterion> criterionsVillage = new ArrayList<Criterion>();
							criterionsVillage.add(Restrictions.eq("code", villageCode));
							criterionsVillage.add(Restrictions.eq("commune.id", commune.getId()));							
							restrictionsVillage.setCriterions(criterionsVillage);
							Village village =  null;
					        List<Village> listVillages = addressService.list(restrictionsVillage);
					        if (listVillages != null && !listVillages.isEmpty()) {
					        	if (listVillages.size() != 1) {
					        		throw new IllegalArgumentException("Commune "+ communeDes + " could not have more than 1 village [" + villageDes + "]");
					        	} else {
					        		village = listVillages.get(0);
					        	}
					        } else {
					        	village = new Village();
					        	village.setCode(villageCode);
					        	village.setDesc(villageDes);
					        	village.setDescEn(villageDes);
					        	village.setCommune(commune);
					        	addressService.saveOrUpdate(village);
					        }
					        
					        String debug = StringUtils.leftPad(provinceCode, 5);
					        debug += StringUtils.leftPad(provinceDes + "|", 40);
					        debug += StringUtils.leftPad(districtCode + "|", 5);
					        debug += StringUtils.leftPad(districtDes + "|", 40);
					        debug += StringUtils.leftPad(communeCode + "|", 5);
					        debug += StringUtils.leftPad(communeDes + "|", 40);
					        debug += StringUtils.leftPad(villageCode + "|", 5);
					        debug += StringUtils.leftPad(villageDes, 40);
					        logger.debug(debug);
				    	}
				    	i++;
				    }
				} catch (FileNotFoundException e) {
					logger.error("FileNotFoundException", e);
				} catch (IOException e) {
					logger.error("IOException", e);
				}
	        }
	    }
	};
}
