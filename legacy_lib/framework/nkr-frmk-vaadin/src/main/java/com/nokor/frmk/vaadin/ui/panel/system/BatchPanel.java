package com.nokor.frmk.vaadin.ui.panel.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.config.model.SettingConfig;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author ky.nora
 *
 */
public class BatchPanel extends Window implements ValueChangeListener, FrmkServicesHelper {

	private static final long serialVersionUID = -8299353702243472373L;
	
	private String batchDir = "";
	private String selectBatch = "";
	VerticalLayout batchParamLayout;
	

	public BatchPanel() {
		setModal(true);		
		setCaption(I18N.message("run.batch"));
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		final VerticalLayout messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("error");
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {
			private static final long serialVersionUID = 3975121141565713259L;
			public void buttonClick(ClickEvent event) {
            	close();
            }
		});
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));		
		navigationPanel.addButton(btnCancel);
		
		HorizontalLayout batchLayout = new HorizontalLayout();
		batchLayout.setSpacing(true);
		
		VerticalLayout batchAppLayout = new VerticalLayout();
		batchParamLayout = new VerticalLayout();
		batchParamLayout.setMargin(true);
		batchParamLayout.setSpacing(true);
        
		
		SettingConfig batchDirConf = ENTITY_SRV.getByCode(SettingConfig.class, "batch.directory");
		batchDir = batchDirConf.getValue();
		
		ArrayList<String> batchs = new ArrayList<String>();
		File myFile = new File(batchDir);
		File[] filesInDirectory = myFile.listFiles();
			if (filesInDirectory != null) {
			for (int i = 0; i < filesInDirectory.length; i++) {
				File oneFile = filesInDirectory[i];
				if (oneFile.isDirectory()) {
					batchs.add(oneFile.getName());
				}
			}
		}
		
		ListSelect batchAppList = new ListSelect("Batch applications", batchs);
		batchAppList.addStyleName("pagedtable");
        batchAppList.setRows(10);
        batchAppList.setNullSelectionAllowed(false);
        batchAppList.setImmediate(true);
        batchAppList.addValueChangeListener(this);
        batchAppLayout.addComponent(batchAppList);
        
		Button btnLaunch = new Button(I18N.message("launch"), new Button.ClickListener() {
			private static final long serialVersionUID = -7928915256329050115L;

			public void buttonClick(ClickEvent event) {
				new BatchThread().start();
				Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
				notification.setDescription(I18N.message("backgroundprocess.batch"));
				notification.setDelayMsec(3000);
				notification.show(Page.getCurrent());
            }
		});
        batchAppLayout.addComponent(btnLaunch);
        
        if (batchs.isEmpty()) {
			Label messageLabel = new Label(I18N.message("config.invalid.batch.dir"));
			messagePanel.setVisible(true);
			messagePanel.addComponent(messageLabel);
			btnLaunch.setEnabled(false);
			batchAppList.setWidth("300px");
		} else {
			batchAppList.select(0);
		}
        
        batchLayout.addComponent(batchAppLayout);
		batchLayout.addComponent(batchParamLayout);
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(batchLayout);
		setContent(contentLayout);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		selectBatch = (String) event.getProperty().getValue();
		batchParamLayout.removeAllComponents();
		String fileName = batchDir + "/" + selectBatch + "/default.properties";
		File myFile = new File(fileName);
		if (!myFile.exists()) {
			return;
		}
		OrderedProperties prop = new OrderedProperties();
    	try {
    		prop.load(new FileInputStream(fileName));
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
		
    	Set<String> propKeys = prop.stringPropertyNames();
		for (String key : propKeys) {
			TextField param = ComponentFactory.getTextField(key, false, 100, 160);
			param.setData(key);
			if (prop.get(key) != null) {
				param.setValue(prop.get(key).toString());
			}
			batchParamLayout.addComponent(param);
		}
	}
	
	/**
	 * @return
	 */
	private List<String> configParameters() {
		List<String> cmdList = new ArrayList<String>();
		int pCount = batchParamLayout.getComponentCount();
		for (int i = 0; i < pCount; i++) {
			TextField param = (TextField) batchParamLayout.getComponent(i);
			String argValue = param.getValue().trim();
			cmdList.add(param.getData() + "=" + argValue);
		}
		return cmdList;
	}
	
	/**
	 * Order Properties
	 * @author ky.nora
	 *
	 */
	private class OrderedProperties extends Properties {
		private static final long serialVersionUID = 3218794742977907027L;
		private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

	    public Enumeration<Object> keys() {
	        return java.util.Collections.<Object>enumeration(keys);
	    }
	    
	    public Object put(Object key, Object value) {
	        keys.add(key);
	        return super.put(key, value);
	    }
	    
	    public Set<String> stringPropertyNames() {
	        Set<String> set = new LinkedHashSet<String>();

	        for (Object key : this.keys) {
	            set.add((String)key);
	        }
	        return set;
	    }
	}
	
	private class BatchThread extends Thread {
	    public void run() {
	    	final String batchName = selectBatch;
	    	String workDir = batchDir + "/" + batchName;
			List<String> command = new ArrayList<String>();
		    command.add("java");
		    command.add("-jar");
		    command.add("launcher.jar");
		    command.addAll(configParameters());

		    ProcessBuilder builder = new ProcessBuilder(command);
		    builder.directory(new File(workDir));

		    Process process = null;
		    try {
		    	process = builder.start();
		    	
		    } catch (IOException e) {
				e.printStackTrace();
			}
		    InputStream is = process.getInputStream();
		    InputStreamReader isr = new InputStreamReader(is);
		    BufferedReader br = new BufferedReader(isr);
		    try {
				while (br.readLine() != null) {
				  // System.out.println(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		    try {
				System.out.println("Batch : " + batchName + " exit with code " + process.waitFor());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	}
}
