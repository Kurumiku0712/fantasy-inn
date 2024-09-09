package com.percyyang.FantasyInn.utils;

import java.util.ArrayList;
import java.util.List;

public class ChatbotUtils {

    // 控制 chatbot 头像风格提示词
    public static List<String> selfieTypes() {
        return List.of(
                "closeup with head and partial shoulders",
                "Reflection in a mirror",
                "action selfie, person in motion",
                "candid photo",
                "standing in nature",
                "sitting on a chair",
                "indoor photograph",
                "outdoor photograph"
        );
    }

    // 控制 chatbot 动漫头像风格提示词
    public static List<String> AnimeSelfieTypes() {
        return List.of(
                "anime style closeup with head and partial shoulders",
                "anime style reflection in a mirror",
                "anime action selfie, person in motion",
                "candid anime style photo",
                "anime character standing in nature",
                "anime character sitting on a chair",
                "indoor anime style photograph",
                "outdoor anime style photograph"
        );
    }


    // 控制 chatbot 性格类型提示词
    public static List<String> generatePersonalityTypes() {
        List<String> personalityTypes = new ArrayList<>();

        String[] dimensions = {
                "E,I", // Extraversion or Introversion
                "S,N", // Sensing or Intuition
                "T,F", // Thinking or Feeling
                "J,P"  // Judging or Perceiving
        };

        // Generate all combinations
        for (String e : dimensions[0].split(",")) {
            for (String s : dimensions[1].split(",")) {
                for (String t : dimensions[2].split(",")) {
                    for (String j : dimensions[3].split(",")) {
                        personalityTypes.add(e + s + t + j);
                    }
                }
            }
        }

        return personalityTypes;
    }

}
