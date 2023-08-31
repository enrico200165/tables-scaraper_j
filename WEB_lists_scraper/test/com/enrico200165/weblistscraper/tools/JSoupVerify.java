package com.enrico200165.weblistscraper.tools;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * Created by enrico on 2/19/2016.
 */
public class JSoupVerify {


   String html = "<tbody> <tr bgcolor=\"444488\"> <td><font class=\"old_text_2\" color=\"ffffff\"><b> Jordan (20) </b>" +
           "</font></td> <td align=\"right\" valign=\"middle\"><font class=\"old_text_2\">" +
           " <img src=\"http:/*www.japan-guide.com/g5/gender_m.gif\" width=\"15\" height=\"16\"> " +
           "</font></td>*/ </tr><tr><td bgcolor=\"dddddd\" colspan=\"2\"><table width=\"100%\" style=\"table-layout:" +
           " fixed; word-wrap: break-word; overflow-wrap: break-word;\" border=\"0\" cellpadding=\"3\" cellspacing=\"0\">" +
           "<tbody><tr><td><font class=\"old_text_2\"><table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">" +
           "<tbody><tr><td valign=\"top\" width=\"100\"><font class=\"old_text_2\"> <b>Interests:</b> </font></td><td valign=\"top\">" +
           "<font class=\"old_text_2\"> Anime, Music, Internet </font></td><td rowspan=\"3\" align=\"right\" valign=\"top\">" +
           "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr><td align=\"right\"><font class=\"old_text_1\">" +
           " <img src=\"http://www.japan-guide.com/penfriend/flags/fr.gif\"><br><a href=\"http://www.japan-guide.com/local/fr/145/\" target=\"_top\">" +
           "<font color=\"000000\">Aquitaine</font></a><br><a href=\"http://www.japan-guide.com/local/fr/\" target=\"_top\"><font color=\"000000\">" +
           "France</font></a></font></td></tr></tbody></table> </td></tr><tr><td valign=\"top\" width=\"100\"><font class=\"old_text_2\"> " +
           "<b>Nationality:</b> </font></td><td valign=\"top\"><font class=\"old_text_2\"> France </font></td></tr><tr><td valign=\"top\" width=\"100\">" +
           "<font class=\"old_text_2\"> <b>Languages:</b> </font></td><td valign=\"top\"><font class=\"old_text_2\"> French &lt;-&gt; Japanese </font>" +
           "</td></tr></tbody></table> </font><p><font class=\"old_text_2\">はじめまして！みんなさん！<br>私はジョルダンです。二十歳です。<br>フランスのボルド" +
           "ーに住んでいます、ボルドーの大学で日本語を勉強しています。<br>私の趣味は音楽を聞いたり、アニメを見たりすることです。<br>私の日本語のレベルが強くなりたいです！<br>" +
           "日本語を増加するために日本人と話したいです！<br>将来、日本でフランス語先生になりたいです！<br>Line と Skype を利用します。<br>よろしくお願いします。 </font>" +
           "</p></td></tr></tbody></table></td></tr><tr><td bgcolor=\"bbbbbb\"><font class=\"old_text_2\"> 2016/2/20 </font></td>" +
           "<td bgcolor=\"bbbbbb\" align=\"right\"><font class=\"old_text_2\"> <a href=\"http://www.japan-guide.com/m/contact.html?997437+893581\" " +
           "target=\"_blank\" rel=\"nofollow\">" +
           "<font color=\"000000\">contact this person</font></a> </font></td></tr></tbody>";


    Document doc = Jsoup.parse(html);

    Elements rows = doc.select("tbody > tr");




    static Logger log=Logger.getLogger(JSoupVerify.class.getSimpleName());
} 