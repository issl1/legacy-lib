package com.nokor.efinance.glf.report.xls;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.jxls.transformer.XLSTransformer;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.tools.report.Report;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.efinance.core.shared.report.ReportParameter;
import com.nokor.frmk.security.context.SecApplicationContextHolder;

public class GLFGuaranteeContractLegalKH implements Report {
	
    protected QuotationService quotationService = (QuotationService) SecApplicationContextHolder.getContext().getBean("quotationService");
    
    /**
	 * 
	 */
	@Override
	public String generate(ReportParameter reportParameter) throws Exception {
		Long quotaId = (Long) reportParameter.getParameters().get("quotaId");
		boolean stamp = (Boolean) reportParameter.getParameters().get("stamp");
		
		Quotation quotation = quotationService.getById(Quotation.class, quotaId);
		Applicant applicant = quotation.getApplicant();
		Applicant guarantor = quotation.getGuarantor();
		
		Individual individual = applicant.getIndividual();
	    Map<String, String> beans = new HashMap<String, String>();
	    beans.put("lesseeName", individual.getLastName() + " " + individual.getFirstName());
	    
	    if(guarantor != null)
	    	beans.put("guaranteeName", guarantor.getIndividual().getLastName() + " " + guarantor.getIndividual().getFirstName());
	    else
	    	beans.put("guaranteeName", "");
	    
	    Date contractStartDate = quotation.getContractStartDate();
		if (contractStartDate == null) {
			contractStartDate = DateUtils.today();
		}
			    
	    beans.put("agreementDate", DateUtils.getDateLabel(contractStartDate, DateUtils.FORMAT_DDMMYYYY_SLASH));
	            
	    String templatePath = AppConfig.getInstance().getConfiguration().getString("specific.templatedir");
	    String templateFileName = templatePath + (stamp ? "/GLFGuaranteeContractLegalStamp.xlsx" : "/GLFGuaranteeContractLegal.xlsx");
	    String outputPath = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	    
	    String prefixOutputName = "GLFGuaranteeContractLegal";
        String sufixOutputName = "xlsx";
	    String uuid = UUID.randomUUID().toString().replace("-", "");
        String xlsFileName = outputPath + "/" + prefixOutputName + uuid + "." + sufixOutputName;
	    
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(templateFileName, beans, xlsFileName);
        
        /*Edit excel file to add photo*/
        /*InputStream inp = new FileInputStream(destFileName);
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);

        CreationHelper helper = wb.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();

            InputStream is = new FileInputStream("D:/Work_local/glf/glf_report/glf/in/client.JPG");
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();

            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setRow1(6);
            anchor.setCol1(23);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize();

        FileOutputStream fileOut1 = new FileOutputStream(destFileName);
        wb.write(fileOut1);
        fileOut1.close(); */
        
        return prefixOutputName + uuid + "." + sufixOutputName;
	}
	
}
