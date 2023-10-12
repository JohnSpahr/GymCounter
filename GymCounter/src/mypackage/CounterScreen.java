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
	//declare public variables...
	public int setCount = 0;
	public int repsInput = 0;
	public LabelField setsLbl = new LabelField();
	public LabelField totalRepsLbl = new LabelField();
	public LabelField stopwatchLbl = new LabelField();
	public ButtonField addSet = new ButtonField(ButtonField.CONSUME_CLICK);
	public ButtonField undoSet = new ButtonField(ButtonField.CONSUME_CLICK);
	
	public CounterScreen(int reps) {
		//set screen title
		setTitle("#1 Gym Counter");

		//set public reps variable to reps integer transfered to this screen
		repsInput = reps;
		
		//create vertical field manager for content and set padding
		VerticalFieldManager counterContent = new VerticalFieldManager();
		counterContent.setPadding(15, 15, 15, 15);
		
		//create label to display reps per set
		LabelField repsLbl = new LabelField();

		//set label text, convert reps integer to string
		repsLbl.setText("Reps per set: " + String.valueOf(reps));
		
		//set total reps label (the combined amount of reps, reps * sets)
		totalRepsLbl.setText("Total Reps: 0");
		
		//set sets label to 0
		setsLbl.setText("Sets: 0");

		try {
	        FontFamily alphaSerifFamily; //create font family
			alphaSerifFamily = FontFamily.forName("BBSansSerifSquare"); //get font family loaded on OS, you know the drill
			Font mainFont = alphaSerifFamily.getFont(Font.BOLD, 14, Ui.UNITS_pt); //main font; bold, 14pt
			Font secondaryFont = alphaSerifFamily.getFont(Font.BOLD, 10, Ui.UNITS_pt); //secondary font; bold, 10pt
	        
			//apply fonts...
			setsLbl.setFont(mainFont);
	        totalRepsLbl.setFont(secondaryFont);
			stopwatchLbl.setFont(mainFont);	        
		} catch (ClassNotFoundException e) {
			//catch exception and log it
			e.printStackTrace();
		} 
			
		//create horizontal field manager to display buttons side-by-side
		HorizontalFieldManager buttonList = new HorizontalFieldManager();
		
		//handle add button press...
		FieldChangeListener addListener = new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
            	if (setCount < 101)
            	{
					//if fewer than 101 sets (which is way higher than needed), execute code below
            		setCount++; //+1 set
            		setsLbl.setText("Sets: " + String.valueOf(setCount)); //update setsLbl to reflect current amount of sets
            		int totalReps = setCount * repsInput; //calculate total reps
            		totalRepsLbl.setText("Total Reps: " + String.valueOf(totalReps)); //update total reps label
            		
					//enable undo button
            		undoSet.setEnabled(true);
            	}
            }
        };

		//handle undo button press...
        FieldChangeListener undoListener = new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
            	if (setCount > 0)
            	{
					//if more than 0 sets, execute code below...
            		setCount--; //decrease set count by one
            		if (setCount == 0)
            		{
						//if setCount is equal to zero, disable the undo button (we don't want -1 sets!)
            			undoSet.setEnabled(false);
            		}

            		setsLbl.setText("Sets: " + String.valueOf(setCount)); //update setsLbl to reflect current amount of sets
            		int totalReps = setCount * repsInput; //calculate total reps
            		totalRepsLbl.setText("Total Reps: " + String.valueOf(totalReps)); //update total reps label
            	}
            }
        };

		//set add button text and assign listener
		addSet.setLabel("+1 Set");
		addSet.setChangeListener(addListener);
		
		//set undo button text and assign listener
		undoSet.setLabel("Undo Add");
		undoSet.setChangeListener(undoListener);
		undoSet.setEnabled(false); //disable undo button by default
		
		//add buttons to our horizontal field manager
		buttonList.add(addSet);
		buttonList.add(undoSet);
		
		//top separator; 15px margin top
		SeparatorField topSeparator = new SeparatorField();
		topSeparator.setMargin(15, 0, 0, 0);
		
		//bottom separator; 15px margin bottom
		SeparatorField bottomSeparator = new SeparatorField();
		bottomSeparator.setMargin(0, 0, 15, 0);

		//set stopwatch label text to default (0 minutes, 0 seconds)
		stopwatchLbl.setText("00:00");
		
		//add all the stuff above to our vertical field manager...
		counterContent.add(repsLbl);
		counterContent.add(topSeparator);
		counterContent.add(setsLbl);
		counterContent.add(totalRepsLbl);
		counterContent.add(bottomSeparator);
		counterContent.add(buttonList);

		//add our vertical field manager to the screen
		add(counterContent);		
	}

	//intercept back button press...
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

	//custom menu item for start/stop stopwatch
    private MenuItem stopwatchControl = new MenuItem("Start/Stop", 0, 0)
	{
        public void run()
		{
        	//TODO: add le code & rename "mypackage"
        }
    };  

	//custom menu item for restart
    private MenuItem restartSet = new MenuItem("Restart", 0, 0)
	{
        public void run()
		{
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
    
	//custom menu item for closing screen
    private MenuItem closeScreen = new MenuItem("Back", 0, 0)
	{
        public void run()
		{
        	//close screen if user confirms when prompted...
        	int ans =  Dialog.ask(Dialog.D_YES_NO, "Are you sure you that you want to go back? Your progress will be lost.", Dialog.YES);

            if (ans == Dialog.YES)
            {
            	//if yes, pop screen
            	Ui.getUiEngine().popScreen(getScreen());
            }
        }
    };  
    
    protected void makeMenu(Menu menu, int instance)
	{
        //add custom menu items and separator to distinguish from system menu items
    	menu.add(stopwatchControl);
		menu.add(restartSet);
        menu.add(closeScreen);
        menu.addSeparator();
    }
}
