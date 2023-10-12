package mypackage;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.FullScreen;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class CounterScreen extends MainScreen {
	
	public int setCount = 0;
	public int repsInput = 0;
	public LabelField setsLbl = new LabelField();
	public LabelField totalRepsLbl = new LabelField();
	public ButtonField addSet = new ButtonField(ButtonField.CONSUME_CLICK);
	public ButtonField undoSet = new ButtonField(ButtonField.CONSUME_CLICK);
	
	public CounterScreen(int reps) {
		super(FullScreen.VERTICAL_SCROLL | FullScreen.VERTICAL_SCROLLBAR);
		setTitle("Gym Counter");
		repsInput = reps;
		VerticalFieldManager counterContent = new VerticalFieldManager();
		counterContent.setPadding(15, 15, 15, 15);
		
		LabelField repsLbl = new LabelField();
		repsLbl.setText("Reps per set: " + String.valueOf(reps));
		
		totalRepsLbl.setText("Total Reps: 0");
		
		setsLbl.setText("Sets: 0");
		try {
	        FontFamily alphaSerifFamily;
			alphaSerifFamily = FontFamily.forName("BBSansSerifSquare");
			Font setFont = alphaSerifFamily.getFont(Font.BOLD, 14, Ui.UNITS_pt);
			Font totalRepsFont = alphaSerifFamily.getFont(Font.BOLD, 10, Ui.UNITS_pt);
	        setsLbl.setFont(setFont);
	        totalRepsLbl.setFont(totalRepsFont);
	        
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			
		HorizontalFieldManager buttonList = new HorizontalFieldManager();
		
		FieldChangeListener addListenere = new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
            	if (setCount < 100)
            	{
            		setCount++;
            		setsLbl.setText("Sets: " + String.valueOf(setCount));
            		int totalReps = setCount * repsInput;
            		totalRepsLbl.setText("Total Reps: " + String.valueOf(totalReps));
            		
            		undoSet.setEnabled(true);
            	}
            }
        };

        FieldChangeListener undoListener = new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
            	if (setCount > 0)
            	{
            		setCount--;
            		if (setCount == 0)
            		{
            			undoSet.setEnabled(false);
            		}

            		setsLbl.setText("Sets: " + String.valueOf(setCount));
            		int totalReps = setCount * repsInput;
            		totalRepsLbl.setText("Total Reps: " + String.valueOf(totalReps));
            	}
            }
        };

		addSet.setLabel("+1 Set");
		addSet.setChangeListener(addListenere);
		
		undoSet.setLabel("Undo Add");
		undoSet.setChangeListener(undoListener);
		undoSet.setEnabled(false);
		
		buttonList.add(addSet);
		buttonList.add(undoSet);
		
		SeparatorField topSeparator = new SeparatorField();
		topSeparator.setMargin(15, 0, 0, 0);
		
		SeparatorField bottomSeparator = new SeparatorField();
		bottomSeparator.setMargin(0, 0, 15, 0);
		
		counterContent.add(repsLbl);
		counterContent.add(topSeparator);
		counterContent.add(setsLbl);
		counterContent.add(totalRepsLbl);
		counterContent.add(bottomSeparator);
		counterContent.add(buttonList);

		
		add(counterContent);
		
		
	}

    public boolean onClose() 
    {
    	//close screen if user confirms when prompted...
    	int ans =  Dialog.ask(Dialog.D_YES_NO, "Are you sure you that you want to go back? Your progress will be lost.", Dialog.YES);

        if (ans == Dialog.YES)
        {
        	//if yes, pop screen
        	Ui.getUiEngine().popScreen(getScreen());
        }
    	//remove save changes dialog on close
    	return true;
    }

    private MenuItem restartSet = new MenuItem("Restart", 100, 10){
        public void run(){
        	//reset sets and reps values if user confirms when prompted...
        	int ans =  Dialog.ask(Dialog.D_YES_NO, "Are you sure you that you want to restart? Your progress will be lost.", Dialog.YES);

            if (ans == Dialog.YES)
            {
            	//if yes, reset values
	        	setCount = 0;
	    		setsLbl.setText("Sets: 0");
	    		totalRepsLbl.setText("Total Reps: 0");
            }
        }
    };  
    
    private MenuItem closeScreen = new MenuItem("Back", 100, 10){
        public void run(){
        	//close screen if user confirms when prompted...
        	int ans =  Dialog.ask(Dialog.D_YES_NO, "Are you sure you that you want to go back? Your progress will be lost.", Dialog.YES);

            if (ans == Dialog.YES)
            {
            	//if yes, pop screen
            	Ui.getUiEngine().popScreen(getScreen());
            }
        }
    };  
    
    protected void makeMenu(Menu menu, int instance){
    	menu.add(restartSet);
        menu.add(closeScreen);
        menu.addSeparator();
    }
}
