package edu.neu.madcourse.deborahho.finalproject;

import edu.neu.madcourse.deborahho.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CheckOutSchedule extends Activity implements OnClickListener{
	
	TextView day01;
	TextView day02;
	TextView day03;
	TextView day04;
	TextView day05;
	TextView day06;
	TextView day07;
	TextView day08;
	TextView day09;
	TextView day10;
	TextView day11;
	TextView day12;
	TextView day13;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalproject_check_out_schedule);
		
		day01 = (TextView) findViewById(R.id.schedule_day_1);
		day02 = (TextView) findViewById(R.id.schedule_day_2);
		day03 = (TextView) findViewById(R.id.schedule_day_3);
		day04 = (TextView) findViewById(R.id.schedule_day_4);
		day05 = (TextView) findViewById(R.id.schedule_day_5);
		day06 = (TextView) findViewById(R.id.schedule_day_6);
		day07 = (TextView) findViewById(R.id.schedule_day_7);
		day08 = (TextView) findViewById(R.id.schedule_day_8);
		day09 = (TextView) findViewById(R.id.schedule_day_9);
		day10 = (TextView) findViewById(R.id.schedule_day_10);
		day11 = (TextView) findViewById(R.id.schedule_day_11);
		day12 = (TextView) findViewById(R.id.schedule_day_12);
		day13 = (TextView) findViewById(R.id.schedule_day_13);
		
		day01.append("Day 01");
		day02.append("Day 02");
		day03.append("Day 03");	
		day04.append("Day 04");
		day05.append("Day 05");
		day06.append("Day 06");
		day07.append("Day 07");
		day08.append("Day 08");
		day09.append("Day 09");
		day10.append("Day 10");
		day11.append("Day 11");
		day12.append("Day 12");
		day13.append("Day 13");

        View backButton = findViewById(R.id.finalproject_back_button);
        backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finalproject_back_button:
			finish();
			break;
		}
	}
}
