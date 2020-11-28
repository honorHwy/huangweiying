package cn.itcast.jd.util.image;

import java.awt.image.BufferedImage;

/**
 * 要用int，不能使用byte，因为byte最大值为128
 */
public class ImageUtils {
    /**
     * 灰度处理的方法
     */
    public static final byte Gray_Type_Min = 1;//最大值法
    public static final byte Gray_Type_Max = 2;//最小值法
    public static final byte Gray_Type_Average = 3;//平均值法
    public static final byte Gray_Type_Weight = 4;//加权法
    public static final byte Gray_Type_Red = 5;//红色值法
    public static final byte Gray_Type_Green = 6;//绿色值法
    public static final byte Gray_Type_Blue = 7;//蓝色值法
    public static final byte Gray_Type_Default = Gray_Type_Weight;//默认加权法

    /**
     * 不同颜色通道的图片
     */
    public static final byte Channel_Color_Red = 1;
    public static final byte Channel_Color_Green = 2;
    public static final byte Channel_Color_Blue = 3;

    //-----------------------------------------------------------------//

    /**
     * 灰度化处理，彩色int[][] 转 灰度byte[][]
     *
     * @param imgArrays 图像二维数组
     * @param grayType  灰度化方法
     */
    public static int[][] grayProcess(int[][] imgArrays, int grayType) throws Exception {
        int[][] newImgArrays = new int[imgArrays.length][imgArrays[0].length];
        for (int h = 0; h < imgArrays.length; h++)
            for (int w = 0; w < imgArrays[0].length; w++)
                newImgArrays[h][w] = getImageGray(getImageRgb(imgArrays[h][w]), grayType);
        return newImgArrays;
    }

    /**
     * 颜色通道处理，彩色int[][] 转 彩色int[][]
     *
     * @param imgArrays    图像二维数组
     * @param channelColor 不同颜色通道
     */
    public static int[][] channelProcess(int[][] imgArrays, int channelColor) {
        int[][] newImgArrays = new int[imgArrays.length][imgArrays[0].length];
        for (int h = 0; h < imgArrays.length; h++) {
            for (int w = 0; w < imgArrays[0].length; w++) {
                final int pixel = imgArrays[h][w];
                if (channelColor == Channel_Color_Red) {
                    newImgArrays[h][w] = pixel & 0xff0000;
                } else if (channelColor == Channel_Color_Green) {
                    newImgArrays[h][w] = pixel & 0x00ff00;
                } else if (channelColor == Channel_Color_Blue) {
                    newImgArrays[h][w] = pixel & 0x0000ff;
                }
            }
        }
        return newImgArrays;
    }

    /**
     * 二值化处理，灰度byte[][] 转 二值byte[][]
     *
     * @param imgArrays 灰度 int[][]
     * @param threshold 阈值
     */
    public static int[][] binaryProcess(int[][] imgArrays, int threshold) {
        int[][] newImgArrays = new int[imgArrays.length][imgArrays[0].length];
        for (int h = 0; h < imgArrays.length; h++)
            for (int w = 0; w < imgArrays[0].length; w++) {
                newImgArrays[h][w] = (imgArrays[h][w] < threshold ? 0 : 0xff);
            }
        return newImgArrays;
    }

    //-----------------------------------------------------------------//

    /**
     * 根据像素，返回r、g、b 的 byte[]
     *
     * @param pixel 像素值
     */
    private static int[] getImageRgb(int pixel) {
        int[] rgb = new int[3];
        rgb[0] = ((pixel >> 16) & 0xff);
        rgb[1] = ((pixel >> 8) & 0xff);
        rgb[2] = (pixel & 0xff);
        return rgb;
    }

    /**
     * 获取像素值
     *
     * @param pixel 像素值
     */
    public static long getPixel(int pixel) {
        return (pixel & 0xff) + (pixel & 0xff00) + (pixel & 0xff0000);
    }

    /**
     * 获取像素值
     *
     * @param rgb r、g、b 的 byte[]
     */
    public static long getPixel(int[] rgb) {
        return (rgb[0] << 16) + (rgb[1] << 8) + rgb[2];
    }

    /**
     * 根据r、g、b 的 byte[]，返回灰度值
     *
     * @param rgb      r、g、b颜色通道的值
     * @param grayType 不同灰度处理的方法
     */
    private static int getImageGray(int[] rgb, int grayType) throws Exception {
        if (grayType == Gray_Type_Average) {
            return ((rgb[0] + rgb[1] + rgb[2]) / 3);   //rgb之和除以3
        } else if (grayType == Gray_Type_Weight) {
            return (int) (0.3 * rgb[0] + 0.59 * rgb[1] + 0.11 * rgb[2]);
        } else if (grayType == Gray_Type_Red) {
            return rgb[0];//取红色值
        } else if (grayType == Gray_Type_Green) {
            return rgb[1];//取绿色值
        } else if (grayType == Gray_Type_Blue) {
            return rgb[2];//取蓝色值
        }
        //比较三个数的大小
        int gray = rgb[0];
        for (int i = 1; i < rgb.length; i++) {
            if (grayType == Gray_Type_Min) {
                if (gray > rgb[i]) {
                    gray = rgb[i];//取最小值
                }
            } else if (grayType == Gray_Type_Max) {
                if (gray < rgb[i]) {
                    gray = rgb[i];//取最大值
                }
            } else {
                throw new Exception("grayType出错");
            }
        }
        return gray;
    }

    //-----------------------------------------------------------------//

    /**
     * 获取图像像素 byte[][] rgb值
     *
     * @param image BufferedImage图像对象
     */
    public static int[][] getImageRgb(BufferedImage image) {
        int[][] imgArrays = new int[image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getHeight(); i++)
            for (int j = 0; j < image.getWidth(); j++)
                imgArrays[i][j] = image.getRGB(j, i);
        return imgArrays;
    }

    /**
     * 获取图像像素 byte[][] 灰度值
     *
     * @param image BufferedImage图像对象
     */
    public static int[][] getImageGray(BufferedImage image) throws Exception {
        return grayProcess(getImageRgb(image), Gray_Type_Default);
    }

    /**
     * 获取图像像素 byte[][] 二值
     *
     * @param image BufferedImage图像对象
     */
    public static int[][] getImageBinary(BufferedImage image) throws Exception {
        return binaryProcess(grayProcess(getImageRgb(image), Gray_Type_Default), 0xff / 2);
    }

    /**
     * 图像像素填充 byte[][]
     *
     * @param image     BufferedImage图像对象
     * @param imgArrays 二维像素
     */
    public static void setImageBytes(BufferedImage image, int[][] imgArrays) {
        for (int i = 0; i < image.getHeight(); i++)
            for (int j = 0; j < image.getWidth(); j++)
                image.setRGB(j, i, (byte) imgArrays[i][j]);
    }

    /**
     * 图像像素填充 byte[][]
     *
     * @param image     BufferedImage图像对象
     * @param imgArrays 二维像素
     */
    public static void setImageBytes(BufferedImage image, byte[][] imgArrays) {
        for (int i = 0; i < image.getHeight(); i++)
            for (int j = 0; j < image.getWidth(); j++)
                image.setRGB(j, i, imgArrays[i][j]);
    }

    /**
     * 图像像素填充 int[][]
     *
     * @param image     BufferedImage图像对象
     * @param imgArrays 二维像素
     */
    public static void setImageRgb(BufferedImage image, int[][] imgArrays) {
        for (int i = 0; i < image.getHeight(); i++)
            for (int j = 0; j < image.getWidth(); j++)
                image.setRGB(j, i, imgArrays[i][j]);
    }

    /**
     * 图像像素填充 将 byte[][] 变为 int[][] 进行填充
     *
     * @param image     BufferedImage图像对象
     * @param imgArrays 二维像素
     */
    public static void setImageRgbByByte(BufferedImage image, int[][] imgArrays) {
        for (int i = 0; i < image.getHeight(); i++)
            for (int j = 0; j < image.getWidth(); j++)
                image.setRGB(j, i, imgArrays[i][j] + (imgArrays[i][j] << 8) + (imgArrays[i][j] << 16));
    }

    //-----------------------------------------------------------------//

    /**
     * 将数组的值域分布到0--0xff之间
     */
    public static int[][] rangeToByte(int[][] arrays) {
        int max = arrays[0][0], min = arrays[0][0];
        for (int[] array : arrays) {
            for (int value : array) {
                if (value > max) {
                    max = value;
                } else if (value < min) {
                    min = value;
                }
            }
        }
        //
        int[][] newArrays = new int[arrays.length][];
        int range = max - min + 1;
        double multiply = (0xff + 1.0) / range;
        for (int i = 0; i < arrays.length; i++) {
            int[] array = arrays[i];
            int[] newArray = new int[array.length];
            for (int j = 0; j < array.length; j++) {
                int value = (int) Math.round((array[j] - min) * multiply);
                newArray[j] = value > 0xff ? 0xff : value;
            }
            newArrays[i] = newArray;
        }
        return newArrays;
    }

    /**
     * 统计灰度分布,0-->255
     */
    public static int[] statisticGray(int[][] arrays) {
        int[] statisticArray = new int[0xff + 1];
        int row = arrays.length;
        int column = arrays[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                statisticArray[arrays[i][j]]++;
            }
        }
        return statisticArray;
    }
}