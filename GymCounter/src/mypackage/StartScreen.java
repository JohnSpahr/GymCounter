package mypackage;

import net.rim.device.api.ui.Font;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.text.NumericTextFilter;

public final class StartScreen extends MainScreen
{
    public EditField repField = new EditField(); //text input field for entering number of reps
    
    public StartScreen()
    {        
        //set screen title    
        setTitle("Gym Counter");

        //create vertical field manager (basically a layout that displays items top-down)
        VerticalFieldManager mainField = new VerticalFieldManager();
        
        //set padding for more polished look
        mainField.setPadding(15, 15, 15, 15);

        //create label for the title of the app, set text to "Gym Counter"
        LabelField mainTitle = new LabelField();
        mainTitle.setText("Gym Counter");

        //attempt to load font that comes with OS
		try {
	        FontFamily alphaSerifFamily; //create font family variable
			alphaSerifFamily = FontFamily.forName("BBSansSerifSquare"); //load "BBSansSerifSquare" font family
			Font titleFont = alphaSerifFamily.getFont(Font.BOLD, 18, Ui.UNITS_pt); //create font. bold, 18pt
	        mainTitle.setFont(titleFont); //set title font
		} catch (ClassNotFoundException e) {
			//catch exception if font not available
			e.printStackTrace();
		} 

        //create label for caption. essentially tells user the purpose of this app
        LabelField mainCaption; //TEST THIS
        mainCaption.setText("Keep track of your progress at the gym!");

        //create separator with top and bottom margin of 15px
        SeparatorField separator = new SeparatorField();
        separator.setMargin(15, 0, 15, 0);
        
        //create label for input description
        LabelField startTxt = new LabelField();
        startTxt.setText("Enter the number of reps per set below...");
        
        //set the publicly-declared "repField" variable to only accept numbers and be labeled as "Reps: "
        NumericTextFilter numericFilter = new NumericTextFilter();
        repField.setFilter(numericFilter);
        repField.setLabel("Reps: ");
        
        //handle "Start" button press (see below)
        FieldChangeListener startListener = new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                //try catch block in case input is out of bounds
            	try
            	{
                    //convert text input to integer from string
                	int repCount = Integer.parseInt(repField.getText());

                    //create new screen
	            	CounterScreen counterScreen = new CounterScreen(repCount);

                    //push screen to stack
	            	Ui.getUiEngine().pushScreen(counterScreen);
            	}
            	catch (NumberFormatException e)
            	{
                    //if invalid input exception is thrown, show a dialog telling the user to correct their error
            		Dialog.alert("Please enter a number of reps.");
            	}
            }
        };

        //create button field
        //CONSUME_ClICK is important because it prevents quick menu from showing up when button is clicked
        ButtonField startBtn = new ButtonField(ButtonField.CONSUME_CLICK);

        //set label and margin
        startBtn.setLabel("Start Workout");
        startBtn.setMargin(15, 0, 0, 0);

        //handle clicks by executing field listener above
        startBtn.setChangeListener(startListener);
        
        //add fields to vertical field manager...
        mainField.add(mainTitle);
        mainField.add(mainCaption);
        mainField.add(separator);
        mainField.add(startTxt);
        mainField.add(repField);
        mainField.add(startBtn);

        //add vertical field manager to screen
        add(mainField);
    }
    
    public boolean onClose() 
    {
    	//remove save changes dialog on close
    	this.close();
    	return true;
    }
     
    //create menu item titled "About"
    private MenuItem aboutApp = new MenuItem("About", 0, 0)
    {
        public void run()
        {
            //on click, show about info dialog
        	Dialog.alert("Gym Counter\nVersion 1.0.0\n\nCreated by John Spahr\n\njohnspahr.org\nlunarproject.org");
        }
    };  
    
    protected void makeMenu(Menu menu, int instance)
    {
        //add custom menu items and separator to distinguish from system menu items
        menu.add(aboutApp);
        menu.addSeparator();
    }
}
