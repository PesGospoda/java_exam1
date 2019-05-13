package robot;

public class MultiRobot extends Thread {
    private BaseRobot robot;
    private int await;

    public MultiRobot(BaseRobot _robot, int _await){
        robot = _robot;
        await = _await;
    }

    @Override
    public void run() {
        robot.onModelUpdateEvent();
        try {
            sleep(await);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
