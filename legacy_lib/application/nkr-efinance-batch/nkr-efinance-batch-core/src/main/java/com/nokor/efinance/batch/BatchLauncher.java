package com.nokor.efinance.batch;


import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;

/**
 * @author ky.nora
 *
 */
public class BatchLauncher {
	public static String DEFAULT_PROPERTIES = "default.properties";
	private static BatchSession session;

	public static void main(String args[]) {
        session = new BatchSession();
        long processTime = System.currentTimeMillis();
        try {
			session.init();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		JobLauncher jobLauncher = (JobLauncher) session.getAppContext().getBean(
				"jobLauncher");
		Job jobBean = (Job) session.getAppContext().getBean("mainJob");
		JobParameters jobParam = buildParamsFromArguments(args);

		try {
			
			JobExecution execution = jobLauncher.run(jobBean, jobParam);
			
			long endTime = System.currentTimeMillis();
	        long elapsedTime = endTime - processTime; 
	        String format = String.format("%%0%dd", 2);  
	        elapsedTime = elapsedTime / 1000;  
	        String seconds = String.format(format, elapsedTime % 60);  
	        String minutes = String.format(format, (elapsedTime % 3600) / 60);  
	        String hours = String.format(format, elapsedTime / 3600);  
	        String time =  hours + ":" + minutes + ":" + seconds;  

			System.out.println("******************************");
			System.out.println("* Finish Status : " + execution.getStatus());
			System.out.println("* Process time  : " + time);
			System.out.println("******************************");
		} catch (Exception e) {
			e.printStackTrace();
		}

		session.shutDown();
	}

	/**
	 * @return
	 */
	public static BatchSession getSession() {
		return session;
	}

	/**
	 * @param session
	 */
	public static void setSession(BatchSession session) {
		BatchLauncher.session = session;
	}

	/**
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unused")
	private static JobParameters buildParamsFromArguments(String[] params) {
		JobParametersBuilder builder = new JobParametersBuilder();
		
		int i = 0;
		if (params != null) {
			for (String param: params) {
				i++;
				builder.addString("param" + i, param);
			}
		}
		if (i == 0) {
			System.out.println("No arguments applied.");
		}
	 
		return builder.toJobParameters();
	}
	
	/**
	 * Extract parameter, separate key and value by "="
	 * @param stepExecution
	 * @return
	 */
	public static Map<String, String> extractParameters(StepExecution stepExecution) {
		JobParameters jobParameters = stepExecution.getJobParameters();
		Map<String, String> pMap = new HashMap<>();
		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			String param = jobParameters.getString("param" + i);
			if (param != null && !param.isEmpty()) {
				String[] parts = param.split("=");
				if (parts.length > 1) {
					pMap.put(parts[0], parts[1]);
				}
			} else {
				break;
			}
		}
		return pMap;
	}
}
