package com.studyproject.wordcloud;

import com.studyproject.domain.WordCount;
import com.studyproject.music.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class WordCountController {

    private final MusicService musicService;
    private final WordCountRepository wordCountRepository;

    @GetMapping("/music/wordcount")
    public String musicWordCount(Model model) {
        List<WordCount> wordCountList = wordCountRepository.findAll();
        model.addAttribute("wordCountList", wordCountList);
        return "wordcount/main";
    }
}
