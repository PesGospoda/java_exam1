package gui;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

import javax.swing.*;

import log.Logger;
import robot.*;
import robot.Robot;


public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final GameWindow gameWindow = new GameWindow();

    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        localization();

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        //GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                //JOptionPane.showMessageDialog(null, "Добро пожаловать, петушок.");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }


            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

        });
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = createJMenu("Режим отображения", KeyEvent.VK_V,
                "Управление режимом отображения приложения");
        lookAndFeelMenu.add(createMenuItem("System sheme", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }));
        lookAndFeelMenu.add(createMenuItem("Universal sheme", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        }));


        JMenu testMenu = createJMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");
        testMenu.add(createMenuItem("Message in log", KeyEvent.VK_S, (event) -> Logger.debug("New String")));


        JMenu systemMenu = createJMenu("system", KeyEvent.VK_Y, "system commands");
        systemMenu.add(createMenuItem("Exit", KeyEvent.VK_E, (event) -> close()));

        JMenu robotsMenu = createJMenu("Robots", KeyEvent.VK_Y, "robot option ");
        robotsMenu.add(createMenuItem("Standard", KeyEvent.VK_S, (event) -> gameWindow.getVisualizer().setRobot(new Robot())));
        HashMap<String, Class> compiledRobots = loadFromStockDirectory();
        for ( String key : compiledRobots.keySet() ) {
            robotsMenu.add(createMenuItem(key.substring(0, key.length() - 6), KeyEvent.VK_S, (event) -> {
                try {
                    gameWindow.getVisualizer().setRobot((IRobot) compiledRobots.get(key).newInstance() );
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }));
        }
        robotsMenu.add(createMenuItem("Smt else...", KeyEvent.VK_E, (event) -> gameWindow.getVisualizer().setRobot(chooseFile())));



        JMenu addRobotsMenu = createJMenu("Add Robots", KeyEvent.VK_Y, "add robot ");
        addRobotsMenu.add(createMenuItem("Standard", KeyEvent.VK_S, (event) -> gameWindow.getVisualizer().addRobot(new MultiRobot(new Robot(), 100))));
        for ( String key : compiledRobots.keySet() ) {
            addRobotsMenu.add(createMenuItem(key.substring(0, key.length() - 6), KeyEvent.VK_S, (event) -> {
                try {
                    gameWindow.getVisualizer().addRobot(new MultiRobot((BaseRobot)compiledRobots.get(key).newInstance(), 100));
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }));
        }
        addRobotsMenu.add(createMenuItem("Smt else...", KeyEvent.VK_E, (event) -> gameWindow.getVisualizer().addRobot(new MultiRobot((BaseRobot)chooseFile(), 100))));


        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(systemMenu);
        menuBar.add(robotsMenu);
        menuBar.add(addRobotsMenu);
        return menuBar;
    }


    private HashMap<String, Class> loadFromStockDirectory() {
        HashMap<String, Class> outDict= new HashMap<>();
        Class myClass = null;
        File folder = new File("modes/");
        File[] listOfFiles = folder.listFiles();
        for (int i=0; i<listOfFiles.length;i++)
        {
            Logger.debug("open  - " + listOfFiles[i].getPath() + listOfFiles[i].getName());
            try {
                MyLoader loader = new MyLoader(listOfFiles[i]);
                myClass = Class.forName(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 6), true, loader);

            } catch (Exception e) {
                Logger.error("super puper err");
                e.printStackTrace();
            }
            outDict.put(listOfFiles[i].getName(), myClass);
        }
        return outDict;
    }



    private IRobot chooseFile(){
        JFileChooser fileopen = new JFileChooser();
        IRobot myClass = null;

        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            Logger.debug("open  - " + file.getPath() + file.getName());
            try {

                MyLoader loader = new MyLoader(file);
                Class clazz= Class.forName(file.getName().substring(0 ,file.getName().length() - 6),true, loader);
                myClass = (IRobot) clazz.newInstance();
            } catch (Exception e) {
                Logger.error("super puper err" );
                e.printStackTrace();
            }
        }
        return myClass;
    }

    private void close() {
        int res = JOptionPane.showConfirmDialog(null, "Выйти из программы?");
        if (res == JOptionPane.YES_OPTION)
            System.exit(0); // ToDo smth else mb
        if (res == JOptionPane.NO_OPTION)
            JOptionPane.showMessageDialog(null, "Возврат в программу");
    }

    private void localization() {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.okButtonText", "Готово");
    }

    private JMenu createJMenu(String text, int key, String description) {
        JMenu menu = new JMenu(text);
        menu.setMnemonic(key);
        menu.getAccessibleContext().setAccessibleDescription(description);
        return menu;
    }

    private JMenuItem createMenuItem(String text, int key, ActionListener smt) {
        JMenuItem item = new JMenuItem(text, key);
        item.addActionListener(smt);
        return item;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
