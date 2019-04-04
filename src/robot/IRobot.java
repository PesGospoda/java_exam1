package robot;

public interface IRobot {
    double maxVelocity = 0.1;
    double maxAngularVelocity = 0.001;

    void onModelUpdateEvent();

    double getRobotPositionX();
    double getRobotPositionY();
    double getRobotDirection();
    int targetPositionX();
    int getTargetPositionY();


    void setRobotState(double R_x, double R_y, double R_d);
    void setTargeState(int T_x, int T_y);
}
