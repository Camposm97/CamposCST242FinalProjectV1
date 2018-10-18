

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class Account {

    /**
     * Purpose.
     * The critical purpose of the Account Class is to simplify the
     * occurrence of redundant code for reading in AccountList.txt and adding
     * the account types from the text file to the ComboBox. There are methods
     * for getting the currently selected item in the combo box and retrieving a
     * UI for switching account types
     *
     * The class also provides TextFields for debit and credit which will be
     * used for recording transactions in the journal. There's a method for
     * getting the total of debit and credit which will be used for checking if
     * this account and another are equal.
     */
    
    //Instance Variable Section
    private String tableName;
    private String directory;
    private boolean debitAcct; //CRITICAL FOR PROGRAM DECISION MAKING
    private ComboBox<String> comboBox;
    private Label lblAcct;
    private TextField tfDebit;
    private TextField tfCredit;
    private TextField tfNote;
    private PreparedStatement entry;
    private Connection connection;

    public Account(boolean debitAcct, String tableName, String directory,
            Connection connection) {
        this.debitAcct = debitAcct;
        this.tableName = tableName;
        this.directory = directory;
        this.connection = connection;
        comboBox = new ComboBox<>();
        tfDebit = new TextField("0.00");
        tfCredit = new TextField("0.00");
        tfNote = new TextField();
        getAccountType("ASSET-ACCOUNT");
        setNodeProperties();
        setlblAcct();
    } //Account Constructor with Args
    
    //PUBLIC GETTER METHODS
    public String getTable() {
        return this.tableName;
    } //Returns tableName

    public String getDebit() {
        return this.tfDebit.getText();
    } //Returns tfDebit.getText()

    public String getCredit() {
        return this.tfCredit.getText();
    } //Returns tfCredit.getText()

    public String getNote() {
        return this.tfNote.getText();
    } //Returns tfNote.getText()
    
    public double getBalance() {
        double balance = Double.parseDouble(getDebit())
                + Double.parseDouble(getCredit());
        return balance;
    }

    public String getAccount() {
        return this.comboBox.getValue();
    } //Returns Value of ComboBox

    public ComboBox<String> getComboBox() {
        return this.comboBox;
    } //Returns ComboBox
    
    public Label getlblAcct() {
        return this.lblAcct;
    } //Returns lblAcct

    public TextField gettfDebit() {
        return this.tfDebit;
    } //Returns tfDebit

    public TextField gettfCredit() {
        return this.tfCredit;
    } //Returns tfCredit

    public TextField gettfNote() {
        return this.tfNote;
    } //Returns tfNote

    public HBox getAccountUI() {
        //Create HBox
        HBox accountTypes = new HBox(10);
        
        //Set accountType Properties
        accountTypes.setAlignment(Pos.CENTER);

        //Create RadioButtons
        RadioButton rbtAsset = new RadioButton("Asset");
        RadioButton rbtLiability = new RadioButton("Liability");
        RadioButton rbtOwnersEquity = new RadioButton("Owner's Equity");
        RadioButton rbtRevenue = new RadioButton("Revenue");
        RadioButton rbtExpense = new RadioButton("Expense");
        
        /**
         * The purpose of the RadioButtons is for getAccountType() to
         * execute again and again when the user wants to switch account types.
         * It's a way to keep the accounts in a category so the user can easily
         * pick the accounts he or she needs for the transaction.
         */

        //Create ToggleGroup for RadioButtons
        ToggleGroup accountTypesGroup = new ToggleGroup();
        rbtAsset.setToggleGroup(accountTypesGroup);
        rbtLiability.setToggleGroup(accountTypesGroup);
        rbtOwnersEquity.setToggleGroup(accountTypesGroup);
        rbtRevenue.setToggleGroup(accountTypesGroup);
        rbtExpense.setToggleGroup(accountTypesGroup);
        accountTypesGroup.selectToggle(rbtAsset);

        //When RadioButton Is Pressed, getAccountType()
        rbtAsset.setOnAction(e -> {
            getAccountType("ASSET-ACCOUNT");
        });
        rbtLiability.setOnAction(e -> {
            getAccountType("LIABILITY-ACCOUNT");
        });
        rbtOwnersEquity.setOnAction(e -> {
            getAccountType("OWNERS-EQUITY-ACCOUNT");
        });
        rbtRevenue.setOnAction(e -> {
            getAccountType("REVENUE-ACCOUNT");
        });
        rbtExpense.setOnAction(e -> {
            getAccountType("EXPENSE-ACCOUNT");
        });

        //Add RadioButtons to HBox
        accountTypes.getChildren().addAll(rbtAsset, rbtLiability,
                rbtOwnersEquity, rbtRevenue, rbtExpense);

        return accountTypes;
    } //Returns Account UI
    
    //PUBLIC SETTER METHODS
    public void setDebit(String debit) {
        this.tfDebit.setText(debit);
    } //Sets tfDebit Text

    public void setCredit(String credit) {
        this.tfCredit.setText(credit);
    } //Sets tfCredit Text

    public void setTable(String tableName) {
        this.tableName = tableName;
    } //Sets Selected Table

    public void setDirectory(String directory) {
        this.directory = directory;
    } //Sets Directory

    public void setConnection(Connection connection) {
        this.connection = connection;
    } //Sets Connection
    
    //PUBLIC TRUE/FALSE METHOD
    public boolean isEntryValid() {
        if (tableName.isEmpty() || comboBox.getValue().isEmpty()
                || getDebit().isEmpty()
                || getCredit().isEmpty()
                || getNote().length() > 128) {
            return false;
        } 
        else {
            return true;
        }

    } //Returns True If Valid
    
    //INSERT INTO DATABASE METHOD
    public void recordTransaction(String date, HashMap<String, Integer> map)
            throws SQLException {
        entry = connection.prepareStatement(""
                + "INSERT INTO " + tableName + " ("
                + "recordId, "
                + "dateOfTransaction, "
                + "accountTitle, "
                + "debit, "
                + "credit, "
                + "note)"
                + "VALUES (?, ?, ?, ?, ?, ?);");

        entry.setInt(1, getNextID(map)); //Record ID
        entry.setString(2, date); //Date of Transaction
        entry.setString(3, getAccount()); //Account Title
        entry.setDouble(4, Double.parseDouble(getDebit())); //Debit
        entry.setDouble(5, Double.parseDouble(getCredit())); //Credit
        entry.setString(6, getNote()); //Comment

        entry.executeUpdate(); //Insert into DB

        System.out.println("Journalization Successful!");
        System.out.println("Accounted in: " + getTable());
        System.out.println("Account: " + getAccount()
                + "\nDebit: " + getDebit()
                + "\nCredit: " + getCredit());
        System.out.println("");
    }
    
    //PRIVATE METHODS
    private void getAccountType(String accountType) {
        ObservableList<String> accountList
                = FXCollections.observableArrayList();

        String[] accountData;
        String currentLine;
        Scanner accountFile;

        try {
            accountFile = new Scanner(
                    new File(directory + "AccountList.txt"));

            while (accountFile.hasNextLine()) {
                currentLine = accountFile.nextLine();
                accountData = currentLine.split("::");

                if (accountData[0].equalsIgnoreCase(accountType)) {
                    accountList.add(accountData[1]);
                }
            }
            comboBox.setValue(accountList.get(0));
            comboBox.getItems().setAll(accountList);

        } catch (FileNotFoundException e) {
            //Execute showExceptionDialog()
            e.printStackTrace();
        }
    }

    private int getNextID(HashMap<String, Integer> map) {
        int currentId = map.get(tableName);
        int nextID = currentId + 1;

        //Update Journal Name in Value
        map.replace(tableName, currentId, nextID);

        System.out.println("Current ID: " + currentId);
        System.out.println("Next ID: " + nextID);
        System.out.println();
        return nextID;
    }
    
    private void setlblAcct() {
        if (debitAcct) {
            this.lblAcct = new Label("Debit:");
        } else {
            this.lblAcct = new Label("Credit:");
        }
    }

    private void setNodeProperties() {
        comboBox.setPrefWidth(320);
        
        if (debitAcct) { //Debit Account
            tfCredit.setPrefColumnCount(5);
            tfCredit.setEditable(false);
            tfCredit.setOpacity(0);
        }
        else { //Credit Account
            tfDebit.setPrefColumnCount(5);
            tfDebit.setEditable(false);
            tfDebit.setOpacity(0);
            tfNote.setPrefColumnCount(20);
        }
    }
}