package solodovlab.platildum;

import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class TrophActivity extends AppCompatActivity {
    String html;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troph);

        WebView webview = (WebView) findViewById(R.id.webview);
        webview.getSettings();
        webview.setBackgroundColor(Color.rgb(43,49,55));

        //webview.loadUrl(url+"/trophies");
        new ParseMyPageTask().execute();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    public class ParseMyPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Document doc = null;//Здесь хранится будет разобранный html документ
            Document doc1 = null;//Здесь хранится будет разобранный html документ
            try {
                Intent intent = getIntent();
                String url = intent.getStringExtra("url");
                doc1 = Jsoup.connect(url+"/trophies").post();
                String fullpagesource = doc1.toString();
                //System.out.println(fullpagesource);
                String fullpage = Jsoup.parse(fullpagesource).select("script").get(21).toString();

                fullpage = fullpage.replace("<script type=\"text/javascript\">" , "");
                fullpage = fullpage.replace("<!--//--><![CDATA[//><!--" , "");
                fullpage = fullpage.replace("var tl_global_bx_settings = {" , "");
                fullpage = fullpage.replace("settings: '" , "");
                fullpage = fullpage.replace("'," , "");
                fullpage = fullpage.trim();
                //Извлекаю первую строчку с data Settings
                String[] bodyLines = fullpage.split("\n");
                String firstLine = bodyLines[0];








              doc = Jsoup.connect("https://www.stratege.ru/ajax_loader/trophies_sp_list?ajax=1")
                      .data("ajax_mode", "trophies_list_loader")
                      .data("settings", firstLine)
                      .post();






            } catch (IOException e) {
                //Если не получилось считать
                System.out.println("Вруби инет, пидор!");
            }

            String css = "<style>" +
                    "body{background:#2b3137;}" +
            ".tlhsltpl_helps_header_helps{display:none;}" +
            ".tltstpl_mini_box_table{width:100%;}" +
        	".tlhsltpl_helps_header_helps{display:none;}" +
        	".tltstpl_mini_box_table{width:100%;}" +
        	".tltstpl_tt_trops_title_box a {color: #ff2186;}" +
        	".tlhsltpl_helps_counter_2 {display: none;}" +
            "img.tltstpl_tt_logo_img {border-radius: 5px;" +
                "border: 6px solid #24292e;" +
"           }" +
"	.tltstpl_tt_trops_add_info {" +
"               display: none;" +
"           }" +
"	.tltstpl_tt_helps_onload {" +
"               display: none;" +
"           }" +
"	.tltstpl_tt_trops_description_box {" +
"               background: #1c1f23;" +
"               color: #6a6a6a;" +
"               padding: 20px;" +
"           }" +
"	.tltstpl_tt_trops_title a {" +
"               text-decoration: none;" +
"               color: #ffffff;" +
"           }    " +
" .tltstpl_tt_logo_box {text-align: center;}" +
"	.tltstpl_tt_trops_title_box {text-align: center;}" +
"	.tltstpl_tt_trops_title a {" +
"                    display: inline-block;" +
           " background-color: #24292e;" +
           " padding-top: 15px;" +
           " padding-bottom: 15px;" +
           " margin-top: 15px;" +
           " width: 100%;" +
"            }" +
                    ".tltstpl_tt_trops_partners_count{display:none;}" +
 "                  .sekr {" +
 "              width: 100%;" +
 "              display: inline-block;" +
 "              background: #24292e;" +
 "              padding: 0 0 17px;" +
 "          }" +
 "      .sekr span {" +
 "              color: #a5a5a5;" +
 "              background: #5c6771;" +
 "              border-radius: 5px;" +
 "              padding: 5px;" +
 "          }" +
                    "</style>";





            html = css + doc.html();


            String fullpagesource2 = html.toString();
            String fullpage2 = Jsoup.parse(fullpagesource2).select("html").get(0).toString();
            fullpage2 = fullpage2.replace("<td", "<div class='td'");
            fullpage2 = fullpage2.replace("<tr", "<div class='tr'");
            fullpage2 = fullpage2.replace("</td>", "</div>");
            fullpage2 = fullpage2.replace("</tr>" , "</div>");
            fullpage2 = fullpage2.replace("(Секретный)" , "<span class=\"sekr\"><span>Секретный</span></span>");


            html = fullpage2;
            System.out.println(html);


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(ProgressBar.INVISIBLE);

           WebView webview = (WebView) findViewById(R.id.webview);

           webview.loadDataWithBaseURL(null, html,"text/html; charset=utf-8", "UTF-8", null);
        }


    }

    }




