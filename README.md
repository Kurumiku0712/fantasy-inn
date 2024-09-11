# Fantasy Inn

Video Link: 

https://drive.google.com/file/d/15vCsdHe3SlKA0T6ZmvEg-KwqPapMK88P/view?usp=sharing



**Project Overview:**
The Fantasy Inn is a virtual world enthusiast's immersive booking system that provides AI-driven personalized services, enabling users to interact with virtual characters and enjoy an enhanced virtual world experience. This platform includes modules such as room booking, management, order search, and a cinema feature. It is developed using **Spring Boot** and **React** in a **decoupled front-end and back-end architecture**.



Backend:

[Kurumiku0712/fantasy-inn (github.com)](https://github.com/Kurumiku0712/fantasy-inn)



Frontend:

[Kurumiku0712/fantasy-inn-ui (github.com)](https://github.com/Kurumiku0712/fantasy-inn-ui)



## Key Features:

- **AI-Powered Chatbot:**
  - Integrated with **Spring AI** and **OpenAI API** to develop an interactive **ChatGPT-based** chatbot. This chatbot automatically generates random names, races, and personalities, enhancing user engagement and fun.

- **Stable Diffusion Avatar Creation:**
  - Using model files from **civitai** and deploying **Stable Diffusion**, the system generates stylized character portraits based on chatbot profiles to enhance the interactive experience.

- **Database Migration:**
  - The database was migrated from **MySQL** to **MongoDB** to improve the system’s **data processing efficiency**

- **Google reCAPTCHA Integration:**
  - **Google reCAPTCHA** is used to collect user behavior data and prevent automated attacks, enhancing the platform's **security**.

- **AWS S3 for Image Storage:**
  - **AWS S3** is used to store images in the cloud, ensuring **high availability** and fast access across the globe.

- **TMDB API Integration:**
  - Integrated with **TMDB API** to display popular works and provide a commenting system, enriching platform content and immersive experience.

## Tech Stack:

- **Backend**: 
  - Spring Boot
  - Spring Security
  - JWT
  - MongoDB
  - AWS S3
  - OpenAI API (ChatGPT)
  - Stable Diffusion (Avatar generation)
  - Google reCAPTCHA
  - TMDB API
  
- **Frontend**: 
  - React.js
  - Tailwind CSS

## Setup Guide:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/...

2. **Backend Setup**:

- Install **Java 21**.
- Navigate to the backend directory and build the Spring Boot application:

```
cd backend
mvn clean install
mvn spring-boot:run
```

3. **MongoDB Setup**:

- Ensure you have 

  MongoDB Cluster and MongoDB Compass

   installed and running locally or provide connection details in the application properties:

  ```
  spring.data.mongodb.uri=mongodb://localhost:/yourdb
  ```

4. **Frontend Setup**:

- Navigate to the frontend directory, install dependencies, and run the development server:

  ```
  npm install
  npm start
  ```

5. **Environment Variables**:

- Create a `.env` file in both the frontend and backend with your configuration details (e.g., **OpenAI API Key**, **AWS S3 credentials**, **MongoDB URI**, **TMDB API Key**).

  **Stable Diffusion Setup**:

  - Download Stable Diffusion models from **civitai** and set up the local server to generate avatars based on user input.

  **Google reCAPTCHA**:

  - Set up **Google reCAPTCHA** keys and integrate them into the frontend to prevent bot submissions.

  **AWS S3 Setup**:

  - Configure **AWS S3** for image storage and set up environment variables for access in both frontend and backend.

  **TMDB API Integration**:

  - Obtain your **TMDB API Key** and configure it in the environment file to enable movie search and display features.

## Additional Features:

- **Interactive Character Profiles**: Characters with randomized attributes interact with users, creating a personalized virtual experience.
- **Room Booking Management**: Users can book rooms and manage bookings seamlessly via an intuitive interface.
- **Movie Commenting System**: Users can comment on popular movies, adding a community interaction element to the platform.