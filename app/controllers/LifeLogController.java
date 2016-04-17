package controllers;

import models.LifeLog;
import play.data.Form;
import play.mvc.Result;
import views.html.editLog;
import views.html.index;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created on 2016/04/06.
 */
public class LifeLogController extends Apps {

	// ルートにアクセスしたときのAction
	public Result index() {
		// 年月の取得
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
		String yearMonth = sf.format(new Date());
		return redirect("/" + yearMonth);
	}

	// 年月毎の表示
	public Result displayIndex(String yearMonth) {
		// 不正な引数のチェック
		if (yearMonth.length() != 6) {
			flash("error", "ERROR:不正なURLです。");
			return badRequest(index.render("ERROR:不正なURLです。", null, null, null));
		}
		String year = yearMonth.substring(0,4);

		List<LifeLog> datas = null;
		try {
			datas = LifeLog.getMonthRecord(yearMonth);
		} catch (ParseException e) {
			flash("error", "ERROR:一覧が取得できません。");
			return badRequest(index.render("ERROR:一覧が取得できません。", datas, null, null));
		}
		return ok(index.render(yearMonth, datas, getMonthlySummary(yearMonth), getYearlySummary(year)));
	}

	// 新規作成時のAction
	public Result displayNew() {
		Form<LifeLog> f = Form.form(LifeLog.class);
		return ok(editLog.render("入力して下さい", f));
	}

	// 入力をSubmitしたときのAction
	// id未採番の場合はCREATE、採番済の場合はUPDATEとする
	public Result edit() {
		Form<LifeLog> f = Form.form(LifeLog.class).bindFromRequest();
		if (!f.hasErrors()) {
			LifeLog data = f.get();

			// デフォルト値設定
			data.setDefaultValue();

			if (data.id != null) {
				data.update();
				flash("success", "更新しました。");
			} else {
				// 日付の重複はエラー：全件検索？もっと楽な方法あるはず
				List<LifeLog> logList = LifeLog.find.all();
				for (LifeLog log : logList) {
					if (log.logDate.compareTo(data.logDate) == 0) {
						flash("error", "ERROR:登録済の日付です。");
						return badRequest(editLog.render("ERROR:登録済の日付です", f));
					}
				}
				data.save();
				flash("success", "登録しました。");
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
			String yearMonth = sf.format(new Date());
			return redirect("/" + yearMonth);
		} else {
			return badRequest(editLog.render("ERROR", f));
		}
	}

	// 編集をSubmitしたときのAction
	public Result displayEdit(Long id) {
		LifeLog data = LifeLog.find.byId(id);
		if (data != null) {
			Form<LifeLog> f = Form.form(LifeLog.class).fill(data);
			return ok(editLog.render("編集して下さい", f));
		} else {
			return ok(index.render("ERROR:見つかりません", null, null, null));
		}
	}

	/**
	 * 月別の集計を返却する.
	 * TODO 別クラスに外出しすべき？
	 * @param yearMonth
	 * @return
	 */
	public LifeLog getMonthlySummary(String yearMonth) {
		List<LifeLog> datas = null;
		try {
			datas = LifeLog.getMonthRecord(yearMonth);
		} catch (ParseException e) {
			return null;
		}

		if (datas.size() == 0) {
			return null;
		}

		return getSummary(datas);
	}

	/**
	 * 年別の集計を返却する.
	 * TODO 別クラスに外出しすべき？
	 * @param year
	 * @return
	 */
	public LifeLog getYearlySummary(String year) {

		List<LifeLog> datas = null;
		try {
			datas = LifeLog.getYearRecord(year);
		} catch (ParseException e) {
			return null;
		}

		if (datas.size() == 0) {
			return null;
		}

		return getSummary(datas);
	}
	// 集計結果を返す
	private LifeLog getSummary(List<LifeLog> resource) {
		// 集計用インスタンス
		LifeLog monSum = new LifeLog();
		// 平均値計算用カウンタ
		int sleepCount = 0;
		int wakeUpCount = 0;
		int leaveCount = 0;

		// 単純に合計
		for (LifeLog data : resource) {
			if (isCalcuratable(data.sleepHour)) {
				monSum.sleepHour += data.sleepHour;
				monSum.sleepMin += data.sleepMin;
				sleepCount += 1;
			}
			if (isCalcuratable(data.wakeUpHour)) {
				monSum.wakeUpHour += data.wakeUpHour;
				monSum.wakeUpMin += data.wakeUpMin;
				wakeUpCount += 1;
			}
			if (isCalcuratable(data.leaveHour)) {
				monSum.leaveHour += data.leaveHour;
				monSum.leaveMin += data.leaveMin;
				leaveCount += 1;
			}
			monSum.walkCount += data.walkCount;
			monSum.runDistance = monSum.runDistance.add(data.runDistance);
			monSum.readCount += data.readCount;
			monSum.techReadCount += data.techReadCount;
			monSum.bizReadCount += data.bizReadCount;
			monSum.techStudyTime += data.techStudyTime;
			monSum.englishStudyTime += data.englishStudyTime;
		}
		// サマリ表示用に再計算
		// 睡眠・起床・退社時間：平均値
		if (sleepCount != 0) {
			Long avSlMin = getMin(monSum.sleepHour, monSum.sleepMin) / sleepCount;
			monSum.sleepHour = avSlMin / 60;
			monSum.sleepMin = avSlMin % 60;
		}
		if (wakeUpCount != 0) {
			Long avWkMin = getMin(monSum.wakeUpHour, monSum.wakeUpMin) / wakeUpCount;
			monSum.wakeUpHour = avWkMin / 60;
			monSum.wakeUpMin = avWkMin % 60;
		}
		if (leaveCount != 0) {
			Long avLvMin = getMin(monSum.leaveHour, monSum.leaveMin) / leaveCount;
			monSum.leaveHour = avLvMin / 60;
			monSum.leaveMin = avLvMin % 60;
		}
		return monSum;
	}

	// 渡された時、分を分に変換して返す
	private Long getMin(Long hour, Long min) {
		return hour * 60L + min;
	}

	// 計算対象かどうかの判定
	private boolean isCalcuratable(Object val) {
		if (val == null) {
			return false;
		}
		return true;
	}
}
