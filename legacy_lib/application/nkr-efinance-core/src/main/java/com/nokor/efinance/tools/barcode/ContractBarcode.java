package com.nokor.efinance.tools.barcode;

import java.io.File;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.conf.AppConfig;

/**
 * @author bunlong.taing
 */
public class ContractBarcode {
	
	private static final String BARCODE_PATH = "/barcode/contract";
	
	/**
	 * @param contract
	 * @return
	 */
	public static File create(Contract contract) {
		String pathName = AppConfig.getInstance().getConfiguration().getString("document.path") + BARCODE_PATH;
		File path = new File(pathName);
		if (!path.exists()) {
			path.mkdirs();
    	}
		
		String fileName = path + "/" + contract.getReference() + ".png";
		File file = new File(fileName);
		if (!file.exists()) {
			Barcode.create(contract.getReference(), file);
		}
		return file;
	}

}
