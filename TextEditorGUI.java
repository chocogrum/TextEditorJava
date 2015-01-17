/**************************************************************
 ** Project 2: TextEditorGUI.java
 ** Description: Define a GUI for a text editor
 ** Name: Graham Thomas
 ** Student Number: 1479585
**************************************************************/

// import required classes
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.event.CaretListener;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;

// declare class to represent main GUI for the text editor
public class TextEditorGUI extends JFrame // inherit JFrame class
{
	/**************************************************************
	 ** Declare GUI components                                   **
	 **************************************************************/
	 
	private JTextArea textArea; // declare main text area
	private JMenuBar menuBar; // declare the menu bar
	private JMenu fileMenu; // declare the File menu
	private JMenu editMenu; // declare the Edit menu
	private JMenu formatMenu; // declare the Format menu
	private JMenu helpMenu; // declare the Help menu
	private JMenuItem fileNewMenuItem; // declare the File --> New menu item
	private JMenuItem fileOpenMenuItem; // declare the File --> Open menu item
	private JMenuItem fileSaveMenuItem; // declare the File --> Save menu item
	private JMenuItem fileSaveAsMenuItem; // declare the File --> Save As menu item
	private JMenuItem fileExitMenuItem; // declare the File --> Exit menu item
	private JMenuItem editCopyMenuItem; // declare the Edit --> Copy menu item
	private JMenuItem editCutMenuItem; // declare the Edit --> Cut menu item
	private JMenuItem editPasteMenuItem; // declare the Edit --> Paste menu item
	private JMenuItem editSearchMenuItem; // declare the Edit --> Search menu item
	private JMenuItem editReplaceMenuItem; // declare the Edit --> Replace menu item
	private JMenuItem formatFontMenuItem; // declare the Format --> Font menu item
	private JCheckBoxMenuItem formatWordWrapMenuItem; // declare the Format --> Word Wrap menu item
	private JMenuItem helpAboutMenuItem; // declare the Help --> About menu item
	
	// begin constructor with one parameter for application name
	public TextEditorGUI( String applicationName )
	{
		// call superclass constructor with name of application
		super( applicationName );
		
		menuBar = new JMenuBar(); // initialise menu bar
		
		// call methods to initialise each menu in the menu bar
		initialiseFileMenu();
		initialiseEditMenu();
		initialiseFormatMenu();
		initialiseHelpMenu();
		
		// add each menu to the menu bar
		menuBar.add( fileMenu );
		menuBar.add( editMenu );
		menuBar.add( formatMenu );
		menuBar.add( helpMenu );
		
		// add the menu bar to the current object
		setJMenuBar( menuBar );
		
		textArea = new JTextArea(); // initialise the main text area
		textArea.setLineWrap( true ); // set line wrap true
		setWrapStyleWord( true ); // set word wrap true
		
		// add text area to the current object inside a scroll pane
		// use default horizontal and vertical scrolling policies
		add( new JScrollPane ( textArea ) );
		
		// set GUI to do nothing on close to trigger the window closing event handler defined in TextEditor to be called
		// this will prevent the application from being exited before prompting the user to save any changes
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
	} // end constructor
	
	/**************************************************************
	 ** Define methods to initialise menu and menu items         **
	 **************************************************************/
	
	// method to initialise the File menu
	private void initialiseFileMenu()
	{
		fileMenu = new JMenu( "File" ); // initialise File menu
		fileNewMenuItem = new JMenuItem( "New" ); // initialise New menu item
		fileOpenMenuItem = new JMenuItem( "Open" ); // initialise Open menu item
		fileSaveMenuItem = new JMenuItem( "Save" ); // initialise Save menu item
		fileSaveAsMenuItem = new JMenuItem( "Save As" ); // initialise Save As menu item
		fileExitMenuItem = new JMenuItem( "Exit" ); // initialise Exit menu item
		fileMenu.add( fileNewMenuItem ); // add New menu item to File menu
		fileMenu.add( fileOpenMenuItem ); // add Open menu item to File menu
		fileMenu.add( fileSaveMenuItem ); // add Save menu item to File menu
		fileMenu.add( fileSaveAsMenuItem ); // add Save As menu item to File menu
		fileMenu.add( fileExitMenuItem ); // add Exit menu item to File menu
	} // end method initialiseFileMenu
	
	// method to initialise the Edit menu
	private void initialiseEditMenu()
	{
		editMenu = new JMenu( "Edit" ); // initialise Edit Menu
		editCopyMenuItem = new JMenuItem( "Copy" ); // initialise Copy menu item
		editCutMenuItem = new JMenuItem( "Cut" ); // initialise Cut menu item
		editPasteMenuItem = new JMenuItem( "Paste" ); // initialise Paste menu item
		editSearchMenuItem = new JMenuItem( "Search" ); // initialise Search menu item
		editReplaceMenuItem = new JMenuItem( "Replace" ); // initialise Replace menu item
		editMenu.add( editCopyMenuItem ); // add Copy menu item to Edit menu
		editMenu.add( editCutMenuItem ); // add Cut menu item to Edit menu
		editMenu.add( editPasteMenuItem ); // add Paste menu item to Edit menu
		editMenu.add( editSearchMenuItem ); // add Search menu item to Edit menu
		editMenu.add( editReplaceMenuItem ); // add Replace menu item to Edit menu
	} // end method initialiseEditMenu
	
	// method to initialise the Format menu
	private void initialiseFormatMenu()
	{
		formatMenu = new JMenu( "Format" ); // initialise Format Menu
		formatFontMenuItem = new JMenuItem( "Font" ); // initialise Font menu item
		// initialise Word Wrap menu item as a check box menu item so users can see if it is selected or deselected
		formatWordWrapMenuItem = new JCheckBoxMenuItem( "Word Wrap" );
		
		formatMenu.add( formatFontMenuItem ); // add Font menu item to Format menu
		formatMenu.add( formatWordWrapMenuItem ); // add WordWrap menu item to Format menu
	} // end method initialiseFormatMenu
	
	// method to initialise the Help menu
	private void initialiseHelpMenu()
	{
		helpMenu = new JMenu( "Help" ); // initialise Help menu
		helpAboutMenuItem = new JMenuItem( "About" ); // initialise About menu item
		helpMenu.add( helpAboutMenuItem ); // add About menu item to Help menu
	} // end method initialiseHelpMenu
	
	/**************************************************************
	 ** Define methods to register event handlers                **
	 **************************************************************/
	
	// method to register a handler for the File --> New menu item
	public void fileNewRegisterHandler( ActionListener actionListener )
	{
		// register event handler for File --> New menu item
		fileNewMenuItem.addActionListener( actionListener );
	} // end method fileNewRegisterHandler
	
	// method to register a handler for the File --> Open menu item
	public void fileOpenRegisterHandler( ActionListener actionListener )
	{
		// register event handler for File --> Open menu item
		fileOpenMenuItem.addActionListener( actionListener );
	} // end method fileOpenRegisterHandler
	
	// method to register a handler for the File --> Save menu item
	public void fileSaveRegisterHandler( ActionListener actionListener )
	{
		// register event handler for File --> Save menu item
		fileSaveMenuItem.addActionListener( actionListener );
	} // end method fileSaveRegisterHandler
	
	// method to register a handler for the File --> Save As menu item
	public void fileSaveAsRegisterHandler( ActionListener actionListener )
	{
		// register event handler for File --> Save As menu item
		fileSaveAsMenuItem.addActionListener( actionListener );
	} // end method fileSaveAsRegisterHandler
	
	// method to register a handler for the File --> Exit menu item
	public void fileExitRegisterHandler( ActionListener actionListener )
	{
		// register event handler for File --> Exit menu item
		fileExitMenuItem.addActionListener( actionListener );
	} // end method fileExitRegisterHandler
	
	// method to register a handler for the Edit --> Copy menu item
	public void editCopyRegisterHandler( ActionListener actionListener )
	{
		// register event handler for Edit --> Copy menu item
		editCopyMenuItem.addActionListener( actionListener );
	} // end method editCopyRegisterHandler
	
	// method to register a handler for the Edit --> Cut menu item
	public void editCutRegisterHandler( ActionListener actionListener )
	{
		// register event handler for Edit --> Cut menu item
		editCutMenuItem.addActionListener( actionListener );
	} // end method editCutRegisterHandler
	
	// method to register a handler for the Edit --> Paste menu item
	public void editPasteRegisterHandler( ActionListener actionListener )
	{
		// register event handler for Edit --> Paste menu item
		editPasteMenuItem.addActionListener( actionListener );
	} // end method editPasteRegisterHandler
	
	// method to register a handler for the Edit --> Search menu item
	public void editSearchRegisterHandler( ActionListener actionListener )
	{
		// register event handler for Edit --> Search menu item
		editSearchMenuItem.addActionListener( actionListener );
	} // end method editSearchRegisterHandler
	
	// method to register a handler for the Edit --> Replace menu item
	public void editReplaceRegisterHandler( ActionListener actionListener )
	{
		// register event handler for Edit --> Replace menu item
		editReplaceMenuItem.addActionListener( actionListener );
	} // end method editReplaceRegisterHandler
	
	// method to register a handler for the Format --> Font menu item
	public void formatFontRegisterHandler( ActionListener actionListener )
	{
		// register event handler for Format --> Font menu item
		formatFontMenuItem.addActionListener( actionListener );
	} // end method formatFontRegisterHandler
	
	// method to register a handler for the Format --> Word Wrap menu item
	public void formatWordWrapRegisterHandler( ActionListener actionListener )
	{
		// register event handler for Format --> Word Wrap menu item
		formatWordWrapMenuItem.addActionListener( actionListener );
	} // end method formatWordWrapRegisterHandler
	
	// method to register a handler for the Help --> About menu item
	public void helpAboutRegisterHandler( ActionListener actionListener )
	{
		// register event handler for Help --> About menu item
		helpAboutMenuItem.addActionListener( actionListener );
	} // end method helpAboutRegisterHandler

	// method to register a handler for a key event in the main text area
	public void textAreaKeyEventHandler( KeyListener keyListener )
	{
		// register key event handler for text area
		textArea.addKeyListener( keyListener );
	} // end method textAreaKeyListener
	
	// method to register a handler for a caret event in the main text area
	public void textAreaCaretEventHandler( CaretListener caretListener )
	{
		// register caret event handler for text area
		textArea.addCaretListener( caretListener );
	} // end method textAreaCaretListener

	/**************************************************************
	 ** Define utility methods                                   **
	 **************************************************************/
	
	// method to get the text of the text area
	public String getText()
	{
		return textArea.getText(); // return the text in the text area
	} // end method getText
	
	// method to set the caret position of the text area
	public void setCaretPosition( int position )
	{
		textArea.setCaretPosition( position ); // set the position of the caret in the text area per the integer passed in
	} // end method setCaretPosition
	
	// method to select a piece of text in the text area
	public void select( int selectionStart, int selectionEnd )
	{
		// select some text as per the start and end positions passed in
		textArea.select( selectionStart, selectionEnd );
	} // end method select
	
	// method to replace a range of text in the text area
	public void replaceRange( String str, int start, int end )
	{
		// replace the range indicated by the start and end positions passed in with the string passed in
		textArea.replaceRange( str, start, end );
	} // end method replaceRange
	
	// method to get the start position of the currently selected text in the text area
	public int getSelectionStart()
	{
		return textArea.getSelectionStart(); // return the start position of the currently selected text
	} // end method getSelectionStart
	
	// method to get the end position of the currently selected text in the text area
	public int getSelectionEnd()
	{
		return textArea.getSelectionEnd(); // return the end position of the currently selected text
	} // end method getSelectionEnd
	
	// method to get the text currently selected in the text area
	public String getSelectedText()
	{
		return textArea.getSelectedText(); // return the currently selected text
	} // end method getSelectedText
	
	// method to set the font of the text area
	public void setFont( Font font )
	{
		textArea.setFont( font ); // set the font to the Font object passed in
	} // end method setFont
	
	// method to get the current font of the text area
	public Font getFont()
	{
		return textArea.getFont(); // return the current font
	} // end method getFont
	
	// method to set the word wrap style of the text area
	public void setWrapStyleWord( boolean wrapStyleWord )
	{
		textArea.setWrapStyleWord( wrapStyleWord ); // set the word wrap style as specified
		formatWordWrapMenuItem.setSelected( wrapStyleWord ); // set the word wrap menu item to be checked or unchecked as specified
	} // end method setWrapStyleWord
	
	// method to copy the currently highlighted text in the text area
	public void copy()
	{
		textArea.copy(); // copy the currently highlighted text
	} // end method copy
	
	// method to cut the currently highlighted text in the text area
	public void cut()
	{
		textArea.cut(); // cut the currently highlighted text
	} // end method cut
	
	// method to paste the text currently in the clipboard to the text area
	public void paste()
	{
		textArea.paste(); // paste the text currently in the clipboard
	} // end method paste
	
	// method to set the text of the text area
	public void setText( String text )
	{
		textArea.setText( text ); // set the text as per the string passed in
	} // end method setText
	
	// method to get the word wrap style of the text area
	public boolean getWrapStyleWord()
	{
		// return true if word wrap is activated, false if not
		return textArea.getWrapStyleWord();
	} // end method getWrapStyleWord

	// method to read text into the text area from a buffered reader
	public void readText( BufferedReader bufferedReader ) throws IOException
	{
		// read the text from the buffered reader
		// if an IOException is encountered, this will be handled by the calling method
		textArea.read( bufferedReader, null );
	} // end method readText
	
	// method to get the current position of the caret in the text area
	public int getCaretPosition()
	{
		return textArea.getCaretPosition(); // return the current caret position
	} // end method getCaretPosition
} // end class TextEditorGUI