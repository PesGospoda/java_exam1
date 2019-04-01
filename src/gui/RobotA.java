package gui;

import java.awt.*;

public class Robot implements IUpdated{
    public volatile double m_robotPositionX = 100;// потом сделаю геттеры
    public volatile double m_robotPositionY = 100;
    public volatile double m_robotDirection = 0;

    public volatile int m_targetPositionX = 150;
    public volatile int m_targetPositionY = 100;

    protected static final double maxVelocity = 0.1;
    protected static final double maxAngularVelocity = 0.001;

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

    protected void setTargetPosition(Point p) {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    protected static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    protected void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX)) {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection + angularVelocity * duration) -
                        Math.cos(m_robotDirection));

        if (!Double.isFinite(newY)) {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;
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
    public void onModelUpdateEvent() {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        double angleBetweenTargetRobot = asNormalizedRadians(angleToTarget - m_robotDirection);
        if (angleBetweenTargetRobot < Math.PI) {
            angularVelocity = maxAngularVelocity;
        }
        else {
            angularVelocity = -maxAngularVelocity;
        }
        if (Math.abs(angleBetweenTargetRobot)<0.1) {
            velocity = maxVelocity;
        }
        else {
            velocity = distance * Math.abs(angularVelocity) / 2;
        }
        moveRobot(velocity, angularVelocity, 10);
    }

    public void clone(Robot other){
        m_robotPositionX = other.m_robotPositionX;
        m_robotDirection = other.m_robotDirection;
        m_robotPositionY = other.m_robotPositionY;
        m_targetPositionX = other.m_targetPositionX;
        m_targetPositionY = other.m_targetPositionY;
    }
}
