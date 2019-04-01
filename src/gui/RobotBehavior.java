package gui;

public class RobotBehavior {

    IOnModelUpdate onUpdate;

    public RobotBehavior(IOnModelUpdate a)
    {
        this.onUpdate = a;
    }
}
