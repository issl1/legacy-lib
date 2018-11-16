package com.nokor.frmk.scripting;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://docs.oracle.com/javase/6/docs/technotes/guides/scripting/programmer_guide/index.html
 * @author prasnar
 * 
 */
public class JavaScriptEngine {
    protected static Logger logger = LoggerFactory.getLogger(JavaScriptEngine.class);

    private static final String EXT = ".js";
    private ScriptEngine engine;
    private String resourcesFolder;
    private BufferedReader reader;
    private Map<String, String> rules;
    private String lastRuleLoaded; 
	
    /**
	 * 
	 */
	public JavaScriptEngine(String resourcesFolder) {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		engine = factory.getEngineByName("JavaScript");
		rules = new HashMap<String, String>();
		if (StringUtils.isNotEmpty(resourcesFolder)) {
			this.resourcesFolder = resourcesFolder + "/";
		} else {
			this.resourcesFolder = "";
		}
	}
	
	
	/**
	 * 
	 * @param ruleResource
	 * @return
	 */
	public boolean loadRule(String ruleResource) {
		try {
			lastRuleLoaded = ruleResource;
			String content = rules.get(lastRuleLoaded);
			if (content != null) {
				return true;
			}
			
			String rulesFile =  resourcesFolder + lastRuleLoaded + EXT;
			InputStream in = ClassLoader.getSystemResourceAsStream(rulesFile);
			if (in == null) {
	        	logger.debug("Searching in the system classpath.." + rulesFile);
	            in = ClassLoader.getSystemResourceAsStream("/" + rulesFile);
	        }
	        if (in == null) {
	        	logger.debug("Searching in the system classpath.." + rulesFile);
	            in = ClassLoader.getSystemClassLoader().getResourceAsStream("/" + rulesFile);
	        }
	        if (in == null) {
	        	logger.debug("Searching in the system classpath.." + rulesFile);
	            in = this.getClass().getClassLoader().getResourceAsStream("/" + rulesFile); 
	        }
	        
	        if (in == null) {
	            String errMsg = "Can't find " + rulesFile + " in the classpath, check your Configuration";
	            logger.error(errMsg);
	            throw new IllegalStateException("Can not find [" + rulesFile + "]");
	        }
	        InputStreamReader is = new InputStreamReader(in);
	        reader = new BufferedReader(is);
			
	        StringBuilder outContent = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            outContent.append(line);
	        }
	        logger.debug(outContent.toString());   //Prints the string content read from input stream
	        reader.close();
	        rules.put(lastRuleLoaded, outContent.toString());
	        
	        return true;
		} catch (Exception e) {
            logger.error("An error has occured while searching the resource", e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setParam(String name, Object value) {
		engine.put(name, value);
	}
	
	/**
	 * 
	 * @throws ScriptException
	 */
	public void eval() throws ScriptException {
		engine.eval(rules.get(lastRuleLoaded));
	}
	
	public void eval(String ruleResource) throws ScriptException {
		engine.eval(rules.get(ruleResource));
	}

	/**
	 * 
	 * @param paramName
	 * @return
	 */
	public Object getValue(String paramName) {
		return engine.get(paramName);
	}
	
}
