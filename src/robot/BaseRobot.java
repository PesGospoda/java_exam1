package robot;


public  abstract class BaseRobot extends Thread implements IRobot {
    public volatile double m_robotPositionX = 100;// потом сделаю геттеры
    public volatile double m_robotPositionY = 100;
    public volatile double m_robotDirection = 0;

    public volatile int m_targetPositionX = 150;
    public volatile int m_targetPositionY = 100;


    protected static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    protected static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }


    protected static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    protected static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    @Override
    public double getRobotPositionX() {
        return m_robotPositionX;
    }

    @Override
    public double getRobotPositionY() {
        return m_robotPositionY;
    }

    @Override
    public double getRobotDirection() {
        return m_robotDirection;
    }

    @Override
    public int targetPositionX() {
        return m_targetPositionX;
    }

    @Override
    public int getTargetPositionY() {
        return m_targetPositionY;
    }

    @Override
    public void setRobotState(double R_x, double R_y, double R_d) {
        m_robotPositionX = R_x;
        m_robotPositionY = R_y;
        m_robotDirection = R_d;

    }

    @Override
    public void setTargeState(int T_x, int T_y) {
        m_targetPositionX = T_x;
        m_targetPositionY = T_y;
    }
}
