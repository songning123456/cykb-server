package com.sn.cykb;

import com.sn.cykb.entity.Chapters;
import com.sn.cykb.entity.Novels;
import com.sn.cykb.repository.ChaptersRepository;
import com.sn.cykb.repository.NovelsRepository;
import com.sn.cykb.util.HttpUtil;
import com.sn.cykb.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CykbServerApplicationTests {

    @Autowired
    private NovelsRepository novelsRepository;
    @Autowired
    private ChaptersRepository chaptersRepository;

    @Test
    public void contextLoads() {
        System.out.println("TEST");
    }

    private static List<String> NOVEL_MALE = Arrays.asList(
            "xuanhuan", "qihuan", "wuxia", "xianxia", "dushi",
            "zhichang", "xianshi", "lishi", "junshi", "youxi",
            "tiyu", "kehuan", "xuanyi", "qingxiaoshuo", "tongren");

    private static List<String> NOVEL_FEMALE = Arrays.asList(
            "gudaiyanqing", "xiandaiyanqing", "xuanhuanyanqing", "xuanyituili", "langmanqingchun",
            "xianxiaqiyuan", "kehuankongjian", "youxijingji", "qingxiaoshuo", "xianshishenghuo");

    private static Map<String, List<String>> NOVEL_MAP;

    static {
        NOVEL_MAP = new HashMap<>(2);
        NOVEL_MAP.put("male", NOVEL_MALE);
        NOVEL_MAP.put("female", NOVEL_FEMALE);
    }

    /**
     * 获取笔趣阁书籍目录
     */
    @Test
    public void theftBixuge() {
        try {
            Document html;
            String url = "http://www.xbiquge.la/xiaoshuodaquan/";
            html = HttpUtil.getHtmlFromUrl(url, true);
            Element mainElement = html.getElementById("main");
            for (Element novellistElement : mainElement.getElementsByClass("novellist")) {
//                String category = novellist.getElementsByTag("h2").get(0).html();
                Element ulElement = novellistElement.getElementsByTag("ul").get(0);
                for (Element aElement : ulElement.getElementsByTag("a")) {
                    int sexRandom = Integer.parseInt(RandomUtil.getRandom(0, 1));
                    Novels novels = new Novels();
                    int categoryRandom = 0;
                    String category = "";
                    String sex = "";
                    if (sexRandom == 0) {
                        sex = "male";
                        categoryRandom = Integer.parseInt(RandomUtil.getRandom(0, 14));
                        category = NOVEL_MAP.get("male").get(categoryRandom);
                    } else {
                        sex = "female";
                        categoryRandom = Integer.parseInt(RandomUtil.getRandom(0, 9));
                        category = NOVEL_MAP.get("female").get(categoryRandom);
                    }
                    String title = aElement.html();
                    String bookUrl = aElement.attr("href");
                    Document childDoc = HttpUtil.getHtmlFromUrl(bookUrl, true);
                    Element maininfoElement = childDoc.getElementById("maininfo");
                    Element infoElement = maininfoElement.getElementById("info");
                    String introduction = maininfoElement.getElementById("intro").getElementsByTag("p").get(1).html();
                    String author = infoElement.getElementsByTag("p").get(0).html().split("：")[1];
                    String latestChapter = infoElement.getElementsByTag("p").get(3).getElementsByTag("a").get(0).html();
                    novels = Novels.builder().title(title).author(author).sex(sex).category(category).introduction(introduction).latestChapter(latestChapter).updateTime(new Date()).build();
                    novels = novelsRepository.save(novels);
                    String novelsId = novels.getId();
                    Chapters chapters;
                    Element dlElement = childDoc.getElementById("list").getElementsByTag("dl").get(0);
                    for (Element ddElement : dlElement.getElementsByTag("dd")) {
                        Element a = ddElement.getElementsByTag("a").get(0);
                        String chapter = a.html();
                        String chapterUrl = "http://www.xbiquge.la/" + a.attr("href");
                        Document contentDoc = HttpUtil.getHtmlFromUrl(chapterUrl, true);
                        String content = contentDoc.getElementById("content").html();
                        chapters = Chapters.builder().chapter(chapter).content(content).novelsId(novelsId).updateTime(new Date()).build();
                        chaptersRepository.save(chapters);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
