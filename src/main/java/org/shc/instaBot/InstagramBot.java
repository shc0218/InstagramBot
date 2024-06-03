package org.shc.instaBot;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.models.media.Media;
import com.github.instagram4j.instagram4j.models.media.reel.ReelMedia;
import com.github.instagram4j.instagram4j.requests.direct.DirectInboxRequest;
import com.github.instagram4j.instagram4j.requests.highlights.HighlightsCreateReelRequest;
import com.github.instagram4j.instagram4j.requests.highlights.HighlightsEditReelRequest;
import com.github.instagram4j.instagram4j.requests.highlights.HighlightsUserTrayRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.direct.DirectInboxResponse;
import com.github.instagram4j.instagram4j.responses.highlights.HighlightsCreateReelResponse;
import com.github.instagram4j.instagram4j.responses.highlights.HighlightsUserTrayResponse;
import com.github.instagram4j.instagram4j.responses.media.MediaResponse;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class InstagramBot {
    public IGClient botStart (String username, String password) {
        try {
            IGClient client = IGClient.builder()
                    .username(username)
                    .password(password).login();
            return client;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void uploadFeed(IGClient client, File file, LocalDate date) {
        client.actions().timeline()
                .uploadPhoto(file, String.format("#신림고#%s월%s일#급식", date.getMonthValue(),date.getDayOfMonth()))
                .thenAccept(response -> {
                    System.out.println(
                            String.format("%s월%s일(%s) 급식 게시물 업로드 완료",
                                    date.getMonthValue(),
                                    date.getDayOfMonth(),
                                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA)
                            ));
                })
                .join();
    }

    public void uploadStory(IGClient client, File file, LocalDate date) {
        client.actions().story()
                .uploadPhoto(file)
                .thenAccept(response -> {
                    System.out.println(
                            String.format("%s월%s일(%s) 급식 스토리 업로드 완료",
                                    date.getMonthValue(),
                                    date.getDayOfMonth(),
                                    date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA)
                            )
                    );
                    addHighlight(client, response.getMedia().getId(), date);
                })
                .join();
    }


    private void addHighlight(IGClient client, String storyId, LocalDate date) {
        IGResponse response = new HighlightsEditReelRequest("highlight:18350891044106587", "2024", storyId, new String[] {storyId}, null).execute(client).join();
        System.out.println(
                String.format("%s월%s일(%s) 급식 스토리 하이라이트 추가 완료",
                        date.getMonthValue(),
                        date.getDayOfMonth(),
                        date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREA)
                )
        );
    }
}
