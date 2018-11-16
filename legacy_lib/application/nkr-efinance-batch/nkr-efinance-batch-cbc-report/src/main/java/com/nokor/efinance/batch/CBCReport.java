package com.nokor.efinance.batch;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;

import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.tools.sync.ConnectionManager;


/**
 * ky.nora
 *
 */
public class CBCReport extends StepExecutionListenerSupport
		implements Tasklet, ContractEntityField {
	private String email;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		Map<String, String> pMap = BatchLauncher.extractParameters(stepExecution);
		if (!pMap.isEmpty()) {
			email = (String) pMap.get("email");
		} else {
			Properties prop = new Properties();
	    	try {
	    		prop.load(new FileInputStream(BatchLauncher.DEFAULT_PROPERTIES));
	    		email = prop.getProperty("email");
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	        }
		}
	}

	@Override
	public RepeatStatus execute(StepContribution stepcontribution,
			ChunkContext chunkcontext) throws Exception {

		CBCReportExtraction report = new CBCReportExtraction();
		String fileName = report.generate(null);
        
		if (email != null && !email.isEmpty()) {
			FileSystemResource attachment = new FileSystemResource(fileName);
	        ConnectionManager.getInstance().sendMail(I18N.message("mail.subject.batch.cbc.report"), I18N.message("mail.message.batch.cbc.report"), email, attachment);
		}
		return RepeatStatus.FINISHED;
	}
}