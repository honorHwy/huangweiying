package cn.itcast.jd.util.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class ImageViewUtils {

    /**
     * 展示图片像素值--rgb的
     */
    public static void getRgbPixel(String sourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(sourcePath));
            int[][] imgArrays = ImageUtils.getImageRgb(image);
            getPixel(imgArrays, sourcePath.split("\\.")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示图片像素值--灰度化的
     */
    public static void getGrayPixel(String sourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(sourcePath));
            int[][] imgArrays = ImageUtils.getImageGray(image);
            getPixel(imgArrays, sourcePath.split("\\.")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示图片像素值--二值化的
     */
    public static void getBinaryPixel(String sourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(sourcePath));
            int[][] imgArrays = ImageUtils.getImageBinary(image);
            getPixel(imgArrays, StringUtils.getFileName(sourcePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示图片像素值（分为原始的、rgb的、灰度化的、二值化的）
     *
     * @param array 元素的像素值
     * @param title Iframe窗口标题
     */
    private static void getPixel(int[][] array, String title) {
        final int frameHeight = 800;
        final int frameWidth = 1000;
        //数据处理
        int row = array.length;
        int column = array[0].length + 1;
        Object[] firstRow = new Object[column];//列数
        firstRow[0] = "列数/行数";
        for (int i = 1; i < column; i++) {
            firstRow[i] = i;
        }
        Object[][] contentRow = new Object[row][column];//行数
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (j == 0) {
                    contentRow[i][0] = i + 1;
                } else {
                    contentRow[i][j] = array[i][j - 1];
                }
            }
        }

        //JTable表格
        JTable table = new JTable(contentRow, firstRow);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //每列的宽度为：当前列最长内容单元格的宽度
        for (int i = 0; i < table.getColumnCount(); i++) {
            int maxWidth = 0;
            for (int j = 0; j < table.getRowCount(); j++) {
                TableCellRenderer rend = table.getCellRenderer(j, i);
                Object value = table.getValueAt(j, i);
                Component comp = rend.getTableCellRendererComponent(table, value, false, false, j, i);
                maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);
            }
            table.getColumnModel().getColumn(i).setPreferredWidth((int) (maxWidth + maxWidth * 0.05));
        }

        //JScrollPane面板
        JScrollPane scrollPane = new JScrollPane(table,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);//JScrollPane

        //JFrame窗口
        JFrame frame = new JFrame(title);
        frameInit(frame, scrollPane, frameWidth, frameHeight);
    }

    //-----------------------------------------------------------------//

    /**
     * 查看图片
     *
     * @param sourcePath 图片地址
     */
    public static void showImage(String sourcePath) {
        try {
            //JLabel
            JLabel label = new JLabel();
            label.setIcon(new ImageIcon(sourcePath));


            //JPanel面板
            JPanel panel = new JPanel();
            panel.add(label);

            //JFrame窗口
            JFrame frame = new JFrame("图片查看：" + StringUtils.getFileName(sourcePath));
            frame.setContentPane(panel);
            frame.setSize(200, 200);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//只会关闭当前窗口
            frame.setLocationRelativeTo(null);//居中
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看灰度直方图
     */
    public static void showHistogram(String sourcePath) {
        final int histogramFrameWidth = 500;
        final int histogramFrameHeight = 500;
        final int histogramWidth = 400;
        final int histogramHeight = 400;
        final int histogramMargin = 20;
        try {
            Canvas histCanvas = new Canvas();
            histCanvas.setSize(histogramWidth, histogramHeight);

            //JPanel面板
            JPanel histogramPanel = new JPanel();
            histogramPanel.setSize(histogramWidth, histogramHeight);
            histogramPanel.setPreferredSize(new Dimension(histogramWidth, histogramHeight));
            histogramPanel.setBorder(new LineBorder(Color.blue));
            histogramPanel.add(histCanvas);

            JPanel panel = new JPanel();
            ImageViewUtils.rootPanelInit(panel, histogramFrameWidth, histogramFrameHeight);
            panel.add(histogramPanel, BorderLayout.CENTER);

            //JFrame窗口
            JFrame frame = new JFrame("title");
            ImageViewUtils.frameInit(frame, panel, histogramFrameWidth, histogramFrameHeight);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        BufferedImage image = ImageIO.read(new File(sourcePath));
                        int[][] imgArrays = ImageUtils.getImageGray(image);
                        int[] statisticArray = ImageUtils.statisticGray(imgArrays);
                        int max = 0;
                        for (int value : statisticArray) {
                            if (max < value) {
                                max = value;
                            }
                        }
                        double multiply = (histogramHeight * 0.8) / max;
                        for (int i = 0; i < statisticArray.length; i++) {
                            statisticArray[i] = (int) Math.round(statisticArray[i] * multiply);
                        }

                        Graphics g = histCanvas.getGraphics();
                        Color c = g.getColor();
                        g.setColor(Color.red);
                        g.drawLine(histogramMargin, histogramHeight - histogramMargin, histogramWidth - histogramMargin, histogramHeight - histogramMargin);
                        g.drawLine(histogramWidth - histogramMargin, histogramHeight - histogramMargin, histogramWidth - histogramMargin * 2, (int) (histogramHeight - histogramMargin * 0.66));
                        g.drawLine(histogramWidth - histogramMargin, histogramHeight - histogramMargin, histogramWidth - histogramMargin * 2, (int) (histogramHeight - histogramMargin * 1.33));
                        g.drawString("灰度级", histogramWidth - 80, histogramHeight - 5);
                        g.drawLine(histogramMargin, histogramHeight - histogramMargin, histogramMargin, histogramMargin);
                        g.drawLine(histogramMargin, histogramMargin, (int) (histogramMargin * 0.66), histogramMargin * 2);
                        g.drawLine(histogramMargin, histogramMargin, (int) (histogramMargin * 1.33), histogramMargin * 2);
                        g.drawString("像素个数", 0, 20);
                        g.setColor(Color.black);
                        for (int i = 0; i < statisticArray.length; i++) {
                            g.drawLine(histogramMargin + i, histogramHeight - histogramMargin, histogramMargin + i, histogramHeight - histogramMargin - statisticArray[i]);
                            if (i % 30 == 0) {
                                g.drawString(i + "", histogramMargin + i, histogramHeight - 5);
                            }
                        }
                        g.setColor(c);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------------//

    /**
     * 设置指定控件的边距，本质上是设置控件的不可见边框
     *
     * @param component 控件
     * @param top       上边距
     * @param bottom    下边距
     * @param left      左边距
     * @param right     右边距
     */
    public static void setMargin(JComponent component, int top, int bottom, int left, int right) {
        Border border = BorderFactory.createEmptyBorder(top, left, bottom, right);
        component.setBorder(border);
    }

    /**
     * 初始化frame
     */
    public static void frameInit(JFrame frame, JComponent panel, int width, int height) {
        frame.setContentPane(panel);
        frame.setSize(width, height);//没有这个会不居中
        frame.setPreferredSize(new Dimension(width, height));//没有这个大小不会变
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//只会关闭当前窗口
        frame.setLocationRelativeTo(null);//居中
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * 初始化panel
     */
    public static void rootPanelInit(JPanel panel, int width, int height) {
        panel.setSize(width, height);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        panel.setLayout(new BorderLayout());
    }
}
