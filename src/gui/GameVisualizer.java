package gui;

import log.Logger;
import robot.IRobot;
import robot.*;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel {
    private final Timer m_timer = initTimer();
    private List<MultiRobot> robots =  new ArrayList<>();
    public IRobot getRobot() {
        return robot;
    }

    public void addRobot(MultiRobot r){
        robots.add(r);
        r.setTarget(robot.targetPositionX(), robot.getTargetPositionY());
        r.start();
    }

    public void setRobot(IRobot rob) {
        Logger.debug("новый робот - " + robot.getClass().toString());
        rob.setRobotState(robot.getRobotPositionX(), robot.getRobotPositionY(), robot.getRobotDirection());
        rob.setTargeState(robot.targetPositionX(), robot.getTargetPositionY());
        this.robot = rob;
    }

    private IRobot robot = new Robot();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public GameVisualizer() {
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                robot.onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                robot.setTargeState(e.getPoint().x, e.getPoint().y);
                for ( MultiRobot r: robots){
                    r.setTarget(e.getPoint().x, e.getPoint().y);
                }
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, round(robot.getRobotPositionX()), round(robot.getRobotPositionY()), robot.getRobotDirection());
        for ( MultiRobot r: robots){
            drawRobot(g2d, round(r.robot.getRobotPositionX()), round(r.robot.getRobotPositionY()), r.robot.getRobotDirection());
        }
        repaint();
        drawTarget(g2d, robot.targetPositionX(), robot.getTargetPositionY());
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        int robotCenterX = round(robot.getRobotPositionX());
        int robotCenterY = round(robot.getRobotPositionY());
        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, x + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x + 10, y, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}

