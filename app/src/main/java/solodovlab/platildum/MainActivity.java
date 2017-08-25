package solodovlab.platildum;

import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String html;
    Integer parse_complete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parse_complete = 0;
        final WebView webview = (WebView) findViewById(R.id.webView);
        webview.getSettings();
        webview.setBackgroundColor(Color.rgb(43,49,55));
        webview.setWebViewClient(new WebViewClient() {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String lol = url;

            Intent intent = new Intent(MainActivity.this, TrophActivity.class);
            intent.putExtra("url", lol);
            startActivity(intent);


           webview.stopLoading();
           return false;


        }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                EditText search = (EditText) findViewById(R.id.editText);
                search.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER) {
                            final

                            EditText search=(EditText)findViewById(R.id.editText);
                            String Name = search.getText().toString();
                            WebView webview = (WebView) findViewById(R.id.webView);
                            webview.getSettings().setJavaScriptEnabled(true);
                            WebSettings webSettings = webview.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            ParseMyPageTask mt = new ParseMyPageTask();
                            mt.execute();

                        }
                        return false;
                    }
                });

            }

        });

        new ParseMyPageTask().execute();
    }

    public class ParseMyPageTask extends AsyncTask<String, Void, String> {



        EditText search=(EditText)findViewById(R.id.editText);
        String Name = search.getText().toString();



        @Override
        protected String doInBackground(String... params) {
            Document doc = null;//Здесь хранится будет разобранный html документ
            Document doc2 = null;//Здесь хранится будет разобранный html документ

            try {
                doc = Jsoup.connect("https://www.stratege.ru/ajax_loader/site_search_ajax")
                        .data("ajax_mode", "site_search")
                        .data("queryfr", Name)
                        .data("category" , "1")
                        .data("page" , "0")
                        .post();



            }


            catch (IOException e) {
                //Если не получилось считать
                System.out.println("Вруби инет, пидор!");
            }





            Elements elements = doc.getElementsByTag("table");



            //Ищем в спарсеном картинки что бы удалить
            Elements imgs = doc.select("img");



            //Ищем в спарсеном описания что бы удалить
            Elements descr = doc.getElementsByClass("ss_search_bx_list_description");



            Elements imgshref = doc.getElementsByClass("ss_search_bx_list_img_box");


            Elements hrefs = doc.select("a");






            //Если всё считалось, что вытаскиваем из считанного html документа заголовок
            if (null!=elements&&!elements.isEmpty()) {

                Element element = elements.first();


                //Удаляем картинки и описания
                descr.remove();
                imgs.remove();
                imgshref.remove();






                //добавляем к ссылкам абсолютный адрес шоб ShouldOverride зарабтал
                for( Element urlElement : hrefs ) {
                    urlElement.attr("href", urlElement.absUrl("href"));
                }






                String css = "<style>" +
                        "body{background:#2b3137;}" +
                        "a{color:#fff;text-decoration:none;padding: 15px 0 15px 0;display:inline-block;border-bottom:1px solid #24292e;width: 100%;}" +
                        "span{color: #a5a5a5;\n" +
                        "    font-size: 12px;\n" +
                        "    background: #5c6771;\n" +
                        "    border-radius: 5px;\n" +
                        "    padding: 5px;}" +

                        "</style>";



                html = css + element.html();


                String fullpagesource = html.toString();
                String fullpage = Jsoup.parse(fullpagesource).select("html").get(0).toString();

                fullpage = fullpage.replace("<tr>" , "");
                fullpage = fullpage.replace("<td width=\"90px\" align=\"left\" valign=\"top\">" , "");
                fullpage = fullpage.replace("<td width=\"820px\" align=\"left\" valign=\"top\">" , "");
                fullpage = fullpage.replace("<div class=\"ss_search_bx_list_text\">" , "");
                fullpage = fullpage.replace("<div class=\"ss_search_bx_list_title\">" , "");
                fullpage = fullpage.replace("<div class=\"ss_search_bx_list_line\">" , "");
                fullpage = fullpage.replace("<span>" , "");
                fullpage = fullpage.replace("</span>" , "");
                fullpage = fullpage.replace("</div>" , "");
                fullpage = fullpage.replace("<br>" , "");
                fullpage = fullpage.replace("<span class=\"ss_search_bx_list_atitle\">" , "");
                fullpage = fullpage.replace("<div class=\"ss_search_bx_list_border_mini\">" , "");
                fullpage = fullpage.replace("<div class=\"ss_search_bx_list_border_maxi\">" , "");
                fullpage = fullpage.replace("(PS3)" , "<span>(ps3)</span>");
                fullpage = fullpage.replace("(PS4)" , "<span>(ps4)</span>");
                fullpage = fullpage.replace("(Vita)" , "<span>(vita)</span>");
                fullpage = fullpage.replace("(Wii U)" , "<span>(wii u)</span>");
                fullpage = fullpage.replace("(X360)" , "<span>(x360)</span>");
                fullpage = fullpage.replace("(XOne)" , "<span>(xone)</span>");
                fullpage = fullpage.replace("(Switch)" , "<span>(switch)</span>");
                fullpage = fullpage.replace("(WP)" , "<span>(wp)</span>");
                fullpage = fullpage.replace("(Win 10)" , "<span>(w10)</span>");
                fullpage = fullpage.replace("(iOS)" , "<span>(ios)</span>");
                fullpage = fullpage.replace("(Kindle Fire)" , "<span>(kf)</span>");
                fullpage = fullpage.replace("(Gear VR)" , "<span>(gvr)</span>");
                fullpage = fullpage.replace("(Android)" , "<span>(andr)</span>");




                html = fullpage;
                System.out.println(html);
            }

          return null;
        }

        @Override
        protected void onPostExecute(String result) {



            WebView webview = (WebView) findViewById(R.id.webView);
            webview.loadDataWithBaseURL(null, html,"text/html; charset=utf-8", "UTF-8", null);
            webview.setVisibility(View.VISIBLE);


        }
    }

}
