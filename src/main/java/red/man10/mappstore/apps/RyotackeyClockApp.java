package red.man10.mappstore.apps;

import org.bukkit.entity.Player;
import red.man10.mappstore.DynamicMapRenderer;
import red.man10.mappstore.MappApp;

import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/////////////////////////////////////////////////////////
//          mApp default template
//
//     https://github.com/takatronix/MappStore/
//     Please give me pull request your mApp!
/////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////
//             RyotackeyClockmAPP
//
//       It can swich from Analog Clock to Digital Clock
//       and change the color!
//
//              created by Ryotackey
//        https://github.com/Ryotackey
/////////////////////////////////////////////////////////


public class RyotackeyClockApp extends MappApp {


    ////////////////////////////////////////////
    //      App name (must be unique)
    //      アプリ名：ユニークな必要があります
    static String appName = "rtclock";

    ////////////////////////////////////////////
    //     Draw refresh Cycle:描画割り込み周期
    //     appTickCycle = 1 -> 1/20 sec
    static int  drawRefreshCycle = 20;


    ///////////////////////////////
    //     Data
    ///////////////////////////////
    static class MappData{
        //   Add your data here / マップごとに保存するデータはここに追加
        int color = 0;
        int kind = 0;

    }
    static HashMap<Integer,MappData> hashMap = new HashMap<>();

    //      ユーザーデーター保存
    static MappData  loadData(int mapId) {
        MappData data = hashMap.get(mapId);
        if(data == null){
            data = new MappData();
        }
        return data;
    }
    //      ユーザーデータ読み込み
    static MappData saveData(int mapId, MappData data){
        return hashMap.put(mapId,data);
    }

    ///////////////////////////////////////////////////////
    //    Call this function to register the your app
    //    アプリを登録するためにこの関数をコールしてください
    static public void register(){

        /////////////////////////////////////////////////
        //      Button (nearby map) clicked event
        //      ボタン押された時の処理
        DynamicMapRenderer.registerButtonEvent(appName, (String key, int mapId, Player player) -> {

            MappData m = loadData(mapId);

            ///////////////////////////////////////////////////////////////
            //      mapごとに別々のデータを表示したい場合は
            //      mapIdをキーにハッシュマップにデータを読み込み・保存してください
            /*
            //     load app data / mapIDをキーにをロードする　
            MappData data = loadData(mapId);

            //    save app data
            saveData(mapId,data);
            */

            //////////////////////////////////////////////
            //  Get Graphics context for drawing
            //  描画用コンテキスト取得

            Graphics2D g = DynamicMapRenderer.getGraphics(mapId);
            if(g == null){
                return false;
            }

            m.kind = (m.kind + 1) % 2;
            //    true -> call drawing logic / trueで描画ロジックがコールされます

            saveData(mapId, m);

            return true;
        });

        /////////////////////////////////////////////////
        //      Display touch event
        //      ディスプレイがタッチされた時の処理
        DynamicMapRenderer.registerDisplayTouchEvent(appName, (String key, int mapId, Player player, int x, int y) -> {

            MappData m = loadData(mapId);

            //////////////////////////////////////////////
            //  Get Graphics context for drawing
            //  描画用コンテキスト取得

            Graphics2D g = DynamicMapRenderer.getGraphics(mapId);
            if(g == null){
                return false;
            }

            m.color = (m.color + 1) % 7;

            //    true -> call drawing logic :描画更新

            saveData(mapId, m);

            return true;
        });


        DynamicMapRenderer.register( appName, drawRefreshCycle, (String key, int mapId,Graphics2D g) -> {

//  Clear screen
            //  g.setColor(Color.BLACK);
            //  g.fillRect(0,0,128,128);

            MappData m = loadData(mapId);

            if (m.kind == 0) {

                switch (m.color) {

                    case 0:
                        //  Clear screen

                        clockDraw(Color.WHITE, Color.BLACK, "ryotackey_clock", g);

                        break;

                    case 1:
                        //  Clear screen
                        clockDraw(Color.BLACK, Color.RED, "ryotackey_clock2", g);
                        break;

                    case 2:
                        //  Clear screen
                        clockDraw(Color.BLACK, Color.CYAN, "ryotackey_clock3", g);
                        break;

                    case 3:
                        //  Clear screen
                        clockDraw(Color.BLACK, Color.WHITE, "ryotackey_clock4", g);
                        break;

                    case 4:
                        //  Clear screen
                        clockDraw(Color.BLACK, Color.GREEN, "ryotackey_clock5", g);
                        break;

                    case 5:
                        //  Clear screen
                        clockDraw(Color.BLACK, Color.YELLOW, "ryotackey_clock6", g);
                        break;

                    case 6:
                        //  Clear screen
                        clockDraw(Color.BLACK, Color.MAGENTA, "ryotackey_clock7", g);
                        break;
                }
            }

            if (m.kind == 1) {

                switch (m.color) {

                    case 0:

                        digitalclockDraw(Color.ORANGE, g, "ryotackey_digital");
                        break;

                    case 1:

                        digitalclockDraw(Color.RED, g, "ryotackey_digital");
                        break;

                    case 2:

                        digitalclockDraw(Color.CYAN, g, "ryotackey_digital");
                        break;

                    case 3:

                        digitalclockDraw(Color.WHITE, g, "ryotackey_digital");
                        break;

                    case 4:

                        digitalclockDraw(Color.GREEN, g, "ryotackey_digital");
                        break;

                    case 5:

                        digitalclockDraw(Color.YELLOW, g, "ryotackey_digital");
                        break;

                    case 6:

                        digitalclockDraw(Color.MAGENTA, g, "ryotackey_digital");
                        break;

                }

            }

            //  true -> update map / trueでマップに画像が転送されます
            return true;
        });
    }

    public static void clockDraw(Color backcolor, Color maincolor, String path, Graphics2D g){

        String hour = formattedTimestamp(current(), "HH");
        String minutes = formattedTimestamp(current(), "mm");
        String seconds = formattedTimestamp(current(), "ss");

        int x1 = getX(50, seconds, 6);
        int y1 = getY(50, seconds, 6);

        int x2 = getX(50, minutes, 6);
        int y2 = getY(50, minutes, 6);

        int x3 = getX(28, hour, 30);
        int y3 = getY(28, hour, 30);

        g.setColor(backcolor);
        g.fillRect(0, 0, 128, 128);

        DynamicMapRenderer.drawImage(g, path, 0, 0, 128, 128);

        BasicStroke wideStroke;

        g.setColor(maincolor);
        g.drawLine(64, 64, x1, y1);
        wideStroke = new BasicStroke(3.0f);
        g.setStroke(wideStroke);
        g.drawLine(64, 64, x2, y2);
        wideStroke = new BasicStroke(5.0f);
        g.setStroke(wideStroke);
        g.drawLine(64, 64, x3, y3);

    }

    public static int getY(int range, String time, int scale){

        int coord = (int) (64 + range * Math.sin(Integer.valueOf(time) * scale * (Math.PI / 180) - Math.PI / 2));

        return coord;
    }

    public static int getX(int range, String time, int scale){

        int coord = (int) (64 + range * Math.cos(Integer.valueOf(time) * scale * (Math.PI / 180) - Math.PI / 2));

        return coord;
    }

    public static void digitalclockDraw(Color maincolor, Graphics2D g, String path){

        DynamicMapRenderer.drawImage(g, path, 0, 0, 128, 128);

        LocalDateTime now = LocalDateTime.now();
        String time = DateTimeFormatter.ofPattern("HH:mm").format(now);
        String second = DateTimeFormatter.ofPattern("ss").format(now);

        g.setColor(Color.BLACK);
        g.setFont(new Font( "SansSerif", Font.BOLD,36));
        g.drawString(time, 7, 72);

        g.setFont(new Font( "SansSerif", Font.BOLD,20));
        g.drawString(second, 104, 72);

        g.setColor(maincolor);
        g.setFont(new Font( "SansSerif", Font.BOLD,36));
        g.drawString(time, 5, 70);

        g.setFont(new Font( "SansSerif", Font.BOLD,20));
        g.drawString(second, 102, 70);

    }

    public static String formattedTimestamp(Timestamp timestamp, String timeFormat) {
        return new SimpleDateFormat(timeFormat).format(timestamp);
    }

    public static Timestamp current() {
        return new Timestamp(System.currentTimeMillis());
    }

}

