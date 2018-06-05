package com.example.hbz.businquiry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hbz.businquiry.BusUtil.BusUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_test;
    private Button btn_test;

    private BusUtil.ResultListener listener = new BusUtil.ResultListener() {
        @Override
        public void onResult(String result) {
            tv_test.setText(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv_test = (TextView) findViewById(R.id.tv_test);
        btn_test = (Button) findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test:
                // 线路查询
//                String city = "";
//                int cityid = 382;// 杭州
//                String transitno = "86";
//                BusUtil.requestData(BusUtil.Type.LINE, listener, city, cityid + "", transitno);

                // 换乘查询
//                String city = "杭州";// utf-8
//                String start = "西溪竞舟苑";// utf-8
//                String end = "杭州汽车北站";// utf-8
//                String type = "";
//                BusUtil.requestData(BusUtil.Type.STATION2S, listener, city, start, end, type);

                // 站点查询
//                int cityid = 382;// 杭州
//                String station = "西溪竞舟苑";// utf-8
//                BusUtil.requestData(BusUtil.Type.STATION, listener, "", cityid + "", station);

                // 附近站点查询
//                String city = "杭州";// utf-8
//                String address = "西溪竞舟苑";// utf-8
//                BusUtil.requestData(BusUtil.Type.NEARBY, listener, city, address);

               // 城市查询
                BusUtil.requestData(BusUtil.Type.CITY, listener);
                break;
        }
    }
}