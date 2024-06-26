package org.shc.instaBot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class makeMealImage {
    public void makeFeedImage(String path, LocalDate date, ArrayList<String> mealMenuList, String mealAllergyList) {
        try {
            BufferedImage image = ImageIO.read(new File(path + "feed_origin.jpg")); // 배경 파일 불러오기
            Graphics g = image.getGraphics();
            g.setColor(Color.BLACK);
            g.setFont(new Font("나눔스퀘어라운드 ExtraBold", Font.BOLD, 70));
            g.drawString(
                    String.format("%s월 %s일 (%s) 급식",
                            date.getMonthValue(),
                            date.getDayOfMonth(),
                            date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA)),
                    300,
                    200);

            g.setFont(new Font("나눔스퀘어라운드 ExtraBold", Font.PLAIN, 45));
            for (int i = 0; i <mealMenuList.size(); i++) {
                g.drawString(mealMenuList.get(i), 100, 400 + (75*i)); // 문자열 삽입
            }
            g.setFont(new Font("나눔스퀘어라운드 ExtraBold", Font.PLAIN, 30));
            g.drawString("*알러지: " + mealAllergyList, 50, 1000); // 문자열 삽입
            ImageIO.write(
                    image,
                    "jpg",
                    new File(
                            path + String.format("/output/%s월%s일(%s)-feed.jpg",
                                    date.getMonthValue(),
                                    date.getDayOfMonth(),
                                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA))
                    )
            );
            System.out.println(String.format("%s월%s일(%s) 피드 이미지 생성 완료!",
                    date.getMonthValue(),
                    date.getDayOfMonth(),
                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA)));
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void makeStoryImage(String path, LocalDate date, ArrayList<String> mealMenuList, String mealAllergyList) {
        try {
            BufferedImage image = ImageIO.read(new File(path + "story_origin.jpg")); // 배경 파일 불러오기
            Graphics g = image.getGraphics();
            g.setColor(Color.BLACK);
            g.setFont(new Font("나눔스퀘어라운드 ExtraBold", Font.BOLD, 90));
            g.drawString(
                    String.format("%s월 %s일 (%s) 급식",
                            date.getMonthValue(),
                            date.getDayOfMonth(),
                            date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA)),
                    200,
                    200);

            g.setFont(new Font("나눔스퀘어라운드 ExtraBold", Font.PLAIN, 70));
            for (int i = 0; i <mealMenuList.size(); i++) {
                g.drawString(mealMenuList.get(i), 100, 600 + (100*i)); // 문자열 삽입
            }
            g.setFont(new Font("나눔스퀘어라운드 ExtraBold", Font.PLAIN, 30));
            g.drawString("*알러지: " + mealAllergyList, 50, 1800); // 문자열 삽입
            ImageIO.write(
                    image,
                    "jpg",
                    new File(
                            path + String.format("/output/%s월%s일(%s)-story.jpg",
                                    date.getMonthValue(),
                                    date.getDayOfMonth(),
                                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA))
                    )
            );
            System.out.println(String.format("%s월%s일(%s) 스토리 이미지 생성 완료!",
                    date.getMonthValue(),
                    date.getDayOfMonth(),
                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA)));
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
