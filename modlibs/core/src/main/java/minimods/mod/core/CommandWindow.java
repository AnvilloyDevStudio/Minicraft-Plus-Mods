package minimods.mod.core;

import java.awt.event.*;
import java.util.List;

import javax.swing.*;

import minicraft.core.World;
import minicraft.level.Level;


public class CommandWindow {
    public void init() {}
    private JFrame frame;
    public CommandWindow() {
        JFrame frame = new JFrame();
        frame.setSize(300, 80);
        frame.setLayout(null);
        frame.setTitle("Command Window");
        JTextField txt = new JTextField();
        txt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(e.getActionCommand());
                txt.setText("");
            }
        });
        txt.setAutoscrolls(true);
        txt.setBounds(0, 0, 300, 30);
        frame.add(txt);
        frame.setResizable(false);
		frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.frame = frame;
    }
    public void close() {
        this.frame.setVisible(false);
        this.frame.dispose();
    }
    private void executeCommand(String command) {
        List<String> args = List.of(command.toLowerCase().split(" "));
        String cmd = args.get(0);
        switch (cmd) {
            case "settile":
				World.levels[World.currentLevel].setTile(Integer.parseInt(args.get(1)), Integer.parseInt(args.get(2)), args.get(3)+(args.size()>4? " "+args.get(4): ""));
                break;
        }
    }
}
