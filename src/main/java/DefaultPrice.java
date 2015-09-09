import net.openhft.lang.io.Bytes;
import net.openhft.lang.io.serialization.BytesMarshallable;

/**
 * <p>Price. </p>
 *
 * @author anavarro - Sep 6, 2015
 *
 */
public final class DefaultPrice implements BytesMarshallable {

    private double bid;
    private double ask;
    private long timestamp = System.currentTimeMillis();

    
    public DefaultPrice() {
        super();
    }


    public DefaultPrice(double aBid, double aAsk) {
        super();
        this.bid = aBid;
        this.ask = aAsk;
    }

    @Override
    public void readMarshallable(Bytes aIn) throws IllegalStateException {
        this.bid = aIn.readDouble();
        this.ask = aIn.readDouble();
        this.timestamp = aIn.readLong();
    }
    
    @Override
    public void writeMarshallable(Bytes aOut) {
        aOut.writeDouble(bid);
        aOut.writeDouble(ask);
        aOut.writeLong(timestamp);
    }
    
    
    /**
     * (non-Javadoc)
     *
     * @see Price#getBid()
     */
    public double getBid() {
        return this.bid;
    }

    /**
     * (non-Javadoc)
     *
     * @see Price#setBid(double)
     */
    public void setBid(double aBid) {
        this.bid = aBid;
    }

    /**
     * (non-Javadoc)
     *
     * @see Price#getAsk()
     */
    public double getAsk() {
        return this.ask;
    }

    /**
     * (non-Javadoc)
     *
     * @see Price#setAsk(double)
     */
    public void setAsk(double aAsk) {
        this.ask = aAsk;
    }

    /**
     * (non-Javadoc)
     *
     * @see Price#getTimestamp()
     */
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * (non-Javadoc)
     *
     * @see Price#setTimestamp(long)
     */
    
    public void setTimestamp(long aTimestamp) {
        this.timestamp = aTimestamp;
    }

    
    public String toString() {
        return "Price [bid=" + this.bid + ", ask=" + this.ask + ", timestamp=" + this.timestamp + "]";
    }
    
    
    
    

}
