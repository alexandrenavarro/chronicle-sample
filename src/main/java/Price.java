import net.openhft.lang.model.Byteable;

public interface Price extends Byteable {

    double getBid();

    void setBid(double aBid);

    double getAsk();

    void setAsk(double aAsk);

    long getTimestamp();

    void setTimestamp(long aTimestamp);

}

