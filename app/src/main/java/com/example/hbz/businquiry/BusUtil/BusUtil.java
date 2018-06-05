package com.example.hbz.businquiry.BusUtil;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static com.example.hbz.businquiry.BusUtil.Constant.APPKEY;
import static com.example.hbz.businquiry.BusUtil.Constant.CITY_URL;
import static com.example.hbz.businquiry.BusUtil.Constant.LINE_URL;
import static com.example.hbz.businquiry.BusUtil.Constant.NEARB_YURL;
import static com.example.hbz.businquiry.BusUtil.Constant.STATION2S_URL;
import static com.example.hbz.businquiry.BusUtil.Constant.STATIO_NURL;

/**
 * @ClassName: com.example.hbz.businquiry
 * @Description: 公交查询工具类
 * @Author: HBZ
 * @Date: 2018/5/10 20:30
 */

public class BusUtil {

    private BusUtil() {
    }

    public enum Type {
        LINE, // 线路查询
        STATION2S, // 换乘查询
        STATION, // 站点查询
        NEARBY, // 附近站点查询
        CITY // 城市查询
    }

    /**
     * 查询接口
     *
     * @param type     查询的类型
     * @param listener 结果监听器
     * @param string   查询参数
     */
    public static void requestData(Type type, ResultListener listener, String... string) {
        String path = null;
        switch (type) {
            case LINE:
                path = line(string);
                break;
            case STATION2S:
                path = station2s(string);
                break;
            case STATION:
                path = station(string);
                break;
            case NEARBY:
                path = nearby(string);
                break;
            case CITY:
                path = city(string);
                break;
            default:
                break;
        }
        if (path != null && !path.equals("")) {
            requestAsync(path, listener);
        }
    }

    private static String line(String[] string) {
        if (string.length < 3) {
            return null;
        }
        String city = string[0]; // string	否	城市 cityid city任选其一
        String cityid = string[1]; // int	否	城市ID
        String transitno = string[2];  // string	是	车次
        String path;

        if (city == null || city.equals("")) {
            path = LINE_URL + "?cityid=" + cityid + "&transitno=" + transitno + "&appkey=" + APPKEY;
        } else {
            path = LINE_URL + "?city=" + city + "&transitno=" + transitno + "&appkey=" + APPKEY;
        }
        return path;
    }

    private static String station2s(String[] string) {
        if (string.length < 4) {
            return null;
        }
        String start = string[0]; //	string	是	起点
        String end = string[1]; //	string	是	终点
        String city = string[2]; //	string	是	城市
        String type = string[3]; //	string	是	换乘类型 car自驾 walk步行 transit公交 默认

        return STATION2S_URL + "?city=" + city + "&start=" + start + "&end=" + end + "&type=" + type + "&appkey=" + APPKEY;
    }

    private static String station(String[] string) {
        String city = string[0]; // string	否	城市 cityid city任选其一
        String cityid = string[1]; // int	否	城市ID
        String station = string[2];  // string	是	站点名称

        String path;
        if (city == null || city.equals("")) {
            path = STATIO_NURL + "?cityid=" + cityid + "&station=" + station + "&appkey=" + APPKEY;
        } else {
            path = STATIO_NURL + "?city=" + city + "&station=" + station + "&appkey=" + APPKEY;
        }

        return path;
    }

    private static String nearby(String[] string) {
        if (string.length < 2) {
            return null;
        }
        String city = string[0]; //	string	是	城市
        String address = string[1]; //	string	是	地址

        return NEARB_YURL + "?city=" + city + "&address=" + address + "&appkey=" + APPKEY;
    }

    private static String city(String[] string) {
        return CITY_URL  + "?appkey=" + APPKEY;
    }

    /**
     * 异步线路查询
     *
     * @param path           查询地址
     * @param resultListener 查询结果监听
     */
    private static void requestAsync(String path, ResultListener resultListener) {
        new LineAsyncTask(resultListener).execute(path);
    }

    /**
     * 同步查询
     *
     * @param path 查询地址
     * @return 查询结果
     */
    static String request(String path) {
        if (path == null || path.equals("")) {
            return null;
        }

        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                StringBuffer sb = new StringBuffer();

                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str).append("\n");
                }
                return sb.toString();
            }
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 异步查询类
     */
    private static class LineAsyncTask extends AsyncTask<String, Integer, String> {
        ResultListener resultListener;

        public LineAsyncTask(ResultListener resultListener) {
            this.resultListener = resultListener;
        }

        @Override
        protected String doInBackground(String... string) {
            return request(string[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (resultListener != null) {
                resultListener.onResult(result);
            }
        }
    }

    public interface ResultListener {
        void onResult(String result);
    }
}
