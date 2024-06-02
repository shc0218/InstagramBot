package org.shc.instaBot;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetMeal { // 클래스 이름은 대문자로 시작하는 것이 일반적인 자바 컨벤션입니다.

    private static final String[] ALLERGY_LIST = {"난류", "우유", "메밀", "땅콩", "대두", "밀", "고등어", "게", "새우", "돼지고기", "복숭아", "토마토", "아황산류", "호두", "닭고기", "쇠고기", "오징어", "조개류(굴, 전복, 홍합 포함)", "잣"};

    public Map<String, ArrayList<String>> getMeal(String key, String cityCode, String schoolCode, String dateNow) {
        String uri = String.format("https://open.neis.go.kr/hub/mealServiceDietInfo" +
                "?KEY=%s" +
                "&Type=json" +
                "&ATPT_OFCDC_SC_CODE=%s" +
                "&SD_SCHUL_CODE=%s" +
                "&MMEAL_SC_CODE=2" +
                "&MLSV_YMD=%s", key, cityCode, schoolCode, dateNow);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(uri).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return parseMealData(response.body().string());
            } else {
                System.err.println("Error Occurred: Response not successful");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, ArrayList<String>> parseMealData(String responseBody) {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject requestDataObject = (JSONObject) jsonParser.parse(responseBody);
            JSONArray requestDataArray = (JSONArray) requestDataObject.get("mealServiceDietInfo");
            JSONArray mealInfo = (JSONArray) ((JSONObject) requestDataArray.get(1)).get("row");
            JSONObject meals = (JSONObject) mealInfo.get(0);

            if (!meals.isEmpty()) {
                String mealString = meals.get("DDISH_NM").toString();
                return extractMealData(mealString);
            } else {
                return null;
            }
        } catch (NullPointerException e) {
            Map<String, ArrayList<String>> mealData = new HashMap<>();
            ArrayList<String> mealMenuList = new ArrayList<>();
            ArrayList<String> mealAllergyList = new ArrayList<>();
            mealMenuList.add("오늘의 급식이 없습니다!");
            mealAllergyList.add("오늘의 급식이 없습니다!");
            mealData.put("menu_list", mealMenuList);
            mealData.put("allergy_list", mealAllergyList);
            return mealData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, ArrayList<String>> extractMealData(String mealString) {
        ArrayList<String> mealMenuList = new ArrayList<>();
        ArrayList<String> mealAllergyList = new ArrayList<>();
        Map<String, ArrayList<String>> mealData = new HashMap<>();

        for (String item : mealString.split("<br/>")) {
            String[] split = item.split(" ");
            if (split.length >= 2) {
                String[] allergies = split[1].replaceAll("[()]", "").split("\\.");
                for (String allergyIndex : allergies) {
                    String allergy = ALLERGY_LIST[Integer.parseInt(allergyIndex) - 1];
                    if (!mealAllergyList.contains(allergy)) {
                        mealAllergyList.add(allergy);
                    }
                }
            }
            mealMenuList.add(split[0]);
        }

        mealData.put("menu_list", mealMenuList);
        mealData.put("allergy_list", mealAllergyList);
        return mealData;
    }
}