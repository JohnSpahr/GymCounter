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

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class StartScreen extends MainScreen
{
    /**
     * Creates a new StartScreen object
     */
    public EditField repField = new EditField();
    
    public StartScreen()
    {        
        // Set the displayed title of the screen       
        setTitle("Gym Counter");
        VerticalFieldManager mainField = new VerticalFieldManager();
        		
        mainField.setPadding(15, 15, 15, 15);
        LabelField mainTitle = new LabelField();
        mainTitle.setText("Gym Counter");
		try {
	        FontFamily alphaSerifFamily;
			alphaSerifFamily = FontFamily.forName("BBSansSerifSquare");
			Font titleFont = alphaSerifFamily.getFont(Font.BOLD, 18, Ui.UNITS_pt);
	        mainTitle.setFont(titleFont);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        LabelField mainCaption = new LabelField();
        mainCaption.setText("Keep track of your progress at the gym!");
        
        LabelField startTxt = new LabelField();
        startTxt.setText("Enter the number of reps per set below...");
        
        NumericTextFilter numericFilter = new NumericTextFilter();
        repField.setFilter(numericFilter);
        repField.setLabel("Reps: ");
        
        FieldChangeListener listener = new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
            	try
            	{
                	int repCount = Integer.parseInt(repField.getText());
	            	CounterScreen counterScreen = new CounterScreen(repCount);
	            	Ui.getUiEngine().pushScreen(counterScreen);
            	}
            	catch (NumberFormatException e)
            	{
            		Dialog.alert("Please enter a number of reps.");
            	}
            }
        };

        ButtonField startBtn = new ButtonField(ButtonField.CONSUME_CLICK);
        startBtn.setLabel("Start Workout");
        startBtn.setMargin(15, 0, 0, 0);
        startBtn.setChangeListener(listener);

        SeparatorField separator = new SeparatorField();
        separator.setMargin(15, 0, 15, 0);
        
        mainField.add(mainTitle);
        mainField.add(mainCaption);
        mainField.add(separator);
        mainField.add(startTxt);
        mainField.add(repField);
        mainField.add(startBtn);
        add(mainField);
    }
    
    public boolean onClose() 
    {
    	//remove save changes dialog on close
    	this.close();
    	return true;
    }
    
    private MenuItem aboutApp = new MenuItem("About", 100, 10){
        public void run(){
            // insert code thats to be done when menu is selected here here
        	Dialog.alert("Gym Counter\nVersion 1.0.0\n\nCreated by John Spahr\n\njohnspahr.org\nlunarproject.org");
        }
    };  
    
    protected void makeMenu(Menu menu, int instance){
        menu.add(aboutApp);
        menu.addSeparator();
    }
}
