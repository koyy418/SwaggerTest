package me.koyunyoung.springbootdeveloper.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.koyunyoung.springbootdeveloper.domain.Article;
import me.koyunyoung.springbootdeveloper.dto.AddArticleRequest;
import me.koyunyoung.springbootdeveloper.dto.ArticleResponse;
import me.koyunyoung.springbootdeveloper.dto.UpdateArticleRequest;
import me.koyunyoung.springbootdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "블로그 API", description = "블로그 API들의 집합.")

public class BlogApiController {
    private final BlogService blogService;

    @Operation(summary = "알림 송신", description = "필요 파라미터 : 알림타입, 수신자ID, 송신자ID, 알림내용 || 알림타입이 1 또는 2라면 status=요청 대기, 3,4라면 status=처리 완료")
    @Parameter(name = "alarmType", description = "보내는 알림의 타입")
    @Parameter(name = "toId", description = "받는 유저의 아이디")
    @Parameter(name = "fromId", description = "보내는 유저의 아이디")
    @Parameter(name = "alarmMsg", description = "알림의 내용")
    @Parameter(name = "alarmStatus", description = "알림의 처리상태, Required = false || 요청 시 보내지 않아도 자동 처리됨.")
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @Operation(summary = "물건", description = "스웨거 연습중")
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();
        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogService.delete(id);
        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id,
                                                 @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);
        return ResponseEntity.ok()
                .body(updatedArticle);
    }
}