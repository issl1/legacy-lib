package com.nokor.efinance.gui.ui.panel.contract.asset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.asset.model.MAsset;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.conf.AppConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.chea
 *
 */
public class AssetConditionsTabPanel extends AbstractControlPanel implements MAsset {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3334575967934534740L;
	
	private SimpleTable<Entity> conditionsTable;
	private List<ColumnDefinition> columnDefinitions;
	
	private TextField txtMileage;
	private ComboBox cbxNbKeys;
	private ComboBox cbxPrimaryGrade;
	private TextArea txtComment;
	private Label lblConditionsRecordByValue;
	private Label lblDateValue;
	
	private CheckBox cbAOMNotice;
	private CheckBox cbMobileable;
	private CheckBox cbRegistrationPlate;
	private CheckBox cbCrashed;
	private CheckBox cbAccidented;
	private CheckBox cbCustomizedSpareParts;
	private CheckBox cbMissingParts;
	private CheckBox cbEnginStarTable;
	private CheckBox cbCDIBox;
	private CheckBox cbBroken;
	
	private Embedded image;
	
	public AssetConditionsTabPanel() {
		setMargin(true);
		
		this.columnDefinitions = createColumnDefinitions();
		conditionsTable = new SimpleTable<>(this.columnDefinitions);
		conditionsTable.setCaption(I18N.message("conditions"));
		conditionsTable.setPageLength(8);
		setTableIndexedContainer();
		
		txtMileage = ComponentFactory.getTextField(20, 90);
		cbxNbKeys = new ComboBox();
		cbxNbKeys.setWidth(60, Unit.PIXELS);
		cbxNbKeys.addItem(0);
		cbxNbKeys.addItem(1);
		cbxNbKeys.addItem(2);
		
		cbxPrimaryGrade = new ComboBox();
		cbxPrimaryGrade.setWidth(60, Unit.PIXELS);
		cbxPrimaryGrade.addItem("A");
		cbxPrimaryGrade.addItem("B");
		cbxPrimaryGrade.addItem("C");
		
		txtComment = ComponentFactory.getTextArea(false, 200, 70);
		lblConditionsRecordByValue = getLabelValue();
		lblDateValue = getLabelValue();
		
		Label lblMileage = ComponentLayoutFactory.getLabelCaption("mileage");
		Label lblnbKeys = ComponentLayoutFactory.getLabelCaption("number.of.key");
		Label lblPrimaryGrade = ComponentLayoutFactory.getLabelCaption("primary.grade");
		Label lblComment = ComponentLayoutFactory.getLabelCaption("comment");
		
		Label lblConditionRecordBy = getLabel("condition.record.by");
		lblConditionRecordBy.setWidthUndefined();
		Label lblDate = getLabel("date");
		
		HorizontalLayout firstRow = ComponentLayoutFactory.getHorizontalLayout(false, true);
		firstRow.addComponent(txtMileage);
		firstRow.addComponent(lblnbKeys);
		firstRow.addComponent(cbxNbKeys);
		firstRow.addComponent(lblPrimaryGrade);
		firstRow.addComponent(cbxPrimaryGrade);
		
		
		GridLayout formLayout = ComponentLayoutFactory.getGridLayout(6, 3);
		formLayout.setSpacing(true);
		int iCol = 0;
		formLayout.addComponent(lblMileage, iCol++, 0);
		formLayout.addComponent(firstRow, iCol++, 0);
		
		iCol = 0;
		formLayout.addComponent(lblComment, iCol++, 1);
		formLayout.addComponent(txtComment, iCol++, 1);
		
		iCol = 0;
		formLayout.addComponent(lblConditionRecordBy, iCol++, 2);
		formLayout.addComponent(lblConditionsRecordByValue, iCol++, 2);
		formLayout.addComponent(lblDate, iCol++, 2);
		formLayout.addComponent(lblDateValue, iCol++, 2);
		
		VerticalLayout conditionLeftLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		conditionLeftLayout.addComponent(conditionsTable);
		conditionLeftLayout.addComponent(formLayout);
		
		HorizontalLayout mainConditionLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		mainConditionLayout.addComponent(conditionLeftLayout);
		mainConditionLayout.addComponent(ComponentFactory.getSpaceLayout(75, Unit.PIXELS));
		mainConditionLayout.addComponent(createConditionRightPanel());
		
		Panel mainPanel = new Panel(mainConditionLayout);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(PATH, I18N.message("path"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(TOTAL_LOSE, I18N.message("total.lose"), CheckBox.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(BAD, I18N.message("bad"), CheckBox.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(AVERAGE, I18N.message("average"), CheckBox.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(GOOD, I18N.message("good"), CheckBox.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(VERY_GOOD, I18N.message("very.good"), CheckBox.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(BRAND_NEW, I18N.message("brand.new"), CheckBox.class, Align.LEFT, 80));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer() {
		conditionsTable.removeAllItems();
		Container indexedContainer = conditionsTable.getContainerDataSource();
		String[] paths = {I18N.message("front.break.pump"), I18N.message("battery"), I18N.message("front.disc"), 
						  I18N.message("carburator"), I18N.message("top.break.pump"), I18N.message("exhaust.pipe"),
						  I18N.message("distart"), I18N.message("chassis")};
		
		for (int i = 0; i < paths.length; i++) {
			Item item = indexedContainer.addItem(i);
			item.getItemProperty(PATH).setValue(paths[i]);
			item.getItemProperty(TOTAL_LOSE).setValue(new CheckBox());
			item.getItemProperty(BAD).setValue(new CheckBox());
			item.getItemProperty(AVERAGE).setValue(new CheckBox());
			item.getItemProperty(GOOD).setValue(new CheckBox());
			item.getItemProperty(VERY_GOOD).setValue(new CheckBox());
			item.getItemProperty(BRAND_NEW).setValue(new CheckBox());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout createConditionRightPanel() {
		cbAOMNotice = new CheckBox(I18N.message("yes"));
		cbMobileable = new CheckBox(I18N.message("yes"));
		cbRegistrationPlate = new CheckBox(I18N.message("yes"));
		cbCrashed = new CheckBox(I18N.message("yes"));
		cbAccidented = new CheckBox(I18N.message("yes"));
		cbCustomizedSpareParts = new CheckBox(I18N.message("yes"));
		cbMissingParts = new CheckBox(I18N.message("yes"));
		cbEnginStarTable = new CheckBox(I18N.message("yes"));
		cbCDIBox = new CheckBox(I18N.message("yes"));
		cbBroken = new CheckBox(I18N.message("yes"));
		
		Label lblAOMNotice = getLabel("aom.notice");
		Label lblMobileable = getLabel("mobileable");
		Label lblRegistrationPlate = getLabel("registration.plate");
		Label lblCrashed = getLabel("crashed");
		Label lblAccidented = getLabel("accidented");
		Label lblCustomizedSpareParts = getLabel("customized.spare.parts");
		Label lblMissingParts = getLabel("missing.parts");
		Label lblEnginStarTable = getLabel("engin.starttable");
		Label lblCDIBox = getLabel("cdi.box");
		Label lblBroken = getLabel("broken");
		
		GridLayout rightGridLayout = ComponentLayoutFactory.getGridLayout(3, 10);
		rightGridLayout.setSpacing(true);
		
		int iCol = 0;
		rightGridLayout.addComponent(lblAOMNotice, iCol++, 0);
		rightGridLayout.addComponent(ComponentFactory.getSpaceLayout(65, Unit.PIXELS), iCol++, 0);
		rightGridLayout.addComponent(cbAOMNotice, iCol++, 0);
		
		iCol = 0;
		rightGridLayout.addComponent(lblMobileable, iCol++, 1);
		rightGridLayout.addComponent(ComponentFactory.getSpaceLayout(65, Unit.PIXELS), iCol++, 1);
		rightGridLayout.addComponent(cbMobileable, iCol++, 1);
		iCol = 0;
		rightGridLayout.addComponent(lblRegistrationPlate, iCol++, 2);
		rightGridLayout.addComponent(ComponentFactory.getSpaceLayout(65, Unit.PIXELS), iCol++, 2);
		rightGridLayout.addComponent(cbRegistrationPlate, iCol++, 2);
		iCol = 0;
		rightGridLayout.addComponent(lblCrashed, iCol++, 3);
		rightGridLayout.addComponent(ComponentFactory.getSpaceLayout(65, Unit.PIXELS), iCol++, 3);
		rightGridLayout.addComponent(cbCrashed, iCol++, 3);
		iCol = 0;
		rightGridLayout.addComponent(lblAccidented, iCol++, 4);
		rightGridLayout.addComponent(ComponentFactory.getSpaceLayout(65, Unit.PIXELS), iCol++, 4);
		rightGridLayout.addComponent(cbAccidented, iCol++, 4);
		iCol = 0;
		rightGridLayout.addComponent(lblCustomizedSpareParts, iCol++, 5);
		rightGridLayout.addComponent(ComponentFactory.getSpaceLayout(65, Unit.PIXELS), iCol++, 5);
		rightGridLayout.addComponent(cbCustomizedSpareParts, iCol++, 5);
		iCol = 0;
		rightGridLayout.addComponent(lblMissingParts, iCol++, 6);
		rightGridLayout.addComponent(ComponentFactory.getSpaceLayout(65, Unit.PIXELS), iCol++, 6);
		rightGridLayout.addComponent(cbMissingParts, iCol++, 6);
		iCol = 0;
		rightGridLayout.addComponent(lblEnginStarTable, iCol++, 7);
		rightGridLayout.addComponent(ComponentFactory.getSpaceLayout(65, Unit.PIXELS), iCol++, 7);
		rightGridLayout.addComponent(cbEnginStarTable, iCol++, 7);
		iCol = 0;
		rightGridLayout.addComponent(lblCDIBox, iCol++, 8);
		rightGridLayout.addComponent(ComponentFactory.getSpaceLayout(65, Unit.PIXELS), iCol++, 8);
		rightGridLayout.addComponent(cbCDIBox, iCol++, 8);
		iCol = 0;
		rightGridLayout.addComponent(lblBroken, iCol++, 9);
		rightGridLayout.addComponent(ComponentFactory.getSpaceLayout(65, Unit.PIXELS), iCol++, 9);
		rightGridLayout.addComponent(cbBroken, iCol++, 9);
		
		VerticalLayout rightLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		rightLayout.addComponent(rightGridLayout);
		rightLayout.addComponent(uploadImagePanel());
		
		return rightLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel uploadImagePanel() {
		image = new Embedded();
		image.setVisible(false);
		image.setWidth(323, Unit.PIXELS);
		image.setHeight(250, Unit.PIXELS);
		
		ImageUploader receiver = new ImageUploader();
		Upload upload = new Upload("", receiver);
		upload.setButtonCaption(I18N.message("start.upload"));
		upload.addSucceededListener(receiver);

		Panel panel = new Panel();
		Layout panelContent = new VerticalLayout();
		panelContent.addComponents(upload, image);
		panel.setContent(panelContent);
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		
		return panel;
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	class ImageUploader implements Receiver, SucceededListener {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 2442834083418962563L;
		public File file;

	    public OutputStream receiveUpload(String filename, String mimeType) {
	        
	    	FileOutputStream fos = null; 
	        try {
	            
	        	String fileDir = AppConfig.getInstance().getConfiguration().getString("specific.tmpdir");
	            file = new File(fileDir + filename);
	            fos = new FileOutputStream(file);
	        } catch (final java.io.FileNotFoundException e) {
	        	ComponentLayoutFactory.displayErrorMsg(e.getMessage());
	            return null;
	        }
	        return fos;
	    }

	    public void uploadSucceeded(SucceededEvent event) {
	        image.setVisible(true);
	        image.setSource(new FileResource(file));
	    }
	};

}
