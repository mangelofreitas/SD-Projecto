import java.io.Serializable;
import java.util.ArrayList;
import java.sql.Date;

/**
 * Created by miguel and maria
 */
public class Project implements Serializable
{
    private User user;
    private int projectID;
    private String projectName;
    private String description;
    private Date dateLimit;
    private int requestedValue;
    private ArrayList<Reward> rewards;
    private int currentAmount;
    private ArrayList<ProductType> productTypes;

    public Project()
    {
        rewards = new ArrayList<Reward>();
        productTypes = new ArrayList<ProductType>();
    }

    public Project(User user, int projectID)
    {
        this.user = user;
        this.projectID = projectID;
    }

    public Project(User user, String projectName, String description, Date dateLimit, int requestedValue, ArrayList<Reward> rewards, ArrayList<ProductType> productTypes)
    {
        this.user = user;
        this.projectName = projectName;
        this.description = description;
        this.dateLimit = dateLimit;
        this.requestedValue = requestedValue;
        this.rewards = rewards;
        this.currentAmount = 0;
        this.productTypes = productTypes;
    }

    public Project(User user,int projectID, String projectName, String description, Date dateLimit, int requestedValue, int currentAmount, ArrayList<Reward> rewards, ArrayList<ProductType> productTypes)
    {
        this.user = user;
        this.projectID = projectID;
        this.projectName = projectName;
        this.description = description;
        this.dateLimit = dateLimit;
        this.requestedValue = requestedValue;
        this.rewards = rewards;
        this.currentAmount = 0;
        this.currentAmount = currentAmount;
        this.productTypes = productTypes;
    }

    public Project(User user,int projectID, String projectName, String description, Date dateLimit, int requestedValue, int currentAmount, ArrayList<Reward> rewards)
    {
        this.user = user;
        this.projectID = projectID;
        this.projectName = projectName;
        this.description = description;
        this.dateLimit = dateLimit;
        this.requestedValue = requestedValue;
        this.rewards = rewards;
        this.currentAmount = 0;
        this.currentAmount = currentAmount;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(Date dateLimit) {
        this.dateLimit = dateLimit;
    }

    public int getRequestedValue() {
        return requestedValue;
    }

    public void setRequestedValue(int requestedValue) {
        this.requestedValue = requestedValue;
    }

    public ArrayList<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(ArrayList<Reward> rewards) {
        this.rewards = rewards;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public ArrayList<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(ArrayList<ProductType> productTypes) {
        this.productTypes = productTypes;
    }
}
