package com.nokor.frmk.vaadin.ui.widget.component.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.nokor.frmk.vaadin.ui.widget.component.client.mask.ConcateMask;
import com.nokor.frmk.vaadin.ui.widget.component.client.mask.DayMask;
import com.nokor.frmk.vaadin.ui.widget.component.client.mask.HourMask;
import com.nokor.frmk.vaadin.ui.widget.component.client.mask.MinuteMask;
import com.nokor.frmk.vaadin.ui.widget.component.client.mask.MonthMask;
import com.nokor.frmk.vaadin.ui.widget.component.client.mask.YearMask;
import com.vaadin.client.ui.VPopupCalendar;


/**
 * @author ky.nora
 */
public class AutoDateFieldWidget extends VPopupCalendar {

    
    protected String mask;
	
	private char placeholder = '_';
	
	private StringBuilder string;
	private List<ConcateMask> maskTest;
	private List<Integer> nullablePositions;
	
	private char emptyChar = '\0';

	/**
	 * Key press that might be ignored by event handlers
	 */
	private char[] ignoredKeys = new char[] {
			KeyCodes.KEY_BACKSPACE,
			KeyCodes.KEY_TAB,
			KeyCodes.KEY_DELETE,  
			KeyCodes.KEY_END,
			KeyCodes.KEY_ENTER,
			KeyCodes.KEY_ESCAPE,
			KeyCodes.KEY_HOME,
			KeyCodes.KEY_LEFT,
			KeyCodes.KEY_PAGEDOWN,
			KeyCodes.KEY_PAGEUP,
			KeyCodes.KEY_RIGHT
	};
	

    public AutoDateFieldWidget() {
        super();

		Arrays.sort(ignoredKeys);
		text.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
					deleteTextOnKeyDown(event);
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
					deleteTextOnKeyDown(event);
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
					setCursorPositionAndPreventDefault(event,getNextPosition(text.getCursorPos()));
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
					setCursorPositionAndPreventDefault(event, getPreviousPosition(text.getCursorPos()));
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_HOME && !event.isShiftKeyDown()) {
					setCursorPositionAndPreventDefault(event, getPreviousPosition(0));
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_END && !event.isShiftKeyDown()) {
					setCursorPositionAndPreventDefault(event, getLastPosition());
				} else if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					onChange(null);
				}
			}
		});

		
		text.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (!isKeyIgnored(event)) {
					if (text.getCursorPos() < maskTest.size()) {
						validateAndShowUserInput(event);
					} else {
						if(event.getCharCode() != emptyChar) {
							text.cancelKey();
						}
					}
				}
				
			}
		});
		
		text.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
                if (text.getText().isEmpty()) {
                    setMask(getFormatString());
                    // text.setCursorPos(getAvaliableCursorPos(0));
                    // chrom bugged on focus and click, remove timer after chrom fixed it.
                    (new Timer() {
                           @Override
                           public void run() { text.setCursorPos(getAvaliableCursorPos(0));
                           }
                        }).schedule(0);
                } else {
                    final String bkText = text.getText();
                    setMask(getFormatString());
                    text.setText(bkText);
                    string = new StringBuilder(bkText);
                }
			}
		});
		
        text.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (isFieldIfIncomplete()) {
                    setText("");
                    onChange(null);
                }
            }
        });
        
        sinkEvents(Event.ONPASTE);
        updateStyleNames();
    }

    @Override 
	public void onBrowserEvent(Event event) { 
	    if(event.getTypeInt() == Event.ONPASTE) {
	    	super.setText("");
	    	super.onBrowserEvent(event);
	    	Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					final String bkText = text.getText();
	                setMask(getFormatString());
	                text.setText(bkText);
	                string = new StringBuilder(bkText);
				}
			});
	    	
	    } else {
	    	super.onBrowserEvent(event);
	    }
    }

    /**
     * For internal use only. May be removed or replaced in the future.
     * 
     * @see com.vaadin.client.ui.VTextualDate#buildDate()
     */
    @Override
    public void buildDate() {
        // Save previous value
        //String previousValue = getText();
        super.buildDate();
        string = new StringBuilder(text.getText());
        
        // Restore previous value if the input could not be parsed
        /*
        if (!parsable) {
            setText(previousValue);
        }
        */

        // superclass sets the text field independently when building date
        text.setEnabled(isEnabled() && isTextFieldEnabled());
    }
    
	public String getMask() {
		return mask;
	}
    
	public void setMask(String mask) {
		this.mask = mask;
		maskTest = new ArrayList<ConcateMask>(mask.length());
		nullablePositions = new ArrayList<Integer>();
		string = new StringBuilder();
		
		configureUserView();
		getNextPosition(-1);
	}
	
	public void setPlaceHolder(char placeHolder) {
		this.placeholder = placeHolder;
	}

	private void configureUserView() {
		for (int index = 0; index < mask.length(); index++) {
			char character = mask.charAt(index);
			createCorrectMaskAndPlaceholder(character, index);
		} 
		super.setText(string.toString());
	}

	private void createCorrectMaskAndPlaceholder(char character, int index) {
		switch (character) {
		case 'M':
			addMaskStrategyAndCharacterPlaceHolder(new MonthMask(),placeholder);
			break;
		case 'y':
			addMaskStrategyAndCharacterPlaceHolder(new YearMask(),placeholder);
			break;
		case 'd':
			addMaskStrategyAndCharacterPlaceHolder(new DayMask(),placeholder);
			break;
		case 'h':
			addMaskStrategyAndCharacterPlaceHolder(new HourMask(),placeholder);
			break;
		case 'm':
			addMaskStrategyAndCharacterPlaceHolder(new MinuteMask(),placeholder);
			break;
		case 's':
			addMaskStrategyAndCharacterPlaceHolder(new MinuteMask(),placeholder);
			break;
		default:
			addMaskStrategyAndCharacterPlaceHolder(null, character);
			break;
		}
	}
	
	private void addMaskStrategyAndCharacterPlaceHolder(ConcateMask maskStrategy, char characterPlaceholder) {
		maskTest.add(maskStrategy);
		string.append(characterPlaceholder);
	}

	private int getNextPosition(int position) {
		while (++position < maskTest.size() && maskTest.get(position) == null)
			;
		return position;
	}

	int getPreviousPosition(int position) {
		while (--position >= 0 && maskTest.get(position) == null)
			;
		if (position < 0)
			return getNextPosition(position);
		return position;
	}

	private int getLastPosition() {
		return text.getText().length() + 1;
	}

	public void onKeyPress(KeyPressEvent event) {
		if (!isKeyIgnored(event)) {
			if (text.getCursorPos() < maskTest.size()) {
				validateAndShowUserInput(event);
			} else {
				if(event.getCharCode() != emptyChar) {
					text.cancelKey();
				}
			}
		}
	}

	private boolean isKeyIgnored(KeyPressEvent event) {
		return (
				event.getCharCode() == emptyChar ||
				event.isShiftKeyDown() && isAnySelectionTextModifiedKey(event.getCharCode()) ||
				isIgnoredKey(event.getCharCode()) ||
				isPasteShorcutPressed(event) ||
				event.isAnyModifierKeyDown() && !event.isShiftKeyDown()
				);
	}
	
	private boolean isIgnoredKey(char charCode) {
		return Arrays.binarySearch(ignoredKeys, charCode) >= 0;
	}
	
	private boolean isAnySelectionTextModifiedKey(char charCode) {
		return (charCode == KeyCodes.KEY_END || charCode == KeyCodes.KEY_HOME);
	}
	
	private boolean isPasteShorcutPressed(KeyPressEvent event) {
		return event.isControlKeyDown() && (Character.toLowerCase(event.getCharCode()) == 'v');
	}
	
	private String concateNearestMask(char newChar, int newPos) {
		int startPos = newPos;
		int endPos = newPos;
		StringBuilder strB = new StringBuilder(string);
		strB.setCharAt(newPos, newChar);
		
		for (; startPos >= 0; startPos--) {
			if (maskTest.get(startPos) == null || strB.charAt(startPos) == placeholder)
				break;
		}
		startPos++;
		
		for (; endPos < maskTest.size(); endPos++) {
			if (maskTest.get(endPos) == null || strB.charAt(endPos) == placeholder)
				break;
		}
		
		return strB.substring(startPos, endPos);
	}

	private void validateAndShowUserInput(KeyPressEvent event) {
		ConcateMask maskStrategy = maskTest.get(getAvaliableCursorPos(text.getCursorPos()));
		if (maskStrategy != null) {
			if(event.getCharCode() == ' ' && nullablePositions.contains(text.getCursorPos())) {
				showUserInput(' ');
			}
			else if (maskStrategy.isValid(event.getCharCode(),
					concateNearestMask(event.getCharCode(), text.getCursorPos()))) {
				char character = maskStrategy.getChar(event.getCharCode());
				showUserInput(character);
				if (!isFieldIfIncomplete()) {
					onChange(null);
				}
			}
			event.preventDefault();
		}
	}
	
	
	
	private void showUserInput(char character) {
		if(text.getText().isEmpty()) {
			configureUserView();
		}
		if(text.getText().length() < maskTest.size()) {
			
		}
		int currentPosition = getAvaliableCursorPos(text.getCursorPos());
		string.setCharAt(currentPosition, character);
		super.setText(string.toString());
		text.setCursorPos(getNextPosition(currentPosition));
	}

	
	private void deleteTextOnKeyDown(KeyDownEvent event) {
		if(!text.getSelectedText().isEmpty()) {
			String selected = text.getSelectedText();
			for(int i=(selected.length()-1); i>=0; i--) {
				int index = text.getText().indexOf(Character.toString(selected.charAt(i)));
				deleteCharacter(index);
			}
			text.setCursorPos(0);
		} else {
			if(event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
				deleteCharacterAndPositionCursor(event, text.getCursorPos());
			} else {
				deleteCharacterAndPositionCursor(event, getPreviousPosition(text.getCursorPos()));
			}
		}
	}

	private void deleteCharacterAndPositionCursor(KeyDownEvent event, int position) {
		deleteCharacter(position);
		setCursorPositionAndPreventDefault(event, position);
	}

	private void setCursorPositionAndPreventDefault(KeyDownEvent event,int position) {
		text.setCursorPos(position);
		event.preventDefault();
	}

	private void deleteCharacter(int position) {
		ConcateMask maskStrategy = maskTest.get(position);
		if (maskStrategy != null) {
			string.setCharAt(position, placeholder);
			super.setText(string.toString());
		}
	}
	
	public int getAvaliableCursorPos(int desiredPosition) {
		int i = desiredPosition;
		for(;i<maskTest.size(); i++) {
			if(maskTest.get(i) != null) {
				break;
			}
		}
		return i;
	}
	private boolean isFieldIfIncomplete() {
		for (int index = 0; index < string.length(); index++) {
			char character = string.charAt(index);
			if (maskTest.get(index) != null && character == placeholder) {
				return true;
			}
		}
		return false;
	}
}