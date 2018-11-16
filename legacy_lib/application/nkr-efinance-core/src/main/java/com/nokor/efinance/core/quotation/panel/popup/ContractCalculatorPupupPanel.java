package com.nokor.efinance.core.quotation.panel.popup;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
/**
 * 
 * @author buntha.chea
 *
 */
public class ContractCalculatorPupupPanel extends Window implements ClickListener{

	/** */
	private static final long serialVersionUID = -7906203971906208273L;

	private double current = 0.0;
    private double stored = 0.0;
    private char lastOperationRequested = 'C';

    // User interface components
   // private final Label display = new Label("0");
    private TextField txtDisplay;
	public ContractCalculatorPupupPanel (String caption){
		super(I18N.message(caption));
		setModal(true);
		setWidth(480, Unit.PIXELS);
		setHeight(450, Unit.PIXELS);
		setResizable(false);
		init();
	}
	
	public void init() {
		final GridLayout layout = new GridLayout(4, 5);
		layout.setStyleName("mygrid-cal");
		//layout.setMargin(true);
		//layout.setSpacing(true);
		
		//display.addStyleName("mylabel-cal");
		txtDisplay = ComponentFactory.getTextField();
		txtDisplay.setStyleName("mytextfield-cal");
		txtDisplay.setEnabled(false);
		txtDisplay.setValue("0");
		txtDisplay.setMaxLength(14);
		layout.addComponent(txtDisplay, 0, 0, 3, 0);
		
		
		String[] operations = new String[] { "7", "8", "9", "0", "4", "5", "6",
                "C", "1", "2", "3", "=", "+", "-", "x", "/" };
		 for (String caption : operations) {
			 // Create a button and use this application for event handling
			 Button button = new NativeButton(caption);
			 if ("C".equals(caption) || "=".equals(caption) || 
				"+".equals(caption) || "-".equals(caption) || "x".equals(caption) || "/".equals(caption)) {
				 button.setStyleName("mybutton-cal-color");
			 } else {
			     button.setStyleName("mybutton-cal");
			 }
			 button.setWidth("78px");
		     button.setHeight("60px");
		     button.addClickListener(this);
		     layout.addComponent(button);
	        }
		 
		 VerticalLayout verticalLayout = new VerticalLayout();
		 verticalLayout.setSizeFull();
		 verticalLayout.addComponent(layout);
		 verticalLayout.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
		 setContent(verticalLayout);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button button = event.getButton();
		
        // Get the requested operation from the button caption
        char requestedOperation = button.getCaption().charAt(0);

        // Calculate the new value
        String newValue = calculate(requestedOperation);

        // Update the result label with the new value
        txtDisplay.setValue(String.valueOf(newValue));
	}
	
    private String calculate(char requestedOperation) {
    	NumberFormat formatter = new DecimalFormat("#0");   
        if ('0' <= requestedOperation && requestedOperation <= '9') {
            current = current * 10 + Integer.valueOf("" + requestedOperation);
            return formatter.format(current);
        }
        switch (lastOperationRequested) {
        case '+':
            stored += current;
            break;
        case '-':
            stored -= current;
            break;
        case '/':
            stored /= current;
            break;
        case 'x':
            stored *= current;
            break;
        case 'C':
            stored = current;
            break;
        }
        lastOperationRequested = requestedOperation;
        current = 0;
        if (requestedOperation == 'C') {
            stored = 0;
        } else if (requestedOperation == '=') {
        	formatter = new DecimalFormat("#0.##");
        }
        return formatter.format(stored);
    }
}
