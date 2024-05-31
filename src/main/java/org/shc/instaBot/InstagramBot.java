package org.shc.instaBot;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;

import java.io.File;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class InstagramBot {
    public IGClient botStart (String username, String password) {
        try {
            IGClient client = IGClient.builder()
                    .username(username)
                    .password(password)
                    .login();
            return client;
        } catch (IGLoginException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void uploadFeed(IGClient client, File file, LocalDate date) {
        client.actions().timeline()
                .uploadPhoto(file, String.format("#신림고#%s월%s일#급식", date.getMonthValue(),date.getDayOfMonth()))
                .thenAccept(response -> {
                    System.out.println(
                            String.format("%s월%s일(%s) 급식 업로드 완료",
                                    date.getMonthValue(),
                                    date.getDayOfMonth(),
                                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA)
                            ));
                })
                .exceptionally(tr -> {
                    return null;
                })
                .join();
    }
}
