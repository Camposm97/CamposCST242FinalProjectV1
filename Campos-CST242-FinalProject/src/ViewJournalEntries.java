

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ViewJournalEntries {
    private String journal;
        private TextArea taEntries;
        private Statement stmt;
        
        public ViewJournalEntries
        (String journal, Connection c) throws SQLException {
            this.journal = journal;
            this.taEntries = new TextArea();
            this.stmt = c.createStatement();
        } //ViewEntries Constructor
        
        public BorderPane getJournalEntriesPane() throws SQLException {
            //Create BorderPane (mainTxnPane)
            BorderPane mainEntryPane = new BorderPane();
            mainEntryPane.setStyle("-fx-background-color: lightblue;"
                    + "-fx-font-family: monospace;"
                    + "-fx-font-size: 10pt;");
            mainEntryPane.setPadding(new Insets(10));
            
            //Add Nodes to mainEntryPane
            mainEntryPane.setTop(getTopPane());
            mainEntryPane.setCenter(getCenterPane());
            mainEntryPane.setBottom(getBottomPane());
            return mainEntryPane;
        }
        
        private VBox getTopPane() {
            //Create VBox 
            VBox topPane = new VBox(5);
            topPane.setPadding(new Insets(10));
            topPane.setAlignment(Pos.CENTER);
            
            topPane.getChildren().addAll(
                    new Label("Journal Entries For: "
                            + journal.toUpperCase()),
                    new Label("On This Date:"));

            return topPane;
        }
        
        private VBox getCenterPane() throws SQLException {
            //Create VBox
            VBox centerPane = new VBox(10);
            centerPane.setPadding(new Insets(10));
            centerPane.setAlignment(Pos.CENTER_LEFT);
            
            //Create String for lblTxn (Attributes in Table)
            String attributes = String.format(
                    " %-10s | %-12s | %-36s | %-15s | %-15s | %-24s\n",
                    "ID", "Date", "Account Title", "Debit", "Credit", "Note");
            Label lblEntries = new Label(attributes);
            
            //Set taTxns Properties
            taEntries.setEditable(false);
            taEntries.setPrefColumnCount(128);
            taEntries.setPrefRowCount(32);
            
            taEntries.setText(getEntries());
            
            centerPane.getChildren().addAll(lblEntries, taEntries);
            
            return centerPane;
        }
        
        private HBox getBottomPane() {
            HBox bottomPane = new HBox(10);
            bottomPane.setPadding(new Insets(10));
            bottomPane.setAlignment(Pos.CENTER);
            
            
            
            //Add Node(s) to bottomPane
            bottomPane.getChildren().addAll();
            
            return bottomPane;
        }
        
        private String getEntries() throws SQLException {
            //Create ResultSet
            ResultSet rsEntry = stmt.executeQuery(
                    "SELECT * FROM " + journal + ";");            
            
            //Variables for TextArea
            String id = "";
            String date = "";
            String acctTitle = "";
            String stringDebit = "";
            String stringCredit = "";
            String note = "";
            String currentEntry = "";
            String entryList = "";
            String entryFormat = 
                    "%-10s | %-12s | %-36s | %-15s | %-15s | %-24s\n";
            
            double debit = 0;
            double credit = 0;
            
            
            while (rsEntry.next()) {
                id = String.format("%-10s", String.valueOf(rsEntry.getInt(1)));
                date = String.format("%-12s", rsEntry.getString(2));
                acctTitle = String.format("%-36s", rsEntry.getString(3));
                debit = rsEntry.getDouble(4);
                credit = rsEntry.getDouble(5);
                stringDebit = String.format("%-15.2f", rsEntry.getDouble(4));
                stringCredit = String.format("%-15.2f", rsEntry.getDouble(5));
                note = String.format("%-24s", rsEntry.getString(6));
                
                //Get Rid of 0.00 For Better View of Entries
                if (debit == 0.00 && credit > 0.00) {
                    stringDebit = String.format("%-15s" ,"");
                } 
                else if (credit == 0.00 && debit > 0.00) {
                    stringCredit = String.format("%-15s", "");
                }

                currentEntry = id + " | " + date + " | " + acctTitle + " | "
                        + stringDebit + " | " + stringCredit + " | " + note + "\n";
                currentEntry += String.format(entryFormat,
                        "", "", "", "", "", "");

                entryList += currentEntry;
            }
            System.out.println();
            return entryList;
        }
}