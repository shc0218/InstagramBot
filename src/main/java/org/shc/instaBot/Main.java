package org.shc.instaBot;

import com.github.instagram4j.instagram4j.IGClient;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {

    public static void main(String[] args) {

        String key = "9361f9c21e834ef58bec4e49db0f2a31";
        String cityCode = "B10";
        String schoolCode = "7010092";;
        String path = System.getProperty("user.dir") + "/";
        LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        GetMeal getMeal = new GetMeal(); 

        makeMealImage makeMealImage = new makeMealImage();
        InstagramBot instagramBot = new InstagramBot();
        IGClient client = instagramBot.botStart("username", "password");

        try {
            Files.createDirectory(Paths.get(System.getProperty("user.dir") + "/output"));
            System.out.println(Paths.get(System.getProperty("user.dir") + "/output") + " 디렉토리가 생성되었습니다.");
        } catch (FileAlreadyExistsException e) {
            System.out.println("");
        } catch (NoSuchFileException e) {
            System.out.println("");
        }catch (IOException e) {
            e.printStackTrace();
        };
        Boolean isUpload = false;
        while (true) {
        LocalTime time = LocalTime.now(ZoneId.of("Asia/Seoul"));
        String timenow = time.format(DateTimeFormatter.ofPattern("HH-mm-ss"));
            date = LocalDate.now(ZoneId.of("Asia/Seoul"));
            formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            time = LocalTime.now(ZoneId.of("Asia/Seoul"));
            timenow = time.format(DateTimeFormatter.ofPattern("HH-mm-ss"));
            if (date.getDayOfWeek().getValue() == 7 || date.getDayOfWeek().getValue() <= 4) {
                if (timenow.equals("22-00-00")){
                    if (!isUpload) {
                        date = date.plusDays(1);
                        String dateNow = date.format(formatter);
                        System.out.println(dateNow);
                        Map<String, ArrayList<String>> meal = getMeal.getMeal(key, cityCode, schoolCode, dateNow);
                        if (!meal.get("menu_list").contains("오늘의 급식이 없습니다!") && !meal.get("allergy_list").contains("오늘의 급식이 없습니다!")) {
                            makeMealImage.makeFeedImage(path, date, meal.get("menu_list"), String.join(",", meal.get("allergy_list")));
                            makeMealImage.makeStoryImage(path, date, meal.get("menu_list"), String.join(",", meal.get("allergy_list")));
                            instagramBot.uploadFeed(
                                    client,
                                    new File(
                                            path
                                                    + String.format("output/%s월%s일(%s)-feed.jpg",
                                                    date.getMonthValue(),
                                                    date.getDayOfMonth(),
                                                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA))
                                    ),
                                    date);
                            instagramBot.uploadStory(
                                    client,
                                    new File(
                                            path
                                                    + String.format("output/%s월%s일(%s)-story.jpg",
                                                    date.getMonthValue(),
                                                    date.getDayOfMonth(),
                                                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA))
                                    ),
                                    date);
                            isUpload = true;
                        }
                    }
                }
                if (timenow.equals("22-00-01")){
                    isUpload = false;
                }
            }
        }
    }
}