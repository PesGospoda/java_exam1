package gui;

import java.awt.*;

public class Robot {

    public volatile double m_robotPositionX = 100;// потом сделаю геттеры
    public volatile double m_robotPositionY = 100;
    public volatile double m_robotDirection = 0;

    public volatile int m_targetPositionX = 150;
    public volatile int m_targetPositionY = 100;

    public volatile double velocity = 0;
    public volatile double angularVelocity = 0;

    public volatile IUpdated behavior;

    public static final int duration = 10;

    public static final double maxVelocity = 0.1;
    public static final double maxAngularVelocity = 0.001;

    public Robot(IUpdated behavior)
    {
        this.behavior = behavior;
    }

    protected void setTargetPosition(Point p) {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    public void clone(Robot other){
            m_robotPositionX = other.m_robotPositionX;
            m_robotDirection = other.m_robotDirection;
           m_robotPositionY = other.m_robotPositionY;
           m_targetPositionX = other.m_targetPositionX;
            m_targetPositionY = other.m_targetPositionY;
        }
}
