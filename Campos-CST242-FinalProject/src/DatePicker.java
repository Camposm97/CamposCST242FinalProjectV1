

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**Purpose.
 * The purpose of DatePicker is to provide a user interface of an HBox
 * consisting of combo boxes representing a date format of:
 * YYYY-MM-DD = Year-Month-Day 
 * 
 * And another format: YYYY-MM = Year-Month
 * 
 * The format "YYYY-MM-DD" is critical for inserting data in an table 
 * in MySQL since it prefers that format.  
 * 
 * The other format, "YYYY-MM" is for retrieving a month for 
 * creating a trial balance which will be used in a dialog before
 * the user creates a trail balance.  
 * 
 * 
 * The class provide three methods:
 * getDatePicker() - Returns an HBox of three combo boxes for 
 * picking the year, month, and day.  Used for 
 * 
 * getYearMonthPicker() - Returns an HBox of two combo boxes for
 * picking the year and month only.  Used for retrieving
 * journal entries within a specified month by user.
 * 
 * getDate() - Returns a string of the date in a YYYY-MM-DD
 * format from retrieving the values from the combo boxes 
 */

public class DatePicker  {
    //Instance Variable Section
    private ComboBox<Integer> cbYear;
    private ComboBox<Integer> cbMonth;
    private ComboBox<Integer> cbDay;
    private java.util.Date date;
    
    public DatePicker() {
        this.cbYear = new ComboBox<>();
        this.cbMonth = new ComboBox<>();
        this.cbDay = new ComboBox<>();
        this.date = new java.util.Date();
        addComboBoxValues();
    } //DatePicker Constructor
    
    public HBox getDatePicker() {
        //Create HBox called hBoxDate
        HBox hBoxDate = new HBox(10);
        hBoxDate.setAlignment(Pos.CENTER);
        
        //Set ComboBox Values 
        cbYear.setValue(date.getYear() + 1900);
        cbMonth.setValue(1);
        cbDay.setValue(1);
        
        //Add ComboBoxes to hBoxDate 
        hBoxDate.getChildren().addAll( //YYYY-MM-DD
                cbYear, new Label("-"), cbMonth, new Label("-"), cbDay);
        return hBoxDate;
    } //Returns an HBox for Picking Dates
    
    public HBox getYearMonthPicker() {
        //Create HBox called hBoxYM
        HBox hBoxYM = new HBox(5);
        hBoxYM.setAlignment(Pos.CENTER);
        
        //Set ComboBox Values
        cbYear.setValue(date.getYear() + 1900);
        cbMonth.setValue(1);
        
        //Add ComboxBoxes cbYear & cbMonth to hBox
        hBoxYM.getChildren().addAll( //YYYY-MM
        cbYear, new Label("-"), cbMonth);
        
        return hBoxYM;
    }
    
    public String getDate() {
        String currentDate
                = String.valueOf(cbYear.getValue() + "-"
                + String.valueOf(cbMonth.getValue() + "-"
                + String.valueOf(cbDay.getValue())));
        System.out.println("Date: " + currentDate);
        return currentDate;
    } //Returns String of ComboBox Values - [YYYY-MM-DD]
    
    private void addComboBoxValues() { 
        //ADD YEAR VALUES
        for (int year = 1990; year <= (1900 + date.getYear()); year++) {
            cbYear.getItems().add(year);
        }
    
        //ADD MONTH VALUES
        for (int month = 1; month <= 12; month++) {
            cbMonth.getItems().add(month); 
        }
        
        //ADD DAY VALUES
        for (int day = 1; day <= 31; day++) {
            cbDay.getItems().add(day);
        } //Adds int Values to All 3 Combo Boxes
        
        //EVENT HANDLERS
        cbYear.setOnAction(e ->{ setDaysOfMonth(); });
        cbMonth.setOnAction(e ->{ setDaysOfMonth(); });
    } //Adds Values to All 3 Combo Boxes 
    
    private void setDaysOfMonth() {
        int day;

        if (is31DayMonth(cbMonth.getValue())) {
            cbDay.getItems().clear();
            for (day = 1; day <= 31; day++) {
                cbDay.getItems().add(day);
            }
        } 
        else if (is30MonthDay(cbMonth.getValue())) {
            cbDay.getItems().clear();
            for (day = 1; day <= 30; day++) {
                cbDay.getItems().add(day);
            }
        } 
        else { //Month is February
            cbDay.getItems().clear();
            if (isLeapYear()) {
                for (day = 1; day <= 29; day++) {
                    cbDay.getItems().add(day);
                }
            } 
            else {
                for (day = 1; day <= 28; day++) {
                    cbDay.getItems().add(day);
                }
            }

        }
    } //Executed When Month Changes
    
    private boolean is31DayMonth(int value) {
        if (value == 1 || value == 3 || value == 5 || value == 7 
                || value == 8 || value == 10 || value == 12) {
            return true;
        } else {
            return false;
        }
    } //Returns True if Month has 31 Days
    
    private boolean is30MonthDay(int value) {
        if (value == 4 || value == 6 || value == 9 || value == 11) {
            return true;
        } else {
            return false;
        }
    } //Returns True if Month has 30 Days
    
    private boolean isLeapYear() {        
        if (cbYear.getValue() % 4 == 0 
                && cbYear.getValue() % 100 != 0 
                || (cbYear.getValue() % 400 == 0)) {
            return true; //Leap year
        } 
        else { //No Leap Year
            return false;
        }
    } //Returns True if Year is a Leap Year
}