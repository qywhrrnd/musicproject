package com.studyproject.scheduler;

import com.studyproject.DTO.WordDTO;
import com.studyproject.domain.Music;
import com.studyproject.domain.WordCount;
import com.studyproject.music.MusicRepository;
import com.studyproject.wordcloud.WordCountRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class WordCountScheduler {

    private final WordCountRepository wordCountRepository;
    private final MusicRepository musicRepository;

    @Scheduled(cron = "0 0/3 10 * * *")
    public void insertWordCountInfo() {
        List<Music> musicList = musicRepository.findBySearchDateAndRank(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // 코모란 시작
        // 분석 결과값이 나온 단어 리스트
        List<String> komoranList = new ArrayList<String>();

        // 코모란 분석기 돌리고 나온 단어 리스트 를 카운팅 한 리스트
        List<String> kWordList = new ArrayList<String>();
        List<Integer> kCntList = new ArrayList<Integer>();

        Iterator<Music> InList = musicList.iterator();

        int t=0;
        if (musicList.size() > 50) {
            while (t < 50) {
                log.info("문장 분석중입니다.");
                Music pDTO = new Music();
                pDTO = InList.next();
                Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
                if (pDTO.getLyric() != null) {
                    KomoranResult analyKomoranResult = komoran.analyze(pDTO.getLyric());
                    komoranList.addAll(analyKomoranResult.getNouns());
                    komoran = null;
                    analyKomoranResult = null;
                    pDTO = null;
                }
                t++;
            }
        } else {
            while (InList.hasNext()) {
                log.info("문장 분석중입니다.");
                Music pDTO = new Music();
                pDTO = InList.next();
                Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
                if (pDTO.getLyric() != null) {
                    KomoranResult analyKomoranResult = komoran.analyze(pDTO.getLyric());
                    komoranList.addAll(analyKomoranResult.getNouns());
                    komoran = null;
                    analyKomoranResult = null;
                    pDTO = null;
                }
            }
        }
        for (int i = 0; i < komoranList.size(); i++) {
            if (komoranList.get(i).length() > 1) {
                if (!kWordList.contains(komoranList.get(i))) {
                    kWordList.add(komoranList.get(i));
                    int tmp = 0;
                    for (int j = 0; j < komoranList.size(); j++) {
                        if (komoranList.get(i).equals(komoranList.get(j)))
                            tmp++;
                    }
                    kCntList.add(tmp);
                }
            }
        }

        List<WordDTO> pList = new ArrayList<WordDTO>();
        for (int i = 0; i < kWordList.size(); i++) {
            WordDTO pDTO = new WordDTO();
            pDTO.setWord(kWordList.get(i));
            pDTO.setCount(Integer.toString(kCntList.get(i)));
            log.info(pDTO.getCount());
            log.info(pDTO.getWord());
            WordCount wordCount = pDTO.toEntity();
            wordCountRepository.save(wordCount);
            pList.add(pDTO);
            pDTO = null;
        }
        kWordList = null;
        kCntList = null;

        //코모란 끝
    }
}
