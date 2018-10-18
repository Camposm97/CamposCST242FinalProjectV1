

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class TrialBalance {
    //Instance Variable Section

    private String journal;
    private String directory;
    private String format; //ACCOUNT TITLE | DEBIT | CREDIT
    private Labeled lblTB;
    private ResultSet rsA, rsL, rsOE, rsR, rsE;
    private Statement stmtA, stmtL, stmtOE, stmtR,stmtE;
    private Statement stmtDebit;
    private Statement stmtCredit;
    private Connection c;

    public TrialBalance(String journal, String directory, Connection c)
            throws Exception {
        this.journal = journal;
        this.directory = directory;
        this.format = "%-80s%-16s%-16s\n";
        this.lblTB = new Label();
        this.stmtA = c.createStatement();
        this.stmtL = c.createStatement();
        this.stmtOE = c.createStatement();
        this.stmtR = c.createStatement();
        this.stmtE = c.createStatement();
        this.c = c;
    } //TrialBalance Constructor

    public BorderPane getTrialBalance() throws Exception {
        //Create BorderPane
        BorderPane mainTBPane = new BorderPane();
        mainTBPane.setStyle("-fx-background-color: lightblue;"
                + "-fx-font-family: monospace;"
                + "-fx-font-size: 13pt;");
        mainTBPane.setPadding(new Insets(10));

        //Add Nodes to mainTBPane
        mainTBPane.setTop(getTopPane());

        return mainTBPane;
    }

    private VBox getTopPane() throws Exception {
        //Create VBox
        VBox centerPane = new VBox(10);
        centerPane.setPadding(new Insets(10));
        centerPane.setAlignment(Pos.CENTER);

        setTrialBalanceForm(); //Set lblTB Properties

        centerPane.getChildren().addAll(
                new Label(journal.toUpperCase()),
                new Label("TRIAL BALANCE"), new Label(
                        String.valueOf(new java.util.Date())),
                lblTB);
        return centerPane;
    }

    private void setTrialBalanceForm() throws Exception {
        //Set lblTB Properties
        lblTB = new Label();
        lblTB.setTextAlignment(TextAlignment.CENTER);
        lblTB.setLineSpacing(10);

        //Variables For Trial Balance
        String TBList = String.format(format,
                "ACCOUNT TITLE", "DEBIT", "CREDIT");
        String currentLine = "";
        
        //Variables For File
        String[] accountTitles;
        String currentFileLine;
        Scanner accountFile;
        accountFile = new Scanner(
                new File(directory + "AccountList.txt"));

        while (accountFile.hasNextLine()) {
            currentFileLine = accountFile.nextLine();
            accountTitles = currentFileLine.split("::");

            if (!accountTitles[0].equals("")) 
            { //Account Is Some Type
                switch (accountTitles[0]) {
                    case "ASSET-ACCOUNT":
                        currentLine += AssetAccounts(accountTitles[1]);
                        totalDebit(accountTitles[1]);
                        break;
                    case "LIABILITY-ACCOUNT":
                        currentLine += LiabilityAccounts(accountTitles[1]);
                        break;
                    case "OWNERS-EQUITY-ACCOUNT":
                        currentLine += OwnersEquityAccounts(accountTitles[1]);
                        break;
                    case "REVENUE-ACCOUNT":
                        currentLine += RevenueAccounts(accountTitles[1]);
                        break;
                    case "EXPENSE-ACCOUNT":
                        currentLine += ExpenseAccounts(accountTitles[1]);
                        break;
                }
            }
        }
        currentLine += totalBalance();
        lblTB.setText(TBList += currentLine);
    }
    
    private String AssetAccounts(String accountTitle)
            throws SQLException 
    {
        String assets = "";
        double debit = 0;
        double credit = 0;
        
        rsA = stmtA.executeQuery(
                "SELECT (SUM(debit) - SUM(credit)), "
                + "(SUM(credit) - SUM(debit)) "
                + "FROM " + journal + " "
                + "WHERE "
                + "accountTitle = \"" + accountTitle + "\";");
        
        while (rsA.next()) {
            debit = rsA.getDouble(1);
            credit = rsA.getDouble(2);
            
            if(debit != 0 && credit != 0) {
                assets = String.format(format,
                    accountTitle, debit, "");
                //System.out.printf(assets);
                
            } else if(debit < 0 && credit > 0) {
                assets += String.format(format,
                    accountTitle, "", credit);
            }
        }
        return assets;
    }
    
    private String LiabilityAccounts(String accountTitle)
            throws SQLException 
    {
        String liabilities = "";
        double debit = 0;
        double credit = 0;
        
        rsL = stmtL.executeQuery(
                "SELECT (SUM(credit) - SUM(debit)), "
                + "(SUM(debit) - SUM(credit)) "
                + "FROM " + journal + " "
                + "WHERE "
                + "accountTitle = \"" + accountTitle + "\";");
        
        while(rsL.next()) {
            credit = rsL.getDouble(1); //Total Credit - Total Debit
            debit = rsL.getDouble(2); //Total Debit - Total Credit
            
            if(debit != 0 && credit != 0) {
                liabilities = String.format(format,
                    accountTitle, "", credit);
            } 
            else if (debit > 0 && credit < 0) {
                liabilities += String.format(format,
                    accountTitle, debit, "");
            }
        }
        return liabilities;
    }
    
    private String OwnersEquityAccounts(String accountTitle) 
            throws SQLException {
        String ownersEquity = "";
        double debit = 0;
        double credit = 0;
        
        rsOE = stmtOE.executeQuery(
                "SELECT (SUM(credit) - SUM(debit)), "
                + "(SUM(debit) - SUM(credit)) "
                + "FROM " + journal + " "
                + "WHERE "
                + "accountTitle = \"" + accountTitle + "\";");
        
        while(rsOE.next()) {
            credit = rsOE.getDouble(1); //Total Credit - Total Debit
            debit = rsOE.getDouble(2); //Total Debit - Total Credit
            
            if(debit < 0 && credit > 0) {
                ownersEquity = String.format(format,
                    accountTitle, "", credit);
            } 
            else if (debit > 0 && credit < 0) {
                ownersEquity += String.format(format,
                    accountTitle, debit, "");
            }
        }
        return ownersEquity;
    }
    
    private String RevenueAccounts(String accountTitle) 
            throws SQLException {
        String revenue = "";
        double debit = 0;
        double credit = 0;
        
        rsR = stmtR.executeQuery(
                "SELECT (SUM(credit) - SUM(debit)), "
                + "(SUM(debit) - SUM(credit)) "
                + "FROM " + journal + " "
                + "WHERE "
                + "accountTitle = \"" + accountTitle + "\";");
        
        while(rsR.next()) {
            credit = rsR.getDouble(1); //Total Credit - Total Debit
            debit = rsR.getDouble(2); //Total Debit - Total Credit
            
            if(debit != 0 && credit != 0) {
                revenue = String.format(format,
                    accountTitle, "", credit);
            } 
            else if (debit > 0 && credit < 0) {
                revenue += String.format(format,
                    accountTitle, debit, "");
            }
        }
        return revenue;
    }
    
    private String ExpenseAccounts(String accountTitle) 
            throws SQLException {
        String revenue = "";
        double debit = 0;
        double credit = 0;
        
        rsE = stmtE.executeQuery(
                "SELECT (SUM(debit) - SUM(credit)), "
                + "(SUM(credit) - SUM(debit)) "
                + "FROM " + journal + " "
                + "WHERE "
                + "accountTitle = \"" + accountTitle + "\";");
        
        while(rsE.next()) {
            debit = rsE.getDouble(1); //Total Debit - Credit
            credit = rsE.getDouble(2); //Total Credit - Total Debit
            
            if(debit != 0 && credit != 0) {
                revenue = String.format(format,
                    accountTitle, debit, "");
            } 
            else if (debit > 0 && credit < 0) {
                revenue += String.format(format,
                    accountTitle, "", credit);
            }
        }
        return revenue;
    }
    
    private String totalBalance() throws SQLException {
        Statement stmtTB = c.createStatement();
        String totalBalance = "";
        double totalDebit = 0;
        double totalCredit = 0;
    
        ResultSet rsTB = stmtTB.executeQuery(
                "SELECT (SUM(debit)) AS 'Total Debit', "
                + "(SUM(credit)) AS 'Total Credit' "
                + "FROM " + journal + ";");
        
        while(rsTB.next()) {
            totalDebit = rsTB.getDouble("Total Debit");
            totalCredit = rsTB.getDouble("Total Credit");
            totalBalance = String.format(
            format, "Total",totalDebit,totalCredit);
            
            if (totalDebit == totalCredit) {
                totalBalance
                        += "\nResults: "
                        + "Total Debit & Credit Equal Each Other.\n"
                        + "This Journal is Ready for Adjusting Entries &"
                        + "Financial Statements.";
            } else {
                totalBalance
                        += "\nResults: "
                        + "Total Debit & Credit Do NOT Equal Each Other.\n"
                        + "This Journal is NOT Ready for Adjusting Entries &"
                        + "Financial Statements.";
            }
        }
        
        return totalBalance;
    }
    
    private void totalDebit(String accountTitle) throws SQLException {
        stmtDebit = c.createStatement();
        double totalDebit = 0;
        double totalCredit = 0;
        
        ResultSet rsDebit = stmtDebit.executeQuery(
                "SELECT (SUM(debit) - SUM(credit)) AS 'Total Debit', "
                + "(SUM(credit) - SUM(debit)) AS 'Total Credit'"
                + "FROM " + journal + " "
                + "WHERE "
                + "accountTitle = \"" + accountTitle + "\";");
        
        while (rsDebit.next()) {
            totalDebit = rsDebit.getDouble("Total Debit");
            totalCredit = rsDebit.getDouble("Total Credit");
            System.out.printf(format, 
                    accountTitle, totalDebit, totalCredit);
        }
    }
    
    private void totalCredit(String accountTitle) throws SQLException {
        stmtCredit = c.createStatement();
        double totalDebit = 0;
        double totalCredit = 0;
        
        ResultSet rsCredit = stmtCredit.executeQuery(
                "SELECT (SUM(debit) - SUM(credit)) AS 'Total Debit', "
                + "(SUM(credit) - SUM(debit)) AS 'Total Credit'"
                + "FROM " + journal + " "
                + "WHERE "
                + "accountTitle = \"" + accountTitle + "\";");
        
        while (rsCredit.next()) {
            totalDebit = rsCredit.getDouble("Total Debit");
            totalCredit = rsCredit.getDouble("Total Credit");
            System.out.printf(format, 
                    accountTitle, totalDebit, totalCredit);
        }
    }
}
