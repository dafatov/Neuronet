package ru.demetrious.util;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private final int max;
    private JProgressBar progressBar;
    private JTextField textField, textField2, textField3;

    public GUI(int max) {
        super("Neuronet.Neuronet learning");
        this.max = max;
        init();
    }

    /**
     * Creation of GUI
     */
    private void init() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        centerPanel.add(progressPanel, BorderLayout.SOUTH);

        textField = new JTextField("0.0%");
        textField.setForeground(Color.BLACK);
        textField.setEditable(false);
        textField.setFocusable(false);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("TimesRoman", Font.BOLD, 256));
        centerPanel.add(textField, BorderLayout.CENTER);

        textField2 = new JTextField("0");
        textField2.setForeground(Color.BLACK);
        textField2.setEditable(false);
        textField2.setFocusable(false);
        textField2.setHorizontalAlignment(SwingConstants.RIGHT);
        textField2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        progressPanel.add(textField2);

        textField3 = new JTextField("0");
        textField3.setForeground(Color.BLACK);
        textField3.setEditable(false);
        textField3.setFocusable(false);
        textField3.setHorizontalAlignment(SwingConstants.RIGHT);
        textField3.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        progressPanel.add(textField3);

        progressBar = new JProgressBar(new DefaultBoundedRangeModel(0, 0, 0, max));
        progressBar.setForeground(Color.BLACK);
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2));
        pack();
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(getSize());
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 4,
                Toolkit.getDefaultToolkit().getScreenSize().height / 4);
    }

    /**
     * Change progress bar status
     *
     * @param value new Value of progress bar status
     */
    public void setProgress(int value) {
        StringBuilder tmp = new StringBuilder();

        if (value < 0 || value > max && max > 0) {
            return;
        }
        if (max > 0) {
            tmp.append((double) Math.round((double) (value) / max * 10000) / 100);
            while (tmp.toString().split("\\.")[1].length() < 2) {
                tmp.append("0");
            }
            while (tmp.toString().split("\\.")[0].length() < 2) {
                tmp.insert(0, '0');
            }

            progressBar.setValue(value);
            textField3.setText(String.valueOf(max));
        }
        textField2.setText(String.valueOf(value));
        if (value == max || max == 0) {
            textField.setText("ETUDE");
            return;
        }
        textField.setText(tmp + "%");
    }
}
