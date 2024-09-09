package com.percyyang.FantasyInn.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.percyyang.FantasyInn.entity.Chatbot;
import com.percyyang.FantasyInn.repo.ChatbotRepository;
import com.percyyang.FantasyInn.service.interfaces.IChatbotCreationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static com.percyyang.FantasyInn.utils.ChatbotUtils.*;

@Service
public class ChatbotCreationService implements IChatbotCreationService {

    private static final String STABLE_DIFFUSION_URL =
          "https://44efe8d25975b2b2c4.gradio.live/sdapi/v1/txt2img";

    private OpenAiChatModel chatModel;

    private HttpClient httpClient;

    private HttpRequest.Builder stableDiffusionRequestBuilder;

    private List<Chatbot> generatedChatbots = new ArrayList<>();

    private static final String PROFILES_FILE_PATH = "chatbots.json";

    @Value("${startup-actions.initChatbotInfo}")
    private Boolean initChatbotsInfo;

    @Value("#{${fantasyinn.character.user}}")
    private Map<String, String> userInfoProperties;

    private ChatbotRepository chatbotRepository;


    public ChatbotCreationService(OpenAiChatModel chatModel, ChatbotRepository chatbotRepository) {
        this.chatModel = chatModel;
        this.chatbotRepository = chatbotRepository;
        this.httpClient = HttpClient.newHttpClient();
        this.stableDiffusionRequestBuilder = HttpRequest.newBuilder()
                .setHeader("Content-type", "application/json")
                .uri(URI.create(STABLE_DIFFUSION_URL));
    }

    private static <T> T getRandomElement(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    @Override
    public Chatbot createChatbots(int numberOfChatbots) {

        if (!this.initChatbotsInfo) {
            return null;
        }

        // 控制 age range, ethnicities, genders for generating chatbots
        List<Integer> ages = new ArrayList<>();
        for (int i = 10; i <= 40; i++) {
            ages.add(i);
        }

//        List<String> ethnicities = new ArrayList<>(List.of
//                ("White", "Black", "Asian", "Indian", "Hispanic"));
        List<String> AnimeEthnicities = new ArrayList<>(List.of(
                "Elves", "Dwarves", "Fairies", "Demons", "Angels", "Dragons",
                "Vampires", "Werewolves", "Robots/Androids", "Giants", "Neko"));

        List<String> personalityTypes = generatePersonalityTypes();
        List<String> genderTypes = new ArrayList<>(List.of("male", "female", "non_binary"));

        while (this.generatedChatbots.size() < numberOfChatbots) {
            int age = getRandomElement(ages);
            if (age == 0) { age = 18; }
            System.out.println("Generated Age: " + age);

            String ethnicity = getRandomElement(AnimeEthnicities);
            if (ethnicity == null) { ethnicity = "Human"; }
            System.out.println("Generated Ethnicity: " + ethnicity);

            String gender = getRandomElement(genderTypes);
            if (gender == null) { gender = "Unknown"; }
            System.out.println("Generated Gender: " + gender);

            String personalityType = getRandomElement(personalityTypes);
            if (personalityType == null) { personalityType = "Unknown"; }
            System.out.println("Generated Personality Type: " + personalityType);

            String prompt = "Create a Tinder profile persona of an " + personalityType + " "
                    + age + " year old " + ethnicity + " " + gender + " "
                    + "including the first name, last name, personality type and a tinder bio. "
                    + " Save the profile using the saveChatbotInfo function";
            System.out.println("Print Prompt: " + prompt);

            // 让ChatGPT根据提示词生成
            ChatResponse response = chatModel.call(new Prompt(prompt,
                    OpenAiChatOptions.builder().withFunction("saveChatbotInfo").build()));
            System.out.println("From ChatGPT: " + "\n" + response.getResult().getOutput().getContent());

        }

        // Save the values in a JSON file
        saveChatbotInfoToJson(this.generatedChatbots);

        // Assuming we're generating only one profile, return the last one generated
        if (!this.generatedChatbots.isEmpty()) {
            Chatbot newChatbot = this.generatedChatbots.get(this.generatedChatbots.size() - 1);
            chatbotRepository.save(newChatbot); // Save to the database
            return newChatbot; // Return the generated chatbot
        }
        return null;
    }

    @Override
    public void saveChatbotInfoToJson(List<Chatbot> generatedChatbots) {
        try {
            Gson gson = new Gson();
            List<Chatbot> existingProfiles = gson.fromJson(
                    new FileReader(PROFILES_FILE_PATH),
                    new TypeToken<ArrayList<Chatbot>>() {
                    }.getType()
            );
            generatedChatbots.addAll(existingProfiles);
            List<Chatbot> chatbotsWithImages = new ArrayList<>();
            for (Chatbot chatbot : generatedChatbots) {
                if (chatbot.getImageUrl() == null || "".equals(chatbot.getImageUrl())) {
                    chatbot = generateChatbotImage(chatbot);
                }
                chatbotsWithImages.add(chatbot);
            }
            String jsonString = gson.toJson(chatbotsWithImages);
            FileWriter writer = new FileWriter(PROFILES_FILE_PATH);
            writer.write(jsonString);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Chatbot generateChatbotImage(Chatbot chatbot) {

        String uuid = StringUtils.isBlank(chatbot.getId()) ? UUID.randomUUID().toString() : chatbot.getId();
        chatbot = new Chatbot(
                uuid,
                StringUtils.defaultIfBlank(chatbot.getFirstName(), "No Name"),
                StringUtils.defaultIfBlank(chatbot.getLastName(), "Chatbot"),
                chatbot.getAge(),
                StringUtils.defaultIfBlank(chatbot.getEthnicity(), "Human"),
                StringUtils.defaultIfBlank(chatbot.getGender(), "Unknown"),
                StringUtils.defaultIfBlank(chatbot.getBio(), "Unknown"),
                uuid + ".jpg",
                StringUtils.defaultIfBlank(chatbot.getPersonality(), "Unknown")
        );
//        String randomSelfieType = getRandomElement(selfieTypes());
        String randomSelfieType = getRandomElement(AnimeSelfieTypes());

        String prompt = "Selfie of a " + chatbot.getAge() + " year old " +
                chatbot.getPersonality() + " " + chatbot.getEthnicity() + " " +
                chatbot.getGender() + ", " + randomSelfieType +
                ", anime style, vibrant colors, soft shading, cel-shaded, highly stylized, " +
                "large expressive eyes, " + "smooth skin, cute and clean facial features, " +
                "2D illustration, anime background, " +
                "anime portrait, best quality, highly detailed. Bio- " + chatbot.getBio();
//                ", photorealistic skin texture and details, individual hairs and pores visible, " +
//                "highly detailed, photorealistic, hyperrealistic, subsurface scattering, 4k DSLR, " +
//                        "ultrarealistic, best quality, masterpiece. Bio- " + chatbot.getBio();

        String negativePrompt = "multiple faces, low-res, text, error, cropped, worst quality, " +
                "low quality, jpeg artifacts, ugly, duplicate, morbid, mutilated, out of frame, " +
                "extra fingers, mutated hands, poorly drawn hands, poorly drawn face, " +
                "mutation, deformed, blurry, dehydrated, bad anatomy, bad proportions, extra limbs, " +
                "cloned face, disfigured, gross proportions, malformed limbs, missing arms, " +
                "missing legs, extra arms, extra legs, fused fingers, too many fingers, " +
                "long neck, username, watermark, signature";

        String jsonString = "{ \"prompt\": \"" + prompt + "\", \"negative_prompt\": \"" +
                negativePrompt + "\", \"steps\":40 }";

        System.out.println("Creating image for " + chatbot.getFirstName() + " " +
                chatbot.getLastName() + " " + chatbot.getAge() + " (" + chatbot.getEthnicity() + ")" +
                " " + chatbot.getGender());

        // Make a POST request to the Stable diffusion URL
        HttpRequest httpRequest = this.stableDiffusionRequestBuilder.POST(
                HttpRequest.BodyPublishers.ofString(jsonString)
        ).build();
        HttpResponse<String> response;
        try {
            response = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Save the generated image in the resources folder
        record ImageResponse(List<String> images) {
        }

        Gson gson = new Gson();
        ImageResponse imageResponse = gson.fromJson(response.body(), ImageResponse.class);
        if (imageResponse.images() != null && !imageResponse.images().isEmpty()) {
            String base64Image = imageResponse.images().get(0);

            // Decode Base64 to binary
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            String directoryPath = "src/main/resources/static/images/";
            String filePath = directoryPath + chatbot.getImageUrl();
            Path directory = Paths.get(directoryPath);
            if (!Files.exists(directory)) {
                try {
                    Files.createDirectories(directory);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            // Save the image to a file
            try (FileOutputStream imageOutFile = new FileOutputStream(filePath)) {
                imageOutFile.write(imageBytes);
            } catch (IOException e) {
                return null;
            }
        }

        return chatbot;
    }


    @Bean
    @Description("Save the Chatbot profile information")
    public Function<Chatbot, Boolean> saveChatbotInfo() {
        return (Chatbot chatbot) -> {
            System.out.println("Saving chatbot profile info from the OpenAI response call function");
            System.out.println(chatbot);
            this.generatedChatbots.add(chatbot);
            return true;
        };
    }

    @Override
    public void saveChatbotInfoToDB() {
        Gson gson = new Gson();
        try {
            List<Chatbot> existingChatbots = gson.fromJson(
                    new FileReader(PROFILES_FILE_PATH),
                    new TypeToken<ArrayList<Chatbot>>() {
                    }.getType()
            );
            chatbotRepository.deleteAll();
            chatbotRepository.saveAll(existingChatbots);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Chatbot userChatbot = new Chatbot(
                userInfoProperties.get("id"),
                userInfoProperties.get("firstName"),
                userInfoProperties.get("lastName"),
                Integer.parseInt(userInfoProperties.get("age")),
                userInfoProperties.get("ethnicity"),
                userInfoProperties.get("gender"),
                userInfoProperties.get("bio"),
                userInfoProperties.get("imageUrl"),
                userInfoProperties.get("personality")
        );
        System.out.println("User Info Properties: " + userInfoProperties);
        chatbotRepository.save(userChatbot);
        System.out.println("Saved user info to the database, the test ends here.");
    }

    @Override
    public Chatbot getRandomChatbot() {
        return chatbotRepository.getRandomChatbot();
    }

}
