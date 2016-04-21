package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created on 2016/04/06.
 */
@Entity
public class LifeLog extends Model {

	/* キー */
	@Id
	public Long id;

	/* 記録日付 */
	@Constraints.Required
	//@Formats.DateTime(pattern="yyyy-MM-dd")
	public Date logDate;

	/* 就寝時刻:時 */
	public Long sleepHour;

	/* 就寝時刻:分 */
	public Long sleepMin;

	/* 起床時刻:時 */
	public Long wakeUpHour;

	/* 起床時刻:分 */
	public Long wakeUpMin;

	/* 退社時刻:時 */
	public Long leaveHour;

	/* 退社時刻:分 */
	public Long leaveMin;

	/* 歩数 */
	@Column(columnDefinition = "bigint(10) default 0")
	public Long walkCount;

	/* RUN */
	@Column(columnDefinition = "decimal(7,2) default 0.00")
	public BigDecimal runDistance;

	/* 読書冊数 */
	@Column(columnDefinition = "bigint(3) default 0")
	public Long readCount;

	/* 技術書読書冊数 */
	@Column(columnDefinition = "bigint(3) default 0")
	public Long techReadCount;

	/* ビジネス書読書冊数 */
	@Column(columnDefinition = "bigint(3) default 0")
	public Long bizReadCount;

	/* 技術勉強時間:分 */
	@Column(columnDefinition = "bigint(5) default 0")
	public Long techStudyTime;

	/* 英語勉強時間:分 */
	@Column(columnDefinition = "bigint(5) default 0")
	public Long englishStudyTime;

	/* 作成日 */
	@CreatedTimestamp
	public Date createDate;

	/* 更新日 */
	@UpdatedTimestamp
	public Date updateDate;

	/* constructor */
	public LifeLog() {
		this.sleepHour = 0L;
		this.sleepMin = 0L;
		this.wakeUpHour = 0L;
		this.wakeUpMin = 0L;
		this.leaveHour = 0L;
		this.leaveMin = 0L;
		this.walkCount = 0L;
		this.runDistance = BigDecimal.ZERO;
		this.readCount = 0L;
		this.techReadCount = 0L;
		this.bizReadCount = 0L;
		this.techStudyTime = 0L;
		this.englishStudyTime = 0L;
	}

	/* Finder */
	public static Find<Long, LifeLog> find = new Find<Long, LifeLog>(){};

	/* 指定した年月日を含む週のレコードを取得 */
	public static List<LifeLog> getWeekRecord(String yearMonthDay) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();

		Date day = df.parse(yearMonthDay);
		cal.setTime(day);

		// 日曜日の日付
		cal.add(Calendar.DATE, -cal.get(Calendar.DAY_OF_WEEK) + 1);
		Date sunday = cal.getTime();
		// 土曜日の日付
		cal.add(Calendar.DATE, 6);
		Date saturday = cal.getTime();

		return LifeLog.find.where().between("logDate", sunday, saturday).setOrderBy("logDate").findList();

	}

	/* 指定した年月のレコードを取得 */
	public static List<LifeLog> getMonthRecord(String yearMonth) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyyMM");
		Calendar cal = Calendar.getInstance();

		String thisMonth = yearMonth;
		Date dThisMonth = df.parse(thisMonth);
		cal.setTime(dThisMonth);
		// 月末日(翌月初日の前日)
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		Date lastDate = cal.getTime();
		return LifeLog.find.where().between("logDate", dThisMonth, lastDate).setOrderBy("logDate").findList();
	}

	/* 指定した年のレコードを取得 */
	public static List<LifeLog> getYearRecord(String year) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy");
		Calendar cal = Calendar.getInstance();

		String thisYear = year;
		Date dThisYear = df.parse(thisYear);
		cal.setTime(dThisYear);
		// 年末日(翌年初日の前日)
		cal.add(Calendar.YEAR, 1);
		cal.add(Calendar.DATE, -1);
		Date lastDate = cal.getTime();
		return LifeLog.find.where().between("logDate", dThisYear, lastDate).setOrderBy("logDate").findList();
	}

	/* 入力がなかった場合のデフォルト値設定 */
	public void setDefaultValue() {
		if (isNull(this.walkCount)) {
			this.walkCount = 0L;
		}
		if (isNull(this.readCount)) {
			this.readCount = 0L;
		}
		if (isNull(this.techReadCount)) {
			this.techReadCount = 0L;
		}
		if (isNull(this.bizReadCount)) {
			this.bizReadCount = 0L;
		}
		if (isNull(this.techStudyTime)) {
			this.techStudyTime = 0L;
		}
		if (isNull(this.englishStudyTime)) {
			this.englishStudyTime = 0L;
		}
	}

	private boolean isNull(Long val) {
		if (val == null) {
			return true;
		}
		return false;
	}

	/* 時刻を時：分形式で表示 */
	public String toHourMin(Long numH, Long numM) {
		if (numH == null && numM == null) {
			return null;
		} else {
			return numH + ":" + addZero(numM);
		}
	}

	/* 分数を時：分形式で表示 */
	public String toHourMin(Long num) {
		if (num == null) {
			return null;
		}
		Long hour = num / 60;
		Long min = num % 60;
		return hour.toString() + ":" + addZero(min);
	}

	/* ゼロ埋めする */
	private static String addZero(Long num) {
		if (num < 10) {
			return "0" + num;
		} else {
			return num.toString();
		}
	}
}
