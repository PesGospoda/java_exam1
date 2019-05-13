package robot;

import log.Logger;

public class MultiRobot extends Thread {
    public BaseRobot robot;
    private int await;
    private Boolean process = true;

    public MultiRobot(BaseRobot _robot, int _await ){
        robot = _robot;
        await = _await;
    }


    public void setTarget(int x, int y){
        robot.setTargeState(x, y);
    }

    @Override
    public void run() {
        Logger.debug("Start - " + getName());
        while (process) {
            robot.onModelUpdateEvent();
            try {
                sleep(await);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Logger.debug("close - " + getName());
    }
}
