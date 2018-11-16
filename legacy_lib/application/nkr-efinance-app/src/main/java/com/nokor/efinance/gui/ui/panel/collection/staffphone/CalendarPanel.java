package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.EColAction;
import com.nokor.efinance.core.collection.model.MCollectionAction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public class CalendarPanel extends VerticalLayout implements FinServicesHelper, MCollectionAction {

	/** */
	private static final long serialVersionUID = -2149733046478982939L;
		
	private List<ColumnDefinition> columnDefinitions;	
	private SimpleTable<Entity> weekTable;
	private SimpleTable<Entity> monthTable;
	
	private Map<String, List<String>> collectionActionsInMonth;
	private Map<String, List<String>> collectionActionsInWeek;

	public CalendarPanel() {
		setSizeFull();
		setMargin(true);
		
		this.columnDefinitions = createColumnWeekDefinitions();
		weekTable = new SimpleTable<Entity>(this.columnDefinitions);
		weekTable.setPageLength(5);
		weekTable.setSizeFull();
		
		Panel weekPanel = new Panel();
		weekPanel.setCaption(I18N.message("this.week"));
		weekPanel.setContent(weekTable);
		
		this.columnDefinitions = createColumnMonthDefinitions();
		monthTable = new SimpleTable<Entity>(this.columnDefinitions);
		monthTable.setPageLength(5);
		monthTable.setSizeFull();
		
		Panel monthPanel = new Panel(I18N.message("this.month"));
		monthPanel.setContent(monthTable);
		
		VerticalLayout calendarLayout = new VerticalLayout();
		calendarLayout.setSpacing(true);
		calendarLayout.setMargin(true);
		calendarLayout.addComponent(weekPanel);
		calendarLayout.addComponent(monthPanel);
		
		setWeekIndexedContainer();
		setMonthIndexedContainer();
		
		addComponent(calendarLayout);
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		setWeekIndexedContainer();
		setMonthIndexedContainer();
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnWeekDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(MONDAY, I18N.message("monday"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(TUESDAY, I18N.message("tuesday"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(WENDNESDAY, I18N.message("wednesday"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(THURSDAY, I18N.message("thursday"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(FRIDAY, I18N.message("friday"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(SATURDAY, I18N.message("saturday"), String.class, Align.LEFT, 125));
		columnDefinitions.add(new ColumnDefinition(SUNDAY, I18N.message("sunday"), String.class, Align.LEFT, 125));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnMonthDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(THIS_WEEK, I18N.message("this.week"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition(NEXT_WEEK, I18N.message("next.week"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition(THE_WEEK_AFTER, I18N.message("the.week.after"), String.class, Align.LEFT, 180));
		return columnDefinitions;
	}
	
	/**
	 * set week IndexContainer
	 */
	@SuppressWarnings("unchecked")
	private void setWeekIndexedContainer() {
		weekTable.removeAllItems();
		Container indexedContainer = weekTable.getContainerDataSource();
		validateCollectionActionsInThisWeek();
		if (collectionActionsInWeek != null && !collectionActionsInWeek.isEmpty()) {
			int collectionActionMaxlength = getMaxLength(collectionActionsInWeek);
			for(int i = 0; i < collectionActionMaxlength; i++) {
				Item item = indexedContainer.addItem(i);
				item.getItemProperty(MONDAY).setValue(getCollectionAction(collectionActionsInWeek, MONDAY, i));
				item.getItemProperty(TUESDAY).setValue(getCollectionAction(collectionActionsInWeek, TUESDAY, i));
				item.getItemProperty(WENDNESDAY).setValue(getCollectionAction(collectionActionsInWeek, WENDNESDAY, i));
				item.getItemProperty(THURSDAY).setValue(getCollectionAction(collectionActionsInWeek, THURSDAY, i));
				item.getItemProperty(FRIDAY).setValue(getCollectionAction(collectionActionsInWeek, FRIDAY, i));
				item.getItemProperty(SATURDAY).setValue(getCollectionAction(collectionActionsInWeek, SATURDAY, i));
				item.getItemProperty(SUNDAY).setValue(getCollectionAction(collectionActionsInWeek, SUNDAY, i));
			}
		}
		
	}
	
	/**
	 * set month IndexContainer
	 */
	@SuppressWarnings("unchecked")
	private void setMonthIndexedContainer() {
		monthTable.removeAllItems();
		Container indexedContainer = monthTable.getContainerDataSource();
		validateCollectionActionsInThisMonth();
		if (collectionActionsInMonth != null && !collectionActionsInMonth.isEmpty()) {
			int collectionActionMaxlength = getMaxLength(collectionActionsInMonth);	
			for(int i = 0; i < collectionActionMaxlength; i++) {
				Item item = indexedContainer.addItem(i);
				item.getItemProperty(THIS_WEEK).setValue(getCollectionAction(collectionActionsInMonth, THIS_WEEK, i));
				item.getItemProperty(NEXT_WEEK).setValue(getCollectionAction(collectionActionsInMonth, NEXT_WEEK, i));
				item.getItemProperty(THE_WEEK_AFTER).setValue(getCollectionAction(collectionActionsInMonth, THE_WEEK_AFTER, i));
			}
		}
	}
	
	/**
	 * validateCollectionActionsInThisWeek
	 */
	private void validateCollectionActionsInThisWeek() {
		List<CollectionAction> collectionActions = COL_ACT_SRV.getCollectionActionOfWeek();
		collectionActionsInWeek = new LinkedHashMap<>();
		collectionActionsInWeek.put(MONDAY, new ArrayList<>());
		collectionActionsInWeek.put(TUESDAY, new ArrayList<>());
		collectionActionsInWeek.put(WENDNESDAY, new ArrayList<>());
		collectionActionsInWeek.put(THURSDAY, new ArrayList<>());
		collectionActionsInWeek.put(FRIDAY, new ArrayList<>());
		collectionActionsInWeek.put(SATURDAY, new ArrayList<>());
		collectionActionsInWeek.put(SUNDAY, new ArrayList<>());
		for (CollectionAction collectionAction : collectionActions) {
			String dayName = COL_ACT_SRV.getNameOfDay(collectionAction.getNextActionDate());
			EColAction colAction = collectionAction.getColAction();
			String colActionDesc = colAction == null ? "" : colAction.getDescEn();
			if ("MONDAY".equals(dayName)) {
				collectionActionsInWeek.get(MONDAY).add(colActionDesc);
			} else if ("TUESDAY".equals(dayName)) {
				collectionActionsInWeek.get(TUESDAY).add(colActionDesc);
			} else if ("WEDNESDAY".equals(dayName)) {
				collectionActionsInWeek.get(WENDNESDAY).add(colActionDesc);
			} else if ("THURSDAY".equals(dayName)) {
				collectionActionsInWeek.get(THURSDAY).add(colActionDesc);
			} else if ("FRIDAY".equals(dayName)) {
				collectionActionsInWeek.get(FRIDAY).add(colActionDesc);
			} else if ("SATURDAY".equals(dayName)) {
				collectionActionsInWeek.get(SATURDAY).add(colActionDesc);
			} else if ("SUNDAY".equals(dayName)) {
				collectionActionsInWeek.get(SUNDAY).add(colActionDesc);
				collectionActionsInWeek.get(SUNDAY).contains(colActionDesc);
			}
		}
	}
	
	/**
	 * validateCollectionActionsInThisMonth
	 */
	private void validateCollectionActionsInThisMonth() {
		List<CollectionAction> collectionActions = COL_ACT_SRV.getCollectionActionOfMonth();
		collectionActionsInMonth = new LinkedHashMap<>();
		collectionActionsInMonth.put(THIS_WEEK, new ArrayList<>());
		collectionActionsInMonth.put(NEXT_WEEK, new ArrayList<>());
		collectionActionsInMonth.put(THE_WEEK_AFTER, new ArrayList<>());
		for (CollectionAction collectionAction : collectionActions) {
			EColAction colAction = collectionAction.getColAction();
			if (COL_ACT_SRV.isNextActionDateInThisWeek(collectionAction)) {
				collectionActionsInMonth.get(THIS_WEEK).add(colAction != null ? colAction.getDescEn() : "");
			} else if (COL_ACT_SRV.isNextActionDateInNextWeek(collectionAction)) {
				collectionActionsInMonth.get(NEXT_WEEK).add(colAction != null ? colAction.getDescEn() : "");
			} else if (COL_ACT_SRV.isNextActionDateInWeekAfter(collectionAction)) {
				collectionActionsInMonth.get(THE_WEEK_AFTER).add(colAction != null ? colAction.getDescEn() : "");
			}
		}
	}
	
	/**
	 * 
	 * @param collectionActions
	 * @return
	 */
	private int getMaxLength(Map<String, List<String>> collectionActions) {
		int maxLength = 0;
		for (List<String> listcollectActions : collectionActions.values()) {
			maxLength = maxLength < getCollectionActionNotSame(listcollectActions).size() ? getCollectionActionNotSame(listcollectActions).size() : maxLength;
		}
		return maxLength;
	}
	/**
	 * 
	 * @param collectionActionInmonthes
	 * @param date
	 * @param i
	 * @return
	 */
	private String getCollectionAction(Map<String, List<String>> collectionActionInmonthes, String date, int i) {
		List<String> collectionActions = getCollectionActionNotSame(collectionActionInmonthes.get(date));
		Map<String, List<Integer>> collectionActionsAfterCount = countCollectionActon(collectionActionInmonthes.get(date));
		String nbCollectionAction = "";
		if (collectionActions != null && !collectionActions.isEmpty()) {
			if (collectionActions.size() >= i + 1) {
				if (collectionActionsAfterCount.get(collectionActions.get(i)).size() > 1) {
					nbCollectionAction = " (" + collectionActionsAfterCount.get(collectionActions.get(i)).size() + ")";
				}
				return collectionActions.get(i) + nbCollectionAction;
			}
		}
		return "";
	}
	
	/**
	 * 
	 * @param collectionActions
	 * @return
	 */
	private List<String> getCollectionActionNotSame(List<String> collectionActions) {
		List<String> collectionActionDescs = new ArrayList<>();
		for (String collectionAction : collectionActions) {
			if (!collectionActionDescs.contains(collectionAction)) {
				collectionActionDescs.add(collectionAction);
			}
		}
		return collectionActionDescs;
	}
	
	/**
	 * 
	 * @param collectionActions
	 * @return
	 */
	private Map<String, List<Integer>> countCollectionActon(List<String> collectionActions) {
		Map<String, List<Integer>> nbCollectionAction = new LinkedHashMap<>();
		List<String> countCollectionActions = new ArrayList<>();
		int i = 0;
		for (String collectionAction : collectionActions) {
			if (!countCollectionActions.contains(collectionAction)) {
				nbCollectionAction.put(collectionAction, new ArrayList<>());
				countCollectionActions.add(collectionAction);
			} 
			nbCollectionAction.get(collectionAction).add(i++);
		}
		return nbCollectionAction;
	}
}
