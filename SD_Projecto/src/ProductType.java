import java.io.Serializable;

/**
 * Created by miguel and maria
 */
public class ProductType implements Serializable
{
    private String type;
    private int productTypeID;
    private int vote;

    public ProductType()
    {

    }

    public ProductType(String type, int vote, int productTypeID)
    {
        this.type = type;
        this.vote = vote;
        this.productTypeID = productTypeID;
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

    public int getProductTypeID() {
        return productTypeID;
    }

    public void setProductTypeID(int productTypeID) {
        this.productTypeID = productTypeID;
    }

    @Override
    public String toString() {
        return  "Type = " + type +
                ", Vote = " + vote;
    }
}
