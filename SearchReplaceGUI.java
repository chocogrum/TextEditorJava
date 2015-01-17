/**************************************************************
 ** Project 2: SearchReplaceGUI.java
 ** Description: Define a search and replace GUI for a piece of text
 ** Name: Graham Thomas
 ** Student Number: 1479585
**************************************************************/

// import required classes
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;

// declare class to represent a search and replace GUI for a piece of text
public class SearchReplaceGUI extends JFrame // inherit from class JFrame
{
	/**************************************************************
	 ** Declare GUI components                                   **
	 **************************************************************/
	 
	private Box leftBox; // box to hold the components on the left side of the GUI
	private Box rightBox; // box to hold the components on the right side of the GUI
	private JPanel searchPanel; // panel to hold the search label and text field
	private JPanel replacePanel; // panel to hold the replace label and text field
	private JTextField searchField; // text field for the search term
	private JTextField replaceField; // text field for the replace term
	private JLabel searchLabel; // search label
	private JLabel replaceLabel; // replace label
	private JCheckBox caseSensitiveCheckBox; // check box to toggle case sensitivity in search
	private JButton nextButton; // button to search for the next occurrence of the search term
	private JButton previousButton; // button to search for the previous occurrence of the search term
	private JButton replaceButton; // button to replace the current selection with the search term
	private JButton replaceAllButton; // button to replace all occurrences of the search term with the replace term
	
	// begin constructor
	public SearchReplaceGUI()
	{
		// call superclass constructor
		super();
		
		/**************************************************************
		 ** Initialise individual GUI components                     **
		 **************************************************************/
	 
		leftBox = Box.createVerticalBox(); // initialise left hand box to add components in vertical layout
		rightBox = Box.createVerticalBox(); // initialise right hand box to add components in vertical layout
		searchField = new JTextField( 20 ); // initialise search text field with 20 columns
		searchLabel = new JLabel( "Search for" ); // initialise search label
		searchPanel = new JPanel(); // initialise search panel
		replaceField = new JTextField( 20 ); // initialise replace text field with 20 columns
		replaceLabel = new JLabel( "Replace with" ); // initialise replace label
		replacePanel = new JPanel(); // initialise replace panel
		caseSensitiveCheckBox = new JCheckBox( "Case Sensitive" ); // initialise check box to toggle case sensitivity
		nextButton = new JButton( "Next" ); // initialise next button
		previousButton = new JButton( "Previous" ); // initialise previous button
		replaceButton = new JButton( "Replace" ); // initialise replace button
		replaceAllButton = new JButton( "Replace All" ); // initialise replace all button
		
		/**************************************************************
		 ** Add components to the GUI                                **
		 **************************************************************/
		
		searchPanel.setLayout( new FlowLayout( FlowLayout.LEFT ) ); // set left flow layout for search panel
		searchPanel.add( searchLabel ); // add search label to search panel
		searchPanel.add( searchField ); // add search text field to search panel
		
		replacePanel.setLayout( new FlowLayout( FlowLayout.LEFT ) ); // set left flow layout for replace panel
		replacePanel.add( replaceLabel ); // add replace label to replace panel
		replacePanel.add( replaceField ); // add replace text field to replace panel
		
		leftBox.add( searchPanel ); // add search panel to left side of GUI
		leftBox.add( caseSensitiveCheckBox ); // add check box to toggle case sensitivity to left side of GUI
		leftBox.add( replacePanel ); // add replace pabel to left side of GUI
		
		rightBox.add( nextButton ); // add next button to right side of GUI
		rightBox.add( Box.createVerticalStrut( 5 ) ); // create spacing of five pixels between next button and previous button
		rightBox.add( previousButton ); // add previous button to right side of GUI
		rightBox.add( Box.createVerticalStrut( 5 ) ); // create spacing of five pixels between previous button and replace button
		rightBox.add( replaceButton ); // add replace button to right side of GUI
		rightBox.add( Box.createVerticalStrut( 5 ) ); // create spacing of five pixels between replace button and replace all button
		rightBox.add( replaceAllButton ); // add replace all button to right side of GUI
		
		setLayout( new FlowLayout( FlowLayout.LEFT ) ); // set left flow layout for the parent frame 
		add( leftBox ); // add left hand side box to the parent frame
		add( rightBox ); // add right hand side box to the parent frame
		
		showReplace( false ); // set the replace functionality to be hidden initially
		setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE ); // set frame to hide when user closes the window
	} // end constructor
	
	/**************************************************************
	 ** Define methods to register event handlers                **
	 **************************************************************/
	
	// method to register a handler for the Next button
	public void nextButtonRegisterHandler( ActionListener actionListener )
	{
		// register event handler for the Next Button
		nextButton.addActionListener( actionListener );
	} // end method nextButtonRegisterHandler
	
	// method to register a handler for the Previous button
	public void previousButtonRegisterHandler( ActionListener actionListener )
	{
		// register event handler for the Previous Button
		previousButton.addActionListener( actionListener );
	} // end method previousButtonRegisterHandler
	
	// method to register a handler for the Replace button
	public void replaceButtonRegisterHandler( ActionListener actionListener )
	{
		// register event handler for the Replace Button
		replaceButton.addActionListener( actionListener );
	} // end method replaceButtonRegisterHandler
	
	// method to register a handler for the Replace All button
	public void replaceAllButtonRegisterHandler( ActionListener actionListener )
	{
		// register event handler for the Replace All Button
		replaceAllButton.addActionListener( actionListener );
	} // end method replaceAllButtonRegisterHandler
	
	// method to register a handler for the Search text field
	public void searchFieldRegisterHandler( ActionListener actionListener )
	{
		// register event handler for the Search text field
		searchField.addActionListener( actionListener );
	} // end method searchFieldRegisterHandler
	
	/**************************************************************
	 ** Define utility methods                                   **
	 **************************************************************/

	// method to get the text in the Search text field
	public String getSearchText()
	{
		return searchField.getText(); // return the search text
	} // end method getSearchText

	// method to get the text in the Replace text field
	public String getReplaceText()
	{
		return replaceField.getText(); // return the replacement text
	} // end method getReplaceText

	// method to determine whether the search should be case sensitive or not
	public boolean isCaseSensitive()
	{
		// return true if case sensitive check box is selected, false if not
		return caseSensitiveCheckBox.isSelected();
	} // end method isCaseSensitive
	
	// method to set the visibility of the replace functionality
	public void showReplace( boolean showReplace )
	{
		replacePanel.setVisible( showReplace ); // show/hide the replace panel
		replaceButton.setVisible( showReplace ); // show/hide the replace button
		replaceAllButton.setVisible( showReplace ); // show/hide the replace all button
		setTitle( ( showReplace ) ? "Search and Replace" : "Search" ); // set the title of the window as necessary
		pack(); // resize window to fit components
	} // end method showReplace
} // end class SearchReplaceGUI