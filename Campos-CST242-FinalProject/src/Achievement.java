

/**
The purpose of this class is to bring the  system of achievements in the 
program for the user to achieve when he or she does something that is a
considered as an "achievement."  
For example: Entering your first transaction will unlock an achievement called 
"First of Many" then have it pop up in the on in the pop on the programs scene
*/

public class Achievement {
    private boolean achievementUnlocked;
    private String achievementName;
    private String achievementHow; //How to achieve the achievement
    
    public Achievement() {
        this.achievementName = null;
        achievementUnlocked = false;
    }//Default Achievement Constructor
    
    public Achievement(String achievementName) {
        this.achievementName = achievementName;
        achievementUnlocked = false; 
    } //Achievement Constructor With Args
    
    public void setAchievementName(String achievementName) {
        this.achievementName = achievementName;
    }
    
    public void setAchievementHow(String howToAchieve) {
        this.achievementHow = howToAchieve;
    }
    
    public void setAchieved(boolean achieved) {
        this.achievementUnlocked = achieved;
        System.out.println("Achievement Unlocked: " + achievementName);
    }
    
    public String getAchievementHow() {
        return achievementHow;
    }
}