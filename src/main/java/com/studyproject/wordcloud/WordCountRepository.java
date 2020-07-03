package com.studyproject.wordcloud;

import com.studyproject.domain.WordCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordCountRepository extends JpaRepository<WordCount, Long> {


}
