package com.sn.cykb;

import com.sn.cykb.entity.Chapters;
import com.sn.cykb.entity.Novels;
import com.sn.cykb.entity.UsersNovelsRelation;
import com.sn.cykb.repository.ChaptersRepository;
import com.sn.cykb.repository.NovelsRepository;
import com.sn.cykb.repository.UsersNovelsRelationRepository;
import com.sn.cykb.util.DateUtil;
import com.sn.cykb.util.HttpUtil;
import com.sn.cykb.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.print.Doc;
import java.util.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CykbServerApplicationTests {

    @Autowired
    private NovelsRepository novelsRepository;
    @Autowired
    private ChaptersRepository chaptersRepository;
    @Autowired
    private UsersNovelsRelationRepository usersNovelsRelationRepository;

    /**
     * 获取笔趣阁书籍目录
     */
    @Test
    public void theftBixuge() {
        try {
            Document html;
            String source = "http://www.xbiquge.la/xiaoshuodaquan/";
            html = HttpUtil.getHtmlFromUrl(source, true);
            Element mainElement = html.getElementById("main");
            for (int i = 0, iLen = mainElement.getElementsByClass("novellist").size(); i < 4; i++) {
                Element ulElement = mainElement.getElementsByClass("novellist").get(i).getElementsByTag("ul").get(0);
                for (int j = 0, jLen = ulElement.getElementsByTag("a").size(); j < 6; j++) {
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
                    for (int k = 0, kLen = dlElement.getElementsByTag("dd").size(); k < 20; k++) {
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

    @Test
    public void insertRelation() {
        List<Novels> novelsList = novelsRepository.findAll();
        String uniqueId = "oP0Dk5Fqq656S4Gfm8eIY3rIXIlE";
        List<UsersNovelsRelation> target = new ArrayList<>();
        for (int i = 0; i < novelsList.size(); i++) {
            Novels item = novelsList.get(i);
            if (i < 45) {
                UsersNovelsRelation relation = UsersNovelsRelation.builder().uniqueId(uniqueId).novelsId(item.getId()).updateTime(new Date()).build();
                target.add(relation);
            }
        }
        usersNovelsRelationRepository.saveAll(target);
    }

    @Test
    public void insertCreateTime() {
        List<Novels> novelsList = novelsRepository.findAll();
        for (Novels item : novelsList) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Date date = new Date();
            long ts = date.getTime();
            novelsRepository.updateCreateTimeNative(ts, item.getId());
        }
    }

    @Test
    public void testInsertChapter() {
        String novelsId = "402880ea70d712230170d7c35a100154";
        Chapters chapters;
        String bookUrl = "http://www.xbiquge.la/8/8345/";
        Document childDoc = HttpUtil.getHtmlFromUrl(bookUrl, true);
        Element dlElement = childDoc.getElementById("list").getElementsByTag("dl").get(0);
        int i = 0;
        for (Element ddElement : dlElement.getElementsByTag("dd")) {
            Element a = ddElement.getElementsByTag("a").get(0);
            String chapter = a.html();
            String content = "暂无数据";
            if (i < 10) {
                String chapterUrl = "http://www.xbiquge.la/" + a.attr("href");
                Document contentDoc = HttpUtil.getHtmlFromUrl(chapterUrl, true);
                content = contentDoc.getElementById("content").html();
            }
            chapters = Chapters.builder().chapter(chapter).content(content).novelsId(novelsId).updateTime(new Date()).build();
            chaptersRepository.save(chapters);
            i++;
        }
    }

    @Test
    public void theft147xs() {
        String prefixUrl = "http://www.147xiaoshuo.com/sort/";
        List<Integer> suffixList = Arrays.asList(1, 2, 3, 4, 6, 7, 10, 11, 8, 12, 9, 5);
        for (int i = 0, iLen = suffixList.size(); i < 1; i++) {
            try {
                String fullUrl = prefixUrl + suffixList.get(i) + "/";
                Document novelsDoc = HttpUtil.getHtmlFromUrl(fullUrl, true);
                Elements liElements = novelsDoc.getElementById("main").getElementsByClass("novellist").get(0).getElementsByTag("ul").get(0).getElementsByTag("li");
                for (int j = 0, jLen = liElements.size(); j < 2; j++) {
                    try {
                        String AContent = liElements.get(j).getElementsByTag("a").get(0).attr("href");
                        String contentUrl = "http://www.147xiaoshuo.com/" + AContent;
                        List<Novels> jNovels = novelsRepository.findBySourceUrl(contentUrl);
                        if (jNovels != null && jNovels.size() > 0) {
                            continue;
                        }
                        Novels novels;
                        Document contentDoc = HttpUtil.getHtmlFromUrl(contentUrl, true);
                        String coverUrl = contentDoc.getElementById("fmimg").getElementsByTag("img").get(0).attr("src");
                        String introduction = contentDoc.getElementById("intro").html();
                        String author = contentDoc.getElementById("info").getElementsByTag("p").get(0).html().split("：")[1];
                        String latestChapter = contentDoc.getElementById("info").getElementsByTag("p").get(3).getElementsByTag("a").get(0).html();
                        Thread.sleep(1);
                        Long createTime = DateUtil.dateToLong(new Date());
                        String title = contentDoc.getElementById("info").getElementsByTag("h1").get(0).html();
                        String category = contentDoc.getElementsByClass("con_top").get(0).getElementsByTag("a").get(1).html();
                        String strUpdateTime = contentDoc.getElementById("info").getElementsByTag("p").get(2).html().split("：")[1];
                        Date updateTime = DateUtil.strToDate(strUpdateTime, "yyyy-MM-dd HH:mm:ss");
                        novels = Novels.builder().title(title).author(author).sourceUrl(contentUrl).sourceName("147小说").category(category).createTime(createTime).coverUrl(coverUrl).introduction(introduction).latestChapter(latestChapter).updateTime(updateTime).build();
                        novels = novelsRepository.save(novels);
                        String novelsId = novels.getId();
                        Chapters chapters;
                        Elements ddElements = contentDoc.getElementById("list").getElementsByTag("dd");
                        for (int k = 0, kLen = ddElements.size(); k < 1; k++) {
                            try {
                                Element chapterElement = ddElements.get(k).getElementsByTag("a").get(0);
                                String chapter = chapterElement.html();
                                List<Chapters> kChapters = chaptersRepository.findByChapterAndNovelsId(chapter, novelsId);
                                if (kChapters != null && kChapters.size() > 0) {
                                    continue;
                                }
                                String chapterUrl = "http://www.147xiaoshuo.com/" + chapterElement.attr("href");
                                Document chapterDoc = HttpUtil.getHtmlFromUrl(chapterUrl, true);
                                String content = chapterDoc.getElementById("content").html();
                                Date chapterUpTime = DateUtil.intervalTime(strUpdateTime, kLen - k - 1);
                                chapters = Chapters.builder().chapter(chapter).content(content).novelsId(novelsId).updateTime(chapterUpTime).build();
                                chaptersRepository.save(chapters);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
