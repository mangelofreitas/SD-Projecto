import java.io.Serializable;

/**
 * Created by miguel and maria
 */
public class Reward implements Serializable
{
    private String name;
    private String description;
    private int valueOfReward;

    public Reward()
    {

    }

    public Reward(String name, String description, int valueOfReward)
    {
        this.name = name;
        this.description = description;
        this.valueOfReward = valueOfReward;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValueOfReward() {
        return valueOfReward;
    }

    public void setValueOfReward(int valueOfReward) {
        this.valueOfReward = valueOfReward;
    }
}
