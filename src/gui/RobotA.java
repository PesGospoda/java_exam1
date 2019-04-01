package gui;

import java.awt.*;

public class RobotA implements IUpdated{

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

    protected void moveRobot(Robot robot) {
        robot.velocity = applyLimits(robot.velocity, 0, robot.maxVelocity);
        robot.angularVelocity = applyLimits(robot.angularVelocity, -robot.maxAngularVelocity, robot.maxAngularVelocity);
        double newX = robot.m_robotPositionX + robot.velocity / robot.angularVelocity *
                (Math.sin(robot.m_robotDirection + robot.angularVelocity * robot.duration) -
                        Math.sin(robot.m_robotDirection));
        if (!Double.isFinite(newX)) {
            newX = robot.m_robotPositionX + robot.velocity * robot.duration * Math.cos(robot.m_robotDirection);
        }
        double newY = robot.m_robotPositionY - robot.velocity / robot.angularVelocity *
                (Math.cos(robot.m_robotDirection + robot.angularVelocity * robot.duration) -
                        Math.cos(robot.m_robotDirection));

        if (!Double.isFinite(newY)) {
            newY = robot.m_robotPositionY + robot.velocity * robot.duration * Math.sin(robot.m_robotDirection);
        }
        robot.m_robotPositionX = newX;
        robot.m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(robot.m_robotDirection + robot.angularVelocity * robot.duration);
        robot.m_robotDirection = newDirection;
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
    public void onModelUpdateEvent(Robot robot) {
        double distance = distance(robot.m_targetPositionX, robot.m_targetPositionY,
                robot.m_robotPositionX, robot.m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        robot.velocity = robot.maxVelocity;
        double angleToTarget = angleTo(robot.m_robotPositionX, robot.m_robotPositionY, robot.m_targetPositionX, robot.m_targetPositionY);
        robot.angularVelocity = 0;
        double angleBetweenTargetRobot = asNormalizedRadians(angleToTarget - robot.m_robotDirection);
        if (angleBetweenTargetRobot < Math.PI) {
            robot.angularVelocity = robot.maxAngularVelocity;
        }
        else {
            robot.angularVelocity = -robot.maxAngularVelocity;
        }
        if (Math.abs(angleBetweenTargetRobot)<0.1) {
            robot.velocity = robot.maxVelocity;
        }
        else {
            robot.velocity = distance * Math.abs(robot.angularVelocity) / 2;
        }
        moveRobot(robot);
    }

    //public void clone(RobotA other){
    //    m_robotPositionX = other.m_robotPositionX;
    //    m_robotDirection = other.m_robotDirection;
     //   m_robotPositionY = other.m_robotPositionY;
     //   m_targetPositionX = other.m_targetPositionX;
    //    m_targetPositionY = other.m_targetPositionY;
    //}
}
