/**************************************************************
 ** Project 2: GrahamsTextEditor.java
 ** Description: Application logic for a text editor
 ** Name: Graham Thomas
 ** Student Number: 1479585
**************************************************************/

// import required classes
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.OutOfMemoryError;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.Font;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

// declare Graham's text editor class to contain application logic
public class GrahamsTextEditor
{

	/**************************************************************
	 ** Declare and initialise constants                         **
	 **************************************************************/
	 
	public static final String APPLICATION_NAME= "Graham's Text Editor"; // name of application
	public static final String DEFAULT_FILE_NAME = "New Text File.txt"; // default name of new file before being saved
	public static final String[] ALLOWED_TEXT_FILE_EXTENSIONS = { "txt" }; // array of file extensions to be used with text file extension filter
	public final File DEFAULT_FILE = new File( DEFAULT_FILE_NAME ); // default file to be used before document is saved
	
	// message to be displayed when user clicks the help --> about button. Using line.separator so that message isn't platform specific
	public static final String HELP_ABOUT_MESSAGE = String.format( "%s was developed by Graham Thomas%sCopyright 2014", APPLICATION_NAME, System.getProperty( "line.separator" ) );
	
	public static final int SEARCH_FORWARDS = 0; // integer to represent the forwards search type
	public static final int SEARCH_BACKWARDS = 1; // integer to represent the backwards search type
	public static final FileNameExtensionFilter TEXT_FILE_EXTENSION_FILTER = new FileNameExtensionFilter( "Text Files", ALLOWED_TEXT_FILE_EXTENSIONS ); // file extension filter for text files

	/**************************************************************
	 ** Declare variables                                        **
	 **************************************************************/
	
	private JFileChooser fileChooser; // declare file chooser
	private File currentFile; // variable for holding the current file being worked on
	private boolean fileSaved; // flag to indicate whether the current file has been saved or not
	private TextEditorGUI textEditor; // main text editor GUI
	private SearchReplaceGUI searchReplaceGUI; // search and replace GUI
	private FontChooseGUI fontChooseGUI; // font chooser GUI
	private int fwdSearchStartPosition; // position where the next forwards search will take place from
	private int bckSearchStartPosition; // position where the next backwards search will take place from
	
	// begin constructor
	public GrahamsTextEditor()
	{
		/**************************************************************
	     ** Initialise variables                                     **
	     **************************************************************/
	
		// initialise main text editor GUI by passing the application name and default filename to be displayed as the window title
		textEditor = new TextEditorGUI( APPLICATION_NAME + " - " + DEFAULT_FILE_NAME );
		textEditor.setSize( 1000, 600 ); // set the size of the window
		textEditor.setVisible( true ); // make the window visible
		
		searchReplaceGUI = new SearchReplaceGUI(); // initialise the search and replace GUI
		searchReplaceGUI.setVisible( false ); // set the search and replace GUI to be invisible initially
		
		fontChooseGUI = new FontChooseGUI(); // initialise the font chooser GUI
		fontChooseGUI.setVisible( false ); // set the font chooser GUI to be invisible initially
		
		fileChooser = new JFileChooser(); // initialise file chooser
		fileChooser.addChoosableFileFilter( TEXT_FILE_EXTENSION_FILTER ); // add text file extension filter to list of filters available to file chooser
		fileChooser.setFileFilter( TEXT_FILE_EXTENSION_FILTER ); // set the currently selected file filter to be the text file extension filter
		fileSaved = true; // set the current file to initially be in a saved state as no text has been entered yet
		resetSearchStartPositions(); // reset the forwards and backwards start positions to default values
		
		/**************************************************************
	     ** Register event handlers                                  **
	     **************************************************************/
		
		// register event handlers for main text editor GUI
		textEditor.fileNewRegisterHandler( new FileNewEventHandler() );
		textEditor.fileOpenRegisterHandler( new FileOpenEventHandler() );
		textEditor.fileSaveRegisterHandler( new FileSaveEventHandler() );
		textEditor.fileSaveAsRegisterHandler( new FileSaveAsEventHandler() );
		textEditor.fileExitRegisterHandler( new FileExitEventHandler() );
		textEditor.editCopyRegisterHandler( new EditCopyEventHandler() );
		textEditor.editCutRegisterHandler( new EditCutEventHandler() );
		textEditor.editPasteRegisterHandler( new EditPasteEventHandler() );
		textEditor.editSearchRegisterHandler( new EditSearchEventHandler() );
		textEditor.editReplaceRegisterHandler( new EditReplaceEventHandler() );
		textEditor.formatFontRegisterHandler( new FormatFontEventHandler() );
		textEditor.formatWordWrapRegisterHandler( new FormatWordWrapEventHandler() );
		textEditor.helpAboutRegisterHandler( new HelpAboutEventHandler() );		
		textEditor.textAreaCaretEventHandler( new TextAreaCaretListener() );
		
		// polymorphically pass TextAreaKeyListener object which extends KeyAdapter which in turn implements KeyListener
		textEditor.textAreaKeyEventHandler( new TextAreaKeyListener() );
		
		// create anonymous inner class as the event handler for main text editor window events 
		textEditor.addWindowListener(
			new WindowAdapter() // use WindowAdapter class so we only have to override the methods we are interested in
			{
				public void windowClosing( WindowEvent windowEvent ) // when text editor window is closing
				{
					attemptExit(); // attempt to exit the application
				} // end method windowClosing
			} // end WindowAdapter class definition
		); // end registering of window listener for main text editor GUI
		
		// register event handlers for the search and replace GUI
		searchReplaceGUI.nextButtonRegisterHandler( new SearchNextButtonEventHandler() );
		searchReplaceGUI.previousButtonRegisterHandler( new SearchPreviousButtonEventHandler() );
		searchReplaceGUI.replaceButtonRegisterHandler( new ReplaceButtonEventHandler() );
		searchReplaceGUI.replaceAllButtonRegisterHandler( new ReplaceAllButtonEventHandler() );
		searchReplaceGUI.searchFieldRegisterHandler( new SearchFieldEventHandler() );

		// register event handlers for the font chooser GUI
		fontChooseGUI.fontNameListRegisterHandler( new FontNameListSelectionListener() );
		fontChooseGUI.fontSizeListRegisterHandler( new FontSizeListSelectionListener() );
		fontChooseGUI.okButtonRegisterHandler( new FontOkButtonEventHandler() );
		fontChooseGUI.cancelButtonRegisterHandler( new FontCancelButtonEventHandler() );
	} // end constructor
	
	/**************************************************************
	 ** Define event handler inner classes                       **
	 **************************************************************/
	
	// event handler inner class for the File --> New menu item
	private class FileNewEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			attemptNew(); // attempt to create a new file
		} // end method actionPerformed
	} // end inner class FileNewEventHandler
	
	// event handler inner class for the File --> Open menu item
	private class FileOpenEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			attemptOpen(); // attempt to open a new file
		} // end method actionPerformed
	} // end inner class FileOpenEventHandler
	
	// event handler inner class for the File --> Save menu item
	private class FileSaveEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			attemptSave(); // attempt to save a file
		} // end method actionPerformed
	} // end inner class FileSaveEventHandler
	
	// event handler inner class for the File --> Save As menu item
	private class FileSaveAsEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			saveAs(); // save file as a specified file name
		} // end method actionPerformed
	} // end inner class FileSaveAsEventHandler
	
	// event handler inner class for the File --> Exit menu item
	private class FileExitEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			attemptExit(); // attempt to exit the application
		} // end method actionPerformed
	} // end inner class FileExitEventHandler
	
	// event handler inner class for the Edit --> Copy menu item
	private class EditCopyEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			textEditor.copy(); // call the text editor GUI's copy method
		} // end method actionPerformed
	} // end inner class EditCopyEventHandler
	
	// event handler inner class for the Edit --> Cut menu item
	private class EditCutEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			textEditor.cut(); // call the text editor GUI's cut method
		} // end method actionPerformed
	} // end inner class EditCutEventHandler
	
	// event handler inner class for the Edit --> Paste menu item
	private class EditPasteEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			textEditor.paste(); // call the text editor GUI's paste method
		} // end method actionPerformed
	} // end inner class EditPasteEventHandler
	
	// event handler inner class for the Edit --> Search menu item
	private class EditSearchEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			searchReplaceGUI.showReplace( false ); // set the search and replace GUI's replace panel to be invisible
			searchReplaceGUI.setVisible( true ); // set the search and replace GUI to be visible
		} // end method actionPerformed
	} // end inner class EditSearchEventHandler
	
	// event handler inner class for the Edit --> Replace menu item
	private class EditReplaceEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			searchReplaceGUI.showReplace( true ); // set the search and replace GUI's replace panel to be visible
			searchReplaceGUI.setVisible( true ); // set the search and replace GUI to be visible
		} // end method actionPerformed
	} // end inner class EditReplaceEventHandler
	
	// event handler inner class for the Format --> Font menu item
	private class FormatFontEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			// deselect the currently selected font name or size in case they have changed since the last time the font chooser window was visible
			fontChooseGUI.clearSelectedFontSize();
			fontChooseGUI.clearSelectedFontName();
			
			// set the selected font name and size in the JLists to be the name and size of the current font
			fontChooseGUI.setSelectedFontName( textEditor.getFont().getName() );
			fontChooseGUI.setSelectedFontSize( textEditor.getFont().getSize() );
			
			// populate the font name and size text fields with the name and size of the current font
			fontChooseGUI.setFontNameText( textEditor.getFont().getName() );
			fontChooseGUI.setFontSizeText( Integer.toString( textEditor.getFont().getSize() ) );
			
			// set the bold and italic check boxes to selected if the current font is bold and/or italic
			fontChooseGUI.setBold( textEditor.getFont().isBold() );
			fontChooseGUI.setItalic( textEditor.getFont().isItalic() );
			
			// set the font chooser GUI to be visible
			fontChooseGUI.setVisible( true );
		} // end method actionPerformed
	} // end inner class FormatFontEventHandler
	
	// event handler inner class for the Format --> Word Wrap menu item
	private class FormatWordWrapEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			toggleWordWrap(); // toggle word wrap on or off
		} // end method actionPerformed
	} // end inner class FormatWordWrapEventHandler
	
	// event handler inner class for the Help --> About menu item
	private class HelpAboutEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			// show a message dialog box with the About message
			JOptionPane.showMessageDialog( textEditor, // parent component is the main text editor GUI
										   HELP_ABOUT_MESSAGE, // display the main About message
										   "About " + APPLICATION_NAME, // set the title of the message dialog box
										   JOptionPane.INFORMATION_MESSAGE); // type of message displayed is Information Message
		} // end method actionPerformed
	} // end inner class HelpAboutEventHandler
	
	// key event handler inner class for the main text area
	private class TextAreaKeyListener extends KeyAdapter // inherit from KeyAdapter so we only have to implement the methods we are interested in
	{
		// override method keyTyped
		public void keyTyped( KeyEvent keyEvent )
		{
			if( fileSaved ) // if the file is currently in a saved state
				setFileUnsaved(); // when a key is typed in the main text area, set the file to be unsaved
		}
	} // end inner class TextAreaKeyListener
	
	// caret event handler inner class for the main text area
	private class TextAreaCaretListener implements CaretListener // implement CaretListener interface
	{
		// implement abstract method caretUpdate
		public void caretUpdate( CaretEvent caretEvent )
		{
			// change the forwards and backwards search start positions to the current position of the caret
			setSearchStartPositions( textEditor.getCaretPosition(), textEditor.getCaretPosition() );
		} // end method caretUpdate
	} // end inner class TextAreaCaretListener
	
	// event handler inner class for the Next button on the Search & Replace GUI
	private class SearchNextButtonEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			search( SEARCH_FORWARDS ); // search forwards
		} // end method actionPerformed
	} // end inner class NextButtonEventHandler
	
	// event handler inner class for the Previous button on the Search & Replace GUI
	private class SearchPreviousButtonEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			search( SEARCH_BACKWARDS ); // search backwards
		} // end method actionPerformed
	} // end inner class PreviousButtonEventHandler
	
	// event handler inner class for the Replace button on the Search & Replace GUI
	private class ReplaceButtonEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			replace(); // replace the search text with the replace text
		} // end method actionPerformed
	} // end inner class ReplaceButtonEventHandler
	
	// event handler inner class for the Replace All button on the Search & Replace GUI
	private class ReplaceAllButtonEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			replaceAll(); // replace all instances of the search text with the replace text
		} // end method actionPerformed
	} // end inner class ReplaceAllButtonEventHandler
	
	// event handler inner class for the Search text field on the Search & Replace GUI
	private class SearchFieldEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			search( SEARCH_FORWARDS ); // search forwards when user hits enter in the Search text field
		} // end method actionPerformed
	} // end inner class SearchFieldEventHandler
	
	// event handler inner class for the Font Names list on the Font Chooser GUI
	private class FontNameListSelectionListener implements ListSelectionListener // implement ListSelectionListener interface
	{
		// implement abstract method valueChanged
		public void valueChanged( ListSelectionEvent event )
		{
			// if there is a font name selected in the Font Names list
			if( fontChooseGUI.isFontNameSelected() )
				// when user chooses a new font name from the list, update the Font Name text field with the chosen font name
				fontChooseGUI.setFontNameText( fontChooseGUI.getSelectedFontName() );
		} // end method valueChanged
	} // end inner class FontNameListSelectionListener
	
	// event handler inner class for the Font Sizes list on the Font Chooser GUI
	private class FontSizeListSelectionListener implements ListSelectionListener // implement ListSelectionListener interface
	{
		// implement abstract method valueChanged
		public void valueChanged( ListSelectionEvent event )
		{
			// if there is a font size selected in the Font Sizes list
			if( fontChooseGUI.isFontSizeSelected() )
				// when user chooses a new font size from the list, update the Font Sizes text field with the chosen font size
				fontChooseGUI.setFontSizeText( Integer.toString( fontChooseGUI.getSelectedFontSize() ) );
		} // end method valueChanged
	} // end inner class FontSizeListSelectionListener
	
	// event handler inner class for the OK button on the Font Chooser GUI
	private class FontOkButtonEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			String fontName; // local String object to hold the font name
			int fontSize; // local int variable to hold the font size
			
			fontName = fontChooseGUI.getFontNameText(); // get the font name from the Font Name text field
			if( fontName == null ) // if the Font Name text field did not contain any text
				fontName = textEditor.getFont().getName(); // get the current font name of the text editor
			
			try // begin try block
			{
				// get the font size from the Font Size text field and convert to Integer
				fontSize = Integer.parseInt( fontChooseGUI.getFontSizeText() );
				
				// if the font size entered is greater than 100 or less than 1
				if( fontSize > 100 || fontSize < 1 )
					fontSize = textEditor.getFont().getSize(); // get the current font size of the text editor
			} // end try block
			// catch NumberFormatException if text entered in Font Size text field cannot be converted to an Integer
			catch( NumberFormatException numberFormatException )
			{
				fontSize = textEditor.getFont().getSize(); // get the current font size of the text editor
			} // end catch block for NumberFormatException
			
			// set the font of the text editor to a new font based on the name, style and size indicated on the Font Chooser GUI
			textEditor.setFont( new Font( fontName, fontStyle(), fontSize ) );
			fontChooseGUI.setVisible( false ); // set the Font Chooser GUI to be invisible again
		} // end method actionPerformed
	} // end inner class FontOkButtonEventHandler
	
	// event handler inner class for the Cancel button on the Font Chooser GUI
	private class FontCancelButtonEventHandler implements ActionListener // implement ActionListener interface
	{
		// implement abstract method actionPerformed
		public void actionPerformed( ActionEvent event )
		{
			fontChooseGUI.setVisible( false ); // set the Font Chooser GUI to be invisible again
		} // end method actionPerformed
	} // end inner class FontCancelButtonEventHandler
	
	/**************************************************************
	 ** Define methods                                           **
	 **************************************************************/
	
	// method to determine the font style based on the options indicated on the Font Chooser GUI
	public int fontStyle()
	{
		if( fontChooseGUI.isBold() ) // if the Bold checkbox is selected
			if( fontChooseGUI.isItalic() ) // if the Italic checkbox is selected
				return Font.BOLD + Font.ITALIC; // font style is Bold + Italic
			else // if Italic checkbox is not selected
				return Font.BOLD; // font style is Bold
		else if( fontChooseGUI.isItalic() ) // if only the Italic checkbox is selected
			return Font.ITALIC; // font style is Italic
		else // neither Bold checkbox nor Italic checkbos is selected
			return Font.PLAIN; // font style is Plain
	} // end method fontStyle

	// method to save the file passed in to disk
	public void save( File file )
	{
		FileWriter writer = null; // declare new FileWriter object for writing to a file, initially set to NULL
	
		try // try writing the file to disk
		{
			// decision was taken not to use a buffered writer as we are only writing one string which is likely to be larger than the buffer size anyway
			// from investigation, buffered writer seems to be intended for when there are multiple small writes occurring
			writer = new FileWriter( file ); // initialise the FileWriter with the file passed in
			writer.write( textEditor.getText() ); // write the text editor text to the file
			writer.close(); // close the FileWriter
			currentFile = file; // set the current file to be the file passed in now that it has been saved
			setFileSaved(); // set file to a status of saved
		} // end try block
		catch( SecurityException securityException ) // catch SecurityException if one occurs while writing to file
		{
			// display error message to the user
			JOptionPane.showMessageDialog( textEditor, // parent component is the main text editor GUI
										   String.format( "%s: You do not have permission to save file %s", securityException, file ), // error message to display
										   "Security Exception", // window title
										   JOptionPane.ERROR_MESSAGE // the type of message dialog is Error Message
										 );
		} // end catch block for SecurityException
		catch( IOException ioException ) // catch IOException if one occurs while writing to file
		{
			// display error message to the user
			JOptionPane.showMessageDialog( textEditor, // parent component is the main text editor GUI
										   String.format( "%s: Could not save file %s. An IO exception occurred.", ioException, file ), // error message to display
										   "IO Exception", // window title
										   JOptionPane.ERROR_MESSAGE // the type of message dialog is Error Message
										 );
		} // end catch block for IOException
		catch( OutOfMemoryError outOfMemoryError )
		{
			// display error message to the user
			JOptionPane.showMessageDialog( textEditor, // parent component is the main text editor GUI
										   String.format( "%s: Could not save file %s. Out of memory.", outOfMemoryError, file ), // error message to display
										   "Out Of Memory", // window title
										   JOptionPane.ERROR_MESSAGE // the type of message dialog is Error Message
										 );
		} // end catch block for OutOfMemoryError
		finally // finally block to try and close the file writer if an exception occurred
		{
			try // try and close the file writer
			{
				if( writer != null ) // if file writer has not already been closed
					writer.close(); // close the file writer
			} // end try block
			catch( IOException ioException ) // catch IOException if one occurs while trying to close the file writer
			{
				// display error message to the user
				JOptionPane.showMessageDialog( textEditor, // parent component is the main text editor GUI
											   String.format( "%s: Could not close file writer%s. An IO exception occurred.", ioException, writer ), // error message to display
											   "IO Exception", // window title
											   JOptionPane.ERROR_MESSAGE // the type of message dialog is Error Message
											 );
			} // end catch block for IOException
		} // end finally block
	} // end method save

	// method to save the current text as a file with a particular name
	// method will return true if the file was saved successfully and false if file was not saved
	public boolean saveAs()
	{		
		while( true ) // keep looping until the method returns a value
		{
			if( currentFile == null ) // if the current file is brand new and not yet saved
				fileChooser.setSelectedFile( DEFAULT_FILE ); // set the selected file in the file chooser to be the default file
			else // if the current file is not brand new
				fileChooser.setSelectedFile( currentFile ); // set the selected file in the file chooser to be the current file
		
			// display the File Chooser save dialog box with the main text editor GUI as the parent component
			if( fileChooser.showSaveDialog( textEditor ) == JFileChooser.APPROVE_OPTION ) // if the user clicks Save on the save dialog box
			{
				String selectedFileName = fileChooser.getSelectedFile().toString(); // get the name of the file selected in the file chooser
				
				if( selectedFileName.lastIndexOf( "." ) == -1 ) // if the selected file name does not contain a file extension
					selectedFileName = selectedFileName + "." + ALLOWED_TEXT_FILE_EXTENSIONS[ 0 ]; // suffix the file name with the first file extension in the list of allowed file extensions					
					
				File file = new File( selectedFileName ); // create local file with the file name selected by the file chooser
				
				if( file.exists() ) // check if the selected file already exists
				{
					// ask user if they wish to overwrite the existing file using a dialog box
					if( JOptionPane.showConfirmDialog( textEditor, // parent component is the main text editor GUI
													   String.format( "File %s already exists. Do you wish to overwrite?", file.toString() ), // message to display
													   "File exists", // title of the dialog box
													   JOptionPane.YES_NO_OPTION  // the type of options available are Yes and No
												 ) == 0 ) // if the user indicates that they do want to overwrite the existing file
					{
						save( file ); // save the file
						return true; // return true to indicate the file was saved successfuly
					} // else do nothing (user does not want to overwrite the existing file
				}
				else // if selected file does not already exist
				{
					save( file ); // save the file
					return true; // return true to indicate the file was saved successfuly
				}
			} // if the user clicked Cancel on the save dialog box, do nothing
			else // if the user did not click Ok on the save dialog box
				return false; // return false to indicate the file was not saved
		} // if this point is reached then the user has indicated that they do not wish to overwrite the existing file so keep looping
	} // end method saveAs
	
	// method to attempt to save the current file
	// returns true if the current file has been successfully saved, false if the file was not saved
	public boolean attemptSave()
	{
		if( currentFile == null ) // if the current file is new and has not been saved before
			return saveAs(); // save the file as a particular file name
		else // file is not new
		{
			if( currentFile.exists() ) // if the file already exists
			{
				save( currentFile ); // simply save the file overwriting the previous version
				return true; // return a successful outcome
			}
			else // file does not yet exist
				return saveAs(); // attempt to save the file as a particular file name and return the outcome
		}
	} // end method attemptSave
	
	// method to attempt to exit the application
	public void attemptExit()
	{
		if( fileSaved ) // if the current file has been saved
			System.exit( 0 ); // simply exit the application
		else // if current file has not been saved
		{
			// ask user if they want to save changes before exiting the application using a dialog box
			int option = JOptionPane.showConfirmDialog( textEditor, // parent component is the main text editor GUI
													    "Do you wish to save the file before exiting?", // message to be displayed
													    "Confirm Save", // title of the dialog box
													    JOptionPane.YES_NO_CANCEL_OPTION // options to display are Yes, No and Cancel
													  );
		
			if( option == 0 ) // if user clicked Yes (save changes)
			{
				if( attemptSave() ) // attempt to save the file
					System.exit( 0 ); // if file was saved successfully, exit the application
			}
			else if( option == 1 ) // if user clicked No (don't save changes)
				System.exit( 0 ); // exit application without saving
			
			// if user clicked Cancel, simply do nothing. File will not be saved and application will not exit
		}
	} // end method attemptExit
	
	// method to attempt to create a brand new file
	public void attemptNew()
	{
		if( fileSaved ) // if the existing file has been saved
			newFile(); // create a new file
		else // if existing file has not been saved
		{
			// ask the user if they want to save changes before creating a new file using a dialog box
			if( JOptionPane.showConfirmDialog( textEditor, // parent componnet is the main text editor GUI
											   "Do you wish to save the file before continuing?", // message to be displayed
											   "Confirm Save", // title of the dialog box
											   JOptionPane.YES_NO_OPTION // options to display are Yes and No
											 ) == 0 ) // if user clicks Yes
			{
				if( attemptSave() ) // attempt to save the existing file. If successful...
					newFile(); // create a new file
			}
			else // if user does not want to save the existing file before continuing
				newFile(); // just create a new file
		}
	} // end method attemptNew
	
	// method to create a new file
	public void newFile()
	{
		textEditor.setText( "" ); // clear all text in the text editor
		currentFile = null; // set the current file to be null as it is new and therefore empty
		setFileSaved(); // set new file to a status of saved as it is a new file and no text has been entered
		resetSearchStartPositions(); // reset the forwards and backwards search start positions to default values
	} // end method newFile
	
	// method to attempt to load a file from disk into the text editor
	public void attemptOpen()
	{
		if( fileSaved ) // if the existing file has been saved
			openFile(); // open a file
		else // if existing file needs to be saved
		{
			// ask the user if they want to save changes before opening a file using a dialog box
			if( JOptionPane.showConfirmDialog( textEditor, // parent component is the main text editor GUI
											   "Do you wish to save the file before continuing?", // message to be displayed
											   "Confirm Save", // title of the dialog box
											   JOptionPane.YES_NO_OPTION // options to display are Yes and No
											 ) == 0 ) // if user clicks Yes
			{
				if( attemptSave() ) // attempt to save the existing file
					openFile(); // open a file
			}
			else // if user does not click Yes
				openFile(); // just open a file
		}
	} // end method attemptOpen
	
	// method to open a file from disk and load it into the text editor
	public void openFile()
	{
		fileChooser.setSelectedFile( new File( "" ) ); // set the currently selected file in the file chooser to be a blank file to remove the selected file from the last time the file chooser was used

		// display the File Chooser open dialog box with the main text editor GUI as the parent component
		if( fileChooser.showOpenDialog( textEditor ) == JFileChooser.APPROVE_OPTION ) // if the user clicks Open on the open dialog box
		{
			File file = null; // local File object for the file we are trying to open
			FileReader reader = null; // FileReader object to read the file from disk
			BufferedReader bufferedReader = null; // BufferedReader object to wrap the FileReader to improve performance
			
			try // try to read a file into the text editor
			{
				file = fileChooser.getSelectedFile(); // initialise file to the file selected in the file chooser
				reader = new FileReader( file ); // initialise file reader to read the file
				bufferedReader = new BufferedReader( reader ); // wrap the file reader in a buffered reader
				textEditor.readText( bufferedReader ); // read text from the file into the text editor using the buffered reader
				bufferedReader.close(); // close the buffered reader
				reader.close(); // close the file reader
				
				currentFile = file; // set the current file to the file just opened
				setFileSaved(); // set the current file to be saved as it has just been opened and no modifications have been made yet
				resetSearchStartPositions(); // reset the forwards and backwards search start positions to default values
			}
			catch( SecurityException securityException ) // catch SecurityException if one occurs while opening the file
			{
				JOptionPane.showMessageDialog( textEditor, // parent component is the main text editor GUI
											   String.format( "%s: You do not have permission to open file %s", securityException, file ), // error message to display
											   "Security Exception", // window title
											   JOptionPane.ERROR_MESSAGE // the type of message dialog is Error Message
											 );
			} // end catch block for SecurityException
			catch( FileNotFoundException fileNotFoundException ) // catch FileNotFoundException if one occurs while opening the file
			{
				JOptionPane.showMessageDialog( textEditor, // parent component is the main text editor GUI
											   String.format( "%s: Could not open file %s. File not found.", fileNotFoundException, file ), // error message to display
											   "Security Exception", // window title
											   JOptionPane.ERROR_MESSAGE // the type of message dialog is Error Message
											 );
			} // end catch block for FileNotFoundException
			catch( IOException ioException ) // catch IOException if one occurs while opening the file
			{
				JOptionPane.showMessageDialog( textEditor, // parent component is the main text editor GUI
											   String.format( "%s: Could not open file %s. An IO exception occurred.", ioException, file ), // error message to display
											   "IO Exception", // window title
											   JOptionPane.ERROR_MESSAGE // the type of message dialog is Error Message
											 );
			} // end catch block for IOException
			catch( OutOfMemoryError outOfMemoryError ) // catch OutOfMemoryError if one occurs while opening the file
			{
				JOptionPane.showMessageDialog( textEditor, // parent component is the main text editor GUI
											   String.format( "%s: Could not open file %s. Out of memory.", outOfMemoryError, file ), // error message to display
											   "Out Of Memory", // window title
											   JOptionPane.ERROR_MESSAGE // the type of message dialog is Error Message
											 );
			} // end catch block for OutOfMemoryError
			finally // if an exception was encountered
			{
				try // try to close the buffered reader and file reader
				{
					if( bufferedReader != null ) // if buffered reader is open
						bufferedReader.close(); // close the buffered reader
					
					if( reader != null ) // if file reader is open
						reader.close(); // close the file reader
				}
				catch( IOException ioException ) // catch IOException if one occurs while closing the buffered reader or file reader
				{
					JOptionPane.showMessageDialog( textEditor, // parent component is the main text editor GUI
												   String.format( "%s: Could not close file reader. An IO exception occurred", ioException ), // error message to display
												   "IO Exception", // window title
												   JOptionPane.ERROR_MESSAGE // the type of message dialog is Error Message
												 );
				} // end catch block for IOException
			} // end finally block
		} // if user clicked Cancel on the open dialog box, do nothing
	} // end method openFile
	
	// method to set current file to a state of saved
	public void setFileSaved()
	{
		if( currentFile == null ) // if current file is new
			textEditor.setTitle( APPLICATION_NAME + " - " + DEFAULT_FILE_NAME ); // update the text editor title bar with the default file name
		else // current file is not new
			textEditor.setTitle( APPLICATION_NAME + " - " + currentFile.getName() ); // update the text editor title bar with the current file name

		fileSaved = true; // set the file to a status of saved
	} // end method setFileSaved
	
	// method to set current file to a state of unsaved
	public void setFileUnsaved()
	{
		// if window title is not already suffixed with an asterisk
		if( textEditor.getTitle().lastIndexOf( "*" ) != textEditor.getTitle().length() )
			textEditor.setTitle( textEditor.getTitle() + "*" ); // suffix the window title with an asterisk to indicate the file has not been saved

		fileSaved = false; // set the file to a status of unsaved
	} // end method setFileUnsaved
	
	// method to toggle word wrap on or off
	public void toggleWordWrap()
	{
		if( textEditor.getWrapStyleWord() ) // if word wrap is already on
			textEditor.setWrapStyleWord( false ); // set word wrap off
		else // if word wrap is already off
			textEditor.setWrapStyleWord( true ); // set word wrap on
	} // end method toggleWordWrap
	
	// method to search for a string within the text area in the specified direction
	private boolean search( int searchDirection )
	{		
		// local variable to hold the search string which is obtained from the Search text field on the search and replace GUI
		String searchTerm = searchReplaceGUI.getSearchText();
		
		boolean returnValue = false; // local boolean to hold the return value for the method. Assume search term won't be found
		int location; // local int to track the location of the search term within the text area
		int newForwardsStartPosition; // local int to hold the new forwards start position after the search term has been found
		int newBackwardsStartPosition; // local int to hold the new backwards start position after the search term has been found
		
		if( !searchTerm.equals( "" ) ) // if a the search text is not null
		{
			if( searchDirection != SEARCH_FORWARDS && searchDirection != SEARCH_BACKWARDS )
				location = -1;
			else
			{			
			//if( searchDirection == SEARCH_FORWARDS ) // if the specified search direction is forwards
			//{
				if( searchReplaceGUI.isCaseSensitive() ) // if search should be case sensitive
					// location = textEditor.getText().indexOf( searchTerm, fwdSearchStartPosition );
					// get the location of the search text within the text area starting from the current forwards or backwards search start position depending on the specified search direction
					location = ( searchDirection == SEARCH_FORWARDS ) ? textEditor.getText().indexOf( searchTerm, fwdSearchStartPosition ) : textEditor.getText().lastIndexOf( searchTerm, bckSearchStartPosition );
				else // search should not be case sensitive
					// location = textEditor.getText().toUpperCase().indexOf( searchTerm.toUpperCase(), fwdSearchStartPosition );
					// get the location of the search text within the text area starting from the current forwards or backwards search start position depending on the specified search direction
					// both search text and text to search converted to upper case to remove case sensitivity
					location = ( searchDirection == SEARCH_FORWARDS ) ? textEditor.getText().toUpperCase().indexOf( searchTerm.toUpperCase(), fwdSearchStartPosition ) : textEditor.getText().toUpperCase().lastIndexOf( searchTerm.toUpperCase(), bckSearchStartPosition );
			}
			
			if( location >= 0 ) // if search term was found
			{
				// new forwards search start position is the character immediately following the search term just found
				// if search term found was at the end of the document, set the forwards start position to the last character in the document
				newForwardsStartPosition = ( location + searchTerm.length() >= textEditor.getText().length() ) ? textEditor.getText().length() - 1 : location + searchTerm.length();
				
				// new backwards search start position is the character immediately preceding the search term just found
				// if search term found was at the beginning of the document, set the backwards start position to the first character in the document
				newBackwardsStartPosition = ( location > 0 ) ? location - 1 : 0;
				
				textEditor.select( location, location + searchTerm.length() ); // highlight the search term that was found in the document
				
				// set the forwards and backwards search start positions for the next search using two argument method
				setSearchStartPositions( newForwardsStartPosition, newBackwardsStartPosition );
				
				returnValue = true; // search term was found so return value will be true
			}
			
			// if search term was not found, do nothing. return value will still be false as per initialisation
		}
		
		// if search term was null, do nothing. return value will still be false as per initialisation
		
		return returnValue; // return true if search term was found, false if not found
	} // end method search
	
	// method to replace a string in the document with another string
	public void replace()
	{
		// local variable for the replace string which is sourced from the Replace text field in the Search & Replace GUI
		String replaceText = searchReplaceGUI.getReplaceText();
		
		if( textEditor.getSelectedText() == null ) // if no part of the document is already selected
			search( SEARCH_FORWARDS ); // search forwards to select the next occurrence of the search term if it exists
		
		if( textEditor.getSelectedText() != null ) // if the document contains selected text
		{
			// replace the selected text with the replacement text
			textEditor.replaceRange( replaceText, textEditor.getSelectionStart(), textEditor.getSelectionEnd() );
			
			if( fileSaved ) // if file is in a saved state
				setFileUnsaved(); // set the file to be unsaved as the text has changed
		}
		
		// if the document contains no selected text then search term could not be found so do nothing
	} // end method replace
	
	// method to replace all occurrences of a string in the document with a replacement string
	public void replaceAll()
	{
		resetSearchStartPositions(); // reset search start positions to begin searching from top of document
		
		// keep searching forwards from the current forwards search position until no more occurrences can be found
		while( search( SEARCH_FORWARDS ) )
		{
			replace(); // for each occurence of the search term found, replace it with the replace term
		}
	} // end method replaceAll
	
	// method to set the forwards and backwards search start positions as specified
	public void setSearchStartPositions( int forwardsStartPosition, int backwardsStartPosition)
	{
		fwdSearchStartPosition = forwardsStartPosition; // set the new forwards search start position
		bckSearchStartPosition = backwardsStartPosition; // set the new backwards search start position
	} // end method setSearchStartPositions
	
	// method to set the forwads and backwards search start positions to their default values
	public void resetSearchStartPositions()
	{
		// set forwards search start position to the beginning of the document
		// set backwards search start position to the end of the document
		setSearchStartPositions( 0, textEditor.getText().length() - 1 );
	} // end method resetSearchStartPositions

	// main method for launching the text editor
	public static void main( String args[] )
	{
		// launch Graham's Text Editor
		GrahamsTextEditor gte = new GrahamsTextEditor();
	} // end method main

} // end class GrahamsTextEditor