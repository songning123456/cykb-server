package com.sn.cykb.util;

import com.sn.cykb.entity.Chapters;
import com.sn.cykb.entity.Novels;
import com.sn.cykb.repository.ChaptersRepository;
import com.sn.cykb.repository.NovelsRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author: songning
 * @date: 2020/3/29 12:11
 */
@Component
@Slf4j
public class TheftUtil {

    @Autowired
    private NovelsRepository novelsRepository;

    @Autowired
    private ChaptersRepository chaptersRepository;


    public void testTheft() {
        log.info("测试 theft");
    }

    public void theftBiquge() {
        try {
            Document html;
            String source = "http://www.xbiquge.la/xiaoshuodaquan/";
            html = HttpUtil.getHtmlFromUrl(source, true);
            Element mainElement = html.getElementById("main");
            for (int i = 0, iLen = mainElement.getElementsByClass("novellist").size(); i < iLen; i++) {
                Element ulElement = mainElement.getElementsByClass("novellist").get(i).getElementsByTag("ul").get(0);
                for (int j = 0, jLen = ulElement.getElementsByTag("a").size(); j < jLen; j++) {
                    Novels novels;
                    String bookUrl = ulElement.getElementsByTag("a").get(j).attr("href");
                    // 判断 是否已经存在，如果存在则跳过
                    List<Novels> jNovels = novelsRepository.findBySourceUrl(bookUrl);
                    if (jNovels != null && jNovels.size() > 0) {
                        continue;
                    }
                    Document childDoc;
                    try {
                        childDoc = HttpUtil.getHtmlFromUrl(bookUrl, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    Element maininfoElement = childDoc.getElementById("maininfo");
                    String coverUrl = childDoc.getElementById("sidebar").getElementsByTag("img").get(0).attr("src");
                    Element infoElement = maininfoElement.getElementById("info");
                    String introduction = maininfoElement.getElementById("intro").getElementsByTag("p").get(1).html();
                    String author = infoElement.getElementsByTag("p").get(0).html().split("：")[1];
                    String latestChapter = infoElement.getElementsByTag("p").get(3).getElementsByTag("a").get(0).html();
                    Thread.sleep(1);
                    Long createTime = DateUtil.dateToLong(new Date());
                    String title = infoElement.getElementsByTag("h1").html();
                    String category = childDoc.getElementsByClass("con_top").get(0).getElementsByTag("a").get(2).html();
                    String strUpdateTime = infoElement.getElementsByTag("p").get(2).html().split("：")[1];
                    Date updateTime = DateUtil.strToDate(strUpdateTime, "yyyy-MM-dd HH:mm:ss");
                    novels = Novels.builder().title(title).author(author).sourceUrl(bookUrl).sourceName("笔趣阁").category(category).createTime(createTime).coverUrl(coverUrl).introduction(introduction).latestChapter(latestChapter).updateTime(updateTime).build();
                    novels = novelsRepository.save(novels);
                    String novelsId = novels.getId();
                    Chapters chapters;
                    Element dlElement = childDoc.getElementById("list").getElementsByTag("dl").get(0);
                    for (int k = 0, kLen = dlElement.getElementsByTag("dd").size(); k < kLen; k++) {
                        Element a = dlElement.getElementsByTag("dd").get(k).getElementsByTag("a").get(0);
                        String chapter = a.html();
                        List<Chapters> kChapters = chaptersRepository.findByChapterAndNovelsId(chapter, novelsId);
                        if (kChapters != null && kChapters.size() > 0) {
                            continue;
                        }
                        String chapterUrl = "http://www.xbiquge.la/" + a.attr("href");
                        Document contentDoc = HttpUtil.getHtmlFromUrl(chapterUrl, true);
                        String content;
                        try {
                            content = contentDoc.getElementById("content").html();
                        } catch (Exception e) {
                            e.printStackTrace();
                            content = "加载出错了!";
                        }
                        Date chapterUpTime = DateUtil.intervalTime(strUpdateTime, kLen - k - 1);
                        chapters = Chapters.builder().chapter(chapter).content(content).novelsId(novelsId).updateTime(chapterUpTime).build();
                        chaptersRepository.save(chapters);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
