package minicraft.core;

import java.awt.Insets;
import java.awt.event.*;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.*;

public class Crash extends Game {
    public void init() {}
    public static class CrashData {
        public final String type;
        public final String content;
		public String name;
		public String position;
        public CrashData(String type, String content) {
            this.type = type;
            this.content = content;
        }
        public CrashData(String type, String content, String name, String position) {
            this.type = type;
            this.content = content;
			this.name = name;
			this.position = position;
        }
    }
    public static String getStackTrace(Throwable th) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        th.printStackTrace(pw);
        return sw.toString();
    }
    public Crash(CrashData data) {
        JFrame frame = new JFrame();
        frame.setSize(600, 450);
        frame.setLayout(null);
        frame.setTitle("Minicraft Crash");
        JTextArea textArea = new JTextArea();
        // textArea.setSize(600, height);;
        textArea.setEditable(false);
        switch(data.type) {
            case "ModLoad":
                frame.setName("Mod Loading Exception");
				if (data.name.length()>0) textArea.append(data.name);
                if (data.position.length()>0) textArea.append("; On Mod "+data.position+"\n");
				textArea.append(data.content);
        }
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setSize(600, 395);
		frame.add(scroll);
        JButton b = new JButton("Close");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
				if (debug) System.out.println("Crash window exits, system exits.");
				System.exit(0);
            }
        });
        b.setBounds(540, 395, 60, 20);
		b.setHorizontalTextPosition(SwingConstants.CENTER);
		b.setVerticalTextPosition(SwingConstants.CENTER);
		b.setMargin(new Insets(0, 0, 0, 0));
        frame.add(b);
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
				if (debug) System.out.println("Crash window exits, system exits.");
				System.exit(0);
            }
        });
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
