package eplex.win.FastNEATJava.genome;

/**
 * Expose `NeatConnection`.
 */

public class NeatConnection
{
    public String gid;
    public double weight;
    public String sourceID;
    public String targetID;
    public boolean isMutated;

    public NeatConnection(String gid, double weight, String sourceID, String targetID)
    {
        //gid must be a string
        this.gid = gid;
        this.weight = weight;

        //node ids are strings now -- so make sure to save as string always
        this.sourceID = sourceID;
        this.targetID = targetID;
    }

    public NeatConnection clone()
    {
        return Copy(this);
    }

    public static NeatConnection Copy(NeatConnection connection)
    {
        return new NeatConnection(connection.gid, connection.weight, connection.sourceID, connection.targetID);
    }
}