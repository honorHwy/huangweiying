package cn.itcast.jd.util.image;

import javax.swing.*;
import java.awt.*;

public class ImageTool {

    /**
     * 程序入口
     */
    public static void main(String[] args) {
        final int frameHeight = 200;
        final int frameWidth = 800;
        //JPanel面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 1, 1, 1));//网格布局

        JFrame frame = new JFrame("图像处理小工具");
        openImagePixel(frame, centerPanel);//查看图片像素

        JPanel panel = new JPanel();
        ImageViewUtils.rootPanelInit(panel, frameWidth, frameHeight);
        panel.add(centerPanel, BorderLayout.CENTER);

        //JFrame窗口
        ImageViewUtils.frameInit(frame, panel, frameWidth, frameHeight);
    }

    /**
     * 展示图片或者图片像素值（分为原始的、rgb的、灰度化的、二值化的）
     */
    private static void openImagePixel(JFrame frame, JComponent parentView) {
        JButton label = new JButton();
        JTextField textField = new JTextField();
        JPanel panel_1 = new JPanel();
        ImageViewUtils.setMargin(panel_1, 20, 10, 10, 20);
        panel_1.setLayout(new FlowLayout(FlowLayout.LEFT));//流式布局（默认），左对齐
        panel_1.add(label);
        panel_1.add(textField);

        JButton btnShow = new JButton();
        JButton btnRgb = new JButton();
        JButton btnGray = new JButton();
        JButton btnBinary = new JButton();
        JButton btnHistogram = new JButton();

        JPanel panel_2 = new JPanel();
        ImageViewUtils.setMargin(panel_2, 20, 10, 10, 20);
        panel_2.setLayout(new FlowLayout(FlowLayout.LEFT));//流式布局（默认），左对齐
        panel_2.add(btnShow);
        panel_2.add(btnRgb);
        panel_2.add(btnGray);
        panel_2.add(btnBinary);
        panel_2.add(btnHistogram);

        parentView.add(panel_1);
        parentView.add(panel_2);

        label.setText("选择文件：");
        label.addActionListener(e -> {
            FileDialog openFileDialog = new FileDialog(frame, "选择文件");
            openFileDialog.setMode(FileDialog.LOAD);    //设置此对话框为从文件加载内容
            openFileDialog.setFile("*.jpg;*.jpeg;*.gif;*.png;*.bmp;*.tif;");
            openFileDialog.setVisible(true);

            String fileName = openFileDialog.getFile();
            String directory = openFileDialog.getDirectory();
            if (null != fileName) {
                textField.setText(directory + fileName);
            } else {
                JOptionPane.showMessageDialog(frame, "您已经取消选择了，请重新选择!");
            }
        });
        //
        textField.setColumns(45);
        //
        btnShow.setText("查看图片");
        btnShow.addActionListener(e -> {
            ImageViewUtils.showImage(textField.getText());
        });
        //
        btnRgb.setText("RGB值");
        btnRgb.addActionListener(e -> {
            ImageViewUtils.getRgbPixel(textField.getText());
        });
        //
        btnGray.setText("灰度值");
        btnGray.addActionListener(e -> {
            ImageViewUtils.getGrayPixel(textField.getText());
        });
        //
        btnBinary.setText("二值化值");
        btnBinary.addActionListener(e -> {
            ImageViewUtils.getBinaryPixel(textField.getText());
        });
        //
        btnHistogram.setText("灰度直方图");
        btnHistogram.addActionListener(e -> {
            ImageViewUtils.showHistogram(textField.getText());
        });
    }
}