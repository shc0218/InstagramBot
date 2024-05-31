package org.shc.instaBot;

import com.github.instagram4j.instagram4j.IGClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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


//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        String key = "key";
        String cityCode = "code";
        String schoolCode = "schoolcode";;
        String path = System.getProperty("user.dir") + "/";

        getMeal getMeal = new getMeal();
        makeMealImage makeMealImage = new makeMealImage();
        InstagramBot instagramBot = new InstagramBot();
        IGClient client = instagramBot.botStart("username", "password");

        try {
            Files.createDirectory(Paths.get(System.getProperty("user.dir") + "/output"));
            System.out.println(Paths.get(System.getProperty("user.dir") + "/output") + " 디렉토리가 생성되었습니다.");
            System.exit(0);
        } catch (FileAlreadyExistsException e) {
            System.out.println("");
        } catch (NoSuchFileException e) {
            System.out.println("");
        }catch (IOException e) {
            e.printStackTrace();
        };
        while (true) {
            LocalDate date = LocalDate.now(ZoneId.of("Asia/Seoul"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String dateNow = date.format(formatter);
            LocalTime time = LocalTime.now(ZoneId.of("Asia/Seoul"));
            String timenow = time.format(DateTimeFormatter.ofPattern("HH-mm-ss"));
            Boolean isUpload = false;
            if (date.getDayOfWeek().getValue() <= 5) {
                if (timenow.equals("06-00-00")){
                    if (!isUpload) {
                        HashMap<String, ArrayList<String>> meal = getMeal.getMeal(key, cityCode, schoolCode, dateNow);
                        makeMealImage.makeMealImage(path, "origin.jpg", date, meal.get("menu_list"), String.join(",", meal.get("allergy_list")));
                        instagramBot.uploadFeed(
                                client,
                                new File(
                                        path
                                                + String.format("output/%s월%s일(%s).jpg",
                                                date.getMonthValue(),
                                                date.getDayOfMonth(),
                                                date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA))
                                ),
                                date);
                        isUpload = true;
                    }
                }
                if (timenow.equals("06-00-01")){
                    isUpload = false;
                }
            }

        }


    }
}