package gui;

import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        localization();

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
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

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(systemMenu);
        return menuBar;
    }

    private void close() {
        int res = JOptionPane.showConfirmDialog(null, "Выйти из программы?" );
        if (res == JOptionPane.YES_OPTION)
            System.exit(0); // ToDo smth else mb
        if (res == JOptionPane.NO_OPTION)
            JOptionPane.showMessageDialog(null, "Возврат в программу");
    }

    private void localization (){
        UIManager.put("OptionPane.yesButtonText"   , "Да"    );
        UIManager.put("OptionPane.noButtonText"    , "Нет"   );
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.okButtonText"    , "Готово");
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
