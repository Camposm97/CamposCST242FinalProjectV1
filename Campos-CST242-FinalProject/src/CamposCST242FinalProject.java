/**
 * Class: CamposCST242FinalProject
 * Author: Michael Campos
 * Course: CST242-FA17
 * Due: 12/20/2017 by 11:59PM
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CamposCST242FinalProject extends Application 
{
    //Instance Variable Section
    private static final String DIRECTORY = "H:\\NetBeansProjects\\CST242 - Production\\Campos-CST242-FinalProject\\src\\";
    /**CHANGE DIRECTORY IF ON DIFFERENT COMPUTER FOR FILES TO LOAD: 
     * Config.txt
     * AccountList.txt
     */
    
    // Error Messages
    private String[] errorMsgs = {"Oh uh, something went wrong...", "I didn't do it! I swear!", "Oh no! I am error!", "You broke the program!",
        "I let you down. Sorry :(", "I'm not suppose to be here...", "This doesn't make any sense!",
        "You found me! :D", "Hello, I'm a message for your error :)", "ERROR ERROR ERROR",
        "Aw geez, this wasn't suppose to happen.", "Woopsy daisy!", "My bad.", "I didn't do it! I swear!"};
    
    // ALL WILL BE READ IN: Config.txt
    private String appTitle = "", appVersion = "", appLicensedTo = "", appDBName = "";	// Will Be Read in Config
    private String appPurpose = "", dbUser = "", dbPassword = "";	//  Will Be Read in Config
    private String currentJournal = "CREATE OR OPEN A JOURNAL";
    
    //Create Map For Journals & MaxID#
    private static HashMap<String, Integer> hashMapJournal;
        
    private Label lblJournal; //Defined in getleftPane()
    private BorderPane root; /**THE REAL MAIN PANE**/
        
    //Date of Transaction
    private DatePicker dateOfEntry; //Defined in Constructor
    private Connection c; //Defined in initializeDB()
    
    //Create Account Objects 
    private Account acctDebit; 
    private Account acctCredit;
    
    //Create ViewEntries & TrialBalance Object
    private ViewJournalEntries viewEntries; 
    private TrialBalance trialBalance; 
        
    public CamposCST242FinalProject() {
        readInConfig(); //Read Config.txt
        initializeDB(); //Connect to Database
        
        //Define DatePicker
        dateOfEntry = new DatePicker();
        
        //Define Account Instances
        acctDebit = new Account(true, currentJournal, DIRECTORY, c);
        acctCredit = new Account(false, currentJournal, DIRECTORY, c);
        showAboutDialog();
    } //Constructor

    @Override
    public void start(Stage primaryStage) {
        setRootProperties();
        
        //Create Scene
        Scene mainScene = new Scene(root,1280,720);
        
        //Set Stage Properties
        primaryStage.setTitle(appTitle); //Title
        primaryStage.setScene(mainScene);
        primaryStage.show(); //Display Stage
    }
    
    public void setRootProperties() {
    	//Define centralPane
        root = new BorderPane();
        root.setStyle("-fx-background-color: lightblue;"
                + "-fx-font-size: 10pt;"
                + "-fx-font-family: Verdana;");
        
        //Add Nodes to centralPane
        root.setCenter(getSimpleEntryPane());
        root.setTop(getCentralTopPane());
    }
    
    //MENU BAR (CENTRAL TOP PANE)
    public MenuBar getCentralTopPane() {
        //Create MenuBar
        MenuBar menuBar = new MenuBar();

        //Create Menus
        //menuFile
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(
                getFileItem("New Journal"), 
                getFileItem("Open Journal"), 
                getFileItem("Delete Journal"), 
                getFileItem("Exit"));
        
        //menuEntry
        Menu menuEntry = new Menu("Account");
        menuEntry.getItems().addAll(
                getAccountItem("Simple Entry"),
                getAccountItem("Compound Entry"));
        
        //menuView
        Menu menuView = new Menu("View");
        menuView.getItems().addAll(
                getViewItem("Journal Entries"),
                getViewItem("Trial Balance"));
        
        //menuHelp
        Menu menuHelp = new Menu("Help");
        menuHelp.getItems().addAll(
                getHelpItem("Help Contents"),
                getHelpItem("Accounting 101"),
                getHelpItem("About"));
        
        //Add MenuFiles to MenuBar
        menuBar.getMenus().addAll(menuFile, menuEntry, menuView, menuHelp);

        return menuBar;
    } //For centralPane
    
    //SIMPLE ENTRY PANE
    public BorderPane getSimpleEntryPane() { 
        //Create BorderPane (MAIN PANE)
        BorderPane mainPane = new BorderPane();
        
        //Set Nodes for BorderPane
        mainPane.setTop(getSimpleEntryTopPane());
        mainPane.setCenter(getSimpleEntryCenterPane());
        mainPane.setBottom(getSimpleEntryBottomPane());
        
        return mainPane;
    } //Pane For Journalizing Entries    
    
    private GridPane getSimpleEntryTopPane() {
        //Create GridPane (CENTERPANE)
        GridPane centerPane = new GridPane();

        //Set GridPane Properties
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(20,10,10,10));
        centerPane.setVgap(10);
        centerPane.setHgap(10);
        
        //Add Nodes to GridPane (1ST COLUMN)
        centerPane.add(new Label("Date:"), 0, 0);
        centerPane.add(dateOfEntry.getDatePicker(), 0, 1);
        centerPane.add(new Label("\t\t\t\t\t\tNote:"), 0, 4);
        
        //2ND COLUMN
        centerPane.add(new Label("Account Title"), 1, 0);
        centerPane.add(acctDebit.getComboBox(), 1, 1);
        centerPane.add(acctCredit.getComboBox(), 1, 3);
        centerPane.add(acctCredit.gettfNote(), 1, 4);

        //3RD COLUMN
        centerPane.add(new Label("Debit"), 2, 0);
        centerPane.add(acctDebit.gettfDebit(), 2, 1);
        centerPane.add(acctCredit.gettfDebit(), 2, 3);

        //4TH COLUMN
        centerPane.add(new Label("Credit"), 3, 0);
        centerPane.add(acctDebit.gettfCredit(), 3, 1);
        centerPane.add(acctCredit.gettfCredit(), 3, 3);

        return centerPane;
    } //For Account Pane
    
    private GridPane getSimpleEntryCenterPane() {
        //Create GridPane 
        GridPane leftPane = new GridPane();
        leftPane.setAlignment(Pos.TOP_CENTER);
        leftPane.setPadding(new Insets(5));
        leftPane.setVgap(5);
        leftPane.setHgap(5);
        
        //Define lblJournal
        lblJournal = new Label(currentJournal);
        
        //1ST COLUMN
        leftPane.add(new Label("Current Journal: "), 0, 0);
        leftPane.add(acctDebit.getlblAcct(), 0, 1);
        leftPane.add(acctCredit.getlblAcct(), 0, 2);

        //2ND COLUMN
        leftPane.add(lblJournal, 1, 0);
        leftPane.add(acctDebit.getAccountUI(), 1, 1);
        leftPane.add(acctCredit.getAccountUI(), 1, 2);
        return leftPane;
    } //For Account Pane    
    
    private HBox getSimpleEntryBottomPane() {
        //Create HBox
        HBox bottomPane = new HBox(10);
        bottomPane.setPadding(new Insets(10));
        bottomPane.setAlignment(Pos.TOP_CENTER);
        
        //Create Button
        Button btJournalize = new Button("Record Transaction");
        btJournalize.setPrefWidth(192);
        
        btJournalize.setOnAction(e -> { //START OF LAMBDA EXPRESSION
            journalizeSimpleEntry();
        }); //END OF LAMBDA EXPRESSION
        
        //Add Node(s) to bottomPane
        bottomPane.getChildren().addAll(btJournalize);
        
        return bottomPane;
    } //For Account Pane
    
    //MENU ITEMS FOR MENUS
    private MenuItem getFileItem(String menuItemName) {
        //Create MenuItem
        MenuItem menuFileItem = new MenuItem();
        
        switch (menuItemName) {
            case "New Journal": //Creates a New Journal
                menuFileItem.setText("New Journal...");
                menuFileItem.setOnAction(e -> { 
                    showNewJournal();
                    System.out.println("Current Journal: " + currentJournal);
                });
                break;
                
            case "Open Journal": //Changes currentJournal
                menuFileItem.setText("Open Journal");
                menuFileItem.setOnAction(e -> {
                    showOpenJournal();
                    System.out.println("Current Journal: " + currentJournal);
                });
                break;
                
            case "Delete Journal":
                menuFileItem.setText("Delete Journal");
                menuFileItem.setOnAction(e -> {
                    showDeleteJournal();
                    System.out.println("Current Journal: " + currentJournal);
                });
                break;
                
            case "Exit":
                menuFileItem.setText("Exit");
                menuFileItem.setOnAction(e -> {
                    showExitDialog();
                });
                break;
        }
        return menuFileItem;
    } //Executed in getCentralTopPane()
    
    private MenuItem getAccountItem(String menuItemName) {
        //Create MenuItem
        MenuItem menuAccountItem = new MenuItem();
      
        switch (menuItemName) {
            case "Simple Entry":
                menuAccountItem.setText("Simple Entry");
                menuAccountItem.setOnAction(e -> 
                        root.setCenter(getSimpleEntryPane()));
                break;

            case "Compound Entry":
                menuAccountItem.setText("Compound Entry");
                menuAccountItem.setOnAction(e -> {
                    showCompoundEntryDialog();
                });
                break;
        }
        return menuAccountItem;
    } //Executed in getCentralTopPane()
    
    private MenuItem getViewItem(String menuItemName) {
        //Create MenuItem
        MenuItem menuViewItem = new MenuItem();
        
        switch (menuItemName) {
            case "Journal Entries": //Views List of Entered Transactions
                menuViewItem.setText("Journal Entries");
                menuViewItem.setOnAction(e -> {
                    try {
                        viewEntries
                                = new ViewJournalEntries(currentJournal, c);
                        root.setCenter(
                                viewEntries.getJournalEntriesPane());
                    } 
                    catch (SQLException ex) {
                        ExceptionDialog(ex, true);
                    }
                    //View Transactions in a List
                });
                break;

            case "Trial Balance":
                menuViewItem.setText("Trial Balance");
                menuViewItem.setOnAction(e -> {
                    try {
                        trialBalance = new TrialBalance(
                                currentJournal, DIRECTORY, c);
                        root.setCenter(
                                trialBalance.getTrialBalance());
                    } 
                    catch (Exception ex) {
                        ExceptionDialog(ex, true);
                    }
                });
                break;
        }
        return menuViewItem;
    } //Executed in getCentralTopPane()
    
    private MenuItem getHelpItem(String menuItemName) {
        //Create MenuItem
        MenuItem menuHelpItem = new MenuItem();
        
        switch (menuItemName) { 
            case "Accounting 101":
                menuHelpItem.setText("Accounting 101");
                menuHelpItem.setOnAction(e -> {
                    System.out.println("ACC101 NOT IMPLEMENTED YET");
                });
                break;
            
            case "About": //Execute showAboutDialog()
                menuHelpItem.setText("About");
                menuHelpItem.setOnAction(e -> {
                    showAboutDialog();
                });
                break;
                
            case "Help Contents":
                menuHelpItem.setText("Help Contents");
                menuHelpItem.setOnAction(e -> {
                    System.out.println("Help Contetns NOT IMPLEMENTED YET");
                });                
        }
        return menuHelpItem;
    } //Executed in getCentralTopPane()
    
    //DIALOGS FOR MENU ITEMS
    private void showNewJournal() {
        Alert alert;
        TextInputDialog inputDialog;
        
        inputDialog = new TextInputDialog();
        inputDialog.setTitle("New Journal");
        inputDialog.setHeaderText(
                "Please enter a journal name to create a new journal.");
        inputDialog.setContentText("Journal Name: ");

        Optional<String> result = inputDialog.showAndWait();

        //Get Response Value
        if (result.isPresent()) {
            if (!result.get().isEmpty() 
                    && isJournalNameValid(result.get(), hashMapJournal)) {
                //Add Result to Map & Set Accounts
                hashMapJournal.put(result.get(), -1);
                currentJournal = result.get();
                acctDebit.setTable(currentJournal);
                acctCredit.setTable(currentJournal);
                lblJournal.setText(currentJournal);
                root.setCenter(getSimpleEntryPane());
                
                //Create New Table in Database
                try {
                    Statement addTable = c.createStatement();
                    addTable.execute(
                            "CREATE TABLE " + appDBName + "."
                            + result.get() + " ("
                            + "recordId INT NOT NULL, "
                            + "dateOfTransaction DATE NOT NULL, "
                            + "accountTitle VARCHAR(50) NOT NULL, "
                            + "debit DECIMAL(10,2) NOT NULL, "
                            + "credit DECIMAL(10,2) NOT NULL, "
                            + "note VARCHAR(128) NULL, "
                            + "PRIMARY KEY (recordId));");
                    
                    //Show That The Table Created Was Successful
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("New Journal");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Created Journal: " 
                            + result.get());
                    alert.showAndWait();
                } catch (SQLException e) {
                    ExceptionDialog(e, true);
                }
            }
            else { //Journal Name is Invalid
                new CustomAlert().Create(
                        AlertType.ERROR,"New Journal",
                        null, "The new journal you attempted to create is"
                        + " invalid for the possible reasons:\n"
                        + "1. The textfield is empty.\n"
                        + "2. It has an uppercase letter in it.\n"
                        + "3. It has a space in it.\n"
                        + "4. It has a punctuation mark in it.\n"
                        + "5. It has a number in it.\n"
                        + "6. It exceeds 64 characters"
                        + "7. The journal already exists.");
            }
        }
    } //Executed When New Journal is Selected
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void showOpenJournal() {
        @SuppressWarnings("rawtypes")
		ChoiceDialog choiceDialog;
        choiceDialog = new ChoiceDialog(null, hashMapJournal.keySet());
        choiceDialog.setTitle("Open Journal");
        choiceDialog.setHeaderText("Please select a journal you'd like"
                + " to open.");
        choiceDialog.setContentText("Open Journal: ");
        
        @SuppressWarnings("unchecked")
		Optional<String> result = choiceDialog.showAndWait();
        
        //Get Response Value
        if (result.isPresent()) {
            currentJournal = result.get();
            acctDebit.setTable(currentJournal);
            acctCredit.setTable(currentJournal);
            lblJournal.setText(currentJournal);
            root.setCenter(getSimpleEntryPane());      
        }
    } //Executed When Open Journal is Selected
    
    private void showDeleteJournal() {
        @SuppressWarnings("rawtypes")
		ChoiceDialog choiceDialog;
        Alert confirmDelete;
        
        choiceDialog = new ChoiceDialog<>(
                null, hashMapJournal.keySet());
        choiceDialog.setTitle("Delete Journal");
        choiceDialog.setHeaderText("Please select a journal you'd"
                + " like to delete.");
        @SuppressWarnings("unchecked")
		Optional<String> result = choiceDialog.showAndWait();

        if (result.isPresent()) {
            confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setTitle("Delete Journal");
            confirmDelete.setHeaderText(null);
            confirmDelete.setContentText("Are you sure you want to"
                    + " delete journal: " + result.get() + "?");

            //Create ButtonTypes For confirmDelete
            ButtonType btYes = new ButtonType("Yes");
            ButtonType btNo = new ButtonType("No",
                     ButtonBar.ButtonData.CANCEL_CLOSE);

            confirmDelete.getButtonTypes().setAll(btYes, btNo);

            Optional<ButtonType> rs = confirmDelete.showAndWait();
            if (rs.get().equals(btYes)) 
            {
                //Commence Deletion of Selected Journal
                if (result.get().equals(currentJournal)) {
                    currentJournal = null;
                    acctDebit.setTable(currentJournal);
                    acctCredit.setTable(currentJournal);
                    lblJournal.setText(currentJournal);
                    root.setCenter(getSimpleEntryPane());
                    new CustomAlert().Create(AlertType.INFORMATION,
                            "Delete Journal", null,
                            "The journal you deleted: " + "\""
                            + result.get() + "\""+ " "
                            + "was the journal you had opened. "
                            + "\nIf you want to record a "
                            + "transaction you need to open or "
                            + "create a new journal.");
                } else {
                    //Do Nothing, Current Journal Is NOT Changed
                }

                //Drop Selected Table
                try {
                    Statement dropTable = c.createStatement();
                    dropTable.executeUpdate(
                            "DROP TABLE " + result.get() + ";");
                    hashMapJournal.remove(result.get());
                } catch (SQLException e) {
                    ExceptionDialog(e, true);
                }
            } 
            else {
                //User Chose to CANCEL Deletion
            }
        }
    } //Executed When Delete Journal is Selected
    
    private void showCompoundEntryDialog() {
        //Create Alert
        Alert ceDialog = new Alert(AlertType.INFORMATION);
        ceDialog.setTitle("Compound Entry");
        ceDialog.setHeaderText("In order to record a compound entry\n"
                + "you must enter a number of accounts that the\n"
                + "compound entry will be debiting and crediting.");
        
        //Create GridPane
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        //Create TextFields
        TextField numOfDebits = new TextField();
        numOfDebits.setPrefColumnCount(3);
        numOfDebits.setAlignment(Pos.CENTER);
        
        
        TextField numOfCredits = new TextField();
        numOfCredits.setPrefColumnCount(3);
        numOfCredits.setAlignment(Pos.CENTER);
        
        //1ST COLUMN (0,X)
        gridPane.add(new Label("Debit Accounts: "), 0 , 0);
        gridPane.add(new Label("Credit Accounts: "),0 , 1);
        
        //2ND COLUMN (1,X)
        gridPane.add(numOfDebits, 1, 0);
        gridPane.add(numOfCredits, 1, 1);
        
        //Add inputPane to ceDialog
        ceDialog.getDialogPane().setContent(gridPane);
        
        ButtonType btCreate = 
                new ButtonType("Create");
        ButtonType btCancel = 
                new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        
        ceDialog.getButtonTypes().setAll(btCreate, btCancel);
        
        Optional<ButtonType> result = ceDialog.showAndWait();
        
        if(result.get() == btCreate) {
            try {
                int debitAccts = Integer.parseInt(numOfDebits.getText());
                int creditAccts = Integer.parseInt(numOfCredits.getText());
                
                createCompoundEntry(debitAccts, creditAccts);  
            }
            catch (Exception e) {
                ExceptionDialog(e, true);
            }
            
        } else {
            //User Chose To Cancel
        }
    } //Executed When Compound Entry is Selected
    
    public void showExitDialog() {
        Alert alert;
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you "
                + "want to exit " + appTitle + "?");
        alert.getDialogPane().setExpandableContent(null);

        //Create ButtonTypes
        ButtonType btYes
                = new ButtonType("Yes");
        ButtonType btNo
                = new ButtonType("No",
                        ButtonData.CANCEL_CLOSE);

        //Add ButtonTypes To MenuItem
        alert.getButtonTypes().setAll(btYes, btNo);

        //Result For Button Pressed
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btYes) {
            System.exit(0); //Exit Program
        } else {
            //User Chose NOT To Exit Program
        }
    } //Executed When Exit is Selected
    
    private void showAboutDialog() {
        new CustomAlert().Create(AlertType.INFORMATION,
                "About", appTitle, 
                appPurpose + "\n"
                + "\nVersion:\t\t\t" + appVersion
                + "\nLicensed To:\t\t" + appLicensedTo
                + "\nDatabase Name:\t" + appDBName
                + "\nDB User:\t\t\t" + dbUser
                + "\nDB Password:\t\t" + dbPassword);
    } //Executed When About is Selected
    
    //CRITICAL METHODS FOR THE ABOVE
    private void initializeDB() {
        //Define journalList
        hashMapJournal = new HashMap<>();
         try {
            //Load the JDBC Driver
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver Loaded");

            //Connect to a Database
            c = DriverManager.getConnection("jdbc:mysql://"
                    + "localhost/accounting101", dbUser, dbPassword);
            
            System.out.println("Sucessfully Connected to Database: "
                    + c.getCatalog());

            //Set appDBName
            appDBName = c.getCatalog();
            
            //Set Keys & Values For hashMapJournal
            setHashJournalMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    } //Executed in Constuctor
    
    private void setHashJournalMap() throws SQLException {
        DatabaseMetaData dbMetaData = c.getMetaData();

        Statement stmt = c.createStatement(); //For Max ID

        ResultSet rsTable = dbMetaData.getTables(null, null, null,
                new String[]{"TABLE"}); //For Retrieving TableName From DB

        ResultSet rsMaxID; //For Retrieving MaxID in Table

        //Get Table Names
        while (rsTable.next()) 
        {
            rsMaxID = stmt.executeQuery(
                    "SELECT MAX(recordId) FROM "
                    + rsTable.getString("TABLE_NAME"));
            while (rsMaxID.next()) 
            {   
                if (rsMaxID.getString(1) == null) { //For Empty Journals
                    hashMapJournal.put(rsTable.getString(3), -1);
                } else {
                    hashMapJournal.put(rsTable.getString("TABLE_NAME"),
                        Integer.valueOf(rsMaxID.getString(1)));
                }
            }
        }
        Set<Map.Entry<String, Integer>> entrySet = hashMapJournal.entrySet();
        for (Map.Entry<String, Integer> e : entrySet) {
            System.out.printf("Journal: %-15sMaxID: %-5s\n", e.getKey(), e.getValue()); //DELETE
        }
    } //Executed Once in initializeDB()

    private void readInConfig() {
        String[] configData;
        String line;
        Scanner inputFile;

        try {
            inputFile = new Scanner(new File(DIRECTORY + "Config.txt"));

            while (inputFile.hasNextLine()) {
                line = inputFile.nextLine();
                configData = line.split("::");

                switch (configData[0]) {
                    case "TITLE":
                        appTitle = configData[1];
                        break;
                    case "VERSION":
                        appVersion = configData[1];
                        break;
                    case "LICENSED TO":
                        appLicensedTo = configData[1];
                        break;
                    case "PURPOSE":
                        appPurpose += configData[1];
                        break;
                    case "USER":
                        dbUser = configData[1];
                        break;
                    case "PASSWORD":
                        dbPassword = configData[1];
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            ExceptionDialog(e, true);
        }
    } //Executed in Constructor
    
    private void journalizeSimpleEntry() {
        try {
            if (acctDebit.isEntryValid() && acctCredit.isEntryValid() 
                    && acctDebit.getBalance() == acctCredit.getBalance()) 
            {
                //Records Transactions
                acctDebit.recordTransaction(dateOfEntry.getDate(),
                        hashMapJournal);
                acctCredit.recordTransaction(dateOfEntry.getDate(),
                        hashMapJournal);

                new CustomAlert().Create(AlertType.INFORMATION,
                        "Record Transaction", null,
                        "Successfully Recorded Transaction!");
            } 
            else 
            { //Invalid Transaction Input
                new CustomAlert().Create(AlertType.ERROR,
                        "Invalid Entry", null,
                        "Entry is invalid.\n"
                        + "Make sure you have the following:\n"
                        + "1. A Selected Journal.\n"
                        + "2. TOTAL Debit and "
                        + "TOTAL Credit are equal.\n"
                        + "3. Note is less than 128 characters");
            }
        } catch (Exception e) {
            ExceptionDialog(e, true);
        }
    } //Executed When Button "Journalize" is Pressed
    
    private void journalizeCompoundEntry
        (Account[] debitAccts, Account[] creditAccts) 
    {
        try {
            int acctNum;
            int totalDebit = 0;
            int totalCredit = 0;
            
            //Check If All Debit & Credit Accounts Are Valid
            for (acctNum = 0; acctNum < debitAccts.length; acctNum++) {
                if (debitAccts[acctNum].isEntryValid()) {
                    System.out.println(
                            "Debit Entry #" + acctNum + " is Valid\n");
                    totalDebit += debitAccts[acctNum].getBalance();
                } else {
                    System.out.println(
                            "Debit Entry #" + acctNum + " is Invalid\n");
                }
            }
            for (acctNum = 0; acctNum < creditAccts.length; acctNum++) {
                if (creditAccts[acctNum].isEntryValid()) {
                    System.out.println(
                            "Credit Entry #" + acctNum + " is Valid\n");
                    totalCredit += creditAccts[acctNum].getBalance();
                } else {
                    System.out.println(
                            "Credit Entry #" + acctNum + "is Invalid\n");
                }
            }
            
            //Check If totalDebit Equals totalCredit
            if (totalDebit == totalCredit) {
                System.out.println("Recording Transactions...");
                //Record Debit Transations
                for (acctNum = 0; acctNum < debitAccts.length; acctNum++) {
                    debitAccts[acctNum].
                            recordTransaction(dateOfEntry.getDate(), hashMapJournal);
                }
                
                //Record Credit Transactions
                for (acctNum = 0; acctNum < creditAccts.length; acctNum++) {
                    creditAccts[acctNum].
                            recordTransaction(dateOfEntry.getDate(), hashMapJournal);
                }
                
                new CustomAlert().Create(AlertType.INFORMATION,
                        "Record Transaction", null, 
                        "Successfully Recorded Transaction!");
                
            } else {
                System.out.println("Total Debit(" + totalDebit + ")"
                        + " and Total Credit (" + totalCredit + ")"
                        + " DO NOT EQUAL EACH OTHER\n");
            }
            
        } catch (Exception e) {
            ExceptionDialog(e, true);
        }
    } //Executed When Button "Journalize" is Pressed
    
    private boolean isJournalNameValid(String journalName, HashMap<String, Integer> map) 
    {
        if (!map.containsKey(journalName) && journalName.length() <= 64) {
            for (int index = 0; index < journalName.length(); index++) 
            {
                char c = journalName.charAt(index);

                if (!Character.isAlphabetic(c) 
                        || Character.isSpace(c) 
                        || Character.isUpperCase(c) 
                        || Character.isDigit(c)) 
                {
                    return false;
                }
                /**
                 * Note. The reason the journal name can ONLY be letters that
                 * are lower case is because SQL (by default) only accepts those
                 * terms. However, the restrictions for the journal name can be
                 * changed in the SQL program, NOT in the my program.
                 *
                 * Suggestion (To myself) - Maybe I can figure out a way how to
                 * change the restrictions when the program launches if SQL
                 * allows that through Connection of the database. Chance of
                 * Success (My Opinion) - Very Low
                 */
            }
        } else {
            return false;
        }
        return true;
    } //Executed During Execution of showNewJournal();
    
    //COMPOUND ENTRY
    private void createCompoundEntry(int debitAccts, int creditAccts) {
        //Create BorderPane (MAIN PANE FOR COMPOUND ENTRY)
        BorderPane borderPane = new BorderPane();
       
        //Create GridPane (TOP PANE)
        GridPane topPane = new GridPane();
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(new Insets(10));
        topPane.setVgap(10);
        topPane.setHgap(10);
        
        //Create GridPane (CENTER PANE)
        GridPane centerPane = new GridPane();
        centerPane.setAlignment(Pos.TOP_CENTER);
        centerPane.setPadding(new Insets(10));
        centerPane.setVgap(10);
        centerPane.setHgap(10);
        
        //Create HBox (BOTTOM PANE)
        HBox bottomPane = new HBox(10);
        bottomPane.setPadding(new Insets(10));
        bottomPane.setAlignment(Pos.CENTER);
        
        //Create an Array of Accounts for Debit & Credit
        Account[] debitAcctArray = new Account[debitAccts];
        Account[] creditAcctArray = new Account[creditAccts];
        int acctNum;
        
        //Create Debit Accounts
        for (acctNum = 0; acctNum < debitAcctArray.length; acctNum++) {
            debitAcctArray[acctNum] =
                    new Account(true, currentJournal, DIRECTORY, c);
            System.out.println("Created Debit Account #" + acctNum);
            
            //Add ComboBox to topPane: 2nd Col.
            topPane.add(debitAcctArray[acctNum].
                    getComboBox(), 1, acctNum + 1);
            
            //Add tfDebit to topPane: 3rd Col.
            topPane.add(debitAcctArray[acctNum].
                    gettfDebit(), 2, acctNum + 1);
            
            //Add tfCredit to topPane: 4th Col.
            topPane.add(debitAcctArray[acctNum].
                    gettfCredit(), 3, acctNum + 1);
            
            //Add lblAcct to centerPane: 1st Col.
            centerPane.add(debitAcctArray[acctNum].
                    getlblAcct(), 0, acctNum + 1);
            
            //Add AccountUI to centerPane: 2nd Col.
            centerPane.add(debitAcctArray[acctNum].
                    getAccountUI(), 1, acctNum + 1);
        }
        
        //Create Credit Accounts
        for (acctNum = 0; acctNum < creditAcctArray.length; acctNum++) {
            creditAcctArray[acctNum] =
                    new Account(false, currentJournal, DIRECTORY, c);
            System.out.println("Created Credit Account #" + acctNum);
            
            //Add ComboBox to topPane: 2nd Col.
            topPane.add(creditAcctArray[acctNum].
                    getComboBox(), 1, (acctNum + debitAccts + 1));
            
            //Add tfDebit to topPane: 3rd Col.
            topPane.add(creditAcctArray[acctNum].
                    gettfDebit(), 2, (acctNum + debitAccts + 1));
            
            //Add tfCredit to topPane: 4th Col.
            topPane.add(creditAcctArray[acctNum].
                    gettfCredit(), 3, (acctNum + debitAccts + 1));
            
            //Add lblAcct to centerPane: 1st Col.
            centerPane.add(creditAcctArray[acctNum].
                    getlblAcct(), 0, (acctNum + debitAccts + 1));
            
            //Add AccountUI to centerPane: 2nd Col.
            centerPane.add(creditAcctArray[acctNum].
                    getAccountUI(), 1, (acctNum + debitAccts + 1));
        }
        
        //Create Journalize Button
        Button btJournalize = new Button("Record Transaction");
        btJournalize.setPrefWidth(192);
        btJournalize.setOnAction(e ->{
            journalizeCompoundEntry(
                    debitAcctArray, creditAcctArray);
        });
                
        //Add Nodes to topPane: 1st Col.
        topPane.add(new Label("Date:"), 0, 0); 
        topPane.add(dateOfEntry.getDatePicker(), 0, 1);
        
        //2nd Col.
        topPane.add(new Label("Account Title"), 1, 0); 

        //3rd Col.
        topPane.add(new Label("Debit"), 2, 0); 
        
        //4th Col.
        topPane.add(new Label("Credit"), 3, 0);
        
        //Add currentJournal to centerPane: 1st Col.
        centerPane.add(new Label("Current Journal: "), 0, 0);
        centerPane.add(new Label(currentJournal), 1, 0);
        
        //Add btJournalize to bottomPane
        bottomPane.getChildren().add(btJournalize);
        
        //Set Nodes for borderPane
        borderPane.setTop(topPane);
        borderPane.setCenter(centerPane);
        borderPane.setBottom(bottomPane);
        
        //Set centralPane's Center Pane
        root.setCenter(borderPane);
    } //Executed When Compound Entry is Selected Under Account    
    
    public void ExceptionDialog(Exception e, boolean ShowStackTrace) {
        Alert alert;
        alert = new Alert(AlertType.ERROR);
        alert.setTitle(e.getClass().getSimpleName());
        alert.setHeaderText(getRandomErrorMsg() + "\n"
                + "Error: " + e.getMessage());
        alert.setContentText("The "
                + e.getClass().getSimpleName()
                + " stacktrace was: ");
        
        alert.getButtonTypes().setAll(
                new ButtonType("Ok", ButtonData.OK_DONE));

        if (ShowStackTrace) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionText = sw.toString();

            //Create TextArea For Exception StackTrace
            TextArea textArea = new TextArea(exceptionText);
            textArea.setPadding(new Insets(2));
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            //Expand Content with Node
            alert.getDialogPane().setExpandableContent(textArea);
            alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        }
        alert.showAndWait();
    }
    
    private String getRandomErrorMsg() {
        return errorMsgs[(int)(Math.random() * errorMsgs.length)];
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}