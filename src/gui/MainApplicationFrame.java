package gui;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.security.MessageDigest;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import log.Logger;


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
        //в лямбде надо наверное метод сделать который будет нужный файл открывать и из него робота пихать в сет робот
        robotsMenu.add(createMenuItem("With Bug", KeyEvent.VK_S, (event) -> gameWindow.getVisualizer().setRobot(new RobotB())));
        robotsMenu.add(createMenuItem("Smt else...", KeyEvent.VK_E, (event) -> gameWindow.getVisualizer().setRobot(chooseFile())));


        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(systemMenu);
        menuBar.add(robotsMenu);
        return menuBar;
    }

    private Robot chooseFile(){
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            //ну вот его как то откроешь и вытащишь робота или файл можеш возвращать хз
        }
        return new Robot();
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
