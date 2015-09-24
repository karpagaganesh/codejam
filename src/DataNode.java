/**
 * Created by kpatchirajan on 9/23/15.
 */
public class DataNode {

    int minimumAmount;
    int maximumAmount;
    int single;
    int marriedFilingJointly;
    int marriedFilingSeparately;
    int headOfHouseHold;

    DataNode next;

    public DataNode(int _minimumAmount, int _maximumAmount, int _single,
                         int _marriedFilingJointly, int _marriedFilingSeparately, int _headOfHouseHold){

        minimumAmount = _minimumAmount;
        maximumAmount = _maximumAmount;
        single = _single;
        marriedFilingJointly = _marriedFilingJointly;
        marriedFilingSeparately = _marriedFilingSeparately;
        headOfHouseHold = _headOfHouseHold;
        next = null;
    }

}
