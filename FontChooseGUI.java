/**************************************************************
 ** Project 2: FontChooseGUI.java
 ** Description: Define a GUI to select a new font
 ** Name: Graham Thomas
 ** Student Number: 1479585
**************************************************************/

// import required classes
import javax.swing.JFrame;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.Box;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import java.awt.FlowLayout;

// declare class to represent a font chooser GUI for a piece of text
public class FontChooseGUI extends JFrame // inherit from class JFrame
{
	/**************************************************************
	 ** Declare GUI components                                   **
	 **************************************************************/
	
	// declare list of possible fonts that the user can choose from
	private static final String FONT_NAMES[] = { Font.DIALOG, Font.DIALOG_INPUT, Font.MONOSPACED, Font.SERIF, Font.SANS_SERIF };
	private Integer[] fontSizes = new Integer[ 50 ]; // declare array of font sizes that the user can select
	private JLabel fontNameLabel; // font name label
	private JLabel fontSizeLabel; // font size label
	private JTextField fontNameTextField; // font name text field
	private JTextField fontSizeTextField; // font size text field
	private JList< String > fontNamesList; // list of font names eith element type String
	private JList< Integer > fontSizeList; // list of font sizes with element type Integer
	private JCheckBox boldCheckBox; // check box to indicate whether font should be bold
	private JCheckBox italicCheckBox; // check box to indicate whether font should be italic
	private JButton okButton; // OK button
	private JButton cancelButton; // Cancel button
	private Box leftBox; // box to hold the components on the left side of the GUI
	private Box rightBox; // box to hold the components on the right side of the GUI
	
	// begin constructor
	public FontChooseGUI()
	{
		super( "Choose Font" ); // call superclass constructor with window title
		
		/**************************************************************
		 ** Initialise individual GUI components                     **
		 **************************************************************/
		
		fontNamesList = new JList< String >( FONT_NAMES ); // initialise list of font names 
		fontNamesList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION ); // set font names list to allow only single selection
		fontNamesList.setVisibleRowCount( 5 ); // set the number of visible rows in the font names list to five
		
		boldCheckBox = new JCheckBox( "Bold" ); // initialise Bold checkbox
		italicCheckBox = new JCheckBox( "Italic" ); // initialise Italic checkbox
		fontNameLabel = new JLabel( "Font" ); // initialise Font Name label
		fontSizeLabel = new JLabel( "Size" ); // initialise Font Size label
		okButton = new JButton( "OK" ); // initialise OK button
		cancelButton = new JButton( "Cancel" ); // initialise Cancel button
		
		// initialise the array of font sizes with every even number from 2 to 100
		for( int i = 0; i < 50; i++ )
			fontSizes[ i ] = ( i * 2 ) + 2;
		
		fontSizeList = new JList< Integer >( fontSizes ); // initialise list of font sizes
		fontSizeList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION ); // set font sizes list to allow only single selection
		fontSizeList.setVisibleRowCount( 5 ); // set the number of visible rows in the font names list to five
		
		fontNameTextField = new JTextField( 20 ); // initialise the font name text field with 20 columns
		fontSizeTextField = new JTextField( 20 ); // initialise the font size text field with 20 columns
		
		leftBox = Box.createVerticalBox(); // initialise left hand box to add components in vertical layout
		rightBox = Box.createVerticalBox(); // initialise right hand box to add components in vertical layout
		
		/**************************************************************
		 ** Add components to the GUI                                **
		 **************************************************************/
		
		leftBox.add( fontNameLabel ); // add font name label to left side of GUI
		leftBox.add( fontNameTextField ); // add font name text field to left side of GUI
		leftBox.add( new JScrollPane( fontNamesList ) ); // add font names list within a scroll pane to left side of GUI
		leftBox.add( boldCheckBox ); // add Bold checkbox to left side of GUI
		leftBox.add( okButton ); // add OK button to left side of GUI
		
		rightBox.add( fontSizeLabel ); // add font size label to right side of GUI
		rightBox.add( fontSizeTextField ); // add font size text field to right side of GUI
		rightBox.add( new JScrollPane( fontSizeList ) ); // add font sizes list within a scroll pane to right side of GUI
		rightBox.add( italicCheckBox ); // add italic checkbox to right side of GUI
		rightBox.add( cancelButton ); // add Cancel button to right side of GUI
		
		setLayout( new FlowLayout() ); // set the layout of the parent frame to the default Flow Layout
		
		add( leftBox ); // add left hand side box to the parent frame using default layout
		add( rightBox ); // add right hand side box to the parent frame using default layout
		
		setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE ); // set frame to hide when user closes the window
		pack(); // resize window to fit components
	} // end constructor
	
	/**************************************************************
	 ** Define methods to register event handlers                **
	 **************************************************************/
	
	// method to register a handler for the Font Names list
	public void fontNameListRegisterHandler( ListSelectionListener listSelectionListener )
	{
		// register event handler for the Font Names list
		fontNamesList.addListSelectionListener( listSelectionListener );
	} // end method fontNameListRegisterHandler
	
	// method to register a handler for the Font Sizes list
	public void fontSizeListRegisterHandler( ListSelectionListener listSelectionListener )
	{
		// register event handler for the Font Sizes list
		fontSizeList.addListSelectionListener( listSelectionListener );
	} // end method fontSizeListRegisterHandler
	
	// method to register a handler for the OK button
	public void okButtonRegisterHandler( ActionListener actionListener )
	{
		// register event handler for the OK Button
		okButton.addActionListener( actionListener );
	} // end method okButtonRegisterHandler
	
	// method to register a handler for the Cancel button
	public void cancelButtonRegisterHandler( ActionListener actionListener )
	{
		// register event handler for the Cancel Button
		cancelButton.addActionListener( actionListener );
	} // end method cancelButtonRegisterHandler
	
	/**************************************************************
	 ** Define utility methods                                   **
	 **************************************************************/
	
	// method to set the text in the Font Name text field
	public void setFontNameText( String text )
	{
		// set the text in the Font Name text field as per the String passed in
		fontNameTextField.setText( text );
	} // end method setFontNameText
	
	// method to get the text in the Font Name text field
	public String getFontNameText()
	{
		return fontNameTextField.getText(); // return the text in the Font Name text field
	} // end method getFontNameText
	
	// method to set the currently selected font name in the Font Names list
	public void setSelectedFontName( String fontName )
	{
		// set the currently selected font name as per the string passed in if it exists in the list
		// second parameter set to true so scroll pane will scroll to selected item
		fontNamesList.setSelectedValue( fontName, true );
	} // end method setSelectedFontName
	
	// method to set the text of the Font Size text field
	public void setFontSizeText( String text )
	{
		// set the text in the Font Size text field as per the String passed in
		fontSizeTextField.setText( text );
	} // end method setFontSizeText
	
	// method to get the text in the Font Size text field
	public String getFontSizeText()
	{
		return fontSizeTextField.getText(); // return the text in the Font Size text field
	} // end method getFontSizeText
	
	// method to set the currently selected font size in the Font Size list
	public void setSelectedFontSize( int fontSize )
	{
		// set the currently selected font name as per the integer passed in if it exists in the list
		// second parameter set to true so scroll pane will scroll to selected item
		fontSizeList.setSelectedValue( fontSize, true );
	} // end method setSelectedFontSize
	
	// method to get the currently selected font name in the Font Names list
	public String getSelectedFontName()
	{
		return fontNamesList.getSelectedValue(); // return the currently selected font name
	} // end method getSelectedFontName
	
	// method to get the currently selected font size in the Font Sizes list
	public int getSelectedFontSize()
	{
		return fontSizeList.getSelectedValue(); // return the currently selected font size
	} // end method getSelectedFontSize
	
	// method to set the Bold checkbox to selected or unselected
	public void setBold( boolean bold )
	{
		boldCheckBox.setSelected( bold ); // set the Bold checkbox selected or unselected as per the parameter passed in
	} // end method setBold
	
	// method to check if Bold checkbox is selected or not
	public boolean isBold()
	{
		// return true if Bold checkbox is selected, false if not
		return boldCheckBox.isSelected();
	} // end method isBold
	
	// method to set the Italic checkbox to selected or unselected
	public void setItalic( boolean italic )
	{
		italicCheckBox.setSelected( italic ); // set the Italic checkbox selected or unselected as per the parameter passed in
	} // end method setItalic
	
	// method to check if Italic checkbox is selected or not
	public boolean isItalic()
	{
		// return true if Italic checkbox is selected, false if not
		return italicCheckBox.isSelected();
	} // end method isItalic
	
	// method to clear the current selection in the Font Sizes list
	public void clearSelectedFontSize()
	{
		// deselect the currently selected item in the Font Size list if there is one
		fontSizeList.clearSelection();
	} // end method clearSelectedFontSize
	
	// method to clear the current selection in the Font Names list
	public void clearSelectedFontName()
	{
		// deselect the currently selected item in the Font Names list if there is one
		fontNamesList.clearSelection();
	} // end method clearSelectedFontName
	
	// method to check if there is a font name selected in the Font Names list
	public boolean isFontNameSelected()
	{
		// return false if Font Name list has nothing selected, true if an item is selected
		return !fontNamesList.isSelectionEmpty();
	} // end method isFontNameSelected
	
	public boolean isFontSizeSelected()
	{
		// return false if Font Size list has nothing selected, true if an item is selected
		return !fontSizeList.isSelectionEmpty();
	} // end method isFontSizeSelected
} // end class FontChooseGUI