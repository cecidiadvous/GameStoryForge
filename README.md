# GameStoryForge

GameStoryForge is a system that generates game storylines using the OpenAI API. Users can register and log in to create and manage games, characters, and chapters, and generate complete storylines and dialogues for their games.

## Table of Contents
- [Libraries Used](#libraries-used)
  - [Backend (Java/Spring Boot)](#backend-javaspring-boot)
  - [Frontend (Vue.js)](#frontend-vuejs)
- [Working Functionalities](#working-functionalities)
  - [User Management](#user-management)
  - [Dashboard](#dashboard)
  - [Workshop Interface](#workshop-interface)
- [Quick Start Guide](#quick-start-guide)
  - [Prerequisites](#prerequisites)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
  - [Accessing the Application](#accessing-the-application)

## Libraries Used

### Backend (Java/Spring Boot)
- Spring Boot Starter Web (`org.springframework.boot:spring-boot-starter-web`): 3.3.2
- Spring AI OpenAI Spring Boot Starter (`org.springframework.ai:spring-ai-openai-spring-boot-starter`): 1.0.0-M1
- Spring Boot DevTools (`org.springframework.boot:spring-boot-devtools`): 3.3.2
- Spring Boot Configuration Processor (`org.springframework.boot:spring-boot-configuration-processor`): 3.3.2
- Lombok (`org.projectlombok:lombok`): Latest
- Spring Boot Starter Test (`org.springframework.boot:spring-boot-starter-test`): 3.3.2
- Jackson Databind (`com.fasterxml.jackson.core:jackson-databind`): 2.15.0
- Spring Boot Starter Data JPA (`org.springframework.boot:spring-boot-starter-data-jpa`): 3.3.2
- Jackson Module JSON Schema (`com.fasterxml.jackson.module:jackson-module-jsonSchema`): 2.12.3
- PostgreSQL JDBC Driver (`org.postgresql:postgresql`): Latest
- PDFCrowd API (`com.pdfcrowd:pdfcrowd`): 5.0.0, 6.2.1
- JUnit Jupiter API (`org.junit.jupiter:junit-jupiter-api`): 5.9.3
- JUnit Jupiter Engine (`org.junit.jupiter:junit-jupiter-engine`): 5.9.3
- Mockito Core (`org.mockito:mockito-core`): 5.0.0

### Frontend (Vue.js)
- axios: ^1.7.4
- vue: ^3.4.29
- vue-router: ^4.0.13
- vuedraggable: ^4.1.0
- @vitejs/plugin-vue: ^5.0.5 (Dev Dependency)
- vite: ^5.3.1 (Dev Dependency)

## Working Functionalities

### User Management
- Registration and Login: Users can create a new account and log in to the system.

### Dashboard
- Game Selection: Choose from existing games or create a new one.
- Game Creation:
  - Input game name and description.
  - Optionally upload a game cover image.
- Game Modification: Modify existing games using the same logic as creation.
- Game Deletion: Delete existing games.

### Workshop Interface

#### Character Management:
- Create Character:
  - Input character name, ability, role, and background.
  - Optionally upload a character avatar.
  - Characters are bound to the specific game.
- Modify Character: Edit existing characters using the same logic as creation.
- Delete Character: Remove characters from the game.
- Character Selection: Select existing characters for use in chapters.

#### Chapter Management:
- Create Chapter:
  - Select characters created for the game.
  - Input the general storyline.
- Generate Content:
  - Send data to the OpenAI API.
  - Receive a complete storyline and dialogue.
- Language Switching: Toggle between different languages for storyline and dialogue.
- Download PDF: Export the storyline and dialogue as a PDF file.

#### Instructions:
- View Instructions: Access guidance on how to use the workshop interface.

## Quick Start Guide

### Prerequisites
- Java 17 or higher
- Node.js (compatible version)
- Maven (for backend build)
- PostgreSQL (database)
- OpenAI API Key (for story generation)

### Backend Setup

1. Clone the Repository
   ```bash
   git clone https://github.sydney.edu.au/2024ELEC5619/Tue-05-08-Lab-Group-10.git
   cd GameStoryForge
    ```
2. Set Up PostgreSQL Database
    - Install PostgreSQL if not already installed.
    - Create a new database for the application.
3. Configure Application Properties
    - Navigate to `src/main/resources/application.properties`.
    - Update the database connection settings:
      ```properties
      spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
      spring.datasource.username=your_database_username
      spring.datasource.password=your_database_password
      ```
    - Add your OpenAI API key:
      ```properties
      openai.api.key=your_openai_api_key
      ```
4. Build and Run the Backend
      ```bash
      mvn clean install
      mvn spring-boot:run
      ```
    The backend server will start on `http://localhost:8080`.
### Frontend Setup
1. Navigate to Frontend Directory
    ```bash
    cd frontend
    ```
2. Install Dependencies
    ```bash
    npm install
    ```
3. Run the Frontend Application
    ```bash
    npm run dev
    ```
    The frontend will be accessible at `http://localhost:5173`.
### Accessing the Application
- Open your web browser and navigate to `http://localhost:5173`.
- Register a new account or log in with existing credentials.
- Start creating games, characters, and chapters.

## Code Coverage Testing with Maven and JaCoCo
 
### Running Tests and Generating Coverage Report:
To measure the code coverage of your tests, JaCoCo can be used as a Maven plugin.

To run the tests and generate the code coverage report, execute the following command:

```bash
mvn clean test jacoco:report
```

### Viewing the Report:
After running the command, the coverage report will be generated in the `target/site/jacoco` directory. Open the `index.html` file in your browser to view the coverage details.

   
