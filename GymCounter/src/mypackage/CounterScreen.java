package mypackage;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class CounterScreen extends MainScreen {
	//declare public variables...
	public int setCount = 0;
	public int repsInput = 0;
	public int isRunning = 1; //stopwatch status
	public int resetStopwatch = 0; //tell stopwatch to reset
	public String secStr; //seconds count string
	public String minStr; //minutes count string
	public LabelField setsLbl = new LabelField();
	public LabelField totalRepsLbl = new LabelField();
	public LabelField stopwatchLbl = new LabelField();
	public ButtonField addSet = new ButtonField(ButtonField.CONSUME_CLICK);
	public ButtonField undoSet = new ButtonField(ButtonField.CONSUME_CLICK);
	
	public CounterScreen(int reps) {
		//set screen title
		setTitle("Workout Tracker");

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
            		addSet.setFocus();
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
		
		//top separator; 15px margin top, 5px margin bottom
		SeparatorField topSeparator = new SeparatorField();
		topSeparator.setMargin(15, 0, 5, 0);
		
		//bottom separator; 15px margin bottom, 5px margin top
		SeparatorField bottomSeparator = new SeparatorField();
		bottomSeparator.setMargin(5, 0, 15, 0);
		
		//bottom separator; 15px margin top and bottom
		SeparatorField stopwatchSeparator = new SeparatorField();
		stopwatchSeparator.setMargin(15, 0, 15, 0);

		//set stopwatch label text to default (0 minutes, 0 seconds)
		stopwatchLbl.setText("00:00");
		
		//add all the stuff above to our vertical field manager...
		counterContent.add(repsLbl);
		counterContent.add(topSeparator);
		counterContent.add(setsLbl);
		counterContent.add(totalRepsLbl);
		counterContent.add(bottomSeparator);
		counterContent.add(buttonList);
		counterContent.add(stopwatchSeparator);
		counterContent.add(stopwatchLbl);

		//add our vertical field manager to the screen
		add(counterContent);	
		
		//once everything has been added, start timer that runs in background thread
		Timer stopwatch = new Timer();
		stopwatch.schedule(new StopwatchTask(), 50, 50); //update every 50ms
	}
	
	class StopwatchTask extends TimerTask {
		//variables for tracking time...
		long currentTime; //keeps track of the current time in ms, refreshes every 50ms
		long startTime = System.currentTimeMillis(); //start time, keeps track of time when stopwatch starts
		long timeElapsed = 0; //keeps track of time elapsed so far when stopwatch is paused
		long diff = 0; //used to calculate different between the current time and the time when the stopwatch was started
		long lastSec = 0; //tracks last second value reported to user so UI thread is not refreshed needlessly
		
		public void run() {
			synchronized (Application.getEventLock()) {
				//code to execute for timer...
				if (isRunning == 1)
				{
					//if stopwatch is enabled...
					currentTime = System.currentTimeMillis(); //set current time to the current time in ms
					//find difference between the current time and the time that the timer started. add time elapsed to account for pauses
					diff = (currentTime - startTime) + timeElapsed;
					
					//calculate minutes and seconds elapsed from difference
					long min = diff / 60000;
					long sec = (diff % 60000) / 1000;
					
					//convert minute and second values we just calculated to strings to be displayed
					minStr = new Long(min).toString();
					secStr = new Long(sec).toString();
					
					//if minutes and seconds values are below 10, add 0s in front of numbers to accurately represent them (example, 4:9 ---> 04:09)s
					if (min < 10)
					{
						minStr = "0" + minStr;
					}
					if (sec < 10)
					{
						secStr = "0" + secStr;
					}
		
					//if last recorded second is different than current second, update UI thread to show updated time...
					if (lastSec != sec)
					{
						UiApplication.getUiApplication().invokeLater(new Runnable() {
						    public void run() {
						    	//use UI thread to set label text
								stopwatchLbl.setText(minStr + ":" + secStr);
						    }
						});
						
						//update lastSec value
						lastSec = sec;
					}
				}
				else
				{
					//if stopwatch is paused...
					if (timeElapsed != diff)
					{
						//if it hasn't already been updated, set timeElapsed to the latest calculated difference between currentTime and startTime 
						timeElapsed = diff;
					}
					
					//if stopwatch reset variable is set to 1, user has requested to reset it
					if (resetStopwatch == 1)
					{
						timeElapsed = 0;
						startTime = System.currentTimeMillis();
						resetStopwatch = 0; //can't keep this on!
						isRunning = 1;
					}
					
					//update startTime to currentTime so stopwatch remains accurate when resumed
					startTime = System.currentTimeMillis();
				}
		 	}
		}
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
        	//toggle stopwatch. start/stop...
        	if (isRunning == 1)
        	{
        		isRunning = 0;
        	}
        	else
        	{
        		isRunning = 1;
        	}
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
	    		isRunning = 0;
	    		resetStopwatch = 1; //reset!
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
