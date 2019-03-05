package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

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


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        JMenu systemMenu = createJMenu("system", KeyEvent.VK_Y, "system comands");
        systemMenu.add(createMenuItem("Exit", KeyEvent.VK_E, (event) -> System.exit(0)));

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(systemMenu);
        return menuBar;
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
