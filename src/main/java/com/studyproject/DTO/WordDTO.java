package com.studyproject.DTO;

import com.studyproject.domain.WordCount;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WordDTO {
    private String word;
    private String count;

    public WordCount toEntity() {
        return WordCount.builder()
                .word(word)
                .count(count)
                .build();
    }
}
