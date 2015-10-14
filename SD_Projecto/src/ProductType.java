import java.io.Serializable;

/**
 * Created by miguel and maria
 */
public class ProductType implements Serializable
{
    private String type;
    private int vote;

    public ProductType()
    {

    }

    public ProductType(String type, int vote)
    {
        this.type = type;
        this.vote = vote;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

}
