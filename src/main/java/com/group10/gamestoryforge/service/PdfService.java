package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.ChapterRepository;
import com.pdfcrowd.Pdfcrowd;
import com.group10.gamestoryforge.dao.GameRepository;
import com.group10.gamestoryforge.model.Chapter;
import com.group10.gamestoryforge.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class PdfService {

    // 定义 API 用户名和 API 密钥
    private static final String API_USERNAME = "suoyi"; // 替换为你的用户名
    private static final String API_KEY = "fdeff2703fe7b1cfbfc3c8aa9a03af70"; // 替换为你的 API 密钥

    @Autowired
    private GameRepository gameRepository; // 注入 GameRepository 来获取游戏数据

    @Autowired
    private ChapterRepository chapterRepository;

    public File generatePdfFromGameID(Long gameId) throws IOException, Pdfcrowd.Error {
        // 根据 gameId 获取游戏和相关章节数据
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId));

        // 使用 Game 实体查找相关章节
        List<Chapter> chapters = chapterRepository.findByGame(game);


        // 构建 HTML 内容
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body>");
        htmlContent.append("<h1>Game: ").append(game.getName()).append("</h1>");

        // 添加所有章节内容
        for (Chapter chapter : chapters) {
            htmlContent.append("<h2>Chapter: ").append(chapter.getName()).append("</h2>");
            htmlContent.append("<p>").append(chapter.getSystemText()).append("</p>");
        }
        htmlContent.append("</body></html>");

        // 创建 PDFCrowd API 客户端实例
        Pdfcrowd.HtmlToPdfClient client = new Pdfcrowd.HtmlToPdfClient(API_USERNAME, API_KEY);

        // 如果该方法不存在，移除以下行：
        // client.setContentViewportWidth("balanced");

        // 生成文件到临时目录
        Path tempFile = Files.createTempFile("game_" + gameId + "_", ".pdf");
        try (FileOutputStream fos = new FileOutputStream(tempFile.toFile())) {
            // 将 HTML 转换为 PDF
            client.convertStringToStream(htmlContent.toString(), fos);
        } catch (Pdfcrowd.Error | IOException e) {
            // 如果有错误，删除临时文件
            Files.deleteIfExists(tempFile);
            throw e; // 抛出异常
        }

        // 返回生成的 PDF 文件
        return tempFile.toFile();
    }
}
